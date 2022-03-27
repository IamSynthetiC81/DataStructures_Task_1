package Tasks;

import org.junit.Rule;
import org.junit.jupiter.api.*;
import org.junit.rules.TemporaryFolder;
import HelperClasses.coordinates;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNotEquals;


public class B1Test {
    
    TaskB1 UUT;
    
    @Rule
    TemporaryFolder temporaryFolder;
    
    @BeforeEach
    void SetUp() {
        UUT = new TaskB1("TaskB1Test.txt");
        
    }
    
    @Test
    public void InputTest() throws IOException {
        for (int i = 0; i < 17; i++) {
            for (int j = 0; j < 17; j++) {
                UUT.Append(new coordinates(i, j));
            }
        }
        
        UUT.Write();    // Write buffer to file regardless of whether is it full
        
        for (int i = 0; i < 17; i++) {
            for (int j = 0; j < 17; j++) {
                assertNotEquals(-1, UUT.Query(new coordinates(i, j)), i + " , " + j);
            }
        }
    }
    
}

