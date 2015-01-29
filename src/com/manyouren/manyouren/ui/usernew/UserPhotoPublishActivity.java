package com.manyouren.manyouren.ui.usernew;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import roboguice.inject.InjectView;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.kevinsawicki.wishlist.Toaster;
import com.loopj.android.http.RequestParams;
import com.manyouren.manyouren.R;
import com.manyouren.manyouren.base.BaseActivity;
import com.manyouren.manyouren.core.user.PicAdapter;
import com.manyouren.manyouren.service.AsyncHttpHandler;
import com.manyouren.manyouren.service.AsyncHttpManager;
import com.manyouren.manyouren.service.PreferenceHelper;
import com.manyouren.manyouren.ui.user.ListItemGridView;
import com.manyouren.manyouren.ui.user.PoiListActivity;
import com.manyouren.manyouren.util.Logot;
import com.manyouren.manyouren.util.PhotoUtils;

public class UserPhotoPublishActivity extends BaseActivity {

	ListItemGridView picgrid;

	@InjectView(R.id.et_description)
	EditText et_description;
	
	@InjectView(R.id.tv_text_length)
	TextView tv_text_length;

	@InjectView(R.id.rg_tag)
	RadioGroup rg_rate;

	@InjectView(R.id.rb_rating)
	RatingBar rb_rating;

	@InjectView(R.id.layout_plan_map)
	RelativeLayout layout_plan_map;

	@InjectView(R.id.tv_loc)
	TextView tv_loc;

	ArrayList<String> pics;
	private int photoIndex = 0;
	private String photoPath;
	PicAdapter picAdapter;

	String content = "";
	float rating = 0;

	String loc_name = "";
	
	String tag="-1";
	static List<String> tags =new ArrayList<String>();
	static{
		tags.add("吃喝");
		tags.add("风景");
		tags.add("娱乐");
		tags.add("住宿");
	}

	private static final int MENUITEM_ALBUMADD_OK = 23001;
	private static final int POI_SELECTED = 23007;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_photo_publish);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

		setActionBar("发布照片");
		initView();
		initEvent();
		init();
	}

	@Override
	protected void initView() {
		picgrid = (ListItemGridView) findViewById(R.id.gv_addphoto);
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
				PhotoUtils.selectPhoto(activity);
				photoIndex = pos;
			}
		});

		layout_plan_map.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivityForResult(new Intent(context,
						PoiListActivity.class), POI_SELECTED);
			}
		});
		
		rg_rate.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				int id = group.getCheckedRadioButtonId();
				RadioButton rb = (RadioButton) findViewById(id);
				String code = rb.getText().toString();
				tag = tags.indexOf(code)+"";
			}
		});
		
		et_description.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				tv_text_length.setText(et_description.getText().toString()
						.length()
						+ "/150字");
			}
		});
	}

	@Override
	protected void init() {

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == RESULT_OK) {

			switch (requestCode) {
			case PhotoUtils.INTENT_REQUEST_CODE_ALBUM:

				Uri uri = data.getData();
				ContentResolver cr = this.getContentResolver();
				
				Log.e("Bitmap", "run here");

				Bitmap bitmap = PhotoUtils.getBitmapFromUri(cr, uri);
				if (bitmap != null) {
					String path = PhotoUtils.savePhotoToSDCard(bitmap);
					PhotoUtils.fliterPhoto(context, activity, path);
					// PhotoUtils.cropPhoto(context, instance, path);
				}
				break;

			case PhotoUtils.INTENT_REQUEST_CODE_FLITER:

				photoPath = data.getStringExtra("path");

				pics.set(photoIndex, photoPath);
				if (picAdapter != null) {
					picAdapter.notifyDataSetChanged();
				}
				break;

			case PhotoUtils.INTENT_REQUEST_CODE_CROP:
				break;
			case POI_SELECTED:
				Bundle b = data.getExtras();
				loc_name = b.getString("name");
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
		case MENUITEM_ALBUMADD_OK:
			if (!isValid(pics, content, rating, loc_name)) {
				Logot.outError("valid", "true");
				return true;
			}
			showProgress();
			postPhotoRequest();
			//postRequest();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	String jsonPhotoUrls = "";
	private void postPhotoRequest(){

		Logot.outError("fetchRequest1", "fetchRequest1");
		
		String url = AsyncHttpManager.UPLOADS_URL;

		RequestParams params = new RequestParams();
		params.put("userId", PreferenceHelper.getUserId());

		try {
			if (pics.get(0) != null &&! pics.get(0).equals("")) {
				params.put("image[0]", new File(pics.get(0)));
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		AsyncHttpManager.getClient(context).post(url, params,
				new AsyncHttpHandler() {
					@Override
					public void onFailured() {
						super.onFailured();
						hideProgress();
						Toaster.showShort(activity, "上传失败!");
					}

					@Override
					public void onSuccessed() {
						super.onSuccessed();
						try {
							jsonPhotoUrls = new JSONObject(getResult())
									.getString("message").toString();
							Logot.outError("jsonPhotoUrls", jsonPhotoUrls);

						} catch (JSONException e) {
							e.printStackTrace();
						}
						postRequest();
					}

					@Override
					public void onError() {
						super.onError();
						hideProgress();
					}
				});
	}

	private void postRequest() {
		
		rating = rb_rating.getRating();
		content = et_description.getText().toString().trim();

		String url = AsyncHttpManager.PHOTO_PUBLISH__URL;

		RequestParams params = new RequestParams();
		params.put("scenicId", "0");
		params.put("userId", PreferenceHelper.getUserId());
		params.put("content", content);
		params.put("score", String.valueOf(rating));
		params.put("tags", tag);
		params.put("images", jsonPhotoUrls);

		AsyncHttpManager.getClient(context).post(url, params,
				new AsyncHttpHandler() {
					@Override
					public void onFailured() {
						super.onFailured();

						hideProgress();
						Toaster.showShort(activity, "上传失败!");
					}

					@Override
					public void onSuccessed() {
						super.onSuccessed();
						hideProgress();
						Toaster.showShort(activity, "上传成功!");
						finish();
					}
					
					@Override
					public void onError() {
						super.onError();
						hideProgress();
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

	private boolean isValid(ArrayList<String> ps, String content,
			double rating, String loc_name) {

		if (ps == null || ps.get(0).equals("")) {
			Toast.makeText(context, "需要选择照片!", Toast.LENGTH_SHORT).show();
		} else if (et_description.getText().toString().trim().equals("")) {
			Toast.makeText(context, "需要填写说明!", Toast.LENGTH_SHORT).show();
		} else  if(tag.equals("-1")){
			Toast.makeText(context, "需要选择标签!", Toast.LENGTH_SHORT).show();
		}else{
			return true;
		}
		return false;
	}

}
