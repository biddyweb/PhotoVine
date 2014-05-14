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
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.AsyncTask;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

import com.brennan_hzl.photovine.activity.EditSlideShowActivity;
import com.brennan_hzl.photovine.bean.ImageBean;
import com.brennan_hzl.photovine.bean.MusicBean;
import com.brennan_hzl.photovine.fragment.MakeProgressFragment;
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
	private MakeProgressFragment progressFragment;
	
	public SlideShowMaker(Context context, List<ImageBean> images) {
		mContext = context;
		mImages = images;
		initFfmpeg();
	}
	
	private void initFfmpeg() {
		try {
			mFfmpegController = new FfmpegController(mContext, StoreDataUtil.getTempPathOfAppInternalStorage(mContext));
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
	
	public void createSlideShow(String videoPath, int shareType) {
		this.videoPath = videoPath;
		progressFragment = MakeProgressFragment.createFragment(videoPath, shareType);
		progressFragment.show(((EditSlideShowActivity)mContext).getSupportFragmentManager(), "makeSlidshow");
		
		ArrayList<Clip> images = new ArrayList<Clip>();
		for (ImageBean value : mImages) {
			Clip image = new Clip(value.imagePath);
			images.add(image);
		}
		if (StoreDataUtil.getPathOfStoreSlideShow(mContext) == null)
			return;
		Clip out = new Clip(videoPath);
		Clip audio = null;
		if (music != null) {
			audio = new Clip(music.getUrl());
			audio.startTime = musicStartTime;
		}
		
		out.width = width;
		out.height = height;
		//out.qscale = "5";
		if (mFfmpegController != null) {
			new CreateSlideShowAsyncTask().execute(images, audio, out, Watermask, durationPerImage);
		}
	}
	
	private class CreateSlideShowAsyncTask extends AsyncTask<Object, Integer, Void> {

		@Override
		protected Void doInBackground(Object... params) {
			@SuppressWarnings("unchecked")
			ArrayList<Clip> images = (ArrayList<Clip>) params[0];
			Clip audio = (Clip) params[1];
			Clip out = (Clip) params[2];
			String watermask = (String) params[3];
			float durationPerSlide = (Float) params[4];
			final String finalpath = out.path;
			try {
				Clip tmp = mFfmpegController.createSlideshowFromImagesAndAudio(images, audio, watermask, out, durationPerSlide, new ShellCallback(){

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
				StoreDataUtil.copyFile(tmp.path, finalpath);
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
			progressFragment.sendSuccessMessage();
			super.onProgressUpdate(values);
		}
		
	}
}
