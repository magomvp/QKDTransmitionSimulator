/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package BB84Protocol;

import Model.Photon;
import Model.Utils;
import java.util.ArrayList;

/**
 *
 * @author root
 */
public class QuantumTransmitedDataManager {
    
    ArrayList<Integer> _values=new ArrayList<Integer>();
    ArrayList<Character> _bases=new ArrayList<Character>();
    
    public Photon createAndSavePhoton()
    {
        Character base = Utils.getRandomBase();
        _bases.add(base);
        Integer value = Utils.getRandomBit();
        _values.add(value);
        
        return new Photon(value,base);
    }
    public void savePhoton(Photon photon)
    {
        Character base = Utils.getRandomBase();
        _bases.add(base);
        Integer value= photon.getValue(base);
        _values.add(value);

    }
}
