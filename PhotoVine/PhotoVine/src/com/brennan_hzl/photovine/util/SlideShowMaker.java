package com.brennan_hzl.photovine.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.ffmpeg.android.Clip;
import org.ffmpeg.android.FfmpegController;
import org.ffmpeg.android.ShellUtils.ShellCallback;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

import com.brennan_hzl.photovine.bean.ImageBean;
import com.brennan_hzl.photovine.bean.MusicBean;
import com.nostra13.universalimageloader.core.assist.ImageSize;


/*
 * 
 */

public class SlideShowMaker {
	
	private String videoPath;
	private File frontFile;
	private int height = 640;
	private int width = 640;
	private float durationPerImage = 1.0f;
	private List<ImageBean> mImages;
	private String Title;
	private String Watermask;
	private MusicBean music;
	private String musicStartTime;
	private Context mContext;
	private FfmpegController mFfmpegController;
	
	public SlideShowMaker(Context context, List<ImageBean> images) {
		mContext = context;
		mImages = images;
		videoPath = StoreDataUtil.getPathOfStoreSlideShow(context).getAbsolutePath()+File.separator+"hello.mp4";
		frontFile = new File(StoreDataUtil.getPathOfStoreSlideShow(context).getAbsolutePath()+File.separator+"Aller_Lt.ttf");
		initFfmpeg();
	}
	
	private void initFfmpeg() {
		try {
			mFfmpegController = new FfmpegController(mContext, StoreDataUtil.getTempPathOfAppInternalStorage(mContext),frontFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void setDurationPerImage(float duration) {
		durationPerImage = duration;
	}
	
	public float getDurationPerImage() {
		return durationPerImage;
	}
	
	public void setTitleAndWatermask(String title, String watermask) {
		Title = title;
		Watermask = watermask;
	}
	
	public List<AnimationClip> getTranslations() {
		Animation anim = new TranslateAnimation(0f,0f,0f,0f);
		anim.setDuration((long) (durationPerImage*1000));
		anim.setFillAfter(true);
		ImageSize size= new ImageSize(mContext.getResources().getDisplayMetrics().widthPixels, mContext.getResources().getDisplayMetrics().widthPixels);
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
	
	public void createSlideShow() {
		ArrayList<Clip> images = new ArrayList<Clip>();
		for (ImageBean value : mImages) {
			Clip image = new Clip(value.imagePath);
			images.add(image);
		}
		if (StoreDataUtil.getPathOfStoreSlideShow(mContext) == null)
			return;
		Clip out = new Clip(videoPath);
		Clip audio;
		if (music != null) {
			audio = new Clip(music.getUrl());
			audio.startTime = musicStartTime;
		}
		
		out.width = width;
		out.height = height;
		//out.qscale = "5";
		if (mFfmpegController != null) {
			new CreateSlideShowAsyncTask().execute(images,null,out,durationPerImage);
		}
	}
	
	private class CreateSlideShowAsyncTask extends AsyncTask<Object, Integer, Void> {

		@Override
		protected Void doInBackground(Object... params) {
			@SuppressWarnings("unchecked")
			ArrayList<Clip> images = (ArrayList<Clip>) params[0];
			Clip audio = (Clip) params[1];
			Clip out = (Clip) params[2];
			float durationPerSlide = (Float) params[3];
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
				StoreDataUtil.clearTempFileOfAppInternalStorage(mContext);
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
		}
		
	}
}
