/**
  * @Package com.manyouren.android.core.chat    
 * @Title: PlanMessageItem.java 
 * @author firefist_wei firefist.wei@gmail.com   
 * @date 2014-7-17 下午1:12:27 
 * @version V1.0   
 */
package com.manyouren.manyouren.core.chat;

import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.manyouren.manyouren.R;
import com.manyouren.manyouren.controller.PlanController;
import com.manyouren.manyouren.entity.PlanEntity;
import com.manyouren.manyouren.ui.plan.PlanCommentActivity;
import com.manyouren.manyouren.util.DateUtils;
import com.manyouren.manyouren.util.JSONUtils;
import com.manyouren.manyouren.util.Logot;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @Description: TODO
 * 
 * @author firefist_wei
 * @date 2014-7-17 下午1:12:27
 * 
 */
public class PlanMessageItem extends MessageItem implements OnClickListener,
		OnLongClickListener {

	private ImageView plan_for;
	private ImageView plan_with;
	private ImageView plan_seek;
	private TextView plan_desti;
	private TextView plan_time;
	private TextView plan_postscript;

	PlanEntity planEntity = null;

	private Context mContext;

	/**
	 * msg.getContent() 解析数据 jsonString
	 * 
	 * @param msg
	 * @param context
	 */
	public PlanMessageItem(ChatMessage msg, Context context) {
		super(msg, context);
		mContext = context;
//		planEntity = parsePlanData(context, msg.getContent());
	}

	/**
	 * parse jsonString to PlanEntity
	 * 
	 * @param jsonString
	 * @return
	 * @return PlanEntity
	 */
	private PlanEntity parsePlanData(Context context, String jsonString) {

		try {
			JSONObject jsonObject = new JSONObject(jsonString);

			/*Logot.outInfo("PLAN", jsonString);
			Map<String, String> map = JSONUtils
					.parseKeyAndValueToMap(jsonObject);
			
			Map<String, String> map

			return PlanController.mapToPlan(context, map);*/

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected void onInitViews() {
		View view = mInflater.inflate(R.layout.message_plan, null);
		mLayoutMessageContainer.addView(view);
		plan_for = (ImageView) view.findViewById(R.id.iv_plan_for);
		plan_with = (ImageView) view.findViewById(R.id.iv_plan_with);
		plan_seek = (ImageView) view.findViewById(R.id.iv_plan_seek);

		plan_desti = (TextView) view.findViewById(R.id.tv_plan_desti);
		plan_time = (TextView) view.findViewById(R.id.tv_plan_time);
		plan_postscript = (TextView) view.findViewById(R.id.tv_plan_postscript);

		view.setOnClickListener(this);
		view.setOnLongClickListener(this);
	}

	@Override
	protected void onFillMessage() {

		plan_for.setImageResource(PlanController.res_for[Integer.valueOf(planEntity.getType())]);

		plan_with.setImageResource(PlanController.res_with[Integer.valueOf(planEntity
				.getTogether())]);
		plan_seek.setImageResource(PlanController.res_seek[Integer.valueOf(planEntity
				.getPurpose())]);

		plan_desti.setText(planEntity.getpName());
		plan_time.setText(DateUtils.getPlanDate(planEntity.getStartDate(),
				planEntity.getEndDate()));
		plan_postscript.setText(planEntity.getPostscript());

	}

	@Override
	public boolean onLongClick(View v) {
		super.onLongClick(v);
		return false;
	}

	@Override
	public void onClick(View v) {
		mContext.startActivity(new Intent(mContext, PlanCommentActivity.class)
				.putExtra("planId", "" + planEntity.getPlanId()).putExtra(
						"PlanEntity", planEntity));
	}

}
