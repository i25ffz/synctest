

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TestMutexMulti {  

    private static long tn = 300000;
    private static boolean isLoaded = false;
    
	static {
		try {
			System.loadLibrary("mutex_multi");
			isLoaded = true;
		} catch(Error e) {
			e.printStackTrace();
			isLoaded = false;
		}
	}    
    
    /** 
     * @param args 
     */  
    public static void main(String[] args) {
    	if (args.length >= 1)
    		tn = Long.valueOf(args[0]);
    	
    	if (isLoaded) {
    	    ExecutorService pool = Executors.newFixedThreadPool(50); 
    		
    	    long time = System.currentTimeMillis();
            for (int i = 0; i < tn; i++) {
                pool.execute(new Runnable() {  
                    @Override  
                    public void run() {  
                        try {
                        	new TestMutexMulti().addCount();
                        } catch (Exception e) {  
                            e.printStackTrace();  
                        } catch (Error e) {  
                            e.printStackTrace();  
                        } 
                    }  
                });  
            }
            
            pool.shutdown();
            
            while (true) {
            	if (pool.isTerminated() || new TestMutexMulti().getCount() == tn)
            		break;
            }

            System.out.println("mutex(m) count:" + new TestMutexMulti().getCount());
            System.out.println("mutex(m) cost," + (System.currentTimeMillis() - time));
    	} else
    		System.out.println("load lib error!");
    }  
  
    public TestMutexMulti() {  
        //pool = Executors.newFixedThreadPool(50);  
    }  
        
    private native void addCount();
    private native long getCount();
}