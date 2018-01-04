package main;

import com.alibaba.fastjson.JSONArray;
import com.ltsznh.model.R;
import com.ltsznh.util.syncData;
import entity.SysMenuEntity;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

public class MainPanelController implements Initializable {
	private Logger logger = LogManager.getLogger(MainPanelController.class);

	// 登录信息
	@FXML
	private Label labWorkDate;
	@FXML
	private Label labShift;
	@FXML
	private Label labUserName;


	@FXML
	private WebView webTest;


	@FXML
	private Accordion nav;

	private TitledPane[] navMenu;

	@FXML
	private TabPane content;

	@FXML
	private void webTest(ActionEvent event) {
		WebEngine webEngine = webTest.getEngine();

		webEngine.load(status.getBaseUrl());
//		webEngine.getLoadWorker().stateProperty().addListener(new ChangeListener<Worker.State>() {
//			@Override
//			public void changed(ObservableValue<? extends Worker.State> observable, Worker.State oldValue, Worker.State newValue) {
//				if (newValue == Worker.State.SUCCEEDED) {
//					System.out.println("load !!!");
//
//					URI uri = URI.create("http://127.0.0.1:8080");
//
//					CookieManager manager = new CookieManager();
//					for (HttpCookie httpCookie : manager.getCookieStore().get(uri)) {
//						System.out.println("test> " + " # " + httpCookie.toString() + " - " + httpCookie.getSecure());
//					}
//				}
//			}
//		});
	}

	/**
	 * 全屏当前选项卡内容
	 *
	 * @param event
	 */
	@FXML
	private void fullScreen(ActionEvent event) {
		((Hyperlink) event.getSource()).setVisited(false);
		SingleSelectionModel<Tab> selectionModel = content.getSelectionModel();
		Tab tab = selectionModel.getSelectedItem();

		status.setFullScreenTab(tab);
		if (content.getTabs().contains(tab))
			content.getTabs().remove(tab);
	}


	@FXML
	private void exit(ActionEvent event) {
		Event.fireEvent(status.mainPanelStage, new WindowEvent(status.mainPanelStage, WindowEvent.WINDOW_CLOSE_REQUEST));
	}

	/**
	 * 界面初始化
	 * 1、初始化显示用户登录信息；
	 * 2、初始化菜单；
	 *
	 * @param location
	 * @param resources
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// 显示当前登录信息
		if (status.getWorkDate() != null)
			labWorkDate.setText(status.getWorkDate().toString());
		labShift.setText(status.getShift().getShiftName());
		labUserName.setText(status.getUserName());

		content.setRotateGraphic(true);
		status.setContent(content);// 以供全屏关闭时使用

		// 后台读取菜单列表
//		new Thread(() -> {
		R menuList = syncData.sendGetRequest(status.getBaseUrl() + "/sys/menu/nav", new HashMap<>());
		if (menuList == null || menuList.getCode() != 0) {
			// 获取菜单错误

		} else {
			List<SysMenuEntity> menus = JSONArray.parseArray(menuList.get("menuList").toString(), SysMenuEntity.class);
			navMenu = new TitledPane[menus.size()];
			SysMenuEntity menu;// 一级菜单
			for (int i = 0; i < menus.size(); i++) {
				// 遍历一级菜单，创建二级菜单容器
//				System.out.println(i + ":" + menus.get(i).getMenuName());
				menu = menus.get(i);
				status.menus.put(menu.getMenuId(), menu);
				navMenu[i] = new TitledPane();
				navMenu[i].setText(menu.getMenuName());

				nav.getPanes().add(i, navMenu[i]);
				List<SysMenuEntity> menus2 = JSONArray.parseArray(menu.getList().toString(), SysMenuEntity.class);

				// 二级菜单容器
				VBox menuBox = new VBox();
//				menuBox.setPadding(new Insets(5));// 二级菜单按钮边距设置
				navMenu[i].setContent(menuBox);
				for (int j = 0; j < menus2.size(); j++) {
					// 遍历二级菜单，逐个添加事件。网页版权限展示网页内容，客户端权限初始化客户端内容。
//					System.out.println(j + ":" + menus2.get(j).getMenuName());
					SysMenuEntity menu2 = menus2.get(j);// 二级菜单
					status.menus.put(menu2.getMenuId(), menu2);
					Button btnMenu = new Button(menu2.getMenuName());
					btnMenu.setId(menu2.getMenuId() + "");
					btnMenu.setPrefWidth(2000);
//					btnMenu.setPrefSize(200, 30);// 默认高度宽度
					menuBox.getChildren().add(j, btnMenu);

//					SysMenuEntity finalMenu = menu2;
					btnMenu.setOnMouseClicked((MouseEvent event) -> {
						if (!status.menus.containsKey(Long.parseLong(btnMenu.getId()))) {// 如果菜单中无此菜单报错
							return;
						}
						SysMenuEntity clickMenu = (SysMenuEntity) status.menus.get(Long.parseLong(btnMenu.getId()));
						if (status.tabs.containsKey(clickMenu.getMenuId())) {// 已经初始化过
							// 如果已经关闭，未被清理掉的，要重新获取，然后添加
							Tab tab = (Tab) status.tabs.get(clickMenu.getMenuId());
							if (!content.getTabs().contains(tab)) content.getTabs().add(tab);
							SingleSelectionModel<Tab> selectionModel = content.getSelectionModel();
							selectionModel.select(tab);
						} else {// 首次点击
							Tab tab = new Tab(clickMenu.getMenuName());
							AnchorPane pane = new AnchorPane();
							pane.setPadding(new Insets(10, 10, 0, 10));
							Node node = null;
							if (menu2.getUrl().endsWith(".html")) {// html权限，打开网页
								node = new WebView();

								// 加载页面
								WebEngine webEngine = ((WebView) node).getEngine();
								webEngine.load(status.getBaseUrl() + "/" + clickMenu.getUrl());
							} else if (menu2.getUrl().endsWith(".fxml")) {// 客户端权限
								try {
									node = (Node) FXMLLoader.load(getClass().getResource(clickMenu.getUrl()));
								} catch (IOException e) {
									logger.error(e.getLocalizedMessage());
									Alert alert = new Alert(Alert.AlertType.ERROR, "找不到该菜单相关资源");
									alert.setTitle("系统错误");
									alert.setHeaderText("该菜单无效！");
									alert.showAndWait();
									return;
								}
							} else return;
							// pane.getStyleClass().add("pane");
							pane.getChildren().add(node);
							AnchorPane.setLeftAnchor(node, 0.0);
							AnchorPane.setTopAnchor(node, 0.0);
							AnchorPane.setRightAnchor(node, 0.0);
							AnchorPane.setBottomAnchor(node, 0.0);

							tab.setContent(pane);

							// 保存当前界面
							status.tabs.put(clickMenu.getMenuId(), tab);

							// 选中当前页面
							content.getTabs().add(tab);
							SingleSelectionModel<Tab> selectionModel = content.getSelectionModel();
							selectionModel.select(tab);
							// selectionModel.clearSelection();

							// tab 关闭事件
							tab.setOnClosed((Event tabClosedEvent) -> {
								if (status.tabs.containsKey(clickMenu.getMenuId())) {// 关闭后，tab可以不移除，下次点击可以再次使用。
									status.tabs.remove(clickMenu.getMenuId());
								}
							});
						}
					});
				}
			}
		}
//		}).start();
	}

	public TabPane getContent() {
		return content;
	}
}
