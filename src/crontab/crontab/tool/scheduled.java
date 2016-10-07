package crontab.tool;
import java.util.Calendar;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cache.CacheFactory;
import com.sqllite.BlogUtil;

import thread.pool.commpools;

/**
 * 名称：OP_12580 - scheduled 
 * 描述： 
 * 作者：Kane 
 * 时间：2016年7月8日 上午6:41:32 
 * 备注：
 * 
 * @version
 */
public class scheduled {
	// slf4j
	private static final Logger log = LoggerFactory.getLogger(scheduled.class);

	public void task() {
		commpools.scheduled_pool.scheduleAtFixedRate(new Runnable() {// 每隔一段时间就触发异常
					public void run() {
						//思路：定期用非阻塞方式获取队列中的全部活动，封装为Map，获取不到则跳出循环
						HashMap<String, AtomicInteger> views_inc_map = new HashMap<String, AtomicInteger>();	
						HashMap<String, AtomicInteger> replies_inc_map = new HashMap<String, AtomicInteger>();	
						HashMap<String, AtomicInteger> likes_inc_map = new HashMap<String, AtomicInteger>();	
						while (true) {
							try {
								String msg=CacheFactory.basket.poll();
								//log.info("msg:"+msg);
								if(msg==null)
									break;
								JSONObject json =JSONObject.parseObject(msg);
								//log.info("views:"+json.getString("views"));
								
								//阅读数+1
								//{"views":"14"}
								String views_id=json.getString("views");
								if(views_id!=null)
								{
									AtomicInteger views_val = views_inc_map.get(views_id); 		
								    if (views_val != null) {
								    	views_val.incrementAndGet();
								    } else {
								    	views_inc_map.put(views_id, new AtomicInteger(1));
								    }
								}
							    //回复数+1
								//{"replies":"14"}
								String replies_id=json.getString("replies");
								if(replies_id!=null)
								{
									AtomicInteger replies_val = replies_inc_map.get(replies_id); 		
								    if (replies_val != null) {
								    	replies_val.incrementAndGet();
								    } else {
								    	replies_inc_map.put(replies_id, new AtomicInteger(1));
								    }
								}
							    
							    //点赞数+1
								//{"replies":"14"}
								String likes_id=json.getString("likes");
								if(likes_id!=null)
								{
									AtomicInteger likes_val = likes_inc_map.get(likes_id); 		
								    if (likes_val != null) {
								    	likes_val.incrementAndGet();
								    } else {
								    	likes_inc_map.put(likes_id, new AtomicInteger(1));
								    }
								}
							    							    
							} catch (Exception e) {
								log.error("crontab执行异常：" + e.getMessage());
								break;
							}
						}
						
						//打印json
						if(views_inc_map.size()>0)
						{
							log.info("views:"+JSON.toJSONString(views_inc_map));
							BlogUtil.getInstance().batch_update_active(views_inc_map,"views");
						}
						if(replies_inc_map.size()>0)
						{
							log.info("replies:"+JSON.toJSONString(replies_inc_map));
							BlogUtil.getInstance().batch_update_active(replies_inc_map,"replies");
						}
						if(likes_inc_map.size()>0)
						{
							log.info("likes:"+JSON.toJSONString(likes_inc_map));
							BlogUtil.getInstance().batch_update_active(likes_inc_map,"likes");
						}
					}
				}, 1000, 10*1000, TimeUnit.MILLISECONDS);// 表示延迟1秒后每10秒执行一次。
	}
	
}
