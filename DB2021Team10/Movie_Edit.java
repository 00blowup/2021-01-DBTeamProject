package DB2021Team10;

import java.sql.*;
import java.util.InputMismatchException;

import javax.swing.*;
import java.awt.event.*;

public class Movie_Edit extends JFrame {

	Connection conn;
	Statement stmt;

	// checkDBm에서 쿼리 정의에 사용할 String 변수
	String checkQuery;

	private JPanel panelOne = new JPanel();
	private JPanel panelTwo = new JPanel();
	private JTextField movieText, directorText;

	private String updateMovie, updateDirector;
	private String genre, summary, representActor, date, country, runningTime, nowPlaying, dvd, ration, sales, audience,
			breakEven, naver, rotten, watchaPlay, realName, actBirth, actName, gender, career;

	private JFrame frame = new JFrame();
	private JFrame frameService = new JFrame();
	private JCheckBox netflix, watcha, wave, tving;
	private String[] stream = new String[4];
	private String[] service = new String[4];

	public Movie_Edit(Connection conn, Statement stmt) {

		this.conn = conn;
		this.stmt = stmt;

		setTitle("영화 수정하기");
		setSize(370, 800);
		setLocation(450, 0);

		editPanelOne(panelOne);
		add(panelOne);

		setVisible(true);

	}

