package welcome;

import com.alibaba.fastjson.JSON;
import com.ltsznh.model.R;
import com.ltsznh.util.Encryption;
import com.ltsznh.util.syncData;
import entity.SysUserEntity;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import main.status;
import entity.Shift;
import util.MiniAlert;
import util.focus;

import java.net.URL;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Optional;
import java.util.ResourceBundle;

public class SignInController implements Initializable {

	@FXML
	private TextField tfUserCode;
	@FXML
	private PasswordField tfPwd;
	@FXML
	private DatePicker dpWorkDate;
	@FXML
	private ComboBox<Shift> cbShift;
	@FXML
	private Button btnSignIn;

//	Gson

	private void alertInfo(TextField item, String itemString) {
		MiniAlert alert = new MiniAlert(Alert.AlertType.INFORMATION, "请输入您的" + itemString);
		alert.setTitle("登录信息不完整");
		alert.setHeaderText("请输入" + itemString);
		alert.showAndWait();

		item.selectAll();
		item.requestFocus();
	}

	@FXML
	private void SignInAction(ActionEvent event) throws Exception {
		// 登录信息验证
		if (tfUserCode.getText().trim().length() <= 0) {
			alertInfo(tfUserCode, "用户名");
			return;
		}
		if (tfPwd.getText().length() <= 0) {
			alertInfo(tfPwd, "密码");
			return;
		}
		if (true) {
			// 登录工作日期正常
			status.setWorkDate(dpWorkDate.getValue());
		} else {
			Alert alert = new Alert(Alert.AlertType.INFORMATION, "请选择准确的工作日期");
			alert.setTitle("日期错误");
			alert.setHeaderText("请选择工作日期");
			alert.showAndWait();

			dpWorkDate.requestFocus();
			return;
		}
		if (cbShift.getSelectionModel().getSelectedItem() != null) {
			status.setShift(cbShift.getSelectionModel().getSelectedItem());
		} else {
			Alert alert = new Alert(Alert.AlertType.INFORMATION, "请选择班次");
			alert.setTitle("班次错误");
			alert.setHeaderText("请选择班次");
			alert.showAndWait();

			cbShift.requestFocus();
			return;
		}

		// 数据校验结束，开始身份验证
//		status.setCOOKIES(null);// 重置Cookies
		// 用户身份验证
		// 拼接登录信息，两种方式
//		HashMap<String, Object> params = new HashMap<>();
//		params.put("phone", status.getUserCode());
//		params.put("password", Encryption.encrypt2SHA1(Encryption.encrypt2SHA1(tfPwd.getText())));
//		R result = syncData.sendPostRequest("http://127.0.0.1:8080/sys/login", params);
		R result = syncData.sendPostRequest(status.getBaseUrl() + "/sys/login?phone=" + tfUserCode.getText().trim() +
				"&password=" + Encryption.encrypt2SHA1(Encryption.encrypt2SHA1(tfPwd.getText())), null);

		// 身份验证失败
		if (result == null || result.getCode() != 0 || !result.getMsg().equals("")) {
			String errorMsg = result == null ? "服务器无响应，请联系管理员！" : "错误代码：" + result.getCode() + "\n错误信息:" + result.getMsg();
			Alert alert = new Alert(Alert.AlertType.ERROR, errorMsg);
			alert.setTitle("登录失败");
			alert.setHeaderText("登录失败");
			alert.showAndWait();

			tfUserCode.requestFocus();
			return;// 登录失败，返回重新登录
		}

		// 身份验证成功
		boolean getInfo = true;
		// 1、设置cookies
		if (result.containsKey("cookies")) status.setCOOKIES(result.get("cookies").toString());
		else {
			getInfo = false;
		}
		// 2、获取用户名称等信息
		R userInfo = syncData.sendGetRequest(status.getBaseUrl() + "/sys/user/info", new HashMap<>());
		if (userInfo == null || userInfo.getCode() != 0) {
			getInfo = false;
		} else {
			status.setUser(JSON.parseObject(userInfo.get("user").toString(), SysUserEntity.class));
		}

		if (!getInfo) {// 获取Cookies、用户信息异常，提示重新登录
			Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "用户登录异常，部分功能无法使用。\n是否重新登录？");
			alert.setTitle("登录异常");
			alert.setHeaderText("是否重新登录？");
			Optional<ButtonType> retry = alert.showAndWait();

			if (retry.isPresent() && retry.get() == ButtonType.OK) {
				return;
			}
		}

		// 登录成功，初始化主界面并显示
		Parent root = FXMLLoader.load(getClass().getResource("../main/MainPanel.fxml"));
		Scene scene = new Scene(root);

		status.mainPanelStage = new Stage();
		status.mainPanelStage.setScene(scene);
		status.mainPanelStage.setTitle("MINI MES");// 设置窗体标题
		status.mainPanelStage.getIcons().add(new Image(getClass().getResourceAsStream("logo.png")));// 设置窗体图标
		status.mainPanelStage.setResizable(true);//不允许重置大小时全屏会盖住任务栏
		status.mainPanelStage.centerOnScreen();// 设置到屏幕中心
		status.mainPanelStage.setMaximized(true);
		status.mainPanelStage.show();// 显示窗体
		status.setMainPanelStageCloseEvent();// 阻止关闭动作，防止误操作


//		Screen screen = Screen.getPrimary();
//		Rectangle2D bounds = screen.getVisualBounds();
//
//		status.mainPanelStage.setX(bounds.getMinX());
//		status.mainPanelStage.setY(bounds.getMinY());
//		status.mainPanelStage.setWidth(bounds.getWidth());
//		status.mainPanelStage.setHeight(bounds.getHeight());
//

		// 登录成功，关闭登录界面
		status.signInStage.close();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// 界面初始化时，新起线程处理默认值等信息
		new Thread(() -> {
			// 默认工作日期
			dpWorkDate.setValue(LocalDate.now());// 登录界面默认工作日期为当前日期

			// 默认获取班次信息
			ObservableList<Shift> shiftList =
					FXCollections.observableArrayList(
							new Shift(1, "白班"),
							new Shift(2, "夜班"));
			cbShift.setItems(shiftList);
			cbShift.getSelectionModel().selectFirst();// 默认选择第一个班次


			tfUserCode.requestFocus();

			focus.next(new Control[]{tfUserCode, tfPwd, dpWorkDate, cbShift, btnSignIn});

		}).start();


	}
}
