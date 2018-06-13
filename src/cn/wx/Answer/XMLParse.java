package cn.wx.Answer;

import org.dom4j.DocumentHelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletInputStream;

import org.dom4j.Document;
import org.dom4j.Element;

import cn.wx.MyData.MyData;
import cn.wx.Utils.SHA1;
import cn.wx.Utils.examineUtils;
import cn.wx.Utils.getByte;

public class XMLParse {

	/**
	 * 提取出XML数据包中的信息
	 * 
	 * @param postStr
	 *            待提取的XML字符串
	 * @param uncoderFlag
	 *            用于判断XML加密模式
	 * @throws WxException
	 */
	public static Map<String, String> extract(String postStr) throws WxException {
		Map<String, String> map = new HashMap<String, String>();
		if (null != postStr && !postStr.isEmpty()) {
			Document document = null;
			try {
				document = DocumentHelper.parseText(postStr);
				if (null == document) {
					return null;
				}
				Element root = document.getRootElement();
				List<Element> elements = root.elements();
				for (Element e : elements) {
					System.out.println("add map key:" + e.getName() + " value:" + e.getText());
					map.put(e.getName(), e.getText());
				}
				if (root.element("Encrypt") != null) {
					MyData.encryptFlag = true;
				} else {
					MyData.encryptFlag = false;
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
		return map;
	}

	/**
	 * 获取微信发送过来的XML数据包
	 * 
	 * @param in
	 *            获取request请求对象
	 * @return XML数据包导出的XML字符串
	 */
	public static String readStreamParameter(ServletInputStream in) {
		StringBuilder buffer = new StringBuilder();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(in));
			String line = null;
			while ((line = reader.readLine()) != null) {
				buffer.append(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != reader) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return buffer.toString();
	}

	/**
	 * 生成XML密文消息（安全模式，兼容模式）
	 *
	 * @param toUserName
	 *            回复帐号（一个OpenID）
	 * @param fromUserName
	 *            开发者微信号
	 * @param content
	 *            回复的明文消息
	 * @param signature
	 *            安全签名
	 * @param nonce
	 *            随机字符串
	 * @return 生成的XML字符串
	 * @throws WxException 
	 */
	public static String generateXml(String postStr, String content, String timestamp, String nonce) throws WxException {
		Map<String, String> map=extract(postStr);
		String unencrypt = generateXml1(map.get("FromUserName"), map.get("ToUserName"), content);
		String encrypt=examineUtils.encrypt(getByte.getRandomStr(),unencrypt);
		String signature=SHA1.getSHA1(timestamp, nonce, encrypt);
		String format = "<xml>\n" + "<Encrypt><![CDATA[%1$s]]></Encrypt>\n"
				+ "<MsgSignature><![CDATA[%2$s]]></MsgSignature>\n" + "<TimeStamp>%3$s</TimeStamp>\n"
				+ "<Nonce><![CDATA[%4$s]]></Nonce>\n" + "</xml>";
		return String.format(format, encrypt, signature, timestamp, nonce);
	}

	/**
	 * 生成XML明文消息（明文模式）
	 * 
	 *
	 * @param toUserName
	 *            回复帐号（一个OpenID）
	 * @param fromUserName
	 *            开发者微信号
	 * @param content
	 *            回复的明文消息
	 * @return 生成的XML字符串
	 */
	public static String generateXml1(String toUserName, String fromUserName, String content) {
		System.out.println("toUserName"+toUserName);
		System.out.println("fromUserName"+fromUserName);
		String format = "<xml><ToUserName><![CDATA[%s]]></ToUserName>" + "<FromUserName><![CDATA[%s]]></FromUserName>"
				+ "<CreateTime>%s</CreateTime>" + "<MsgType><![CDATA[text]]></MsgType>"
				+ "<Content><![CDATA[%s]]></Content>" + "<MsgId>%s</MsgId></xml>";
		return String.format(format, toUserName, fromUserName, new Date().getTime(), content, MyData.MsgID++);
	}
}
