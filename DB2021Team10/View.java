package DB2021Team10;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import java.util.*;

public class View {

	Connection conn;
	Statement stmt;

	// 테이블 타이틀,패널
	JLabel menu, l1, l2_1, l2_2, l2_3, l2_4, l3;
	JPanel mainPanel, panel1, panel2_1, panel2_2, panel2_3, panel2_4, panel3; // +JLabel 부착
	JPanel group;

	// <현재 상영 중인 영화> JTable
	Object ob[][] = new Object[0][5]; // 데이터 표시(행X) 열만 나오게 설정
	DefaultTableModel model1; // 데이터 저장부분
	JTable table1;
	JScrollPane js1;
	String str[] = { "제목", "감독", "장르", "개봉_날짜", "국가" }; // 컬럼명

	// <넷플릭스에서 상영 중인 영화> JTable
	DefaultTableModel model2_1; // 데이터 저장부분
	JTable table2_1;
	JScrollPane js2_1;

	// <왓챠에서 상영 중인 영화> JTable
	DefaultTableModel model2_2; // 데이터 저장부분
	JTable table2_2;
	JScrollPane js2_2;

	// <웨이브에서 상영 중인 영화> JTable
	DefaultTableModel model2_3; // 데이터 저장부분
	JTable table2_3;
	JScrollPane js2_3;

	// <티빙에서 상영 중인 영화> JTable
	DefaultTableModel model2_4; // 데이터 저장부분
	JTable table2_4;
	JScrollPane js2_4;

	// <DVD를 제공해주는 영화> JTable
	DefaultTableModel model3; // 데이터 저장부분
	JTable table3;
	JScrollPane js3;

