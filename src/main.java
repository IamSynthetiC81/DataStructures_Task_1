import HelperClasses.MyProgressBar;
import HelperClasses.coordinates;
import Tasks.TaskA1;
import Tasks.TaskA2;
import Tasks.TaskB1;
import Tasks.TaskB2;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.IntSummaryStatistics;
import java.util.Random;

import static java.lang.Math.pow;

public class main {
    
    static Random randomNumber = new Random();
    static int numberLimit = (int) pow(2,16);
    
    
    /** Number of data                                              */
    static int[] K = new int[]{1000, 10000, 30000, 50000, 70000, 100000};
    /** Table size                                                  */
    static int N = 500;
    /** Number of Queries to be executed                            */
    static int QueryNum = 100;
    
    static coordinates[] ElementsToSearch = new coordinates[QueryNum];
    
     public static void main(String[] args) throws IOException {
         MyProgressBar bar = new MyProgressBar(50);
         
        for(int k : K){
            
            Instant Start = Instant.now();
            
            int ElementCounter = 0;
            
            TaskA1 A1 = new TaskA1();
            TaskA2 A2 = new TaskA2(N);
            TaskB1 B1 = new TaskB1("TaskB2_Extra-B1.txt");
            TaskB2 B2 = new TaskB2(N, "TaskB2_Extra-B2.txt");
            
            /*  Filling the arrays with data                            */
            for(int i = 0 ; i < k ; i++) {
                /*  Creating a random coordinate                        */
                coordinates coord = new coordinates(randomNumber.nextInt(numberLimit), randomNumber.nextInt(numberLimit));
                
                /*  Adding the coordinates to the arrays                */
                Instant start = Instant.now();  /* Time record          */
                A1.insert(coord);
                Instant A1end = Instant.now();  /* Time record          */
                A2.add(coord);
                Instant A2end = Instant.now();  /* Time record          */
                B1.Append(coord);
                Instant A3end = Instant.now();  /* Time record          */
                B2.Append(coord);
                Instant A4end = Instant.now();  /* Time record          */
                
                /*  Calculating duration of each process                */
                long A1AppendTime = Duration.between(start,A1end).toMillis();
                long A2AppendTime = Duration.between(A1end,A2end).toMillis();
                long B1AppendTime = Duration.between(A2end,A3end).toMillis();
                long B2AppendTime = Duration.between(A3end,A4end).toMillis();
    
                long A1TotalTime =+ Duration.between(start,A1end).toMillis();
                long A2TotalTime =+ Duration.between(A1end,A2end).toMillis();
                long B1TotalTime =+ Duration.between(A2end,A3end).toMillis();
                long B2TotalTime =+ Duration.between(A3end,A4end).toMillis();
                
                /*  Saving the last 100 coordinates to search later    */
                if(randomNumber.nextBoolean() & ElementCounter < 100) {
                    ElementsToSearch[ElementCounter] = coord;
                    ElementCounter++;
                }
                
                Instant Elapsed = Instant.now();
    
                System.out.printf("Append | A1:%02dms, A2:%02dms, Β1:%02dms, Β2:%02dms | Elapsed : %02d:%02ds > ",A1AppendTime,A2AppendTime,B1AppendTime,B2AppendTime,Duration.between(Start,Elapsed).toMinutesPart(),Duration.between(Start,Elapsed).toSecondsPart());
                
                bar.update(i* 100L /k);
            }
            
            int[] A1SearchResults = new int[QueryNum];
            int[] A2SearchResults = new int[QueryNum];
            int[] B1SearchResults = new int[QueryNum];
            int[] B2SearchResults = new int[QueryNum];
    
            /*  Used for timekeeping                                    */
            long A1QueryTime;
            long A2QueryTime;
            long B1QueryTime;
            long B2QueryTime;
            
            for(int i = 0 ; i < QueryNum ; i++ ){
                Instant start = Instant.now();  /* Time record          */
                A1SearchResults[i] = A1.query(ElementsToSearch[i]);
                Instant A1END = Instant.now();  /* Time record          */
                A2SearchResults[i] = A2.query(ElementsToSearch[i]);
                Instant A2END = Instant.now();  /* Time record          */
                B1SearchResults[i] = B1.Query(ElementsToSearch[i]);
                Instant B1END = Instant.now();  /* Time record          */
                B2SearchResults[i] = B2.Query(ElementsToSearch[i]);
                Instant B2END = Instant.now();  /* Time record          */
    
                /*  Calculate instant Query duration times              */
                A1QueryTime = Duration.between(start,A1END).toMillis();
                A2QueryTime = Duration.between(A1END,A2END).toMillis();
                B1QueryTime = Duration.between(A2END,B1END).toMillis();
                B2QueryTime = Duration.between(B1END,B2END).toMillis();
                
                Instant Elapsed = Instant.now();
                
                /*  Progress bar                                         */
                
                System.out.printf("Query | A1:%02dms, A2:%02dms, Β1:%02dms, Β2:%02dms | Elapsed : %02d:%02ds > ",A1QueryTime,A2QueryTime,B1QueryTime,B2QueryTime,Duration.between(Start,Elapsed).toMinutesPart(),Duration.between(Start,Elapsed).toSecondsPart());
                bar.update(i* 100L /k);
                
                
            }
            
            Instant End = Instant.now();
    
            /*  Calculating statistics for each process                  */
            IntSummaryStatistics A1Stats = Arrays.stream(A1SearchResults).summaryStatistics();
            IntSummaryStatistics A2Stats = Arrays.stream(A2SearchResults).summaryStatistics();
            IntSummaryStatistics B1Stats = Arrays.stream(B1SearchResults).summaryStatistics();
            IntSummaryStatistics B2Stats = Arrays.stream(B2SearchResults).summaryStatistics();
            
            /*  Printout                                                 */
            System.out.println("The results for K = " + k + " are the following :");
            System.out.printf("A1 Average -> %7.1f | [Min:Max] : [%5d:%5d]\n", A1Stats.getAverage(),A1Stats.getMin(),A1Stats.getMax());
            System.out.printf("A2 Average -> %7.1f | [Min:Max] : [%5d:%5d]\n", A2Stats.getAverage(),A2Stats.getMin(),A2Stats.getMax());
            System.out.printf("B1 Average -> %7.1f | [Min:Max] : [%5d:%5d]\n", B1Stats.getAverage(),B1Stats.getMin(),B1Stats.getMax());
            System.out.printf("B2 Average -> %7.1f | [Min:Max] : [%5d:%5d]\n", B2Stats.getAverage(),B2Stats.getMin(),B2Stats.getMax());
            System.out.printf("Time elapsed = %02d:%02ds\n\n",Duration.between(Start,End).toMinutesPart(),Duration.between(Start,End).toSecondsPart());
            
            B1.Close(); /*  Deletes the file    */
            B2.Close(); /*  Deletes the file    */
        }
    }
}
