package com.brennan_hzl.photovine.activity;

import java.io.File;
import java.util.List;

import uk.co.senab.photoview.PhotoViewAttacher;

import com.brennan_hzl.photovine.R;
import com.brennan_hzl.photovine.bean.ImageBean;
import com.brennan_hzl.photovine.util.NativeImageLoader;
import com.brennan_hzl.photovine.util.StoreDataUtil;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.RectF;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;

public class CropImageActivity extends Activity {
	private ImageBean image;
	private PhotoViewAttacher mAttacher;
	private ImageView mImageView;
	private Bitmap imageSource;
 	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_crop_image);
		initImageBean();
		initImageView();
		initAttacher();
		initTitle();
	}
	
	private void initTitle() {
		((TextView)findViewById(R.id.navigation_title)).setText(R.string.title_crop_image);
	}

	@SuppressWarnings("unchecked")
	private void initImageBean() {
		String key = getIntent().getStringExtra("datakey");
		List<ImageBean> list = (List<ImageBean>) ((SlideshowApplication)getApplication()).getData(key);
		int postion = getIntent().getIntExtra("chooseImagePostion", 0);
		image = list.get(postion);	
	}

	private void initImageView() {
		mImageView = (ImageView) findViewById(R.id.photoView);
		LayoutParams lp = mImageView.getLayoutParams();
		lp.height = getResources().getDisplayMetrics().widthPixels;
		Point point = new Point(0, 0);
		
		point.set(lp.height, lp.height);
		imageSource = NativeImageLoader.decodeThumbBitmapForFile(image.imagePath, getResources().getDisplayMetrics().widthPixels, getResources().getDisplayMetrics().heightPixels);
		
		if (imageSource != null) {
			mImageView.setImageBitmap(imageSource);
		}
	}
	
	private void initAttacher() {
		mAttacher = new PhotoViewAttacher(mImageView);
		mAttacher.setScaleType(ImageView.ScaleType.FIT_CENTER);
	}

	public void goBack(View v) {
		finish();
	}
	
	public void goAhead(View v) {
		RectF localRectF = mAttacher.getDisplayRect();
		float scale = imageSource.getWidth() / localRectF.width();
		saveBitmap(cropBitmap(scale, -localRectF.left, -localRectF.top, mImageView.getWidth(), mImageView.getWidth()));
		setResult(2);
		finish();
	}
	
	private Bitmap cropBitmap(float s ,float rx, float ry, float rw, float rh) {
		int x = (int) (s * rx);
		int y = (int) (s * ry);
		int w = (int) (s * rw);
		int h = (int) (s * rh);
		return Bitmap.createBitmap(this.imageSource, x, y, w, h);
	}
	
	private void saveBitmap(Bitmap paramBitmap) {
	    String path = getCacheDir().getAbsolutePath() + File.separator + System.currentTimeMillis()+ ".JPEG";
	    StoreDataUtil.saveImgFileToAppInternalStorage(paramBitmap, path);
	    image.imagePath = path;
	    image.imageUri = "file://" + path;
	    image.original = false;
	    paramBitmap.recycle();
	}

}

