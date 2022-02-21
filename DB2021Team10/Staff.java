
package DB2021Team10;

import java.awt.BorderLayout;
import java.awt.Font;
import java.sql.Connection;
import java.sql.PreparedStatement;
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

public class Staff {

	Connection conn;
	Statement stmt;

	// 테이블 타이틀,패널
	JLabel menu, l1, l2; // <부원 리스트(부원용)>, <부원 리스트(회장용)>
	JPanel mainPanel, panel1, panel2; // +JLabel 부착

	// <부원 리스트(부원용)> JTable
	Object ob1[][] = new Object[0][2]; // 데이터 표시(행X) 열만 나오게 설정
	DefaultTableModel model1; // 데이터 저장부분
	JTable table1;
	JScrollPane js1;
	String str1[] = { "학번", "이름" }; // 컬럼명

	// <부원 리스트(회장용)> JTable
	Object ob2[][] = new Object[0][5]; // 데이터 표시(행X) 열만 나오게 설정
	DefaultTableModel model2; // 데이터 저장부분
	JTable table2;
	JScrollPane js2;
	String str2[] = { "학번", "이름", "비밀번호", "기수", "직책" }; // 컬럼명

	Staff(Connection conn, Statement stmt) {
		this.conn = conn;
		this.stmt = stmt;

		// 가장 큰 패널
		mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

		// 테이블을 붙일 패널생성
		panel1 = new JPanel();
		panel1.setBorder(new EmptyBorder(20, 40, 20, 40));
		panel1.setLayout(new BorderLayout());
		panel2 = new JPanel();
		panel2.setBorder(new EmptyBorder(20, 40, 20, 40));
		panel2.setLayout(new BorderLayout());

		// 각 테이블 이름을 레이블로 달기
		Font f1 = new Font("굴림", Font.BOLD, 20);

		menu = new JLabel("부원 관리 홈", JLabel.CENTER);
		menu.setFont(f1);
		menu.setBorder(new EmptyBorder(40, 0, 0, 0));
		mainPanel.add(menu);

		l1 = new JLabel("<부원 리스트(부원용)>", JLabel.CENTER);
		l1.setFont(f1);
		l1.setBorder(new EmptyBorder(40, 40, 20, 40));
		panel1.add(l1, BorderLayout.NORTH);

		l2 = new JLabel("<부원 리스트(임원용)>", JLabel.CENTER);
		l2.setFont(f1);
		l2.setBorder(new EmptyBorder(40, 40, 20, 40));
		panel2.add(l2, BorderLayout.NORTH);

		// <부원 리스트(부원용)> JTable 생성, 배치
		model1 = new DefaultTableModel(ob1, str1); // 1)데이저 저장[][], 2)컬럼명
		table1 = new JTable(model1); // table=new JTable(ob,str);
		js1 = new JScrollPane(table1);
		js1.setBorder(new EmptyBorder(0, 40, 40, 40));
		panel1.add(js1, BorderLayout.CENTER);

		// <부원 리스트(임원용)> JTable 생성, 배치
		model2 = new DefaultTableModel(ob2, str2); // 1)데이저 저장[][], 2)컬럼명
		table2 = new JTable(model2); // table=new JTable(ob,str);
		js2 = new JScrollPane(table2);
		js2.setBorder(new EmptyBorder(0, 40, 40, 40));
		panel2.add(js2, BorderLayout.CENTER);

		getRole();
		getTable1();
		getTable2();
	}

	public void getTable1() {

		try {
			
			ResultSet rs = stmt.executeQuery("SELECT * FROM DB2021_Member");

			while (rs.next()) {
				String id = rs.getString("학번");
				String name = rs.getString("이름");

				Object data[] = { id, name };
				model1.addRow(data);

			}

		} catch (SQLException sqle) {
			System.out.println("SQLException : " + sqle);
		}

	}

	public void getTable2() {

		try {
			
			ResultSet rs = stmt.executeQuery("SELECT * FROM DB2021_Chairman");

			while (rs.next()) {
				String id = rs.getString("학번");
				String name = rs.getString("이름");
				String pwd = rs.getString("비밀번호");
				int inception = rs.getInt("기수");
				String role = rs.getString("직책");

				Object data[] = { id, name, pwd, inception, role };
				model2.addRow(data);

			}

		} catch (SQLException sqle) {
			System.out.println("SQLException : " + sqle);
		}

	}

	// 직책에 따라 다른 테이블을 페널에 붙임
	public void getRole() {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String role;

		// 로그인 정보
		String ID = Login.myID;

		try {
			pstmt = conn.prepareStatement("SELECT 직책 FROM DB2021_부원 WHERE 학번 = ? ");
			pstmt.setString(1, ID);
			rs = pstmt.executeQuery(); // 쿼리 실행

			if (rs.next()) {
				role = rs.getString("직책");
				if (role.equals("회장") || role.equals("부회장"))
					mainPanel.add(panel2);

				else if (role.equals("부원") || role.equals("비활동"))
					mainPanel.add(panel1);

			}
		} catch (SQLException sqle) {
			System.out.println("SQLException : " + sqle);
		}
	}

}
