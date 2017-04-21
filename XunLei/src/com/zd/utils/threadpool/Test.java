package com.zd.utils.threadpool;

import java.util.Date;

public class Test {
	public static void main(String[] args) {
		ThreadPoolManager tpm=new ThreadPoolManager(3);
		tpm.process(new TimeTask());
//		tpm.process(new CountTask( new MyNotify(){
//			@Override
//			public void notifyResult(Object obj) {
//				System.out.println(obj.toString());
//			}
//
//		}));
		for(int i=0;i<15;i++){
			tpm.process(new CountTask( new MyNotify(){
				@Override
				public void notifyResult(Object obj) {
					System.out.println(obj.toString());
				}

			}));
		}


	}
}
class TimeTask implements Runnable{

	@Override
	public void run() {
		while(true){
			Date d=new Date();
			System.out.println("线程："+Thread.currentThread().getName()+"输出时间："+d);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

}
class CountTask implements Runnable {
	private MyNotify myNotify;
	
	public CountTask(MyNotify myNotify) {
		this.myNotify=myNotify;
	
	}

	@Override
	public void run() {
		int i=10;
		while(true){
			myNotify.notifyResult(i);
			i--;
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if(i<=0){
				break;
			}
		}
	}

}