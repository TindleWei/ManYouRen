package com.manyouren.manyouren.guide;

import java.util.ArrayList;
import java.util.List;

import com.manyouren.manyouren.R;

import android.app.Activity;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class GuideActivity extends FragmentActivity implements OnClickListener,
		OnPageChangeListener {
	private ViewPager vp;

	private ViewPagerAdapter vpAdapter;

	private List<View> views;
	private static final int[] pics = { R.drawable.a,

	R.drawable.b, R.drawable.bg05,

	};

	private ImageView[] dots;

	private int currentIndex;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guide);
		views = new ArrayList<View>();

		LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);

		for (int i = 0; i < pics.length; i++) {

			ImageView iv = new ImageView(this);

			iv.setLayoutParams(mParams);

			iv.setImageResource(pics[i]);

			views.add(iv);

		}

		vp = (ViewPager) findViewById(R.id.viewpager);

		vpAdapter = new ViewPagerAdapter(views);
		vp.setAdapter(vpAdapter);
		vp.setOnPageChangeListener(this);

		initDots();

	}

	private void initDots() {

		LinearLayout ll = (LinearLayout) findViewById(R.id.ll);

		dots = new ImageView[pics.length];

		// ѭ��ȡ��С��ͼƬ

		for (int i = 0; i < pics.length; i++) {

			// �õ�һ��LinearLayout�����ÿһ����Ԫ��

			dots[i] = (ImageView) ll.getChildAt(i);

			dots[i].setEnabled(true);// ����Ϊ��ɫ

			dots[i].setOnClickListener((OnClickListener) this);

			dots[i].setTag(i);// ����λ��tag������ȡ���뵱ǰλ�ö�Ӧ

		}

		currentIndex = 0;

		dots[currentIndex].setImageResource(R.drawable.white_dot);// ����Ϊ��ɫ����ѡ��״̬

	}

	private void setCurView(int position)

	{

		if (position < 0 || position >= pics.length) {

			return;

		}

		vp.setCurrentItem(position);

	}

	private void setCurDot(int positon)

	{

		if (positon < 0 || positon > pics.length - 1 || currentIndex == positon) {

			return;

		}
		dots[positon].setImageResource(R.drawable.white_dot);
		;

		dots[currentIndex].setImageResource(R.drawable.dark_dot);

		currentIndex = positon;

	}

	// ������״̬�ı�ʱ����

	public void onPageScrollStateChanged(int pos) {

		// TODO Auto-generated method stub

	}

	// ����ǰҳ�汻����ʱ����

	public void onPageScrolled(int arg0, float arg1, int arg2) {

		// TODO Auto-generated method stub

	}

	// ���µ�ҳ�汻ѡ��ʱ����

	public void onPageSelected(int arg0) {

		// ���õײ�С��ѡ��״̬

		setCurDot(arg0);

	}

	public void onClick(View v) {

		int position = (Integer) v.getTag();

		setCurView(position);

		setCurDot(position);

	}

	public class ViewPagerAdapter extends PagerAdapter {

		// �����б�

		private List<View> views;

		public ViewPagerAdapter(List<View> views) {

			this.views = views;

		}

		// ���arg1λ�õĽ���

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {

			((ViewPager) arg0).removeView(views.get(arg1));

		}

		@Override
		public void finishUpdate(View arg0) {

			// TODO Auto-generated method stub

		}

		@Override
		public int getCount() {

			if (views != null)

			{

				return views.size();

			}

			return 0;

		}

		@Override
		public Object instantiateItem(View arg0, int arg1) {

			((ViewPager) arg0).addView(views.get(arg1), 0);

			return views.get(arg1);

		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {

			return (arg0 == arg1);

		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {

			// TODO Auto-generated method stub

		}

		@Override
		public Parcelable saveState() {

			// TODO Auto-generated method stub

			return null;

		}

		@Override
		public void startUpdate(View arg0) {

			// TODO Auto-generated method stub

		}

	}
}
