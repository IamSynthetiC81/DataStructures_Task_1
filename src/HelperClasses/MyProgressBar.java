package HelperClasses;

public class MyProgressBar {
    
    private final int Length;
    
    public MyProgressBar(int Length){
        this.Length = Length;
    }
    
    public void update(long progress){
        assert(progress >= 0 & progress <= 100);
        
        int barLength = (int) (progress*0.01 * Length);
        
        for(int i = 0 ; i < Length ; i++ ){
            if(i <= barLength) {
                System.out.print(":");
            }else {
                System.out.print(" ");
            }
        }
        
        System.out.printf("| %02d \r",progress);
        
    }
    
}
