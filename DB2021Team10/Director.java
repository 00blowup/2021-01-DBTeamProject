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

public class Director {

	Connection conn;
	Statement stmt;

	// 테이블 타이틀,패널
	JLabel menu, l1, l2, l3, l4, l5;
	JPanel mainPanel, group, panel1, panel2, panel3, panel4, panel5;

	// <데뷔작과 대표작이 같은 감독> JTable
	Object ob1[][] = new Object[0][2]; // 데이터 표시(행X) 열만 나오게 설정
	DefaultTableModel model1; // 데이터 저장부분
	JTable table1;
	JScrollPane js1;
	String str1[] = { "감독", "작품_제목" }; // 컬럼명

	// <천 만 관객을 달성해본 감독> JTable
	Object ob[][] = new Object[0][1]; // 데이터 표시(행X) 열만 나오게 설정
	DefaultTableModel model2; // 데이터 저장부분
	JTable table2;
	JScrollPane js2;
	String str[] = { "감독" }; // 컬럼명

	// <작품 개수가 10개 이상인 감독> JTable
	DefaultTableModel model3; // 데이터 저장부분
	JTable table3;
	JScrollPane js3;

	// <젊은 감독> JTable
	DefaultTableModel model4; // 데이터 저장부분
	JTable table4;
	JScrollPane js4;

	// <신인 감독> JTable
	DefaultTableModel model5; // 데이터 저장부분
	JTable table5;
	JScrollPane js5;

	Director(Connection conn, Statement stmt) {
		this.conn = conn;
		this.stmt = stmt;

		// 가장 큰 패널
		mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

		// 테이블 가로로 정렬
		group = new JPanel();
		group.setLayout(new GridLayout(3, 2));

		// 테이블을 붙일 패널생성
		panel1 = new JPanel();
		panel1.setBorder(new EmptyBorder(20, 40, 20, 40));
		panel1.setLayout(new BorderLayout());

		panel2 = new JPanel();
		panel2.setBorder(new EmptyBorder(20, 40, 20, 40));
		panel2.setLayout(new BorderLayout());

		panel3 = new JPanel();
		panel3.setBorder(new EmptyBorder(20, 40, 20, 40));
		panel3.setLayout(new BorderLayout());

		panel4 = new JPanel();
		panel4.setBorder(new EmptyBorder(20, 40, 20, 40));
		panel4.setLayout(new BorderLayout());

		panel5 = new JPanel();
		panel5.setBorder(new EmptyBorder(20, 40, 20, 40));
		panel5.setLayout(new BorderLayout());

		// 각 테이블 이름을 레이블로 달기
		Font f1 = new Font("굴림", Font.BOLD, 20);

		menu = new JLabel("감독별", JLabel.CENTER);
		menu.setFont(f1);
		menu.setBorder(new EmptyBorder(40, 0, 0, 0));
		mainPanel.add(menu);

		l1 = new JLabel("<데뷔작과 대표작이 같은 감독>", JLabel.CENTER);
		l1.setFont(f1);
		l1.setBorder(new EmptyBorder(40, 40, 20, 40));
		panel1.add(l1, BorderLayout.NORTH);

		l2 = new JLabel("<천 만 관객을 달성해본 감독>", JLabel.CENTER);
		l2.setFont(f1);
		l2.setBorder(new EmptyBorder(40, 40, 20, 40));
		panel2.add(l2, BorderLayout.NORTH);

		l3 = new JLabel("<작품 개수가 10개 이상인 감독>", JLabel.CENTER);
		l3.setFont(f1);
		l3.setBorder(new EmptyBorder(40, 40, 20, 40));
		panel3.add(l3, BorderLayout.NORTH);

		l4 = new JLabel("<젊은 감독>", JLabel.CENTER);
		l4.setFont(f1);
		l4.setBorder(new EmptyBorder(40, 40, 20, 40));
		panel4.add(l4, BorderLayout.NORTH);

		l5 = new JLabel("<신인 감독>", JLabel.CENTER);
		l5.setFont(f1);
		l5.setBorder(new EmptyBorder(40, 40, 20, 40));
		panel5.add(l5, BorderLayout.NORTH);

		// <데뷔작과 대표작이 같은 감독> JTable 생성, 배치
		model1 = new DefaultTableModel(ob1, str1); // 1)데이저 저장[][], 2)컬럼명
		table1 = new JTable(model1); // table=new JTable(ob,str);
		js1 = new JScrollPane(table1);
		js1.setBorder(new EmptyBorder(0, 40, 40, 40));
		panel1.add(js1, BorderLayout.CENTER);
		group.add(panel1);

		// <천 만 관객을 달성해본 감독> JTable 생성, 배치
		model2 = new DefaultTableModel(ob, str); // 1)데이저 저장[][], 2)컬럼명
		table2 = new JTable(model2); // table=new JTable(ob,str);
		js2 = new JScrollPane(table2);
		js2.setBorder(new EmptyBorder(0, 40, 40, 40));
		panel2.add(js2, BorderLayout.CENTER);
		group.add(panel2);

		// <작품 개수가 10개 이상인 감독> JTable 생성, 배치
		model3 = new DefaultTableModel(ob, str); // 1)데이저 저장[][], 2)컬럼명
		table3 = new JTable(model3); // table=new JTable(ob,str);
		js3 = new JScrollPane(table3);
		js3.setBorder(new EmptyBorder(0, 40, 40, 40));
		panel3.add(js3, BorderLayout.CENTER);
		group.add(panel3);

		// <젊은 감독> JTable 생성, 배치
		model4 = new DefaultTableModel(ob, str); // 1)데이저 저장[][], 2)컬럼명
		table4 = new JTable(model4); // table=new JTable(ob,str);
		js4 = new JScrollPane(table4);
		js4.setBorder(new EmptyBorder(0, 40, 40, 40));
		panel4.add(js4, BorderLayout.CENTER);
		group.add(panel4);

		// <신인 감독> JTable 생성, 배치
		model5 = new DefaultTableModel(ob, str); // 1)데이저 저장[][], 2)컬럼명
		table5 = new JTable(model5); // table=new JTable(ob,str);
		js5 = new JScrollPane(table5);
		js5.setBorder(new EmptyBorder(0, 40, 40, 40));
		panel5.add(js5, BorderLayout.CENTER);
		group.add(panel5);

		mainPanel.add(group);

		getTable1();
		getTable2();
		getTable3();
		getTable4();
		getTable5();

	}

