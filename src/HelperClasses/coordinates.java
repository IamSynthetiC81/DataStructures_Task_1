package HelperClasses;

import Interfaces.Convertable;

import java.security.InvalidParameterException;
import java.util.Objects;

import static java.lang.Math.pow;

public class coordinates implements Convertable {
    public int Limit = (int) pow(2, 18) - 1;
    
    public Integer X;
    public Integer Y;
    
    public coordinates(Integer x, Integer y) {
        if (x < 0 | y < 0)
            throw new IllegalArgumentException("Values must be positive");
        if (x > Limit | y > Limit)
            throw new IllegalArgumentException("Values must not exceed the number 2^18");
        
        this.X = x;
        this.Y = y;
    }
    
   
    /**
     * Normally an int is 4 bytes, so 32 bits, but since our coordinate limits
     * rage from 0 to 2^16, we only use half of them. So we use only 16 bits per
     * integer.
     *
     *  Must be implemented in the Parser as well.
     */
    @Override
    public byte[] toByte() {
        int NumberOfBits = 16;
        
        byte[] XB = new byte[NumberOfBits];
        byte[] YB = new byte[NumberOfBits];
        int ValueX;
        int ValueY;
        
        for(int i = 0 ; i < NumberOfBits ; i++ ){
            ValueX = X / (int) pow(2,i);
            ValueY = Y / (int) pow(2,i);
            
            if(ValueX % 2 == 1)
                XB[NumberOfBits - 1 - i] = 1;
            else
                XB[NumberOfBits - 1 - i] = 0;
            
            if(ValueY % 2 == 1)
                YB[NumberOfBits - 1 - i] = 1;
            else
                YB[NumberOfBits - 1 - i] = 0;
        }
        
        /* We create a space for both elements - 8 bytes */
        byte[] output = new byte[2 * NumberOfBits];
        
        /* Put both bytes into the output array          */
        for (int i = 0; i < XB.length; i++) {
            output[i] = XB[i];
        }
        for (int i = 0; i < YB.length; i++) {
            output[i + XB.length] = YB[i];
        }
        return output;
    }
    
    /**
     * This Override is essential, so we can easily
     * compare two coordinate elements.
     * @param o {@link coordinates} to compare.
     * @return Returns true if elements are equal, false if not.
     */
    @Override
    public boolean equals(Object o) {
        /*  HouseKeeping Code       */
        if(o.getClass() != this.getClass()) throw new InvalidParameterException();
        
        /*  Essential Typecasting   */
        coordinates data = (coordinates) o;
        
        return (Objects.equals(this.X, data.X)) & (Objects.equals(this.Y, data.Y));
    }
    
}

