package com.ltsznh.util;

import java.util.HashMap;

import com.ltsznh.db.dbo;
import com.ltsznh.gwt.shared.model.ResultData;

public class check {

	public static ResultData checkOverdue(HashMap<String, Object> param) {
		ResultData result = null;

		getResult.getRequestInfo(param);

		if (param == null) {
			result = new ResultData();
			result.setStatus(false);
			result.setMessage("参数错误");
			return result;
		}
		// 此处可检验用户是否登录超时，是否在其他IP登录
		try {// 检测IP\id\过期时间是否正确
			if (!param.containsKey("client_type"))
				param.put("client_type", "P");
			String sqlString = "SELECT 1 " + "FROM sys_users AS a "
					+ "LEFT OUTER JOIN  sys_users_login_info AS b ON a.userId = b.userId "
					// + "AND b.type = ? "
//					+ "LEFT OUTER JOIN sys_org AS c ON a.orgCode = c.orgCode "
					+ "WHERE  a.isEnabled = 'Y' "
					+ "AND a.userId = ? AND b.loginId = ? AND (curdate() <= date_add(loginTime,INTERVAL loginExpire DAY) OR ? = 'M')"
					+ " AND (b.ip = ? OR ? = 'M' OR ? = 'I' OR ? = 'P')";
			String[] sqlParam = new String[] {
					//
					param.get("userId").toString(), param.get("loginId").toString(),
					param.get("client_type").toString(), param.get("pip").toString(),
					param.getOrDefault("client_type", "P").toString(),
					param.getOrDefault("client_type", "P").toString(),
					param.getOrDefault("client_type", "P").toString() };

			result = dbo.executeParamQuerySilence(sqlString, param, sqlParam);
			if (result.getStatus()) {
				if (result.size() < 1) {
					result = new ResultData();
					result.setStatus(false);
					result.setLevel(-1);
					result.setMessage("已过期!");
					return result;
				} else {
//					param.put("userName", result.getText(0, "userName"));
//					param.put("userPost", result.getText(0, "post"));
					return result;
				}
			} else {
				return result;
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			ServerLog.err("Servlet exe", e1);
			result = new ResultData();
			result.setStatus(false);
			result.setMessage("err:\n请联系管理员\nid:" + param.getOrDefault("id", "").toString());
			return result;
		}
	}

}
