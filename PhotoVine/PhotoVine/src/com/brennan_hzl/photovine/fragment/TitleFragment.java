package com.brennan_hzl.photovine.fragment;

import it.sephiroth.android.wheel.view.Wheel;
import it.sephiroth.android.wheel.view.Wheel.OnScrollListener;

import com.brennan_hzl.photovine.R;
import com.brennan_hzl.photovine.activity.EditSlideShowActivity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * 
 */
public class TitleFragment extends Fragment {

	private EditText Title;
	private EditText WaterMask;
	private ImageView back;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_title, null);
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
		Title = (EditText) view.findViewById(R.id.add_title);
		WaterMask = (EditText) view.findViewById(R.id.add_watermask);
		Title.addTextChangedListener(new MyTextWatcher());
		WaterMask.addTextChangedListener(new MyTextWatcher());
		
		super.onViewCreated(view, savedInstanceState);
	}

	private class MyTextWatcher implements TextWatcher {

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			
		}

		@Override
		public void afterTextChanged(Editable s) {
			((EditSlideShowActivity)getActivity()).setTitleAndWatermask(Title.getText().toString(), WaterMask.getText().toString());
		}
		
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		
		super.onActivityCreated(savedInstanceState);
	}
	

}