	View(Connection conn, Statement stmt) {
		this.conn = conn;
		this.stmt = stmt;

		// 가장 큰 패널
		mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

		// 스트리밍 서비스 묶음 패널
		group = new JPanel();
		group.setLayout(new GridLayout(2, 2));

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

		panel3 = new JPanel();
		panel3.setBorder(new EmptyBorder(20, 40, 20, 40));
		panel3.setLayout(new BorderLayout());

		// 각 테이블 이름을 레이블로 달기
		Font f1 = new Font("굴림", Font.BOLD, 20);

		menu = new JLabel("관람 정보", JLabel.CENTER);
		menu.setFont(f1);
		menu.setBorder(new EmptyBorder(40, 0, 0, 0));
		mainPanel.add(menu);

		l1 = new JLabel("<현재 상영 중인 영화>", JLabel.CENTER);
		l1.setFont(f1);
		l1.setBorder(new EmptyBorder(40, 40, 20, 40));
		panel1.add(l1, BorderLayout.NORTH);

		l2_1 = new JLabel("<넷플릭스에서 상영 중인 영화>", JLabel.CENTER);
		l2_1.setFont(f1);
		l2_1.setBorder(new EmptyBorder(40, 40, 20, 40));
		panel2_1.add(l2_1, BorderLayout.NORTH);

		l2_2 = new JLabel("<왓챠에서 상영 중인 영화>", JLabel.CENTER);
		l2_2.setFont(f1);
		l2_2.setBorder(new EmptyBorder(40, 40, 20, 40));
		panel2_2.add(l2_2, BorderLayout.NORTH);

		l2_3 = new JLabel("<웨이브에서 상영 중인 영화>", JLabel.CENTER);
		l2_3.setFont(f1);
		l2_3.setBorder(new EmptyBorder(40, 40, 20, 40));
		panel2_3.add(l2_3, BorderLayout.NORTH);

		l2_4 = new JLabel("<티빙에서 상영 중인 영화>", JLabel.CENTER);
		l2_4.setFont(f1);
		l2_4.setBorder(new EmptyBorder(40, 40, 20, 40));
		panel2_4.add(l2_4, BorderLayout.NORTH);

		l3 = new JLabel("<DVD를 제공해주는 영화>", JLabel.CENTER);
		l3.setFont(f1);
		l3.setBorder(new EmptyBorder(40, 40, 20, 40));
		panel3.add(l3, BorderLayout.NORTH);

		// <현재 상영 중인 영화> JTable 생성, 배치
		model1 = new DefaultTableModel(ob, str); // 1)데이저 저장[][], 2)컬럼명
		table1 = new JTable(model1); // table=new JTable(ob,str);
		js1 = new JScrollPane(table1);
		js1.setBorder(new EmptyBorder(0, 40, 40, 40));
		panel1.add(js1, BorderLayout.CENTER);
		mainPanel.add(panel1);

		// <넷플릭스에서 상영 중인 영화> JTable 생성, 배치
		model2_1 = new DefaultTableModel(ob, str); // 1)데이저 저장[][], 2)컬럼명
		table2_1 = new JTable(model2_1); // table=new JTable(ob,str);
		js2_1 = new JScrollPane(table2_1);
		js2_1.setBorder(new EmptyBorder(0, 40, 40, 40));
		panel2_1.add(js2_1, BorderLayout.CENTER);
		group.add(panel2_1);

		// <왓챠에서 상영 중인 영화> JTable 생성, 배치
		model2_2 = new DefaultTableModel(ob, str); // 1)데이저 저장[][], 2)컬럼명
		table2_2 = new JTable(model2_2); // table=new JTable(ob,str);
		js2_2 = new JScrollPane(table2_2);
		js2_2.setBorder(new EmptyBorder(0, 40, 40, 40));
		panel2_2.add(js2_2, BorderLayout.CENTER);
		group.add(panel2_2);

		// <웨이브에서 상영 중인 영화> JTable 생성, 배치
		model2_3 = new DefaultTableModel(ob, str); // 1)데이저 저장[][], 2)컬럼명
		table2_3 = new JTable(model2_3); // table=new JTable(ob,str);
		js2_3 = new JScrollPane(table2_3);
		js2_3.setBorder(new EmptyBorder(0, 40, 40, 40));
		panel2_3.add(js2_3, BorderLayout.CENTER);
		group.add(panel2_3);

		// <티빙에서 상영 중인 영화> JTable 생성, 배치
		model2_4 = new DefaultTableModel(ob, str); // 1)데이저 저장[][], 2)컬럼명
		table2_4 = new JTable(model2_4); // table=new JTable(ob,str);
		js2_4 = new JScrollPane(table2_4);
		js2_4.setBorder(new EmptyBorder(0, 40, 40, 40));
		panel2_4.add(js2_4, BorderLayout.CENTER);
		group.add(panel2_4);

		mainPanel.add(group);

		// <DVD를 제공해주는 영화> JTable 생성, 배치
		model3 = new DefaultTableModel(ob, str); // 1)데이저 저장[][], 2)컬럼명
		table3 = new JTable(model3); // table=new JTable(ob,str);
		js3 = new JScrollPane(table3);
		js3.setBorder(new EmptyBorder(0, 40, 40, 40));
		panel3.add(js3, BorderLayout.CENTER);
		mainPanel.add(panel3);

		getTable1();

		getTable2_1();
		getTable2_2();
		getTable2_3();
		getTable2_4();

		getTable3();

	}

	public void getTable1() {
		try {
	
			ResultSet rs = stmt.executeQuery("SELECT 제목, 감독, 장르, 개봉_날짜, 국가 FROM DB2021_영화 WHERE 현재_상영 = 'true'");

			while (rs.next()) {
				String title = rs.getString("제목");
				String director = rs.getString("감독");
				String genre = rs.getString("장르");
				Date date = rs.getDate("개봉_날짜");
				String country = rs.getString("국가");

				Object data[] = { title, director, genre, date, country };
				model1.addRow(data);
				
			}

		} catch (SQLException sqle) {
			System.out.println("SQLException : " + sqle);
		}

	}

