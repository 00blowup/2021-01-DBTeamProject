package DB2021Team10;

import java.sql.*;
import java.util.InputMismatchException;

import javax.swing.*;
import java.awt.event.*;

public class Rate_Insert extends JFrame {

	Connection conn;
	Statement stmt;

	private JPanel panel1 = new JPanel();
	private JPanel panel = new JPanel();
	private String ID = Login.myID;
	private String name, num, updateMovie, updateDirector, feelingResult, weatherResult, relationshipResult;
	private JTextField titleText, directorText, gradeText, reviewText, similarMovieText, similarDirectorText;

	public Rate_Insert(Connection conn, Statement stmt) {
		this.conn = conn;
		setTitle("평가 추가하기");
		setSize(340, 460);
		setResizable(false);
		setLocation(450, 200);
		
		insertBefore();
		setVisible(true);
	}

	// 부원 테이블에서 기존 정보 가져오기 (이름, 기수) 
	public void insertBefore() {

		PreparedStatement pstmt = null;
		ResultSet rs = null;

		String callMemInfo = "SELECT 이름, 기수 FROM DB2021_부원 WHERE 학번=?";

		try {
			pstmt = conn.prepareStatement(callMemInfo);
			pstmt.setString(1, ID);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				name = rs.getString("이름");
				num = Integer.toString(rs.getInt("기수"));
			}

		} catch (SQLException e) {
			System.out.println("에러!");
		}

