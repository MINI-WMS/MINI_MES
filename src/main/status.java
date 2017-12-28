package main;

import com.ltsznh.util.Const;
import entity.SysUserEntity;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import entity.Shift;
import util.WindowsCloseEvent;

import java.io.IOException;
import java.net.CookieHandler;
import java.net.URI;
import java.time.LocalDate;
import java.util.*;

public class status {
	// 页面
	public static Stage signInStage;// 登录页面
	public static Stage mainPanelStage;// 主页面
	public static Stage workStage;// 当前弹出工作页面

	/**
	 * 主界面防止误操作关闭，增加提示框
	 */
	public static void setMainPanelStageCloseEvent() {
		if (mainPanelStage != null) mainPanelStage.setOnCloseRequest(new WindowsCloseEvent(mainPanelStage));
	}


	// 变量
	// 登录变量
	private static SysUserEntity user ;// 用户
	private static LocalDate workDate = LocalDate.now();// 工作日期

	private static Shift shift = new Shift(0, "模拟班次");// 班次


	// 与WEB端交互变量
	private static String BASE_URL = "http://localhost:8080";// WEB端SESSION值
	private static String COOKIES = null;// WEB端SESSION值

	public static String getBaseUrl() {
		return BASE_URL;
	}

	public static String getCOOKIES() {
		return COOKIES;
	}

	/**
	 * 设置Cookies
	 * @param COOKIES
	 */
	public static void setCOOKIES(String COOKIES) {
		status.COOKIES = COOKIES;
		if (COOKIES == null) return;
		// 设置cookies
		new WebView();// 初始化一个WebView，否则CookieHandler.getDefault()为null
		URI uri = URI.create(status.getBaseUrl());
		Map<String, List<String>> headers = new LinkedHashMap<>();
		headers.put(Const.KEY_HEADER_COOKIE, Arrays.asList(status.getCOOKIES()));
		try {
			CookieHandler.getDefault().put(uri, headers);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static Long getUserId() {
		return user.getUserId();
	}

	public static String getUserName() {
		return user.getUsername();
	}

	public static SysUserEntity getUser() {
		return user;
	}

	public static void setUser(SysUserEntity user) {
		status.user = user;
	}

	public static LocalDate getWorkDate() {
		return workDate;
	}

	public static void setWorkDate(LocalDate workDate) {
		status.workDate = workDate;
	}

	public static Shift getShift() {
		return shift;
	}

	public static void setShift(Shift shift) {
		status.shift = shift;
	}
}
