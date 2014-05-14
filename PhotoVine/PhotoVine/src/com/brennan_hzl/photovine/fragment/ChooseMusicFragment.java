package com.brennan_hzl.photovine.fragment;

import static android.app.Activity.RESULT_CANCELED;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.brennan_hzl.photovine.R;
import com.brennan_hzl.photovine.bean.MusicBean;

import com.brennan_hzl.photovine.util.MusicInfoLoader;
import com.brennan_hzl.photovine.util.MusicInfoLoader.MusicInfoResponeHander;
import com.brennan_hzl.photovine.view.LightAlertDialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

import android.os.Bundle;

import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;

/**
 * 
 */
public class ChooseMusicFragment extends DialogFragment implements MusicInfoResponeHander, OnClickListener{

	private ListView mMusicList;
	private List<MusicBean> mAlbums;
	@Override
	public void onActivityCreated(Bundle arg0) {
		new MusicInfoLoader(getActivity()).requestAlbums(this);
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
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, activity.getString(R.string.cancel), this);
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, activity.getString(R.string.ok), this);
		
        LayoutInflater inflater = activity.getLayoutInflater();
        LinearLayout viewGroup = (LinearLayout)inflater.inflate(R.layout.dialog_choose_music, null);
        mMusicList = (ListView) viewGroup.findViewById(R.id.music_list);
        
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
	public void onViewCreated(View view, Bundle savedInstanceState) {
		
		super.onViewCreated(view, savedInstanceState);
	}

	@Override
	public void onSuccess(List<MusicBean> albums) {

		mAlbums = albums;
		setListAdpter(mAlbums);
		
	}

	@Override
	public void onFailed() {

	}

	public void setListAdpter(List<MusicBean> mp3Infos) {  
        List<HashMap<String, String>> mp3list = new ArrayList<HashMap<String, String>>();  
        for (Iterator<MusicBean> iterator = mp3Infos.iterator(); iterator.hasNext();) {  
        	MusicBean mp3Info = (MusicBean) iterator.next();  
            HashMap<String, String> map = new HashMap<String, String>();  
            map.put("title", mp3Info.getTitle());  
            mp3list.add(map);  
        }  
        SimpleAdapter mAdapter = new SimpleAdapter(getActivity(), mp3list,  
                R.layout.list_music_item, new String[] { "title" }, new int[] { R.id.music_title});  
        mMusicList.setAdapter(mAdapter); 
    }

	@Override
	public void onClick(DialogInterface dialog, int which) {
		Log.v("DialogInterface", " "+which);
		switch (which) {
		case DialogInterface.BUTTON_POSITIVE :
			
		}
		
	} 

    


}
