package DB2021Team10;

import java.sql.*;
import java.util.InputMismatchException;

import javax.swing.*;
import java.awt.event.*;

public class Movie_Insert extends JFrame {

	private JTextField titleText, directorText, dateText, genreText, actorText, countryText, runningtimeText,
			realnameText, birthText, careerText, rationText, salesText, audienceText, rottenText, naverText, watchaText,
			actnameText, dirnameText, dirbirthText, debutText, debutyearText, worknumText, reprenameText;
	private JTextArea summaryText;
	private JPanel panelBasic = new JPanel();
	private JPanel panelMore = new JPanel();
	private JPanel panelDirector = new JPanel();
	private JPanel panelActor = new JPanel();

	private Boolean nowPlayBoolean, dvdBoolean, breakevenBoolean;
	private String updateTitle, updateDirector, genderResult;
	private String[] stream = new String[4];

	// 필드 정의
	Connection conn;
	Statement stmt;

	// 커넥션 맺기
	public Movie_Insert(Connection conn, Statement stmt) {

		this.conn = conn;

		// 창 디자인
		setTitle("영화 추가하기");
		setSize(340, 600);
		setResizable(false);
		setLocation(450, 200);

		// Frame 위에 Panel 추가
		panelBasic(panelBasic);
		add(panelBasic);
		setVisible(true);

	}