	public void getTable2_1() {

		try {
			
			ResultSet rs = stmt.executeQuery(
					"SELECT 제목, 감독, 장르, 개봉_날짜, 국가 FROM DB2021_영화 WHERE 제목 in (SELECT 제목 FROM DB2021_스트리밍 WHERE 서비스 = '넷플릭스') and 감독 in (SELECT 감독 FROM DB2021_스트리밍 WHERE 서비스 = '넷플릭스')");

			while (rs.next()) {
				String title = rs.getString("제목");
				String director = rs.getString("감독");
				String genre = rs.getString("장르");
				Date date = rs.getDate("개봉_날짜");
				String country = rs.getString("국가");

				Object data[] = { title, director, genre, date, country };
				model2_1.addRow(data);
				
			}

		} catch (SQLException sqle) {
			System.out.println("SQLException : " + sqle);
		}

	}

	public void getTable2_2() {

		try {
	
			ResultSet rs = stmt.executeQuery(
					"SELECT 제목, 감독, 장르, 개봉_날짜, 국가 FROM DB2021_영화 WHERE 제목 in (SELECT 제목 FROM DB2021_스트리밍 WHERE 서비스 = '왓챠') and 감독 in (SELECT 감독 FROM DB2021_스트리밍 WHERE 서비스 = '왓챠')");

			while (rs.next()) {
				String title = rs.getString("제목");
				String director = rs.getString("감독");
				String genre = rs.getString("장르");
				Date date = rs.getDate("개봉_날짜");
				String country = rs.getString("국가");

				Object data[] = { title, director, genre, date, country };
				model2_2.addRow(data);
				
			}

		} catch (SQLException sqle) {
			System.out.println("SQLException : " + sqle);
		}

	}

	public void getTable2_3() {

		try {
			
			ResultSet rs = stmt.executeQuery(
					"SELECT 제목, 감독, 장르, 개봉_날짜, 국가 FROM DB2021_영화 WHERE 제목 in (SELECT 제목 FROM DB2021_스트리밍 WHERE 서비스 = '웨이브') and 감독 in (SELECT 감독 FROM DB2021_스트리밍 WHERE 서비스 = '웨이브')");

			while (rs.next()) {
				String title = rs.getString("제목");
				String director = rs.getString("감독");
				String genre = rs.getString("장르");
				Date date = rs.getDate("개봉_날짜");
				String country = rs.getString("국가");

				Object data[] = { title, director, genre, date, country };
				model2_3.addRow(data);
				
			}

		} catch (SQLException sqle) {
			System.out.println("SQLException : " + sqle);
		}

	}

	public void getTable2_4() {

		try {
			
			ResultSet rs = stmt.executeQuery(
					"SELECT 제목, 감독, 장르, 개봉_날짜, 국가 FROM DB2021_영화 WHERE 제목 in (SELECT 제목 FROM DB2021_스트리밍 WHERE 서비스 = '티빙') and 감독 in (SELECT 감독 FROM DB2021_스트리밍 WHERE 서비스 = '티빙')");

			while (rs.next()) {
				String title = rs.getString("제목");
				String director = rs.getString("감독");
				String genre = rs.getString("장르");
				Date date = rs.getDate("개봉_날짜");
				String country = rs.getString("국가");

				Object data[] = { title, director, genre, date, country };
				model2_4.addRow(data);
			
			}

		} catch (SQLException sqle) {
			System.out.println("SQLException : " + sqle);
		}

	}

	public void getTable3() {

		try {
			
			ResultSet rs = stmt.executeQuery(
					"SELECT 제목, 감독, 장르, 개봉_날짜, 국가 FROM DB2021_영화 WHERE 제목 in (SELECT 제목 FROM DB2021_유통 WHERE DVD_제공 = 'true') and 감독 in (SELECT 감독 FROM DB2021_유통 WHERE DVD_제공 = 'true')");

			while (rs.next()) {
				String title = rs.getString("제목");
				String director = rs.getString("감독");
				String genre = rs.getString("장르");
				Date date = rs.getDate("개봉_날짜");
				String country = rs.getString("국가");

				Object data[] = { title, director, genre, date, country };
				model3.addRow(data);
				
			}

		} catch (SQLException sqle) {
			System.out.println("SQLException : " + sqle);
		}

	}

}
