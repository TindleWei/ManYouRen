/**
 * @Package com.manyouren.android.ui.plan    
 * @Title: PlanPublishActivity.java 
 * @Description: TODO
 * @author firefist_wei firefist.wei@gmail.com   
 * @date 2014-6-21 上午10:45:48 
 * @version V1.0   
 */
package com.manyouren.manyouren.ui.plan;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import roboguice.inject.InjectView;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore.MediaColumns;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.github.kevinsawicki.wishlist.Toaster;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;
import com.manyouren.manyouren.R;
import com.manyouren.manyouren.base.BaseActivity;
import com.manyouren.manyouren.config.Constants;
import com.manyouren.manyouren.config.HttpConfig;
import com.manyouren.manyouren.controller.PlanController;
import com.manyouren.manyouren.service.AsyncHttpHandler;
import com.manyouren.manyouren.service.AsyncHttpLoader;
import com.manyouren.manyouren.service.AsyncHttpManager;
import com.manyouren.manyouren.service.PreferenceHelper;
import com.manyouren.manyouren.ui.HorizontalListView;
import com.manyouren.manyouren.ui.HorizontalListViewAdapter;
import com.manyouren.manyouren.ui.user.CityListActivity;
import com.manyouren.manyouren.ui.usernew.AccountSignupActivity;
import com.manyouren.manyouren.util.DateUtils;
import com.manyouren.manyouren.util.Ln;
import com.manyouren.manyouren.util.Logot;
import com.manyouren.manyouren.util.PhotoUtils;
import com.manyouren.manyouren.util.WidgetUtils;
import com.manyouren.manyouren.widget.ElasticScrollView;
import com.squareup.timessquare.CalendarPickerView;
import com.squareup.timessquare.CalendarPickerView.OnDateSelectedListener;
import com.squareup.timessquare.CalendarPickerView.SelectionMode;

/**
 * 
 * @author firefist_wei
 * @date 2014-6-21 上午10:45:48
 * 
 */
@SuppressLint("NewApi")
public class PlanPublishActivity extends BaseActivity {

	public static final int ACTION_REQUEST_SELEC_CITY = 15;

	@InjectView(R.id.layout_desti)
	private RelativeLayout layout_desti;

	@InjectView(R.id.layout_date)
	private RelativeLayout layout_date;

	@InjectView(R.id.layout_startcity)
	private RelativeLayout layout_startcity;

	@InjectView(R.id.tv_desti)
	private TextView tv_desti;

	@InjectView(R.id.tv_start_date)
	private TextView start_date;

	@InjectView(R.id.tv_start_city)
	private TextView tv_start_city;

	@InjectView(R.id.tv_text_length)
	private TextView tv_text_length;

	@InjectView(R.id.et_postscript)
	private EditText et_postscript;

	private PlanPublishActivity instance;

	private AlertDialog dialogTimes;

	private CalendarPickerView calendar;

	ArrayList<Date> dateList = null;

	private String str_for, str_with, str_seek;

	private ImageView pho1, pho2, pho3;

	private String p1Path, p2Path, p3Path;

	private String photoPath;

	public static int REQUESTCODE_PUBLISH = 1;

	/**
	 * to mark which photo part would be show
	 */
	private int photoIndex = 0;

	private static final int MENUITEM_PLAN_UP = 11001;

	HorizontalListView hListView;

	HorizontalListViewAdapter hListViewAdapter;

	ImageView type_for, type_with, type_seek;

	private TextView tv_for, tv_with, tv_seek;

	private RelativeLayout layout_plantype;
	// private TextView selectCityBt;

	private ElasticScrollView elasticScrollView;

	String destinationStr = "";

	String startCityStr = Constants.PLACE_NOW;

	PlacesAutoAdapter pacAdapter = null;

	private String address;

	private String scenicId = "";
	
