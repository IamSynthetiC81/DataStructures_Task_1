package Tasks;

import java.io.*;

import HelperClasses.ByteBuffer;
import Interfaces.Convertable;


public class TaskB1 {
    /** Size of each Page                                                               */
    private final int BUFFER_MAX_SIZE = 256;
    /** Means with which we access the {@link #filename File}                            */
    RandomAccessFile randomAccessFile;
    /** This buffer holds the data read from a page                                     */
    private final ByteBuffer RBuffer = new ByteBuffer(BUFFER_MAX_SIZE);
    /** The buffer holds the data of a page before writing it all into the file         */
    private final ByteBuffer WBuffer = new ByteBuffer(BUFFER_MAX_SIZE);
    
    /** Index of the Page we are reading or writing */
    int PageIndex = 0;
    /** Number of Pages in the file */
    int PageCount = 0;
    /** Database File               */
    String filename;
    /** File to open    */
    private File file;
    
    /**
     * TaskB1 - Writes data into a file using the Paging method.
     * @implNote The type of data must implement the interface
     * {@link Convertable}.
     * @param Filename The name of the file where data are
     *                 going to be saved into, and read from.
     */
    public TaskB1(String Filename) {

        this.filename = Filename;
        this.file = new File(Filename);
        
        try {
            randomAccessFile = new RandomAccessFile(Filename, "rws");
        } catch (FileNotFoundException e) {
            System.out.println("The file does not exist\n" +
                    "Creating a new one");
            e.printStackTrace();
        }
    }
    /**
     * Loads the data into the {@link #WBuffer WriterBuffer}.
     * - If the buffer is full, it is written into the file,
     *   then its cleared, and finally the data are stored
     *   into the new buffer.
     * @param data Data to be inserted.
     * @param <T> Must implement {@link Convertable}.
     */
    public <T extends Convertable> void Append(T data) throws IOException {
        try {
            WBuffer.attachToBuffer(data);
        } catch (Exception e) {
            /* No more space in the WriteBuffer,
                Clear the buffer,
                and finally save the data to the
                new and clean buffer.           */
            
            Write(); /*  We write the current buffer to file */
            
            WBuffer.Clear(); /*  We clear the buffer                 */
            
            Append(data); /* Retry                                */
            
        }
        
    }
    /**
     * Searches throughout the Pages of the file for the
     * specified data.
     * @param data Data to be searched for.
     * @param <T> Must implement {@link Convertable}.
     * @return Returns the amount of Reads from the disk.
     *         Returns -1 when data is not present.
     * @apiNote This does not search through the {@link #WBuffer WriterBuffer}.
     *          Therefore, data which could be <i>inserted</i> using the
     *          {@link #Append(Convertable)}, if the page is not filled,
     *          they will not be found by this function.
     */
    public <T extends Convertable> int Query(T data) throws IOException {
        
        PageIndex = 0;  /* Through this, we mark the first page
                           for reading,
                           Also, this counts as the counter for
                           the amount of reads performed        */
        
        if(WBuffer.Contains(data)){
            return 0;
        }
        
        int ReadBytes;
        do {
            ReadBytes = Read();             /*  Read page       */
            if (RBuffer.Contains(data)) {
                return PageIndex + 1;
            }
        }while(ReadBytes == BUFFER_MAX_SIZE);   /* If we read less than 256 bytes
                                                   it means we have reached EOF */
       
        return -1;
    }
    /**
     * Reads a Page from the {@link #filename File}.
     * After a Read call on a Page, the next call will
     * Read the next Page of the file.
     * @return Return the amount of Bytes that where read.
     */
    public int Read() throws IOException {
        int ReadBytes;

        /* Opening the reader   */
        randomAccessFile = new RandomAccessFile(filename, "rws");
        
        /* Jumping to position  */
        randomAccessFile.seek((long) PageIndex * BUFFER_MAX_SIZE);
        
        /* Reading Page         */
        ReadBytes = randomAccessFile.read(RBuffer.buffer);
        
        /* If we read 256 Bytes, that means we read a full Page, and
           that we can move the PageIndex up
         */
        PageIndex = PageIndex + ReadBytes/BUFFER_MAX_SIZE;

    
        /* Closing the Reader is essential  */
        randomAccessFile.close();
    
        return ReadBytes;
    }
    /**
     * Writes the {@link #WBuffer WriterBuffer} to the file.
     */
    public void Write() throws IOException {
        
        /* Opening the Writer    */
        randomAccessFile = new RandomAccessFile(filename, "rws");

        /* Jumping to position  */
        randomAccessFile.seek((long) PageCount * BUFFER_MAX_SIZE);
        
        /* Writing to Page      */
        randomAccessFile.write(WBuffer.buffer);

        /*  Closing the Writer is essential */
        randomAccessFile.close();
    
        /* We have added another page, therefore
           we must increment the Page counter
         */
        PageCount++;
    }
    
    /**
     * Deletes the file this task opened.
     * @return Returns True if successful, false if not.
     */
    public boolean Close(){
        return file.delete();
    }
    
}



