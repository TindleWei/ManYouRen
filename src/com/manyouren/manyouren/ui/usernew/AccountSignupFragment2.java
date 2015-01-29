/**
 * @Package com.manyouren.android.ui.user    
 * @Title: SignupSlideFragment.java 
 * @Description: TODO
 * @author firefist_wei firefist.wei@gmail.com   
 * @date 2014-8-12 下午3:14:27 
 * @version V1.0   
 */
package com.manyouren.manyouren.ui.usernew;

import java.util.Calendar;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import com.manyouren.manyouren.R;
import com.manyouren.manyouren.ui.user.CityListActivity;
import com.manyouren.manyouren.util.Logot;

/**
 * @Description: TODO
 * 
 * @author firefist_wei
 * @date 2014-8-12 下午3:14:27
 * 
 */
@TargetApi(11)
public class AccountSignupFragment2 extends Fragment implements OnClickListener {

	public static final String ARG_PAGE = "page";

	static EditText et_liveland;

	private Button btn_done;

	private Context context = null;

	/*
	 * private String mHeadPath = Environment.getExternalStorageDirectory()
	 * .toString() + File.separator + "ManYouRen/My/myhead.jpg";
	 */

	public static AccountSignupFragment2 create(int pageNumber) {

		AccountSignupFragment2 fragment = new AccountSignupFragment2();
		Bundle args = new Bundle();
		args.putInt(ARG_PAGE, pageNumber);
		fragment.setArguments(args);

		return fragment;
	}

	public AccountSignupFragment2() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = getActivity();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		Logot.outError("fragment2", "on Resume");
		et_liveland = (EditText) getActivity().findViewById(R.id.et_liveland);
		et_liveland.setOnClickListener(this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ViewGroup rootView = null;

		rootView = (ViewGroup) inflater.inflate(
				R.layout.fragment_signup_slide_3, container, false);

		if (et_liveland == null)
			et_liveland = (EditText) rootView.findViewById(R.id.et_liveland);

		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		initEvent();
	}

	private void initEvent() {
		et_liveland.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Logot.outError("onCLick", "v id "+ v.getId());		
		switch (v.getId()) {
		
		case R.id.et_liveland:
			startActivityForResult(new Intent(getActivity(),
					CityListActivity.class), 31);
			getActivity().overridePendingTransition(R.anim.left_in,
					R.anim.left_out);

			break;
		}
	}

	public static void myActivityResult(int requestCode, int resultCode,
			Intent data) {
		Logot.outError("Tag", "fuck");
		try {
			et_liveland.setText(data.getStringExtra("CityName"));
			AccountSignupActivity.liveland = et_liveland.getText().toString();
			Logot.outError("Tag", "fuck1");

		} catch (Exception e) {
			Logot.outError("Tag", "fuck2");

		}

	}

}
