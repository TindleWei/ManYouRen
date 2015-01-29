package com.manyouren.manyouren.service;

import android.content.Context;

import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.PushService;
import com.avos.avoscloud.SignUpCallback;
import com.manyouren.manyouren.HomeActivity;
import com.avos.avoscloud.AVInstallation;

public class AVService {
	public static void signUp(String username, String password, String email, SignUpCallback signUpCallback) {
	    AVUser user = new AVUser();
	    user.setUsername(username);
	    user.setPassword(password);
	    user.setEmail(email);
	    user.signUpInBackground(signUpCallback);
	  }

	  public static void logout() {
	    AVUser.logOut();
	  }
	  
	  public static void initPushService(Context ctx) {
		  PushService.setDefaultPushCallback(ctx, HomeActivity.class);
		    PushService.subscribe(ctx, "public", HomeActivity.class);
		    AVInstallation.getCurrentInstallation().saveInBackground();
		  }

}
