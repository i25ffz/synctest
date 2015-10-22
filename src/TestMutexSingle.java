

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TestMutexSingle {  
    private ExecutorService pool;
    private static long tn = 300000;
    private static boolean isLoaded = false;
    
	static {
		try {
			System.loadLibrary("mutex_single");
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
    		TestMutexSingle jni = new TestMutexSingle();
    		jni.countThread();
    	} else
    		System.out.println("load lib error!");
    }  
  
    public TestMutexSingle() {  
        pool = Executors.newFixedThreadPool(50);  
    }  
    
    public void countThread() {
    	long time = System.currentTimeMillis();
        for (int i = 0; i < tn; i++) {
            pool.execute(new Runnable() {  
                @Override  
                public void run() {  
                    try {
                    	addCount();
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
        	if (pool.isTerminated() || getCount() == tn)
        		break;
        }

        System.out.println("mutex(s) count:" + getCount());
        System.out.println("mutex(s) cost," + (System.currentTimeMillis() - time));
    }
    
    private native void addCount();
    private native long getCount();
}