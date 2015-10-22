

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

public class TestAtomic {
    private AtomicLong count = new AtomicLong();
    private ExecutorService pool;
    private static long tn = 300000;
  
    /** 
     * @param args 
     */  
    public static void main(String[] args) {
    	if (args.length >= 1)
    		tn = Long.valueOf(args[0]);
    	
        new TestAtomic().countThread();  
    }  
  
    public TestAtomic() {  
        pool = Executors.newFixedThreadPool(50);  
    }  
  
    public void countThread() {
    	long time = System.currentTimeMillis();
        for (int i = 0; i < tn; i++) {
            pool.execute(new Runnable() {  
                @Override  
                public void run() {    
                    try {  
                    	count.getAndIncrement(); 
                    } catch (Exception e) {  
                        e.printStackTrace();  
                    } 
                }  
            });  
        }
        
        pool.shutdown();
        
        while (true) {
        	if (pool.isTerminated() || count.get() == tn)
        		break;
        }

        System.out.println("atomic count:" + count);
        System.out.println("atomic cost," + (System.currentTimeMillis() - time));
    }  
}
