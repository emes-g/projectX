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
	public void recordExercise() {
		System.out.println("\n[3] 운동 기록하기");
		System.out.println("1.운동 추가하기 2.운동 기록하기");
		
		System.out.print("입력 : ");
		int select = Main.scan.nextInt();
		switch(select) {
		case 1:	// 운동 추가하기
			String name;
			System.out.print("운동 : ");
			Main.scan.nextLine();	// 버퍼 비우기
			name = Main.scan.nextLine();
	
			int calorie;	// 1시간 소모 칼로리
			System.out.print("1시간 소모 칼로리 : ");
			calorie = Main.scan.nextInt();
			
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
			for(int i=0; i<exerciseInFile.size(); i++) {
				if(i % 5 == 0 && i != 0) {
					System.out.println();
				}
				System.out.printf("%d.%s ", i+1, exerciseInFile.get(i).getName());
			}
			System.out.println();
			int exerciseType, exerciseTime;
			System.out.print("입력 : ");
			exerciseType = Main.scan.nextInt();
			System.out.print("운동 시간(분) : ");
			exerciseTime = Main.scan.nextInt();
			exercises.add(new Exercise(exerciseInFile.get(exerciseType-1).getName(), exerciseTime));
			
			System.out.printf("소모 칼로리는 %.0fkcal입니다.\n", 
					exerciseInFile.get(exerciseType-1).getCalorie() / 60.0 * exerciseTime);
			break;
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
}
