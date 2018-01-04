package welcome;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import main.status;

/**
 * 登录界面
 */
public class SignIn extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		//显示登录界面
		status.signInStage = primaryStage;
		Parent root = FXMLLoader.load(getClass().getResource("SignIn.fxml"));

		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle("MINI MES");// 设置窗体标题
		primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/img/logo.png")));// 设置窗体图标
		primaryStage.setResizable(false);
		primaryStage.centerOnScreen();// 设置到屏幕中心

		primaryStage.show();// 显示窗体
	}
}
