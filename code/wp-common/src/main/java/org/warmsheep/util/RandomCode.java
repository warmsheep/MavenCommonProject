package org.warmsheep.util;

/**
 * 随机数生成
 * 
 * @author tianjing
 * @date 2015年7月1日 下午6:54:55
 * @version 2.0
 */
public class RandomCode
{
	/**
	 * 产生一个指定长度的随机数
	 * 
	 * @param numberFlag 随机数是否只是数字标识
	 * @param length 随机数长度
	 * @return 随机数
	 * @author tianjing
	 * @date 2015年7月1日 下午7:27:38
	 */
	public static final String buildRandom(boolean numberFlag, int length)
	{
		String strTable = numberFlag ? "123456789" : "123456789abcdefghijkmnpqrstuvwxyz";
		int len = strTable.length();
		StringBuffer retStr = new StringBuffer();
		String str;
		for (int i = 0; i < length; i++)
		{
			double dblR = Math.random() * len;
			str = String.valueOf(dblR);
			int intR = Integer.parseInt(str.substring(0, str.indexOf(".")));
			retStr.append(strTable.charAt(intR));
		}
		return retStr.toString();
	}
}
