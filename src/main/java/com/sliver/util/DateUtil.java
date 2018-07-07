/**  
* <p>Title: DateUtil.java</p>  
* <p>Description: </p>  
* <p>Copyright: Copyright (c) 2018</p>  
* <p>Company: www.iipcloud.com</p>  
* @author 肖晓霖  
* @date 2018年6月19日  
* @version 1.0  
*/  
package com.sliver.util;

import java.text.SimpleDateFormat;
import java.util.Date;


/**  
* <p>Title: DateUtil</p>  
* <p>Description: </p>  
* @author 肖晓霖  
* @date 2018年6月19日  
*/
public class DateUtil {
	/**
	 * 
	 * <p>Title: isSameDay</p>  
	 * <p>Description: 判断两个日期是否是同一天</p>  
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static boolean isSameDay(Date date1, Date date2){
		if(new SimpleDateFormat("yyyy-MM-dd").format(date1).equals(new SimpleDateFormat("yyyy-MM-dd").format(date2))){
			return true;
		}else{
			return false;
		}
	}
}
