package cn.wx.Utils;

import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

import cn.wx.Answer.WxException;
import cn.wx.Answer.XMLParse;
import cn.wx.MyData.MyData;


public class examineUtils {
	private static Base64 base64 = new Base64();
	private static byte[] aesKey;

	public examineUtils() {
		aesKey = Base64.decodeBase64(MyData.EncodingAESKey + "=");
	}

	/**
	 * 检验消息的真实性，并且获取解密后的明文.
	 * 
	 * @param msgSignature
	 *            签名串，对应URL参数的msg_signature
	 * @param timeStamp
	 *            时间戳，对应URL参数的timestamp
	 * @param nonce
	 *            随机串，对应URL参数的nonce
	 * @param postData
	 *            密文，对应POST请求的数据
	 * 
	 * @return 解密后的原文
	 * @throws WxException
	 *             执行失败，请查看该异常的错误码和具体的错误信息
	 */
	public String decryptMsg(String msgSignature, String timeStamp, String nonce, Map<String, String> map)
			throws WxException {
		String signature = SHA1.getSHA1(timeStamp, nonce, map.get("Encrypt"));
		if (!signature.equals(msgSignature)) {
			throw new WxException(WxException.ValidateSignatureError);
		}
		String result = decrypt(map.get("Encrypt"));
		return result;
	}

	/**
	 * 对密文解密
	 * 
	 * @param context
	 *            需要解密的密文
	 * @return 解密得到的明文
	 * @throws WxException
	 *             AES解密失败
	 */
	private String decrypt(String context) throws WxException {
		// TODO Auto-generated method stub
		byte[] original;
		try {
			// 设置解密模式为AES的CBC模式
			Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
			SecretKeySpec key_spec = new SecretKeySpec(aesKey, "AES");
			IvParameterSpec iv = new IvParameterSpec(Arrays.copyOfRange(aesKey, 0, 16));
			cipher.init(Cipher.DECRYPT_MODE, key_spec, iv);

			// 使用base64对密文解码
			byte[] encrypted = Base64.decodeBase64(context);

			// 解密
			original = cipher.doFinal(encrypted);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw new WxException(WxException.DecryptAESError);
		}

		String xmlContent, from_appid;
		try {
			// 去除补位字符
			byte[] bb = PKCS7Coder.decoder(original);
			// 分离16位随机字符串，网络字节序和AppID
			byte[] networkOrder = Arrays.copyOfRange(bb, 16, 20);

			int xmlLength = getByte.recoverNetworkBytesOrder(networkOrder);

			xmlContent = new String(Arrays.copyOfRange(bb, 20, 20 + xmlLength), MyData.charset);
			from_appid = new String(Arrays.copyOfRange(bb, 20 + xmlLength, bb.length), MyData.charset);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw new WxException(WxException.IllegalBuffer);
		}
		// AppID不同！！！
		if (!from_appid.equals(MyData.AppID)) {
			throw new WxException(WxException.ValidateAppidError);
		}
		return xmlContent;
	}

	/**
	 * 将公众平台回复用户的消息加密打包
	 * 
	 * @param content
	 *            公众平台待回复用户的消息，未加密的字符串
	 * @param timeStamp
	 *            时间戳，可以自己生成，也可以用URL参数的timeStamp
	 * @param nonce
	 *            随机字符串，可以自己生成，也可以用URL参数的nonce
	 * @param map
	 *            保存
	 * 
	 * @return 加密后用于回复的密文，包括signature,timeStamp,nonce,encrypt的xml字符串
	 * @throws WxException
	 *             执行失败，查看该异常的错误码和具体错误信息
	 */
	public String encryptMsg(String content, String timeStamp, String nonce, String postStr) throws WxException {
		if (timeStamp == "")
			timeStamp = Long.toString(System.currentTimeMillis());
		String result = XMLParse.generateXml(postStr, content, timeStamp, nonce);
		return result;
	}
	
	/**
	 * 对需要传递的明文进行加密.
	 * 
	 * @param context
	 *            需要加密的明文
	 * @return 加密后的base64编码的字符串
	 * @throws WxException
	 *             AES加密失败
	 */
	public static String encrypt(String randomStr, String context) throws WxException {
		ByteGroup byteCollector = new ByteGroup();
		byte[] randomStrBytes = randomStr.getBytes(MyData.charset);
		byte[] textBytes = context.getBytes(MyData.charset);
		byte[] networkBytesOrder = getByte.getNetWorkBytesOrder(textBytes.length);
		byte[] appidBytes = MyData.AppID.getBytes(MyData.charset);

		// randomStr + networkBytesOrder + text + appid
		byteCollector.addBytes(randomStrBytes);
		byteCollector.addBytes(networkBytesOrder);
		byteCollector.addBytes(textBytes);
		byteCollector.addBytes(appidBytes);

		// ... + pad: 使用自定义的填充方式对明文进行补位填充
		byte[] padBytes = PKCS7Coder.encode(byteCollector.size());
		byteCollector.addBytes(padBytes);

		// 获得最终的字节流, 未加密
		byte[] unencrypted = byteCollector.toBytes();

		try {
			// 设置加密模式为AES的CBC模式
			Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
			SecretKeySpec keySpec = new SecretKeySpec(aesKey, "AES");
			IvParameterSpec iv = new IvParameterSpec(aesKey, 0, 16);
			cipher.init(Cipher.ENCRYPT_MODE, keySpec, iv);

			// 加密
			byte[] encrypted = cipher.doFinal(unencrypted);

			// 使用BASE64对加密后的字符串进行编码
			String base64Encrypted = base64.encodeToString(encrypted);

			return base64Encrypted;
		} catch (Exception e) {
			e.printStackTrace();
			throw new WxException(WxException.EncryptAESError);
		}
	}
	
	public static boolean checkSignature(String signature, String timestamp, String nonce) {
		String[] tmpArr = new String[] { MyData.TOKEN, timestamp, nonce };
		Arrays.sort(tmpArr);
		StringBuilder content = new StringBuilder();
		for (int i = 0; i < tmpArr.length; i++) {
			content.append(tmpArr[i]);
		}
		MessageDigest md = null;
		String tmpStr = null;
		try {
			md = MessageDigest.getInstance("SHA-1");
			byte[] digest = md.digest(content.toString().getBytes());
			tmpStr = MyData.bytetoStr(digest);
		} catch (Exception e) {
			e.printStackTrace();
		}
		content = null;
		// System.out.println("checkSignture：" + tmpStr);
		return tmpStr != null ? tmpStr.equals(signature.toUpperCase()) : false;
	}
}