	public void panelBasic(JPanel panel) {

		panel.setLayout(null);

		// '제목' 받기
		JLabel insertMovie = new JLabel("제목");
		insertMovie.setBounds(10, 10, 80, 25);
		panel.add(insertMovie);

		titleText = new JTextField(40);
		titleText.setBounds(165, 10, 160, 25);
		panel.add(titleText);

		// '감독' 받기
		JLabel insertDirector = new JLabel("감독");
		insertDirector.setBounds(10, 40, 80, 25);
		panel.add(insertDirector);

		directorText = new JTextField(40);
		directorText.setBounds(165, 40, 160, 25);
		panel.add(directorText);

		// '추가하기' 버튼
		JButton btnUpdate = new JButton("추가하기");
		btnUpdate.setBounds(225, 80, 100, 25);
		panel.add(btnUpdate);

		// 버튼 클릭시, insertBasic 메소드 호출
		btnUpdate.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				insertBasic();
			}
		});
	}

	public void insertBasic() {

		// prepared statement 사용
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		// 앞에서 입력 받은 input 값 (제목, 감독)
		updateTitle = titleText.getText();
		updateDirector = directorText.getText();

		String movie = null;

		// 이미 존재하는 영화인지 확인하기 위한 Select Query
		String sqlExistMovie = "SELECT 제목 FROM DB2021_영화 WHERE 감독 = ?";

		// 영화 제목과 감독은 필수 입력
		if (updateTitle.length() < 1 || updateDirector.length() < 1) {
			JOptionPane.showMessageDialog(null, "영화 제목과 감독은 필수 입력!");
			titleText.setText("");
			directorText.setText("");
			return;
		}

		try {

			// auto commit 해제
			conn.setAutoCommit(false);

			pstmt = conn.prepareStatement(sqlExistMovie);
			pstmt.setString(1, updateDirector);
			rs = pstmt.executeQuery();

			// 입력값과 일치하는 제목, 감독 행이 존재한다면,
			if (rs.next()) {

				movie = rs.getString("제목");
				if (movie.equals(updateTitle)) {
					JOptionPane.showMessageDialog(null, "이미 존재하는 영화입니다.");
					titleText.setText("");
					directorText.setText("");
					return;
				}

			} else {

				// 일치하는 행이 존재하지 않는다면,
				JOptionPane.showMessageDialog(null, "추가 입력할 사항이 있습니다.");

				// 현재 panel 숨기고 새로운 panel 추가
				panelBasic.setVisible(false);
				panelMore(panelMore);
				add(panelMore);
				panelMore.setVisible(true);

			}

		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Insert 구문 에러!");
		}
	}

	public void panelMore(JPanel panel) {

		panel.setLayout(null);

		// 1. 영화 테이블 Insert
		JLabel insertGenre = new JLabel("장르");
		insertGenre.setBounds(10, 10, 80, 25);
		panel.add(insertGenre);

		genreText = new JTextField(40);
		genreText.setBounds(165, 10, 160, 25);
		panel.add(genreText);

		JLabel insertSummary = new JLabel("줄거리");
		insertSummary.setBounds(10, 40, 80, 25);
		panel.add(insertSummary);

		summaryText = new JTextArea(7, 200);
		summaryText.setBounds(165, 40, 160, 45);
		panel.add(summaryText);

		JLabel insertActor = new JLabel("대표배우(1인)");
		insertActor.setBounds(10, 90, 80, 25);
		panel.add(insertActor);

		actorText = new JTextField(40);
		actorText.setBounds(165, 90, 160, 25);
		panel.add(actorText);

		JLabel insertDate = new JLabel("개봉날짜(0000-00-00)");
		insertDate.setBounds(10, 120, 140, 25);
		panel.add(insertDate);

		dateText = new JTextField(40);
		dateText.setBounds(165, 120, 160, 25);
		panel.add(dateText);

		JLabel insertCountry = new JLabel("제작국가");
		insertCountry.setBounds(10, 150, 80, 25);
		panel.add(insertCountry);

		countryText = new JTextField(40);
		countryText.setBounds(165, 150, 160, 25);
		panel.add(countryText);

		JLabel insertRunningTime = new JLabel("러닝타임(분)");
		insertRunningTime.setBounds(10, 180, 80, 25);
		panel.add(insertRunningTime);

		runningtimeText = new JTextField(40);
		runningtimeText.setBounds(165, 180, 160, 25);
		panel.add(runningtimeText);

		// '현재_상영' attribute는 radio button으로 구현
		JLabel insertNowplaying = new JLabel("현재상영여부");
		insertNowplaying.setBounds(10, 210, 80, 25);
		panel.add(insertNowplaying);

		JRadioButton nowTrue = new JRadioButton("True");
		JRadioButton nowFalse = new JRadioButton("False");
		ButtonGroup now = new ButtonGroup();

		JLabel nowPlaying = new JLabel();
		nowTrue.setBounds(165, 210, 80, 25);
		nowFalse.setBounds(235, 210, 80, 25);
		nowPlaying.setBounds(165, 210, 160, 25);
		panel.add(nowTrue);
		panel.add(nowFalse);
		nowTrue.setSelected(true);
		panel.add(nowPlaying);
		now.add(nowTrue);
		now.add(nowFalse);

		if (nowTrue.isSelected()) {
			nowPlayBoolean = true;
		} else {
			nowPlayBoolean = false;
		}

		// 2. 유통 테이블 Insert
		// 'DVD_제공' attribute는 radio button으로 구현
		JLabel insertDVD = new JLabel("DVD제공");
		insertDVD.setBounds(10, 240, 80, 25);
		panel.add(insertDVD);

		JRadioButton DVDTrue = new JRadioButton("True");
		JRadioButton DVDFalse = new JRadioButton("False");
		ButtonGroup DVD = new ButtonGroup();

		JLabel existDVD = new JLabel();
		DVDTrue.setBounds(165, 240, 80, 25);
		DVDFalse.setBounds(235, 240, 80, 25);
		existDVD.setBounds(165, 240, 160, 25);
		panel.add(DVDTrue);
		panel.add(DVDFalse);
		DVDTrue.setSelected(true);
		panel.add(existDVD);
		DVD.add(DVDTrue);
		DVD.add(DVDFalse);

		if (DVDTrue.isSelected()) {
			dvdBoolean = true;
		} else {
			dvdBoolean = false;
		}

		JLabel insertRation = new JLabel("배급사");
		insertRation.setBounds(10, 270, 80, 25);
		panel.add(insertRation);

		rationText = new JTextField(40);
		rationText.setBounds(165, 270, 160, 25);
		panel.add(rationText);

		// 3. 스트리밍 테이블 Insert
		// 스트리밍 테이블은 check box로 구현
		JLabel insertStream = new JLabel("스트리밍서비스");
		insertStream.setBounds(10, 300, 80, 25);
		panel.add(insertStream);

		JCheckBox netflix = new JCheckBox("넷플릭스");
		JCheckBox watcha = new JCheckBox("왓챠");
		JCheckBox wave = new JCheckBox("웨이브");
		JCheckBox tving = new JCheckBox("티빙");
		netflix.setBounds(165, 300, 80, 25);
		watcha.setBounds(235, 300, 80, 25);
		wave.setBounds(165, 320, 80, 25);
		tving.setBounds(235, 320, 80, 25);
		panel.add(netflix);
		panel.add(watcha);
		panel.add(wave);
		panel.add(tving);

		if (netflix.isSelected()) {
			stream[0] = "넷플릭스";
		} else {
			stream[0] = null;
		}
		if (watcha.isSelected()) {
			stream[1] = "왓챠";
		} else {
			stream[1] = null;
		}
		if (wave.isSelected()) {
			stream[2] = "웨이브";
		} else {
			stream[2] = null;
		}
		if (tving.isSelected()) {
			stream[3] = "티빙";
		} else {
			stream[3] = null;
		}

		// 4. 수익 테이블 Insert
		JLabel insertSales = new JLabel("매출");
		insertSales.setBounds(10, 350, 80, 25);
		panel.add(insertSales);

		salesText = new JTextField(40);
		salesText.setBounds(165, 350, 160, 25);
		panel.add(salesText);

		JLabel insertAudience = new JLabel("관객수(명)");
		insertAudience.setBounds(10, 380, 80, 25);
		panel.add(insertAudience);

		audienceText = new JTextField(40);
		audienceText.setBounds(165, 380, 160, 25);
		panel.add(audienceText);

		JLabel insertBreakeven = new JLabel("손익분기점도달");
		insertBreakeven.setBounds(10, 410, 140, 25);
		panel.add(insertBreakeven);

		JRadioButton breakevenTrue = new JRadioButton("True");
		JRadioButton breakevenFalse = new JRadioButton("False");
		ButtonGroup begroup = new ButtonGroup();

		JLabel breakEven = new JLabel();
		breakevenTrue.setBounds(165, 410, 80, 25);
		breakevenFalse.setBounds(235, 410, 80, 25);
		breakEven.setBounds(165, 410, 160, 25);
		panel.add(breakevenTrue);
		panel.add(breakevenFalse);
		breakevenTrue.setSelected(true);
		panel.add(breakEven);
		begroup.add(breakevenTrue);
		begroup.add(breakevenFalse);

		if (breakevenTrue.isSelected()) {
			breakevenBoolean = true;
		} else {
			breakevenBoolean = false;
		}

		JLabel insertNaver = new JLabel("네이버평점(/10.0)");
		insertNaver.setBounds(10, 440, 140, 25);
		panel.add(insertNaver);

		naverText = new JTextField(40);
		naverText.setBounds(165, 440, 160, 25);
		panel.add(naverText);

		JLabel insertRotten = new JLabel("로튼토마토평점(/100)");
		insertRotten.setBounds(10, 470, 140, 25);
		panel.add(insertRotten);

		rottenText = new JTextField(40);
		rottenText.setBounds(165, 470, 160, 25);
		panel.add(rottenText);

		JLabel insertWatcha = new JLabel("왓챠평점(/5.0)");
		insertWatcha.setBounds(10, 500, 140, 25);
		panel.add(insertWatcha);

		watchaText = new JTextField(40);
		watchaText.setBounds(165, 510, 160, 25);
		panel.add(watchaText);

		JButton btnNext = new JButton("이어서 작성하기");
		btnNext.setBounds(225, 540, 100, 25);
		panel.add(btnNext);

		// 버튼 클릭시, panelDirector 메소드 호출 
		btnNext.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				// 현재 panel 숨기고 새로운 panel 부착 
				panelMore.setVisible(false);
				panelDirector(panelDirector);
				add(panelDirector);
				panelDirector.setVisible(true);
			}
		});
	}

	public void panelDirector(JPanel panel) {

		panel.setLayout(null);

		// 감독 테이블 Insert
		JLabel insertDirname = new JLabel("감독이름");
		insertDirname.setBounds(10, 10, 80, 25);
		panel.add(insertDirname);

		JLabel dirname = new JLabel(updateDirector);
		dirname.setBounds(165, 10, 160, 25);
		panel.add(dirname);

		JLabel insertDirbirth = new JLabel("*생년월일(0000-00-00)");
		insertDirbirth.setBounds(10, 40, 140, 25);
		panel.add(insertDirbirth);

		dirbirthText = new JTextField(40);
		dirbirthText.setBounds(165, 40, 160, 25);
		panel.add(dirbirthText);

		JLabel insertDebut = new JLabel("데뷔작");
		insertDebut.setBounds(10, 70, 80, 25);
		panel.add(insertDebut);

		debutText = new JTextField(40);
		debutText.setBounds(165, 70, 160, 25);
		panel.add(debutText);

		JLabel insertDebutyear = new JLabel("데뷔연도");
		insertDebutyear.setBounds(10, 100, 80, 25);
		panel.add(insertDebutyear);

		debutyearText = new JTextField(40);
		debutyearText.setBounds(165, 100, 160, 25);
		panel.add(debutyearText);

		JLabel insertWorknum = new JLabel("작품개수");
		insertWorknum.setBounds(10, 130, 80, 25);
		panel.add(insertWorknum);

		worknumText = new JTextField(40);
		worknumText.setBounds(165, 130, 160, 25);
		panel.add(worknumText);

		JLabel insertReprename = new JLabel("대표작");
		insertReprename.setBounds(10, 160, 80, 25);
		panel.add(insertReprename);

		reprenameText = new JTextField(40);
		reprenameText.setBounds(165, 160, 160, 25);
		panel.add(reprenameText);

		JButton btnNext3 = new JButton("추가하기");
		btnNext3.setBounds(225, 210, 100, 25);
		panel.add(btnNext3);

		// 버튼 클릭시, insertFour 메소드 호출 및 완료메시지 띄우기
		btnNext3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				insertDirector();
			}
		});
	}

	public void insertDirector() {

		PreparedStatement pstmtDirector = null;

		// 감독 테이블에 들어갈 값들
		String updateDirbirth = dirbirthText.getText();
		String updateDebut = debutText.getText();
		String updateReprename = reprenameText.getText();

		// 감독 테이블에 접근할 쿼리
		String directorQuery = "INSERT INTO DB2021_감독 VALUES (?,?,?,?,?,?)";

		// 감독이름과 생년월일은 필수 입력
		if (updateDirbirth.length() < 1) {
			JOptionPane.showMessageDialog(null, "감독이름과 생년월일은 필수 입력!");
			dirnameText.setText("");
			dirbirthText.setText("");
			return;
		}

		try {

			// 감독 테이블에 아이템 삽입
			pstmtDirector = conn.prepareStatement(directorQuery);
			pstmtDirector.setString(1, updateDirector);
			pstmtDirector.setString(2, updateDirbirth);
			pstmtDirector.setString(3, updateDebut);
			pstmtDirector.setInt(4, Integer.parseInt(debutyearText.getText()));
			pstmtDirector.setInt(5, Integer.parseInt(worknumText.getText()));
			pstmtDirector.setString(6, updateReprename);
			pstmtDirector.executeUpdate();

			// insertMore 메소드 호출 
			insertMore();

		} catch (SQLException e) {

			JOptionPane.showMessageDialog(null, "Insert 구문 에러!");

		} catch (NumberFormatException sen) {
			JOptionPane.showMessageDialog(null, "숫자만 입력해주세요.");
		}
	}

	public void insertMore() {
		PreparedStatement pstmtInsert = null;
		PreparedStatement pstmtCirculate = null;
		PreparedStatement pstmtStreaming = null;
		PreparedStatement pstmtRevenue = null;
		PreparedStatement pstmtGrade = null;
		System.out.println("prepared");

		// 영화 테이블에 들어갈 값들
		String updateGenre = genreText.getText();
		String updateSummary = summaryText.getText();
		String updateActor = actorText.getText();
		String updateDate = dateText.getText();
		String updateCountry = countryText.getText();
		int updateRunningtime = Integer.parseInt(runningtimeText.getText());

		// 유통 테이블에 들어갈 값
		String updateRation = rationText.getText();

		// 수익 테이블에 들어갈 값
		long updateSales = Long.parseLong(salesText.getText());
		long updateAudience = Long.parseLong(audienceText.getText());

		// 평점 테이블에 들어갈 값
		Float updateNaver = Float.parseFloat(naverText.getText());
		int updateRotten = Integer.parseInt(rottenText.getText());
		Float updateWatcha = Float.parseFloat(watchaText.getText());

		// 각 테이블에 들어갈 쿼리들
		String movieQuery = "INSERT INTO DB2021_영화 VALUES (?,?,?,?,?,?,?,?,?)";
		String circulateQuery = "INSERT INTO DB2021_유통 VALUES (?,?,?,?);";
		String streamingQuery = "INSERT INTO DB2021_스트리밍 VALUES (?,?,?);";
		String revenueQuery = "INSERT INTO DB2021_수익 VALUES (?,?,?,?,?);";
		String gradeQuery = "INSERT INTO DB2021_평점 VALUES (?,?,?,?,?);";

		try {
			// 영화 테이블에 아이템 삽입
			pstmtInsert = conn.prepareStatement(movieQuery);
			pstmtInsert.setString(1, updateTitle);
			pstmtInsert.setString(2, updateDirector);
			pstmtInsert.setString(3, updateGenre);
			pstmtInsert.setString(4, updateSummary);
			pstmtInsert.setString(5, updateActor);
			pstmtInsert.setString(6, updateDate);
			pstmtInsert.setString(7, updateCountry);
			pstmtInsert.setInt(8, updateRunningtime);
			pstmtInsert.setBoolean(9, nowPlayBoolean);
			pstmtInsert.executeUpdate();
			System.out.println("movie");

			// 유통 테이블에 아이템 삽입
			pstmtCirculate = conn.prepareStatement(circulateQuery);
			pstmtCirculate.setString(1, updateTitle);
			pstmtCirculate.setString(2, updateDirector);
			pstmtCirculate.setBoolean(3, dvdBoolean);
			pstmtCirculate.setString(4, updateRation);
			pstmtCirculate.executeUpdate();

			// 스트리밍 테이블에 아이템 삽입 (체크 박스에서 선택한 개수만큼 행 추가)
			pstmtStreaming = conn.prepareStatement(streamingQuery);
			for (int i = 0; i < stream.length; i++) {
				if (stream[i] != null) {
					pstmtStreaming.setString(1, updateTitle);
					pstmtStreaming.setString(2, updateDirector);
					pstmtStreaming.setString(3, stream[i]);
					pstmtStreaming.executeUpdate();
				}
			}

			// 수익 테이블에 아이템 삽입
			pstmtRevenue = conn.prepareStatement(revenueQuery);
			pstmtRevenue.setString(1, updateTitle);
			pstmtRevenue.setString(2, updateDirector);
			pstmtRevenue.setLong(3, updateSales);
			pstmtRevenue.setLong(4, updateAudience);
			pstmtRevenue.setBoolean(5, breakevenBoolean);
			pstmtRevenue.executeUpdate();

			// 평점 테이블에 아이템 삽입
			pstmtGrade = conn.prepareStatement(gradeQuery);
			pstmtGrade.setString(1, updateTitle);
			pstmtGrade.setString(2, updateDirector);
			pstmtGrade.setFloat(3, updateNaver);
			pstmtGrade.setInt(4, updateRotten);
			pstmtGrade.setFloat(5, updateWatcha);
			pstmtGrade.executeUpdate();

			// 기존 panel을 숨긴 후, 새로운 panel 부착
			panelDirector.setVisible(false);
			insertActor(panelActor);
			add(panelActor);
			panelActor.setVisible(true);

		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Insert 구문 에러!");
		}
	}

	public void insertActor(JPanel panel) {

		panel.setLayout(null);

		// 배우 테이블 Insert
		JLabel insertRealname = new JLabel("*배우이름");
		insertRealname.setBounds(10, 10, 80, 25);
		panel.add(insertRealname);

		realnameText = new JTextField(40);
		realnameText.setBounds(165, 10, 160, 25);
		panel.add(realnameText);

		JLabel insertBirth = new JLabel("*생년월일(0000-00-00)");
		insertBirth.setBounds(10, 40, 140, 25);
		panel.add(insertBirth);

		birthText = new JTextField(40);
		birthText.setBounds(165, 40, 160, 25);
		panel.add(birthText);

		JLabel insertActname = new JLabel("배역이름");
		insertActname.setBounds(10, 70, 80, 25);
		panel.add(insertActname);

		actnameText = new JTextField(40);
		actnameText.setBounds(165, 70, 160, 25);
		panel.add(actnameText);

		// 배우의 성별은 radio button으로 구현
		JLabel insertGender = new JLabel("성별");
		insertGender.setBounds(10, 100, 80, 25);
		panel.add(insertGender);

		JRadioButton femaleB = new JRadioButton("여성");
		JRadioButton maleB = new JRadioButton("남성");
		ButtonGroup gender = new ButtonGroup();

		JLabel genderRadio = new JLabel();
		femaleB.setBounds(165, 100, 80, 25);
		maleB.setBounds(235, 100, 80, 25);
		genderRadio.setBounds(165, 100, 160, 25);
		panel.add(femaleB);
		panel.add(maleB);
		panel.add(genderRadio);
		gender.add(femaleB);
		gender.add(maleB);

		if (femaleB.isSelected()) {
			genderResult = "여성";
		} else {
			genderResult = "남성";
		}

		JLabel insertCareer = new JLabel("직업");
		insertCareer.setBounds(10, 130, 80, 25);
		panel.add(insertCareer);

		careerText = new JTextField(40);
		careerText.setBounds(165, 130, 160, 25);
		panel.add(careerText);

		JButton btnNext2 = new JButton("추가하기");
		btnNext2.setBounds(225, 180, 100, 25);
		panel.add(btnNext2);

		// 버튼 클릭시, insertThree 메소드 호출
		btnNext2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				insertActor();
				JOptionPane.showMessageDialog(null, "영화 업로드가 완료되었습니다\n 창을 닫아주세요.");
			}
		});
	}

	public void insertActor() {

		PreparedStatement pstmtActor = null;

		// 배우 테이블에 들어갈 값들
		String updateRealname = realnameText.getText();
		String updateBirth = birthText.getText();
		String updateActname = actnameText.getText();
		String updateCareer = careerText.getText();

		// 배우 테이블에 접근하는 쿼리
		String actorQuery = "INSERT INTO DB2021_배우 VALUES (?,?,?,?,?,?,?)";

		// 배우 이름과 생년월일 입력은 필수
		if (updateRealname.length() < 1 || updateBirth.length() < 1) {
			JOptionPane.showMessageDialog(null, "배우 이름과 생년월일은 필수 입력!");
			realnameText.setText("");
			birthText.setText("");
			return;
		}

		try {

			// 배우 테이블에 아이템 삽입
			pstmtActor = conn.prepareStatement(actorQuery);
			pstmtActor.setString(1, updateTitle);
			pstmtActor.setString(2, updateDirector);
			pstmtActor.setString(3, updateRealname);
			pstmtActor.setString(4, updateBirth);
			pstmtActor.setString(5, updateActname);
			pstmtActor.setString(6, genderResult);
			pstmtActor.setString(7, updateCareer);
			pstmtActor.executeUpdate();

			// 처음부터 여기까지 문제없이 진행됐다면, commit
			conn.commit();

			// reset
			conn.setAutoCommit(true);

		} catch (InputMismatchException ime) {

			JOptionPane.showMessageDialog(null, "형식이 틀린 입력값이 있습니다.\n 수정 후 다시 시도해주세요.");

		} catch (SQLException e) {

			try {
				// 문제 생겼으면 rollback
				conn.rollback();

			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			System.out.println("INSERT 구문 에러!");
		}
	}

}