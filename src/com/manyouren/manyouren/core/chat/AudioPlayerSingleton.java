/**
* @Package com.manyouren.android.core.chat    
* @Title: AudioPlayerSingleton.java 
* @Description: TODO
* @author firefist_wei firefist.wei@gmail.com   
* @date 2014-9-12 下午9:39:28 
* @version V1.0   
*/
package com.manyouren.manyouren.core.chat;

import android.media.MediaPlayer;
import android.os.Handler;

/** 
 * @Description: TODO
 *
 * @author firefist_wei
 * @date 2014-9-12 下午9:39:28 
 *  
 */
public class AudioPlayerSingleton {
	
	private static MediaPlayer mediaPlayer = null;
	private static AudioPlayerSingleton instance = null;
	
	public synchronized static AudioPlayerSingleton getInstance() {
		if (instance == null)
			instance = new AudioPlayerSingleton();
		return instance;
	}

	private AudioPlayerSingleton() {
	}
	
	private String path;
	
	private Handler handler;
	
	public void stopPlayer(){
		if(handler!=null){
			handler.sendEmptyMessage(0);
		}
		
		if (mediaPlayer != null) {
		mediaPlayer.stop();
		mediaPlayer.setOnCompletionListener(null);
		mediaPlayer.setOnPreparedListener(null);
		mediaPlayer = null;
		}
	}
	
	public void startPlayer(Handler handler, String path) {
		this.handler = handler;
		this.path = path;

		if (mediaPlayer != null) {
			if (mediaPlayer.isPlaying()) {

				mediaPlayer.stop();
				mediaPlayer.setOnCompletionListener(null);
				mediaPlayer.setOnPreparedListener(null);
				mediaPlayer = null;
			}
		}
		mediaPlayer = new MediaPlayer();
		try {
			mediaPlayer.setDataSource(path);
			mediaPlayer.prepareAsync();
		} catch (Exception e) {
		}
		resetListener();
	}
	
	public void resetListener() {
		mediaPlayer
				.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

					@Override
					public void onCompletion(MediaPlayer mp) {

						if (handler != null) {
							handler.sendEmptyMessage(0);
						}
					}
				});
		mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

			@Override
			public void onPrepared(MediaPlayer mp) {
				mp.start();
				if (handler != null) {
					handler.sendEmptyMessage(1);
				}
			}
		});
	}
}
