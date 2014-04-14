package com.brennan_hzl.photovine.activity;

import java.util.ArrayList;
import java.util.List;

import com.brennan_hzl.photovine.R;
import com.brennan_hzl.photovine.adapter.AlbumAdapter;
import com.brennan_hzl.photovine.adapter.ImageAdapter;
import com.brennan_hzl.photovine.bean.AlbumBean;
import com.brennan_hzl.photovine.bean.ImageBean;
import com.brennan_hzl.photovine.bean.SelectedImageBean;
import com.brennan_hzl.photovine.util.AlbumInfoLoader;
import com.brennan_hzl.photovine.util.Sconstants;
import com.brennan_hzl.photovine.util.AlbumInfoLoader.AlbumInfoResponeHander;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

/**
 * @author speedinghzl
 */
public class ChooseImageActivity extends Activity implements AlbumInfoResponeHander {
	private List<AlbumBean> mAlbums = new ArrayList<AlbumBean>();
	private ProgressDialog mProgressDialog;
	private AlbumAdapter mAlbumAdapter;
	private ImageAdapter mImageAdapter;
	private GridView mAlbumGridView;
	private GridView mImageGridView;
	private TextView mNavigationTitle;
	private AlbumBean mCurrentAlbum;
	private SelectedImageBean mSelectdImageAlbum = SelectedImageBean.getInstance();
	/*
	 * False if mAlbumGridView is invisible, 
	 */
	private boolean GridViewState = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_choose_album);
		
		initView();
		initAlbums();	
	}
	
	@Override
	protected void onStart() {
		((TextView)findViewById(R.id.navigation_title)).setText(
				String.format(getResources().getString(R.string.selected_image_count)
						,mSelectdImageAlbum.getImagesCount()));

		if (GridViewState) {
			switchGridView();
		}
		super.onStart();
	}

	public void goBack(View v) {
		if (GridViewState) {
			switchGridView();
		} else {
			//go to welcomeActivity
		}
	}
	
	public void goAhead(View v) {
		if (SelectedImageBean.getInstance().getImagesCount() > 0 ) {
			List<ImageBean> childList = SelectedImageBean.getInstance().getChoosedImages();
			String key = Long.toString(System.currentTimeMillis());
			((SlideshowApplication)getApplication()).addData(key, childList);
			
			Intent mIntent = new Intent(ChooseImageActivity.this, EditImageActivity.class);
			mIntent.putExtra("datakey", key);
			startActivity(mIntent);
		} else {
			Toast.makeText(getBaseContext(), R.string.less_than_one, Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * 
	 */
	private void initAlbums() {
		mProgressDialog = ProgressDialog.show(this, null, getString(R.string.onloading));
		new AlbumInfoLoader(this).requestAlbums(this);
	}
	
	private void initView() {
		mAlbumGridView = (GridView) findViewById(R.id.main_grid);
		mAlbumGridView.setColumnWidth(Sconstants.AIBUM_GRIDVIEW_ITEM_WIDTH);
		mAlbumGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				switchGridView();
				mCurrentAlbum = mAlbums.get(position);
				mImageAdapter = new ImageAdapter(ChooseImageActivity.this, mCurrentAlbum.getImages(), mImageGridView);
				mImageGridView.setAdapter(mImageAdapter);
			}
		});
		mImageGridView = (GridView) findViewById(R.id.child_grid);
		mImageGridView.setColumnWidth(Sconstants.IMAGE_GRIDVIEW_ITEM_WIDTH);
		
		mNavigationTitle = (TextView) findViewById(R.id.navigation_title);
	}
	
	private void switchGridView() {
		if (GridViewState) {
			Animation outAnim = AnimationUtils.loadAnimation(this, R.anim.push_right_out);
			mImageGridView.setAnimation(outAnim);
			Animation inAnim = AnimationUtils.loadAnimation(this, R.anim.push_left_in);
			mAlbumGridView.setAnimation(inAnim);
			setVisibility(mAlbumGridView, false);
			setVisibility(mImageGridView, true);
		} else {
			Animation outAnim = AnimationUtils.loadAnimation(this, R.anim.shrink_fade_out_right);
			mAlbumGridView.setAnimation(outAnim);
			Animation inAnim = AnimationUtils.loadAnimation(this, R.anim.slide_in_right);
			mImageGridView.setAnimation(inAnim);
			setVisibility(mImageGridView, false);
			setVisibility(mAlbumGridView, true);
		}
		GridViewState = !GridViewState; 
	}
	
	private void setVisibility(View view, boolean isGone) {
		if (isGone) {
			view.setVisibility(View.GONE);
		} else {
			view.setVisibility(View.VISIBLE);
		}
	}
	
	public void changeTitleCount() {
		mNavigationTitle.setText(String.format(getResources().getString(R.string.selected_image_count), mSelectdImageAlbum.getImagesCount()));
	}

	@Override
	public void onBackPressed() {
		if (GridViewState) {
			switchGridView();
		} else {
			super.onBackPressed();
		}
	}

	@Override
	public void onSuccess(List<AlbumBean> albums) {
		mProgressDialog.dismiss();
		mAlbums = albums;
		mAlbumAdapter = new AlbumAdapter(ChooseImageActivity.this, mAlbums, mAlbumGridView);
		mAlbumGridView.setAdapter(mAlbumAdapter);
	}

	@Override
	public void onFailed() {
		mProgressDialog.dismiss();
	}


}

