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
import android.content.DialogInterface.OnClickListener;
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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 
 */
public class ExcuteProgressFragment extends DialogFragment implements OnClickListener{

	private ProgressBar progressBar;
	private int sucessedCount = 0;
	private TextView showProgress;
	private List<ImageBean> mImages = SelectedImageBean.getInstance().getChoosedImages();
	private int allCount = mImages.size();
	@Override
	public void onActivityCreated(Bundle arg0) {
		makePhotoProcess();
		super.onActivityCreated(arg0);
	}

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
		
        LayoutInflater inflater = activity.getLayoutInflater();
        RelativeLayout viewGroup = (RelativeLayout)inflater.inflate(R.layout.dialog_excute_photo, null);
        
        progressBar = (ProgressBar) viewGroup.findViewById(R.id.progressBar);
        progressBar.setMax(mImages.size());
        showProgress = (TextView) viewGroup.findViewById(R.id.tv_load_message);
        
        loadingUpdate();
        
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
    
    private Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			
			switch (msg.what) {
			case 1:
				loadingUpdate();
				break;
			case 2:
				successUpdate();
				break;
			}
		}
		
	};
	
	private void makePhotoProcess() {
		
		new Thread(new Runnable() {
					
			@Override
			public void run() {
				//processPhoto(mImages.get(0).imagePath, String.format(Locale.US,"img%03d", 0));
				for (int i=0; i<allCount; i++) {
					String newName = String.format(Locale.US,"img%03d", i+1);
					processPhoto(mImages.get(i).imagePath, newName);
					sendExcutedCountMessage();
				}
				sendSuccessMessage();
			}
		}).start();
	}
	
	private void processPhoto(String uri, String newName) {
		Bitmap localBitmap1 = NativeImageLoader.decodeThumbBitmapForFile(uri, 640, 640);
		Bitmap localBitmap2 = Bitmap.createBitmap(640, 640, Bitmap.Config.ARGB_8888);
		Canvas localCanvas = new Canvas(localBitmap2);
		localCanvas.drawColor(-1);
		
		int width = localBitmap1.getWidth();
	    int height = localBitmap1.getHeight();
	    
	    Rect localRect1 = new Rect(0, 0, width, height);
	    Rect localRect2 = new Rect(0, 0, 640, 640);
	    
	    int m = 0;
	    if (width < 640) {
	        m = 320 - width / 2;
	        int i3 = 640 - m;
	        localRect2 = new Rect(m, 0, i3, 640);
	    } 
	    if (height < 640) {
	        int n = 320 - height / 2;
	        int i2 = 640 - n;
	        localRect2 = new Rect(0, n, 640, i2);
	    }
		
		localCanvas.drawBitmap(localBitmap1, localRect1, localRect2, new Paint(Paint.FILTER_BITMAP_FLAG));
		StoreDataUtil.saveImgFileToAppInternalStorage(localBitmap2,
				StoreDataUtil.getTempImagePathOfAppInternalStorage(getActivity()).getAbsolutePath() +File.separator + newName + "." + Bitmap.CompressFormat.JPEG);
		
		localBitmap1.recycle();
		localBitmap2.recycle();
	}
	
	protected void sendExcutedCountMessage() {
	    this.sucessedCount = (1 + this.sucessedCount);
	    Message localMessage = this.mHandler.obtainMessage();
	    localMessage.what = 1;
	    this.mHandler.sendMessage(localMessage);
	}
	
	protected void sendSuccessMessage() {
	    Message localMessage = this.mHandler.obtainMessage();
	    localMessage.what = 2;
	    this.mHandler.sendMessage(localMessage);
	}
	
	protected void loadingUpdate() {
	    this.showProgress.setText(sucessedCount + "/" + allCount);
	    this.progressBar.setProgress(sucessedCount);
	}
	
	protected void successUpdate() {
	    dismiss();
	    getActivity().startActivity(new Intent(getActivity(), EditSlideShowActivity.class));
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
	}



	@Override
	public void onClick(DialogInterface dialog, int which) {
		
	} 

    


}
