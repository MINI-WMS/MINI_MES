package welcome;

import com.ltsznh.param.PARAM;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import main.status;
import util.WindowsCloseEvent;

public class Test extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws  Exception {
		PARAM.setCOOKIES(null);
		//显示登录界面
		status.mainPanelStage = primaryStage;
		Parent root = FXMLLoader.load(getClass().getResource("/main/MainPanel.fxml"));

		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle("MINI MES");// 设置窗体标题
		primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/img/logo.png")));// 设置窗体图标

		primaryStage.setResizable(true);//不允许重置大小时全屏会盖住任务栏
		primaryStage.centerOnScreen();// 设置到屏幕中心
		primaryStage.setMaximized(true);//窗口最大化
		primaryStage.show();// 显示窗体
		primaryStage.setOnCloseRequest(new WindowsCloseEvent(primaryStage));// 阻止关闭动作，防止误操作

		primaryStage.show();// 显示窗体
	}
}
