package HelperClasses;

import Interfaces.Convertable;

import java.io.*;

public class Page {
    /** Size of the Page                    |   4bytes      */
    private final static int PageSize = 256;
    /** Location of the page into the file  |   4bytes     */
    public int PageIndex;
    /** Pointer to the NextPage in the list |   4bytes     */
    public Page NextPage;
    /** Filename for the file               |   2*Length    */
    private final String TargetFileName;
    /** Used for marking the page as full   |     1bit      */
    private boolean PageIsFull;
    /** Buffer for writing                  |   1032bytes   */
    private ByteBuffer WriteBuffer;
    /** Random Access File : Virtual Memory |  Unknown size */
    private RandomAccessFile RAF;
    /* Rough estimation of size in bytes : 1045+ bytes ~ 1Kb
       When the Page is full, the WriteBuffer is set to null,
       and is only an object reference, holding only 4 bytes.
       the size then drops to 25+ bytes                     */
    
    /**
     * This Page holds the following :
     * <ul>
     *    <li>A {@link ByteBuffer} which temporarily holds the data to be written to the file.
     *    <li>A {@link #NextPage } pointer to the next Page in the list.
     *    <li>A {@link #PageIndex} which guides the Reader and Writer to the location of the
     *          page in the file.
     *    <li>The {@link #PageSize} which indicates the size of each page.
     *    </ul>
     * @param FileName Name of the file where the data are going to be stored.
     * @param PageIndex Index of this specific page into the file.
     */
    public Page(String FileName, int PageIndex){
        /*  Assigning FileName                  */
        this.TargetFileName = FileName;
        
        /*  Assigning PageIndex                 */
        this.PageIndex = PageIndex;
        
        /*  Initializing NextPage               */
        this.NextPage = null;
        
        /*  Initializing PageIsFull             */
        this.PageIsFull = false;
        
        /*  Initializing WriteBuffer    `       */
        WriteBuffer = null;
    }
    
    /**
     * Opens the RAF
     * @throws FileNotFoundException Throws when the file was not found.
     */
    private void Open() throws IOException{
        try {
            RAF = new RandomAccessFile(TargetFileName, "rws");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Reads the data of the current Page.
     * @return Return a {@code byte[]} holding the data
     *          of tha Page.
     * @throws IOException Throws when an error occurs while reading.
     */
    public int Read(byte[] b) throws IOException {
        /*  new Buffer                          */
        byte[] buffer = new byte[PageSize];
        
        /* Opening RAF                          */
        Open();
        
        /*  Jumping to position                 */
        RAF.seek((long) PageIndex * PageSize);
        
        /*  Reading                             */
        RAF.read(buffer);
    
        /*  Closing RAF is essential            */
        RAF.close();
    
        /*  Prepping for deserialization        */
        ByteArrayInputStream bis = new ByteArrayInputStream(buffer);
        DataInputStream din = new DataInputStream(bis);
        
        /*  Deserializing to ReturningBuffer    */
        /*  Returned what was read              */
        return din.read(b);
    }

    /**
     * Writes the currently held data to the Page.
     * @throws IOException Throws when en error occurs while writing.
     */
    public void Write(byte[] b) throws IOException{
        /*  housekeeping                            */
        assert b.length <= PageSize & b.length > 0;
        
        /*  Open RAF                                */
        Open();
        
        /*  Start Serialization                     */
        ByteArrayOutputStream bos = new ByteArrayOutputStream() ;
        DataOutputStream out = new DataOutputStream(bos);
    
        /*  Serialize buffered data                 */
        out.write(b);
        
        /*  Close DataOutputStream                  */
        out.close();
        
        /*  Jump to data on Page                    */
        RAF.seek( (long) PageIndex * PageSize);
        
        /*  Write data on Page                      */
        RAF.write(bos.toByteArray());
        
        /*  Close ByteArrayOutputStream             */
        bos.close();
        
        /*  Closing the RAF is essential            */
        RAF.close();
    }

    /**
     * Searches through the page for the specified data.
     * <p>Firstly, the buffer is searched, if the data are found
     * this returns 0 since no {@link #Read(byte[])} ever occurred</p>
     * <p>If data are not contained withing the buffer, then the Page
     * is Read from the file, and searched. If found, this function
     * return 1</p>
     * <p>If the data are not found, this function return -1 </p>
     * @param data Data to be searched for.
     * @param <T> Must implement {@link Convertable}.
     * @return Returns the amount of {@link #Read(byte[]) Reads} required until the
     * location of the specified data. Returns -1 if data are
     * not present.
     * @throws IOException Throws when the file could not be read.
     */
    public <T extends Convertable> int Query(T data) throws IOException {
        /*  First search the buffer                 */
        if(!PageIsFull & WriteBuffer != null) {
            if (WriteBuffer.Contains(data))
                return 0;
        }
        
        ByteBuffer TempBuffer = new ByteBuffer(PageSize);
        TempBuffer.Clear();
        
        /*  Read the Page                           */
        Read(TempBuffer.buffer);
        
        /*  Then search the FilePage                */
        if(TempBuffer.Contains(data))
            return 1;
        
        /*  the specified dataset does not exist    */
        return -1;
    }

    public <T extends Convertable> int Append(T data) throws IOException {
        /* If Page is full, return -1               */
        if(PageIsFull)
            throw new EOFException();
        /*  If WriteBuffer is null, create on       */
        if(WriteBuffer == null)
            WriteBuffer = new ByteBuffer(PageSize);
        
        /* Is -1 if data already exist, 0 if not    */
        int PreExistentData;
        
        try {
            PreExistentData = WriteBuffer.attachToBuffer(data);
        } catch (IndexOutOfBoundsException e) {
            /* No more room in buffer               */
            
            /*  The buffer is written to the file   */
            Write(WriteBuffer.buffer);
            
            /* The Page is marked as full           */
            PageIsFull = true;
            
            /*  Close the buffer to save space      */
            WriteBuffer = null;
            
            /* Throws to indicate EndOfPage         */
            throw new EOFException();
        }
        
        return PreExistentData;
    }
    
}
