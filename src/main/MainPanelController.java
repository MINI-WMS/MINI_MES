package main;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainPanelController implements Initializable {


	@FXML
	private void pigWeighing(ActionEvent event) throws Exception{
		Parent workScene = null;
		try {
			workScene = FXMLLoader.load(getClass().getResource("../mes/pig/PigWeighing.fxml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		Scene scene = new Scene(workScene);
		status.workStage = new Stage();
		status.workStage.setScene(scene);
		// 设置窗体标题
		status.workStage.setTitle("MINI MES");
		// 设置窗体图标
		status.workStage.getIcons().add(new Image(getClass().getResourceAsStream("../welcome/logo.png")));
		status.workStage.setResizable(false);
		// 设置到屏幕中心
		status.workStage.centerOnScreen();
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


	@Override
	public void initialize(URL location, ResourceBundle resources) {
	}
}
