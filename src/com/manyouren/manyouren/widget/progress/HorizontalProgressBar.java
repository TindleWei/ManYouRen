package com.manyouren.manyouren.widget.progress;

import com.manyouren.manyouren.R;
import com.manyouren.manyouren.util.StringUtils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class HorizontalProgressBar {
	/** 单例 */
	public static HorizontalProgressBar mInstance = null;
	/** 加载进度条 **/
	public Dialog progressDialog;

	/**
	 * 获取单例
	 * 
	 * @return ProgressUtil对象
	 */
	public static HorizontalProgressBar onCreate() {
		if (mInstance == null) {
			synchronized (HorizontalProgressBar.class) {
				if (mInstance == null) {
					mInstance = new HorizontalProgressBar();
				}
			}
		}
		return mInstance;
	}
	
	private HorizontalProgressBar() {
	}
	
	/**
	 * 显示进度条
	 * @param con
	 * @param msg
	 * @param hasCancel
	 */
	public void show(Context con, String msg, boolean hasCancel){
		if (null == con) {
			return;
		}
		create(con, msg, hasCancel);
	}
	
	/**
	 * 隐藏进度条
	 */
	public void hide() {
		if (progressDialog != null && progressDialog.isShowing()) {
			progressDialog.cancel();
		}
	}

	/**
	 * 显示进度条
	 * 
	 * @param msg
	 *            进度条信息
	 * @param canCancel
	 *            是否有取消按钮（true:有，false：无）
	 */
	private void create(Context con, String msg, boolean canCancel) {
		if (null == con) {
			return;
		}
		progressDialog = null;
		progressDialog = new Dialog(con);
		progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		progressDialog.getWindow().setFlags(LayoutParams.FLAG_FULLSCREEN,
				LayoutParams.FLAG_FULLSCREEN);
		progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
		if (StringUtils.isEmpty(msg)) {
			msg = "正在加载，请稍后...";
		}
		progressDialog.show();
		progressDialog.setCancelable(!canCancel);
		progressDialog.setCanceledOnTouchOutside(false);
		Window window = progressDialog.getWindow();
		window.setContentView(R.layout.dialog_progress);
		TextView message = (TextView) window.findViewById(R.id.progress_msg_tv);
		message.setText(msg);

	}

}