	public void getTable1() {

		try {
			
			ResultSet rs = stmt.executeQuery("SELECT 이름, 데뷔작 FROM DB2021_감독 WHERE 데뷔작 = 대표작");

			while (rs.next()) {
				String director = rs.getString("이름");
				String movie = rs.getString("데뷔작");

				Object data[] = { director, movie };
				model1.addRow(data);
			}

		} catch (SQLException sqle) {
			System.out.println("SQLException : " + sqle);
		}

	}

	public void getTable2() {

		try {
		
			ResultSet rs = stmt.executeQuery("SELECT DISTINCT(감독) FROM DB2021_수익 WHERE 관객_수 > 10000000");

			while (rs.next()) {
				String director = rs.getString("감독");

				Object data[] = { director };
				model2.addRow(data);
				
			}

		} catch (SQLException sqle) {
			System.out.println("SQLException : " + sqle);
		}

	}

	public void getTable3() {

		try {
			
			ResultSet rs = stmt.executeQuery("SELECT 이름 FROM DB2021_감독 WHERE 작품_개수 >= 10");

			while (rs.next()) {
				String director = rs.getString("이름");

				Object data[] = { director };
				model3.addRow(data);
			
			}

		} catch (SQLException sqle) {
			System.out.println("SQLException : " + sqle);
		}

	}

	public void getTable4() {

		try {
		
			ResultSet rs = stmt.executeQuery("SELECT 이름 FROM DB2021_감독 WHERE TIMESTAMPDIFF(year, 생년월일, NOW()) < 40");

			while (rs.next()) {
				String director = rs.getString("이름");

				Object data[] = { director };
				model4.addRow(data);
			
			}

		} catch (SQLException sqle) {
			System.out.println("SQLException : " + sqle);
		}

	}

	public void getTable5() {

		try {

			ResultSet rs = stmt.executeQuery("SELECT 이름 FROM DB2021_감독 WHERE YEAR(NOW()) - 데뷔_연도 <= 5");

			while (rs.next()) {
				String director = rs.getString("이름");

				Object data[] = { director };
				model5.addRow(data);

			}

		} catch (SQLException sqle) {
			System.out.println("SQLException : " + sqle);
		}

	}

}
