package BB84Protocol;

import Model.Photon;
import Model.Utils;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 *
 * @author root
 */
public class BB84Protocol extends Thread {

    private Socket _socket;
    private Utils.States _state;
    private boolean _isInitiator;
    private QuantumTransmitedDataManager _dataManager;
    private Photon _lastPhoton;
    private int bitNumber=0;
    private int keyLength=10;

    ObjectInputStream _ois;
    ObjectOutputStream _oos;

    public BB84Protocol(Socket s, boolean isInitiator) {
        try {
            _socket = s;
            _isInitiator = isInitiator;
            _oos = new ObjectOutputStream(_socket.getOutputStream());
            _ois = new ObjectInputStream(_socket.getInputStream());
        } catch (IOException ex) {
            System.err.println(ex.getLocalizedMessage());
            return;
        }

        _state = Utils.States.None;
        _dataManager = new QuantumTransmitedDataManager();

        this.start();
    }

    private void sendMessage(Object obj) {
        try {
            _oos.writeObject(obj);
        } catch (IOException ex) {
            System.err.println(ex.getLocalizedMessage());
        }
    }

        private void initiatorBehaviour(Object dataPacket) {

        if (dataPacket instanceof Utils.States)
            {
                _state = (Utils.States) dataPacket;
            }

        switch(_state)
        {
            case None:
            {
                _state= Utils.States.TransmissionStart;
                sendMessage(Utils.States.TransmissionStart);
                break;
            }
            case TransmissionStart:
            {
                break;
            }
            case TransmissionStarted:
            {
                _lastPhoton=_dataManager.createAndSavePhoton();
                sendMessage(_lastPhoton);
                bitNumber++;
                break;
            }
            case TransmissionEnd:
            {

                break;
            }
            case TransmissionEnded:
            {
                sendMessage(Utils.States.AnnouncementStart);
                break;
            }
            case Detected:
            {

                if(bitNumber==keyLength)
                {
                    sendMessage(Utils.States.TransmissionEnd);
                    break;
                }
                _lastPhoton=_dataManager.createAndSavePhoton();
                sendMessage(_lastPhoton);
                bitNumber++;
                break;
            }
            case NotDetected:
            {
                sendMessage(_lastPhoton);
                break;
            }

        }
    }

    private void receiverBehaviour(Object dataPacket) {

        if (dataPacket == null) {
            sendMessage(Utils.States.NotDetected);
        }

        if (dataPacket instanceof Utils.States) {
            _state = (Utils.States) dataPacket;

            if(_state.equals(Utils.States.Detected) || _state.equals(Utils.States.NotDetected)) {
                _state = Utils.States.TransmissionStarted;
            }
        }

        switch (_state) {

            case None: {
                break;
            }
            case TransmissionStart: {

                _state = Utils.States.TransmissionStarted;
                sendMessage(_state);

                break;
            }
            case TransmissionStarted: {

                if (dataPacket instanceof Photon) {
                    _dataManager.savePhoton((Photon) dataPacket);
                    sendMessage(Utils.States.Detected);
                }
                break;

            }
            case TransmissionEnd: {

                _state = Utils.States.TransmissionEnded;
                sendMessage(_state);

                break;
            }
            case TransmissionEnded: {
                break;
            }
            case AnnouncementStart: {
                break;
            }
            case AnnouncementStarted: {
                break;
            }
            case AnnouncementEnd: {
                break;
            }
            case AnnouncementEnded: {
                break;
            }
            default: {
                break;
            }
        }
    }

    @Override
    public void run() {
        if (_isInitiator) {
            initiatorBehaviour(_state);
        }

        try {
            while (true) {

                Object receivedObject = _ois.readObject();

                if (!_isInitiator) {
                    receiverBehaviour(receivedObject);
                    continue;
                }

                initiatorBehaviour(receivedObject);
                //TO DO protocol message and object proccesor
            }

        } catch (ClassNotFoundException ex) {
            System.err.println("ObjectInput error: \n" + ex.getLocalizedMessage());
        } catch (IOException e) {
            System.err.println("IO Exception");
        } finally {
            try {
                _ois.close();
                _oos.close();

                System.out.println("closing...");
            } catch (IOException ex) {
                System.err.println("Ooops Error :" + ex.getLocalizedMessage());
            }

            this.interrupt();
        }
    }
}
