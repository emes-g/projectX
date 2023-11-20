package java3.iku;

public class Diet {

	private String food;
	private int calorie;
	
	public Diet(String food, int calorie) {
		this.food = food;
		this.calorie = calorie;
	}
	public String getFood() {
		return food;
	}
	public void setFood(String food) {
		this.food = food;
	}
	public int getCalorie() {
		return calorie;
	}
	public void setCalorie(int calorie) {
		this.calorie = calorie;
	}
}
