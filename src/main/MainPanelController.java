package main;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.net.*;
import java.util.*;

public class MainPanelController implements Initializable {

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

	@FXML
	private void pigWeighing(ActionEvent event) throws Exception {
		Parent workScene = null;
		try {
			workScene = FXMLLoader.load(getClass().getResource("../mes/pig/PigWeighing.fxml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		Scene scene = new Scene(workScene);
		status.workStage = new Stage();
		status.workStage.setScene(scene);
		status.workStage.setTitle("MINI MES");// 设置窗体标题
//		new Image("",true);
		status.workStage.getIcons().add(new Image(getClass().getResourceAsStream("../img/logo.png")));    // 设置窗体图标
		status.workStage.setResizable(true);
		status.workStage.setMaximized(true);
		status.workStage.setFullScreen(true);
		// 显示窗体
		status.workStage.show();

		status.mainPanelStage.hide();

		status.workStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
				System.out.println("close");
				status.mainPanelStage.show();
			}
		});
	}

	@FXML
	private void exit(ActionEvent event) {
		Event.fireEvent(status.mainPanelStage, new WindowEvent(status.mainPanelStage, WindowEvent.WINDOW_CLOSE_REQUEST));
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		if (status.getWorkDate() != null)
			labWorkDate.setText(status.getWorkDate().toString());
		labShift.setText(status.getShift().getShiftName());
		labUserName.setText(status.getUserName());
	}
}
