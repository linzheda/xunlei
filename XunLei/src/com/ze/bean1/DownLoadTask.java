package com.ze.bean1;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;

import com.zd.utils.threadpool.MyNotify;

public class DownLoadTask implements Runnable {

	private URL url;//要下载的文件大小
	
	private File saveFile;//下载后保存的文件
	private long fileSizePerThread;//每个线程要下载的文件长度
	private int i;//线程编号
	
	private long start;
	private long end;
	private MyNotify myNotify;

	public DownLoadTask(URL url, File saveFile, long fileSizePerThread, int i , MyNotify myNotify) {
		this.url=url;
		this.saveFile=saveFile;
		this.fileSizePerThread=fileSizePerThread;
		this.i=i;
		
		this.start=i*this.fileSizePerThread;
		this.end=(i+1)*this.fileSizePerThread-1;
		this.myNotify=myNotify;
		
	}

	@Override
	public void run() {
		BufferedInputStream bis=null;
		RandomAccessFile raf=null;
		try {
			raf=new RandomAccessFile(saveFile,"rw");
			HttpURLConnection con=(HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			con.setRequestProperty("Range", "bytes="+this.start+"-"+this.end);
			raf.seek(this.start);//寻址下载
			
			bis=new BufferedInputStream(con.getInputStream());
			byte[] bs=new byte[10*1024];
			int length=0;
			while((length=bis.read(bs,0,bs.length))!=-1){
				raf.write(bs,0,length);
				if(this.myNotify!=null){
					
					synchronized( this){
					    this.myNotify.notifyResult(length);
					}
					
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (ProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(bis!=null){
				try {
					bis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(raf!=null){
				try {
					raf.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			
			
		}

	}

}
