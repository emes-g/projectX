package java3.iku;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;

public class DietManager {

	private final int TYPE_NUM = 4;
	private ArrayList<Diet>[] diets;
	private HashMap<String, ArrayList<Diet>> frequentlyEatenDiet;
	private int type, weight, height, age, targetCalorie, currentCalorie;
	private double BMR, standardWeight; // BMR: 기초대사량, standardWeight: 표준체중
	private String gender;

// =================================== 생성자 ===================================
	public DietManager() {
		this.diets = new ArrayList[TYPE_NUM];
		this.frequentlyEatenDiet = new HashMap<String, ArrayList<Diet>>();

		try {
			File f = new File("frequentlyEatenDiet.txt");
			if (f.createNewFile()) {
				System.out.println("파일 생성 완료 : " + f.getName());
				return;
			}
			Scanner fscan = new Scanner(f);
			boolean parenthesisOpen = false; // ====================(해당 식단의 시작과 끝을 파악)
			while (fscan.hasNextLine()) {
				String line = fscan.nextLine();
				if (line.equals("=".repeat(20))) {
					parenthesisOpen = true;
				}
				if (parenthesisOpen) {
					ArrayList<Diet> diet = new ArrayList<Diet>();
					String name = fscan.nextLine();
					while (true) {
						line = fscan.nextLine();
						if (line.equals("=".repeat(20))) {
							parenthesisOpen = false;
							break;
						}
						String[] element = line.split("\t");
						diet.add(new Diet(element[0], Integer.parseInt(element[1])));
					}
					frequentlyEatenDiet.put(name, diet);
				}
			}
		} catch (Exception e) {
			System.out.println("에러 발생");
			e.printStackTrace();
		}
		for (int i = 0; i < TYPE_NUM; i++) {
			diets[i] = new ArrayList<Diet>();
		}
	}

// ================================== 개인 설정 ==================================
	public void personalization() {
		try {
			File f = new File("myInfo.txt");
			if (f.createNewFile()) {
				setPersonalInfo();
			} 
			else {
				Scanner fscan = new Scanner(f);
				fscan.next();	weight = fscan.nextInt();
				fscan.next();	height = fscan.nextInt();
				fscan.next();	age = fscan.nextInt();
				fscan.next();	gender = fscan.next();
				fscan.next();	targetCalorie = fscan.nextInt();
			}
		} catch (Exception e) {
			System.out.println("에러 발생");
			e.printStackTrace();
		}
	}
	public void setPersonalInfo() {
		int select;
		double activityLevel, recommendCalories;
		
		System.out.println("기초 대사량 측정을 위해 체중, 키, 나이, 성별을 입력해주세요.");
		System.out.print("체중(kg): ");	weight = Main.scan.nextInt();
		System.out.print("키(cm): ");	height = Main.scan.nextInt();
		System.out.print("나이(세): ");	age = Main.scan.nextInt();
		Main.clearBuffer();
		System.out.print("성별(남/여): ");	gender = Main.scan.nextLine();
		if (gender.equals("남")) {
			BMR = 66.47 + (13.75 * weight) + (5 * height) - (6.76 * age);
			standardWeight = (height * 0.01) * (height * 0.01) * 22;
		} else if (gender.equals("여")) {
			BMR = 665.1 + (9.56 * weight) + (1.85 * height) - (4.68 * age);
			standardWeight = (height * 0.01) * (height * 0.01) * 21;
		}
		System.out.printf("기초대사량은 %.1f kcal 이며, 표준체중은 %.1f kg입니다.\n\n", BMR, standardWeight);
		System.out.println("일일 권장 칼로리 측정을 위해 신체 활동 레벨을 입력해주세요.");
		System.out.println("레벨 1: 운동을 거의 하지 않고 앉아 있는 시간이 많다.");
		System.out.println("레벨 2: 통근, 통학할 때 주로 서 있거나, 집안일이나 가벼운 운동을 한다.");
		System.out.println("레벨 3: 서서 일을 하거나, 운동을 꾸준히 한다.");
		System.out.println("레벨 4: 강도 높은 일이나 운동을 많은 시간동안 한다.");
		System.out.print("입력: ");
		select = Main.scan.nextInt();
		switch (select) {
		case 1:
			activityLevel = 1.2;
			break;
		case 2:
			activityLevel = 1.3;
			break;
		case 3:
			activityLevel = 1.5;
			break;
		case 4:
			activityLevel = 1.75;
			break;
		default:
			activityLevel = 0;
			break;
		}
		recommendCalories = BMR * activityLevel;
		System.out.printf("일일 권장 칼로리는 %.1fkcal입니다.\n", recommendCalories);
		System.out.println("일일 목표 칼로리를 설정합니다.");
		System.out.print("입력: ");
		targetCalorie = Main.scan.nextInt();
		Main.clearBuffer();
		System.out.println("일일 목표 칼로리 설정 성공!\n");

		try {
			FileWriter fw = new FileWriter("myInfo.txt");
			fw.write("체중 " + weight + "\n");
			fw.write("키 " + height + "\n");
			fw.write("나이 " + age + "\n");
			fw.write("성별 " + gender + "\n");
			fw.write("일일목표칼로리 " + targetCalorie + "\n");
			fw.flush();
		} catch (IOException e) {
			System.out.println("에러 발생");
			e.printStackTrace();
		}
	}

