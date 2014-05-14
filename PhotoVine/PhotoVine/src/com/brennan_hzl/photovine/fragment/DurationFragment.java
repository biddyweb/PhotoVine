package com.brennan_hzl.photovine.fragment;

import it.sephiroth.android.wheel.view.Wheel;
import it.sephiroth.android.wheel.view.Wheel.OnScrollListener;

import com.brennan_hzl.photovine.R;
import com.brennan_hzl.photovine.activity.EditSlideShowActivity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * 
 */
public class DurationFragment extends Fragment {

	private float duration;
	private Wheel mWheel;
	private CheckBox autoInstagram;
	private ImageView back;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_duration, null);
	}
	

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		back = (ImageView) view.findViewById(R.id.back);
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				getActivity().getSupportFragmentManager().popBackStack();
			}
		});
		autoInstagram = (CheckBox) view.findViewById(R.id.checkbox_auto_instagram);
		autoInstagram.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				((EditSlideShowActivity)getActivity()).setItemDuration(isChecked);
				mWheel.setEnabled(!isChecked);
			}
		});
		mWheel = (Wheel) view.findViewById(R.id.wheel);
		mWheel.setValue((duration*10-55)/45, true);
		mWheel.setOnScrollListener( new OnScrollListener() {

	        @Override
	        public void onScrollStarted( Wheel view, float value, int roundValue ) {
	        }

	        @Override
	        public void onScrollFinished( Wheel view, float value, int roundValue ) {
	        	
	        }

	        @Override
	        public void onScroll( Wheel view, float value, int roundValue ) {
	        	duration = ((float)(roundValue+55))/10;
	        	((EditSlideShowActivity)getActivity()).setItemDuration(duration);
	        }
	    } );
		super.onViewCreated(view, savedInstanceState);
	}


	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		
		super.onActivityCreated(savedInstanceState);
	}
	

}
