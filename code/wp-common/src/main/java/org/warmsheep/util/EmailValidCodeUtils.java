package org.warmsheep.util;

import java.util.Random;

/**
 * 
 * @author han
 * 
 */
public class EmailValidCodeUtils {

	/**
	 * 基础字符
	 * 
	 * @param cellphoneNo
	 * @return
	 */
	private static char[] CODE_SEQUENCE = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',  
            'K', 'L', 'M', 'N',  'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',  
            'X', 'Y', 'Z',  '1', '2', '3', '4', '5', '6', '7', '8', '9' };

	public static String TEST_STRING = null;
	
	public static int CODE_COUNT = 4;
	/**
	 * 隐藏卡号信息
	 * 
	 * @param cardNo
	 * @return
	 */
	public static String createEmailValidCode() {
		if(TEST_STRING != null) return TEST_STRING;
		// randomCode记录随机产生的验证码  
        StringBuffer randomCode = new StringBuffer(); 
        // 生成随机数  
        Random random = new Random();
        // 随机产生codeCount个字符的验证码。  
        for (int i = 0; i < CODE_COUNT; i++) {  
            String strRand = String.valueOf(CODE_SEQUENCE[random.nextInt(CODE_SEQUENCE.length)]);  
            // 将产生的四个随机数组合在一起。  
            randomCode.append(strRand);  
        }  
        // 将四位数字的验证码保存到Session中。  
        return randomCode.toString();       
	}

}
