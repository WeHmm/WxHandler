package cn.wx.Utils;

import java.util.Arrays;

import cn.wx.MyData.MyData;
import cn.wx.MyInterface.Coder;

public class PKCS7Coder implements Coder {
	private final static int block_size = 32;

	public static byte[] encode(int count) {
		// 计算需要填充的位数
		int amountToPad = block_size - (count % block_size);
		if (amountToPad == 0) {
			amountToPad = block_size;
		}
		// 获得补位所用的字符
		char padChr = chr(amountToPad);
		String tmp = new String();
		for (int index = 0; index < amountToPad; index++) {
			tmp += padChr;
		}
		return tmp.getBytes(MyData.charset);
	}

	/**
	 * 删除解密后明文的补位字符
	 * 
	 * @param decrypted 解密后的明文
	 * @return 删除补位字符后的明文
	 */
	public static byte[] decoder(byte[] decrypted) {
		int pad = (int) decrypted[decrypted.length - 1];
		if (pad < 1 || pad > 32) {
			pad = 0;
		}
		return Arrays.copyOfRange(decrypted, 0, decrypted.length - pad);
	}

	/**
	 * 将数字转化成ASCII码对应的字符，用于对明文进行补码
	 * 
	 * @param a 需要转化的数字
	 * @return 转化得到的字符
	 */
	public static char chr(int a) {
		byte target = (byte) (a & 0xFF);
		return (char) target;
	}
}
