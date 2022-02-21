package DB2021Team10;

import java.awt.BorderLayout;
import java.awt.Font;
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

public class Rate_Movie {

	Connection conn;
	Statement stmt;

	// 테이블 타이틀,패널
	JLabel menu, l1; // <평가 작성 가능한 영화 목록>
	JPanel panel; // +JLabel 부착
	JPanel mainPanel;

	// JTable
	Object ob[][] = new Object[0][5]; // 데이터 표시(행X) 열만 나오게 설정
	DefaultTableModel model; // 데이터 저장부분
	JTable table;
	JScrollPane js;
	String str[] = { "제목", "감독", "학번", "평점", "평론" }; // 컬럼명

	Rate_Movie(Connection conn, Statement stmt) {
		this.conn = conn;
		this.stmt = stmt;

		// 가장 큰 패널
		mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

		// 테이블을 붙일 패널생성
		panel = new JPanel();
		panel.setBorder(new EmptyBorder(0, 40, 0, 40));
		panel.setLayout(new BorderLayout());

		// 각 테이블 이름을 레이블로 달기
		Font f1 = new Font("굴림", Font.BOLD, 20);

		menu = new JLabel("제목별", JLabel.CENTER);
		menu.setFont(f1);
		menu.setBorder(new EmptyBorder(40, 0, 0, 0));
		mainPanel.add(menu);

		l1 = new JLabel("<영화제목별 평가>", JLabel.CENTER);
		l1.setFont(f1);
		l1.setBorder(new EmptyBorder(40, 40, 20, 40));
		panel.add(l1, BorderLayout.NORTH);

		// JTable 생성, 배치
		model = new DefaultTableModel(ob, str); // 1)데이저 저장[][], 2)컬럼명
		table = new JTable(model); // table=new JTable(ob,str);
		js = new JScrollPane(table);
		js.setBorder(new EmptyBorder(0, 40, 0, 40)); // top, left, bottom, right
		panel.add(js, BorderLayout.CENTER);
		mainPanel.add(panel);

		getTable();

	}

	public void getTable() {

		try {

			ResultSet rs = stmt.executeQuery("SELECT 제목, 감독, 학번, 평점, 평론 FROM DB2021_평가 ORDER BY 제목");

			while (rs.next()) {
				String title = rs.getString("제목");
				String director = rs.getString("감독");
				String id = rs.getString("학번");
				Float score = rs.getFloat("평점");
				String review = rs.getString("평론");

				Object data[] = { title, director, id, score, review };
				model.addRow(data);

			}

		} catch (SQLException sqle) {
			System.out.println("SQLException : " + sqle);
		}

	}

}