		insertBeforeTwo(panel);
		add(panel);
		setVisible(true);

	}

	// 평가를 적고자 하는 영화의 데이터가 존재하는지 확인하기 위한 입력
	public void insertBeforeTwo(JPanel panel) {

		panel.setLayout(null);

		JLabel insertMovie = new JLabel("제목");
		insertMovie.setBounds(30, 8, 105, 25);
		panel.add(insertMovie);

		titleText = new JTextField(40);
		titleText.setBounds(150, 8, 130, 25);
		panel.add(titleText);

		JLabel insertDirector = new JLabel("감독");
		insertDirector.setBounds(30, 40, 80, 25);
		panel.add(insertDirector);

		directorText = new JTextField(40);
		directorText.setBounds(150, 40, 130, 25);
		panel.add(directorText);

		JButton btnCheck = new JButton("추가하기");
		btnCheck.setBounds(185, 80, 95, 25);
		panel.add(btnCheck);
		btnCheck.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				insertCheck();
			}
		});

	}

	// 입력값을 바탕으로 이후 진행이 가능한지 계산
	public void insertCheck() {

		PreparedStatement pstmt = null;
		PreparedStatement pstmtTwo = null;
		ResultSet rs = null;
		ResultSet rsTwo = null;

		updateMovie = titleText.getText();
		updateDirector = directorText.getText();

		String movie = null;
		String movieRate = null;

		// 필수 입력값을 입력하지 않았을 경우,
		if (updateMovie.length() < 1 || updateDirector.length() < 1) {
			JOptionPane.showMessageDialog(null, "영화 제목과 감독은 필수 입력!");
			titleText.setText("");
			directorText.setText("");
			return;
		}

		String sqlExistMovie = "SELECT 제목, 감독 FROM DB2021_영화 WHERE 감독=?";
		String sqlExistRate = "SELECT 제목, 감독 FROM DB2021_평가 WHERE 감독=? AND 학번=?";

		try {
			pstmt = conn.prepareStatement(sqlExistMovie);
			pstmt.setString(1, updateDirector);
			rs = pstmt.executeQuery();

			pstmtTwo = conn.prepareStatement(sqlExistRate);
			pstmtTwo.setString(1, updateDirector);
			pstmtTwo.setString(2, ID);
			rsTwo = pstmtTwo.executeQuery();

			if (rsTwo.next()) {
				movieRate = rsTwo.getString("제목");

				if (movieRate.equals(updateMovie)) {
					// 이미 평가가 작성된 경우
					JOptionPane.showMessageDialog(null, "이미 평가가 존재합니다.");
					titleText.setText("");
					directorText.setText("");
					return;

				}
			} else {
				if (rs.next()) {
					movie = rs.getString("제목");

					if (movie.equals(updateMovie)) {
						JOptionPane.showMessageDialog(null, "영화 평가 입력을 시작합니다.");

						panel.setVisible(false);
						insertRatePanel(panel1);
						add(panel1);
						panel1.setVisible(true);

					}

				} else {
					// 영화 정보에 없는 영화일 경우,
					JOptionPane.showMessageDialog(null, "목록에 없는 영화입니다.\n 영화 메뉴로 돌아가 영화 정보를 추가해주세요.");
					titleText.setText("");
					directorText.setText("");

				}

			}

		} catch (SQLException e) {

			System.out.println("INSERT 구문 에러!");

		}

	}

	// 영화 평가 입력 받기
	public void insertRatePanel(JPanel panel) {

		panel.setLayout(null);

		JLabel idTag = new JLabel("학번");
		idTag.setBounds(10, 6, 105, 25);
		panel.add(idTag);

		JLabel idResult = new JLabel(ID);
		idResult.setBounds(120, 6, 125, 25);
		panel.add(idResult);

		JLabel nameTag = new JLabel("이름");
		nameTag.setBounds(10, 31, 105, 25);
		panel.add(nameTag);

		JLabel nameResult = new JLabel(name);
		nameResult.setBounds(120, 31, 125, 25);
		panel.add(nameResult);

		JLabel numTag = new JLabel("기수");
		numTag.setBounds(10, 56, 105, 25);
		panel.add(numTag);

		JLabel numResult = new JLabel(num);
		numResult.setBounds(120, 56, 125, 25);
		panel.add(numResult);

		JLabel titleTag = new JLabel("제목");
		titleTag.setBounds(10, 81, 105, 25);
		panel.add(titleTag);

		JLabel titleResult = new JLabel(updateMovie);
		titleResult.setBounds(120, 81, 125, 25);
		panel.add(titleResult);

		JLabel directorTag = new JLabel("감독");
		directorTag.setBounds(10, 106, 105, 25);
		panel.add(directorTag);

		JLabel directorResult = new JLabel(updateDirector);
		directorResult.setBounds(120, 106, 125, 25);
		panel.add(directorResult);

		JLabel gradeTag = new JLabel("평점(0.0)");
		gradeTag.setBounds(10, 131, 105, 25);
		panel.add(gradeTag);

		gradeText = new JTextField(40);
		gradeText.setBounds(120, 131, 135, 25);
		panel.add(gradeText);

		JLabel reviewTag = new JLabel("평론");
		reviewTag.setBounds(10, 156, 105, 25);
		panel.add(reviewTag);

		reviewText = new JTextField(40);
		reviewText.setBounds(120, 156, 135, 25);
		panel.add(reviewText);

		JLabel similarMovieTag = new JLabel("비슷한 영화 제목");
		similarMovieTag.setBounds(10, 181, 105, 25);
		panel.add(similarMovieTag);

		similarMovieText = new JTextField(40);
		similarMovieText.setBounds(120, 181, 135, 25);
		panel.add(similarMovieText);

		JLabel similarDirectorTag = new JLabel("비슷한 영화의 감독");
		similarDirectorTag.setBounds(10, 206, 105, 25);
		panel.add(similarDirectorTag);

		similarDirectorText = new JTextField(40);
		similarDirectorText.setBounds(120, 206, 135, 25);
		panel.add(similarDirectorText);

		JLabel recommendFeelingTag = new JLabel("추천 감정");
		recommendFeelingTag.setBounds(10, 231, 105, 25);
		panel.add(recommendFeelingTag);

		// 추천 감정은 radio button으로 구현
		JRadioButton bored = new JRadioButton("심심할 때");
		JRadioButton sad = new JRadioButton("우울할 때");
		JRadioButton tired = new JRadioButton("잠이 안 올 때");
		JRadioButton flutter = new JRadioButton("설레고 싶을 때");
		ButtonGroup feeling = new ButtonGroup();

		JLabel recommendFeeling = new JLabel();
		bored.setBounds(115, 231, 100, 25);
		sad.setBounds(215, 231, 120, 25);
		tired.setBounds(115, 256, 100, 25);
		flutter.setBounds(215, 256, 120, 25);
		recommendFeeling.setBounds(115, 231, 220, 50);
		panel.add(bored);
		panel.add(sad);
		panel.add(tired);
		panel.add(flutter);
		panel.add(recommendFeeling);
		bored.setSelected(true);
		feeling.add(bored);
		feeling.add(sad);
		feeling.add(tired);
		feeling.add(flutter);

		if (bored.isSelected()) {
			feelingResult = "심심할 때";
		} else if (sad.isSelected()) {
			feelingResult = "우울할 때";
		} else if (tired.isSelected()) {
			feelingResult = "잠이 안 올 때";
		} else if (flutter.isSelected()) {
			feelingResult = "설레고 싶을 때";
		}

		JLabel recommendWeatherTag = new JLabel("추천 날씨");
		recommendWeatherTag.setBounds(10, 285, 105, 25);
		panel.add(recommendWeatherTag);

		// 추천 날씨는 radio button으로 구현
		JRadioButton sunny = new JRadioButton("맑은 날");
		JRadioButton cloudy = new JRadioButton("흐린 날");
		JRadioButton snowy = new JRadioButton("눈 오는 날");
		JRadioButton rainy = new JRadioButton("비 오는 날");
		ButtonGroup weather = new ButtonGroup();

		JLabel recommendWeather = new JLabel();
		sunny.setBounds(115, 285, 100, 25);
		cloudy.setBounds(215, 285, 120, 25);
		snowy.setBounds(115, 310, 100, 25);
		rainy.setBounds(215, 310, 120, 25);
		recommendWeather.setBounds(115, 285, 220, 50);
		panel.add(sunny);
		panel.add(cloudy);
		panel.add(snowy);
		panel.add(rainy);
		panel.add(recommendWeather);
		sunny.setSelected(true);
		weather.add(sunny);
		weather.add(cloudy);
		weather.add(snowy);
		weather.add(rainy);

		if (sunny.isSelected()) {
			weatherResult = "맑은 날";
		} else if (cloudy.isSelected()) {
			weatherResult = "흐린 날";
		} else if (snowy.isSelected()) {
			weatherResult = "눈 오는 날";
		} else if (rainy.isSelected()) {
			weatherResult = "비 오는 날";
		}

		JLabel recommendRelationshipTag = new JLabel("추천 관계");
		recommendRelationshipTag.setBounds(10, 339, 105, 25);
		panel.add(recommendRelationshipTag);

		// 추천 관계는 radio button으로 구현
		JRadioButton alone = new JRadioButton("혼자");
		JRadioButton couple = new JRadioButton("연인");
		JRadioButton family = new JRadioButton("가족");
		JRadioButton friend = new JRadioButton("친구");
		ButtonGroup relationship = new ButtonGroup();

		JLabel recommendRelationship = new JLabel();
		alone.setBounds(115, 339, 100, 25);
		couple.setBounds(215, 339, 120, 25);
		family.setBounds(115, 364, 100, 25);
		friend.setBounds(215, 364, 120, 25);
		recommendRelationship.setBounds(115, 339, 220, 50);
		panel.add(alone);
		panel.add(couple);
		panel.add(family);
		panel.add(friend);
		panel.add(recommendRelationship);
		alone.setSelected(true);
		relationship.add(alone);
		relationship.add(couple);
		relationship.add(family);
		relationship.add(friend);

		if (alone.isSelected()) {
			relationshipResult = "혼자";
		} else if (couple.isSelected()) {
			relationshipResult = "연인";
		} else if (family.isSelected()) {
			relationshipResult = "가족";
		} else if (friend.isSelected()) {
			relationshipResult = "친구";
		}

		JButton btnInsert = new JButton("평가 추가하기");
		btnInsert.setBounds(10, 395, 310, 22);
		panel.add(btnInsert);

		btnInsert.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				insertRate();

			}
		});

	}

	public void insertRate() {

		PreparedStatement pstmtInsert = null;

		// String insertTitle = titleText.getText();
		// String insertDirector = directorText.getText();
		String insertReview = reviewText.getText();
		String insertSimilarMovie = similarMovieText.getText();
		String insertSimilarDirector = similarDirectorText.getText();

		String rateQuery = "INSERT INTO DB2021_평가 VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";

		try {
			pstmtInsert = conn.prepareStatement(rateQuery);
			pstmtInsert.setString(1, ID);
			pstmtInsert.setString(2, name);
			pstmtInsert.setInt(3, Integer.parseInt(num));
			pstmtInsert.setString(4, updateMovie);
			pstmtInsert.setString(5, updateDirector);
			pstmtInsert.setFloat(6, Float.parseFloat(gradeText.getText()));
			pstmtInsert.setString(7, insertReview);
			pstmtInsert.setString(8, insertSimilarMovie);
			pstmtInsert.setString(9, insertSimilarDirector);
			pstmtInsert.setString(10, feelingResult);
			pstmtInsert.setString(11, weatherResult);
			pstmtInsert.setString(12, relationshipResult);
			System.out.print("execute 전");
			pstmtInsert.executeUpdate();

			JOptionPane.showMessageDialog(null, "평가가 추가되었습니다.\n창을 닫아 프로그램을 종료하세요.");

		} catch (SQLException e) {
			System.out.println("에러!");
		} catch (NumberFormatException sen) {
			JOptionPane.showMessageDialog(null, "입력 형식에 문제가 발생했습니다.");
		}
	}

}
