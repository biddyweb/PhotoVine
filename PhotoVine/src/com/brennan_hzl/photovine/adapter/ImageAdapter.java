package com.brennan_hzl.photovine.adapter;

import java.util.List;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.GridView;
import android.widget.ToggleButton;

import com.brennan_hzl.photovine.R;
import com.brennan_hzl.photovine.activity.ChooseImageActivity;
import com.brennan_hzl.photovine.bean.ImageBean;
import com.brennan_hzl.photovine.bean.SelectedImageBean;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

public class ImageAdapter extends BaseAdapter {
	private static int PLACEHOLDER_BACKGROUND_COLOR_SIZE = 6;
	private SelectedImageBean selectedImage = SelectedImageBean.getInstance();
	private List<ImageBean> list;
	protected LayoutInflater mInflater;
	private DisplayImageOptions options;
	private int[] mPhotoPlaceholderColor; 
	private Context mContext;
	
	
	public ImageAdapter(Context context, List<ImageBean> list, GridView gridView) {
		mContext = context;
		this.list = list;
		mInflater = LayoutInflater.from(context);
		options = new DisplayImageOptions.Builder() 
        .bitmapConfig(Bitmap.Config.RGB_565)
        .imageScaleType(ImageScaleType.EXACTLY)
        .cacheInMemory(true)
        .cacheOnDisc(true)
        .build();
		mPhotoPlaceholderColor = context.getResources().getIntArray(R.array.photo_placeholder_bg_color);
	}
	
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}


	@Override
	public long getItemId(int position) {
		return position;
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder viewHolder;
		String path = list.get(position).getImageUri();
		
		if(convertView == null){
			convertView = mInflater.inflate(R.layout.grid_image_item, null);
			viewHolder = new ViewHolder();
			viewHolder.mImageView = (ImageView) convertView.findViewById(R.id.child_image);
			viewHolder.mCheckBox = (CheckBox) convertView.findViewById(R.id.child_checkbox);
			viewHolder.mToggleButton = (ToggleButton) convertView.findViewById(R.id.toggle_button);
			convertView.setTag(viewHolder);
			
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
			viewHolder.mImageView.setImageDrawable(null);
		}
		viewHolder.mImageView.setBackgroundColor(getBeautifulBackgroundColor(position));
		
		viewHolder.mImageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				selectedImage.changeItemState(list.get(position));
				viewHolder.mToggleButton.setChecked(list.get(position).getSelectd());
				//addAnimation(viewHolder.mCheckBox);
				((ChooseImageActivity)mContext).changeTitleCount();
			}
		});
//
//		viewHolder.mCheckBox.setClickable(false);
//		viewHolder.mCheckBox.setChecked(list.get(position).getSelectd());
		viewHolder.mToggleButton.setClickable(false);
		viewHolder.mToggleButton.setChecked(list.get(position).getSelectd());
		ImageLoader.getInstance().displayImage(path, viewHolder.mImageView, options);
		
		
		return convertView;
	}
	
	/**
	 * @param view
	 */
	public void addAnimation(View view){
		float [] vaules = new float[]{0.5f, 0.6f, 0.7f, 0.8f, 0.9f, 1.0f, 1.1f, 1.2f, 1.3f, 1.25f, 1.2f, 1.15f, 1.1f, 1.0f};
		AnimatorSet set = new AnimatorSet();
		set.playTogether(ObjectAnimator.ofFloat(view, "scaleX", vaules), 
				ObjectAnimator.ofFloat(view, "scaleY", vaules));
				set.setDuration(150);
		set.start();
	}
	
	
	public static class ViewHolder{
		public ImageView mImageView;
		public CheckBox mCheckBox;
		public ToggleButton mToggleButton;
	}

	private int getBeautifulBackgroundColor(int position) {
    	int i = position % PLACEHOLDER_BACKGROUND_COLOR_SIZE;
    	return mPhotoPlaceholderColor[i];
    }

}
