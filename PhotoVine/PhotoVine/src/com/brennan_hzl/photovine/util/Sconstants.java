package com.brennan_hzl.photovine.util;

import android.graphics.Bitmap;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

/*
 * 
 */

public class Sconstants {
	/*
	 * 
	 */
	public static float DENSITY; 
	
	public static int DEVICE_SCREEN_WIDTH;
	
	public static int DEVICE_SCREEN_HEIGHT;
	
	public static int IMAGE_GRIDVIEW_ITEM_WIDTH;
	
	public static int IMAGE_GRIDVIEW_ITEM_HEIGHT;
	
	public static int AIBUM_GRIDVIEW_ITEM_WIDTH;
	
	public static int AIBUM_GRIDVIEW_ITEM_HEIGHT;
	
	public static DisplayImageOptions options = new DisplayImageOptions.Builder() 
    .bitmapConfig(Bitmap.Config.RGB_565)
    .imageScaleType(ImageScaleType.EXACTLY)
    .cacheInMemory(true)
    .cacheOnDisc(true)
    .build();
}
