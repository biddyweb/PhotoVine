package com.brennan_hzl.photovine.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.ffmpeg.android.Clip;
import org.ffmpeg.android.FfmpegController;
import org.ffmpeg.android.ShellUtils.ShellCallback;

import com.brennan_hzl.photovine.R;
import com.brennan_hzl.photovine.bean.ImageBean;
import com.brennan_hzl.photovine.bean.MusicBean;
import com.brennan_hzl.photovine.bean.SelectedImageBean;
import com.brennan_hzl.photovine.fragment.ChooseMusicFragment;
import com.brennan_hzl.photovine.fragment.DurationFragment;
import com.brennan_hzl.photovine.fragment.TitleFragment;
import com.brennan_hzl.photovine.util.AnimationClip;
import com.brennan_hzl.photovine.util.Sconstants;
import com.brennan_hzl.photovine.util.SlideShowMaker;
import com.brennan_hzl.photovine.util.StoreDataUtil;
import com.brennan_hzl.photovine.util.TiltEffectAttacher;
import com.brennan_hzl.photovine.view.SlideShowView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.utils.StorageUtils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager.OnBackStackChangedListener;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.TextView;

public class EditSlideShowActivity extends FragmentActivity {

	private SlideShowMaker mMaker;
	private SlideShowView mPlayer;
	private ProgressDialog mProgressDialog;
	private View buttonDuration;
	private View buttonText;
	private View buttonMusic;
	private View buttonEffect;
	private boolean editState = false;
	private List<ImageBean> mImages = SelectedImageBean.getInstance().getChoosedImages();
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_slideshow);
		mMaker = new SlideShowMaker(this, mImages);
		getSupportFragmentManager().addOnBackStackChangedListener(new OnBackStackChangedListener() {
			
			@Override
			public void onBackStackChanged() {
				editState = !editState;
			}
		});
		initView();
	}

	@Override
	protected void onResume() {
		mPlayer.setSlideShowMaker(mMaker);
		super.onResume();
	}

	private void initView() {
		mPlayer = (SlideShowView) findViewById(R.id.slideshow_player);
		
		((TextView)findViewById(R.id.navigation_title)).setText(R.string.title_edit_slideshow);
		
		buttonDuration = findViewById(R.id.change_duration);
		buttonText = findViewById(R.id.add_text);
		buttonMusic = findViewById(R.id.add_music);
		buttonEffect = findViewById(R.id.add_effect);
		OnClickListener listener = new ToolClickListener();
		buttonDuration.setOnClickListener(listener);
		buttonText.setOnClickListener(listener);
		buttonMusic.setOnClickListener(listener);
		buttonEffect.setOnClickListener(listener);
		
		TiltEffectAttacher.attach(buttonDuration);
		TiltEffectAttacher.attach(buttonText);
		TiltEffectAttacher.attach(buttonMusic);
		TiltEffectAttacher.attach(buttonEffect);
	}

	
	public void goBack(View v) {
		finish();
	}
	
	public void goAhead(View v) {
		String videoPath = StoreDataUtil.getPathOfStoreSlideShow(this).getAbsolutePath()+File.separator+"PhotoVine"+ new Random(6).nextLong()+".mp4"; 
		mMaker.createSlideShow(videoPath,1);
	}
	
	public void setItemDuration(boolean isAuto) {
		if (isAuto) {
			setItemDuration(15.0f/mImages.size());
		} else {
			setItemDuration(1.0f);
		}
	}
	
	public void setItemDuration(float duration) {
		if (mPlayer.isShowing()) {
			mPlayer.stop();
		}
		mMaker.setDurationPerImage(duration);
		mPlayer.setDurationPerImage(duration);
	}
	
	public void setTitleAndWatermask(String title, String watermask) {
		mMaker.setTitleAndWatermask(title, watermask);
		mPlayer.setTitleAndWatermask(title, watermask);
	}
	
	public void setMusic(MusicBean music) {
		
	}
	
	private class ToolClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			if (editState) { 
				return;
			}
			Fragment newFragment;
			FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
			transaction.setCustomAnimations(
	                R.anim.slide_bottom_in,
	                R.anim.no_anim,
	                R.anim.no_anim,
	                R.anim.slide_up_out);
			
			switch (v.getId()) {
			case R.id.change_duration:
				newFragment = new DurationFragment();
				transaction.add(R.id.fragment_container, newFragment);
				transaction.addToBackStack(null);
				transaction.commit();
				break;
			case R.id.add_text:
				newFragment = new TitleFragment();
				transaction.add(R.id.fragment_container, newFragment);
				transaction.addToBackStack(null);
				transaction.commit();
				break;
			default:
				break;	
			}
			
		}
		
	}

	@Override
	protected void onDestroy() {
		StoreDataUtil.clearTempFileOfAppInternalStorage(this);
		super.onDestroy();
	}
	
}

