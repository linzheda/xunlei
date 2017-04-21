package com.ze.bean1;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;

import com.zd.utils.threadpool.MyNotify;
import com.zd.utils.threadpool.ThreadPoolManager;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class Test {

	protected Shell shell;
	private Text txtHttpurlbzcomdownebbeaeaecebeeeebbebdbdexe;
	private Text text_1;
	private ThreadPoolManager tpm;
	private int filesize;
	private DownloadManager dlm;
	private  int value;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			Test window = new Test();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell();
		shell.setSize(710, 399);
		shell.setText("迅雷增强版");
		
		Label label = new Label(shell, SWT.NONE);
		label.setBounds(50, 29, 61, 17);
		label.setText("文件地址：");
		
		txtHttpurlbzcomdownebbeaeaecebeeeebbebdbdexe = new Text(shell, SWT.BORDER);
		txtHttpurlbzcomdownebbeaeaecebeeeebbebdbdexe.setText("http://url.222bz.com/down/%E6%8B%B3%E7%9A%8797%E5%AE%8C%E7%BE%8E%E7%89%88%E4%B8%8B%E8%BD%BD@322_565.exe");
		txtHttpurlbzcomdownebbeaeaecebeeeebbebdbdexe.setBounds(117, 29, 483, 23);
		
		Label label_1 = new Label(shell, SWT.NONE);
		label_1.setBounds(50, 71, 61, 17);
		label_1.setText("线程数：");
		
		final Combo combo = new Combo(shell, SWT.NONE);
		combo.setBounds(117, 71, 88, 25);
		combo.setItems(new String[]{"2","3","4","5","6","7","8","9","10"});
		combo.setText("6");
		Label label_2 = new Label(shell, SWT.NONE);
		label_2.setBounds(50, 119, 61, 17);
		label_2.setText("保存目录");
		
		text_1 = new Text(shell, SWT.BORDER);
		text_1.setBounds(117, 119, 230, 23);
		text_1.setText(System.getProperty("user.home")+File.separator);
		Button button = new Button(shell, SWT.NONE);
		
		button.setBounds(388, 119, 80, 27);
		button.setText("选择目录");
		
		Button button_1 = new Button(shell, SWT.NONE);
		
		button_1.setBounds(159, 175, 80, 27);
		button_1.setText("下载");
		
		final ProgressBar progressBar = new ProgressBar(shell, SWT.NONE);
		progressBar.setBounds(116, 242, 474, 17);
		
		
		//点击选择目录
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				DirectoryDialog dd=new DirectoryDialog(shell,SWT.OPEN);
				dd.setFilterPath(System.getProperty("user.home")+File.separator);
				String dirPath=dd.open();
				if(dirPath==null||"".equals(dirPath)){
					return ;
				}
				text_1.setText(dirPath);
			}
		});
		//点击下载
		button_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					int threadSize=Integer.parseInt(combo.getText());
					String addr=txtHttpurlbzcomdownebbeaeaecebeeeebbebdbdexe.getText();
					URL url=new URL(addr);
					String saveDir=System.getProperty("user.home")+File.separator;
					
					 dlm=new DownloadManager(threadSize,url,saveDir,tpm, new MyNotify(){

						@Override
						public synchronized void notifyResult(Object obj) {
							long size=Long.parseLong(obj.toString());
							filesize+=size;
							
							Display.getDefault().asyncExec(new Runnable(){
								@Override
								public  void run() {
									value=(int ) ((filesize/(double)dlm.getFileTotalSize())*100);
									progressBar.setSelection(value);
									if(value==100){
										MessageBox md=new MessageBox(shell,SWT.NONE);
										md.setText("下载成功...");
										md.open();
										//progressBar.setSelection(0);
									}
								}
								
							});
						}
					});
					dlm.startDownLoad();
				} catch (Exception e1) {
					e1.printStackTrace();
				}finally{
					
				} 
				
				
				
				
			}
		});

	}
}
