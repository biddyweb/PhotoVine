package com.brennan_hzl.photovine.adapter;


import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import org.askerov.dynamicgid.BaseDynamicGridAdapter;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.brennan_hzl.photovine.R;
import com.brennan_hzl.photovine.bean.ImageBean;

import java.util.List;

/**
 * Author: speedinghzl
 * Date: 12/4/14
 * Time: 00:04 AM
 */
public class DynamicAdapter extends BaseDynamicGridAdapter<ImageBean> {
	private DisplayImageOptions options;
	private static int PLACEHOLDER_BACKGROUND_COLOR_SIZE = 6;
	private int[] mPhotoPlaceholderColor; 
	
    public DynamicAdapter(Context context, List<ImageBean> items, int columnCount) {
        super(context, items, columnCount);
        
        options = new DisplayImageOptions.Builder()
        .bitmapConfig(Bitmap.Config.RGB_565)
        .imageScaleType(ImageScaleType.EXACTLY)
        .cacheInMemory(true)
        .cacheOnDisc(true)
        .build();
        
        mPhotoPlaceholderColor = context.getResources().getIntArray(R.array.photo_placeholder_bg_color);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.grid_edit_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
            holder.image.setImageDrawable(null);
        }
        holder.image.setBackgroundColor(getBeautifulBackgroundColor(position));
        holder.build(getItem(position).getImageUri());
        return convertView;
    }

	private class ViewHolder {
        private ImageView image;

        private ViewHolder(View view) {
            image = (ImageView) view.findViewById(R.id.item_img);
        }

        void build(String uri) {
        	ImageLoader.getInstance().displayImage(uri, image, options);
        }
    }
	
	private int getBeautifulBackgroundColor(int position) {
    	int i = position % PLACEHOLDER_BACKGROUND_COLOR_SIZE;
    	return mPhotoPlaceholderColor[i];
    }
}