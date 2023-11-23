package java3.iku;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {

	public static Scanner scan = new Scanner(System.in);
	public static ExerciseManager em = new ExerciseManager();

	public static void main(String[] args) {
		mainPrompt();
		scan.close();
	}

	public static void mainPrompt() {

		int select = 0;
		DietManager dm = new DietManager();
		dm.personalization();
		do {
			System.out.println("*** 식단 관리 프로그램");
			System.out.printf("%.0f / %d kcal\n", dm.getCurrentCalorie(), dm.getTargetCalorie());
			// 출력 : 목표 칼로리 섭취 부족\n\n

			System.out.print("[1] 식단 기록하기\t\t");
			System.out.print("[2] 운동 기록하기\t\t");
			System.out.print("[3] 현재 칼로리 확인하기\n");
			System.out.print("[4] 자주 먹는 식단 저장하기\t");
			System.out.print("[5] 내 정보 확인하기\t\t");
			System.out.print("[6] 종료하기\n");
			System.out.print("입력 : ");
			try {
				select = scan.nextInt();
			}catch (InputMismatchException e) {
				System.out.println("1~6 사이의 숫자를 입력해주세요.\n");
				continue;
			}finally {
				clearBuffer();
			}
			switch (select) {
			case 1:
				dm.recordDiet();
				break;
			case 2:
				em.recordExercise();
				break;
			case 3:
				dm.checkCalorie();
				break;
			case 4:
				dm.saveDiet();
				break;
			case 5:
				dm.watchMyInfo();
				break;
			case 6:
				System.out.println("식단 관리 프로그램을 종료합니다.");
				return;
			default:
				System.out.println("1~6 사이의 숫자를 입력해주세요.");
				break;
			}
			System.out.println("\n"+"=".repeat(70));
		} while (select != 6);
	}

	public static void clearBuffer() {
		scan.nextLine();
	}
}
