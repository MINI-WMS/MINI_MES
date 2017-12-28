package com.ltsznh.util;

import java.lang.reflect.Method;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import com.ltsznh.gwt.shared.model.ResultData;

/**
 * Created by 涛 on 2015/6/28.
 */
public class getResult {

	public static ResultData get(String className, String methodName, HashMap<String, Object> param) {
		getRequestInfo(param);

		ResultData result = null;
		String idString;
		if (!param.containsKey("id")) {
			// 生成唯一ID,随机数加一，确保随机数位数相同，否则小于0.1的缺位数
			idString = Tools.getRandomIdString();
			param.put("id", idString);
		} else {
			idString = param.get("id").toString();
		}

		String classString = "com.ltsznh." + className;
		ServerLog.log("Server", idString, param.getOrDefault("pip", "无IP").toString() + " "
				+ param.getOrDefault("userId", "无用户id") + " " + classString + " - " + methodName);
		// 根据类名获取到到该类的class类
		Method method = null;
		try {
			Class<?> cls = Class.forName(classString);
			Object obj = cls.newInstance();

			if (param != null) {
				method = cls.getMethod(methodName, new Class[] { param.getClass() });
				result = (ResultData) method.invoke(obj, new Object[] { param });
			}
		} catch (Exception e) {
			// TODO: handle exception
			ServerLog.err("Server", idString, e);
			if (result == null) {
				result = new ResultData();
				result.setStatus(false);
				result.setMessage(idString + "系统异常，请联系管理员");
			} else {
				result.setStatus(false);
				result.setMessage(idString + "系统异常，请联系管理员");
			}
		}
		return result;
	}

	public static void getRequestInfo(HashMap<String, Object> param) {
		// TODO Auto-generated method stub
		HttpServletRequest request = (HttpServletRequest) param.get("request");
		// 跟踪IP
		if (null != request) {
			param.put("pip", IP.getPIP(request));
			String userAgentString = request.getHeader("user-agent").toString();
			if (null != userAgentString) {
				param.put("user-agent", userAgentString);
			}
		}

	}

}
