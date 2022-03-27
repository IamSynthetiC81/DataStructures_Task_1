package Interfaces;

import java.io.*;

public interface FileOps {
    
    public byte[] Read() throws IOException;

    public void Write(byte [] b)throws IOException;

    public <T extends Convertable> int Append(T data) throws IOException;
}
