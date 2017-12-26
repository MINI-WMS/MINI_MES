package main;

import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import utils.WindowsCloseEvent;

public class status {

	public static Stage signInStage;//登录页面
	public static Stage mainPanelStage;//主页面

	public static void setMainPanelStageCloseEvent(){
		if(mainPanelStage != null )mainPanelStage.setOnCloseRequest(new WindowsCloseEvent(mainPanelStage));
	}

	public static Stage workStage;//当前工作页面



}
