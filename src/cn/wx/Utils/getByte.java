package cn.wx.Utils;

import java.util.Random;

public class getByte  {
	/**
	 * 生成4个字节的网络字节序
	 */
	public static byte[] getNetWorkBytesOrder(int sourceNumber) {
		byte[] orderByte = new byte[4];
		orderByte[3] = (byte) (sourceNumber & 0xFF);
		orderByte[2] = (byte) (sourceNumber >> 8 & 0xFF);
		orderByte[1] = (byte) (sourceNumber >> 16 & 0xFF);
		orderByte[0] = (byte) (sourceNumber >> 24 & 0xFF);
		return orderByte;
	}

	/**
	 * 还原4个字节的网络字节序
	 */
	public static int recoverNetworkBytesOrder(byte[] orderByte) {
		int sourceNumber = 0;
		for (int i = 0; i < 4; i++) {
			sourceNumber <<= 8;
			sourceNumber |= orderByte[i] & 0xff;
		}
		return sourceNumber;
	}

	/**
	 * 随机生成16位字符串
	 */
	public static String getRandomStr() {
		String base = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
		Random random = new Random();
		StringBuilder sBuilder = new StringBuilder();
		for (int i = 0; i < 16; i++) {
			int num = random.nextInt(base.length());
			sBuilder.append(base.charAt(num));
		}
		return sBuilder.toString();
	}
}
