package com.zd.utils.threadpool;

import java.math.BigDecimal;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

public class ThreadPoolManager {
	private Vector<SimpleThread> vector; //存线程的集合
	private int initialsize=10; //线程池的初始大小
	private int number;
	private final float critical=(float) 0.35; //阈值（临界值）
	
	public void setNumber(int number) {
		this.number = number;
	}

	public void setInitialSize( int initialSize){
		this.initialsize=initialSize;
	}
	
	public ThreadPoolManager(int initialSize){
		this.initialsize=initialSize;
		vector=new Vector<SimpleThread>();
		for(int i=0;i<this.initialsize;i++){
			SimpleThread st=new SimpleThread( i+1 );
			vector.add(st);
			//启动线程
			st.start();
		}
		
		new Timer().schedule(new TimerTask(){ //计时器
			float num;
			@Override
			public void run() {
				num=(float)(number+1)/initialsize;
				BigDecimal   b=new BigDecimal(num);
				double  f1=  b.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();//保留两位小数
				if(f1>=critical){
					for(int j=initialsize-1;j>0;j--){
						SimpleThread st=vector.get(j);
						if(st.isRunning()==false){
							st.setExit(false);

						}
					}
				}
			}
		}, 1000);
	}
	
	//调度线程池中线程的方法
	public void process( Runnable task ){
		//循环Vector所有的线程，找其中runningflag为false
		int i;
		
		for(i=0;i<vector.size();i++){
			SimpleThread st=vector.get(i);
			if(st.isRunning()==false){
				//找到后，调用这个线程的setTask(task)
				//  调用这个线程的setRunning(true)
				System.out.println("线程"+st.getThreadNumber()+"开始执行新任务了.....");
				number++;
				st.setTask(task);
				st.setRunning(true);
				
				return;
			}
		}
		
		//扩展线程池的代码
		System.out.println("============================");
		System.out.println("当前线程池大小为:"+this.initialsize+",已经没有空闲线程了，自动扩容程序开始运行...");
		System.out.println("=============================");
		if( i==vector.size()){
			int temp=i+10;
			this.initialsize=temp;
			for(;i<temp;i++){
				SimpleThread st=new SimpleThread( i+1 );
				vector.add(st);
				//启动线程
				st.start();
				
			}
			this.process(task);
		} 
	}
		
}
