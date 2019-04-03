/**
 * FileName:     ${JudgeUnicodeAndChinese.java}
 * @Description: ${todo}(Unicode中文转换)
 * All rights Reserved, Designed By ZTE-ITS
 * Copyright:    Copyright(C) 2010-2014
 * Company       ZTE-ITS WuXi LTD.
 * @author:    zhangneng
 * @version    V1.0 
 * Createdate:         ${date} ${time}
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * ${date}       wu.zh          1.0             1.0
 * Why & What is modified: <修改原因描述>
 */
package cn.com.cmplatform.utils;

public class UtilsJudgeUnicodeAndChinese {
	public String chineseToUnicode(String str) {
		char[] arChar = str.toCharArray();
		int iValue = 0;
		StringBuffer uStr = new StringBuffer();
		for (int i = 0; i < arChar.length; i++) {
			iValue = (int) str.charAt(i);
			uStr.append("&#" + iValue + ";");
		}
		return uStr.toString();
	}

	public String unicodeToChinese(String str) {
		String[] strings = str.split(";");
		StringBuffer aStr = new StringBuffer();
		for (int i = 0; i < strings.length; i++) {
			String s = strings[i].replace("&#", "");
			aStr.append((char) Integer.parseInt(s));
		}
		return aStr.toString();
	}
}
