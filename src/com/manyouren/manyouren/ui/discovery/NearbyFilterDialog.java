/**
 * @Package com.manyouren.android.ui.discovery    
 * @Title: NearbyFilterDialog.java 
 * @Description: TODO
 * @author firefist_wei firefist.wei@gmail.com   
 * @date 2014-7-30 上午9:50:42 
 * 
 * @version V1.0   
 */
package com.manyouren.manyouren.ui.discovery;

/** 
 * @Description: TODO
 *
 * @author firefist_wei
 * @date 2014-7-30 上午9:50:42 
 *  
 */

import java.util.Date;
import java.util.HashMap;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.github.kevinsawicki.wishlist.Toaster;
import com.loopj.android.http.RequestParams;
import com.manyouren.manyouren.R;
import com.manyouren.manyouren.baseold.BaseDialog;
import com.manyouren.manyouren.config.Constants;
import com.manyouren.manyouren.config.PreferConfig;
import com.manyouren.manyouren.controller.DiscoveryController;
import com.manyouren.manyouren.controller.PlanController;
import com.manyouren.manyouren.service.AsyncHttpHandler;
import com.manyouren.manyouren.service.AsyncHttpManager;
import com.manyouren.manyouren.service.PreferenceHelper;
import com.manyouren.manyouren.service.greendao.GreenUser;
import com.manyouren.manyouren.ui.discovery.old.PeopleNearbyActivity;
import com.manyouren.manyouren.ui.plan.PlanFragment;
import com.manyouren.manyouren.ui.plan.PlanFragmentOld;
import com.manyouren.manyouren.util.PreferenceUtils;

/**
 * @Description: TODO
 * 
 * @author firefist_wei
 * @date 2014-6-23 下午12:27:57
 * 
 */
public class NearbyFilterDialog extends BaseDialog {

	RadioGroup rg_gender, rg_place;

	String str_gender, str_place;

	Button btn_cancel, btn_submit;

	Context context = null;
	Handler handler = null;

	// ProgressBar progressBar = null;

	public NearbyFilterDialog(Context context,
			Handler handler) {
		super(context);
		setContentView(R.layout.dialog_nearby_filter);

		this.context = context;
		this.handler = handler;

		initView();

		initEvent();
	}

	/**
	 * @Description: TODO
	 * 
	 * @return void
	 */
	private void initEvent() {

		rg_gender.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				int id = group.getCheckedRadioButtonId();
				RadioButton rb = (RadioButton) findViewById(id);
				str_gender = rb.getText().toString();

			}
		});

		rg_place.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				int id = group.getCheckedRadioButtonId();
				RadioButton rb = (RadioButton) findViewById(id);
				str_place = rb.getText().toString();

			}
		});

		btn_cancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();

			}
		});

		btn_submit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				if (str_gender != null) {

					if (str_gender.equals("全部")) {
						PreferenceUtils.putInt(context,
								PreferConfig.PREFER_NEARBY_FILTER_SEX, -1);
						NearbyFragment.FILTER_SEX = -1;

					} else if (str_gender.equals("男")) {
						PreferenceUtils.putInt(context,
								PreferConfig.PREFER_NEARBY_FILTER_SEX, 1);
						NearbyFragment.FILTER_SEX = 1;

					} else if (str_gender.equals("女")) {
						PreferenceUtils.putInt(context,
								PreferConfig.PREFER_NEARBY_FILTER_SEX, 0);
						NearbyFragment.FILTER_SEX = 0;

					}
				}

				if (str_place != null) {
					if (str_place.equals("全部")) {
						PreferenceUtils.putInt(context,
								PreferConfig.PREFER_NEARBY_FILTER_LOC, -1);
						NearbyFragment.FILTER_LOC = -1;

					} else if (str_place.equals("当地")) {
						PreferenceUtils.putInt(context,
								PreferConfig.PREFER_NEARBY_FILTER_LOC, 0);
						NearbyFragment.FILTER_LOC = 0;

					} else if (str_place.equals("外地")) {
						PreferenceUtils.putInt(context,
								PreferConfig.PREFER_NEARBY_FILTER_LOC, 1);
						NearbyFragment.FILTER_LOC = 1;
					}
				}

				handler.sendEmptyMessage(-1);
				postRequest();
				dismiss();
			}
		});

	}

	/**
	 * 
	 * @return void
	 */
	private void initView() {
		rg_gender = (RadioGroup) findViewById(R.id.dialog_rg_gender);
		rg_place = (RadioGroup) findViewById(R.id.dialog_rg_place);

		btn_cancel = (Button) findViewById(R.id.dialog_filter_cancel);
		btn_submit = (Button) findViewById(R.id.dialog_filter_submit);

		if (NearbyFragment.FILTER_SEX == -1) {
			rg_gender.check(R.id.dialog_rb_gender0);
		} else if (NearbyFragment.FILTER_SEX == 1) {
			rg_gender.check(R.id.dialog_rb_gender1);
		} else if (NearbyFragment.FILTER_SEX == 0) {
			rg_gender.check(R.id.dialog_rb_gender2);
		}

		if (NearbyFragment.FILTER_LOC == -1) {
			rg_place.check(R.id.dialog_rb_place0);
		} else if (NearbyFragment.FILTER_LOC == 0) {
			rg_place.check(R.id.dialog_rb_place1);
		} else if (NearbyFragment.FILTER_LOC == 1) {
			rg_place.check(R.id.dialog_rb_place2);
		}
	}

	public void postRequest() {

		String url = AsyncHttpManager.NEARBY_PEOPLE_URL;
		RequestParams params = new RequestParams();
		params.put("latitude", PreferenceHelper.getLatitude());
		params.put("longitude", PreferenceHelper.getLongitude());
		params.put("currentCity", PreferenceHelper.getCity());

		params.put(
				"residence",
				GreenUser
						.getInstance(context)
						.getUserById(Long.valueOf(PreferenceHelper.getUserId()))
						.getResidence());
		params.put("userId", PreferenceHelper.getUserId());

		params.put("gender", NearbyFragment.FILTER_SEX == -1 ? ""
				: NearbyFragment.FILTER_SEX + "");
		if (NearbyFragment.FILTER_LOC == -1)
			;// hashMap.put("SearchForm[residence]", "");
		else
			params.put("residenceFlag", NearbyFragment.FILTER_LOC + "");

		AsyncHttpManager.getClient(context).post(url, params,
				new AsyncHttpHandler() {
					@Override
					public void onFailured() {
						super.onFailured();
						handler.sendEmptyMessage(0);
					}

					@Override
					public void onSuccessed() {
						super.onSuccessed();
						NearbyFragment.dialogJson = getResult();
						handler.sendEmptyMessage(1);
					}

					@Override
					public void onEmpty() {
						super.onEmpty();
						handler.sendEmptyMessage(2);
					}
				});
	}

}