package java3.iku;

public class Exercise {
	
	private String name;
	private int minute;		// 운동 시간 (분)
	
	public Exercise(String name, int minute) {
		this.name = name;
		this.minute = minute;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getMinute() {
		return minute;
	}
	public void setMinute(int minute) {
		this.minute = minute;
	}
}
