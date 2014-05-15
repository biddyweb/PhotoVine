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
import android.content.ActivityNotFoundException;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import android.net.Uri;
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
import android.widget.Toast;

/**
 * 
 */
public class MakeProgressFragment extends DialogFragment implements OnClickListener{

	private static String VIDEO_PATH = "video_path";
	public static String SHARE_TYPE = "share_type";
	public static int TYPE_SAVE = 0;
	public static final int TYPE_SHARE_INSTAGRAM = 1;
	public static final int TYPE_SHARE_FACEBOOK = 2;
	private ProgressBar progressBar;
	private int sucessedCount = 0;
	private TextView showProgress;
	private int allCount = 100;
	private Thread mThread;
	@Override
	public void onActivityCreated(Bundle arg0) {
		makePhotoProcess();
		super.onActivityCreated(arg0);
	}

	public static MakeProgressFragment createFragment(String videoPath, int shareType) {
		MakeProgressFragment frgment = new MakeProgressFragment();
		Bundle bd = new Bundle();
		bd.putString(VIDEO_PATH, videoPath);
		bd.putInt(SHARE_TYPE, shareType);
		frgment.setArguments(bd);
		return frgment;
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
        progressBar.setMax(100);
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
		
		mThread = new Thread(new Runnable() {
					
			@Override
			public void run() {
				for (int i=0; i<allCount-2; i++) {
					try {
						Thread.sleep(250);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					sendExcutedCountMessage();
				}
			}
		});
		mThread.start();
	}
	
	protected void sendExcutedCountMessage() {
	    sucessedCount = (1 + this.sucessedCount);
	    Message localMessage = this.mHandler.obtainMessage();
	    localMessage.what = 1;
	    this.mHandler.sendMessage(localMessage);
	}
	
	public void sendSuccessMessage() {
	    Message localMessage = this.mHandler.obtainMessage();
	    localMessage.what = 2;
	    this.mHandler.sendMessage(localMessage);
	}
	
	protected void loadingUpdate() {
	    this.showProgress.setText(sucessedCount + "/" + allCount);
	    this.progressBar.setProgress(sucessedCount);
	}
	
	@SuppressWarnings("deprecation")
	protected void successUpdate() {
		this.progressBar.setProgress(allCount);
		if (mThread.isAlive()) {
	    	//mThread.stop(new InterruptedException());
	    }
		((EditSlideShowActivity)getActivity()).finish();
	    dismiss();
	    share();
	}

	private void share() {
		String videoPath = null;
	    int shareType = 0;
	    if (getArguments() != null){
	    	videoPath =	getArguments().getString(VIDEO_PATH);
	    	shareType = getArguments().getInt(SHARE_TYPE);
	    }
	    switch (shareType) {
		case 0:
			Toast.makeText(getActivity(), videoPath, Toast.LENGTH_SHORT).show();
			break;
		case 1:
			try {
				startActivity(getShareIntent(videoPath, "com.instagram.android"));
			    return;
		    } catch (ActivityNotFoundException localActivityNotFoundException) {
		      
		    }
			break;
		case 2:
			try {
			    startActivity(getShareIntent(videoPath, "com.facebook.katana"));
			    return;
		    } catch (ActivityNotFoundException localActivityNotFoundException) {
		      
		    }
			break;

		default:
			break;
		}
	}

	private Intent getShareIntent(String videoPath, String packagename) {
	    Intent localIntent = new Intent("android.intent.action.SEND");
	    try {
	      localIntent.putExtra("android.intent.extra.STREAM", Uri.fromFile(new File(videoPath)));
	      localIntent.putExtra("android.intent.extra.TEXT", "#PhotoVine");
	      localIntent.setType("video/*");
	      if (packagename != null){
	        localIntent.setPackage(packagename);
	        localIntent.setFlags(32768);
	      }
	      return localIntent;
	      
	    } catch (Exception localException) {
	        localException.printStackTrace();
	    }
		return null;
	    
	  }
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
	}



	@Override
	public void onClick(DialogInterface dialog, int which) {
		
	} 

    


}
