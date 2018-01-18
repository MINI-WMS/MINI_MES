package comm;

import com.ltsznh.comm.SerialReader;
import entity.ComboBoxItem;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ToggleButton;

import java.net.URL;
import java.util.Enumeration;
import java.util.ResourceBundle;

public class MiniComm implements Initializable {

	private SerialReader serialReader;

	@FXML
	ComboBox cbPortList;

	@FXML
	ComboBox cbBaudrate;

	@FXML
	ComboBox cbDatabits;

	@FXML
	ComboBox cbStopbits;

	@FXML
	ComboBox cbParity;

	@FXML
	ToggleButton btnPortControl;


	@Override
	public void initialize(URL location, ResourceBundle resources) {
		serialReader = new SerialReader();

		// 设置串口列表
		Enumeration portList = serialReader.getPortList();
		ObservableList<String> ports =
				FXCollections.observableArrayList();

		while (portList.hasMoreElements()) {
			CommPortIdentifier portId = (CommPortIdentifier) portList.nextElement();
//			if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
			ports.add(portId.getName());
//			}
		}
		cbPortList.setItems(ports);
		cbPortList.getSelectionModel().selectFirst();

		// 设置波特率
		ObservableList<String> baudrates =
				FXCollections.observableArrayList("1200", "2400", "4800", "9600", "19200", "38400", "43000", "56000", "57600", "115200");

		cbBaudrate.setItems(baudrates);
		cbBaudrate.getSelectionModel().select("9600");

		// 设置数据位
		ObservableList<String> databits =
				FXCollections.observableArrayList("5", "6", "7", "8");

		cbDatabits.setItems(databits);
		cbDatabits.getSelectionModel().selectLast();

		// 设置校验位
		ObservableList<ComboBoxItem> paritys =
				FXCollections.observableArrayList(new ComboBoxItem(SerialPort.PARITY_NONE, "无校验"),
						new ComboBoxItem(SerialPort.PARITY_ODD, "奇校验"),
						new ComboBoxItem(SerialPort.PARITY_EVEN, "偶校验"),
						new ComboBoxItem(SerialPort.PARITY_MARK, "标记校验(校验位总为0)"),
						new ComboBoxItem(SerialPort.PARITY_SPACE, "校验位总为1"));

		cbParity.setItems(paritys);
		cbParity.getSelectionModel().selectFirst();

		// 设置停止位
		ObservableList<ComboBoxItem> stopbits =
				FXCollections.observableArrayList(new ComboBoxItem(SerialPort.STOPBITS_1, "1位停止位"),
						new ComboBoxItem(SerialPort.STOPBITS_1_5, "1.5位停止位"),
						new ComboBoxItem(SerialPort.STOPBITS_2, "2位停止位"));

		cbStopbits.setItems(stopbits);
		cbStopbits.getSelectionModel().selectFirst();  
	}
}
