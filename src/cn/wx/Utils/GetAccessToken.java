package cn.wx.Utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import cn.wx.MyData.MyData;
import net.sf.json.JSONObject;

public class GetAccessToken {
	public static String[] GetAT() {
		String[]result=new String[2];
		String json=null;
		json=Getting();
		JSONObject jsonObject=JSONObject.fromObject(json);
		result[0]=jsonObject.getString("access_token");
		System.out.println(result[0]);
		result[1]=jsonObject.getString("expires_in");
		return result;
	}
	private static String Getting() {
		String url ="https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%1$s&secret=%2$s";
		url=String.format(url, MyData.AppID,MyData.AppSecret);
		BufferedReader in = null; 
        StringBuffer sb = new StringBuffer();
        try {  
            URL realUrl = new URL(url);  
            // 打开和URL之间的连接  
            URLConnection connection = realUrl.openConnection();  
            // 设置通用的请求属性  
            connection.setRequestProperty("accept", "*/*");  
            connection.setRequestProperty("connection", "Keep-Alive");  
            connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");  
            connection.setConnectTimeout(5000);  
            connection.setReadTimeout(5000);  
            // 建立实际的连接  
            connection.connect();  
            // 定义 BufferedReader输入流来读取URL的响应  
            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));    
            String line;  
            while ((line = in.readLine()) != null) {  
                sb.append(line);  
            }  
        } catch (Exception e) {  
           e.printStackTrace();
        }  
        // 使用finally块来关闭输入流  
        finally {  
            try {  
                if (in != null) {  
                    in.close();  
                }  
            } catch (Exception e2) {  
                e2.printStackTrace();  
            }  
        } 
        return sb.toString();
	}
}
