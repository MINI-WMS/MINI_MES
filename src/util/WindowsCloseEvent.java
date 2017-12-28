package util;

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
		alert.setTitle("退出系统");
		alert.setHeaderText("确认退出系统？");
		alert.setContentText("退出系统，结束本次工作？\n");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK){
		this.stage.close();
		} else {

		}
	}
}

