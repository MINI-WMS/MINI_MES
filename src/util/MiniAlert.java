package util;

import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.util.Optional;

public class MiniAlert extends Alert {


	public MiniAlert(AlertType alertType) {
		super(alertType);
	}
	public MiniAlert(AlertType alertType, String contentText) {
		super(alertType, contentText);
	}
	public MiniAlert(AlertType alertType, String contentText, ButtonType... buttons) {
		super(alertType, contentText, buttons);
	}

}

