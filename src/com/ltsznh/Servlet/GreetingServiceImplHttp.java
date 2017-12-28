package com.ltsznh.Servlet;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.ltsznh.util.Convert;
import com.ltsznh.util.ServerLog;
import com.ltsznh.util.check;
import com.ltsznh.util.getResult;
import com.ltsznh.gwt.shared.model.ResultData;
import com.ltsznh.gwt.shared.param.PARAM;

public class GreetingServiceImplHttp extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		exe(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		exe(request, response);
	}

	@SuppressWarnings("unchecked")
	private void exe(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub

		ResultData result = null;
		HashMap<String, Object> param = null;

		try {

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

		param.put("request", request);
		param.put("response", response);

		result = check.checkOverdue(param);

		if (!result.getStatus()) {
			ServerLog.log("serverlet", "", "检查登录状态数据失败!");
			// return result;
		}

		result = getResult.get(param.get("className").toString(), param.get("methodName").toString(), param);

		response(response, result);
	}

	private void response(HttpServletResponse response, ResultData result) {
		// TODO Auto-generated method stub
		response.setContentType("text/xml; charset=UTF-8");
		PrintWriter out = null;
		try {
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
