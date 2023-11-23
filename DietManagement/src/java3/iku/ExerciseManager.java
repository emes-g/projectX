package java3.iku;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class ExerciseManager {
	
	private ArrayList<Exercise> exercises;	// 사용자가 수행한 운동 리스트
	private ArrayList<ExerciseInFile> exerciseInFile;	// 파일에 저장된 운동 리스트
	
	public ExerciseManager() {
		exercises = new ArrayList<Exercise>();
		exerciseInFile = new ArrayList<ExerciseInFile>();
		try {
			File f = new File("exercise.txt");
			if (f.createNewFile()) {
				System.out.println("파일 생성 완료 : " + f.getName());
			} else {
				Scanner fscan = new Scanner(f);
				while(fscan.hasNextLine()) {
					String line = fscan.nextLine();
					String[] element = line.split("\t");
					String name = element[0];
					int calorie = Integer.parseInt(element[1]);	// 1시간 소모 칼로리
					
					exerciseInFile.add(new ExerciseInFile(name, calorie));
				}
			}
		} catch (Exception e) {
			System.out.println("에러 발생");
			e.printStackTrace();
		}
	}
	public ArrayList<Exercise> getExercises() {
		return exercises;
	}
	public ArrayList<ExerciseInFile> getExerciseInFile() {
		return exerciseInFile;
	}
	public int getTotalCalorie() {
		int total = 0;
		for(int i=0; i<exercises.size(); i++) {
			String name = exercises.get(i).getName();
			int min = exercises.get(i).getMinute();
			for(ExerciseInFile e : exerciseInFile) {
				if(name.equals(e.getName())) {
					total += e.getCalorie() / 60.0 * min; 
					break;
				}
			}
		}
		return total;
	}
	
// =============================== [2] 운동 기록하기 ===============================
	public void recordExercise() {
		System.out.println("\n[2] 운동 기록하기");
		System.out.println("1.운동 추가하기 2.운동 기록하기 3.메뉴로 돌아가기");
		
		int select = 0;
		if((select = getInt(1, 3, "입력 : ", "1~3 사이의 숫자를 입력해주세요.\n")) == 3)	return;
		switch(select) {
		case 1:	// 운동 추가하기
			String name;
			System.out.print("운동 : ");
			name = Main.scan.nextLine();
	
			int calorie;	// 1시간 소모 칼로리
			calorie = getInt(0, 0, "1시간 소모 칼로리 : ", "0 이상의 숫자를 입력해주세요.\n");
			
			try {
				FileWriter fw = new FileWriter("exercise.txt", true);
				fw.write(name + "\t" + calorie + "\n");
				fw.flush();
			}catch (Exception e) {
				System.out.println("에러 발생");
				e.printStackTrace();
			}
			exerciseInFile.add(new ExerciseInFile(name, calorie));
			break;
		case 2:	// 운동 기록하기
			int count;
			for(count=0; count<exerciseInFile.size(); count++) {
				if(count % 5 == 0 && count != 0) {
					System.out.println();
				}
				System.out.printf("%d.%s ", count+1, exerciseInFile.get(count).getName());
			}
			System.out.println();
			int exerciseType, exerciseTime;
			exerciseType = getInt(1, count, "입력 : ","숫자를 입력해주세요.\n");
			exerciseTime = getInt(0, 0, "운동 시간(분) : ", "0 이상의 숫자를 입력해주세요.\n");
			exercises.add(new Exercise(exerciseInFile.get(exerciseType-1).getName(), exerciseTime));
			
			System.out.printf("소모 칼로리는 %.0fkcal입니다.\n", 
					exerciseInFile.get(exerciseType-1).getCalorie() / 60.0 * exerciseTime);
			break;
		case 3:
			return;
		}
	}
	public int getCalorieByExercise(Exercise e) {
		for(ExerciseInFile eif : exerciseInFile) {
			if(e.getName().equals(eif.getName())) {
				return (int)(eif.getCalorie() / 60.0 * e.getMinute()); 
			}
		}
		return -1;
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
