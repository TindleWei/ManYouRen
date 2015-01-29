package com.manyouren.manyouren.service;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.manyouren.manyouren.RootApplication;
import com.manyouren.manyouren.util.Logot;

public class AsyncHttpHandler extends AsyncHttpResponseHandler {

	public static final String TAG = "AsyncHttpHandler";
	
	public String result = "";
	
	public String getResult(){
		return result;
	}
	
	public void setResult(String result){
		this.result = result;
	}
	
	@Override
	public void onFailure(int code, Header[] header, byte[] bytes,
			Throwable throwable) {
		//这个直接调用就可以了, 将来做进一步处理
		onFailured();
	}

	@Override
	public void onSuccess(int code, Header[] header, byte[] bytes) {
		String result = new String(bytes);
		Logot.outError(TAG, "result: " + result);
		
		try {
			JSONObject response = new JSONObject(result);
			if(response.getInt("errorCode") == 0){
				setResult(response.toString());
				onSuccessed();
				
			}else if(response.getInt("errorCode")==3){  //数据为空
				onEmpty();
				
			}else{ //处理各种错误
				onHandleError(response.getInt("errorCode"));
				onError();
			}
		} catch (JSONException e) {
			e.printStackTrace();
			onError();
		}
	}
	
	private void onHandleError(int code) {
		String errorMsg= "";
		switch(code){
		case 1: //缺少参数或者参数非法
			errorMsg = "缺少参数或者参数非法";
			break;
		case 2: //用户名或者密码错误
			errorMsg = "用户名或者密码错误";
			break;
		case 4: //致命错误。这一般是由服务器造成的
			errorMsg = "好友之间没有关系";
			break;
		case 6: //邮箱已经被注册
			errorMsg = "邮箱已被注册";
			break;
		case 7: //对方已经将您添加到黑名单
			errorMsg = "对方已经将您添加到黑名单";
			break;
		}
		Toast.makeText(RootApplication.getInstance(), errorMsg, 1000).show();
	}
	
	public void onEmpty(){
		
	}
	
	public void onSuccessed(){
		
	}
	
	public void onFailured(){
		//处理访问网络的错误
	}

	public void onError(){
		//处理业务逻辑的error
	}
}
