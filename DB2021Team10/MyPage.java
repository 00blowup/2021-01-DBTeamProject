package DB2021Team10;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class MyPage {

	Connection conn;
	Statement stmt;

	// 테이블 타이틀,패널
	JLabel menu, l; // <랭킹(관객 수)>,<랭킹(평균 평점)>
	JPanel mainPanel, btnpanel1, btnpanel2, panel; // +JLabel 부착

	// 평가 버튼 2종, 계정 버튼 2종
	JButton btnEdit, btnDelete;
	JButton btnEditAcct, btnDeleteAcct;

	// <나의 평가> JTable
	Object ob[][] = new Object[0][9]; // 데이터 표시(행X) 열만 나오게 설정
	DefaultTableModel model; // 데이터 저장부분
	JTable table;
	JScrollPane js;
	String str[] = { "제목", "감독", "평점", "평론", "비슷한_영화_제목", "비슷한_영화의_감독", "추천_감정", "추천_날씨", "추천_관계" }; // 컬럼명

	MyPage(Connection conn, Statement stmt) {
		this.conn = conn;
		this.stmt = stmt;

		// 가장 큰 패널
		mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

		// 테이블을 붙일 패널생성
		panel = new JPanel();
		panel.setBorder(new EmptyBorder(20, 10, 20, 10));
		panel.setLayout(new BorderLayout());

		// 각 테이블 이름을 레이블로 달기
		Font f1 = new Font("굴림", Font.BOLD, 20);

		menu = new JLabel("마이 페이지", JLabel.CENTER);
		menu.setFont(f1);
		menu.setBorder(new EmptyBorder(40, 0, 0, 0));
		mainPanel.add(menu);

		l = new JLabel("<나의 평가>", JLabel.CENTER);
		l.setFont(f1);
		l.setBorder(new EmptyBorder(40, 0, 40, 0));
		panel.add(l, BorderLayout.NORTH);

		// <나의 평가> JTable 생성, 배치
		model = new DefaultTableModel(ob, str); // 1)데이저 저장[][], 2)컬럼명
		table = new JTable(model); // table=new JTable(ob,str);
		js = new JScrollPane(table);
		js.setPreferredSize(new Dimension(1100, 350));
		js.setBorder(new EmptyBorder(0, 0, 0, 0));
		panel.add(js, BorderLayout.CENTER);
		mainPanel.add(panel);

		btnpanel1 = new JPanel();
		btnpanel1.setBorder(new EmptyBorder(0, 40, 0, 40));

		// 내 평가 수정 버튼 배치
		btnEdit = new JButton("수정");
		btnpanel1.add(btnEdit);
		btnEdit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new Rate_Edit(conn, stmt);
			}
		});

		// 내 평가 삭제 버튼 배치
		btnDelete = new JButton("삭제");
		btnpanel1.add(btnDelete);
		btnDelete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new Rate_Delete(conn, stmt);
			}
		});

		mainPanel.add(btnpanel1);

		btnpanel2 = new JPanel();
		btnpanel2.setBorder(new EmptyBorder(50, 40, 0, 40));

		// 계정 정보 수정 버튼 배치
		btnEditAcct = new JButton("내 정보 수정");
		btnpanel2.add(btnEditAcct);
		btnEditAcct.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new Account_Edit(conn, stmt);
			}
		});

		// 내 계정 삭제 버튼 배치
		btnDeleteAcct = new JButton("계정 삭제");
		btnpanel2.add(btnDeleteAcct);
		btnDeleteAcct.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new Account_Delete(conn, stmt);
			}
		});

		mainPanel.add(btnpanel2);

		getTable();
	}

	public void getTable() {
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		String ID = Login.myID;

		try {
			// System.out.println("<나의 평가>");
			pstmt = conn.prepareStatement(
					"SELECT 제목, 감독, 평점, 평론, 비슷한_영화_제목, 비슷한_영화의_감독, 추천_감정, 추천_날씨, 추천_관계 FROM DB2021_평가 WHERE 학번 = ? ");
			pstmt.setString(1, ID);
			rs = pstmt.executeQuery(); // 쿼리 실행

			while (rs.next()) {
				String title = rs.getString("제목");
				String director = rs.getString("감독");
				float rate = rs.getFloat("평점");
				String review = rs.getString("평론");
				String simTitle = rs.getString("비슷한_영화_제목");
				String simDirector = rs.getString("비슷한_영화의_감독");
				String mood = rs.getString("추천_감정");
				String weather = rs.getString("추천_날씨");
				String withWho = rs.getString("추천_관계");

				Object data[] = { title, director, rate, review, simTitle, simDirector, mood, weather, withWho };
				model.addRow(data);
			}

		} catch (SQLException sqle) {
			System.out.println("SQLException : " + sqle);
		}

	}

}
