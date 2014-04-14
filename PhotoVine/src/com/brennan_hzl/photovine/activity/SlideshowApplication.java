package com.brennan_hzl.photovine.activity;

import java.util.HashMap;
import java.util.Map;

import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.brennan_hzl.photovine.util.Sconstants;

import android.app.Application;
import android.util.Log;


public class SlideshowApplication extends Application {
	
	private final Map<String, Object> mDataMap = new HashMap<String, Object>();
	
	@Override
	public void onCreate() {
		initImageLoader();
		initUiDimens();
		super.onCreate();
	}
	
	private void initUiDimens() {
    	
		int screenWidth = getResources().getDisplayMetrics().widthPixels;
		int screenHeight = getResources().getDisplayMetrics().heightPixels;
		Sconstants.DEVICE_SCREEN_WIDTH = screenWidth;
		Sconstants.DEVICE_SCREEN_HEIGHT = screenHeight;
		Sconstants.DENSITY = getResources().getDisplayMetrics().density;
		
		Sconstants.IMAGE_GRIDVIEW_ITEM_WIDTH = (Sconstants.DEVICE_SCREEN_WIDTH - 8)/ 3;
		Sconstants.IMAGE_GRIDVIEW_ITEM_HEIGHT = Sconstants.IMAGE_GRIDVIEW_ITEM_WIDTH;
		Sconstants.AIBUM_GRIDVIEW_ITEM_WIDTH = (int) ((Sconstants.DEVICE_SCREEN_WIDTH - 30 * Sconstants.DENSITY) / 2);
	}

	private void initImageLoader() {
		int defualtDisplay = (int)getResources().getDisplayMetrics().widthPixels / 3;
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
        .memoryCacheExtraOptions(defualtDisplay, defualtDisplay)
        .memoryCache(new WeakMemoryCache())
        .denyCacheImageMultipleSizesInMemory()
        .build();
		
		ImageLoader.getInstance().init(config);
	}
	
	public Object getData(String key) {
		if (mDataMap.containsKey(key)) {
			return mDataMap.get(key);
		}
		
		return null;
	}
	
	public void addData(String key, Object data) {
		if (mDataMap.containsKey(key)) {
			Log.w("hello", "you are in danger");
		}
		mDataMap.put(key, data);
	}
	
	public void removeData(String key) {
		if (mDataMap.containsKey(key)) {
			mDataMap.remove(key);
		}
	}
	
}
