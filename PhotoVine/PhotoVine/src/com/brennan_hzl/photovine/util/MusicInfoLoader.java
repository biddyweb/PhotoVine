package com.brennan_hzl.photovine.util;

import java.util.ArrayList;
import java.util.List;

import com.brennan_hzl.photovine.bean.MusicBean;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.widget.Toast;

/*
 * 
 */

public class MusicInfoLoader {
	/*
	 * 
	 */
	private final static int SCAN_OK = 1;
	private final static int SCAN_FAIL = 0;
	private Context mContext;
	private MusicInfoResponeHander mInfoHander;
	private List<MusicBean> mMusic = new ArrayList<MusicBean>();
	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (mInfoHander == null) {
				return;
			}
			switch (msg.what) {
			case SCAN_OK:
				mInfoHander.onSuccess(mMusic);
				break;
			case SCAN_FAIL:
				mInfoHander.onFailed();
				break;
			}
		}
		
	};
	
	
	public MusicInfoLoader(Context context) {
		mContext = context;
	}
	
	public void requestAlbums(MusicInfoResponeHander infoHanser) {
		mInfoHander = infoHanser;
		if (mInfoHander != null) {
			getAlbums();
		}
	}
	
	private void getAlbums() {
		if(!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
			Toast.makeText(mContext, "‘›ŒﬁÕ‚≤ø¥Ê¥¢", Toast.LENGTH_SHORT).show();
			mHandler.sendEmptyMessage(SCAN_FAIL);
		}
		
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				ContentResolver mContentResolver = mContext.getContentResolver();

				Cursor cursor = mContentResolver.query(  
		                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null,  
		                MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
				
				while (cursor.moveToNext()) {
					  
					MusicBean mp3Info = new MusicBean();  
					
		            long id = cursor.getLong(cursor  
		                    .getColumnIndex(MediaStore.Audio.Media._ID));
		            String title = cursor.getString((cursor  
		                    .getColumnIndex(MediaStore.Audio.Media.TITLE)));
		            String artist = cursor.getString(cursor  
		                    .getColumnIndex(MediaStore.Audio.Media.ARTIST));
		            long duration = cursor.getLong(cursor  
		                    .getColumnIndex(MediaStore.Audio.Media.DURATION));
		            long size = cursor.getLong(cursor  
		                    .getColumnIndex(MediaStore.Audio.Media.SIZE)); 
		            String url = cursor.getString(cursor  
		                    .getColumnIndex(MediaStore.Audio.Media.DATA)); 
		            int isMusic = cursor.getInt(cursor  
		                    .getColumnIndex(MediaStore.Audio.Media.IS_MUSIC));
		            if (isMusic != 0) { 
		                mp3Info.setId(id);  
		                mp3Info.setTitle(title);  
		                mp3Info.setArtist(artist);  
		                mp3Info.setDuration(duration);  
		                mp3Info.setSize(size);  
		                mp3Info.setUrl(url);  
		                mMusic.add(mp3Info);  
		            }  
		        }  
				cursor.close();

				mHandler.sendEmptyMessage(SCAN_OK);
				
			}
		}).start();
		
	}
	
	
	public interface MusicInfoResponeHander {
		 public void onSuccess(List<MusicBean> albums);
		 public void onFailed();
	}
}
