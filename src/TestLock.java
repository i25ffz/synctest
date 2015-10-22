

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TestLock {
    private long count = 0;  
    private ExecutorService pool;
    private Lock lock;  
    private static long tn = 300000;
    private boolean isFair;
  
    /** 
     * @param args 
     */  
    public static void main(String[] args) {
    	if (args.length >= 1)
    		tn = Long.valueOf(args[0]);
    	
    	if (args.length >= 2 && args[1].equals("-f"))
    		new TestLock(true).countThread();
    	else
    		new TestLock(false).countThread();  
    }  
  
    public TestLock(boolean isFair) {  
        pool = Executors.newFixedThreadPool(50);  
        lock = new ReentrantLock(isFair);
        this.isFair = isFair; 
    }  
  
    public void countThread() {
    	long time = System.currentTimeMillis();
        for (int i = 0; i < tn; i++) {
            pool.execute(new Runnable() {  
                @Override  
                public void run() {  
                    lock.lock();  
                    try {  
                        count++;  
                    } catch (Exception e) {  
                        e.printStackTrace();  
                    } finally {  
                        lock.unlock();  
                    }  
                }  
            });  
        }
        
        pool.shutdown();
        
        while (true) {
        	if (pool.isTerminated() || count == tn)
        		break;
        }
        
        System.out.println((isFair ? "lock(f)" : "lock") + " count:" + count);
        System.out.println((isFair ? "lock(f)" : "lock") + " cost," + (System.currentTimeMillis() - time));
    }  
}