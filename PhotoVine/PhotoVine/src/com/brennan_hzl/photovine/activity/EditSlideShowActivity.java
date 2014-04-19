package com.brennan_hzl.photovine.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.ffmpeg.android.Clip;
import org.ffmpeg.android.FfmpegController;
import org.ffmpeg.android.ShellUtils.ShellCallback;

import com.brennan_hzl.photovine.R;
import com.brennan_hzl.photovine.bean.ImageBean;
import com.brennan_hzl.photovine.bean.SelectedImageBean;
import com.brennan_hzl.photovine.util.AnimationClip;
import com.brennan_hzl.photovine.util.Sconstants;
import com.brennan_hzl.photovine.util.StoreDataUtil;
import com.brennan_hzl.photovine.view.SlideShowView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.utils.StorageUtils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.TextView;

public class EditSlideShowActivity extends Activity {

	private SlideShowView mPlayer;
	private ImageLoader mImageLoader = ImageLoader.getInstance();
	private ProgressDialog mProgressDialog;
	private List<ImageBean> mImages = SelectedImageBean.getInstance().getChoosedImages();
	private FfmpegController mFfmpegController;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_slideshow);
		initView();
		initFfmpeg();
		
	}
	
	private void initFfmpeg() {
		try {
			mFfmpegController = new FfmpegController(this, StoreDataUtil.getTempPathOfAppInternalStorage(this));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void initView() {
		initPlay();
		initTitle();
	}

	private void initPlay() {
		mPlayer = (SlideShowView) findViewById(R.id.slideshow_player);
		mPlayer.setTranslation(getTranslations());
	}

	private List<AnimationClip> getTranslations() {
		Animation anim = new TranslateAnimation(0f,0f,0f,0f);
		anim.setDuration(1000);
		anim.setFillAfter(true);
		ImageSize size= new ImageSize(getResources().getDisplayMetrics().widthPixels, getResources().getDisplayMetrics().widthPixels);
		List<AnimationClip> clips = new ArrayList<AnimationClip>();
		for (ImageBean image : mImages) {
			AnimationClip clip = new AnimationClip();
			clip.uri = image.imageUri;
			clip.targetImageSize = size;
			clip.anim = anim;
			clips.add(clip);
		}
		return clips;
	}

	private void initTitle() {
		((TextView)findViewById(R.id.navigation_title)).setText(R.string.title_edit_slideshow);
	}
	
	public void goBack(View v) {
		finish();
	}
	
	public void goAhead(View v) {
		mProgressDialog = ProgressDialog.show(this, null, getString(R.string.onloading));
		ArrayList<Clip> images = new ArrayList<Clip>();
		for (ImageBean value : mImages) {
			Clip image = new Clip(value.imagePath);
			images.add(image);
		}
		if (StoreDataUtil.getPathOfStoreSlideShow(this) == null)
			return;
		Clip out = new Clip(StoreDataUtil.getPathOfStoreSlideShow(this).getAbsolutePath()+File.separator+"hello.mp4");
		out.width = 640;
		out.height = 640;
		//out.qscale = "5";
		if (mFfmpegController != null) {
			new CreateSlideShowAsyncTask().execute(images,null,out,1);
		}
		
	}
	
	private class CreateSlideShowAsyncTask extends AsyncTask<Object, Integer, Void> {

		@Override
		protected Void doInBackground(Object... params) {
			@SuppressWarnings("unchecked")
			ArrayList<Clip> images = (ArrayList<Clip>) params[0];
			Clip audio = (Clip) params[1];
			Clip out = (Clip) params[2];
			int durationPerSlide = (Integer) params[3];
			try {
				mFfmpegController.createSlideshowFromImagesAndAudio(images, audio, out, durationPerSlide, new ShellCallback(){

					@Override
					public void shellOut(String shellLine) {
						Log.v("Ffmpeghello", "Shellout:"+shellLine);
					}

					@Override
					public void processComplete(int exitValue) {
						if (exitValue == 843133702) {
							publishProgress(100);
						}
					}
				});
			} catch (Exception e) {
				Log.v("Ffmpeghello", "Exception");
				e.printStackTrace();
			} finally {
				StoreDataUtil.clearTempFileOfAppInternalStorage(getApplicationContext());
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			mProgressDialog.dismiss();
			super.onProgressUpdate(values);
		}
		
	}
	
	private void combine() {
	    Clip tmp1 = new Clip(StoreDataUtil.getPathOfStoreSlideShow(this).getAbsolutePath()+File.separator+"hello1.mp4");
	    Clip tmp2 = new Clip(StoreDataUtil.getPathOfStoreSlideShow(this).getAbsolutePath()+File.separator+"hello.mp4");
	    ArrayList<Clip> videos = new ArrayList<Clip>();
	    videos.add(tmp1);
	    videos.add(tmp2);
	    Clip out = new Clip(StoreDataUtil.getPathOfStoreSlideShow(this).getAbsolutePath()+File.separator+"hello2.mp4");
		try {
			mFfmpegController.concatAndTrimFilesMP4Stream(videos, out, true, false, new ShellCallback(){

				@Override
				public void shellOut(String shellLine) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void processComplete(int exitValue) {
					// TODO Auto-generated method stub
					
				}
			
			});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

