package com.brennan_hzl.photovine.view;

import java.util.ArrayList;
import java.util.List;

import com.brennan_hzl.photovine.R;
import com.brennan_hzl.photovine.util.AnimationClip;
import com.brennan_hzl.photovine.util.Sconstants;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class SlideShowView extends RelativeLayout {

	//private int durationPerImage = 1000; //1 second
	private ImageLoader imageLoader = ImageLoader.getInstance();
	private boolean transfer = false;
	private boolean isShowing = false;
	private List<AnimationClip> translationList = new ArrayList<AnimationClip>();
	private ImageView currentImage;
	private ImageView nextImage;
	private ImageButton btnPlay;
	private int currentId = 0;
	
	public SlideShowView(Context context) {
		super(context);
		init();
	}

	public SlideShowView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public SlideShowView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}
	
	@SuppressWarnings("deprecation")
	private void init() {
		currentImage = new ImageView(getContext());
		nextImage = new ImageView(getContext());
		currentImage.setAdjustViewBounds(true);
		currentImage.setClickable(false);
		currentImage.setFocusable(false);
		nextImage.setAdjustViewBounds(true);
		nextImage.setClickable(false);
		nextImage.setFocusable(false);
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		addView(nextImage, lp);
		addView(currentImage, lp);
		
		btnPlay = new ImageButton(getContext());
		btnPlay.setImageDrawable(getContext().getResources().getDrawable(R.drawable.btn_slideshow_play));
		btnPlay.setBackgroundDrawable(null);
		LayoutParams lpb = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lpb.addRule(CENTER_IN_PARENT, TRUE);
		addView(btnPlay, lpb);
		btnPlay.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				btnPlay.setVisibility(GONE);
				play();
			}
		});
		
		setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (isShowing)
					stop();
				return false;
			}
		});
	}

	private void setCoverImage() {
		currentImage.setAdjustViewBounds(false);
		//currentImage.setScaleType(ImageView.ScaleType.CENTER);
		AnimationClip fristClip = translationList.get(0);
		currentImage.setImageBitmap(imageLoader.loadImageSync(fristClip.uri, fristClip.targetImageSize, Sconstants.options));
	}
	
	public void setTranslation(List<AnimationClip> translations) {
		if (translations.size() == 0)
			return;
		translationList.clear();
		translationList.addAll(translations);
		setCoverImage();
	}
	
	public void addTranslation(AnimationClip clip) {
		if (clip != null) {
			translationList.add(clip);
		}
	}
	
	public void removeTranslation(String uri) {
		AnimationClip rClip = null;
		if (uri != null) {
			for (AnimationClip clip : translationList) {
				if (clip.uri.equalsIgnoreCase(uri)) {
					rClip = clip;
					break;
				}
			}
			if (rClip != null) {
				translationList.remove(rClip);
			}
		}
	}
	
	public void clearAllTranslation() {
		translationList.clear();
	}
	
	public void stop() {
		isShowing = false;
		currentImage.clearAnimation();
		nextImage.clearAnimation();
		currentId = 0;
		btnPlay.setVisibility(VISIBLE);
		setCoverImage();
	}
	
	public void play() {
		if (translationList.size() == 0 || isShowing)
			return;
		isShowing = true;
		playSlideShow();
	}
	
	private void playSlideShow() {
		if (!isShowing)
			return;
		if (currentId >= translationList.size()) {
			stop();
			return;
		}
		AnimationClip clip = translationList.get(currentId);
		currentImage.setImageBitmap(imageLoader.loadImageSync(clip.uri, clip.targetImageSize, Sconstants.options));
		currentImage.startAnimation(clip.anim);
		clip.anim.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				currentId++;
				currentImage.clearAnimation();
				if (!transfer) {
					playSlideShow();
				} else {
					transfer();
				}
			}
		});
	}

	protected void transfer() {
		if (currentId < translationList.size()) {
			AnimationClip clip = translationList.get(currentId);
			nextImage.setImageBitmap(imageLoader.loadImageSync(clip.uri, clip.targetImageSize, Sconstants.options));
			//addTransfer();
			//playSlideShow();
		} 
		
	}
	
	
	

}