	public void editPanelOne(JPanel panel) {

		panel.setLayout(null);

		JLabel editMovie = new JLabel("제목");
		editMovie.setBounds(10, 10, 80, 25);
		panel.add(editMovie);

		movieText = new JTextField(40);
		movieText.setBounds(165, 10, 170, 25);
		panel.add(movieText);

		JLabel editDirector = new JLabel("감독");
		editDirector.setBounds(10, 40, 80, 25);
		panel.add(editDirector);

		directorText = new JTextField(40);
		directorText.setBounds(165, 40, 170, 25);
		panel.add(directorText);

		JButton btnUpdate = new JButton("수정하기");
		btnUpdate.setBounds(225, 80, 100, 25);
		panel.add(btnUpdate);

		btnUpdate.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// 위에서 입력 받은 영화 제목, 영화 감독
				updateMovie = movieText.getText();
				updateDirector = directorText.getText();

				// 입력한 제목, 감독에 해당하는 영화의 평가를 한 적이 있는지 체크
				int checkedResult = checkDBm(updateMovie, updateDirector);

				// 해당 영화가 DB에 존재한다면 수정을 수행
				if (checkedResult == 1) {
					editOne();
				} else if (checkedResult == 0) {
					// 그렇지 않다면 경고메시지 띄우기
					JOptionPane.showMessageDialog(null, "DB에 존재하지 않는 영화이거나, 입력이 잘못되었습니다");

				}
			}

		});

	}

	// 입력한 영화가 DB에 존재하는지 확인하는 메소드
	public int checkDBm(String title, String director) {
		int result = 0;
		try {

			// DB에 저장된 모든 영화의 제목과 감독 값을 받아와, ResultSet checkedrs에 저장
			checkQuery = "select 제목, 감독 from DB2021_영화";

			ResultSet checkedrs = stmt.executeQuery(checkQuery);

			// 수정을 위해 입력한 제목과 감독의 값을, checkedrs의 값들과 비교
			while (checkedrs.next()) {
				if ((checkedrs.getString("제목").equals(updateMovie))
						&& (checkedrs.getString("감독").equals(updateDirector))) {
					// 입력한 제목, 감독 값에 해당되는 영화가 존재하면 result값을 1로 설정하고 while문을 종료
					result = 1;
					break;
				} else
					continue;
			}
			// 입력한 제목, 감독 값에 해당되는 영화가 없다면 result값이 0인 채로 while문이 종료됨

		} catch (SQLException e) {
			System.out.print("checkDB 에러!");
		}

		// result를 리턴
		return result;
	}

	// 기존의 값들 불러오기
	public void editOne() {

		String movieExist = null;

		PreparedStatement movie = null;
		PreparedStatement circulate = null;
		PreparedStatement streaming = null;
		PreparedStatement revenue = null;
		PreparedStatement grade = null;
		PreparedStatement actor = null;
		PreparedStatement pstmt = null;

		ResultSet rsMovie = null;
		ResultSet rsCirculate = null;
		ResultSet rsStreaming = null;
		ResultSet rsRevenue = null;
		ResultSet rsGrade = null;
		ResultSet rsActor = null;
		ResultSet rs = null;

		// 각 테이블에게 접근할 쿼리들
		String existMovie = "SELECT 제목, 감독 FROM DB2021_영화 WHERE 감독=?";
		String movieQuery = "SELECT 장르, 줄거리, 대표_배우, 개봉_날짜, 국가, 러닝_타임, 현재_상영 FROM DB2021_영화 WHERE 제목=? and 감독=?";
		String circulateQuery = "SELECT DVD_제공, 배급사 FROM DB2021_유통 WHERE 제목=? and 감독=?";
		String streamingQuery = "SELECT 서비스 FROM DB2021_스트리밍 WHERE 제목=? and 감독=?";
		String revenueQuery = "SELECT 매출, 관객_수, 손익분기점_도달 FROM DB2021_수익 WHERE 제목=? and 감독=?";
		String gradeQuery = "SELECT 네이버, 로튼토마토, 왓챠 FROM DB2021_평점 WHERE 제목=? and 감독=?";
		String actorQuery = "SELECT 이름, 생년월일, 배역_이름, 성별, 직업 FROM DB2021_배우 WHERE 제목=? and 감독=?";

		// 위에서 입력 받은 값이 없을 경우, 필수로 입력하도록 return
		if (updateMovie.length() < 1 || updateDirector.length() < 1) {
			JOptionPane.showMessageDialog(null, "수정할 영화 제목과 감독은 필수 입력!");
			movieText.setText("");
			directorText.setText("");
			return;
		}

		try {

			pstmt = conn.prepareStatement(existMovie);
			pstmt.setString(1, updateDirector);
			rs = pstmt.executeQuery();

			// 입력값과 일치하는 제목, 감독 행이 존재한다면,
			if (rs.next()) {

				movieExist = rs.getString("제목");

				if (movieExist.equals(updateMovie)) {
					// 영화 테이블에서 값들 불러오기
					movie = conn.prepareStatement(movieQuery);
					movie.setString(1, updateMovie);
					movie.setString(2, updateDirector);
					rsMovie = movie.executeQuery();

					if (rsMovie.next()) {

						genre = rsMovie.getString("장르");
						summary = rsMovie.getString("줄거리");
						representActor = rsMovie.getString("대표_배우");
						date = rsMovie.getString("개봉_날짜");
						country = rsMovie.getString("국가");
						runningTime = Integer.toString(rsMovie.getInt("러닝_타임"));
						nowPlaying = Boolean.toString(rsMovie.getBoolean("현재_상영"));

					}

					// 유통 테이블에서 값들 불러오기
					circulate = conn.prepareStatement(circulateQuery);
					circulate.setString(1, updateMovie);
					circulate.setString(2, updateDirector);
					rsCirculate = circulate.executeQuery();

					if (rsCirculate.next()) {

						dvd = Boolean.toString(rsCirculate.getBoolean("DVD_제공"));
						ration = rsCirculate.getString("배급사");

					}

					// 스트리밍 테이블에서 값들 불러오기
					streaming = conn.prepareStatement(streamingQuery);
					streaming.setString(1, updateMovie);
					streaming.setString(2, updateDirector);
					rsStreaming = streaming.executeQuery();

					int i = 0;

					while (rsStreaming.next()) {

						service[i] = rsStreaming.getString("서비스");
						i++;

					}

					// 수익 테이블에서 값들 불러오기
					revenue = conn.prepareStatement(revenueQuery);
					revenue.setString(1, updateMovie);
					revenue.setString(2, updateDirector);
					rsRevenue = revenue.executeQuery();

					if (rsRevenue.next()) {

						sales = Long.toString(rsRevenue.getLong("매출"));
						audience = Long.toString(rsRevenue.getLong("관객_수"));
						breakEven = Boolean.toString(rsRevenue.getBoolean("손익분기점_도달"));

					}

					// 평점 테이블에서 값들 불러오기
					grade = conn.prepareStatement(gradeQuery);
					grade.setString(1, updateMovie);
					grade.setString(2, updateDirector);
					rsGrade = grade.executeQuery();

					if (rsGrade.next()) {

						naver = Float.toString(rsGrade.getFloat("네이버"));
						rotten = Integer.toString(rsGrade.getInt("로튼토마토"));
						watchaPlay = Float.toString(rsGrade.getFloat("왓챠"));

					}

					// 배우 테이블에서 값들 불러오기
					actor = conn.prepareStatement(actorQuery);
					actor.setString(1, updateMovie);
					actor.setString(2, updateDirector);
					rsActor = actor.executeQuery();

					if (rsActor.next()) {

						realName = rsActor.getString("이름");
						actBirth = rsActor.getString("생년월일");
						actName = rsActor.getString("배역_이름");
						gender = rsActor.getString("성별");
						career = rsActor.getString("직업");

					}

					// 기존 panel 숨기고 새로운 panel 추가하기
					panelOne.setVisible(false);
					editPanelTwo(panelTwo);
					add(panelTwo);
					panelTwo.setVisible(true);

				}

			} else {

				// 일치하는 행이 존재하지 않는다면,
				JOptionPane.showMessageDialog(null, "존재하지 않는 영화입니다.");
				movieText.setText("");
				directorText.setText("");
				return;

			}

		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "UPDATE 구문 에러!");
		} catch (NumberFormatException sen) {
			JOptionPane.showMessageDialog(null, "숫자만 입력해주세요.");
		}
	}

	// 불러온 값들 보여주기
	public void editPanelTwo(JPanel panel) {

		panel.setLayout(null);

		JLabel titleTag = new JLabel("제목");
		titleTag.setBounds(13, 15, 105, 25);
		panel.add(titleTag);

		JLabel titleResult = new JLabel(updateMovie);
		titleResult.setBounds(125, 15, 125, 25);
		panel.add(titleResult);

		JButton btnTitle = new JButton("수정");
		btnTitle.setBounds(270, 15, 80, 25);
		panel.add(btnTitle);

		// 제목 수정
		btnTitle.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				editInput("제목");
			}
		});

		JLabel directorTag = new JLabel("감독");
		directorTag.setBounds(13, 45, 105, 25);
		panel.add(directorTag);

		JLabel directorResult = new JLabel(updateDirector);
		directorResult.setBounds(125, 45, 125, 25);
		panel.add(directorResult);

		JButton btnDirector = new JButton("수정");
		btnDirector.setBounds(270, 45, 80, 25);
		panel.add(btnDirector);

		// 감독 수정
		btnDirector.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				editInput("감독");
			}
		});

		JLabel genreTag = new JLabel("장르");
		genreTag.setBounds(13, 75, 105, 25);
		panel.add(genreTag);

		JLabel genreResult = new JLabel(genre);
		genreResult.setBounds(125, 75, 125, 25);
		panel.add(genreResult);

		JButton btnGenre = new JButton("수정");
		btnGenre.setBounds(270, 75, 80, 25);
		panel.add(btnGenre);

		// 장르 수정
		btnGenre.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				editInput("장르");
			}
		});

		JLabel summaryTag = new JLabel("줄거리");
		summaryTag.setBounds(13, 105, 105, 25);
		panel.add(summaryTag);

		JLabel summaryResult = new JLabel(summary);
		summaryResult.setBounds(125, 105, 125, 25);
		panel.add(summaryResult);

		JButton btnSummary = new JButton("수정");
		btnSummary.setBounds(270, 105, 80, 25);
		panel.add(btnSummary);

		// 줄거리 수정
		btnSummary.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				editInput("줄거리");
			}
		});

		JLabel actorTag = new JLabel("대표배우");
		actorTag.setBounds(13, 135, 105, 25);
		panel.add(actorTag);

		JLabel actorResult = new JLabel(representActor);
		actorResult.setBounds(125, 135, 125, 25);
		panel.add(actorResult);

		JButton btnActor = new JButton("수정");
		btnActor.setBounds(270, 135, 80, 25);
		panel.add(btnActor);

		// 대표배우 수정
		btnActor.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				editInput("대표배우");
			}
		});

		JLabel dateTag = new JLabel("개봉날짜");
		dateTag.setBounds(13, 165, 105, 25);
		panel.add(dateTag);

		JLabel dateResult = new JLabel(date);
		dateResult.setBounds(125, 165, 125, 25);
		panel.add(dateResult);

		JButton btnDate = new JButton("수정");
		btnDate.setBounds(270, 165, 80, 25);
		panel.add(btnDate);

		// 개봉날짜 수정
		btnDate.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				editInput("개봉날짜");
			}
		});

		JLabel countryTag = new JLabel("국가");
		countryTag.setBounds(13, 195, 105, 25);
		panel.add(countryTag);

		JLabel countryResult = new JLabel(country);
		countryResult.setBounds(125, 195, 125, 25);
		panel.add(countryResult);

		JButton btnCountry = new JButton("수정");
		btnCountry.setBounds(270, 195, 80, 25);
		panel.add(btnCountry);

		// 국가 수정
		btnCountry.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				editInput("국가");
			}
		});

		JLabel runningTimeTag = new JLabel("러닝타임");
		runningTimeTag.setBounds(13, 225, 105, 25);
		panel.add(runningTimeTag);

		JLabel runningTimeResult = new JLabel(runningTime);
		runningTimeResult.setBounds(125, 225, 125, 25);
		panel.add(runningTimeResult);

		JButton btnRunningTime = new JButton("수정");
		btnRunningTime.setBounds(270, 225, 80, 25);
		panel.add(btnRunningTime);

		// 러닝타임 수정
		btnRunningTime.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				editInput("러닝타임");
			}
		});

		JLabel nowPlayingTag = new JLabel("현재상영");
		nowPlayingTag.setBounds(13, 255, 105, 25);
		panel.add(nowPlayingTag);

		JLabel nowPlayingResult = new JLabel(nowPlaying);
		nowPlayingResult.setBounds(125, 255, 125, 25);
		panel.add(nowPlayingResult);

		JButton btnNowPlaying = new JButton("수정");
		btnNowPlaying.setBounds(270, 255, 80, 25);
		panel.add(btnNowPlaying);

		// 현재상영 수정
		btnNowPlaying.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				editInput("현재상영");
			}
		});

		JLabel dvdTag = new JLabel("DVD제공");
		dvdTag.setBounds(13, 285, 105, 25);
		panel.add(dvdTag);

		JLabel dvdResult = new JLabel(dvd);
		dvdResult.setBounds(125, 285, 125, 25);
		panel.add(dvdResult);

		JButton btnDVD = new JButton("수정");
		btnDVD.setBounds(270, 285, 80, 25);
		panel.add(btnDVD);

		// DVD제공 수정
		btnDVD.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				editInput("DVD제공");
			}
		});

		JLabel rationTag = new JLabel("배급사");
		rationTag.setBounds(13, 315, 105, 25);
		panel.add(rationTag);

		JLabel rationResult = new JLabel(ration);
		rationResult.setBounds(125, 315, 125, 25);
		panel.add(rationResult);

		JButton btnRation = new JButton("수정");
		btnRation.setBounds(270, 315, 80, 25);
		panel.add(btnRation);

		// 배급사 수정
		btnRation.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				editInput("배급사");
			}
		});

		JLabel serviceTag = new JLabel("서비스");
		serviceTag.setBounds(13, 345, 105, 25);
		panel.add(serviceTag);

		if (service.length == 1) {

			JLabel serviceResult = new JLabel(service[0]);
			serviceResult.setBounds(125, 345, 70, 25);
			panel.add(serviceResult);

		} else if (service.length == 2) {

			JLabel serviceResult = new JLabel(service[0]);
			serviceResult.setBounds(125, 345, 70, 25);
			panel.add(serviceResult);

			JLabel serviceResultTwo = new JLabel(service[1]);
			serviceResultTwo.setBounds(125, 375, 70, 25);
			panel.add(serviceResultTwo);

		} else if (service.length == 3) {

			JLabel serviceResult = new JLabel(service[0]);
			serviceResult.setBounds(125, 345, 70, 25);
			panel.add(serviceResult);

			JLabel serviceResultTwo = new JLabel(service[1]);
			serviceResultTwo.setBounds(125, 375, 70, 25);
			panel.add(serviceResultTwo);

			JLabel serviceResultThree = new JLabel(service[2]);
			serviceResultThree.setBounds(195, 345, 70, 25);
			panel.add(serviceResultThree);

		} else if (service.length == 4) {

			JLabel serviceResult = new JLabel(service[0]);
			serviceResult.setBounds(125, 345, 70, 25);
			panel.add(serviceResult);

			JLabel serviceResultTwo = new JLabel(service[1]);
			serviceResultTwo.setBounds(125, 375, 70, 25);
			panel.add(serviceResultTwo);

			JLabel serviceResultThree = new JLabel(service[2]);
			serviceResultThree.setBounds(195, 345, 70, 25);
			panel.add(serviceResultThree);

			JLabel serviceResultFour = new JLabel(service[3]);
			serviceResultFour.setBounds(195, 375, 70, 25);
			panel.add(serviceResultFour);

		}

		JButton btnService = new JButton("수정");
		btnService.setBounds(270, 345, 80, 25);
		panel.add(btnService);

		// 서비스 수정
		btnService.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				editInputService();
			}
		});

		JLabel salesTag = new JLabel("매출");
		salesTag.setBounds(13, 405, 105, 25);
		panel.add(salesTag);

		JLabel salesResult = new JLabel(sales);
		salesResult.setBounds(125, 405, 125, 25);
		panel.add(salesResult);

		JButton btnSales = new JButton("수정");
		btnSales.setBounds(270, 405, 80, 25);
		panel.add(btnSales);

		// 매출 수정
		btnSales.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				editInput("매출");
			}
		});

		JLabel audienceTag = new JLabel("관객수");
		audienceTag.setBounds(13, 435, 105, 25);
		panel.add(audienceTag);

		JLabel audienceResult = new JLabel(audience);
		audienceResult.setBounds(125, 435, 125, 25);
		panel.add(audienceResult);

		JButton btnAudience = new JButton("수정");
		btnAudience.setBounds(270, 435, 80, 25);
		panel.add(btnAudience);

		// 관객수 수정
		btnAudience.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				editInput("관객수");
			}
		});

		JLabel breakEvenTag = new JLabel("손익분기점도달");
		breakEvenTag.setBounds(13, 465, 105, 25);
		panel.add(breakEvenTag);

		JLabel breakEvenResult = new JLabel(breakEven);
		breakEvenResult.setBounds(125, 465, 125, 25);
		panel.add(breakEvenResult);

		JButton btnBreakEven = new JButton("수정");
		btnBreakEven.setBounds(270, 465, 80, 25);
		panel.add(btnAudience);

		// 손익분기점도달 수정
		btnBreakEven.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				editInput("손익분기점도달");
			}
		});

		JLabel naverTag = new JLabel("네이버(/5.0)");
		naverTag.setBounds(13, 495, 105, 25);
		panel.add(naverTag);

		JLabel naverResult = new JLabel(naver);
		naverResult.setBounds(125, 495, 125, 25);
		panel.add(naverResult);

		JButton btnNaver = new JButton("수정");
		btnNaver.setBounds(270, 495, 80, 25);
		panel.add(btnNaver);

		// 네이버 수정
		btnNaver.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				editInput("네이버");
			}
		});

		JLabel rottenTag = new JLabel("로튼토마토(/100)");
		rottenTag.setBounds(13, 525, 105, 25);
		panel.add(rottenTag);

		JLabel rottenResult = new JLabel(rotten);
		rottenResult.setBounds(125, 525, 125, 25);
		panel.add(rottenResult);

		JButton btnRotten = new JButton("수정");
		btnRotten.setBounds(270, 525, 80, 25);
		panel.add(btnRotten);

		// 로튼토마토 수정
		btnRotten.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				editInput("로튼토마토");
			}
		});

		JLabel watchaTag = new JLabel("왓챠(/5.0)");
		watchaTag.setBounds(13, 555, 105, 25);
		panel.add(watchaTag);

		JLabel watchaResult = new JLabel(watchaPlay);
		watchaResult.setBounds(125, 555, 125, 25);
		panel.add(watchaResult);

		JButton btnWatcha = new JButton("수정");
		btnWatcha.setBounds(270, 555, 80, 25);
		panel.add(btnWatcha);

		// 왓챠 수정
		btnWatcha.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				editInput("왓챠");
			}
		});

		JLabel realNameTag = new JLabel("배우이름");
		realNameTag.setBounds(13, 585, 105, 25);
		panel.add(realNameTag);

		JLabel realNameResult = new JLabel(realName);
		realNameResult.setBounds(125, 585, 125, 25);
		panel.add(realNameResult);

		JButton btnRealName = new JButton("수정");
		btnRealName.setBounds(270, 585, 80, 25);
		panel.add(btnRealName);

		// 배우이름 수정
		btnRealName.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				editInput("배우이름");
			}
		});

		JLabel actBirthTag = new JLabel("생년월일");
		actBirthTag.setBounds(13, 615, 105, 25);
		panel.add(actBirthTag);

		JLabel actBirthResult = new JLabel(actBirth);
		actBirthResult.setBounds(125, 615, 125, 25);
		panel.add(actBirthResult);

		JButton btnActBirth = new JButton("수정");
		btnActBirth.setBounds(270, 615, 80, 25);
		panel.add(btnActBirth);

		// 생년월일 수정
		btnActBirth.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				editInput("생년월일");
			}
		});

		JLabel actNameTag = new JLabel("배역이름");
		actNameTag.setBounds(13, 645, 105, 25);
		panel.add(actNameTag);

		JLabel actNameResult = new JLabel(actName);
		actNameResult.setBounds(125, 645, 125, 25);
		panel.add(actNameResult);

		JButton btnActName = new JButton("수정");
		btnActName.setBounds(270, 645, 80, 25);
		panel.add(btnActName);

		// 배역이름 수정
		btnActName.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				editInput("배역이름");
			}
		});

		JLabel genderTag = new JLabel("성별");
		genderTag.setBounds(13, 675, 105, 25);
		panel.add(genderTag);

		JLabel genderResult = new JLabel(gender);
		genderResult.setBounds(125, 675, 125, 25);
		panel.add(genderResult);

		JButton btnGender = new JButton("수정");
		btnGender.setBounds(270, 675, 80, 25);
		panel.add(btnGender);

		// 성별 수정
		btnGender.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				editInput("성별");
			}
		});

		JLabel careerTag = new JLabel("직업");
		careerTag.setBounds(13, 705, 105, 25);
		panel.add(careerTag);

		JLabel careerResult = new JLabel(career);
		careerResult.setBounds(125, 705, 125, 25);
		panel.add(careerResult);

		JButton btnCareer = new JButton("수정");
		btnCareer.setBounds(270, 705, 80, 25);
		panel.add(btnCareer);

		// 직업 수정
		btnCareer.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				editInput("직업");
			}
		});

	}

	public void editInput(String attr) {

		frame.setSize(400, 300);
		frame.setLocation(470, 50);
		frame.setLayout(null);
		frame.setVisible(true);

		JLabel edit = new JLabel("수정할 내용을 입력해주세요.");
		edit.setBounds(100, 20, 320, 50);
		frame.add(edit);

		// 수정할 내용 입력값으로 받아오기
		JTextField input = new JTextField();
		input.setBounds(100, 70, 160, 50);
		frame.add(input);

		JButton btnFinish = new JButton("완료");
		btnFinish.setBounds(100, 150, 80, 25);
		frame.add(btnFinish);

		btnFinish.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				edit(attr, input.getText());
			}
		});

	}

	public void edit(String attr, String input) {

		String query = null;
		PreparedStatement pstmt = null;

		try {
			// 해당되는 attribute 의 값 수정
			switch (attr) {

			case "제목":
				query = "UPDATE DB2021_영화 SET 제목=? WHERE 제목=? AND 감독=?";
				pstmt = conn.prepareStatement(query);
				pstmt.setString(1, input);
				pstmt.setString(2, updateMovie);
				pstmt.setString(3, updateDirector);
				pstmt.executeUpdate();
				break;

			case "감독":
				query = "UPDATE DB2021_영화 SET 감독=? WHERE 제목=? AND 감독=?";
				pstmt = conn.prepareStatement(query);
				pstmt.setString(1, input);
				pstmt.setString(2, updateMovie);
				pstmt.setString(3, updateDirector);
				pstmt.executeUpdate();
				break;

			case "장르":
				query = "UPDATE DB2021_영화 SET 장르=? WHERE 제목=? AND 감독=?";
				pstmt = conn.prepareStatement(query);
				pstmt.setString(1, input);
				pstmt.setString(2, updateMovie);
				pstmt.setString(3, updateDirector);
				pstmt.executeUpdate();
				break;

			case "줄거리":
				query = "UPDATE DB2021_영화 SET 줄거리=? WHERE 제목=? AND 감독=?";
				pstmt = conn.prepareStatement(query);
				pstmt.setString(1, input);
				pstmt.setString(2, updateMovie);
				pstmt.setString(3, updateDirector);
				pstmt.executeUpdate();
				break;

			case "대표배우":
				query = "UPDATE DB2021_영화 SET 대표_배우=? WHERE 제목=? AND 감독=?";
				pstmt = conn.prepareStatement(query);
				pstmt.setString(1, input);
				pstmt.setString(2, updateMovie);
				pstmt.setString(3, updateDirector);
				pstmt.executeUpdate();
				break;

			case "개봉날짜":
				query = "UPDATE DB2021_영화 SET 개봉_날짜=? WHERE 제목=? AND 감독=?";
				pstmt = conn.prepareStatement(query);
				pstmt.setString(1, input);
				pstmt.setString(2, updateMovie);
				pstmt.setString(3, updateDirector);
				pstmt.executeUpdate();
				break;

			case "국가":
				query = "UPDATE DB2021_영화 SET 국가=? WHERE 제목=? AND 감독=?";
				pstmt = conn.prepareStatement(query);
				pstmt.setString(1, input);
				pstmt.setString(2, updateMovie);
				pstmt.setString(3, updateDirector);
				pstmt.executeUpdate();
				break;

			case "러닝타임":
				query = "UPDATE DB2021_영화 SET 러닝_타임=? WHERE 제목=? AND 감독=?";
				pstmt = conn.prepareStatement(query);
				pstmt.setInt(1, Integer.parseInt(input));
				pstmt.setString(2, updateMovie);
				pstmt.setString(3, updateDirector);
				pstmt.executeUpdate();
				break;

			case "현재상영":
				query = "UPDATE DB2021_영화 SET 현재_상영=? WHERE 제목=? AND 감독=?";
				pstmt = conn.prepareStatement(query);
				pstmt.setBoolean(1, Boolean.parseBoolean(input));
				pstmt.setString(2, updateMovie);
				pstmt.setString(3, updateDirector);
				pstmt.executeUpdate();
				break;

			case "DVD제공":
				query = "UPDATE DB2021_유통 SET DVD_제공=? WHERE 제목=? AND 감독=?";
				pstmt = conn.prepareStatement(query);
				pstmt.setBoolean(1, Boolean.parseBoolean(input));
				pstmt.setString(2, updateMovie);
				pstmt.setString(3, updateDirector);
				pstmt.executeUpdate();
				break;

			case "배급사":
				query = "UPDATE DB2021_유통 SET 배급사=? WHERE 제목=? AND 감독=?";
				pstmt = conn.prepareStatement(query);
				pstmt.setString(1, input);
				pstmt.setString(2, updateMovie);
				pstmt.setString(3, updateDirector);
				pstmt.executeUpdate();
				break;

			case "매출":
				query = "UPDATE DB2021_수익 SET 매출=? WHERE 제목=? AND 감독=?";
				pstmt = conn.prepareStatement(query);
				pstmt.setLong(1, Long.parseLong(input));
				pstmt.setString(2, updateMovie);
				pstmt.setString(3, updateDirector);
				pstmt.executeUpdate();
				break;

			case "관객수":
				query = "UPDATE DB2021_수익 SET 관객_수=? WHERE 제목=? AND 감독=?";
				pstmt = conn.prepareStatement(query);
				pstmt.setLong(1, Long.parseLong(input));
				pstmt.setString(2, updateMovie);
				pstmt.setString(3, updateDirector);
				pstmt.executeUpdate();
				break;

			case "손익분기점도달":
				query = "UPDATE DB2021_수익 SET 손익분기점_도달=? WHERE 제목=? AND 감독=?";
				pstmt = conn.prepareStatement(query);
				pstmt.setBoolean(1, Boolean.parseBoolean(input));
				pstmt.setString(2, updateMovie);
				pstmt.setString(3, updateDirector);
				pstmt.executeUpdate();
				break;

			case "네이버":
				query = "UPDATE DB2021_평점 SET 네이버=? WHERE 제목=? AND 감독=?";
				pstmt = conn.prepareStatement(query);
				pstmt.setFloat(1, Float.parseFloat(input));
				pstmt.setString(2, updateMovie);
				pstmt.setString(3, updateDirector);
				pstmt.executeUpdate();
				break;

			case "로튼토마토":
				query = "UPDATE DB2021_평점 SET 로튼토마토=? WHERE 제목=? AND 감독=?";
				pstmt = conn.prepareStatement(query);
				pstmt.setInt(1, Integer.parseInt(input));
				pstmt.setString(2, updateMovie);
				pstmt.setString(3, updateDirector);
				pstmt.executeUpdate();
				break;

			case "왓챠":
				query = "UPDATE DB2021_평점 SET 왓챠=? WHERE 제목=? AND 감독=?";
				pstmt = conn.prepareStatement(query);
				pstmt.setFloat(1, Float.parseFloat(input));
				pstmt.setString(2, updateMovie);
				pstmt.setString(3, updateDirector);
				pstmt.executeUpdate();
				break;

			case "배우이름":
				query = "UPDATE DB2021_배우 SET 이름=? WHERE 제목=? AND 감독=?";
				pstmt = conn.prepareStatement(query);
				pstmt.setString(1, input);
				pstmt.setString(2, updateMovie);
				pstmt.setString(3, updateDirector);
				pstmt.executeUpdate();
				break;

			case "생년월일":
				query = "UPDATE DB2021_배우 SET 생년월일=? WHERE 제목=? AND 감독=?";
				pstmt = conn.prepareStatement(query);
				pstmt.setString(1, input);
				pstmt.setString(2, updateMovie);
				pstmt.setString(3, updateDirector);
				pstmt.executeUpdate();
				break;

			case "배역이름":
				query = "UPDATE DB2021_배우 SET 배역_이름=? WHERE 제목=? AND 감독=?";
				pstmt = conn.prepareStatement(query);
				pstmt.setString(1, input);
				pstmt.setString(2, updateMovie);
				pstmt.setString(3, updateDirector);
				pstmt.executeUpdate();
				break;

			case "성별":
				query = "UPDATE DB2021_배우 SET 성별=? WHERE 제목=? AND 감독=?";
				pstmt = conn.prepareStatement(query);
				pstmt.setString(1, input);
				pstmt.setString(2, updateMovie);
				pstmt.setString(3, updateDirector);
				pstmt.executeUpdate();
				break;

			case "직업":
				query = "UPDATE DB2021_배우 SET 직업=? WHERE 제목=? AND 감독=?";
				pstmt = conn.prepareStatement(query);
				pstmt.setString(1, input);
				pstmt.setString(2, updateMovie);
				pstmt.setString(3, updateDirector);
				pstmt.executeUpdate();
				break;

			}

			JOptionPane.showMessageDialog(null, "영화 수정이 완료되었습니다\n 창을 닫아주세요.");

		} catch (SQLException se) {
			System.out.println("에러!");
		}
	}

	// 스트리밍 테이블의 경우 checkbox로 수정
	public void editInputService() {

		frameService.setSize(400, 300);
		frameService.setLocation(470, 50);
		frameService.setLayout(null);
		frameService.setVisible(true);

		JLabel edit = new JLabel("지원되는 스트리밍 서비스를 선택해주세요.");
		edit.setBounds(100, 20, 320, 50);
		frameService.add(edit);

		netflix = new JCheckBox("넷플릭스");
		watcha = new JCheckBox("왓챠");
		wave = new JCheckBox("웨이브");
		tving = new JCheckBox("티빙");

		netflix.setBounds(100, 90, 80, 25);
		watcha.setBounds(100, 140, 80, 25);
		wave.setBounds(200, 90, 80, 25);
		tving.setBounds(200, 140, 80, 25);
		frameService.add(netflix);
		frameService.add(watcha);
		frameService.add(wave);
		frameService.add(tving);

		JButton btnFinish = new JButton("완료");
		btnFinish.setBounds(200, 200, 80, 25);
		frameService.add(btnFinish);

		btnFinish.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				editService();
				JOptionPane.showMessageDialog(null, "영화 수정이 완료되었습니다\n 창을 닫아주세요.");
			}
		});

	}

	// 서비스 테이블 내용 수정 (반영)
	public void editService() {

		PreparedStatement deleteAll = null;
		PreparedStatement insertAll = null;

		String deleteQuery = "DELETE FROM DB2021_스트리밍 WHERE 제목=? AND 감독=?";
		String insertService = "INSERT INTO DB2021_스트리밍 VALUES (?,?,?)";

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

		try {

			conn.setAutoCommit(false);

			deleteAll = conn.prepareStatement(deleteQuery);
			deleteAll.setString(1, updateMovie);
			deleteAll.setString(2, updateDirector);
			deleteAll.executeUpdate();

			insertAll = conn.prepareStatement(insertService);
			for (int i = 0; i < stream.length; i++) {
				if (stream[i] != null) {
					insertAll.setString(1, updateMovie);
					insertAll.setString(2, updateDirector);
					insertAll.setString(3, stream[i]);
					insertAll.executeUpdate();
				}

			}
			conn.commit();

			conn.setAutoCommit(true);

		} catch (SQLException e) {
			System.out.println("에러!");
		}

	}

}