package crontab.tool;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class Tasks implements Runnable{
	private static final Log log = LogFactory.getLog(Tasks.class);

	private String className;

	public Tasks(String className) {
		this.className = className;
	}

	public void run() {
		try {
			Class clazz = Class.forName(className);
			ICrontab exec=(ICrontab)clazz.newInstance();
			exec.execute();
			log.info("任务调度："+className);
		} catch (Exception e) {
			log.error("任务调度："+className+" - 异常原因："+e.getMessage());
		}
	}

}
