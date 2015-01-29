/**
 * @Package com.manyouren.android.ui.plan    
 * @Title: GalleryFileActivity.java 
 * @Description: TODO
 * @author firefist_wei firefist.wei@gmail.com   
 * @date 2014-9-12 下午8:54:56 
 * @version V1.0   
 */
package com.manyouren.manyouren.ui.plan;

import java.util.ArrayList;
import java.util.List;

import com.manyouren.manyouren.R;
import com.manyouren.manyouren.widget.touchgallery.FilePagerAdapter;
import com.manyouren.manyouren.widget.touchgallery.GalleryViewPager;
import com.manyouren.manyouren.widget.touchgallery.BasePagerAdapter.OnItemChangeListener;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Window;

/**
 * @Description: TODO
 * 
 * @author firefist_wei
 * @date 2014-9-12 下午8:54:56
 * 
 */
public class GalleryFileActivity extends FragmentActivity {

	public static final String GALLERY_FILES = "gallery_files";

	public static final String GALLERY_INDEX = "gallery_index";

	private GalleryViewPager mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_galleryurl);

		String[] urls = getIntent().getStringArrayExtra(GALLERY_FILES);

		int index = getIntent().getIntExtra(GALLERY_INDEX, 0);

		List<String> items = new ArrayList<String>();

		for (int i = 0; i < urls.length; i++) {
			if (urls[i] != null)
				items.add(urls[i]);
		}
		
		FilePagerAdapter pagerAdapter = new FilePagerAdapter(this, items);
		pagerAdapter.setOnItemChangeListener(new OnItemChangeListener() {
			@Override
			public void onItemChange(int currentPosition) {

			}
		});
		
		mViewPager = (GalleryViewPager) findViewById(R.id.touchgallery_viewer);
		mViewPager.setOffscreenPageLimit(3);
		mViewPager.setAdapter(pagerAdapter);

		mViewPager.setCurrentItem(index);
		
		
	}

}
