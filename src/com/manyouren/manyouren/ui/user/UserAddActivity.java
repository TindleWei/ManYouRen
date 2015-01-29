/**
* @Package com.manyouren.android.ui.user    
* @Title: UserAddActivity.java 
* @Description: TODO
* @author firefist_wei firefist.wei@gmail.com   
* @date 2014-7-18 下午1:25:16 
* @version V1.0   
*/
package com.manyouren.manyouren.ui.user;

import android.os.Bundle;

import com.manyouren.manyouren.R;
import com.manyouren.manyouren.baseold.BaActivity;

/** 
 * @Description: TODO
 *
 * @author firefist_wei
 * @date 2014-7-18 下午1:25:16 
 *  
 */
public class UserAddActivity extends BaActivity{
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_adduser);
		
		getActionBar().setTitle("添加朋友");
		getActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.actionbar_bg));
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	protected void initViews() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void initEvents() {
		// TODO Auto-generated method stub
		
	}

}