	private String myPName ="";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_plan_publish2);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

		instance = this;

		setActionBar(getResources().getString(R.string.title_publish));

		scenicId = getStringExtra("scenicId");
		address = getStringExtra("address");
		myPName = address;

		initView();
		initEvent();
		init();

		initTimesDialog();
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		// must store the new intent unless getIntent() will return the old one
		setIntent(intent);
		address = getStringExtra("address");
		scenicId = getStringExtra("scenicId");

		tv_desti.setText(address);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuItem done = menu.add(0, MENUITEM_PLAN_UP, 0, "Upload");
		done.setIcon(this.getResources().getDrawable(
				R.drawable.ic_action_accept));
		done.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {
		switch (item.getItemId()) {
		// This is the home button in the top left corner of the screen.
		case MENUITEM_PLAN_UP:
			handlePlanUpload();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// WidgetUtils.hideKeyBoard(instance, autoCompView);
	}

	@Override
	protected void initView() {

		pho1 = (ImageView) findViewById(R.id.more_iv1);

		pho2 = (ImageView) findViewById(R.id.more_iv2);

		pho3 = (ImageView) findViewById(R.id.more_iv3);

		hListView = (HorizontalListView) findViewById(R.id.horizon_listview);

		type_for = (ImageView) findViewById(R.id.type_for);

		type_with = (ImageView) findViewById(R.id.type_with);

		type_seek = (ImageView) findViewById(R.id.type_seek);

		layout_plantype = (RelativeLayout) findViewById(R.id.layout_plantype);

		elasticScrollView = (ElasticScrollView) findViewById(R.id.elastic_scrollview);

		tv_for = (TextView) findViewById(R.id.type_tv_for);

		tv_with = (TextView) findViewById(R.id.type_tv_with);

		tv_seek = (TextView) findViewById(R.id.type_tv_seek);
	}

	@Override
	protected void initEvent() {

		MyOnClickListener listener = new MyOnClickListener();
		pho1.setOnClickListener(listener);
		pho2.setOnClickListener(listener);
		pho3.setOnClickListener(listener);
		layout_startcity.setOnClickListener(listener);

		tv_desti.setText(address);
		layout_desti.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				startActivityForResult(new Intent(PlanPublishActivity.this,
						ScenicsListActivity.class), REQUESTCODE_PUBLISH);
				overridePendingTransition(R.anim.left_in, R.anim.left_out);
			}
		});

		type_for.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				initHorizontalListView(0, PlanController.list_for,
						PlanController.res_for);

				if (!layout_plantype.isShown()) {
					type_for.setVisibility(View.VISIBLE);
					type_with.setVisibility(View.INVISIBLE);
					type_seek.setVisibility(View.INVISIBLE);

					tv_for.setVisibility(View.VISIBLE);
					tv_with.setVisibility(View.INVISIBLE);
					tv_seek.setVisibility(View.INVISIBLE);

					ObjectAnimator anim = ObjectAnimator.ofFloat(type_for,
							"rotation", 0f, 45f);
					anim.setDuration(250).start();

					layout_plantype.setVisibility(View.VISIBLE);

				} else {

					type_for.setVisibility(View.VISIBLE);
					type_with.setVisibility(View.VISIBLE);
					type_seek.setVisibility(View.VISIBLE);

					tv_for.setVisibility(View.VISIBLE);
					tv_with.setVisibility(View.VISIBLE);
					tv_seek.setVisibility(View.VISIBLE);

					ObjectAnimator anim = ObjectAnimator.ofFloat(type_for,
							"rotation", 45f, 0f);
					anim.setDuration(250).start();

					layout_plantype.setVisibility(View.GONE);
				}

			}
		});

		type_with.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				initHorizontalListView(1, PlanController.list_with,
						PlanController.res_with);

				if (!layout_plantype.isShown()) {
					type_for.setVisibility(View.INVISIBLE);
					type_with.setVisibility(View.VISIBLE);
					type_seek.setVisibility(View.INVISIBLE);

					tv_for.setVisibility(View.INVISIBLE);
					tv_with.setVisibility(View.VISIBLE);
					tv_seek.setVisibility(View.INVISIBLE);

					ObjectAnimator anim = ObjectAnimator.ofFloat(type_with,
							"rotation", 0f, 45f);
					anim.setDuration(250).start();

					layout_plantype.setVisibility(View.VISIBLE);

				} else {
					type_for.setVisibility(View.VISIBLE);
					type_with.setVisibility(View.VISIBLE);
					type_seek.setVisibility(View.VISIBLE);

					tv_for.setVisibility(View.VISIBLE);
					tv_with.setVisibility(View.VISIBLE);
					tv_seek.setVisibility(View.VISIBLE);

					ObjectAnimator anim = ObjectAnimator.ofFloat(type_with,
							"rotation", 45f, 0f);
					anim.setDuration(250).start();

					layout_plantype.setVisibility(View.GONE);
				}

			}
		});

		type_seek.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				initHorizontalListView(2, PlanController.list_seek,
						PlanController.res_seek);

				if (!layout_plantype.isShown()) {
					type_for.setVisibility(View.INVISIBLE);
					type_with.setVisibility(View.INVISIBLE);
					type_seek.setVisibility(View.VISIBLE);

					tv_for.setVisibility(View.INVISIBLE);
					tv_with.setVisibility(View.INVISIBLE);
					tv_seek.setVisibility(View.VISIBLE);

					ObjectAnimator anim = ObjectAnimator.ofFloat(type_seek,
							"rotation", 0f, 45f);
					anim.setDuration(250).start();

					layout_plantype.setVisibility(View.VISIBLE);

				} else {
					type_for.setVisibility(View.VISIBLE);
					type_with.setVisibility(View.VISIBLE);
					type_seek.setVisibility(View.VISIBLE);

					tv_for.setVisibility(View.VISIBLE);
					tv_with.setVisibility(View.VISIBLE);
					tv_seek.setVisibility(View.VISIBLE);

					ObjectAnimator anim = ObjectAnimator.ofFloat(type_seek,
							"rotation", 45f, 0f);
					anim.setDuration(250).start();

					layout_plantype.setVisibility(View.GONE);
				}

			}
		});

		et_postscript.addTextChangedListener(new TextWatcher() {

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
				tv_text_length.setText(et_postscript.getText().toString()
						.length()
						+ "/150字");
			}
		});

	}

	public void initHorizontalListView(final int index,
			final List<String> list, int[] ids) {

		if (hListViewAdapter != null) {
			hListViewAdapter = null;
		}

		hListViewAdapter = new HorizontalListViewAdapter(
				getApplicationContext(), list, ids);

		hListView.setAdapter(hListViewAdapter);

		hListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// Toaster.showLong(PlanPublishActivity.this,
				// list.get(position));

				final int pos = position;

				if (index == 0) {

					str_for = list.get(pos);

					ObjectAnimator anim = ObjectAnimator.ofFloat(type_for,
							"rotation", 45f, 0f);
					anim.setDuration(250).start();

					layout_plantype.postDelayed(new Runnable() {

						@Override
						public void run() {
							layout_plantype.setVisibility(View.GONE);

							type_for.setImageResource(PlanController.res_for[pos]);
							tv_for.setText(list.get(pos));

							type_for.setVisibility(View.VISIBLE);
							type_with.setVisibility(View.VISIBLE);
							type_seek.setVisibility(View.VISIBLE);

							tv_for.setVisibility(View.VISIBLE);
							tv_with.setVisibility(View.VISIBLE);
							tv_seek.setVisibility(View.VISIBLE);
						}

					}, 300);

				} else if (index == 1) {

					str_with = list.get(pos);

					ObjectAnimator anim = ObjectAnimator.ofFloat(type_with,
							"rotation", 45f, 0f);
					anim.setDuration(250).start();

					layout_plantype.postDelayed(new Runnable() {

						@Override
						public void run() {
							layout_plantype.setVisibility(View.GONE);

							type_with
									.setImageResource(PlanController.res_with[pos]);
							tv_with.setText(list.get(pos));

							type_for.setVisibility(View.VISIBLE);
							type_with.setVisibility(View.VISIBLE);
							type_seek.setVisibility(View.VISIBLE);

							tv_for.setVisibility(View.VISIBLE);
							tv_with.setVisibility(View.VISIBLE);
							tv_seek.setVisibility(View.VISIBLE);
						}

					}, 300);

				} else if (index == 2) {

					str_seek = list.get(pos);

					ObjectAnimator anim = ObjectAnimator.ofFloat(type_seek,
							"rotation", 45f, 0f);
					anim.setDuration(250).start();

					layout_plantype.postDelayed(new Runnable() {

						@Override
						public void run() {
							layout_plantype.setVisibility(View.GONE);

							type_seek
									.setImageResource(PlanController.res_seek[pos]);
							tv_seek.setText(list.get(pos));

							type_for.setVisibility(View.VISIBLE);
							type_with.setVisibility(View.VISIBLE);
							type_seek.setVisibility(View.VISIBLE);

							tv_for.setVisibility(View.VISIBLE);
							tv_with.setVisibility(View.VISIBLE);
							tv_seek.setVisibility(View.VISIBLE);
						}
					}, 300);
				}
				hListViewAdapter.setSelectIndex(position);
				hListViewAdapter.notifyDataSetChanged();
			}
		});
	}

	public Bitmap decodeFile(String path) {
		try {
			// Decode image size
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(path, o);
			// The new size we want to scale to
			final int REQUIRED_SIZE = 70;

			// Find the correct scale value. It should be the power of 2.
			int scale = 1;
			while (o.outWidth / scale / 2 >= REQUIRED_SIZE
					&& o.outHeight / scale / 2 >= REQUIRED_SIZE)
				scale *= 2;

			// Decode with inSampleSize
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			return BitmapFactory.decodeFile(path, o2);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return null;

	}

	public String getAbsolutePath(Uri uri) {
		String[] projection = { MediaColumns.DATA };
		@SuppressWarnings("deprecation")
		Cursor cursor = managedQuery(uri, projection, null, null, null);
		if (cursor != null) {
			int column_index = cursor.getColumnIndexOrThrow(MediaColumns.DATA);
			cursor.moveToFirst();
			return cursor.getString(column_index);
		} else
			return null;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == RESULT_OK) {

			switch (requestCode) {
			case PhotoUtils.INTENT_REQUEST_CODE_ALBUM:

				/*
				 * String path = getAbsolutePath(data.getData()); Bitmap bitmap
				 * = decodeFile(path);
				 */

				Uri uri = data.getData();
				ContentResolver cr = this.getContentResolver();

				Bitmap bitmap = PhotoUtils.getBitmapFromUri(cr, uri);
				if (bitmap != null) {
					String path2 = PhotoUtils.savePhotoToSDCard(bitmap);
					PhotoUtils.fliterPhoto(context, instance, path2);
					// PhotoUtils.cropPhoto(context, instance, path);
				}
				break;

			case PhotoUtils.INTENT_REQUEST_CODE_FLITER:

				photoPath = data.getStringExtra("path");

				Bitmap filterBitmap = PhotoUtils.getBitmapFromFile(photoPath);

				if (photoIndex == 0) {
					pho1.setImageBitmap(filterBitmap);
					pho2.setVisibility(View.VISIBLE);
					p1Path = photoPath;
				} else if (photoIndex == 1) {
					pho2.setImageBitmap(filterBitmap);
					pho3.setVisibility(View.VISIBLE);
					p2Path = photoPath;
				} else if (photoIndex == 2) {
					pho3.setImageBitmap(filterBitmap);
					p3Path = photoPath;
				}
				break;

			case PhotoUtils.INTENT_REQUEST_CODE_CROP:
				break;

			case ACTION_REQUEST_SELEC_CITY:
				// selectCityBt.setText(data.getStringExtra("CityName"));
				// startCityStr = selectCityBt.getText().toString();
				// pacAdapter.setSreachCity(startCityStr);
				break;

			case 31:
				tv_start_city.setText(data.getStringExtra("CityName"));
				break;

			}
		}
	}

	/**
	 * this is times square open-sources
	 * 
	 * @return void
	 */
	private void initTimesDialog() {

		layout_date.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				start_date.setEnabled(false);

				Calendar nextYear = Calendar.getInstance();

				Calendar thisYear = Calendar.getInstance();

				dateList = new ArrayList<Date>();

				thisYear.add(Calendar.YEAR, 0);

				nextYear.add(Calendar.YEAR, 1);

				calendar = (CalendarPickerView) getLayoutInflater().inflate(
						R.layout.dialog_times, null, false);

				calendar.init(thisYear.getTime(), nextYear.getTime()).inMode(
						SelectionMode.RANGE);

				calendar.setOnDateSelectedListener(new OnDateSelectedListener() {

					@Override
					public void onDateUnselected(Date date) {
					}

					@Override
					public void onDateSelected(Date date) {
						if (dateList.size() == 0) {
							dateList.add(date);
						} else if (dateList.size() == 1) {
							if (date.after(dateList.get(0))) {
								dateList.add(date);
							} else {
								dateList.remove(0);
								dateList.add(date);
							}
						} else if (dateList.size() > 1) {
							if (date.after(dateList.get(1))) {
								dateList.remove(1);
								dateList.remove(0);
								dateList.add(date);
							} else if (date.before(dateList.get(0))) {
								dateList.remove(1);
								dateList.remove(0);
								dateList.add(date);

							} else {
								dateList.remove(1);
								dateList.remove(0);
								dateList.add(date);
							}
						}
					}
				});

				dialogTimes = new AlertDialog.Builder(PlanPublishActivity.this)
						.setView(calendar)
						.setPositiveButton("OK",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(
											DialogInterface dialogInterface,
											int i) {
										dialogInterface.dismiss();
										showTimes();
										start_date.setEnabled(true);
									}
								}).create();
				dialogTimes
						.setOnShowListener(new DialogInterface.OnShowListener() {
							@Override
							public void onShow(DialogInterface dialogInterface) {
								calendar.fixDialogDimens();
								start_date.setEnabled(true);
							}
						});
				dialogTimes.show();
			}
		});

	}

	public void showTimes() {

		if (dateList.size() == 2) {
			start_date.setText(DateUtils.getPlanDigitalDate(dateList.get(0),
					dateList.get(1)));
		} else if (dateList.size() == 1) {
			start_date.setText(DateUtils.getPlanDigitalDate(dateList.get(0),
					dateList.get(0)));
		} else {
			start_date.setText("");
		}
	}

	class MyOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.layout_startcity:
				startActivityForResult(new Intent(context,
						CityListActivity.class), 31);
				overridePendingTransition(R.anim.left_in, R.anim.left_out);

				break;

			case R.id.more_iv1:
				PhotoUtils.selectPhoto(instance);
				photoIndex = 0;
				break;

			case R.id.more_iv2:
				PhotoUtils.selectPhoto(instance);
				photoIndex = 1;
				break;

			case R.id.more_iv3:
				PhotoUtils.selectPhoto(instance);
				photoIndex = 2;
				break;
			}
		}
	}

	/**
	 * Hide progress dialog
	 */
	@SuppressWarnings("deprecation")
	protected void hideProgress() {
		dismissDialog(0);
	}

	/**
	 * Show progress dialog
	 */
	@SuppressWarnings("deprecation")
	protected void showProgress() {
		showDialog(0);
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		final ProgressDialog dialog = new ProgressDialog(this);
		dialog.setMessage("发布计划中");
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

	/**
	 * 判断 能否上传
	 * 
	 * @return void
	 */
	private void handlePlanUpload() {

		if (tv_desti.getText().toString().trim().equals("")) {
			Toaster.showShort(PlanPublishActivity.this, "需要有目的地");
			return;
		}

		if (dateList == null || dateList.size() == 0) {
			Toaster.showShort(PlanPublishActivity.this, "需要有时间");
			return;
		}

		if (str_for == null || str_seek == null || str_with == null) {
			Toaster.showShort(PlanPublishActivity.this, "需要目的，和谁，寻求");
			return;
		}
		destinationStr = tv_desti.getText().toString();
		showProgress();

		if (p1Path != null) {
			fetchRequest1();
		} else {
			fetchRequest2();
		}
	}

	List<String> savedPhotoUrls = new ArrayList<String>();
	String jsonPhotoUrls = "";

	private void fetchRequest1() {
		Logot.outError("fetchRequest1", "fetchRequest1");
		String url = AsyncHttpManager.UPLOADS_URL;

		RequestParams params = new RequestParams();
		params.put("userId", PreferenceHelper.getUserId());

		try {
			if (p1Path != null) {
				params.put("image[0]", new File(p1Path));
			}
			if (p2Path != null) {
				params.put("image[1]", new File(p2Path));
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
						Toaster.showLong(PlanPublishActivity.this, "发布失败");
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
						
						if (p3Path != null) {
							fetchRequest1_5();
						}else{
							fetchRequest2();
						}
						/*
						 * JSONArray array = new JSONArray(new JSONObject(
						 * getResult()).getString("message")); for (int i = 0; i
						 * < array.length(); i++) {
						 * savedPhotoUrls.add(array.get(0).toString()); }
						 */
					}

					@Override
					public void onError() {
						super.onError();
						hideProgress();
					}
				});
	}

	private void fetchRequest1_5() {
			String url = AsyncHttpManager.UPLOADS_URL;

			RequestParams params = new RequestParams();
			params.put("userId", PreferenceHelper.getUserId());
			try {
				params.put("image[0]", new File(p3Path));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			AsyncHttpManager.getClient(context).post(url, params,
					new AsyncHttpHandler() {
						@Override
						public void onFailured() {
							super.onFailured();
							hideProgress();
							Toaster.showLong(PlanPublishActivity.this, "发布失败");
						}

						@Override
						public void onSuccessed() {
							super.onSuccessed();
							try {
								String photoUrl = new JSONObject(getResult())
										.getString("message").toString();
								Logot.outError("jsonPhotoUrls 3", photoUrl);
								
								//字符拼接
								// jsonPhotoUrls   ["01414823910.jpg","11414823910.jpg"]
								//	photoUrl    ["01414823911.jpg"]
								jsonPhotoUrls = jsonPhotoUrls.substring(0, jsonPhotoUrls.length()-1) + "," +
										photoUrl.substring(1, photoUrl.length());
								Logot.outError("new jsonPhotoUlrs", jsonPhotoUrls);

							} catch (JSONException e) {
								e.printStackTrace();
							}
							
							fetchRequest2();
							
							/*
							 * JSONArray array = new JSONArray(new JSONObject(
							 * getResult()).getString("message")); for (int i = 0; i
							 * < array.length(); i++) {
							 * savedPhotoUrls.add(array.get(0).toString()); }
							 */					
						}
						@Override
						public void onError() {
							super.onError();
							hideProgress();
						}
					});
	}

	/**
	 * 传递 上传参数
	 * 
	 * @return void
	 */
	private void fetchRequest2() {
		String url = AsyncHttpManager.PLAN_PUBLISH_URL;

		RequestParams params = new RequestParams();
		params.put("userId", PreferenceHelper.getUserId());
		params.put("scenicId", scenicId);
		
		if(scenicId.equals("0")){
			params.put("myPName", myPName);
		}

		params.put("startCity", tv_start_city.getText().toString());
		//json 判断
		params.put("images", jsonPhotoUrls);
		// params.put("flight",);
		// params.put("flight", );

		params.put("type", "" + PlanController.list_for.indexOf(str_for));
		params.put("together", "" + PlanController.list_with.indexOf(str_with));
		params.put("purpose", "" + PlanController.list_seek.indexOf(str_seek));

		if (dateList != null) {
			if (dateList.size() == 1) {
				params.put("startDate", DateUtils.getTime(dateList.get(0)));
				params.put("endDate", DateUtils.getTime(dateList.get(0)));
			} else if (dateList.size() == 2) {
				params.put("startDate", DateUtils.getTime(dateList.get(0)));
				params.put("endDate", DateUtils.getTime(dateList.get(1)));
			}
		}
		if (et_postscript.getText().toString().isEmpty()) {
			String with = (str_with.equals("自己") ? "一个人" : "和" + str_with);
			String note = "" + with + "去" + tv_desti.getText().toString()
					+ str_for + ",寻求" + str_seek + "。";
			params.put("postscript", note);

		} else {
			params.put("postscript", et_postscript.getText().toString());
		}

		AsyncHttpManager.getClient(context).post(url, params,
				new AsyncHttpHandler() {
					@Override
					public void onFailured() {
						super.onFailured();
						hideProgress();
						Toaster.showLong(PlanPublishActivity.this, "发布失败");
					}

					@Override
					public void onSuccessed() {
						super.onSuccessed();
						hideProgress();
						Toaster.showLong(PlanPublishActivity.this, "发布成功");
						// to put user's plan to the top
						activity.setResult(RESULT_OK);
						finish();
					}

					@Override
					public void onError() {
						super.onError();
						hideProgress();
					}
				});
	}

	@Override
	protected void init() {
		tv_start_city.setText(startCityStr);
	}

}
