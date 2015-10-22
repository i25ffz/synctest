

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TestVolatile {
    private volatile long count = 0;  
    private ExecutorService pool;
    private static long tn = 300000;
  
    /** 
     * @param args 
     */  
    public static void main(String[] args) {
    	if (args.length >= 1)
    		tn = Long.valueOf(args[0]);
    	
        new TestVolatile().countThread();  
    }  
  
    public TestVolatile() {  
        pool = Executors.newFixedThreadPool(50);  
    }  
  
    public void countThread() {
    	long time = System.currentTimeMillis();
        for (int i = 0; i < tn; i++) {
            pool.execute(new Runnable() {  
                @Override  
                public void run() {    
                    try {
                        count++;
                    } catch (Exception e) {  
                        e.printStackTrace();  
                    } 
                }  
            });  
        }
        
        pool.shutdown();
        
        while (true) {
        	if (pool.isTerminated() || count == tn)
        		break;
        }

        System.out.println("volatile count:" + count);
        System.out.println("volatile cost," + (System.currentTimeMillis() - time));
    }  
}
