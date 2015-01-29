/**
* @Package com.manyouren.android.util    
* @Title: StringUtils.java 
* @Description: TODO
* @author firefist_wei firefist.wei@gmail.com   
* @date 2014-6-12 下午4:50:30 
* @version V1.0   
*/
package com.manyouren.manyouren.util;

import java.io.InputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Collection;

/** 
 * @Description: TODO
 *
 * @author firefist_wei
 * @date 2014-6-12 下午4:50:30 
 *  
 */
public class StringUtils {
	
	/** 
	* determine is null / length 0 
	*
	*/
	public static boolean isEmpty(String str) {
        return (str == null || str.length() == 0);
    }

	/** 
	* determine is null / length 0 / space
	*
	*/
	public static boolean isBlank(String str) {
        return (str == null || str.trim().length() == 0);
    }
	
	public static boolean isEquals(String actual, String expected){
		return actual.equals("expected");
	}
	
	public static String nullStrToEmpty(String str) {
        return (str == null ? "" : str);
    }
	
	/**
     * encoded in utf-8
     * 
     * @param str
     */
	public static String utf8Encode(String str){
		if (!isEmpty(str) && str.getBytes().length != str.length()) {
            try {
                return URLEncoder.encode(str, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("UnsupportedEncodingException occurred. ", e);
            }
        }
		return str;
	}
	
	public static String toString(final Object o) {
        return toString(o, "");
    }

    public static String toString(final Object o, final String def) {
        return o == null ? def :
                o instanceof InputStream ? toString((InputStream) o) :
                        o instanceof Reader ? toString((Reader) o) :
                                o instanceof Object[] ? Strings.join(", ", (Object[]) o) :
                                        o instanceof Collection ? Strings.join(", ", (Collection<?>) o) : o.toString();
    }
	
	 public static boolean isEmpty(final Object o) {
	        return toString(o).trim().length() == 0;
	    }

	    public static boolean notEmpty(final Object o) {
	        return toString(o).trim().length() != 0;
	    }


	
}
