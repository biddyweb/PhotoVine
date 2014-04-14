package com.brennan_hzl.photovine.activity;

import java.io.File;
import java.util.List;

import org.askerov.dynamicgid.DynamicGridView;

import com.brennan_hzl.photovine.R;
import com.brennan_hzl.photovine.adapter.DynamicAdapter;
import com.brennan_hzl.photovine.bean.ImageBean;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class EditImageActivity extends Activity {
	private DynamicGridView mGridView;
	private View mToolBar;
	private List<ImageBean> list;
	private DynamicAdapter adapter;
	private int selectedId = -1;
	private View oldSelectView;
	private String key;

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_image);
		
		key = getIntent().getStringExtra("datakey");
		list = (List<ImageBean>) ((SlideshowApplication)getApplication()).getData(key);
		//((SlideshowApplication)getApplication()).removeData(key);
		
		initGridView();
		initToolBar();
		initTitle();
	}
	
	private void initTitle() {
		((TextView)findViewById(R.id.navigation_title)).setText(R.string.title_edit_image);
	}
	
	private void initToolBar() {
		mToolBar = findViewById(R.id.layoutToolBar);
	}

	private void initGridView() {
		
		mGridView = (DynamicGridView) findViewById(R.id.dynamic_grid);
		mGridView.setSelector(new ColorDrawable(0));
		mGridView.setOnDropListener(new DynamicGridView.OnDropListener() {
          @Override
          public void onActionDrop() {
        	  mGridView.stopEditMode();
          }
        });
		
		mGridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            	if (selectedId != -1) {
					oldSelectView.setBackgroundColor(getResources().getColor(R.color.black_background));
					oldSelectView.invalidate();
					oldSelectView = null;
					selectedId = -1;
				}
            	toogleToolBar();
            	mGridView.startEditMode();
                return false;
            }
        });
		
		mGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (selectedId != position) {
					if (oldSelectView != null) {
						oldSelectView.setBackgroundColor(getResources().getColor(R.color.black_background));
						oldSelectView.invalidate();
					}
					oldSelectView = view;
					selectedId = position;
					oldSelectView.setBackgroundColor(getResources().getColor(R.color.yellow));
					oldSelectView.invalidate();
				} else {
					if (oldSelectView != null) {
						oldSelectView.setBackgroundColor(getResources().getColor(R.color.black_background));
						oldSelectView.invalidate();
					}
					oldSelectView = null;
					selectedId = -1;
				}
				
				toogleToolBar();
			}
			
		});
		adapter = new DynamicAdapter(this, list, 3);
		mGridView.setAdapter(adapter);
	}
	
	private void toogleToolBar() {
		if (selectedId != -1 && mToolBar.getVisibility() == View.GONE) {
			TranslateAnimation localTranslateAnimation = new TranslateAnimation(
					Animation.RELATIVE_TO_SELF,
					0,
					Animation.RELATIVE_TO_SELF,
					0,
					Animation.RELATIVE_TO_SELF,
					1,
					Animation.RELATIVE_TO_SELF,
					0);
			localTranslateAnimation.setInterpolator(new DecelerateInterpolator(2f));
			AlphaAnimation localAlphaAnimation = new AlphaAnimation(0.0F, 1.0F);
			AnimationSet localAnimationSet = new AnimationSet(false);
			localAnimationSet.addAnimation(localTranslateAnimation);
		    localAnimationSet.addAnimation(localAlphaAnimation);
		    localAnimationSet.setDuration(250L);
			mToolBar.setAnimation(localAnimationSet);
			mToolBar.setVisibility(View.VISIBLE);
		} else if (selectedId == -1 && mToolBar.getVisibility() == View.VISIBLE) {
			TranslateAnimation localTranslateAnimation = new TranslateAnimation(
					Animation.RELATIVE_TO_SELF,
					0,
					Animation.RELATIVE_TO_SELF,
					0,
					Animation.RELATIVE_TO_SELF,
					0,
					Animation.RELATIVE_TO_SELF,
					1);
			localTranslateAnimation.setInterpolator(new DecelerateInterpolator(2f));
			AlphaAnimation localAlphaAnimation = new AlphaAnimation(1.0F, 0.0F);
			AnimationSet localAnimationSet = new AnimationSet(false);
			localAnimationSet.addAnimation(localTranslateAnimation);
		    localAnimationSet.addAnimation(localAlphaAnimation);
		    localAnimationSet.setDuration(250L);
		    localAnimationSet.setAnimationListener(new AnimationListener() {
				
				@Override
				public void onAnimationStart(Animation animation) {}
				
				@Override
				public void onAnimationRepeat(Animation animation) {}
				
				@Override
				public void onAnimationEnd(Animation animation) {
					mToolBar.clearAnimation();
					mToolBar.setVisibility(View.GONE);
				}
			});
			mToolBar.startAnimation(localAnimationSet);
		}
	}

	public void deleteImage(View deleteView) {
		ImageBean image = list.get(selectedId);
		image.setSelectd(false);
		adapter.remove(image);
		if (!image.original) {
			new File(image.imagePath).delete();
		}
	}
	
	public void copyImage(View paramView) {
		ImageBean image = list.get(selectedId);
		ImageBean image2 = null;
		try {
			image2 = (ImageBean) image.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		if (image2 != null) {
			list.add(selectedId + 1, image2);
			adapter.set(list);
		}
		
	}
	
	public void cropImage(View paramView) {
		Intent localIntent = new Intent(this, CropImageActivity.class);
	    localIntent.putExtra("datakey", key);
	    localIntent.putExtra("chooseImagePostion", selectedId);
	    startActivityForResult(localIntent, 2);
	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		switch (arg0) {
		case 2:
			adapter.notifyDataSetChanged();
			break;
		}
		super.onActivityResult(arg0, arg1, arg2);
	}
	
	public void goBack(View v) {
		finish();
	}
	
	public void goAhead(View v) {
		finish();
	}
}

