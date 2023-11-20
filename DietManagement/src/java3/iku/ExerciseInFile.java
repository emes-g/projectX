package java3.iku;

public class ExerciseInFile {

	private String name;
	private int calorie;	// 한 시간 소모 칼로리
	
	public ExerciseInFile(String name, int calorie) {
		this.name = name;
		this.calorie = calorie;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getCalorie() {
		return calorie;
	}
	public void setCalorie(int calorie) {
		this.calorie = calorie;
	}
}
