package com.brennan_hzl.photovine.view;

import java.util.ArrayList;
import java.util.List;

import com.brennan_hzl.photovine.R;
import com.brennan_hzl.photovine.util.AnimationClip;
import com.brennan_hzl.photovine.util.Sconstants;
import com.brennan_hzl.photovine.util.SlideShowMaker;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SlideShowView extends SquareLayout {

	//private int durationPerImage = 1000; //1 second
	private ImageLoader imageLoader = ImageLoader.getInstance();
	private boolean transfer = false;
	private boolean isShowing = false;
	private List<AnimationClip> translationList = new ArrayList<AnimationClip>();
	private ImageView currentImage;
	private ImageView nextImage;
	private ImageButton btnPlay;
	private TextView showDuration;
	private TextView Title;
	private TextView Watermark;
	private SlideShowMaker mMaker;
	private float durationPerImage = 1.0f;
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
		View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_slideshowview, this);
		currentImage = (ImageView) view.findViewById(R.id.current_image);
		nextImage = (ImageView) view.findViewById(R.id.next_image);
		
		
		
		Title = (TextView) view.findViewById(R.id.text_title);
		Watermark = (TextView) view.findViewById(R.id.text_watermask);
		
		
		
		btnPlay = (ImageButton) view.findViewById(R.id.button_play);
		btnPlay.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				btnPlay.setVisibility(GONE);
				showDuration.setVisibility(GONE);
				play();
			}
		});
		showDuration = (TextView) view.findViewById(R.id.text_duration);
		
		setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (isShowing)
					stop();
				return false;
			}
		});
	}

	public void setDurationPerImage(float duration) {
		durationPerImage = duration;
		showDuration.setText(translationList.size()+String.format(getContext().getString(R.string.show_duration), Float.toString(durationPerImage)));
	}
	
	private void setCoverImage() {
		currentImage.setAdjustViewBounds(false);
		//currentImage.setScaleType(ImageView.ScaleType.CENTER);
		AnimationClip fristClip = translationList.get(0);
		currentImage.setImageBitmap(imageLoader.loadImageSync(fristClip.uri, fristClip.targetImageSize, Sconstants.options));
	}
	
	public void setTitleAndWatermask(String title, String watermask) {
		Title.setText(title);
		Watermark.setText(watermask);
	}
	
	public void setSlideShowMaker(SlideShowMaker maker) {
		mMaker = maker;
		setTranslation(mMaker.getTranslations());
		showDuration.setText(translationList.size()+String.format(getContext().getString(R.string.show_duration), Float.toString(durationPerImage)));
		setCoverImage();
	}
	
	private void setTranslation(List<AnimationClip> translations) {
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
		showDuration.setVisibility(VISIBLE);
		setCoverImage();
	}
	
	public void play() {
		if (mMaker != null) {
			setTranslation(mMaker.getTranslations());
		}
		if (translationList.size() == 0 || isShowing)
			return;
		isShowing = true;
		currentId = 0;
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
		
		currentImage.startAnimation(clip.anim);
	}

	protected void transfer() {
		if (currentId < translationList.size()) {
			AnimationClip clip = translationList.get(currentId);
			nextImage.setImageBitmap(imageLoader.loadImageSync(clip.uri, clip.targetImageSize, Sconstants.options));
			//addTransfer();
			//playSlideShow();
		} 
		
	}
	
	public boolean isShowing() {
		return isShowing;
	}
	

}
