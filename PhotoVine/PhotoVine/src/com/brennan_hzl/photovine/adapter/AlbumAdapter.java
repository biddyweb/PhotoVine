package com.brennan_hzl.photovine.adapter;

import java.util.List;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.brennan_hzl.photovine.R;
import com.brennan_hzl.photovine.bean.AlbumBean;
import com.brennan_hzl.photovine.bean.ImageBean;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class AlbumAdapter extends BaseAdapter{
	
	private static int PLACEHOLDER_BACKGROUND_COLOR_SIZE = 6;
	private List<AlbumBean> list;
	protected LayoutInflater mInflater;
	private int[] mPhotoPlaceholderColor; 
	private DisplayImageOptions options;
	private Context mContext;
	
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
	
	public AlbumAdapter(Context context, List<AlbumBean> list, GridView mGridView){
		mContext = context;
		this.list = list;
		mInflater = LayoutInflater.from(context);
		mPhotoPlaceholderColor = context.getResources().getIntArray(R.array.photo_placeholder_bg_color);
		options = new DisplayImageOptions.Builder() 
        .bitmapConfig(Bitmap.Config.RGB_565)
        .imageScaleType(ImageScaleType.EXACTLY)
        .cacheInMemory(true)
        .cacheOnDisc(true)
        .build();
	}
	

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder viewHolder;
		AlbumBean mAlbumBean = list.get(position);
		List<ImageBean> images= mAlbumBean.getImages();
		if(convertView == null){
			viewHolder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.grid_album_item, null);
			viewHolder.mImageViews[0] = (ImageView) convertView.findViewById(R.id.album_image1);
			viewHolder.mImageViews[1] = (ImageView) convertView.findViewById(R.id.album_image2);
			viewHolder.mImageViews[2] = (ImageView) convertView.findViewById(R.id.album_image3);
			viewHolder.mImageViews[3] = (ImageView) convertView.findViewById(R.id.album_image4);
			viewHolder.mTextViewTitle = (TextView) convertView.findViewById(R.id.album_name);
			viewHolder.mTextViewCounts = (TextView) convertView.findViewById(R.id.album_count);
			//resetImageViewLayoutParams(viewHolder.mImageViews);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
			for (int i= 0; i < viewHolder.mImageViews.length; i++) {
				viewHolder.mImageViews[i].setImageDrawable(null);
			}
		}
		
		viewHolder.mTextViewTitle.setText(mAlbumBean.getFolderName());
		viewHolder.mTextViewCounts.setText(String.format(mContext.getResources().getString(R.string.album_image_count), mAlbumBean.getImageCounts()));

		setImageViewBackgroundColor(viewHolder.mImageViews, position);
		setImageViewResource(viewHolder.mImageViews, images);
		
		return convertView;
	}
	
	
	
	public static class ViewHolder{
		public ImageView[] mImageViews = new ImageView[4];
		public TextView mTextViewTitle;
		public TextView mTextViewCounts;
	}

	private int getBeautifulBackgroundColor(int position) {
    	int i = position % PLACEHOLDER_BACKGROUND_COLOR_SIZE;
    	return mPhotoPlaceholderColor[i];
    }

//	public void resetImageViewLayoutParams(ImageView[] views) {
//		for (int i=0; i < views.length; i++) { 
//			LayoutParams lp = views[i].getLayoutParams();
//			lp.height = (Sconstants.AIBUM_GRIDVIEW_ITEM_WIDTH - 5 * 2)/2;
//		}
//	}
	
	public void setImageViewResource(ImageView[] views, List<ImageBean> images) {
		for (int i=0; i<views.length && i<images.size(); i++) { 
			ImageLoader.getInstance().displayImage(images.get(i).getImageUri(), views[i], options);
		}
		
	}
	
	public void setImageViewBackgroundColor(ImageView[] views, int position) {
		for (int i=0; i<views.length; i++) { 
			views[i].setBackgroundColor(getBeautifulBackgroundColor(position+i));
		}
	}

	
}
