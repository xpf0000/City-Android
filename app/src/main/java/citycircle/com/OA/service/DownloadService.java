/*
 * @Title DownloadService.java
 * @Copyright Copyright 2010-2015 Yann Software Co,.Ltd All Rights Reserved.
 * @Description：
 * @author Yann
 * @date 2015-8-7 下午10:03:42
 * @version 1.0
 */
package citycircle.com.OA.service;

import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;

import citycircle.com.OA.entities.FileInfo;

/**
 * 类注释
 * @author Yann
 * @date 2015-8-7 下午10:03:42
 */
public class DownloadService extends Service
{
	public static final String DOWNLOAD_PATH =
			Environment.getExternalStorageDirectory().getAbsolutePath()
					+ "/citycircle/OADowns";
	public static final String ACTION_START = "ACTION_START";
	public static final String ACTION_STOP = "ACTION_STOP";
	public static final String ACTION_UPDATE = "ACTION_UPDATE";
	public static final String ACTION_FINISHED = "ACTION_FINISHED";
	public static final int MSG_INIT = 0;
	private String TAG = "DownloadService";
	private Map<Integer, DownloadTask> mTasks =
			new LinkedHashMap<Integer, DownloadTask>();

	/**
	 * @see android.app.Service#onStartCommand(android.content.Intent, int, int)
	 */
	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{
		// 获得Activity传过来的参数
		if (ACTION_START.equals(intent.getAction()))
		{
			FileInfo fileInfo = (FileInfo) intent.getSerializableExtra("fileInfo");
			Log.i(TAG , "Start:" + fileInfo.toString());
			// 启动初始化线程
			new InitThread(fileInfo).start();
		}
		else if (ACTION_STOP.equals(intent.getAction()))
		{
			FileInfo fileInfo = (FileInfo) intent.getSerializableExtra("fileInfo");
			Log.i(TAG , "Stop:" + fileInfo.toString());

			// 从集合中取出下载任务
			DownloadTask task = mTasks.get(fileInfo.getId());
			if (task != null)
			{
				task.isPause = true;
			}
		}

		return super.onStartCommand(intent, flags, startId);
	}

	private Handler mHandler = new Handler()
	{
		public void handleMessage(android.os.Message msg) {
			switch (msg.what)
			{
				case MSG_INIT:
					FileInfo fileInfo = (FileInfo) msg.obj;
					Log.i(TAG, "Init:" + fileInfo);
					// 启动下载任务
					DownloadTask task = new DownloadTask(DownloadService.this, fileInfo, 3);
					task.downLoad();
					// 把下载任务添加到集合中
					mTasks.put(fileInfo.getId(), task);
					break;

				default:
					break;
			}
		};
	};

	private class InitThread extends Thread
	{
		private FileInfo mFileInfo = null;

		public InitThread(FileInfo mFileInfo)
		{
			this.mFileInfo = mFileInfo;
		}

		/**
		 * @see java.lang.Thread#run()
		 */
		@Override
		public void run()
		{
			HttpURLConnection connection = null;
			RandomAccessFile raf = null;

			try
			{
				// 连接网络文件
				URL url = new URL(mFileInfo.getUrl());
				connection = (HttpURLConnection) url.openConnection();
				connection.setConnectTimeout(5000);
				connection.setRequestMethod("GET");
				int length = -1;

				if (connection.getResponseCode() == HttpURLConnection.HTTP_OK)
				{
					// 获得文件的长度
					length = connection.getContentLength();
				}

				if (length <= 0)
				{
					return;
				}

				File dir = new File(DOWNLOAD_PATH);
				if (!dir.exists())
				{
					dir.mkdir();
				}

				// 在本地创建文件
				File file = new File(dir, mFileInfo.getFileName());
				raf = new RandomAccessFile(file, "rwd");
				// 设置文件长度
				raf.setLength(length);
				mFileInfo.setLength(length);
				mHandler.obtainMessage(MSG_INIT, mFileInfo).sendToTarget();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			finally
			{
				if (connection != null)
				{
					connection.disconnect();
				}
				if (raf != null)
				{
					try
					{
						raf.close();
					}
					catch (IOException e)
					{
						e.printStackTrace();
					}
				}
			}
		}
	}

	/**
	 * @see android.app.Service#onBind(android.content.Intent)
	 */
	@Override
	public IBinder onBind(Intent intent)
	{
		return null;
	}

}
