package java3.iku;

import java.util.Scanner;

public class Main {
	
	public static Scanner scan = new Scanner(System.in);
	public static ExerciseManager em = new ExerciseManager();
	
	public static void main(String[] args) {
		mainPrompt();
		scan.close();
	}
	
	public static void mainPrompt() {
		int select;
		DietManager dm = new DietManager();
		
		do {
			System.out.println("*** 식단 관리 프로그램");
			// 출력 : 0 / 2000 kcal\n
			// 출력 : 목표 칼로리 섭취 부족\n\n
			
			System.out.print("[1] 식단 기록하기\t");
			System.out.print("[2] 현재 칼로리 확인하기\n");
			System.out.print("[3] 운동 기록하기\t");
			System.out.print("[4] 자주 먹는 식단 저장하기\t");
			System.out.print("[5] 종료\n");
			System.out.print("입력 : ");
			select = scan.nextInt();
			switch(select) {
			case 1:
				dm.recordDiet();
				break;
			case 2:
				dm.checkCalorie();
				break;
			case 3:
				em.recordExercise();
				break;
			case 4:
				dm.saveDiet();
				break;
			case 5:
				System.out.println("식단 관리 프로그램을 종료합니다.");
				return;
			}
			System.out.println("\n========================================");
		}while(select != 5);
	}
	
	public static void clearBuffer() {
		scan.nextLine();
	}
}
