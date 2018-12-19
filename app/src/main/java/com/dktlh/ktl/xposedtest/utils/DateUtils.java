package com.dktlh.ktl.xposedtest.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 
 * @author wangkai
 * @2016年4月7日 下午12:53:55
 * @desc:日期工具
 */
/**
 * @author wangkai
 * @2016年6月23日 下午10:22:31
 * @desc:
 */
public class DateUtils {

	/**
	 * 获取当前系统的时间戳
	 * 
	 * @return
	 */
	public static String getCurrentTimestamp() {

		long timeStamp = new Date().getTime();
		return String.valueOf(timeStamp);
	}

	public static String getCurrentTimestamp10() {

		long timeStamp = new Date().getTime() / 1000;
		String timestr = String.valueOf(timeStamp);
		return timestr;
	}

	public static String getTimeStamp() {
		int time = (int) (System.currentTimeMillis() / 1000);
		return String.valueOf(time);
	}

	/*
	 * public static void main(String[] args) throws JsonGenerationException,
	 * JsonMappingException, IOException {
	 * System.out.println(DateUtils.getTimeStamp()); Map<String,String> map =
	 * Maps.newHashMap(); map.put("package", "Sign=Wxpay"); ObjectMapper mapper
	 * = new ObjectMapper(); String str = mapper.writeValueAsString(map);
	 * System.out.println(str); }
	 */

	/**
	 * 时间戳转日期
	 * @param ms
	 * @return
	 */
	public static Date transForDate(Long ms){
		if(ms == null){
			ms = 0L;
		}
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date temp=null;
		if(ms!=null){
			try {
				String str=sdf.format(ms);
				temp=sdf.parse(str);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return temp;
	}

	public static String transForString(Long ms) {
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//这个是你要转成后的时间的格式
		String sd = sdf.format(new Date(ms));   // 时间戳转换成时间
		return sd;
	}

	public static String transForString2(Long ms) {
		SimpleDateFormat sdf=new SimpleDateFormat("HH:mm:ss");//这个是你要转成后的时间的格式
		String sd = sdf.format(new Date(ms));   // 时间戳转换成时间
		return sd;
	}

}
