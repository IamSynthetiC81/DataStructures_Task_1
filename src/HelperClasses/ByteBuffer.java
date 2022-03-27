package HelperClasses;

import Interfaces.Convertable;

public class ByteBuffer {
    /** Specified max size of the buffer        |   4bytes      */
    private static int BUFFER_MAX_SIZE;
    /** Buffer                                  |4*length bytes */
    public byte[] buffer;
    /** Counter for the amount of elements     |   4bytes       */
    private int elementCount;
    /* Rough estimation of size in bytes : 1032 bytes ~ 1Kb    */
    

    public ByteBuffer(int size){
        BUFFER_MAX_SIZE = size;
        
        buffer = new byte[BUFFER_MAX_SIZE];
        
        elementCount = 0;
        
        fill((byte) -1 );
    }

    /**
     * Fills the buffer with the specified value
     * @param val Must be a byte value, allowed into the array.
     */
    private void fill(byte val){
        for(int i = 0 ; i < BUFFER_MAX_SIZE ; i++ ){
            buffer[i] = val;
        }
    }

    /**
     * Clears the buffer and sets all elements to -1
     *
     * Not 0, because 0 is a possible value.
     */
    public void Clear(){
        fill((byte) -1);
        elementCount = 0;
    }

    /**
     * Attaches the specified data to the buffer
     * @param data Data to be attached
     * @param <T> Must implement {@link Convertable}
     * @throws Exception Throws when the buffer is full.
     *          Must be used to write the page to the file,
     *          and load another one.
     * @return Returns 0 on success, and -1 when the data
     * specified are already present in the buffer.
     */
    public <T extends Convertable> int attachToBuffer(T data) throws IndexOutOfBoundsException {
        byte[] dataToByte = data.toByte();
        
        if(Contains(data))
            return -1;
        
        if(elementCount + dataToByte.length > BUFFER_MAX_SIZE)
            throw new IndexOutOfBoundsException("Buffer is full");
    
        System.arraycopy(dataToByte, 0, buffer, elementCount*dataToByte.length, dataToByte.length);
        
        elementCount++;
        
        return 0;
        
    }

    /**
     * Searches for the specified data and returns true if
     * the buffer contains that data.
     * @param data Data to be searched for.
     * @param <T> Must implement {@link Convertable}.
     * @return True if data is contained within the buffer, false if not.
     */
    public <T extends Convertable> boolean Contains(T data){
        byte[] dataToByte = data.toByte();
        
        for(int i = 0 ; i < BUFFER_MAX_SIZE ; i = i + dataToByte.length ){
            for(int j = 0 ; j < dataToByte.length ; j++ ){
                if(buffer[i + j] != dataToByte[j])
                    break;
                
                if(j == dataToByte.length - 1)
                    return true;
                
            }
        }
        return false;
    }
    
}
