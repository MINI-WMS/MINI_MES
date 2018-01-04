package main;

import com.ltsznh.util.Const;
import entity.Shift;
import entity.SysUserEntity;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import util.WindowsCloseEvent;

import java.io.IOException;
import java.net.CookieHandler;
import java.net.URI;
import java.time.LocalDate;
import java.util.*;

public class status {
	private static Logger logger = LogManager.getLogger(status.class);
	// 页面
	public static Stage signInStage;// 登录页面
	public static Stage mainPanelStage;// 主页面
	public static Stage workStage;// 当前弹出工作页面
	public static TabPane content;// 页面
	public static Tab fullScreenTab;// 全屏的页面


	/**
	 * 主界面防止误操作关闭，增加提示框
	 */
	public static void setMainPanelStageCloseEvent() {
		if (mainPanelStage != null) mainPanelStage.setOnCloseRequest(new WindowsCloseEvent(mainPanelStage));
	}


	// 变量
	// 登录变量
	private static SysUserEntity user;// 用户
	private static LocalDate workDate = LocalDate.now();// 工作日期

	private static Shift shift = new Shift(0, "模拟班次");// 班次

	public static HashMap menus = new HashMap();
	public static HashMap tabs = new HashMap();


	// 与WEB端交互变量
	private static String BASE_URL = "http://localhost:8080";// WEB端SESSION值
	//	private static String COOKIES = null;// WEB端SESSION值
	private static String COOKIES = "JSESSIONID=dd1347c3-e71d-4646-97fa-4461cf8eb129;rememberMe=deleteMe;";// WEB端SESSION值

	public static String getBaseUrl() {
		return BASE_URL;
	}

	public static String getCOOKIES() {
		return COOKIES;
	}


	/**
	 * 设置Cookies
	 *
	 * @param COOKIES
	 */
	public static void setCOOKIES(String COOKIES) {
		if (COOKIES != null) status.COOKIES = COOKIES;
		if (status.COOKIES == null) return;
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

	public static void setUser(SysUserEntity user) {
		if (user == null) {
			user = new SysUserEntity();
			user.setUsername("测试");
		}
		status.user = user;
	}

	public static Long getUserId() {
		return user.getUserId();
	}

	public static String getUserName() {
		if (user == null) return "测试用户";
		return user.getUsername();
	}

	public static SysUserEntity getUser() {
		return user;
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

	public static void setWorkStageNode(Node node, String title) {
		logger.info("打开全屏窗口。窗口：" + title);
		AnchorPane pane = new AnchorPane();


		Scene scene = new Scene(pane);

		pane.getChildren().add(node);
		AnchorPane.setLeftAnchor(node, 0.0);
		AnchorPane.setTopAnchor(node, 0.0);
		AnchorPane.setRightAnchor(node, 0.0);
		AnchorPane.setBottomAnchor(node, 0.0);


//		new Group(node)
		if (status.workStage == null)
			status.workStage = new Stage();
		status.workStage.setScene(scene);
		status.workStage.setTitle(title);// 设置窗体标题
		status.workStage.getIcons().add(new Image(status.class.getResourceAsStream("/img/logo.png")));    // 设置窗体图标
		status.workStage.setResizable(true);
		status.workStage.setMaximized(true);
		status.workStage.setFullScreen(true);
		// 显示窗体
		status.workStage.show();

		status.mainPanelStage.hide();

		status.workStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
				logger.info("关闭全屏窗口。窗口：" + workStage.getTitle());
				status.mainPanelStage.show();

				if (!content.getTabs().contains(fullScreenTab)) content.getTabs().add(fullScreenTab);
				SingleSelectionModel<Tab> selectionModel = content.getSelectionModel();
				selectionModel.select(fullScreenTab);
			}
		});
	}

	public static void setFullScreenTab(Tab fullScreenTab) {
		status.fullScreenTab = fullScreenTab;
		if (status.fullScreenTab == null) return;
		setWorkStageNode(status.fullScreenTab.getContent(), status.fullScreenTab.getText());

		if(content.getTabs().contains(fullScreenTab))content.getTabs().remove(fullScreenTab);
	}

	public static TabPane getContent() {
		return content;
	}

	public static void setContent(TabPane content) {
		status.content = content;
	}
}
