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
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class Rate {
	// 검색을 위한 변수 2개
	String rSelected = "--"; // 검색기준(초기값은 "--")
	String rKeyword; // 검색어

	Connection conn;
	Statement stmt;

	// 버튼 3종
	JButton btnAdd;
	JButton btnEdit;
	JButton btnDelete;

	// 테이블 타이틀,패널
	JLabel menu, l1; // <평가 작성 가능한 영화 목록>
	JPanel panel; // +JLabel 부착
	JPanel mainPanel, btnpanel;

	// JTable
	Object ob[][] = new Object[0][2]; // 데이터 표시(행X) 열만 나오게 설정
	DefaultTableModel model; // 데이터 저장부분
	JTable table;
	JScrollPane js;
	String str[] = { "제목", "감독" }; // 컬럼명

	Rate(Connection conn, Statement stmt) {
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

		menu = new JLabel("영화 평가 홈", JLabel.CENTER);
		menu.setFont(f1);
		menu.setBorder(new EmptyBorder(40, 0, 0, 0));
		mainPanel.add(menu);

		l1 = new JLabel("<평가 작성 가능한 영화 목록>", JLabel.CENTER);
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

		btnpanel = new JPanel();
		btnpanel.setBorder(new EmptyBorder(0, 40, 200, 40));

		// 버튼 배치
		btnAdd = new JButton("평가 추가");
		btnpanel.add(btnAdd);
		btnAdd.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new Rate_Insert(conn, stmt);
			}
		});

		// 검색창 추가.
		// South 구역에 검색창을 추가하기 위해, South 구역에 있는 btnpanel에 끼워넣었습니다
		addSearchArea(btnpanel);

		mainPanel.add(btnpanel);

		getTable();
	}

	public void getTable() {
		try {

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
		String[] select = { "--", "제목", "학번", "단어" };
		JComboBox<String> searchSelect = new JComboBox<String>(select); // 검색기준 버튼
		searchSelect.addActionListener(new ActionListener() {
			// 콤보박스에서 선택을 실행했을 경우 발생하는 액션 정의
			public void actionPerformed(ActionEvent e) {
				rSelected = searchSelect.getSelectedItem().toString(); // 선택된 검색기준을 mSelected 변수에 저장
			}
		});
		JTextField searchField = new JTextField(50); // 검색창 텍스트필드
		JButton searchButton = new JButton("Search"); // 검색버튼
		searchButton.addActionListener(new ActionListener() { // Search버튼 액션리스너
			// Search 버튼을 눌렀을 경우 발생하는 액션 정의
			public void actionPerformed(ActionEvent e) {
				rKeyword = searchField.getText(); // 검색창에 입력된 값을 mKeyword 변수에 저장
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
		Object rsheader[] = { "기수", "학번", "이름", "제목", "감독", "평점", "평론" }; // 항목 이름
		Object rsob[][] = new Object[0][11];
		DefaultTableModel rsmodel = new DefaultTableModel(rsob, rsheader);
		JTable rstable = new JTable(rsmodel);
		JScrollPane rsjs;

		if (rSelected == "--") {
			searchWindow.setTitle("검색 실패");
			searchWindow.setSize(400, 200); // 창 크기조절
			JLabel rstext = new JLabel("검색기준을 선택해주십시오", JLabel.CENTER); // 경고메시지
			Font rsf = new Font("굴림", Font.BOLD, 15);
			rstext.setFont(rsf);
			searchWindow.add("Center", rstext); // 경고메시지 출력

		} else {
			// 검색결과 값 가져오기
			ResultSet rsr1 = reviewSearch();

			// 가져온 결과값 출력
			try {
				while (rsr1.next()) {
					// reviewSearch 함수에서 받아온 결과값들을 각각의 변수에 저장
					int rsclass = rsr1.getInt("기수");
					String rsnumber = rsr1.getString("학번");
					String rsname = rsr1.getString("이름");
					String rstitle = rsr1.getString("제목");
					String rsdirector = rsr1.getString("감독");
					float rsscore = rsr1.getFloat("평점");
					String rscritic = rsr1.getString("평론");
					
					// 각 변수들을 오브젝트 배열에 저장
					Object rscontents[] = { rsclass, rsnumber, rsname, rstitle, rsdirector, rsscore, rscritic };
					
					// 오브젝트 배열을 모델에 추가
					rsmodel.addRow(rscontents);

					// 검색결과 창에 출력될 객체들
					JLabel rstext = new JLabel("<영화평가 검색결과>", JLabel.CENTER);
					Font rsf = new Font("굴림", Font.BOLD, 20);
					rstext.setFont(rsf);
					JPanel resultPanel = new JPanel();
					rsjs = new JScrollPane(rstable);
					rstable.setPreferredSize(new Dimension(1100, 700));
					rsjs.setPreferredSize(new Dimension(1100, 700));
					rstable.getColumn("평론").setPreferredWidth(500);
					rstable.getColumn("평점").setPreferredWidth(5);
					rstable.getColumn("기수").setPreferredWidth(5);
					rstable.getColumn("제목").setPreferredWidth(200);

					// 객체들 붙이기
					searchWindow.add("North", rstext);
					resultPanel.add(rsjs);
					searchWindow.add("Center", resultPanel);

				}
			} catch (SQLException sqle) {
				System.out.println("SQLException : " + sqle);
			}

		}

	}

	// 영화평가 검색을 수행하여 ResultSet을 리턴하는 함수
	public ResultSet reviewSearch() {

		ResultSet rsr1 = null;

		try {

			// 검색기준이 "제목"인 경우
			if (rSelected == "제목") {
				PreparedStatement rspstmt = conn
						.prepareStatement("select 기수, 학번, 이름, 제목, 감독, 평점, 평론      from DB2021_평가    where 제목 = ?");
				rspstmt.setString(1, rKeyword);
				rsr1 = rspstmt.executeQuery();

				// 검색기준이 "학번"인 경우
			} else if (rSelected == "학번") {
				PreparedStatement rspstmt = conn
						.prepareStatement("select 기수, 학번, 이름, 제목, 감독, 평점, 평론      from DB2021_평가    where 학번 = ?");
				rspstmt.setString(1, rKeyword);
				rsr1 = rspstmt.executeQuery();

			} else if (rSelected == "단어") {
				PreparedStatement rspstmt = conn
						.prepareStatement("select 기수, 학번, 이름, 제목, 감독, 평점, 평론      from DB2021_평가    where 평론 like ?");
				rspstmt.setString(1, '%' + rKeyword + '%');
				rsr1 = rspstmt.executeQuery();
			}

		} catch (SQLException sqle) {
			System.out.println("SQLException : " + sqle);
		}
		return rsr1;

	}

}
