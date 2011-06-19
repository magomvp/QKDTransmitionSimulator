/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.io.Serializable;

/**
 *
 * @author root
 */
public class Photon implements Serializable {
    private String _polarization;
    public Photon(int val,char base)
    {
        switch (base)
        {
            case '+':
                if(val==0) _polarization="-";
                else _polarization="|";
            case 'x':
                if(val==0) _polarization="\\";
                else _polarization="/";
        }
    }
    public int getValue(char base)
    {
        if(base == 'x' && _polarization.equals("\\"))
        {
            return 0;
        }
        
        if(base == 'x' && _polarization.equals("/"))
        {
            return 1;
        }
        
        if(base == '+' && _polarization.equals("-"))
        {
            return 0;
        }
        
        if(base == '+' && _polarization.equals("|"))
        {
            return 1;
        }
        
        return Utils.getRandomBit();
    }
}