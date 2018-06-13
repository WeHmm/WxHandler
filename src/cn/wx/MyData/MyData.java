package cn.wx.MyData;

import java.nio.charset.Charset;

import cn.wx.Utils.GetAccessToken;

public class MyData {

	/** 用户自定TOKEN*/
	public final static String TOKEN = "pppa20180608";
	/** 管理员ID*/
	public final static String AppID = "wxe127d9b9fe06cf6d";
	/** 管理员密码*/
	public final static String AppSecret = "f8593e6cdd6a11ec4519a7cbd8b9f7d2";
	/**
	 * access_token获取地址
	 * https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET
	 */
	public static String AccessToken = null;
	public static int Timeout=0;
	/**
	 * 微信公众平台开发者设置的EncodingAESKey
	 */
	public final static String EncodingAESKey = "7CSZu9Gxp4plMy7iUI63WvfIlToUhkl93urJCYdMOMW";
	/**
	 * 定义字符集为UTF-8
	 */
	public final static Charset charset = Charset.forName("utf-8");

	public static int MsgID=0;
	/**
	 * 定义一个flag用于判断当前公众平台消息加密模式，若为true则公众平台消息加密模式为密文或兼容模式 ，此时发送加密XML数据包，若为 false
	 * 公众平台消息加密模式为明文模式 ，此时发送不加密的XML数据包
	 */
	public static boolean encryptFlag = true;

	public static String getAccessToken() {
		return AccessToken;
	}

	public static void setAccessToken() {
		String []result=GetAccessToken.GetAT();
		AccessToken = result[0];
		Timeout=Integer.parseInt(result[1]);
		System.out.println(AccessToken);
	}

	public static String bytetoStr(byte[] bb) {
		String strDigest = "";
		for (int i = 0; i < bb.length; i++) {
			strDigest += byteToHexStr(bb[i]);
		}
		return strDigest;
	}

	public static String byteToHexStr(byte b) {
		char[] Digit = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
		char[] tempArr = new char[2];
		tempArr[0] = Digit[(b >>> 4) & 0X0F];
		tempArr[1] = Digit[b & 0X0F];
		String s = new String(tempArr);
		return s;

	}

}
