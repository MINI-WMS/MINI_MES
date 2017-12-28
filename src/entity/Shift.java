package entity;

public class Shift {
	private int shiftId;
	private String shiftName;

	public Shift(int shiftId, String shiftName) {
		setShiftId(shiftId);
		setShiftName(shiftName);
	}

	@Override
	public String toString() {
		return shiftName.toString();
	}

	public int getShiftId() {
		return shiftId;
	}

	public void setShiftId(int shiftId) {
		this.shiftId = shiftId;
	}

	public String getShiftName() {
		return shiftName;
	}

	public void setShiftName(String shiftName) {
		this.shiftName = shiftName;
	}
}
