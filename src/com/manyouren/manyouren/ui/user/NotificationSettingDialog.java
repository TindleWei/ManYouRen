/**
* @Package com.manyouren.android.ui.plan    
* @Title: DialogPlanFilter.java 
* @Description: TODO
* @author firefist_wei firefist.wei@gmail.com   
* @date 2014-6-23 下午12:27:57 
* @version V1.0   
*/
package com.manyouren.manyouren.ui.user;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.manyouren.manyouren.R;
import com.manyouren.manyouren.baseold.BaseDialog;
import com.manyouren.manyouren.util.PreferenceUtils;

/** 
 * @Description: TODO
 *
 * @author firefist_wei
 * @date 2014-6-23 下午12:27:57 
 *  
 */
public abstract class NotificationSettingDialog extends BaseDialog{
	
	RadioGroup dialog_rg_notify;
	CheckBox dialog_cb_rington;
	CheckBox dialog_cb_vibrat;
	RadioButton dialog_rb_notifyon;
	RadioButton dialog_rb_notifyoff;
	Button dialog_notify_cancel;
	Button dialog_notify_submit;
	
	boolean isNotify = false;
	boolean isRington = false;
	boolean isVibrat = false;
	
	Context context;
	
	public NotificationSettingDialog(Context context) {
		super(context);
		setContentView(R.layout.dialog_notification_setting);
		
		this.context = context;
		
		isNotify = PreferenceUtils.getBoolean(context, "notify");
		isRington = PreferenceUtils.getBoolean(context, "rington");
		isVibrat = PreferenceUtils.getBoolean(context, "vibrat");
		
		initView();
		
		initEvent();
	}
	/** 
	* @Description: TODO
	*
	* @return void
	*/
	private void initEvent() {
		dialog_rg_notify.setOnCheckedChangeListener(new OnCheckedChangeListener(){

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				switch(checkedId){
				case R.id.dialog_rb_notifyon:
					isNotify = true;
					dialog_cb_rington.setEnabled(true);
					dialog_cb_vibrat.setEnabled(true);
					break;
				case R.id.dialog_rb_notifyoff:
					isNotify = false;
					dialog_cb_rington.setEnabled(false);
					dialog_cb_vibrat.setEnabled(false);
					break;
				}
			}
			
		});
		
		dialog_cb_rington.setOnCheckedChangeListener(new android.widget.CompoundButton.OnCheckedChangeListener(){

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				isRington = isChecked;
			}
			
		});
		
		dialog_cb_vibrat.setOnCheckedChangeListener(new android.widget.CompoundButton.OnCheckedChangeListener(){

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				isVibrat = isChecked;
			}
			
		});
		
		dialog_notify_cancel.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dismiss();
			}
			
		});
		
		dialog_notify_submit.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				PreferenceUtils.putBoolean(context, "notify", isNotify);
				PreferenceUtils.putBoolean(context, "rington", isRington);
				PreferenceUtils.putBoolean(context, "vibrat", isVibrat);
				invalidate();
				dismiss();
			}
			
		});
	}
	
	public abstract void invalidate();

	/** 
	* @Description: TODO
	*
	* @return void
	*/
	private void initView() {
		dialog_rg_notify = (RadioGroup)findViewById(R.id.dialog_rg_notify);
		dialog_cb_rington = (CheckBox)findViewById(R.id.dialog_cb_rington);
		dialog_cb_vibrat = (CheckBox)findViewById(R.id.dialog_cb_vibrat);
		dialog_rb_notifyon = (RadioButton)findViewById(R.id.dialog_rb_notifyon);
		dialog_rb_notifyoff = (RadioButton)findViewById(R.id.dialog_rb_notifyoff);
		dialog_notify_cancel = (Button)findViewById(R.id.dialog_notify_cancel); 
		dialog_notify_submit = (Button)findViewById(R.id.dialog_notify_submit);
		
		if(!isNotify){
			dialog_rb_notifyoff.setChecked(true);
			dialog_cb_rington.setEnabled(false);
			dialog_cb_vibrat.setEnabled(false);
		}else{
			dialog_rb_notifyon.setChecked(true);
			dialog_cb_rington.setEnabled(true);
			dialog_cb_vibrat.setEnabled(true);
		}
		
		if(isRington)
			dialog_cb_rington.setChecked(true);
		else
			dialog_cb_rington.setChecked(false);
		
		if(isVibrat)
			dialog_cb_vibrat.setChecked(true);
		else
			dialog_cb_vibrat.setChecked(false);
	}
}
