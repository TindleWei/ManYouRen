/**   
 * @Title: AlbumAddActivity.java 
 * @Package com.manyouren.android.ui.user 
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author ssz 31807077_qq_com   
 * @date 2014-7-14 下午4:58:05 
 * @version V1.0   
 */
package com.manyouren.manyouren.ui.user;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.TimeZone;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.kevinsawicki.wishlist.Toaster;
import com.loopj.android.http.RequestParams;
import com.manyouren.manyouren.R;
import com.manyouren.manyouren.base.BaseActivity;
import com.manyouren.manyouren.config.Constants;
import com.manyouren.manyouren.core.user.Album;
import com.manyouren.manyouren.core.user.PicAdapter;
import com.manyouren.manyouren.service.AsyncHttpHandler;
import com.manyouren.manyouren.service.AsyncHttpManager;
import com.manyouren.manyouren.service.PreferenceHelper;
import com.manyouren.manyouren.util.Logot;
import com.manyouren.manyouren.util.PhotoUtils;

/**
 * @包名: com.manyouren.android.ui.user
 * @描述: TODO(这里用一句话描述这个类的作用)
 * @作者 ssz 31807077_qq_com
 * @日期 2014-7-14 下午4:58:05
 * @版本 V 1.0
 * 
 */
public class AlbumAddActivity extends BaseActivity {

	private Context context;
	private AlbumAddActivity instance;
	MyHandler handler;

	ListItemGridView picgrid;
	EditText et_description;
	RadioGroup rg_seek;
	RadioGroup rg_rate;
	RatingBar rb_rating;
	RelativeLayout layout_plan_map;
	TextView tv_loc;

	ArrayList<String> pics;
	private int photoIndex = 0;
	private String photoPath;
	PicAdapter picAdapter;

	String content = "";
	float rating = 0;

	/**
	 * 地址信息
	 */
	double selLat = 0;
	double selLng = 0;
	String loc_name = "";
	String city = Constants.PLACE_NOW;
	String addr = "";
	String pguid = "";

