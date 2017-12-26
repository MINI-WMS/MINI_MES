package utils;

import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.util.Optional;
import java.util.logging.Logger;

public class WindowsCloseEvent implements EventHandler<WindowEvent> {

	private Stage stage;

	public WindowsCloseEvent(Stage stage){
		this.stage = stage;
	}

	public void handle(WindowEvent event) {
		event.consume();

		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle("确认关闭？");
		alert.setHeaderText("系统正在运行！");
		alert.setContentText("退出可能造成数据丢失...");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK){
		this.stage.close();
		} else {

		}
	}
}

