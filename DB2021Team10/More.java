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

public class More {

	Connection conn;
	Statement stmt;

	// 테이블 타이틀,패널
	JLabel menu, l1, l2; // <러닝타임이 긴 영화>,<러닝타임이 짧은 영화>
	JPanel mainPanel, group, panel1, panel2; // +JLabel 부착

	// <러닝타임이 긴 영화> JTable
	Object ob[][] = new Object[0][]; // 데이터 표시(행X) 열만 나오게 설정
	DefaultTableModel model1; // 데이터 저장부분
	JTable table1;
	JScrollPane js1;
	String str[] = { "제목", "감독", "러닝_타임" }; // 컬럼명

	// <러닝타임이 짧은 영화> JTable
	DefaultTableModel model2; // 데이터 저장부분
	JTable table2;
	JScrollPane js2;

	More(Connection conn, Statement stmt) {
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

		menu = new JLabel("기타", JLabel.CENTER);
		menu.setFont(f1);
		menu.setBorder(new EmptyBorder(40, 0, 0, 0));
		mainPanel.add(menu);

		l1 = new JLabel("<러닝타임이 긴 영화>", JLabel.CENTER);
		l1.setFont(f1);
		l1.setBorder(new EmptyBorder(40, 40, 20, 40));
		panel1.add(l1, BorderLayout.NORTH);

		l2 = new JLabel("<러닝타임이 짧은 영화>", JLabel.CENTER);
		l2.setFont(f1);
		l2.setBorder(new EmptyBorder(40, 40, 20, 40));
		panel2.add(l2, BorderLayout.NORTH);

		// <랭킹(관객 수)> JTable 생성, 배치
		model1 = new DefaultTableModel(ob, str); // 1)데이저 저장[][], 2)컬럼명
		table1 = new JTable(model1); // table=new JTable(ob,str);
		js1 = new JScrollPane(table1);
		js1.setBorder(new EmptyBorder(0, 40, 40, 40));
		panel1.add(js1, BorderLayout.CENTER);
		group.add(panel1);

		// <러닝타임이 짧은 영화> JTable 생성, 배치
		model2 = new DefaultTableModel(ob, str); // 1)데이저 저장[][], 2)컬럼명
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
			// System.out.println("<러닝타임이 긴 영화>");
			ResultSet rs = stmt.executeQuery("SELECT 제목, 감독, 러닝_타임 FROM DB2021_영화 WHERE 러닝_타임 >= 120");

			while (rs.next()) {
				String title = rs.getString("제목");
				String director = rs.getString("감독");
				int runningTime = rs.getInt("러닝_타임");

				Object data[] = { title, director, runningTime };
				model1.addRow(data);
				// System.out.print(rs.getString("제목") + '\t' + rs.getString("감독") + '\t' +
				// rs.getInt("러닝_타임") + '\n');
			}

		} catch (SQLException sqle) {
			System.out.println("SQLException : " + sqle);
		}

	}

	public void getTable2() {

		try {

			ResultSet rs = stmt.executeQuery("SELECT 제목, 감독, 러닝_타임 FROM DB2021_영화 WHERE 러닝_타임 <= 90");

			while (rs.next()) {
				String title = rs.getString("제목");
				String director = rs.getString("감독");
				int runningTime = rs.getInt("러닝_타임");

				Object data[] = { title, director, runningTime };
				model2.addRow(data);

			}

		} catch (SQLException sqle) {
			System.out.println("SQLException : " + sqle);
		}

	}

}
