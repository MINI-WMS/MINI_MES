package welcome;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import main.status;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class  SignInController  implements Initializable {

	@FXML
	private TextField tfUserCode;

	@FXML
	private DatePicker dpWorkDate;

	@FXML
	private void SignInAction(ActionEvent event) throws Exception{
		Parent root = FXMLLoader.load(getClass().getResource("../main/MainPanel.fxml"));
		Scene scene = new Scene(root);
		status.mainPanelStage  = new Stage();
		status.mainPanelStage.setScene(scene);
		// 设置窗体标题
		status.mainPanelStage.setTitle("MINI MES");
		// 设置窗体图标
		status.mainPanelStage.getIcons().add(new Image(getClass().getResourceAsStream("logo.png")));
		status.mainPanelStage.setResizable(false);
		// 设置到屏幕中心
		status.mainPanelStage.centerOnScreen();
		// 显示窗体
		status.mainPanelStage.show();

		status.setMainPanelStageCloseEvent();

		status.signInStage.close();

	}



	@Override
	public void initialize(URL location, ResourceBundle resources) {
		dpWorkDate.setValue(LocalDate.now());
	}
}
