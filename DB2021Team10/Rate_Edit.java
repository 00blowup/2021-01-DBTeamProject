package DB2021Team10;

import java.sql.*;
import java.util.InputMismatchException;

import javax.swing.*;
import java.awt.event.*;

public class Rate_Edit extends JFrame {

//checkDB에서 쓸 String 변수
	String checkQuery;

	Connection conn;
	Statement stmt;

	private JPanel panelOne = new JPanel();
	private JPanel panelTwo = new JPanel();
	private JTextField movieText, directorText;
	private JButton btnFinish; // 완료 버튼
	private JRadioButton mood1, mood2, mood3, mood4, weather1, weather2, weather3, weather4, withWho1, withWho2,
			withWho3, withWho4;
	private String moodResult, weatherResult, withWhoResult;
	private String updateMovie, updateDirector, name, num, grade, review, similarMovie, similarDirector,
			recommendFeeling, recommendWeather, recommendRelationship;
	private String ID = Login.myID;

	public Rate_Edit(Connection conn, Statement stmt) {

		this.conn = conn;
		this.stmt = stmt;
		setTitle("평가 수정하기");
		setSize(370, 420);
		setLocation(450, 0);

		editRatePanel(panelOne);
		add(panelOne);

		setVisible(true);

	}

	// 수정할 평가의 영화 제목과 감독 입력 받기
	public void editRatePanel(JPanel panel) {

		panel.setLayout(null);

		JLabel editMovie = new JLabel("제목");
		editMovie.setBounds(50, 20, 80, 25);
		panel.add(editMovie);

		movieText = new JTextField(40);
		movieText.setBounds(165, 20, 130, 25);
		panel.add(movieText);

		JLabel editDirector = new JLabel("감독");
		editDirector.setBounds(50, 50, 80, 25);
		panel.add(editDirector);

		directorText = new JTextField(40);
		directorText.setBounds(165, 50, 130, 25);
		panel.add(directorText);

		JButton btnUpdate = new JButton("수정하기");
		btnUpdate.setBounds(195, 80, 100, 25);
		panel.add(btnUpdate);

		btnUpdate.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				updateMovie = movieText.getText();
				updateDirector = directorText.getText();

				// 입력한 제목, 감독에 해당하는 영화의 평가를 한 적이 있는지 체크
				int checkedResult = checkDB(updateMovie, updateDirector);

				// 해당 영화의 평가를 쓴 적이 있다면(즉 평가 수정이 가능하다면) 수정을 수행
				if (checkedResult == 1) {
					editRate();
				} else if (checkedResult == 0) {
					// 그렇지 않다면 경고메시지 띄우기
					JOptionPane.showMessageDialog(null, "해당 영화의 평가를 쓰신 적이 없거나, 입력이 잘못되었습니다");

				}
			}
		});

	}

	// 입력한 영화가 DB에 존재하는지 확인하는 메소드
	public int checkDB(String title, String director) {
		int result = 0;
		try {
			// 로그인 시에 입력한 학번 값을 받아온다.
			String ID = Login.myID;

			// 본인이 평가를 쓴 적이 있는 모든 영화들의 제목과 감독값을 받아와, ResultSet checkedrs에 저장
			checkQuery = "select 제목, 감독 from DB2021_평가 where 학번 = ?";
			PreparedStatement checkDB = conn.prepareStatement(checkQuery);
			checkDB.setString(1, ID);
			ResultSet checkedrs = checkDB.executeQuery();

			// 수정을 위해 입력한 제목과 감독의 값을, checkedrs의 값들과 비교
			while (checkedrs.next()) {
				if ((checkedrs.getString("제목").equals(updateMovie))
						&& (checkedrs.getString("감독").equals(updateDirector))) {
					// 입력한 제목, 감독 값에 해당되는 본인의 평가가 존재하면 result값을 1로 설정하고 while문을 종료
					result = 1;
					break;
				} else
					continue;
			}
			// 입력한 제목, 감독 값에 해당되는 본인의 평가가 없다면 result값이 0인 채로 while문이 종료됨

		} catch (SQLException e) {
			System.out.print("checkDB 에러!");
		}

		// result를 리턴
		return result;
	}

	// 기존에 있는 평가 내용 불러오기
	public void editRate() {

		PreparedStatement rate = null;
		ResultSet rsRate = null;

		String lastRate = "SELECT 이름, 기수, 평점, 평론, 비슷한_영화_제목, 비슷한_영화의_감독, 추천_감정, 추천_날씨, 추천_관계 FROM DB2021_평가 WHERE 제목=? and 감독=? and 학번=?";

		// 위에서 입력한 값이 없을 경우, 요청 후 return
		if (updateMovie.length() < 1 || updateDirector.length() < 1) {
			JOptionPane.showMessageDialog(null, "삭제할 영화 제목과 감독은 필수 입력!");
			movieText.setText("");
			directorText.setText("");
			return;
		}

		try {
			rate = conn.prepareStatement(lastRate);
			rate.setString(1, updateMovie);
			rate.setString(2, updateDirector);
			rate.setString(3, ID);
			rsRate = rate.executeQuery();

			if (rsRate.next()) {
				name = rsRate.getString("이름");
				num = Integer.toString(rsRate.getInt("기수"));
				grade = Float.toString(rsRate.getFloat("평점"));
				review = rsRate.getString("평론");
				similarMovie = rsRate.getString("비슷한_영화_제목");
				similarDirector = rsRate.getString("비슷한_영화의_감독");
				recommendFeeling = rsRate.getString("추천_감정");
				recommendWeather = rsRate.getString("추천_날씨");
				recommendRelationship = rsRate.getString("추천_관계");

			}

			panelOne.setVisible(false);
			editRatePanelTwo(panelTwo);
			add(panelTwo);
			panelTwo.setVisible(true);

		} catch (SQLException e) {
			System.out.print("에러!");
		}
	}

	// 불러온 내용 보여주기
	public void editRatePanelTwo(JPanel panel) {

		panel.setLayout(null);

		JLabel idTag = new JLabel("학번");
		idTag.setBounds(13, 15, 105, 25);
		panel.add(idTag);

		JLabel idResult = new JLabel(ID);
		idResult.setBounds(125, 15, 125, 25);
		panel.add(idResult);

		JLabel nameTag = new JLabel("이름");
		nameTag.setBounds(13, 45, 105, 25);
		panel.add(nameTag);

		JLabel nameResult = new JLabel(name);
		nameResult.setBounds(125, 45, 125, 25);
		panel.add(nameResult);

		JLabel numTag = new JLabel("기수");
		numTag.setBounds(13, 75, 105, 25);
		panel.add(numTag);

		JLabel numResult = new JLabel(num);
		numResult.setBounds(125, 75, 125, 25);
		panel.add(numResult);

		JLabel titleTag = new JLabel("제목");
		titleTag.setBounds(13, 105, 105, 25);
		panel.add(titleTag);

		JLabel titleResult = new JLabel(updateMovie);
		titleResult.setBounds(125, 105, 125, 25);
		panel.add(titleResult);

		JLabel directorTag = new JLabel("감독");
		directorTag.setBounds(13, 135, 105, 25);
		panel.add(directorTag);

		JLabel directorResult = new JLabel(updateDirector);
		directorResult.setBounds(125, 135, 125, 25);
		panel.add(directorResult);

		// 아래부터 수정 가능함
		JLabel gradeTag = new JLabel("평점");
		gradeTag.setBounds(13, 165, 105, 25);
		panel.add(gradeTag);

		JLabel gradeResult = new JLabel(grade);
		gradeResult.setBounds(125, 165, 125, 25);
		panel.add(gradeResult);

		JButton btnGrade = new JButton("수정");
		btnGrade.setBounds(270, 165, 80, 25);
		panel.add(btnGrade);

		// 평점 수정
		btnGrade.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				editRateInput("평점");
			}
		});

		JLabel reviewTag = new JLabel("평론");
		reviewTag.setBounds(13, 195, 105, 25);
		panel.add(reviewTag);

		JLabel reviewResult = new JLabel(review);
		reviewResult.setBounds(125, 195, 125, 25);
		panel.add(reviewResult);

		JButton btnReview = new JButton("수정");
		btnReview.setBounds(270, 195, 80, 25);
		panel.add(btnReview);

		// 평론 수정
		btnReview.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				editRateInput("평론");
			}
		});

		JLabel similarMovieTag = new JLabel("비슷한 영화 제목");
		similarMovieTag.setBounds(13, 225, 105, 25);
		panel.add(similarMovieTag);

		JLabel similarMovieResult = new JLabel(similarMovie);
		similarMovieResult.setBounds(125, 225, 125, 25);
		panel.add(similarMovieResult);

		JButton btnSimilarMovie = new JButton("수정");
		btnSimilarMovie.setBounds(270, 225, 80, 25);
		panel.add(btnSimilarMovie);

		// 비슷한영화제목 수정
		btnSimilarMovie.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				editRateInput("비슷한영화제목");
			}
		});

		JLabel similarDirectorTag = new JLabel("비슷한 영화의 감독");
		similarDirectorTag.setBounds(13, 255, 105, 25);
		panel.add(similarDirectorTag);

		JLabel similarDirectorResult = new JLabel(similarDirector);
		similarDirectorResult.setBounds(125, 255, 125, 25);
		panel.add(similarDirectorResult);

		JButton btnSimilarDirector = new JButton("수정");
		btnSimilarDirector.setBounds(270, 255, 80, 25);
		panel.add(btnSimilarDirector);

		// 비슷한영화의감독 수정
		btnSimilarDirector.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				editRateInput("비슷한영화의감독");
			}
		});

		JLabel recommendFeelingTag = new JLabel("추천 감정");
		recommendFeelingTag.setBounds(13, 285, 105, 25);
		panel.add(recommendFeelingTag);

		JLabel recommendFeelingResult = new JLabel(recommendFeeling);
		recommendFeelingResult.setBounds(125, 285, 125, 25);
		panel.add(recommendFeelingResult);

		JButton btnRecommendFeeling = new JButton("수정");
		btnRecommendFeeling.setBounds(270, 285, 80, 25);
		panel.add(btnRecommendFeeling);

		// 추천감정 수정
		btnRecommendFeeling.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				editInputMood();
			}
		});

		JLabel recommendWeatherTag = new JLabel("추천 날씨");
		recommendWeatherTag.setBounds(13, 315, 105, 25);
		panel.add(recommendWeatherTag);

		JLabel recommendWeatherResult = new JLabel(recommendWeather);
		recommendWeatherResult.setBounds(125, 315, 125, 25);
		panel.add(recommendWeatherResult);

		JButton btnRecommendWeather = new JButton("수정");
		btnRecommendWeather.setBounds(270, 315, 80, 25);
		panel.add(btnRecommendWeather);

		// 추천날씨 수정
		btnRecommendWeather.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				editInputWeather();
			}
		});

		JLabel recommendRelationshipTag = new JLabel("추천 관계");
		recommendRelationshipTag.setBounds(13, 345, 105, 25);
		panel.add(recommendRelationshipTag);

		JLabel recommendRelationshipResult = new JLabel(recommendRelationship);
		recommendRelationshipResult.setBounds(125, 345, 125, 25);
		panel.add(recommendRelationshipResult);

		JButton btnRecommendRelationship = new JButton("수정");
		btnRecommendRelationship.setBounds(270, 345, 80, 25);
		panel.add(btnRecommendRelationship);

		// 추천관계 수정
		btnRecommendRelationship.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				editInputWithWho();
			}
		});

	}

	// 수정 정보 입력 받는 frame
	public void editRateInput(String attr) {
		JFrame frame = new JFrame();

		frame.setSize(400, 300);
		frame.setLocation(470, 50);
		frame.setLayout(null);
		frame.setVisible(true);

		JLabel edit = new JLabel("수정할 내용을 입력해주세요.");
		edit.setBounds(100, 20, 320, 50);
		frame.add(edit);

		// 수정 내용 입력 받기
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
				frame.dispose();
			}
		});

	}

	// 입력 받은 값 DB에 업데이트(반영)
	public void edit(String attr, String input) {

		String query = null;
		PreparedStatement pstmt = null;

		try {
			switch (attr) {
			case "평점":
				query = "UPDATE DB2021_평가 SET 평점=? WHERE 제목=? AND 감독=? AND 학번=?";
				pstmt = conn.prepareStatement(query);
				pstmt.setFloat(1, Float.parseFloat(input));
				pstmt.setString(2, updateMovie);
				pstmt.setString(3, updateDirector);
				pstmt.setString(4, ID);
				pstmt.executeUpdate();
				break;

			case "평론":
				query = "UPDATE DB2021_평가 SET 평론=? WHERE 제목=? AND 감독=? AND 학번=?";
				pstmt = conn.prepareStatement(query);
				pstmt.setString(1, input);
				pstmt.setString(2, updateMovie);
				pstmt.setString(3, updateDirector);
				pstmt.setString(4, ID);
				pstmt.executeUpdate();
				break;

			case "비슷한영화제목":
				query = "UPDATE DB2021_평가 SET 비슷한_영화_제목=? WHERE 제목=? AND 감독=? AND 학번=?";
				pstmt = conn.prepareStatement(query);
				pstmt.setString(1, input);
				pstmt.setString(2, updateMovie);
				pstmt.setString(3, updateDirector);
				pstmt.setString(4, ID);
				pstmt.executeUpdate();
				break;

			case "비슷한영화의감독":
				query = "UPDATE DB2021_평가 SET 비슷한_영화의_감독=? WHERE 제목=? AND 감독=? AND 학번=?";
				pstmt = conn.prepareStatement(query);
				pstmt.setString(1, input);
				pstmt.setString(2, updateMovie);
				pstmt.setString(3, updateDirector);
				pstmt.setString(4, ID);
				pstmt.executeUpdate();
				break;
			}

			JOptionPane.showMessageDialog(null, "평가 수정이 완료되었습니다\n 창을 닫아주세요.");

		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "입력 형식이 틀렸습니다.");
		} catch (NumberFormatException sen) {
			JOptionPane.showMessageDialog(null, "입력 형식에 문제가 있습니다.");
		}

	}

	// 추천 감정 수정 값 입력 받을 frame
	public void editInputMood() {
		JFrame frameMood = new JFrame();

		mood1 = new JRadioButton("심심할 때");
		mood2 = new JRadioButton("우울할 때");
		mood3 = new JRadioButton("잠이 안 올 때");
		mood4 = new JRadioButton("설레고 싶을 때");
		ButtonGroup moodGroup = new ButtonGroup();

		frameMood.setSize(400, 300);
		frameMood.setLocation(470, 50);
		frameMood.setLayout(null);
		frameMood.setVisible(true);

		JLabel edit = new JLabel("추천 감정을 선택해 주세요");
		edit.setBounds(100, 20, 320, 50);
		frameMood.add(edit);

		mood1.setBounds(100, 90, 80, 25);
		mood2.setBounds(100, 140, 80, 25);
		mood3.setBounds(200, 90, 80, 25);
		mood4.setBounds(200, 140, 80, 25);

		frameMood.add(mood1);
		frameMood.add(mood2);
		frameMood.add(mood3);
		frameMood.add(mood4);

		moodGroup.add(mood1);
		moodGroup.add(mood2);
		moodGroup.add(mood3);
		moodGroup.add(mood4);

		btnFinish = new JButton("완료");
		btnFinish.setBounds(200, 200, 80, 25);
		frameMood.add(btnFinish);

		btnFinish.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				editMood();
				frameMood.dispose();
			}
		});

	}

	// 입력 받은 추천 감정 값 DB에 업데이트(반영)
	public void editMood() {

		PreparedStatement pstmt = null;

		String query = "UPDATE DB2021_평가 SET 추천_감정=? WHERE 제목=? AND 감독=? AND 학번=?";

		if (mood1.isSelected()) {
			moodResult = "심심할 때";
		} else if (mood2.isSelected()) {
			moodResult = "우울할 때";
		} else if (mood3.isSelected()) {
			moodResult = "잠이 안 올 때";
		} else if (mood4.isSelected()) {
			moodResult = "설레고 싶을 때";
		}

		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, moodResult);
			pstmt.setString(2, updateMovie);
			pstmt.setString(3, updateDirector);
			pstmt.setString(4, ID);
			pstmt.executeUpdate();
			JOptionPane.showMessageDialog(null, "정보 수정이 완료되었습니다\n 창을 닫아주세요.");
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "입력 중 에러가 발생하였습니다.");
		}

	}

	// 추천 날씨 수정 값 입력 받을 frame
	public void editInputWeather() {
		JFrame frameWeather = new JFrame();

		weather1 = new JRadioButton("맑은 날");
		weather2 = new JRadioButton("흐린 날");
		weather3 = new JRadioButton("눈 오는 날");
		weather4 = new JRadioButton("비 오는 날");
		ButtonGroup weatherGroup = new ButtonGroup();

		frameWeather.setSize(400, 300);
		frameWeather.setLocation(470, 50);
		frameWeather.setLayout(null);
		frameWeather.setVisible(true);

		JLabel edit = new JLabel("추천 날씨를 선택해 주세요");
		edit.setBounds(100, 20, 320, 50);
		frameWeather.add(edit);

		weather1.setBounds(100, 90, 80, 25);
		weather2.setBounds(100, 140, 80, 25);
		weather3.setBounds(200, 90, 80, 25);
		weather4.setBounds(200, 140, 80, 25);

		frameWeather.add(weather1);
		frameWeather.add(weather2);
		frameWeather.add(weather3);
		frameWeather.add(weather4);

		weatherGroup.add(weather1);
		weatherGroup.add(weather2);
		weatherGroup.add(weather3);
		weatherGroup.add(weather4);

		btnFinish = new JButton("완료");
		btnFinish.setBounds(200, 200, 80, 25);
		frameWeather.add(btnFinish);

		btnFinish.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				editWeather();
				frameWeather.dispose();
			}
		});

	}

	// 입력 받은 추천 날씨 값 DB에 업데이트(반영)
	public void editWeather() {

		PreparedStatement pstmt = null;

		String query = "UPDATE DB2021_평가 SET 추천_날씨=? WHERE 제목=? AND 감독=? AND 학번=?";

		if (weather1.isSelected()) {
			weatherResult = "맑은 날";
		} else if (weather2.isSelected()) {
			weatherResult = "흐린 날";
		} else if (weather3.isSelected()) {
			weatherResult = "눈 오는 날";
		} else if (weather4.isSelected()) {
			weatherResult = "비 오는 날";
		}

		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, weatherResult);
			pstmt.setString(2, updateMovie);
			pstmt.setString(3, updateDirector);
			pstmt.setString(4, ID);
			pstmt.executeUpdate();
			JOptionPane.showMessageDialog(null, "정보 수정이 완료되었습니다\n 창을 닫아주세요.");
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "입력 중 에러가 발생하였습니다.");
		}

	}

	// 추천 관계 수정 값 입력 받을 frame
	public void editInputWithWho() {
		JFrame frameWithWho = new JFrame();

		withWho1 = new JRadioButton("혼자");
		withWho2 = new JRadioButton("연인");
		withWho3 = new JRadioButton("가족");
		withWho4 = new JRadioButton("친구");
		ButtonGroup withWhoGroup = new ButtonGroup();

		frameWithWho.setSize(400, 300);
		frameWithWho.setLocation(470, 50);
		frameWithWho.setLayout(null);
		frameWithWho.setVisible(true);

		JLabel edit = new JLabel("추천 관계를 선택해 주세요");
		edit.setBounds(100, 20, 320, 50);
		frameWithWho.add(edit);

		withWho1.setBounds(100, 90, 80, 25);
		withWho2.setBounds(100, 140, 80, 25);
		withWho3.setBounds(200, 90, 80, 25);
		withWho4.setBounds(200, 140, 80, 25);

		frameWithWho.add(withWho1);
		frameWithWho.add(withWho2);
		frameWithWho.add(withWho3);
		frameWithWho.add(withWho4);

		withWhoGroup.add(withWho1);
		withWhoGroup.add(withWho2);
		withWhoGroup.add(withWho3);
		withWhoGroup.add(withWho4);

		btnFinish = new JButton("완료");
		btnFinish.setBounds(200, 200, 80, 25);
		frameWithWho.add(btnFinish);

		btnFinish.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				editWithWho();
				frameWithWho.dispose();
			}
		});

	}

	// 입력 받은 추천 관계 값 DB에 업데이트(반영)
	public void editWithWho() {

		PreparedStatement pstmt = null;

		String query = "UPDATE DB2021_평가 SET 추천_관계=? WHERE 제목=? AND 감독=? AND 학번=?";

		if (withWho1.isSelected()) {
			withWhoResult = "혼자";
		} else if (withWho2.isSelected()) {
			withWhoResult = "연인";
		} else if (withWho3.isSelected()) {
			withWhoResult = "가족";
		} else if (withWho4.isSelected()) {
			withWhoResult = "친구";
		}

		try {
			pstmt = conn.prepareStatement(query);
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, withWhoResult);
			pstmt.setString(2, updateMovie);
			pstmt.setString(3, updateDirector);
			pstmt.setString(4, ID);
			pstmt.executeUpdate();
			JOptionPane.showMessageDialog(null, "정보 수정이 완료되었습니다\n 창을 닫아주세요.");
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "입력 중 에러가 발생하였습니다.");
		}

	}

}