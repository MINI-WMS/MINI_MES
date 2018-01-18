package entity;

public class ComboBoxItem {
	private String value;
	private String text;

	public ComboBoxItem(String value, String text) {
		this.value = value;
		this.text = text;
	}

	public ComboBoxItem(int value, String text) {
		this.value = Integer.toString(value);
		this.text = text;
	}

	@Override
	public String toString() {
		return text;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
}
