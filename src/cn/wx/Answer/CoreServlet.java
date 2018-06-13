package cn.wx.Answer;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.wx.MyData.MyData;
import cn.wx.Thread.TokenThread;
import cn.wx.Utils.CheckEncrypt;
import cn.wx.Utils.examineUtils;

/**
 * Servlet implementation class CoreServlet
 */
@WebServlet("/CoreServlet")
public class CoreServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Map<String, String> map;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public CoreServlet() {
		super();
		// TODO Auto-generated constructor stub
		System.out.println("running");
	}

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		// TODO Auto-generated method stub
		new Thread(new TokenThread()).start();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub

		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");

		String signature = request.getParameter("signature");
		String timestamp = request.getParameter("timestamp");
		String nonce = request.getParameter("nonce");
		String echostr = request.getParameter("echostr");

		if (echostr != null) {
			if (examineUtils.checkSignature(signature, timestamp, nonce)) {
				response.getWriter().print(echostr);
			}
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub

		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");

		String msgSignature = request.getParameter("msg_signature");
		String timestamp = request.getParameter("timestamp");
		String nonce = request.getParameter("nonce");

		String postStr = XMLParse.readStreamParameter(request.getInputStream());
		try {
			map = XMLParse.extract(postStr);
			examineUtils eUtils = new examineUtils();
			if (MyData.encryptFlag) {
				String echoStr = CheckEncrypt.encryptMsgProcess(msgSignature,"默认回复", timestamp, nonce, eUtils, map);
				response.getWriter().print(echoStr);
			} else {

			}

		} catch (Exception e) {
			// TODO: handle exception
		}
	}

}
