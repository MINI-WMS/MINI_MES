package com.ltsznh.Servlet;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.ltsznh.util.Convert;
import com.ltsznh.util.IP;
import com.ltsznh.util.ServerLog;
import com.ltsznh.util.Tools;
import com.ltsznh.util.get;
import com.ltsznh.util.getResult;
import com.ltsznh.gwt.shared.model.ResultData;
import com.ltsznh.gwt.shared.param.PARAM;

public class SignInServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// System.out.println("==SignIn get");
		signin(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// System.out.println("==SignIn get");
		signin(request, response);
	}

	@SuppressWarnings("unchecked")
	private void signin(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		ResultData result = null;
		HashMap<String, Object> param = null;
		try {
			// 处理中文乱码问题解决办法
			String br = URLDecoder.decode(
					Convert.inputStreamToString(
							new InputStreamReader((ServletInputStream) request.getInputStream(), PARAM.ENCODING)),
					PARAM.ENCODING);
			param = new Gson().fromJson(br, HashMap.class);
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
			result = new ResultData();
			result.setStatus(false);
			result.setMessage(e2.getLocalizedMessage());
			response(response, result);
			return;
		}

		if (param == null) {
			result = new ResultData();
			result.setStatus(false);
			result.setMessage("参数错误");
			response(response, result);
			return;
		}

		// 跟踪IP
		String ipString = get.getPIP(request);
		param.put("ip", ipString);

		String idString;
		if (!param.containsKey("id")) {
			idString = Tools.getRandomIdString();
			param.put("id", idString);
		} else {
			idString = param.get("id").toString();
		}

		param.put("request", request);

		if (param.get("client_type") == null)
			param.put("client_type", "M");

		String fromClass = param.get("fromClass").toString();
		String className = param.get("className").toString();
		String methodName = param.get("methodName").toString();

		if (!classFilter(className)) {
			result = new ResultData();
			result.setStatus(false);
			result.setMessage("非法入侵！");
			ServerLog.err("Servlet", "非法入侵", IP.getPIP(request) + fromClass + " " + className + " " + methodName);
			response(response, result);
		}

		response(response, getResult.get(className, methodName, param));
	}

	private static String[] classString = new String[] { "root.login"// 同步用户登录状态
	};
	private static List<String> classList = Arrays.asList(classString);// 转换为list

	private boolean classFilter(String className) {
		// TODO Auto-generated method stub
		// 利用list的包含方法,进行判断
		if (classList.contains(className)) {
			return true;
		} else {
			return false;
		}
	}

	private void response(HttpServletResponse response, ResultData result) {
		// TODO Auto-generated method stub
		response.setContentType("text/xml; charset=UTF-8");
		PrintWriter out = null;
		try {
			if (!result.getStatus())
				ServerLog.err("Sever SignIn", "error", result.getMessage());
			out = response.getWriter();
			out.print(new Gson().toJson(result));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			out.flush();
			out.close();
		}
	}

}
