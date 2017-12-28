package util;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Control;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.util.Date;

public class focus {

	public static void next(Control[] item) {
		for (int i = 0; i < item.length - 1; i++) next(item[i], item[i + 1]);
	}

	public static void next(Control item, Control next) {
		item.setOnKeyPressed(event -> {
			if (event.getCode() == KeyCode.ENTER)
				next.requestFocus();
		});
	}
}
