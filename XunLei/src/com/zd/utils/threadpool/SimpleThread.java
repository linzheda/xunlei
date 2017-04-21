package com.zd.utils.threadpool;

import java.math.BigDecimal;

public class SimpleThread extends Thread{
	private int threadNumber;//线程编号
	private boolean runningFlag; //当前线程运行状态true，false
	private boolean exit=true; //线程收缩
	private Runnable task; //要执行的操作


	public synchronized  void setExit(boolean runningFlag){
		this.exit=runningFlag;  //true
		//资源的问题，当前线程running后，要通知其它的线程，要通知其他的线程可能进入了阻塞状态->notify
		if(this.exit){
			this.notifyAll();
		}
	}
	
	
	public int getThreadNumber() {
		return this.threadNumber;
	}
	
	public boolean isRunning(){
		return this.runningFlag;
	}
	//关键方法，通过这个方法来唤醒线程
	public synchronized  void setRunning(boolean runningFlag){
		this.runningFlag=runningFlag;  //true
		//资源的问题，当前线程running后，要通知其它的线程，要通知其他的线程可能进入了阻塞状态->notify
		if(this.runningFlag){
			this.notifyAll();
		}
	}
	
	public Runnable getTask(){
		return this.task;
	}
	
	public void setTask(Runnable task){ //得到一个任务
		this.task=task;
	}
	
	public SimpleThread(int threadNumber){
		this.threadNumber=threadNumber;
		this.runningFlag=false;
		System.out.println("线程："+this.threadNumber+"创建...");
	}
	
	public synchronized void run(){
		try {
			while(exit){
				if(!runningFlag){ //false
					//当前线程还不能启动，则让当前线程进入wait()状态，这样不用耗费cpu
					this.wait();
				}else{
					//当前线程要运行起来
					System.out.println("线程："+this.threadNumber+"开始执行任务....");
					
					if(this.task!=null){
						this.task.run();//  xxxx => jvm调用Runnable中的run()
					}
					//当前任务task运行完毕......
					Thread.sleep(1000);//当前这个线程刚刚运行完，则休息一下
					System.out.println("线程："+threadNumber+",重新进行等待状态......等待下一个调度");
					this.setRunning(false);
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
}
