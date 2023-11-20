package java3.iku;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;

public class DietManager {
	
	private final int TYPE_NUM = 4;
	private ArrayList<Diet>[] diets;
	private HashMap<String, ArrayList<Diet>> frequentlyEatenDiet;
	private int type;
	
	public DietManager() {
		this.diets = new ArrayList[TYPE_NUM];
		this.frequentlyEatenDiet = new HashMap<String, ArrayList<Diet>>();
		
		try {
			File f = new File("frequentlyEatenDiet.txt");
			if (f.createNewFile()) {
				System.out.println("파일 생성 완료 : " + f.getName());
			} else {
				Scanner fscan = new Scanner(f);
				boolean parenthesisOpen = false;	// ====================(해당 식단의 시작과 끝을 파악)
				while(fscan.hasNextLine()) {
					String line = fscan.nextLine();
					if(line.equals("====================")) {
						parenthesisOpen = true;
					}
					if(parenthesisOpen) {
						ArrayList<Diet> diet = new ArrayList<Diet>();
						String name = fscan.nextLine();
						while(true) {
							line = fscan.nextLine();
							if(line.equals("====================")) {
								parenthesisOpen = false;
								break;
							}
							String[] element = line.split("\t");
							diet.add(new Diet(element[0], Integer.parseInt(element[1])));
						}
						frequentlyEatenDiet.put(name, diet);
					}
				}
			}
		}catch(Exception e) {
			System.out.println("에러 발생");
			e.printStackTrace();
		}
		for(int i=0; i<TYPE_NUM; i++) {
			diets[i] = new ArrayList<Diet>();
		}
	}
	public ArrayList<Diet>[] getDiets() {
		return diets;
	}
//	public void addDiet(Diet diet) {
//		diets[type-1].add(diet);
//	}
//	public void deleteDiet(Diet diet) {
//		diets[type-1].remove(diet);
//	}
	public int getTotalCalorie(int type) {
		int total = 0;
		for(int i=0; i<diets[type].size(); i++) {
			total += diets[type].get(i).getCalorie();
		}
		return total;
	}
	public void recordDiet() {
		System.out.println("\n[1] 식단 기록하기");
		System.out.println("1.아침 2.점심 3.저녁 4.간식");
		System.out.print("입력 : ");
		type = Main.scan.nextInt();
		
		int select;
		System.out.println("1.개별 음식 추가하기 2.식단 불러오기");
		System.out.print("입력 : ");
		select = Main.scan.nextInt();
		System.out.println("기록을 종료하려면 [종료]를 입력하세요");
		
		String food;
		int calorie;
		switch(select) {
		case 1:	// 개별 음식 추가
			while(true) {
				System.out.print("음식 : ");
				Main.clearBuffer();
				food = Main.scan.nextLine();
				if(food.equals("종료")) {
					System.out.println("식단 기록을 종료합니다.");
					break;
				}
				System.out.print("칼로리 : ");
				calorie = Main.scan.nextInt();
				diets[type-1].add(new Diet(food, calorie));
			}
			break;
		case 2: // 식단 불러오기
			int count = 1;
			ArrayList<String> foodKind = new ArrayList<String>();
			
			for(String key : frequentlyEatenDiet.keySet()) {
				if(count % 5 == 0) {
					System.out.println();
				}
				System.out.printf("%d.%s ", count++, key);
				foodKind.add(key);
			}
			System.out.print("\n식단 선택 : ");
			int choice = Main.scan.nextInt();
			Main.clearBuffer();
			
			ArrayList<Diet> diet = frequentlyEatenDiet.get(foodKind.get(choice-1));
			for(Diet d : diet) {
				diets[type-1].add(d);
			}
			break;
		}
	}
	public void checkCalorie() {
		final String[] time = {"아침", "점심", "저녁", "간식"};
		
		int total = 0;
		System.out.println("===============");
		for(int i=0; i<TYPE_NUM; i++) {
			System.out.printf("%d.%s\t%dkcal\n", i+1, time[i], getTotalCalorie(i));
			total += getTotalCalorie(i);
			if(getTotalCalorie(i) != 0) {
				System.out.println("---------------");
			}
			for(int j=0; j<diets[i].size(); j++) {
				System.out.printf("%s\t%dkcal\n", diets[i].get(j).getFood(), diets[i].get(j).getCalorie());
			}
			System.out.println("===============");
		}
		
		System.out.printf("%d.운동\t%dkcal\n", TYPE_NUM+1, Main.em.getTotalCalorie());
		total -= Main.em.getTotalCalorie();
		if(Main.em.getTotalCalorie() != 0) {
			System.out.println("---------------");
		}
		ArrayList<Exercise> exercises = Main.em.getExercises();
		for(int i=0; i<exercises.size(); i++) {
			System.out.printf("%s\t%dkcal\n", exercises.get(i).getName(),
					Main.em.getCalorieByExercise(exercises.get(i)));
		}
		System.out.println("===============");
		System.out.printf("현재 칼로리 : %dkcal\n", total);
		System.out.println("===============");		
	}
	public void saveDiet() {
		String name, food;
		int calorie;
		ArrayList<Diet> diet = new ArrayList<Diet>();
		
		Main.clearBuffer();	// 버퍼 비우기
		System.out.print("식단 이름 : ");
		name = Main.scan.nextLine();
		System.out.println("음식 추가를 종료하려면 [종료]를 입력하세요");
		while(true) {
			System.out.print("음식: ");
			food = Main.scan.nextLine();
			if(food.equals("종료")) {
				System.out.println("자주 먹는 식단 저장을 종료합니다.");
				break;
			}
			System.out.print("칼로리 : ");
			calorie = Main.scan.nextInt();
			Main.clearBuffer();
			diet.add(new Diet(food, calorie));
		}
		frequentlyEatenDiet.put(name, diet);
		
		try {
			FileWriter fw = new FileWriter("frequentlyEatenDiet.txt", true);
			fw.write("====================\n");
			fw.write(name + "\n");
			for(Diet d : diet) {
				fw.write(d.getFood() + "\t" + d.getCalorie() + "\n");
			}
			fw.write("====================\n\n");
			fw.flush();
		}catch(Exception e) {
			System.out.println("에러 발생");
			e.printStackTrace();
		}
	}
}
