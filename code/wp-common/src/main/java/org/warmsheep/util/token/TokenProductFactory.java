package org.warmsheep.util.token;

/**
 * ClassName: TokenProductFactory <br/>
 * Function: 生成Token 类 ，用于不同平台间安全验证<br/>
 * date: 2014-8-5 下午8:25:58 <br/>
 * 
 * @author laich
 */
public class TokenProductFactory {
	
	 public final static String key="gzzyzz";
	
	/**
	 * 放入各种定制的参数，生产Token
	 * @param pramaters
	 * @return
	 */
	public static String productToken(String[]  pramaters){
		if(pramaters==null || pramaters.length==0){
			return null;
		}else{
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < pramaters.length; i++) {
				sb.append(pramaters[i]+"-");
			}
			sb.append(key);//最后加上Key值
			return TokenToolEncrypter.encrypt(sb.toString());
		}
	}
	/**
	 * 放入各种定制的参数，生产Token
	 * @param userNo
	 * @return
	 */
	public static String productToken(String pix,String userNo){
		return TokenToolEncrypter.encrypt(pix+"-"+userNo+"-"+System.currentTimeMillis()+"-"+key);
	}
	public static void main(String[] args) {
		String[] s={"gw","1288888888@qq.com"};
		String ss=productToken("GATEPAY","129999999999");
		System.out.println("加密后的值："+productToken(s));
		String gg=TokenToolEncrypter.decrypt(ss);
		System.out.println("解密后的值："+gg);
	}
}
