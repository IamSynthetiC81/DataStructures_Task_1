package Tasks;

import HelperClasses.Page;
import HelperClasses.coordinates;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.sql.SQLOutput;
import java.time.Duration;
import java.time.Instant;
import java.util.LinkedList;


import static org.junit.jupiter.api.Assertions.*;

class B2Test {
    TaskB2 UUT;
    TaskB2_Extra UUT2;
    
    private final String Filename = "TaskB2Test.txt";
    private final int TableSize = 10;
    
    @BeforeEach
    void setUp(){
        UUT = new TaskB2(TableSize, Filename);
    }
    
    @AfterEach
    void Close(){
        UUT.Close();
    }

    @Test
    public void AppendAndQueryTest(){
        Instant Start = Instant.now();
        for(int i = 0 ; i < 100 ; i++ ){
            for(int j = 0 ; j < 100 ; j++ ){
                try{
                    assertNotEquals(-1, UUT.Append(new coordinates(i,j)),i + " , " + j);
                }catch (IOException ignored){
                
                }
            }
        }
        Instant Midway = Instant.now();
        for(int i = 0 ; i < 100 ; i++ ){
            for(int j = 0 ; j < 100 ; j++ ){
                try{
                    assertNotEquals(-1,UUT.Query(new coordinates(i,j)), i + " , " + j);
                }catch (IOException ignored){
                }
            }
        }
        Instant End = Instant.now();
    
        System.out.println(Duration.between(Start,Midway).toSecondsPart());
        System.out.println(Duration.between(Midway,End).toSeconds());
    }
    
    @Test
    public void WriteAndReadIndexArray() throws IOException {
        /*  Fill the UUT    */
        AppendAndQueryTest();
        
        UUT2 = new TaskB2_Extra(UUT,"TASKB2-ExtraTest.txt");
        LinkedList<LinkedList<Byte>> test;

        UUT2.Write();

        int[][] IndexArray = UUT2.getIndexArray();
    
        for (int i = 0 ; i < IndexArray.length ; i++ ) {
            Page pointer = UUT.Table[i].getHead();
            
            for (int j = 0; j < IndexArray[i].length; j++) {
                
                assertEquals(IndexArray[i][j],pointer.PageIndex, "Page index should be predicted by the IndexArray");
                
                if(j == IndexArray[i].length - 1) assertNull(pointer.NextPage);
                
                /*  Increment pointer   */
                pointer = pointer.NextPage;
            }
        }
    }
    
    @Test
    public void BuildTest() throws IOException{
//        UUT2 = new TaskB2_Extra(UUT,Filename);
        Instant Start = Instant.now();
        
        /*  Fills UUT and File              */
        for(int i = 0 ; i < 100000 ; i++ ){
                try{
                    assertNotEquals(-1, UUT.Append(new coordinates(i,i)),i + "ΓΕΗ");
                }catch (IOException ignored){
                }
            }

        
        Instant End = Instant.now();
    
        System.out.println("Time elapsed :"+ Duration.between(Start,End).toMinutesPart() + ":" + Duration.between(Start,End).toSecondsPart() +"s");
        
    }
}