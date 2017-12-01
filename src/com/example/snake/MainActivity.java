package com.example.snake;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.RelativeLayout;


public class MainActivity extends Activity {
	private SnakeView mSnakeView;  //需要刷新的对象，填充窗口的view
	private static final int REFRESH=1;  //定义消息
	private static final int REFRESHINTERVAL=300; //刷新的时间间隔
	private boolean isPaused = false; //线程的停止标志位	
	private Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if(msg.arg1 == REFRESH){
				if(mSnakeView != null){
					mSnakeView.invalidate();
				}
			}
		}  //thread handler消息处理		
	};
	private Thread mRefreshThread; //用于发送刷新消息的线程

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);   //关联activity_main.xml文件
        mSnakeView = new SnakeView(this);       
        setContentView(mSnakeView);
        
        isPaused = false;
        
        mRefreshThread = new Thread("TimeThread"){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				super.run();
				while(!isPaused){
					Message msg = mHandler.obtainMessage();
					msg.arg1=REFRESH;
					mHandler.sendMessage(msg);
					try {
						Thread.sleep(REFRESHINTERVAL);   //休眠一段时间后再次发送刷新信息
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			
        	
        };
        mRefreshThread.start();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
