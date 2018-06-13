package cn.wx.MyInterface;

public interface Coder {
	/**
	 * 获得对明文补位填充的字节
	 * 
	 * @param count 需要进行填充操作的明文字节个数
	 * @return 补齐用字节数组
	 * */
	public static byte[] encoder(int count) {
		return null;
	}
	/**
	 * 删除解密后明文的补位字符
	 * 
	 * @param decrypted 解密后的明文
	 * @return 删除补位字符后的明文 
	 * */
	public static byte[] decoder(byte[] decrypted) {
		return null;
	}
}
