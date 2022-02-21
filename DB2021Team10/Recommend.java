package DB2021Team10;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class Recommend extends JFrame {

	Connection conn;
	Statement stmt;

	// 테이블 타이틀,패널
	JLabel menu, l1, l2_1, l2_2, l2_3, l2_4, l3_1, l3_2, l3_3, l3_4, l4_1, l4_2, l4_3, l4_4;
	JPanel mainPanel, panel1, panel2_1, panel2_2, panel2_3, panel2_4, panel3_1, panel3_2, panel3_3, panel3_4, panel4_1,
			panel4_2, panel4_3, panel4_4; // +JLabel 부착
	JPanel group1, group2, group3;

	// <비슷한 영화들> JTable
	Object ob1[][] = new Object[0][4]; // 데이터 표시(행X) 열만 나오게 설정
	DefaultTableModel model1; // 데이터 저장부분
	JTable table1;
	JScrollPane js1;
	String str1[] = { "제목", "감독", "비슷한_영화_제목", "비슷한_영화의_감독" }; // 컬럼명

	// <심심할 때 보기 좋은 영화들> JTable
	Object ob[][] = new Object[0][6]; // 데이터 표시(행X) 열만 나오게 설정
	DefaultTableModel model2_1; // 데이터 저장부분
	JTable table2_1;
	JScrollPane js2_1;
	String str[] = { "제목", "감독", "장르", "줄거리", "국가", "러닝_타임" }; // 컬럼명

	// <우울할 때 보기 좋은 영화들> JTable
	DefaultTableModel model2_2; // 데이터 저장부분
	JTable table2_2;
	JScrollPane js2_2;

	// <잠이 안 올 때 보기 좋은 영화들> JTable
	DefaultTableModel model2_3; // 데이터 저장부분
	JTable table2_3;
	JScrollPane js2_3;

	// <설레고 싶을 때 보기 좋은 영화들> JTable
	DefaultTableModel model2_4; // 데이터 저장부분
	JTable table2_4;
	JScrollPane js2_4;

	// <맑은 날에 보기 좋은 영화들> JTable
	DefaultTableModel model3_1; // 데이터 저장부분
	JTable table3_1;
	JScrollPane js3_1;

	// <흐린 날에 보기 좋은 영화들> JTable
	DefaultTableModel model3_2; // 데이터 저장부분
	JTable table3_2;
	JScrollPane js3_2;

	// <눈 오는 날에 보기 좋은 영화들> JTable
	DefaultTableModel model3_3; // 데이터 저장부분
	JTable table3_3;
	JScrollPane js3_3;

	// <비 오는 날에 보기 좋은 영화들> JTable
	DefaultTableModel model3_4; // 데이터 저장부분
	JTable table3_4;
	JScrollPane js3_4;

	// <혼자 보기 좋은 영화들> JTable
	DefaultTableModel model4_1; // 데이터 저장부분
	JTable table4_1;
	JScrollPane js4_1;

	// <연인과 함께 보기 좋은 영화들> JTable
	DefaultTableModel model4_2; // 데이터 저장부분
	JTable table4_2;
	JScrollPane js4_2;

	// <가족과 함께 보기 좋은 영화들> JTable
	DefaultTableModel model4_3; // 데이터 저장부분
	JTable table4_3;
	JScrollPane js4_3;

	// <친구와 함께 보기 좋은 영화들> JTable
	DefaultTableModel model4_4; // 데이터 저장부분
	JTable table4_4;
	JScrollPane js4_4;

	Recommend(Connection conn, Statement stmt) {
		this.conn = conn;
		this.stmt = stmt;

		// 가장 큰 패널, 세로로 정렬
		mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

		// 추천 종목별 패널, 가로로 정렬
		group1 = new JPanel();
		group1.setLayout(new GridLayout(2, 2));
		group2 = new JPanel();
		group2.setLayout(new GridLayout(2, 2));
		group3 = new JPanel();
		group3.setLayout(new GridLayout(2, 2));

		// 테이블을 붙일 패널생성
		panel1 = new JPanel();
		panel1.setBorder(new EmptyBorder(20, 40, 20, 40));
		panel1.setLayout(new BorderLayout());

		panel2_1 = new JPanel();
		panel2_1.setBorder(new EmptyBorder(20, 40, 20, 40));
		panel2_1.setLayout(new BorderLayout());

		panel2_2 = new JPanel();
		panel2_2.setBorder(new EmptyBorder(20, 40, 20, 40));
		panel2_2.setLayout(new BorderLayout());

		panel2_3 = new JPanel();
		panel2_3.setBorder(new EmptyBorder(20, 40, 20, 40));
		panel2_3.setLayout(new BorderLayout());

		panel2_4 = new JPanel();
		panel2_4.setBorder(new EmptyBorder(20, 40, 20, 40));
		panel2_4.setLayout(new BorderLayout());

		panel3_1 = new JPanel();
		panel3_1.setBorder(new EmptyBorder(20, 40, 20, 40));
		panel3_1.setLayout(new BorderLayout());

		panel3_2 = new JPanel();
		panel3_2.setBorder(new EmptyBorder(20, 40, 20, 40));
		panel3_2.setLayout(new BorderLayout());

		panel3_3 = new JPanel();
		panel3_3.setBorder(new EmptyBorder(20, 40, 20, 40));
		panel3_3.setLayout(new BorderLayout());

		panel3_4 = new JPanel();
		panel3_4.setBorder(new EmptyBorder(20, 40, 20, 40));
		panel3_4.setLayout(new BorderLayout());

		panel4_1 = new JPanel();
		panel4_1.setBorder(new EmptyBorder(20, 40, 20, 40));
		panel4_1.setLayout(new BorderLayout());

		panel4_2 = new JPanel();
		panel4_2.setBorder(new EmptyBorder(20, 40, 20, 40));
		panel4_2.setLayout(new BorderLayout());

		panel4_3 = new JPanel();
		panel4_3.setBorder(new EmptyBorder(20, 40, 20, 40));
		panel4_3.setLayout(new BorderLayout());

		panel4_4 = new JPanel();
		panel4_4.setBorder(new EmptyBorder(20, 40, 20, 40));
		panel4_4.setLayout(new BorderLayout());

		// 각 테이블 이름을 레이블로 달기
		Font f1 = new Font("굴림", Font.BOLD, 20);

		menu = new JLabel("추천", JLabel.CENTER);
		menu.setFont(f1);
		menu.setBorder(new EmptyBorder(40, 0, 0, 0));
		mainPanel.add(menu);

		l1 = new JLabel("<비슷한 영화들>", JLabel.CENTER);
		l1.setFont(f1);
		l1.setBorder(new EmptyBorder(40, 40, 20, 40));
		panel1.add(l1, BorderLayout.NORTH);

		l2_1 = new JLabel("<심심할 때 보기 좋은 영화들>", JLabel.CENTER);
		l2_1.setFont(f1);
		l2_1.setBorder(new EmptyBorder(0, 40, 20, 40));
		panel2_1.add(l2_1, BorderLayout.NORTH);

		l2_2 = new JLabel("<우울할 때 보기 좋은 영화들>", JLabel.CENTER);
		l2_2.setFont(f1);
		l2_2.setBorder(new EmptyBorder(0, 40, 20, 40));
		panel2_2.add(l2_2, BorderLayout.NORTH);

		l2_3 = new JLabel("<잠이 안 올 때 보기 좋은 영화들>", JLabel.CENTER);
		l2_3.setFont(f1);
		l2_3.setBorder(new EmptyBorder(0, 40, 20, 40));
		panel2_3.add(l2_3, BorderLayout.NORTH);

		l2_4 = new JLabel("<설레고 싶을 때 보기 좋은 영화들>", JLabel.CENTER);
		l2_4.setFont(f1);
		l2_4.setBorder(new EmptyBorder(0, 40, 20, 40));
		panel2_4.add(l2_4, BorderLayout.NORTH);

		l3_1 = new JLabel("<맑은 날에 보기 좋은 영화들>", JLabel.CENTER);
		l3_1.setFont(f1);
		l3_1.setBorder(new EmptyBorder(0, 40, 20, 40));
		panel3_1.add(l3_1, BorderLayout.NORTH);

		l3_2 = new JLabel("<흐린 날에 보기 좋은 영화들>", JLabel.CENTER);
		l3_2.setFont(f1);
		l3_2.setBorder(new EmptyBorder(0, 40, 20, 40));
		panel3_2.add(l3_2, BorderLayout.NORTH);

		l3_3 = new JLabel("<눈 오는 날에 보기 좋은 영화들>", JLabel.CENTER);
		l3_3.setFont(f1);
		l3_3.setBorder(new EmptyBorder(0, 40, 20, 40));
		panel3_3.add(l3_3, BorderLayout.NORTH);

		l3_4 = new JLabel("<비 오는 날에 보기 좋은 영화들>", JLabel.CENTER);
		l3_4.setFont(f1);
		l3_4.setBorder(new EmptyBorder(0, 40, 20, 40));
		panel3_4.add(l3_4, BorderLayout.NORTH);

		l4_1 = new JLabel("<혼자 보기 좋은 영화들>", JLabel.CENTER);
		l4_1.setFont(f1);
		l4_1.setBorder(new EmptyBorder(0, 40, 20, 40));
		panel4_1.add(l4_1, BorderLayout.NORTH);

		l4_2 = new JLabel("<연인과 함께 보기 좋은 영화들>", JLabel.CENTER);
		l4_2.setFont(f1);
		l4_2.setBorder(new EmptyBorder(0, 40, 20, 40));
		panel4_2.add(l4_2, BorderLayout.NORTH);

		l4_3 = new JLabel("<가족과 함께 보기 좋은 영화들>", JLabel.CENTER);
		l4_3.setFont(f1);
		l4_3.setBorder(new EmptyBorder(0, 40, 20, 40));
		panel4_3.add(l4_3, BorderLayout.NORTH);

		l4_4 = new JLabel("<친구와 함께 보기 좋은 영화들>", JLabel.CENTER);
		l4_4.setFont(f1);
		l4_4.setBorder(new EmptyBorder(0, 40, 20, 40));
		panel4_4.add(l4_4, BorderLayout.NORTH);

		// <비슷한 영화들> JTable 생성, 배치
		model1 = new DefaultTableModel(ob1, str1); // 1)데이저 저장[][], 2)컬럼명
		table1 = new JTable(model1); // table=new JTable(ob,str);
		js1 = new JScrollPane(table1);
		js1.setPreferredSize(new Dimension(1100, 350));
		js1.setBorder(new EmptyBorder(0, 40, 40, 40));
		panel1.add(js1, BorderLayout.CENTER);
		mainPanel.add(panel1);

		// <심심할 때 보기 좋은 영화들> JTable 생성, 배치
		model2_1 = new DefaultTableModel(ob, str); // 1)데이저 저장[][], 2)컬럼명
		table2_1 = new JTable(model2_1); // table=new JTable(ob,str);
		js2_1 = new JScrollPane(table2_1);
		js2_1.setBorder(new EmptyBorder(0, 40, 40, 40));
		panel2_1.add(js2_1, BorderLayout.CENTER);
		group1.add(panel2_1);

		// <우울할 때 보기 좋은 영화들> JTable 생성, 배치
		model2_2 = new DefaultTableModel(ob, str); // 1)데이저 저장[][], 2)컬럼명
		table2_2 = new JTable(model2_2); // table=new JTable(ob,str);
		js2_2 = new JScrollPane(table2_2);
		js2_2.setBorder(new EmptyBorder(0, 40, 40, 40));
		panel2_2.add(js2_2, BorderLayout.CENTER);
		group1.add(panel2_2);

		// <잠이 안 올 때 보기 좋은 영화들> JTable 생성, 배치
		model2_3 = new DefaultTableModel(ob, str); // 1)데이저 저장[][], 2)컬럼명
		table2_3 = new JTable(model2_3); // table=new JTable(ob,str);
		js2_3 = new JScrollPane(table2_3);
		js2_3.setBorder(new EmptyBorder(0, 40, 40, 40));
		panel2_3.add(js2_3, BorderLayout.CENTER);
		group1.add(panel2_3);

		// <설레고 싶을 때 보기 좋은 영화들> JTable 생성, 배치
		model2_4 = new DefaultTableModel(ob, str); // 1)데이저 저장[][], 2)컬럼명
		table2_4 = new JTable(model2_4); // table=new JTable(ob,str);
		js2_4 = new JScrollPane(table2_4);
		js2_4.setBorder(new EmptyBorder(0, 40, 40, 40));
		panel2_4.add(js2_4, BorderLayout.CENTER);
		group1.add(panel2_4);

		mainPanel.add(group1);

		// <맑은 날에 보기 좋은 영화들> JTable 생성, 배치
		model3_1 = new DefaultTableModel(ob, str); // 1)데이저 저장[][], 2)컬럼명
		table3_1 = new JTable(model3_1); // table=new JTable(ob,str);
		js3_1 = new JScrollPane(table3_1);
		js3_1.setBorder(new EmptyBorder(0, 40, 40, 40));
		panel3_1.add(js3_1, BorderLayout.CENTER);
		group2.add(panel3_1);

		// <흐린 날에 보기 좋은 영화들> JTable 생성, 배치
		model3_2 = new DefaultTableModel(ob, str); // 1)데이저 저장[][], 2)컬럼명
		table3_2 = new JTable(model3_2); // table=new JTable(ob,str);
		js3_2 = new JScrollPane(table3_2);
		js3_2.setBorder(new EmptyBorder(0, 40, 40, 40));
		panel3_2.add(js3_2, BorderLayout.CENTER);
		group2.add(panel3_2);

		// <눈 오는 날에 보기 좋은 영화들> JTable 생성, 배치
		model3_3 = new DefaultTableModel(ob, str); // 1)데이저 저장[][], 2)컬럼명
		table3_3 = new JTable(model3_3); // table=new JTable(ob,str);
		js3_3 = new JScrollPane(table3_3);
		js3_3.setBorder(new EmptyBorder(0, 40, 40, 40));
		panel3_3.add(js3_3, BorderLayout.CENTER);
		group2.add(panel3_3);

		// <비 오는 날에 보기 좋은 영화들> JTable 생성, 배치
		model3_4 = new DefaultTableModel(ob, str); // 1)데이저 저장[][], 2)컬럼명
		table3_4 = new JTable(model3_4); // table=new JTable(ob,str);
		js3_4 = new JScrollPane(table3_4);
		js3_4.setBorder(new EmptyBorder(0, 40, 40, 40));
		panel3_4.add(js3_4, BorderLayout.CENTER);
		group2.add(panel3_4);

		mainPanel.add(group2);

		// <혼자 보기 좋은 영화들> JTable 생성, 배치
		model4_1 = new DefaultTableModel(ob, str); // 1)데이저 저장[][], 2)컬럼명
		table4_1 = new JTable(model4_1); // table=new JTable(ob,str);
		js4_1 = new JScrollPane(table4_1);
		js4_1.setBorder(new EmptyBorder(0, 40, 40, 40));
		panel4_1.add(js4_1, BorderLayout.CENTER);
		group3.add(panel4_1);

		// <연인과 함께 보기 좋은 영화들> JTable 생성, 배치
		model4_2 = new DefaultTableModel(ob, str); // 1)데이저 저장[][], 2)컬럼명
		table4_2 = new JTable(model4_2); // table=new JTable(ob,str);
		js4_2 = new JScrollPane(table4_2);
		js4_2.setBorder(new EmptyBorder(0, 40, 40, 40));
		panel4_2.add(js4_2, BorderLayout.CENTER);
		group3.add(panel4_2);

		// <가족과 함께 보기 좋은 영화들> JTable 생성, 배치
		model4_3 = new DefaultTableModel(ob, str); // 1)데이저 저장[][], 2)컬럼명
		table4_3 = new JTable(model4_3); // table=new JTable(ob,str);
		js4_3 = new JScrollPane(table4_3);
		js4_3.setBorder(new EmptyBorder(0, 40, 40, 40));
		panel4_3.add(js4_3, BorderLayout.CENTER);
		group3.add(panel4_3);

		// <친구와 함께 보기 좋은 영화들> JTable 생성, 배치
		model4_4 = new DefaultTableModel(ob, str); // 1)데이저 저장[][], 2)컬럼명
		table4_4 = new JTable(model4_4); // table=new JTable(ob,str);
		js4_4 = new JScrollPane(table4_4);
		js4_4.setBorder(new EmptyBorder(0, 40, 40, 40));
		panel4_4.add(js4_4, BorderLayout.CENTER);
		group3.add(panel4_4);

		mainPanel.add(group3);

		getTable1();

		getTable2_1();
		getTable2_2();
		getTable2_3();
		getTable2_4();

		getTable3_1();
		getTable3_2();
		getTable3_3();
		getTable3_4();

		getTable4_1();
		getTable4_2();
		getTable4_3();
		getTable4_4();
	}

	public void getTable1() {

		try {

			ResultSet rs = stmt.executeQuery("SELECT 제목, 감독, 비슷한_영화_제목, 비슷한_영화의_감독 FROM DB2021_평가");

			while (rs.next()) {
				String title = rs.getString("제목");
				String director = rs.getString("감독");
				String simTitle = rs.getString("비슷한_영화_제목");
				String simDirector = rs.getString("비슷한_영화의_감독");

				Object data[] = { title, director, simTitle, simDirector };
				model1.addRow(data);
		
			}

		} catch (SQLException sqle) {
			System.out.println("SQLException : " + sqle);
		}

	}

	public void getTable2_1() {

		try {

			ResultSet rs = stmt.executeQuery(
					"SELECT 제목, 감독, 장르, 줄거리, 국가, 러닝_타임 FROM DB2021_영화 WHERE 제목 in (SELECT 제목 FROM DB2021_평가 WHERE 추천_감정 = '심심할 때')");

			while (rs.next()) {
				String title = rs.getString("제목");
				String director = rs.getString("감독");
				String genre = rs.getString("장르");
				String story = rs.getString("줄거리");
				String country = rs.getString("국가");
				int runningTime = rs.getInt("러닝_타임");

				Object data[] = { title, director, genre, story, country, runningTime };
				model2_1.addRow(data);
		
			}

		} catch (SQLException sqle) {
			System.out.println("SQLException : " + sqle);
		}

	}

	public void getTable2_2() {

		try {

			ResultSet rs = stmt.executeQuery(
					"SELECT 제목, 감독, 장르, 줄거리, 국가, 러닝_타임 FROM DB2021_영화 WHERE 제목 in (SELECT 제목 FROM DB2021_평가 WHERE 추천_감정 = '우울할 때') and 감독 in (SELECT 감독 FROM DB2021_평가 WHERE 추천_감정 = '우울할 때')");

			while (rs.next()) {
				String title = rs.getString("제목");
				String director = rs.getString("감독");
				String genre = rs.getString("장르");
				String story = rs.getString("줄거리");
				String country = rs.getString("국가");
				int runningTime = rs.getInt("러닝_타임");

				Object data[] = { title, director, genre, story, country, runningTime };
				model2_2.addRow(data);
			
			}

		} catch (SQLException sqle) {
			System.out.println("SQLException : " + sqle);
		}

	}

	public void getTable2_3() {

		try {
		
			ResultSet rs = stmt.executeQuery(
					"SELECT 제목, 감독, 장르, 줄거리, 국가, 러닝_타임 FROM DB2021_영화 WHERE 제목 in (SELECT 제목 FROM DB2021_평가 WHERE 추천_감정 = '잠이 안 올 때') and 감독 in (SELECT 감독 FROM DB2021_평가 WHERE 추천_감정 = '잠이 안 올 때')");

			while (rs.next()) {
				String title = rs.getString("제목");
				String director = rs.getString("감독");
				String genre = rs.getString("장르");
				String story = rs.getString("줄거리");
				String country = rs.getString("국가");
				int runningTime = rs.getInt("러닝_타임");

				Object data[] = { title, director, genre, story, country, runningTime };
				model2_3.addRow(data);
			
			}

		} catch (SQLException sqle) {
			System.out.println("SQLException : " + sqle);
		}

	}

	public void getTable2_4() {

		try {
	
			ResultSet rs = stmt.executeQuery(
					"SELECT 제목, 감독, 장르, 줄거리, 국가, 러닝_타임 FROM DB2021_영화 WHERE 제목 in (SELECT 제목 FROM DB2021_평가 WHERE 추천_감정 = '설레고 싶을 때') and 감독 in (SELECT 감독 FROM DB2021_평가 WHERE 추천_감정 = '설레고 싶을 때')");

			while (rs.next()) {
				String title = rs.getString("제목");
				String director = rs.getString("감독");
				String genre = rs.getString("장르");
				String story = rs.getString("줄거리");
				String country = rs.getString("국가");
				int runningTime = rs.getInt("러닝_타임");

				Object data[] = { title, director, genre, story, country, runningTime };
				model2_4.addRow(data);
			
			}

		} catch (SQLException sqle) {
			System.out.println("SQLException : " + sqle);
		}

	}

	public void getTable3_1() {

		try {
		
			ResultSet rs = stmt.executeQuery(
					"SELECT 제목, 감독, 장르, 줄거리, 국가, 러닝_타임 FROM DB2021_영화 WHERE 제목 in (SELECT 제목 FROM DB2021_평가 WHERE 추천_날씨 = '맑은 날') and 감독 in (SELECT 감독 FROM DB2021_평가 WHERE 추천_날씨 = '맑은 날')");

			while (rs.next()) {
				String title = rs.getString("제목");
				String director = rs.getString("감독");
				String genre = rs.getString("장르");
				String story = rs.getString("줄거리");
				String country = rs.getString("국가");
				int runningTime = rs.getInt("러닝_타임");

				Object data[] = { title, director, genre, story, country, runningTime };
				model3_1.addRow(data);
			
			}

		} catch (SQLException sqle) {
			System.out.println("SQLException : " + sqle);
		}

	}

	public void getTable3_2() {

		try {
		
			ResultSet rs = stmt.executeQuery(
					"SELECT 제목, 감독, 장르, 줄거리, 국가, 러닝_타임 FROM DB2021_영화 WHERE 제목 in (SELECT 제목 FROM DB2021_평가 WHERE 추천_날씨 = '흐린 날') and 감독 in (SELECT 감독 FROM DB2021_평가 WHERE 추천_날씨 = '흐린 날')");

			while (rs.next()) {
				String title = rs.getString("제목");
				String director = rs.getString("감독");
				String genre = rs.getString("장르");
				String story = rs.getString("줄거리");
				String country = rs.getString("국가");
				int runningTime = rs.getInt("러닝_타임");

				Object data[] = { title, director, genre, story, country, runningTime };
				model3_2.addRow(data);
				
			}

		} catch (SQLException sqle) {
			System.out.println("SQLException : " + sqle);
		}

	}

	public void getTable3_3() {

		try {
		
			ResultSet rs = stmt.executeQuery(
					"SELECT 제목, 감독, 장르, 줄거리, 국가, 러닝_타임 FROM DB2021_영화 WHERE 제목 in (SELECT 제목 FROM DB2021_평가 WHERE 추천_날씨 = '눈 오는 날') and 감독 in (SELECT 감독 FROM DB2021_평가 WHERE 추천_날씨 = '눈 오는 날')");

			while (rs.next()) {
				String title = rs.getString("제목");
				String director = rs.getString("감독");
				String genre = rs.getString("장르");
				String story = rs.getString("줄거리");
				String country = rs.getString("국가");
				int runningTime = rs.getInt("러닝_타임");

				Object data[] = { title, director, genre, story, country, runningTime };
				model3_3.addRow(data);
				
			}

		} catch (SQLException sqle) {
			System.out.println("SQLException : " + sqle);
		}

	}

	public void getTable3_4() {

		try {
		
			ResultSet rs = stmt.executeQuery(
					"SELECT 제목, 감독, 장르, 줄거리, 국가, 러닝_타임 FROM DB2021_영화 WHERE 제목 in (SELECT 제목 FROM DB2021_평가 WHERE 추천_날씨 = '비 오는 날') and 감독 in (SELECT 감독 FROM DB2021_평가 WHERE 추천_날씨 = '비 오는 날')");

			while (rs.next()) {
				String title = rs.getString("제목");
				String director = rs.getString("감독");
				String genre = rs.getString("장르");
				String story = rs.getString("줄거리");
				String country = rs.getString("국가");
				int runningTime = rs.getInt("러닝_타임");

				Object data[] = { title, director, genre, story, country, runningTime };
				model3_4.addRow(data);
		
			}

		} catch (SQLException sqle) {
			System.out.println("SQLException : " + sqle);
		}

	}

	public void getTable4_1() {

		try {
		
			ResultSet rs = stmt.executeQuery(
					"SELECT 제목, 감독, 장르, 줄거리, 국가, 러닝_타임 FROM DB2021_영화 WHERE 제목 in (SELECT 제목 FROM DB2021_평가 WHERE 추천_관계 = '혼자') and 감독 in (SELECT 감독 FROM DB2021_평가 WHERE 추천_관계 = '혼자')");

			while (rs.next()) {
				String title = rs.getString("제목");
				String director = rs.getString("감독");
				String genre = rs.getString("장르");
				String story = rs.getString("줄거리");
				String country = rs.getString("국가");
				int runningTime = rs.getInt("러닝_타임");

				Object data[] = { title, director, genre, story, country, runningTime };
				model4_1.addRow(data);
				
			}

		} catch (SQLException sqle) {
			System.out.println("SQLException : " + sqle);
		}

	}

	public void getTable4_2() {

		try {
			
			ResultSet rs = stmt.executeQuery(
					"SELECT 제목, 감독, 장르, 줄거리, 국가, 러닝_타임 FROM DB2021_영화 WHERE 제목 in (SELECT 제목 FROM DB2021_평가 WHERE 추천_관계 = '연인') and 감독 in (SELECT 감독 FROM DB2021_평가 WHERE 추천_관계 = '연인')");

			while (rs.next()) {
				String title = rs.getString("제목");
				String director = rs.getString("감독");
				String genre = rs.getString("장르");
				String story = rs.getString("줄거리");
				String country = rs.getString("국가");
				int runningTime = rs.getInt("러닝_타임");

				Object data[] = { title, director, genre, story, country, runningTime };
				model4_2.addRow(data);
				
			}

		} catch (SQLException sqle) {
			System.out.println("SQLException : " + sqle);
		}

	}

	public void getTable4_3() {

		try {
		
			ResultSet rs = stmt.executeQuery(
					"SELECT 제목, 감독, 장르, 줄거리, 국가, 러닝_타임 FROM DB2021_영화 WHERE 제목 in (SELECT 제목 FROM DB2021_평가 WHERE 추천_관계 = '가족') and 감독 in (SELECT 감독 FROM DB2021_평가 WHERE 추천_관계 = '가족')");

			while (rs.next()) {
				String title = rs.getString("제목");
				String director = rs.getString("감독");
				String genre = rs.getString("장르");
				String story = rs.getString("줄거리");
				String country = rs.getString("국가");
				int runningTime = rs.getInt("러닝_타임");

				Object data[] = { title, director, genre, story, country, runningTime };
				model4_3.addRow(data);
		
			}

		} catch (SQLException sqle) {
			System.out.println("SQLException : " + sqle);
		}

	}

	public void getTable4_4() {

		try {
		
			ResultSet rs = stmt.executeQuery(
					"SELECT 제목, 감독, 장르, 줄거리, 국가, 러닝_타임 FROM DB2021_영화 WHERE 제목 in (SELECT 제목 FROM DB2021_평가 WHERE 추천_관계 = '친구') and 감독 in (SELECT 감독 FROM DB2021_평가 WHERE 추천_관계 = '친구')");

			while (rs.next()) {
				String title = rs.getString("제목");
				String director = rs.getString("감독");
				String genre = rs.getString("장르");
				String story = rs.getString("줄거리");
				String country = rs.getString("국가");
				int runningTime = rs.getInt("러닝_타임");

				Object data[] = { title, director, genre, story, country, runningTime };
				model4_4.addRow(data);
				
			}

		} catch (SQLException sqle) {
			System.out.println("SQLException : " + sqle);
		}

	}

}