/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.util.Random;

/**
 *
 * @author root
 */
public class Utils {

    public static int getRandomBit() {
        Random randomGenerator = new Random(System.currentTimeMillis());
        return randomGenerator.nextInt(2);
    }

    public static char getRandomBase() {
        int randBit = getRandomBit();
        switch (randBit) {
            case 0:
                return 'x';
            case 1:
            default:
                return '+';
        }
    }

    public static enum States {

        TransmissionStart,
        TransmissionStarted,
        TransmissionEnded,
        TransmissionEnd,

        AnnouncementStart,
        AnnouncementStarted,
        AnnouncementEnded,
        AnnouncementEnd,

        Detected,
        NotDetected,

        None,
    }
}