	private static final int MENUITEM_ALBUMADD_OK = 23001;
	private static final int POI_SELECTED = 23007;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_album_add);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		context = this;
		instance = this;

		setActionBar("发布照片");

		initView();
		initEvent();
		init();

	}

	@Override
	protected void init() {
		handler = new MyHandler(AlbumAddActivity.this);
	}

	@Override
	protected void initView() {

		picgrid = (ListItemGridView) findViewById(R.id.picgrid);
		et_description = (EditText) findViewById(R.id.et_description);
		rg_seek = (RadioGroup) findViewById(R.id.rg_seek);
		rg_rate = (RadioGroup) findViewById(R.id.rg_rate);
		rb_rating = (RatingBar) findViewById(R.id.rb_rating);
		layout_plan_map = (RelativeLayout) findViewById(R.id.layout_plan_map);
		tv_loc = (TextView) findViewById(R.id.tv_loc);
	}

	@Override
	protected void initEvent() {
		pics = new ArrayList<String>();
		pics.add("");

		picAdapter = new PicAdapter(context, pics, "filepath");
		picAdapter.setDelEnable(true);
		picgrid.setAdapter(picAdapter);

		picgrid.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int pos,
					long id) {
				// TODO Auto-generated method stub
				PhotoUtils.selectPhoto(instance);
				photoIndex = pos;
			}

		});

		layout_plan_map.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivityForResult(new Intent(context,
						PoiListActivity.class), POI_SELECTED);
			}

		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == RESULT_OK) {

			switch (requestCode) {
			case PhotoUtils.INTENT_REQUEST_CODE_ALBUM:

				Uri uri = data.getData();
				ContentResolver cr = this.getContentResolver();

				Bitmap bitmap = PhotoUtils.getBitmapFromUri(cr, uri);
				if (bitmap != null) {
					String path = PhotoUtils.savePhotoToSDCard(bitmap);
					PhotoUtils.fliterPhoto(context, instance, path);
					// PhotoUtils.cropPhoto(context, instance, path);
				}
				break;

			case PhotoUtils.INTENT_REQUEST_CODE_FLITER:

				photoPath = data.getStringExtra("path");

				pics.set(photoIndex, photoPath);
				// if(pics.size()<9 && photoIndex==pics.size()-1)
				// pics.add("");
				//
				if (picAdapter != null) {
					picAdapter.notifyDataSetChanged();
				}
				break;

			case PhotoUtils.INTENT_REQUEST_CODE_CROP:
				break;
			case POI_SELECTED:
				Bundle b = data.getExtras();
				selLng = b.getDouble("lng", 0);
				selLat = b.getDouble("lat", 0);
				city = b.getString("city");
				addr = city + b.getString("addr");
				loc_name = b.getString("name");

				pguid = b.getString("guid");
				tv_loc.setText(loc_name);
				break;
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuItem done = menu.add(0, MENUITEM_ALBUMADD_OK, 0, "Upload");
		done.setIcon(this.getResources().getDrawable(
				R.drawable.ic_action_accept));
		done.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {
		switch (item.getItemId()) {
		// This is the home button in the top left corner of the screen.
		case android.R.id.home:
			this.finish();
			return true;
		case MENUITEM_ALBUMADD_OK:
			handleAlbumAdd();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/**
	 * 
	 * @Title: AlbumAddActivity
	 * @Description: TODO(上传照片)
	 * @param 设定文件
	 * @return AlbumAddActivity 返回类型
	 * @throws
	 */
	private void handleAlbumAdd() {
		showProgress();
		int isPublic = 1;
		if (!isValid(pics, content, isPublic, rating, city, loc_name)) {
			return;
		}
		rating = rb_rating.getRating();
		content = et_description.getText().toString().trim();

		String url = AsyncHttpManager.PHOTO_PUBLISH__URL;

		RequestParams params = new RequestParams();
		params.put("scenicId", "1");
		params.put("userId", PreferenceHelper.getUserId());
		params.put("content", content);
		params.put("score", String.valueOf(rating));
		params.put("tags", 1);
		params.put("images", "");

		AsyncHttpManager.getClient(context).post(url, params,
				new AsyncHttpHandler() {
					@Override
					public void onFailured() {
						super.onFailured();

						hideProgress();
						Toaster.showShort(AlbumAddActivity.this, "上传失败!");
					}

					@Override
					public void onSuccessed() {
						super.onSuccessed();
						onPostSuccess();
					}
				});
	}

	/**
	 * Hide progress dialog
	 */
	protected void hideProgress() {
		dismissDialog(0);
	}

	/**
	 * Show progress dialog
	 */
	protected void showProgress() {
		showDialog(0);
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		final ProgressDialog dialog = new ProgressDialog(this);
		dialog.setMessage("正在上传");
		dialog.setIndeterminate(true);
		/*
		 * dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		 * dialog.setProgress(100);
		 */
		dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
			}
		});
		return dialog;
	}

	private boolean isValid(ArrayList<String> ps, String content, int isPublic,
			double rating, String city, String loc_name) {

		if (ps == null || ps.size() == 0 || ps.size() > 9) {
			Toast.makeText(context, "请选择照片!", Toast.LENGTH_SHORT).show();
		} else if (content.equals("")) {
			Toast.makeText(context, "请填写说明!", Toast.LENGTH_SHORT).show();
		} else if (city.equals("") || loc_name.equals("")) {
			Toast.makeText(context, "请选择地点!", Toast.LENGTH_SHORT).show();
		} else {
			return true;
		}
		return false;
	}

	class MyHandler extends Handler {

		WeakReference<AlbumAddActivity> mActivity;

		MyHandler(AlbumAddActivity activity) {

			mActivity = new WeakReference<AlbumAddActivity>(activity);

		}

		@Override
		public void dispatchMessage(Message msg) {
			final AlbumAddActivity aActivity = mActivity.get();
			switch (msg.what) {
			case -1: // fail
				break;
			case 0: // finish
				break;
			case 1: // success

				break;
			}
		}
	}

	public void onPostSuccess() {
		hideProgress();
		Toaster.showShort(AlbumAddActivity.this, "上传成功!");
		// to put user's plan to the top
		Album album = new Album();
		album.setId(0l);
		album.setUserId(Long.parseLong(Constants.USER_ID));
		album.setAlbumcity(city);
		album.setAlbumtype(1);
		album.setContent(content);
		SimpleDateFormat formatter = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss", Locale.CHINA);
		formatter.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
		long stime = System.currentTimeMillis();
		String pubDate = formatter.format(stime);
		album.setDateline(pubDate);
		album.setLikenum(0);
		album.setLocation(loc_name);

		ArrayList<String> upPics = pics;
		Log.i("AlbumAddActivity", "aActivity.pics:" + pics.toString());
		if (upPics != null && upPics.size() > 0 && upPics.size() < 9) {
			// upPics.remove(upPics.size()-1);
			Log.i("AlbumAddActivity", "upPics:" + pics.toString());
			try {
				JSONObject pic = new JSONObject();
				JSONArray origin = new JSONArray();
				for (int i = 0; i < upPics.size(); i++) {
					origin.put(upPics.get(i));
				}

				pic.put("thumb", origin);
				pic.put("origin", origin);
				Log.i("AlbumAddActivity", "pics:" + pic.toString());
				album.setPics(pic.toString());
			} catch (JSONException e) {
				Logot.outError("AlbumAddActivity", "return error:" + e.toString());
				album.setPics("[]");
				e.printStackTrace();
			}
		} else {
			album.setPics("[]");
		}

		album.setPicResource("filepath");
		album.setRating(rating);
		Intent i = new Intent(context, AlbumListActivity.class);
		i.putExtra("album", album);
		setResult(RESULT_OK, i);
		finish();
	}

}
