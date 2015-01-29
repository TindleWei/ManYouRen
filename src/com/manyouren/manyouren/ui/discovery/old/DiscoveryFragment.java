/**   
* @Title: PlanFragment.java 
* @Package com.manyouren.android.ui 
* @Description: TODO(用一句话描述该文件做什么) 
* @author firefist_wei firefist.wei@gmail.com   
* @date 2014-6-6 上午3:50:01 
* @version V1.0   
*/
package com.manyouren.manyouren.ui.discovery.old;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.manyouren.manyouren.R;
import com.manyouren.manyouren.baseold.BaFragment;

/** 
 * @ClassName: PlanFragment 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author firefist_wei firefist.wei@gmail.com
 * @date 2014-6-6 上午3:50:01 
 *  
 */
public class DiscoveryFragment extends BaFragment implements OnClickListener{

	LinearLayout layout_meidi;
	
	RelativeLayout layout_manyoudai;
	
	RelativeLayout layout_jianren;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_discovery, null);	
		
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		//initViews(),initEvents(),init() are all in there super class;
		

	}

	@Override
	protected void initViews() {
		layout_meidi = (LinearLayout)getActivity().findViewById(R.id.disc_item_meidi);
		layout_manyoudai = (RelativeLayout)getActivity().findViewById(R.id.disc_item_manyoudai);
		layout_jianren = (RelativeLayout)getActivity().findViewById(R.id.disc_item_jianren);
		
	}

	@Override
	protected void initEvents() {
		layout_meidi.setOnClickListener(this);
		layout_manyoudai.setOnClickListener(this);
		layout_jianren.setOnClickListener(this);		
	}

	@Override
	protected void init() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.disc_item_meidi:
			getActivity().startActivity(new Intent(getActivity(),BeautyPlaceActivity.class));
			getActivity().overridePendingTransition(R.anim.left_in, R.anim.left_out);
			break;
		case R.id.disc_item_manyoudai:
			getActivity().startActivity(new Intent(getActivity(),ManYouListActivity.class));
			getActivity().overridePendingTransition(R.anim.left_in, R.anim.left_out);
			break;
		case R.id.disc_item_jianren:
			getActivity().startActivity(new Intent(getActivity(),PeopleNearbyActivity.class));
			getActivity().overridePendingTransition(R.anim.left_in, R.anim.left_out);
			
			break;
		
		}
	}

}
