/**
 * @Package com.manyouren.android.service.http    
 * @Title: AsyncHttpLoader.java 
 * @Description: TODO
 * @author firefist_wei firefist.wei@gmail.com   
 * @date 2014-6-12 上午11:01:25 
 * @version V1.0   
 */
package com.manyouren.manyouren.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.manyouren.manyouren.config.PreferConfig;
import com.manyouren.manyouren.controller.BaseController;
import com.manyouren.manyouren.ui.usernew.AccountLoginActivity;
import com.manyouren.manyouren.util.Logot;
import com.manyouren.manyouren.util.NetworkUtils;
import com.manyouren.manyouren.util.PreferenceUtils;

/**
 * async-http-client
 * 
 * @author firefist_wei
 * @date 2014-6-12 上午11:01:25
 * 
 */
public class AsyncHttpLoader {

	private static AsyncHttpClient client = null;
	public static String SessionId = null;
	public static final String TAG = "AsyncHttpLoader";

	public static AsyncHttpClient getClient(Context context) {

		client = new AsyncHttpClient();
		client.setTimeout(5000);
		client.addHeader("User-Agent", "android");

		client.addHeader(
				"Cookie",
				"PHPSESSID="
						+ PreferenceUtils.getString(context,
								PreferConfig.PREFER_INITIAL_SESSIONID));
		Logot.outInfo(TAG, "Cookie: " + SessionId);

		return client;
	}

	public static AsyncHttpClient getClient(Context context, int backName) {

		client = new AsyncHttpClient();
		client.setTimeout(5000);
		client.addHeader("User-Agent", "android");

		if (backName == BaseController.CONTROLLER_LOGIN
				|| backName == BaseController.CONTROLLER_SIGNUP) {
			SessionId = null;
			Logot.outInfo(TAG, "Cookie: null");

		} else {
			client.addHeader(
					"Cookie",
					"PHPSESSID="
							+ PreferenceUtils.getString(context,
									PreferConfig.PREFER_INITIAL_SESSIONID));
			Logot.outInfo(TAG, "Cookie: " + SessionId);
		}
		return client;
	}

	/**
	 * Post请求数据
	 */
	public static void fetchRequest(final Context context, final String url,
			final HashMap<String, Object> hashMap, final Handler handler,
			final int backName) {

		if (checkNetwork(context) != true) {
			Toast.makeText(context, "网络未连接，请检查网络", 1500).show();
			/*
			 * handler.sendEmptyMessage(-1); return;
			 */
		}

		if (hashMap != null)
			Logot.outInfo(TAG, "上传参数： " + hashMap.toString());
		if (SessionId != null)
			Logot.outInfo(TAG, "SESSION: " + SessionId);

		Logot.outInfo(TAG, url);

		RequestParams params = new RequestParams();

		Logot.outInfo("BUG", "params before: " + params.toString());

		parseHashMap(hashMap, params);

		getClient(context, backName).post(url, params,
				new AsyncHttpResponseHandler() {

					@Override
					public void onFailure(int code, Header[] header,
							byte[] bytes, Throwable throwable) {

						if (throwable.getCause() instanceof ConnectTimeoutException) {
							System.out.println("Connection timeout !");
							Logot.outInfo(TAG, "timeout fail");
							if (handler != null)
								handler.sendEmptyMessage(-1);
							return;
						}
						Logot.outInfo(TAG, "post fail");
						if (handler != null)
							handler.sendEmptyMessage(-1);

					}

					@Override
					public void onSuccess(int code, Header[] header,
							byte[] bytes) {
						String str = new String(bytes);
						if (str != null)
							Logot.outError(TAG, str);
						try {
							JSONObject response = new JSONObject(str);

							Logot.outInfo(TAG, "post success");

							if (backName == BaseController.CONTROLLER_SIGNUP) {

								if (response.getInt("errorCode") == 4) {
									// 注册失败
									handler.sendEmptyMessage(4);
									return;
								} else if (response.getInt("errorCode") == 6) {
									// 邮箱已注册
									handler.sendEmptyMessage(6);
									return;
								}

							} else if (backName == BaseController.CONTROLLER_LOGIN) {

								if (response.getInt("errorCode") == 2) {
									// 用户名或密码错误
									handler.sendEmptyMessage(2);
									return;
								}
							}

							if (response.getInt("errorCode") == 0) {
								BaseController.handleBack(context, backName,
										response.toString());
								handler.sendEmptyMessage(1);
							} else if (response.getInt("errorCode") == 1) {
								// 缺少参数
								handler.sendEmptyMessage(-1);
							} else if (response.getInt("errorCode") == 2) {
								// 需要重新登陆
								reLogin(context);
							} else if (response.getInt("errorCode") == 3) {
								// {"message":"数据为空","errorCode":3}

								if (backName == BaseController.CONTROLLER_PLAN_FILTER) {
									BaseController.handleBack(context,
											backName, "");
								}
								handler.sendEmptyMessage(3);
							}

						} catch (JSONException e1) {
							e1.printStackTrace();
							Logot.outInfo(TAG, "请求异常 " + e1.toString());
							// 异常，当失败处理
							handler.sendEmptyMessage(-1);
						}
					}

					@Override
					public void onFinish() {
						super.onFinish();
						Logot.outInfo(TAG, "post finish");
						handler.sendEmptyMessage(0);
					}
				});
	}

