package com.brennan_hzl.photovine.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;

public class StoreDataUtil {
	
	public static void saveImgFileToAppInternalStorage(Bitmap bitmap, String filePath) {
		File file = new File(filePath);
		FileOutputStream fos = null;
		try {
	       if (!file.exists()) {
	    	   file.createNewFile();
	       }
	       fos = new FileOutputStream(file);
	       bitmap.compress(CompressFormat.JPEG, 100, fos); 
	       fos.flush();
	       fos.close();
		} catch (FileNotFoundException e) {
		    e.printStackTrace();
		} catch (IOException e) {
		    e.printStackTrace();
		}
	}

	
}