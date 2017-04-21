package com.ze.bean1;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.zd.utils.threadpool.MyNotify;
import com.zd.utils.threadpool.ThreadPoolManager;
/**
 * 下载管理器
 * @author linzd
 *
 */
public class DownloadManager {
	private int threadSize;//线程数量
	private URL url;//要下载的文件大小
	private String saveDir;//保存目录
	private long fileTotalSize;//要下载的文件的总文件
	private File saveFile;//下载后保存的文件
	private long fileSizePerThread;//每个线程要下载的文件长度
	private MyNotify myNotify;
	private ThreadPoolManager tpm;
	
	public DownloadManager(int threadSize, URL url, String saveDir) {
		this.threadSize = threadSize;
		this.url = url;
		this.saveDir = saveDir;
	}
	
	
	public DownloadManager(int threadSize, URL url, String saveDir, MyNotify myNotify) {
		this.threadSize = threadSize;
		this.url = url;
		this.saveDir = saveDir;
		this.myNotify=myNotify;
	}
	
	public DownloadManager(int threadSize, URL url, String saveDir,ThreadPoolManager tpm, MyNotify myNotify) {
		this.threadSize = threadSize;
		this.url = url;
		this.saveDir = saveDir;
		this.myNotify=myNotify;
		this.tpm=tpm;
	}


	/**
	 * 下载方法
	 * @throws IOException 
	 */
	public void startDownLoad() throws IOException {
		String newFileName=getNewFileName(url);
		fileTotalSize=getDownLoadFileSize(url);
		saveFile =getSaveFile(this.saveDir,newFileName);
		createEmptyFile(saveFile,fileTotalSize);
		countFileSizePerThread();
		
		for(int i=0;i<this.threadSize;i++){
			//Thread t=new Thread(new DownLoadTask(url,saveFile,fileSizePerThread,i,myNotify)  ) ;
			//t.start();
			if(tpm!=null){
				tpm.process(new DownLoadTask(url,saveFile,fileSizePerThread,i,myNotify));
			}else{
				Thread t=new Thread(new DownLoadTask(url,saveFile,fileSizePerThread,i,myNotify)  ) ;
				t.start();
			}
			
			
			
		}
		
	}
	/**
	 * 计算每个线程下载的文件长度
	 */
	public void countFileSizePerThread(){
		this.fileSizePerThread= this.fileTotalSize%threadSize==0?this.fileTotalSize/threadSize:this.fileTotalSize/threadSize+1;
	}
	/**
	 * 生成新文件的文件名 ：yyyyMMddHHmmss.后缀名
	 * @param url
	 * @return
	 */
	private  String getNewFileName(URL url){
		String suffixName= url.getFile().substring(url.getFile().lastIndexOf("."));
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmss");
		String fileName=sdf.format(new Date()) ;
		return fileName+suffixName;
	}
	private  File getSaveFile(String dirName,String fileName){
		if(dirName==null){
			dirName=System.getProperty("user.home")+File.separator;
			
		}
		File f=new File(dirName,fileName);
		return f;
	}
	
	/**
	 * 根据创建文件为准和文件大小，创建一个固定大小的空文件
	 * @param file
	 * @param fileLength
	 * @throws IOException 
	 */
	private  void createEmptyFile(File file,long fileLength) throws IOException{
		RandomAccessFile raf=new RandomAccessFile(file ,"rw");
		raf.setLength(fileLength);
		raf.close();
	}
	
	/**
	 * 得到要下载的文件的大小
	 * @param url
	 * @return
	 * @throws IOException
	 */
	
	private  long getDownLoadFileSize(URL url) throws IOException{
		HttpURLConnection con=(HttpURLConnection) url.openConnection();
		con.setRequestMethod("HEAD");
		con.connect();
		long result=con.getContentLength();
		return result;
	}


	public long getFileTotalSize() {
		return this.fileTotalSize;
	}

}
