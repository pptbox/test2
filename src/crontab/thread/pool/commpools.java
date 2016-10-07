package thread.pool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**    
 * 名称：OP_12580 - commpools    
 * 描述：
 * 作者：Kane    
 * 时间：2016年7月17日 下午2:52:52    
 * 备注：    
 * @version     
 */
public class commpools {
	// 创建一个可重用固定线程数的线程池
    public static ExecutorService fixed_pool = Executors.newFixedThreadPool(200);
    //创建一个计划任务线程池
    public static ScheduledThreadPoolExecutor scheduled_pool = new ScheduledThreadPoolExecutor(10);
    
}
