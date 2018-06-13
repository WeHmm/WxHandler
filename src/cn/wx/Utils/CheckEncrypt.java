package cn.wx.Utils;

import java.util.Map;

import cn.wx.Answer.XMLParse;
import cn.wx.MyData.MyData;

public class CheckEncrypt {
	public static String encryptMsgProcess(String msgSignature,String content, String timestamp, String nonce, examineUtils eUtil,
		Map<String, String> map) throws Exception {
		String result =eUtil.decryptMsg(msgSignature, timestamp, nonce, map);
		MyData.encryptFlag = true;
		String encrypt = eUtil.encryptMsg(content, timestamp, nonce,result);
		return encrypt;
	}

	public static String unencryptedMsgProcess(String content, Map<String, String> map) throws Exception {
		String result = XMLParse.generateXml1(map.get("fromUserName"), map.get("toUserName"), content);
		return result;
	}
}
