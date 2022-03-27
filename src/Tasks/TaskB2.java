package Tasks;

import HelperClasses.Element;
import HelperClasses.Page;
import HelperClasses.coordinates;
import Interfaces.Convertable;
import Interfaces.Paging;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;

public class TaskB2 implements Paging {
    private final int TableSize;
    
    public final Element<TaskB2>[] Table;
    
    public int PageCounter = -1;
    
    private final File file;
    
    
    /**
     * An array of {@link Element Elements} are created, and at this level,
     * all Queries, Reads or Writes are requested from these Elements, and
     * handled within the {@link Element Element Class}
     * @param TableSize Size of the {@link Element} array
     * @param FileName Name of the file where the data are going to be stored.
     */
    public TaskB2(int TableSize, String FileName){
    
        this.TableSize = TableSize;
        Table = new Element[TableSize];
        
        /*  Initialising Table                      */
        for(int i = 0 ; i < TableSize ; i++ )
            Table[i] = new Element<>(FileName, this);
        
        file = new File(FileName);
        
    }
    
    public <T extends Convertable> int hashCode(@NotNull T data) {
        /*  Data FOR NOW are coordinates    */
        coordinates coord = (coordinates) data;
        
        int hash = (int)(((long)coord.X * coord.Limit + coord.Y) % TableSize);
    
        if (hash < 0 || hash > TableSize)
            throw new IndexOutOfBoundsException("Invalid index");
    
        return hash;
    }
    
    public <T extends Convertable> int Append(@NotNull  T data) throws IOException {
        int Index = hashCode(data);
        
        return Table[Index].Append(data);
    }

    public <T extends Convertable> int Query(@NotNull T data) throws IOException {
        int Index = hashCode(data);
        
        return Table[Index].Query(data);
    }
    
    public void Build() throws IOException {
        /*  Housekeeping    */
        assert (file != null);
        
        TaskB2_Extra PageIndexStorageSolution = new TaskB2_Extra(this, file.getName());
        
        int[][] IndexArray = PageIndexStorageSolution.getIndexArray();
        
        for(int i = 0 ; i < IndexArray.length ; i++ ){
            /*  Pointer to the head of the specific element of the table    */
            Page pointer = Table[i].getHead();
            
            for(int j = 0 ; j < IndexArray[i].length - 1 ; j++ ){
                /*  Create new Page                                         */
                if(pointer.NextPage == null){
                    pointer.NextPage = new Page(file.getName(), IndexArray[i][j+1]);
                }
                
                /*  WTF IS THIS                                             */
                if(pointer.PageIndex != IndexArray[i][j])
                    throw new Error("I swear this has not happened before");
                
                /*  Increment pointer                                       */
                pointer = pointer.NextPage;
                
                /*  Tail fix                                                */
                Table[i].setTail(pointer);
                
            }
        }
        System.out.println("Built");
    }
    /**
     * Deletes the file this task opened.
     * @return Returns True if successfully, false if not.
     */
    public boolean Close(){
        return file.delete();
    }
    
    @Override
    public int getPageCounter(){
        return ++PageCounter;
    }
}
