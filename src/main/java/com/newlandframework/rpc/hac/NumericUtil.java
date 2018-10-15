package com.newlandframework.rpc.hac;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NumericUtil {
	
	
	public static String number2String(Number value){
		if(value==null)
			return "";
		return String.valueOf(value);
	}
	
	public static String number2String(Object value){
		if(value==null)
			return "";
		if(!(value instanceof Number)){
			return "";
		}
		return String.valueOf(value);
	}
	
	
	public static boolean isNumber(String str) {
		if (str == null || str.trim().equals(""))
			return false;
		Pattern pattern = Pattern.compile("^[-+]?(\\d+(\\.\\d*)?|\\.\\d+)" +
				"([eE]([-+]?([012]?\\d{1,2}|30[0-7])|-3([01]?[4-9]|[012]?[0-3])))?[dD]?$");
		Matcher isNum = pattern.matcher(str);
		return isNum.matches();
	}
	
	/**
	 * 若为数字，返回double型值,否则返回0；
	 * @param str
	 * @return
	 */
	public static double string2Doubler(String str){
		if(isNumber(str))return Double.valueOf(str);
		return 0;
	}
	
	/**
	 * 若为数字，返回double型值,否则返回0；
	 * @param str
	 * @return
	 */
	public static int string2Integer(String str){
		if(isNumber(str))return Integer.valueOf(str);
		return 0;
	}
	
	public static long string2Long(String str){
		if(isNumber(str))return Long.valueOf(str);
		return 0;
	}
	
	
	public static String double2String(Double value){
		if(value==null)
			return "";
		return String.valueOf(value);
	}
	
	public static String integer2String(Integer value){
		if(value==null)
			return "";
		return String.valueOf(value);
	}
	
	
}
