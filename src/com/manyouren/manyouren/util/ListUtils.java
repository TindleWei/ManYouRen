/**
* @Package com.manyouren.android.util    
* @Title: ListUtils.java 
* @Description: TODO
* @author firefist_wei firefist.wei@gmail.com   
* @date 2014-6-26 下午3:43:54 
* @version V1.0   
*/
package com.manyouren.manyouren.util;

import java.util.Date;
import java.util.List;

/** 
 * @Description: TODO
 *
 * @author firefist_wei
 * @date 2014-6-26 下午3:43:54 
 *  
 */
public class ListUtils {
	
	/** 
	* Bubble sort
	*
	* @param list
	* @return List<Date>
	*/
	public static List<Date> sortDateList(List<Date> list){
		Date temp = null;
		
		for(int i = 0; i < list.size(); i++){
			for(int j = i; j < list.size()-1-i; j++){
				
				if(list.get(j).getTime()>list.get(j+1).getTime()){
					temp = list.get(j+1);
					list.set(j, list.get(j+1));
					list.set(j+1, temp);
				}
				
			}
		}
		return list;
	}

}