	public int getTargetCalorie() {
		return targetCalorie;
	}

	public double getCurrentCalorie() {
		currentCalorie = 0;
		for (int i = 0; i < TYPE_NUM; i++) {
			currentCalorie += this.getTotalCalorie(i);
		}
		currentCalorie -= Main.em.getTotalCalorie();
		return currentCalorie;
	}

	public void setTargetCalorie(int targetCalorie) {
		this.targetCalorie = targetCalorie;
	}

	public ArrayList<Diet>[] getDiets() {
		return diets;
	}
	public int getTotalCalorie(int type) {
		int total = 0;
		for (int i = 0; i < diets[type].size(); i++) {
			total += diets[type].get(i).getCalorie();
		}
		return total;
	}
	public int getTotalCalorie(ArrayList<Diet> diet) {
		int total = 0;
		for (int i = 0; i < diet.size(); i++) {
			total += diet.get(i).getCalorie();
		}
		return total;
	}

// =============================== [1] 식단 기록하기 ===============================
	public void recordDiet() {
		System.out.println("\n[1] 식단 기록하기");
		System.out.println("1.아침 2.점심 3.저녁 4.간식 5.메뉴로 돌아가기");
		if((type = getInt(1, 5, "입력 : ", "1~5 사이의 숫자를 입력해주세요.\n")) == 5)	return;

		int select;
		System.out.println("1.개별 음식 추가하기 2.식단 불러오기 3.메뉴로 돌아가기");
		if((select = getInt(1, 3, "입력 : ", "1~3 사이의 숫자를 입력해주세요.\n")) == 3)	return;
		System.out.println("기록을 종료하려면 [종료]를 입력하세요");

		String food;
		int calorie;
		switch (select) {
		case 1: // 개별 음식 추가
			while (true) {
				System.out.print("음식 : ");
				food = Main.scan.nextLine();
				if (food.equals("종료")) {
					System.out.println("식단 기록을 종료합니다.");
					break;
				}
				calorie = getInt(0, 0, "칼로리 : ", "0 이상의 숫자를 입력해주세요.\n");
				diets[type - 1].add(new Diet(food, calorie));
			}
			break;
		case 2: // 식단 불러오기
			int count = 1;
			ArrayList<String> foodKind = new ArrayList<String>();

			for (String key : frequentlyEatenDiet.keySet()) {
				if (count % 5 == 0) {
					System.out.println();
				}
				System.out.printf("%d.%s ", count++, key);
				foodKind.add(key);
			}
			int choice = getInt(1, count-1, "식단 선택 : ","숫자를 입력해주세요.\n");

			ArrayList<Diet> diet = frequentlyEatenDiet.get(foodKind.get(choice - 1));
			for (Diet d : diet) {
				diets[type - 1].add(d);
			}
			break;
		}
	}

// ============================ [3] 현재  칼로리 확인하기 ============================
	public void checkCalorie() {
		final String[] time = { "아침", "점심", "저녁", "간식" };

		int total = 0, index = 1;
		System.out.println("\n[3] 현재 칼로리 확인하기");
		System.out.println("=".repeat(24));
		for (int i = 0; i < TYPE_NUM; i++) {
			if (getTotalCalorie(i) != 0) {
				System.out.printf("%d. %s\t%dkcal\n", index++, time[i], getTotalCalorie(i));
				total += getTotalCalorie(i);
				System.out.println("-".repeat(24));
				for (int j = 0; j < diets[i].size(); j++) {
					System.out.printf("%s\t%dkcal\n", diets[i].get(j).getFood(), diets[i].get(j).getCalorie());
				}
				System.out.println("=".repeat(24));
			}
		}

		if (Main.em.getTotalCalorie() != 0) {
			System.out.printf("%d. 운동\t%dkcal\n", index, Main.em.getTotalCalorie());
			total -= Main.em.getTotalCalorie();
			System.out.println("-".repeat(24));
			ArrayList<Exercise> exercises = Main.em.getExercises();
			for (int i = 0; i < exercises.size(); i++) {
				System.out.printf("%s\t%dkcal\n", exercises.get(i).getName(),
						Main.em.getCalorieByExercise(exercises.get(i)));
			}
			System.out.println("=".repeat(24));
		}
		if (total == 0 && Main.em.getTotalCalorie() == 0)
			System.out.println("입력된 칼로리 정보가 없습니다.");
		else
			System.out.printf("현재 칼로리 : %dkcal\n", total);
		System.out.println("=".repeat(24));
	}

// =============================== [4] 식단 저장하기 ===============================
	public void saveDiet() {
		String name, food;
		int calorie, select;
		ArrayList<Diet> diet = new ArrayList<Diet>();
		
		System.out.println("1.식단 저장하기 2.식단 확인하기 3.메뉴로 돌아가기");
		if((select = getInt(1, 3, "입력 : ", "1~3 사이의 숫자를 입력해주세요.\n")) == 3)	return;
		
		switch(select) {
		case 1:
			System.out.println("\n[4] 자주 먹는 식단 저장하기");
			System.out.print("식단 이름 (메뉴로 돌아가려면 '종료'를 입력해주세요) : ");
			name = Main.scan.nextLine();
			if(name.equals("종료")) {
				System.out.println("메인 메뉴로 돌아갑니다.");
				return;
			}
			System.out.println("음식 추가를 종료하려면 [종료]를 입력하세요");
			while (true) {
				System.out.print("음식: ");
				food = Main.scan.nextLine();
				if (food.equals("종료")) {
					System.out.println("자주 먹는 식단 저장을 종료합니다.");
					break;
				}
				calorie = getInt(0, 0, "칼로리 : ", "0 이상의 숫자를 입력해주세요.\n");
				diet.add(new Diet(food, calorie));
			}
			frequentlyEatenDiet.put(name, diet);

			try {
				FileWriter fw = new FileWriter("frequentlyEatenDiet.txt", true);
				fw.write("====================\n");
				fw.write(name + "\n");
				for (Diet d : diet) {
					fw.write(d.getFood() + "\t" + d.getCalorie() + "\n");
				}
				fw.write("====================\n\n");
				fw.flush();
			} catch (Exception e) {
				System.out.println("에러 발생");
				e.printStackTrace();
			}
			break;
		case 2:
			int num = 1;
			for (Map.Entry<String, ArrayList<Diet>> pair : frequentlyEatenDiet.entrySet()) {
				System.out.println("=".repeat(24));
				System.out.printf("%d.%s\t총 칼로리\t%dkcal\n", num++, pair.getKey(), getTotalCalorie(pair.getValue()));
				for(Diet d : pair.getValue()) {
					System.out.printf("%s\t%dkcal\n", d.getFood(), d.getCalorie());
				}
				System.out.println("=".repeat(24));
			}
			break;
		case 3:
			return;
		}
	}

// ============================== [5] 내 정보 확인하기 ==============================
	public void watchMyInfo() {
		// 정보 출력
		System.out.println("\n[5] 내 정보 확인하기");
		System.out.println("=".repeat(24));
		System.out.println("체중\t   : " + weight + "kg");
		System.out.println("키\t   : " + height + "cm");
		System.out.println("나이\t   : " + age + "세");
		System.out.println("성별\t   : " + gender);
		System.out.println("일일 목표 칼로리 : " + targetCalorie + "kcal");
		System.out.println("=".repeat(24));
		
		// 메뉴 선택
		int select = 0;
		System.out.println("1.정보 수정하기 2.메뉴로 돌아가기");
		if((select = getInt(1, 2, "입력 : ", "1~2 사이의 숫자를 입력해주세요.\n")) == 2)	return;
		setPersonalInfo();	// select == 1인 경우만
	}
	
	public int getInt(int start, int end, String Text, String warningText) {
		int x = 0;
		do {
			try {
				System.out.printf("%s", Text);
				x = Main.scan.nextInt();
				if(start != end) {
					while(x < start || x > end) {
						System.out.printf("%d~%d 사이의 숫자를 입력해주세요.\n", start, end);
						System.out.print("입력 : ");
						x = Main.scan.nextInt();
					}
				}
			}catch (Exception e) {
				System.out.printf("%s", warningText);
				x = -1;
			}finally {
				Main.clearBuffer();
			}
		}while(x == -1);
		return x;
	}
}
