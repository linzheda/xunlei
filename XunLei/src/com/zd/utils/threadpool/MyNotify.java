package com.zd.utils.threadpool;
/**
 * 回调接口
 * @author linzd
 *
 */
public interface MyNotify {
	/***
	 * 回调方法：当子线程完成操作，向主线程通信
	 */
	public void notifyResult(Object obj);
	
}
