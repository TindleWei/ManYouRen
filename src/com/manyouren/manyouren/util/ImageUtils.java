/**
* @Package com.manyouren.android.util    
* @Title: ImageUtils.java 
* @Description: TODO
* @author firefist_wei firefist.wei@gmail.com   
* @date 2014-6-10 上午11:17:13 
* @version V1.0   
*/
package com.manyouren.manyouren.util;

import static android.graphics.Bitmap.Config.ARGB_8888;
import static android.graphics.Color.WHITE;
import static android.graphics.PorterDuff.Mode.DST_IN;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Image utilities
 */
public class ImageUtils {

    private static final String TAG = "ImageUtils";

    /**
     * Get a bitmap from the image path
     *
     * @param imagePath
     * @return bitmap or null if read fails
     */
    public static Bitmap getBitmap(final String imagePath) {
        return getBitmap(imagePath, 1);
    }

    /**
     * Get a bitmap from the image path
     *
     * @param imagePath
     * @param sampleSize
     * @return bitmap or null if read fails
     */
    public static Bitmap getBitmap(final String imagePath, int sampleSize) {
        final Options options = new Options();
        options.inDither = false;
        options.inSampleSize = sampleSize;

        RandomAccessFile file = null;
        try {
            file = new RandomAccessFile(imagePath, "r");
            return BitmapFactory.decodeFileDescriptor(file.getFD(), null,
                    options);
        } catch (IOException e) {
        	Logot.outDebug(TAG, e.getMessage(), e);
            return null;
        } finally {
            if (file != null)
                try {
                    file.close();
                } catch (IOException e) {
                	Logot.outDebug(TAG, e.getMessage(), e);
                }
        }
    }

    /**
     * Get a bitmap from the image
     *
     * @param image
     * @param sampleSize
     * @return bitmap or null if read fails
     */
    public static Bitmap getBitmap(final byte[] image, int sampleSize) {
        final Options options = new Options();
        options.inDither = false;
        options.inSampleSize = sampleSize;
        return BitmapFactory.decodeByteArray(image, 0, image.length, options);
    }

    /**
     * Get scale for image of size and max height/width
     *
     * @param size
     * @param width
     * @param height
     * @return scale
     */
    public static int getScale(Point size, int width, int height) {
        if (size.x > width || size.y > height)
            return Math.max(Math.round((float) size.y / (float) height),
                    Math.round((float) size.x / (float) width));
        else
            return 1;
    }

    /**
     * Get size of image
     *
     * @param imagePath
     * @return size
     */
    public static Point getSize(final String imagePath) {
        final Options options = new Options();
        options.inJustDecodeBounds = true;

        RandomAccessFile file = null;
        try {
            file = new RandomAccessFile(imagePath, "r");
            BitmapFactory.decodeFileDescriptor(file.getFD(), null, options);
            return new Point(options.outWidth, options.outHeight);
        } catch (IOException e) {
        	Logot.outDebug(TAG, e.getMessage(), e);
            return null;
        } finally {
            if (file != null)
                try {
                    file.close();
                } catch (IOException e) {
                	Logot.outDebug(TAG, e.getMessage(), e);
                }
        }
    }

    /**
     * Get size of image
     *
     * @param image
     * @return size
     */
    public static Point getSize(final byte[] image) {
        final Options options = new Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(image, 0, image.length, options);
        return new Point(options.outWidth, options.outHeight);
    }

    /**
     * Get bitmap with maximum height or width
     *
     * @param imagePath
     * @param width
     * @param height
     * @return image
     */
    public static Bitmap getBitmap(final String imagePath, int width, int height) {
        Point size = getSize(imagePath);
        return getBitmap(imagePath, getScale(size, width, height));
    }

    /**
     * Get bitmap with maximum height or width
     *
     * @param image
     * @param width
     * @param height
     * @return image
     */
    public static Bitmap getBitmap(final byte[] image, int width, int height) {
        Point size = getSize(image);
        return getBitmap(image, getScale(size, width, height));
    }

    /**
     * Get bitmap with maximum height or width
     *
     * @param image
     * @param width
     * @param height
     * @return image
     */
    public static Bitmap getBitmap(final File image, int width, int height) {
        return getBitmap(image.getAbsolutePath(), width, height);
    }

    /**
     * Get a bitmap from the image file
     *
     * @param image
     * @return bitmap or null if read fails
     */
    public static Bitmap getBitmap(final File image) {
        return getBitmap(image.getAbsolutePath());
    }

    /**
     * Load a {@link Bitmap} from the given path and set it on the given
     * {@link ImageView}
     *
     * @param imagePath
     * @param view
     */
    public static void setImage(final String imagePath, final ImageView view) {
        setImage(new File(imagePath), view);
    }

    /**
     * Load a {@link Bitmap} from the given {@link File} and set it on the given
     * {@link ImageView}
     *
     * @param image
     * @param view
     */
    public static void setImage(final File image, final ImageView view) {
        Bitmap bitmap = getBitmap(image);
        if (bitmap != null)
            view.setImageBitmap(bitmap);
    }

    /**
     * Round the corners of a {@link Bitmap}
     *
     * @param source
     * @param radius
     * @return rounded corner bitmap
     */
    public static Bitmap roundCorners(final Bitmap source, final float radius) {
        int width = source.getWidth();
        int height = source.getHeight();

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(WHITE);

        Bitmap clipped = Bitmap.createBitmap(width, height, ARGB_8888);
        Canvas canvas = new Canvas(clipped);
        canvas.drawRoundRect(new RectF(0, 0, width, height), radius, radius,
                paint);
        paint.setXfermode(new PorterDuffXfermode(DST_IN));

        Bitmap rounded = Bitmap.createBitmap(width, height, ARGB_8888);
        canvas = new Canvas(rounded);
        canvas.drawBitmap(source, 0, 0, null);
        canvas.drawBitmap(clipped, 0, 0, paint);

        source.recycle();
        clipped.recycle();

        return rounded;
    }
    
    /** 
    * convert Bitmap to byte array
    *
    * @param b
    * @return byte[]
    */
    public static byte[] bitmapToByte(Bitmap b){
    	if(b == null){
    		return null;
    	}
    	ByteArrayOutputStream o = new ByteArrayOutputStream();
    	b.compress(Bitmap.CompressFormat.PNG, 100, o);
    	return o.toByteArray();
    }
    
    /** 
    * convert byte array to Bitmap
    *
    * @param b
    * @return Bitmap
    */
    public static Bitmap byteToBitmap(byte[] b){
    	return (b == null || b.length == 0) ? null :
    		BitmapFactory.decodeByteArray(b, 0, b.length);
    }
    
    /** 
    * convert Drawable to Bitmap
    *
    * @param d
    * @return Bitmap
    */
    public static Bitmap drawableToBitmap(Drawable d){
    	return d == null ? null :((BitmapDrawable)d).getBitmap();
    }
    
    /**
     * convert Bitmap to Drawable
     * 
     * @param b
     * @return
     */
    public static Drawable bitmapToDrawable(Bitmap b) {
        return b == null ? null : new BitmapDrawable(b);
    }
    
    /**
     * convert Drawable to byte array
     * 
     * @param d
     * @return
     */
    public static byte[] drawableToByte(Drawable d) {
        return bitmapToByte(drawableToBitmap(d));
    }

    /**
     * convert byte array to Drawable
     * 
     * @param b
     * @return
     */
    public static Drawable byteToDrawable(byte[] b) {
        return bitmapToDrawable(byteToBitmap(b));
    }

}














