package DB2021Team10;

import java.sql.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;

import java.awt.event.*;
import java.awt.*;

public class Movie extends JFrame {
	// 검색을 위한 변수 2개
	String mSelected = "--"; // 검색기준(초기값은 "--")
	String mKeyword; // 검색어

	// 버튼 3종
	JButton btnAdd;
	JButton btnEdit;
	JButton btnDelete;

	// 테이블 타이틀,패널
	JLabel menu, l1; // <기본 영화 목록>
	JPanel panel; // +JLabel 부착
	JPanel mainPanel;
	JPanel btnpanel;

	// JTable
	Object ob[][] = new Object[0][2]; // 데이터 표시(행X) 열만 나오게 설정
	DefaultTableModel model; // 데이터 저장부분
	JTable table;
	JScrollPane js;
	String str[] = { "제목", "감독" }; // 컬럼명

	Connection conn;
	Statement stmt;

	Movie(Connection conn, Statement stmt) {
		this.conn = conn;
		this.stmt = stmt;

		mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

		panel = new JPanel();
		panel.setBorder(new EmptyBorder(0, 40, 0, 40));
		panel.setLayout(new BorderLayout());

		// 1) 문자열, 2)위치(left,Center,right)
		Font f1 = new Font("굴림", Font.BOLD, 20);

		menu = new JLabel("영화 정보 홈", JLabel.CENTER);
		menu.setFont(f1);
		menu.setBorder(new EmptyBorder(40, 0, 0, 0));
		mainPanel.add(menu);

		l1 = new JLabel("<기본 영화 목록>", JLabel.CENTER);
		l1.setBorder(new EmptyBorder(40, 40, 20, 40));
		l1.setFont(f1);

		panel.add(l1, BorderLayout.NORTH);

		// JTable 가운데 배치
		model = new DefaultTableModel(ob, str); // 1)데이저 저장[][], 2)컬럼명
		table = new JTable(model); // table=new JTable(ob,str);
		js = new JScrollPane(table);
		js.setBorder(new EmptyBorder(0, 40, 0, 40));
		panel.add(js, BorderLayout.CENTER);
		mainPanel.add(panel);

		btnpanel = new JPanel();
		btnpanel.setBorder(new EmptyBorder(0, 40, 200, 40));

		// 버튼 배치
		btnAdd = new JButton("추가");
		btnpanel.add(btnAdd);
		btnAdd.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new Movie_Insert(conn, stmt);
			}
		});

		btnEdit = new JButton("수정");
		btnpanel.add(btnEdit);
		btnEdit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new Movie_Edit(conn, stmt);
			}
		});

		btnDelete = new JButton("삭제");
		btnpanel.add(btnDelete);
		btnDelete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new Movie_Delete(conn, stmt);
			}
		});

		// 검색창 추가.
		// South 구역에 검색창을 추가하기 위해, South 구역에 있는 btnpanel에 끼워넣음
		addSearchArea(btnpanel);

		mainPanel.add(btnpanel);

		getTable();

	}

	public void getTable() {
		try {
			// System.out.println("<기본 영화 목록>");
			ResultSet rs = stmt.executeQuery("SELECT * FROM DB2021_BasicList");

			while (rs.next()) {
				String title = rs.getString("제목");
				String director = rs.getString("감독");

				Object data[] = { title, director };
				model.addRow(data);

			}

		} catch (SQLException sqle) {
			System.out.println("SQLException : " + sqle);
		}

	}

	// 검색창을 추가하는 메소드
	void addSearchArea(JPanel panel) {

		// 객체 정의
		JLabel searchLable = new JLabel("검색"); // "검색" 텍스트
		String[] select = { "--", "제목", "감독" };
		JComboBox<String> searchSelect = new JComboBox<String>(select); // 검색기준 버튼
		searchSelect.addActionListener(new ActionListener() {
			// 콤보박스에서 선택을 실행했을 경우 발생하는 액션 정의
			public void actionPerformed(ActionEvent e) {
				mSelected = searchSelect.getSelectedItem().toString(); // 선택된 검색기준을 mSelected 변수에 저장
			}
		});
		JTextField searchField = new JTextField(50); // 검색창 텍스트필드
		JButton searchButton = new JButton("Search"); // 검색버튼
		searchButton.addActionListener(new ActionListener() { // Search버튼 액션리스너
			// Search 버튼을 눌렀을 경우 발생하는 액션 정의
			public void actionPerformed(ActionEvent e) {
				mKeyword = searchField.getText(); // 검색창에 입력된 값을 mKeyword 변수에 저장
				printMstable(); // 검색결과 창을 새로 띄우고 검색결과 테이블을 출력
			}

		});

		// 화면에 검색 관련 객체들을 add
		panel.add(searchLable);
		panel.add(searchSelect);
		panel.add(searchField);
		panel.add(searchButton);

	}

	// 검색 결과 출력 함수
	public void printMstable() {
		
		// 검색결과를 출력할 새 창 설정
		JFrame searchWindow = new JFrame();
		searchWindow.setTitle("검색결과");
		searchWindow.setSize(1200, 800);
		searchWindow.setVisible(true);
		searchWindow.setLayout(new BorderLayout());

		// 검색결과를 저장할 테이블
		Object msheader[] = { "제목", "감독", "장르", "줄거리", "대표배우", "개봉날짜", "국가", "러닝타임", "현재상영여부", "DVD유무", "관객수(명)" }; // 항목
																													// 이름
		Object msob[][] = new Object[0][11];
		DefaultTableModel msmodel = new DefaultTableModel(msob, msheader);
		JTable mstable = new JTable(msmodel);
		JScrollPane msjs;

		if (mSelected == "--") {
			searchWindow.setTitle("검색 실패");
			searchWindow.setSize(400, 200); // 창 크기조절
			JLabel mstext = new JLabel("검색기준을 선택해주십시오", JLabel.CENTER); // 경고메시지
			Font msf = new Font("굴림", Font.BOLD, 15);
			mstext.setFont(msf);
			searchWindow.add("Center", mstext); // 경고메시지 출력

		} else {
			
			// 검색결과 값 가져오기
			ResultSet msr1 = movieSearch();

			// 가져온 결과값 출력
			try {
				while (msr1.next()) {

					// movieSearch 함수에서 받아온 결과값들을 각각의 변수에 저장
					String mstitle = msr1.getString("제목");
					String msdirector = msr1.getString("감독");
					String msgenre = msr1.getString("장르");
					String msstory = msr1.getString("줄거리");
					String msactor = msr1.getString("대표_배우");
					String msdate = msr1.getString("개봉_날짜");
					String mscountry = msr1.getString("국가");
					int mstime = msr1.getInt("러닝_타임");
					int msnow = msr1.getInt("현재_상영");
					int msdvd = msr1.getInt("DVD_제공");
					int msaudience = msr1.getInt("관객_수");
					
					// 각 변수들을 오브젝트 배열에 저장
					Object mscontents[] = { mstitle, msdirector, msgenre, msstory, msactor, msdate, mscountry, mstime,
							msnow, msdvd, msaudience };
					
					// 오브젝트 배열을 모델에 추가
					msmodel.addRow(mscontents);

					// 검색결과 창에 출력될 객체들
					JLabel mstext = new JLabel("<영화정보 검색결과>", JLabel.CENTER);
					Font msf = new Font("굴림", Font.BOLD, 20);
					mstext.setFont(msf);
					JPanel resultPanel = new JPanel();
					msjs = new JScrollPane(mstable);
					mstable.setPreferredSize(new Dimension(1100, 700));
					msjs.setPreferredSize(new Dimension(1100, 700));
					mstable.getColumn("제목").setPreferredWidth(200);
					mstable.getColumn("국가").setPreferredWidth(50);
					mstable.getColumn("개봉날짜").setPreferredWidth(100);
					mstable.getColumn("줄거리").setPreferredWidth(300);

					// 객체들 붙이기
					searchWindow.add("North", mstext);
					resultPanel.add(msjs);
					searchWindow.add("Center", resultPanel);

				}
			} catch (SQLException sqle) {
				System.out.println("SQLException : " + sqle);
			}

		}

	}

	// 영화 검색을 수행하여 ResultSet을 리턴하는 함수
	public ResultSet movieSearch() {

		ResultSet msr1 = null;

		try {

			// 검색기준이 "제목"인 경우
			if (mSelected == "제목") {
				PreparedStatement mspstmt = conn.prepareStatement(
						"select 제목, 감독, 장르, 줄거리, 대표_배우, 개봉_날짜, 국가, 러닝_타임, 현재_상영, DVD_제공, 관객_수		from DB2021_영화 natural left outer join DB2021_유통 natural left outer join DB2021_수익 	where 제목 = ?");
				mspstmt.setString(1, mKeyword);
				msr1 = mspstmt.executeQuery();

				// 검색기준이 "감독"인 경우
			} else if (mSelected == "감독") {
				PreparedStatement mspstmt = conn.prepareStatement(
						"select 제목, 감독, 장르, 줄거리, 대표_배우, 개봉_날짜, 국가, 러닝_타임, 현재_상영, DVD_제공, 관객_수		from DB2021_영화 natural left outer join DB2021_유통 natural left outer join DB2021_수익 	where 감독 = ?");
				mspstmt.setString(1, mKeyword);
				msr1 = mspstmt.executeQuery();

			}

		} catch (SQLException sqle) {
			System.out.println("SQLException : " + sqle);
		}
		return msr1;

	}

}
