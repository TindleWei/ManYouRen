/**
 * @Package com.manyouren.android.ui.user    
 * @Title: InfoEditActivity.java 
 * @Description: TODO
 * @author firefist_wei firefist.wei@gmail.com   
 * @date 2014-9-7 上午3:19:16 
 * @version V1.0   
 */
package com.manyouren.manyouren.ui.user;

import java.util.HashMap;

import roboguice.inject.InjectView;
import android.annotation.TargetApi;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.github.kevinsawicki.wishlist.Toaster;
import com.loopj.android.http.RequestParams;
import com.manyouren.manyouren.R;
import com.manyouren.manyouren.base.BaseActivity;
import com.manyouren.manyouren.config.Constants;
import com.manyouren.manyouren.controller.UserController;
import com.manyouren.manyouren.service.AsyncHttpHandler;
import com.manyouren.manyouren.service.AsyncHttpManager;
import com.manyouren.manyouren.service.PreferenceHelper;
import com.manyouren.manyouren.service.greendao.GreenUser;

/**
 * @Description: TODO
 * 
 * @author firefist_wei
 * @date 2014-9-7 上午3:19:16
 * 
 */
@TargetApi(11)
public class InfoEditActivity extends BaseActivity {

	private String title = "";
	private String key = "";
	private String value = "";

	@InjectView(R.id.et_content)
	EditText et_content;

	@InjectView(R.id.tv_tip)
	TextView tv_tip;

	HashMap<String, Object> hashMap = null;

	private String finalValue = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_info_editor);

		title = getStringExtra("title");
		key = getStringExtra("key");
		value = getStringExtra("value");

		setActionBar(title);

		init();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		MenuItem done = menu.add(0, 10001, 0, "保存");
		done.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case 10001:
			finalValue = et_content.getText().toString().trim();
			if (finalValue.equals("")) {
				Toaster.showShort(InfoEditActivity.this, "输入为空");
				return true;
			} else if (finalValue.equals(value)) {
				Toaster.showShort(InfoEditActivity.this, "值没有改变");
				return true;
			}

			postInfoRequest();

			return true;
		case android.R.id.home:

			finish();
			overridePendingTransition(R.anim.right_in, R.anim.right_out);
			return true;

		default:
			return true;
		}
	}

	@Override
	protected void init() {

		hashMap = new HashMap<String, Object>();

		et_content.setText(value);

		if (key.equals("username")) {

			tv_tip.setText("好名字可以让你的朋友更容易记住你");

		} else if (key.equals("want2Go")) {

			tv_tip.setText("写下你想去的地方，也许你能遇到另一个自己");

		} else if (key.equals("beenThere")) {

			tv_tip.setText("别人可以更容易与你找到话题哦");

		} else if (key.equals("hobbyText")) {

			tv_tip.setText("也许有人也有和你相同的癖好");

		} else if (key.equals("vehicleText")) {

			tv_tip.setText("是一起骑车呢还是找个人付油钱");

		} else if (key.equals("signText")) {

			tv_tip.setText("关于旅行，想说什么说什么吧");

		}
	}

	@Override
	protected void initView() {

	}

	@Override
	protected void initEvent() {

	}

	public void postInfoRequest() {
		String url = AsyncHttpManager.USER_CHANGE_INFO_URL;

		RequestParams params = new RequestParams();
		params.put("userId", PreferenceHelper.getUserId());
		params.put(key, et_content.getText().toString().trim());

		AsyncHttpManager.getClient(context).post(url, params,
				new AsyncHttpHandler() {
					@Override
					public void onFailured() {
						super.onFailured();
						Toaster.showShort(InfoEditActivity.this, "更新失败");
						if (pd != null && pd.isShowing())
							pd.dismiss();
					}

					@Override
					public void onSuccessed() {
						super.onSuccessed();
						Toast.makeText(context, "更新成功！", 1000).show();

						// save in greendao
						de.greenrobot.daoexample.User user = GreenUser
								.getInstance(context).getUserById(
										Long.valueOf(Constants.USER_ID));
				

						if (key.equals("username")) {
							user.setUserName(finalValue);

						} else if (key.equals("want2Go")) {
							user.setWant2Go(finalValue);

						} else if (key.equals("beenThere")) {
							user.setBeenThere(finalValue);

						} else if (key.equals("hobbyText")) {
							user.setHobbyText(finalValue);

						} else if (key.equals("vehicleText")) {
							user.setVehicle(finalValue);

						} else if (key.equals("signText")) {
							user.setSignText(finalValue);
						}
						GreenUser.getInstance(context).saveUser(user);

						finish();
						overridePendingTransition(R.anim.left_in,
								R.anim.left_out);
					}
				});
		}

}
