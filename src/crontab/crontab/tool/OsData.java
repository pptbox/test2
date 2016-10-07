package crontab.tool;

import com.sqllite.BlogUtil;

public class OsData implements ICrontab{
	/**
	 * @param args
	 */
	public void execute(){
		/*
		 
		 strftime('%Y-%m-%d %H:%M:%S', ...)  
		 
		 每天1:00执行此任务（昨天的数据->hour表）
		 insert into os_data_hour
		select source,action,max(info),time from os_data group by source,action,strftime('%Y-%m-%d %H',time) order by source,action,time

		每天1:00执行删除os_data今天之前的数据
		
		每天2:00执行此任务（昨天的数据->day表）
		 insert into os_data_day
		select source,action,max(info),time from os_data_hour group by source,action,strftime('%Y-%m-%d',time) order by source,action,time
		
		每天1:00执行删除os_data_hour三个月之前的数据
		
		 */
		BlogUtil.getInstance().task("insert into os_data_hour select source,action,max(info),time from os_data where time<datetime('now','localtime','start of day') group by source,action,strftime('%Y-%m-%d %H',time)");
		BlogUtil.getInstance().task("delete from os_data where time<datetime('now','start of day')");
		
		BlogUtil.getInstance().task("insert into os_data_day select source,action,max(info),time from os_data_hour where time<datetime('now','localtime','start of day') group by source,action,strftime('%Y-%m-%d',time)");
		BlogUtil.getInstance().task("delete from os_data_hour where time<datetime('now','localtime', 'start of month','-3 month');");

	}


}
