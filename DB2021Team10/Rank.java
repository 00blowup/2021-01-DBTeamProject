package DB2021Team10;

import java.sql.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;

import java.awt.event.*;
import java.awt.*;

public class Rank extends JFrame {

	Connection conn;
	Statement stmt;

	// 테이블 타이틀,패널
	JLabel menu, l1, l2; // <랭킹(관객 수)>,<랭킹(평균 평점)>
	JPanel mainPanel, group, panel1, panel2; // +JLabel 부착

	// <랭킹(관객 수)> JTable
	Object ob1[][] = new Object[0][4]; // 데이터 표시(행X) 열만 나오게 설정
	DefaultTableModel model1; // 데이터 저장부분
	JTable table1;
	JScrollPane js1;
	String str1[] = { "랭킹", "제목", "감독", "관객_수" }; // 컬럼명

	// <랭킹(평균 평점)> JTable
	Object ob2[][] = new Object[0][4]; // 데이터 표시(행X) 열만 나오게 설정
	DefaultTableModel model2; // 데이터 저장부분
	JTable table2;
	JScrollPane js2;
	String str2[] = { "랭킹", "제목", "감독", "평균_평점" }; // 컬럼명

	Rank(Connection conn, Statement stmt) {
		this.conn = conn;
		this.stmt = stmt;

		// 가장 큰 패널
		mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

		// 테이블 가로로 정렬
		group = new JPanel();
		group.setLayout(new GridLayout(1, 2));

		// 테이블을 붙일 패널생성
		panel1 = new JPanel();
		panel1.setBorder(new EmptyBorder(20, 40, 20, 40));
		panel1.setLayout(new BorderLayout());
		panel2 = new JPanel();
		panel2.setBorder(new EmptyBorder(20, 40, 20, 40));
		panel2.setLayout(new BorderLayout());

		// 각 테이블 이름을 레이블로 달기
		Font f1 = new Font("굴림", Font.BOLD, 20);

		menu = new JLabel("랭킹", JLabel.CENTER);
		menu.setFont(f1);
		menu.setBorder(new EmptyBorder(40, 0, 0, 0));
		mainPanel.add(menu);

		l1 = new JLabel("<랭킹(관객 수)>", JLabel.CENTER);
		l1.setFont(f1);
		l1.setBorder(new EmptyBorder(40, 40, 20, 40));
		panel1.add(l1, BorderLayout.NORTH);

		l2 = new JLabel("<랭킹(평균 평점)>", JLabel.CENTER);
		l2.setFont(f1);
		l2.setBorder(new EmptyBorder(40, 40, 20, 40));
		panel2.add(l2, BorderLayout.NORTH);

		// <랭킹(관객 수)> JTable 생성, 배치
		model1 = new DefaultTableModel(ob1, str1); // 1)데이저 저장[][], 2)컬럼명
		table1 = new JTable(model1); // table=new JTable(ob,str);
		js1 = new JScrollPane(table1);
		js1.setBorder(new EmptyBorder(0, 40, 40, 40));
		panel1.add(js1, BorderLayout.CENTER);
		group.add(panel1);

		// <랭킹(평균 평점)> JTable 생성, 배치
		model2 = new DefaultTableModel(ob2, str2); // 1)데이저 저장[][], 2)컬럼명
		table2 = new JTable(model2); // table=new JTable(ob,str);
		js2 = new JScrollPane(table2);
		js2.setBorder(new EmptyBorder(0, 40, 40, 40));
		panel2.add(js2, BorderLayout.CENTER);
		group.add(panel2);

		mainPanel.add(group);

		getTable1();
		getTable2();

	}

	public void getTable1() {

		try {

			ResultSet rs = stmt
					.executeQuery("SELECT rank() over(order by 관객_수 desc) as 랭킹, 제목, 감독, 관객_수 FROM DB2021_수익 ORDER BY 랭킹");

			while (rs.next()) {
				int ranking = rs.getInt("랭킹");
				String title = rs.getString("제목");
				String director = rs.getString("감독");
				long num = rs.getLong("관객_수");

				Object data1[] = { ranking, title, director, num };
				model1.addRow(data1);
				// System.out.print(rs.getInt("랭킹") + "\t" + rs.getString("제목") + "\t" +
				// rs.getString("감독") + "\t" + rs.getLong("관객_수") + "\n");
			}

		} catch (SQLException sqle) {
			System.out.println("SQLException : " + sqle);
		}

	}

	public void getTable2() {

		try {

			ResultSet rs = stmt.executeQuery(
					"WITH 평균평점 AS (SELECT 제목, 감독, (네이버*10+로튼토마토+왓챠*20)/3 as SCORE_RATE FROM DB2021_평점) SELECT rank() over(order by SCORE_RATE desc) as 랭킹, 제목, 감독, SCORE_RATE FROM 평균평점 ORDER BY 랭킹");

			while (rs.next()) {
				int ranking = rs.getInt("랭킹");
				String title = rs.getString("제목");
				String director = rs.getString("감독");
				long rate = rs.getLong("SCORE_RATE");

				Object data2[] = { ranking, title, director, rate };
				model2.addRow(data2);

			}

		} catch (SQLException sqle) {
			System.out.println("SQLException : " + sqle);
		}

	}

}
