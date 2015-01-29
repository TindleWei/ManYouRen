/**
 * @Package com.manyouren.android.ui.plan    
 * @Title: DialogPlanFilter.java 
 * @Description: TODO
 * @author firefist_wei firefist.wei@gmail.com   
 * @date 2014-6-23 下午12:27:57 
 * @version V1.0   
 */
package com.manyouren.manyouren.ui.plan;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.loopj.android.http.RequestParams;
import com.manyouren.manyouren.R;
import com.manyouren.manyouren.baseold.BaseDialog;
import com.manyouren.manyouren.config.PreferConfig;
import com.manyouren.manyouren.service.AsyncHttpHandler;
import com.manyouren.manyouren.service.AsyncHttpManager;
import com.manyouren.manyouren.service.PreferenceHelper;
import com.manyouren.manyouren.util.PreferenceUtils;

/**
 * 
 * @author firefist_wei
 * @date 2014-6-23 下午12:27:57
 * 
 */
public class PlanFilterDialog extends BaseDialog {

	RadioGroup rg_gender, rg_place;

	String str_gender, str_place;

	int month, day;

	Button btn_month;

	ImageButton ib_month_left, ib_month_right;
	
	Button btn_day;

	ImageButton ib_day_left, ib_day_right;

	Button btn_cancel, btn_submit;

	Context context = null;
	Handler handler = null;

	Date date;

	public PlanFilterDialog(Context context,
			Handler handler) {
		super(context);
		setContentView(R.layout.dialog_plan_filter);

		this.context = context;
		this.handler = handler;

		date = new Date();
		String month_str = new SimpleDateFormat("MM").format(date);
		String day_str = new SimpleDateFormat("dd").format(date);  
		month = Integer.parseInt(month_str);
		day = Integer.parseInt(day_str);

		initView();

		initEvent();
	
	}
	
	private void initView() {
		rg_gender = (RadioGroup) findViewById(R.id.dialog_rg_gender);
		rg_place = (RadioGroup) findViewById(R.id.dialog_rg_place);

		btn_cancel = (Button) findViewById(R.id.dialog_filter_cancel);
		btn_submit = (Button) findViewById(R.id.dialog_filter_submit);

		ib_month_left = (ImageButton) findViewById(R.id.dialog_month_left);
		ib_month_right = (ImageButton) findViewById(R.id.dialog_month_right);

		btn_month = (Button) findViewById(R.id.dialog_btn_month);
		
		ib_day_left = (ImageButton) findViewById(R.id.dialog_day_left);
		ib_day_right = (ImageButton) findViewById(R.id.dialog_day_right);

		btn_day = (Button) findViewById(R.id.dialog_btn_day);

		if (PlanFragment.FILTER_SEX == -1) {
			rg_gender.check(R.id.dialog_rb_gender0);
		} else if (PlanFragment.FILTER_SEX == 1) {
			rg_gender.check(R.id.dialog_rb_gender1);
		} else if (PlanFragment.FILTER_SEX == 0) {
			rg_gender.check(R.id.dialog_rb_gender2);
		}

		if (PlanFragment.FILTER_LOC == -1) {
			rg_place.check(R.id.dialog_rb_place0);
		} else if (PlanFragment.FILTER_LOC == 0) {
			rg_place.check(R.id.dialog_rb_place1);
		} else if (PlanFragment.FILTER_LOC == 1) {
			rg_place.check(R.id.dialog_rb_place2);
		}

		btn_month.setText(month + "月");
		btn_day.setText(day + "日");

	}


	private void initEvent() {

		ib_month_left.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				month--;
				if (month == 0) {
					month = 1;
				}
				btn_month.setText(month + "月");

				PlanFragment.FILTER_MONTH = month;
			}
		});

		ib_month_right.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				month++;
				if (month == 13) {
					month = 12;
				}
				btn_month.setText(month + "月");

				PlanFragment.FILTER_MONTH = month;
			}
		});
		
		ib_day_left.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				day--;
				if (day == 0) {
					day = 1;
				}
				btn_day.setText(day + "日");

			}
		});

		ib_day_right.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				day++;
				if (day > 31) {
					day = 1;
				}
				btn_day.setText(day + "日");
				
			}
		});


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
								PreferConfig.PREFER_PLAN_FILTER_SEX, -1);
						PlanFragment.FILTER_SEX = -1;

					} else if (str_gender.equals("男")) {
						PreferenceUtils.putInt(context,
								PreferConfig.PREFER_PLAN_FILTER_SEX, 1);
						PlanFragment.FILTER_SEX = 1;

					} else if (str_gender.equals("女")) {
						PreferenceUtils.putInt(context,
								PreferConfig.PREFER_PLAN_FILTER_SEX, 0);
						PlanFragment.FILTER_SEX = 0;

					}
				}

				if (str_place != null) {
					if (str_place.equals("全部")) {
						PreferenceUtils.putInt(context,
								PreferConfig.PREFER_PLAN_FILTER_LOC, -1);
						PlanFragment.FILTER_LOC = -1;

					} else if (str_place.equals("当地")) {
						PreferenceUtils.putInt(context,
								PreferConfig.PREFER_PLAN_FILTER_LOC, 0);
						PlanFragment.FILTER_LOC = 0;

					} else if (str_place.equals("外地")) {
						PreferenceUtils.putInt(context,
								PreferConfig.PREFER_PLAN_FILTER_LOC, 1);
						PlanFragment.FILTER_LOC = 1;
					}
				}
				
				handler.sendEmptyMessage(-1);
				postRequest();
				dismiss();
			}
		});
	}
	
	private void postRequest() {
		String url = AsyncHttpManager.PLAN_FILTER_URL;

		RequestParams params = new RequestParams();
		params.put("userId", PreferenceHelper.getUserId());
		params.put("latitude", PreferenceHelper.getLatitude());
		params.put("longitude", PreferenceHelper.getLongitude());
		params.put("currentCity", PreferenceHelper.getCity());
		params.put("residence", PreferenceHelper.getResidence());
		
		if(PlanFragment.FILTER_SEX == -1){
			params.put("gender", "");
		}else if(PlanFragment.FILTER_SEX == 0){
			params.put("gender", "0");
		}else if(PlanFragment.FILTER_SEX == 1){
			params.put("gender", "1");
		}
		
		if(PlanFragment.FILTER_LOC == -1){
			params.put("residenceFlag", "");
		}else if(PlanFragment.FILTER_LOC == 0){
			params.put("residenceFlag", "0");
		}else if(PlanFragment.FILTER_LOC == 1){
			params.put("residenceFlag", "1");
		}

		if (PlanFragment.FILTER_DEST.equals(""))
			params.put("location", "");
		else {
			params.put("location", PlanFragment.FILTER_DEST);
		}

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
						PlanFragment.dialogJson = getResult();
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