	public class InterruptThread implements Runnable {

		Thread parent;
		Handler handler;

		public InterruptThread(Thread parent, Handler handler) {
			this.parent = parent;
			this.handler = handler;
		}

		public void run() {
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				if (handler != null)
					handler.sendEmptyMessage(-1);
			}

			if (handler != null)
				handler.sendEmptyMessage(-1);

			System.out
					.println("Timer thread forcing parent to quit connection");
			System.out
					.println("Timer thread closed connection held by parent, exiting");
		}
	}

	/**
	 * by get method
	 * 
	 */
	public static String getRequest(final Context context, final String url,
			final Handler handler, final int backName) {

		Logot.outInfo(TAG, "session: " + SessionId);
		getClient(context, backName).get(url, null,
				new JsonHttpResponseHandler() {

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONObject errorResponse) {
						super.onFailure(statusCode, headers, throwable,
								errorResponse);
						Logot.outInfo(TAG, "get fail");
						if (handler != null)
							handler.sendEmptyMessage(-1);
					}

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						super.onSuccess(statusCode, headers, response);

						Logot.outInfo(TAG, "get success");
						Logot.outInfo(TAG, response.toString());

						try {
							if (response.getInt("errorCode") == 0) {
								BaseController.handleBack(context, backName,
										response.toString());
								if (handler != null)
									handler.sendEmptyMessage(1);

							} else if (response.getInt("errorCode") == 2) {
								if (handler != null)
									reLogin(context);

							} else {
								if (handler != null)
									handler.sendEmptyMessage(-1);
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}

					@Override
					public void onFinish() {
						super.onFinish();
						Logot.outInfo(TAG, "get finish");
						if (handler != null)
							handler.sendEmptyMessage(0);
					}
				});
		return null;
	}

	private static void reLogin(Context context) {

		Toast.makeText(context, "用户验证失效，需要重新登录", 1000).show();

		PreferenceUtils.putBoolean(context,
				PreferConfig.PREFER_REQUEST_NEWACCOUNT, true);
		context.startActivity(new Intent(context, AccountLoginActivity.class));
	}

	/**
	 * 
	 * @return void
	 */
	private static boolean checkNetwork(Context context) {

		NetworkUtils.NetWorkState state = new NetworkUtils(context)
				.getConnectState();
		if (state.equals(NetworkUtils.NetWorkState.NONE)) {
			Logot.outInfo(TAG, "网络未连接");
			return false;
		} else {
			Logot.outInfo(TAG, "网络正常");
			return true;
		}
	}

	/**
	 * 这个方法暂时留着有用
	 * convert hashMap key and value to requestParams
	 * 
	 * @return void
	 */
	public static RequestParams parseHashMap(HashMap<String, Object> hashMap,
			RequestParams params) {

		if (hashMap != null) {
			Iterator<Map.Entry<String, Object>> it = hashMap.entrySet()
					.iterator();
			while (it.hasNext()) {
				Map.Entry<String, Object> entry = (Map.Entry<String, Object>) it
						.next();
				if (entry.getValue() instanceof java.io.File) {
					Logot.outInfo(TAG, "there ia a file: " + entry.getKey());
					try {
						params.put(entry.getKey(), (File) entry.getValue());

					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}
				} else if (entry.getValue() instanceof java.util.Date) {
					Logot.outInfo(TAG, "there ia a date: " + entry.getKey());
					params.put(entry.getKey(), (Date) entry.getValue());
				} else {
					params.put(entry.getKey(), entry.getValue());
				}
			}
		}
		return params;
	}

	/**
	 * 检查更新
	 * 
	 * @param handler
	 */
	public static void requestCheckVersion(JsonHttpResponseHandler handler) {

		Logot.outError("TAG", "requestCheckVersion");

		String action = "http://www.travelman.com.cn/server1/index.php/version";
		AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
		asyncHttpClient.setTimeout(5000);
		asyncHttpClient.get(action, handler);
	}

}
