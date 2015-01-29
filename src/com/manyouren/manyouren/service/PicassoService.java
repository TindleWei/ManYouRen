/**
 * @Package com.manyouren.android.service    
 * @Title: PicassoService.java 
 * @author firefist_wei firefist.wei@gmail.com   
 * @date 2014-9-4 下午4:13:45 
 * @version V1.0   
 */
package com.manyouren.manyouren.service;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Bitmap.Config;
import android.widget.ImageView;

import com.manyouren.manyouren.R;
import com.manyouren.manyouren.RootApplication;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

/**
 * 
 * @author firefist_wei
 * @date 2014-9-4 下午4:13:45
 * 
 */
public class PicassoService {

	public static void setCirclePhoto(String url, ImageView imageView) {
		Picasso.with(RootApplication.getInstance()).load(url)
				.transform(new CircleTransform())
				.placeholder(R.drawable.gravatar_icon).into(imageView);
	}
	
	public static void setSquarePhoto(String url, ImageView imageView) {
		Picasso.with(RootApplication.getInstance()).load(url)
				.placeholder(R.drawable.gravatar_icon).into(imageView);
	}

	public static class CircleTransform implements Transformation {
		@Override
		public Bitmap transform(Bitmap source) {
			int size = Math.min(source.getWidth(), source.getHeight());

			int x = (source.getWidth() - size) / 2;
			int y = (source.getHeight() - size) / 2;

			Bitmap squaredBitmap = Bitmap
					.createBitmap(source, x, y, size, size);
			if (squaredBitmap != source) {
				source.recycle();
			}

			Bitmap bitmap = Bitmap.createBitmap(size, size, source.getConfig());

			Canvas canvas = new Canvas(bitmap);
			Paint paint = new Paint();
			BitmapShader shader = new BitmapShader(squaredBitmap,
					BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
			paint.setShader(shader);
			paint.setAntiAlias(true);

			float r = size / 2f;
			canvas.drawCircle(r, r, r, paint);

			squaredBitmap.recycle();
			return bitmap;
		}

		@Override
		public String key() {
			return "circle";
		}
	}

	
	public static class RoundedTransformation implements
			com.squareup.picasso.Transformation {
		private final int radius;
		private final int margin; // dp

		// radius is corner radii in dp
		// margin is the board in dp
		public RoundedTransformation(final int radius, final int margin) {
			this.radius = radius;
			this.margin = margin;
		}

		@Override
		public Bitmap transform(final Bitmap source) {
			final Paint paint = new Paint();
			paint.setAntiAlias(true);
			paint.setShader(new BitmapShader(source, Shader.TileMode.CLAMP,
					Shader.TileMode.CLAMP));

			Bitmap output = Bitmap.createBitmap(source.getWidth(),
					source.getHeight(), Config.ARGB_8888);
			Canvas canvas = new Canvas(output);
			canvas.drawRoundRect(new RectF(margin, margin, source.getWidth()
					- margin, source.getHeight() - margin), radius, radius,
					paint);

			if (source != output) {
				source.recycle();
			}
			return output;
		}

		@Override
		public String key() {
			return "rounded(radius=" + radius + ", margin=" + margin + ")";
		}
	}

}
