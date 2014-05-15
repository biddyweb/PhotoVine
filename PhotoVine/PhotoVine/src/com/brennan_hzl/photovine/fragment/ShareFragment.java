package com.brennan_hzl.photovine.fragment;

import java.io.File;
import java.util.List;
import java.util.Locale;


import com.brennan_hzl.photovine.R;



import com.brennan_hzl.photovine.activity.EditSlideShowActivity;
import com.brennan_hzl.photovine.bean.ImageBean;
import com.brennan_hzl.photovine.bean.SelectedImageBean;
import com.brennan_hzl.photovine.util.NativeImageLoader;
import com.brennan_hzl.photovine.util.StoreDataUtil;
import com.brennan_hzl.photovine.view.LightAlertDialog;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageSize;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

/**
 * 
 */
public class ShareFragment extends DialogFragment implements DialogInterface.OnClickListener{


	private ImageButton instagram;
	private ImageButton facebook;
	private ImageButton save;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
		final Activity activity = getActivity();
		
		final AlertDialog dialog = createDialog();
        //dialog.setIcon(R.drawable.ic_instagram);
        dialog.setTitle(null);
        dialog.setCanceledOnTouchOutside(false);
		
        View viewGroup = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_share, null);
        instagram = (ImageButton) viewGroup.findViewById(R.id.btn_instagram);
		facebook = (ImageButton) viewGroup.findViewById(R.id.btn_facebook);
		save = (ImageButton) viewGroup.findViewById(R.id.btn_save);
		instagram.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				((EditSlideShowActivity)getActivity()).createSlideshow(MakeProgressFragment.TYPE_SHARE_INSTAGRAM);
				dismiss();
			}
		});
		facebook.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				((EditSlideShowActivity)getActivity()).createSlideshow(MakeProgressFragment.TYPE_SHARE_FACEBOOK);
				dismiss();
			}
		});
		save.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				((EditSlideShowActivity)getActivity()).createSlideshow(MakeProgressFragment.TYPE_SAVE);
				dismiss();
			}

		});
        
        dialog.setView(viewGroup);
        
		return dialog;
	}
	
	/**
     * Create default dialog
     *
     * @return dialog
     */
    protected AlertDialog createDialog() {
        final AlertDialog dialog = LightAlertDialog.create(getActivity());
        
        dialog.setCancelable(true);
        dialog.setOnCancelListener(this);
        return dialog;
    }
	

	@Override
	public void onClick(DialogInterface dialog, int which) {
		
	} 

    


}
