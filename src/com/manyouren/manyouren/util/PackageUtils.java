/**
 * @Package com.manyouren.android.util    
 * @Title: PackageUtils.java 
 * @Description: TODO
 * @author firefist_wei firefist.wei@gmail.com   
 * @date 2014-6-13 下午6:05:37 
 * @version V1.0   
 */
package com.manyouren.manyouren.util;

import java.io.File;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.widget.Toast;

@SuppressLint("NewApi")
public class PackageUtils {

	public static final String TAG = "PackageUtils";
	/**
	 * App installation location settings values, same to {@link #PackageHelper}
	 */
	public static final int APP_INSTALL_AUTO = 0;
	public static final int APP_INSTALL_INTERNAL = 1;
	public static final int APP_INSTALL_EXTERNAL = 2;

	/**
	 * install package normal by system intent
	 * 
	 * @param context
	 * @param filePath
	 *            file path of package
	 * @return whether apk exist
	 */
	public static boolean installNormal(Context context, String filePath) {
		Intent i = new Intent(Intent.ACTION_VIEW);
		File file = new File(filePath);
		if (file == null || !file.exists() || !file.isFile()
				|| file.length() <= 0) {
			return false;
		}

		i.setDataAndType(Uri.parse("file://" + filePath),
				"application/vnd.android.package-archive");
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(i);
		return true;
	}


	/**
	 * uninstall package normal by system intent
	 * 
	 * @param context
	 * @param packageName
	 *            package name of app
	 * @return whether package name is empty
	 */
	public static boolean uninstallNormal(Context context, String packageName) {
		if (packageName == null || packageName.length() == 0) {
			return false;
		}

		Intent i = new Intent(Intent.ACTION_DELETE,
				Uri.parse(new StringBuilder(32).append("package:")
						.append(packageName).toString()));
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(i);
		return true;
	}


	/**
	 * whether context is system application
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isSystemApplication(Context context) {
		if (context == null) {
			return false;
		}

		return isSystemApplication(context, context.getPackageName());
	}

	/**
	 * whether packageName is system application
	 * 
	 * @param context
	 * @param packageName
	 * @return
	 */
	public static boolean isSystemApplication(Context context,
			String packageName) {
		if (context == null) {
			return false;
		}

		return isSystemApplication(context.getPackageManager(), packageName);
	}

	/**
	 * whether packageName is system application
	 * 
	 * @param packageManager
	 * @param packageName
	 * @return 
	 */
	public static boolean isSystemApplication(PackageManager packageManager,
			String packageName) {
		if (packageManager == null || packageName == null
				|| packageName.length() == 0) {
			return false;
		}

		try {
			ApplicationInfo app = packageManager.getApplicationInfo(
					packageName, 0);
			return (app != null && (app.flags & ApplicationInfo.FLAG_SYSTEM) > 0);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * whether the app whost package's name is packageName is on the top of the
	 * stack
	 * <ul>
	 * <strong>Attentions:</strong>
	 * <li>You should add <strong>android.permission.GET_TASKS</strong> in
	 * manifest</li>
	 * </ul>
	 * 
	 * @param context
	 * @param packageName
	 * @return if params error or task stack is null, return null, otherwise
	 *         retun whether the app is on the top of stack
	 */
	public static Boolean isTopActivity(Context context, String packageName) {
		if (context == null || StringUtils.isEmpty(packageName)) {
			return null;
		}

		ActivityManager activityManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> tasksInfo = activityManager.getRunningTasks(1);
		if (tasksInfo ==null || tasksInfo.size()==0) {
			return null;
		}
		try {
			return packageName.equals(tasksInfo.get(0).topActivity
					.getPackageName());
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * get app version code
	 * 
	 * @param context
	 * @return
	 */
	public static int getAppVersionCode(Context context) {
		if (context != null) {
			PackageManager pm = context.getPackageManager();
			if (pm != null) {
				PackageInfo pi;
				try {
					pi = pm.getPackageInfo(context.getPackageName(), 0);
					if (pi != null) {
						return pi.versionCode;
					}
				} catch (NameNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
		return -1;
	}

	public static void downloadApp(Context context, String url, String fileName) {
		if (!Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			Toast.makeText(context, "SD卡异常，请检查SD卡是否正常插入", Toast.LENGTH_SHORT)
					.show();
			return;
		}
		String dir = "ManyourenDowloads";
		Uri uri = Uri.parse(url);
//		Log.i("CHECKupdate", "uri:" + uri.toString());
		DownloadManager downloadManager = (DownloadManager) context
				.getSystemService(Context.DOWNLOAD_SERVICE);
		DownloadManager.Request downloadRequest = new DownloadManager.Request(
				uri);
//		Log.i("CHECKupdate", "downloadRequest:" + downloadRequest.toString());
		downloadRequest
				.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE
						| DownloadManager.Request.NETWORK_WIFI);
		downloadRequest.setVisibleInDownloadsUi(true);
		downloadRequest.setTitle(fileName);
		File folder = Environment.getExternalStoragePublicDirectory(dir);
		if (!folder.exists()) {
			folder.mkdir();
		}
		downloadRequest.setDestinationInExternalPublicDir(dir, fileName);
		context.getSharedPreferences("downLoadFile", Context.MODE_PRIVATE)
				.edit()
				.putLong("updateAppDownLoadId",
						downloadManager.enqueue(downloadRequest)).commit();

	}

	/**
	 * start InstalledAppDetails Activity
	 * 
	 * @param context
	 * @param packageName
	 */
	public static void startInstalledAppDetails(Context context,
			String packageName) {
		Intent intent = new Intent();
		int sdkVersion = Build.VERSION.SDK_INT;
		if (sdkVersion >= 9) {
			intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
			intent.setData(Uri.fromParts("package", packageName, null));
		} else {
			intent.setAction(Intent.ACTION_VIEW);
			intent.setClassName("com.android.settings",
					"com.android.settings.InstalledAppDetails");
			intent.putExtra((sdkVersion == 8 ? "pkg"
					: "com.android.settings.ApplicationPkgName"), packageName);
		}
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}

}
