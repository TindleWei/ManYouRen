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
import com.manyouren.manyouren.ui.user.ActivityForResultUtil;
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
public class AccountSignupFragment extends Fragment implements OnClickListener {

	public static final String ARG_PAGE = "page";

	private int mPageNumber;

	private EditText et_birthday;

	private EditText et_gender;

	public static EditText et_liveland;

	private Button btn_done;

	private Context context = null;

	/*
	 * private String mHeadPath = Environment.getExternalStorageDirectory()
	 * .toString() + File.separator + "ManYouRen/My/myhead.jpg";
	 */

	public static AccountSignupFragment create(int pageNumber) {

		AccountSignupFragment fragment = new AccountSignupFragment();
		Bundle args = new Bundle();
		args.putInt(ARG_PAGE, pageNumber);
		fragment.setArguments(args);

		return fragment;
	}

	public AccountSignupFragment() {
	}
	
	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mPageNumber = getArguments().getInt(ARG_PAGE);
		context = getActivity();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ViewGroup rootView = null;

		Logot.outError("TAG", mPageNumber + "!!!!!!");
		switch (mPageNumber) {
		case 1:

			rootView = (ViewGroup) inflater.inflate(
					R.layout.fragment_signup_slide_2, container, false);

			if (et_gender == null)
				et_gender = (EditText) rootView.findViewById(R.id.et_gender);
			if (et_birthday == null)
				et_birthday = (EditText) rootView
						.findViewById(R.id.et_birthday);

			break;
		case 2:

			rootView = (ViewGroup) inflater.inflate(
					R.layout.fragment_signup_slide_3, container, false);

			if (et_liveland == null)
				et_liveland = (EditText) rootView
						.findViewById(R.id.et_liveland);
			et_liveland.setOnClickListener(this);

			break;
		}

		return rootView;
	}

	public int getPageNumber() {
		return mPageNumber;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		initEvent(mPageNumber);
	}

	private void initEvent(int pageNumber) {

		switch (pageNumber) {
		case 1:
			et_birthday.setOnClickListener(this);
			et_gender.setOnClickListener(this);
			break;
		case 2:
			et_liveland.setOnClickListener(this);
			break;
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_avatar:

			new AlertDialog.Builder(context)
					.setTitle("修改头像")
					.setItems(new String[] { "拍照上传", "上传相册中的照片" },
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									Intent intent = null;
									switch (which) {
									case 0:
										break;
									case 1:
										intent = new Intent(Intent.ACTION_PICK,
												null);
										intent.setDataAndType(
												MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
												"image/*");
										startActivityForResult(
												intent,
												ActivityForResultUtil.REQUESTCODE_UPLOADAVATAR_LOCATION);
										break;
									}
								}
							}).create().show();

			break;

		case R.id.et_birthday:

			DialogDatePicker myDateFragment = new DialogDatePicker();
			myDateFragment.show(getActivity().getFragmentManager(), null);

			break;

		case R.id.et_gender:

			new AlertDialog.Builder(context)
					.setTitle("性别")
					.setSingleChoiceItems(new String[] { "女", "男" },
							AccountSignupActivity.gender,
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									if (which == 0) {
										et_gender.setText("女");
										AccountSignupActivity.gender = 0;
									} else {
										et_gender.setText("男");
										AccountSignupActivity.gender = 1;
									}
									dialog.dismiss();
								}
							}).show();
			break;

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

	@SuppressLint("ValidFragment")
	class DialogDatePicker extends DialogFragment {

		private Calendar calendar;

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			Dialog dialog = null;
			calendar = Calendar.getInstance();
			dialog = new DatePickerDialog(getActivity(),
					new DatePickerDialog.OnDateSetListener() {

						@Override
						public void onDateSet(DatePicker view, int year,
								int monthOfYear, int dayOfMonth) {

							String birthday = (year
									+ "-"
									+ ((monthOfYear + 1) < 10 ? "0"
											+ (monthOfYear + 1)
											: (monthOfYear + 1)) + "-" + (dayOfMonth < 10 ? "0"
									+ dayOfMonth
									: dayOfMonth));
							et_birthday.setText(birthday);
							AccountSignupActivity.birthday = birthday;
						}
					}, 1990, 0, 1);

			return dialog;
		}

	}

}
