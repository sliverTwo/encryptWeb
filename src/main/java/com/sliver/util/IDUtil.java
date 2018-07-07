/**  

* <p>Title: UUIDUtil.java</p>  

* <p>Description: </p>  

* <p>Copyright: Copyright (c) 2017</p>  

* <p>Company: www.iipcloud.com</p>  

* @author 肖晓霖  

* @date 2018年6月8日  

* @version 1.0  

*/  
package com.sliver.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**  
* <p>Title: UUIDUtil</p>  
* <p>Description:生成唯一32位ID </p>  
* @author 肖晓霖  
* @date 2018年6月8日  
*/
public class IDUtil {
	/**
	 * 
	 * <p>Title: getUUID</p>  
	 * <p>Description: 获取UUID</p>  
	 * @return
	 */
	public static String getUUID(){
		return UUID.randomUUID().toString().replace("-", "");
	}
	
	/**
	 * 
	 * <p>Title: generateCode</p>  
	 * <p>Description:生成工单编号 </p>  
	 * @return
	 */
	public static synchronized String  generateCode(){
		try {
			Thread.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return new SimpleDateFormat("yyMMddHHmmss-SSS").format(new Date());
	}
	
	/**
	 * 
	 * <p>Title: generateRepairCode</p>  
	 * <p>Description: 生成维修工单编号</p>  
	 * @return
	 */
	public static String generateRepairCode(){
		return "WX".concat(generateCode());
	}
	
	/**
	 * 
	 * <p>Title: generateCheckCode</p>  
	 * <p>Description: 生成点检工单编号</p>  
	 * @return
	 */
	public static String generateCheckCode(){
		return "DJ".concat(generateCode());
	}
}
