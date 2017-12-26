package welcome;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import main.status;

import java.util.Locale;

/**
 * 登录界面
 */
public class SignIn extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("SignIn.fxml"));

		Scene scene = new Scene(root);

		primaryStage.setScene(scene);

		// 设置窗体标题
		primaryStage.setTitle("MINI MES");

		// 设置窗体图标
		primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("logo.png")));

		primaryStage.setResizable(false);

		// 设置到屏幕中心
		primaryStage.centerOnScreen();

		// 显示窗体
		primaryStage.show();

		status.signInStage = primaryStage;
	}
}
