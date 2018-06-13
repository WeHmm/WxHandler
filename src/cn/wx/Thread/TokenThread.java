package cn.wx.Thread;

import cn.wx.MyData.MyData;

public class TokenThread implements Runnable {

	public TokenThread() {
		// TODO Auto-generated constructor stub

	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println("Thread Started");
		while (true) {
			try {
				MyData.setAccessToken();
				Thread.sleep((MyData.Timeout-100) * 1000);
			} catch (Exception e) {
				// TODO: handle exception
				try {
					Thread.sleep(60 * 1000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				e.printStackTrace();
			}
		}
	}

}
