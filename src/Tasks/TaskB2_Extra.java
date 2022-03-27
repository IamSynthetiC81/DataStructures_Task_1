package Tasks;

import HelperClasses.Page;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.LinkedList;

public class TaskB2_Extra{
    
    TaskB2 target;
    
    String TargetFileName;
    
    RandomAccessFile RAF;
    
    public TaskB2_Extra(TaskB2 target, String Filename){
        this.target = target;
        TargetFileName = Filename;
    }
    
    private void Open() throws IOException {
        try {
            RAF = new RandomAccessFile(TargetFileName, "rws");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    public void Write() throws IOException {
        LinkedList<Byte> List = new LinkedList<>();
        
        /* For every element of the table, create the List
            with their indexes. Each new row is marked by a "-1"
         */
        for(int i = 0 ; i < target.Table.length ; i++ ){
            /*  Get HeadPage of the row                             */
            Page pointer = target.Table[i].getHead();
            
            /*  Goes through each element of the row                */
            while(pointer != null){
                /*  Adds every index of this row into the List      */
                List.add((byte) pointer.PageIndex);
                
                /*  Increment the pointer                           */
                pointer = pointer.NextPage;
            }
            
            /*  Mark the end of this row  by adding "-1"             */
            List.add((byte) -1);
        }
    
        /*  Copy data into a byte array             */
        byte[] buffer = new byte[List.size()];
        for(int i = 0 ; i < List.size() ; i++ ){
            buffer[i] = List.get(i);
        }
        
        /*  Start Serialization                     */
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(bos);
        
        /*  Serialize buffered data                 */
        out.write(buffer);

        /*  Close DataOutputStream                  */
        out.close();

        /* Open RAF                                 */
        Open();
        
        /*  Go to start of file                     */
        RAF.seek(0);
        
        /*  Write data on Page                      */
        RAF.write(bos.toByteArray());

        /*  Close ByteArrayOutputStream             */
        bos.close();

        /*  Closing the RAF is essential            */
        RAF.close();
    }
    
    private void Read(@NotNull LinkedList<Byte> IndexArray) throws IOException {
        int BYTES_PER_READ = 256;
        
        byte[] buffer = new byte[BYTES_PER_READ];
        byte[] b = new byte[BYTES_PER_READ];
        
        /* Opening RAF                                  */
        Open();
        int bytesRead;
        do {
            /*  Reading                                 */
            bytesRead = RAF.read(buffer);
    
            /*  Prepping for deserialization            */
            ByteArrayInputStream bis = new ByteArrayInputStream(buffer);
            DataInputStream din = new DataInputStream(bis);
    
            /*  Deserializing to b                      */
            din.read(b);
            
            /*  Adding what was read to the IndexArray  */
            for (byte value : b) {
                IndexArray.add(value);
            }
        }while(bytesRead != -1);
        
        
    }
    
    public int[][] getIndexArray() throws IOException {
        LinkedList<Byte> IndexList = new LinkedList<>();
        Read(IndexList);
        
        LinkedList<LinkedList<Byte>> IndexArray = new LinkedList<>();
        LinkedList<Byte> buffer = new LinkedList<>();
        for (Byte index : IndexList) {
            if(index != -1){
                /*  Search if that index is contained within the row    */
                if(buffer.contains(index))
                    break;
                /*  Search if that index is contained within the Array  */
                for(LinkedList<Byte> element : IndexArray)
                    if (element.contains(index))
                        break;
                /*  If it is contained a second time, the index must be a 0,
                    and we must have reached the EOF (End of File).
                 */
                
                buffer.add(index);
            }else{
                /*  Add data to original List           */
                IndexArray.add(buffer);
                /*  Clear buffer                        */
                buffer = new LinkedList<>();
            }
        }
        
        /*  Figure out dimensions of array              */
        int max = 0;
    
        for (LinkedList<Byte> bytes : IndexArray) {
            if (bytes.size() > max) max = bytes.size();
            
        }
        
        
        /*  Create array and fill it                    */
        int[][] Array = new int[IndexArray.size()][max];
        for (int i = 0 ; i < Array.length ; i++ ) {
            for(int j = 0 ; j < max ; j++ ){
                try{
                    int index = IndexArray.get(i).get(j);
                    Array[i][j] = index;
                }catch (IndexOutOfBoundsException ignore){
                    break;
                }
                
            }
        }
        
        return Array;
    }
    
    public boolean Close(){
        File file = new File(TargetFileName);
        
        return file.delete();
    }
    
}
