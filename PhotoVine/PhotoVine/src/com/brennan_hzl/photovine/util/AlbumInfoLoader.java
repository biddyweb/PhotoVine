package com.brennan_hzl.photovine.util;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.brennan_hzl.photovine.bean.AlbumBean;
import com.brennan_hzl.photovine.bean.ImageBean;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.widget.Toast;

/*
 * 
 */

public class AlbumInfoLoader {
	/*
	 * 
	 */
	private final static int SCAN_OK = 1;
	private final static int SCAN_FAIL = 0;
	private Context mContext;
	private AlbumInfoResponeHander mInfoHanser;
	private List<AlbumBean> mAlbums = new ArrayList<AlbumBean>();
	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (mInfoHanser == null) {
				return;
			}
			switch (msg.what) {
			case SCAN_OK:
				mInfoHanser.onSuccess(mAlbums);
				break;
			case SCAN_FAIL:
				mInfoHanser.onFailed();
				break;
			}
		}
		
	};
	
	
	public AlbumInfoLoader(Context context) {
		mContext = context;
	}
	
	public void requestAlbums(AlbumInfoResponeHander infoHanser) {
		mInfoHanser = infoHanser;
		if (mInfoHanser != null) {
			getAlbums();
		}
	}
	
	private void getAlbums() {
		if(!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
			Toast.makeText(mContext, "暂无外部存储", Toast.LENGTH_SHORT).show();
			mHandler.sendEmptyMessage(SCAN_FAIL);
		}
		
		//显示进度条
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				ContentResolver mContentResolver = mContext.getContentResolver();

				HashMap<String, List<ImageBean>> mGruopMap = new HashMap<String, List<ImageBean>>();
				//只查询jpeg和png的图片
				Cursor mCursor = mContentResolver.query(mImageUri, null,
						MediaStore.Images.Media.MIME_TYPE + "=? or "
								+ MediaStore.Images.Media.MIME_TYPE + "=? or "
									+ MediaStore.Images.Media.SIZE + ">?" ,
						new String[] { "image/jpeg", "image/png", "50000" }, MediaStore.Images.Media.DATE_MODIFIED);
				
				while (mCursor.moveToNext()) {
					//获取图片的路径
					String path = mCursor.getString(mCursor
							.getColumnIndex(MediaStore.Images.Media.DATA));
					
					ImageBean imageBean = new ImageBean(path);
					//获取该图片的父路径名
					String parentName = new File(path).getParentFile().getName();

					
					//根据父路径名将图片放入到mGruopMap中
					if (!mGruopMap.containsKey(parentName)) {
						List<ImageBean> chileList = new ArrayList<ImageBean>();
						chileList.add(imageBean);
						mGruopMap.put(parentName, chileList);
					} else {
						mGruopMap.get(parentName).add(imageBean);
					}
				}
				mAlbums = subGroupOfImage(mGruopMap);
				mCursor.close();
				//通知Handler扫描图片完成
				mHandler.sendEmptyMessage(SCAN_OK);
				
			}
		}).start();
		
	}
	
	private List<AlbumBean> subGroupOfImage(HashMap<String, List<ImageBean>> mGruopMap){
		if(mGruopMap.size() == 0){
			return null;
		}
		List<AlbumBean> list = new ArrayList<AlbumBean>();
		
		Iterator<Map.Entry<String, List<ImageBean>>> it = mGruopMap.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, List<ImageBean>> entry = it.next();
			AlbumBean mAlbumBean = new AlbumBean();
			String key = entry.getKey();
			List<ImageBean> value = entry.getValue();
			
			mAlbumBean.setFolderName(key);
			mAlbumBean.setImageCounts(value.size());
			mAlbumBean.setImages(value);
			
			list.add(mAlbumBean);
		}
		
		return list;
		
	}
	
	public interface AlbumInfoResponeHander {
		 public void onSuccess(List<AlbumBean> albums);
		 public void onFailed();
	}
}
