package DB2021Team10;

import java.sql.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.event.*;
import java.awt.*;

public class MainFrame extends JFrame {
	JPanel initPanel = new JPanel();
	JLabel init = new JLabel(); // 기본 화면
	JScrollPane scrollPane = new JScrollPane();

	Connection conn;
	Statement stmt;

	public MainFrame(Connection conn, Statement stmt) {
		this.conn = conn;
		this.stmt = stmt;
		setTitle("10지 않조의 영화 창고"); // 이름 지정
		createMenu(); // 메뉴 생성, 프레임에 삽입

		this.add(scrollPane);

		init = new JLabel("<html>안녕하세요 <br /> 10조의 영화 창고에 오신 것을 환영합니다<br />:)</html>)", JLabel.CENTER);
		init.setFont(new Font("굴림", Font.BOLD, 40));
		init.setBorder(new EmptyBorder(300, 0, 0, 0));
		initPanel.add(init);

		scrollPane.setViewportView(initPanel);
		this.setSize(1400, 800); // 윈도우 창 사이즈 조정
		this.setVisible(true); // 윈도우 창 보이게하기
	}

	void createMenu() {
		JMenuBar menuBar = new JMenuBar(); // 긴 메뉴바 생성

		// 영화 정보
		JMenuItem[] MovInfoMenuItem = new JMenuItem[6]; // 메뉴 아이템 배열
		String[] MovInfoItemTitle = { "영화 정보 홈", "랭킹", "추천", "관람 정보", "감독별", "기타" }; // 메뉴 아이템 이름 배열

		// 영화 평가
		JMenuItem[] MovReviewMenuItem = new JMenuItem[3]; // 메뉴 아이템 배열
		String[] MovReviewItemTitle = { "영화 평가 홈", "기수별", "영화별" }; // 메뉴 아이템 이름 배열

		// 부원 관리
		JMenuItem[] MemberMngMenuItem = new JMenuItem[1]; // 메뉴 아이템 배열
		String[] MemberMngItemTitle = { "부원 관리 홈" }; // 메뉴 아이템 이름 배열
		
		// 마이 페이지
		JMenuItem[] MyPageMenuItem = new JMenuItem[1]; // 메뉴 아이템 배열
		String[] MyPageItemTitle = { "나의 평가" }; // 메뉴 아이템 이름 배열

		MenuActionListener listener = new MenuActionListener(); // 이벤트 리스너 선언

		// 영화 정보 아래 메뉴
		JMenu MovInfoMenu = new JMenu("영화 정보"); // 메뉴 생성
		for (int i = 0; i < MovInfoMenuItem.length; i++) {
			MovInfoMenuItem[i] = new JMenuItem(MovInfoItemTitle[i]);
			MovInfoMenuItem[i].addActionListener(listener);
			MovInfoMenu.add(MovInfoMenuItem[i]);
		}
		menuBar.add(MovInfoMenu); // 메뉴바에 메뉴 부착

		// 영화 평가 아래 메뉴
		JMenu MovReviewMenu = new JMenu("영화 평가"); // 메뉴 생성
		for (int i = 0; i < MovReviewMenuItem.length; i++) {
			MovReviewMenuItem[i] = new JMenuItem(MovReviewItemTitle[i]);
			MovReviewMenuItem[i].addActionListener(listener);
			MovReviewMenu.add(MovReviewMenuItem[i]);
		}
		menuBar.add(MovReviewMenu); // 메뉴바에 메뉴 부착

		// 부원 관리 아래 메뉴
		JMenu MemberMngMenu = new JMenu("부원 관리"); // 메뉴 생성
		MemberMngMenu.addActionListener(listener);
		for (int i = 0; i < MemberMngMenuItem.length; i++) {
			MemberMngMenuItem[i] = new JMenuItem(MemberMngItemTitle[i]);
			MemberMngMenuItem[i].addActionListener(listener);
			MemberMngMenu.add(MemberMngMenuItem[i]);
		}
		menuBar.add(MemberMngMenu); // 메뉴바에 메뉴 부착

		// 마이 페이지 아래 메뉴
		JMenu MyPageMenu = new JMenu("마이 페이지"); // 메뉴 생성
		for (int i = 0; i < MyPageMenuItem.length; i++) {
			MyPageMenuItem[i] = new JMenuItem(MyPageItemTitle[i]);
			MyPageMenuItem[i].addActionListener(listener);
			MyPageMenu.add(MyPageMenuItem[i]);
		}
		menuBar.add(MyPageMenu); // 메뉴바에 메뉴 부착

		setJMenuBar(menuBar); // 메뉴 아이템 생성 후 메뉴에 메뉴 아이템 부착

	}

	class MenuActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			JPanel movie = new JPanel();
			JPanel rank = new JPanel();
			JPanel recommend = new JPanel();
			JPanel view = new JPanel();
			JPanel director = new JPanel();
			JPanel more = new JPanel();

			JPanel rate = new JPanel();
			JPanel rateMovie = new JPanel();
			JPanel rateNum = new JPanel();

			JPanel staff = new JPanel();

			JPanel myPage = new JPanel();

			Movie rc = new Movie(conn, stmt);
			movie.add(rc.mainPanel);
			Rank rc1 = new Rank(conn, stmt);
			rank.add(rc1.mainPanel);
			Recommend rc2 = new Recommend(conn, stmt);
			recommend.add(rc2.mainPanel);
			View rc3 = new View(conn, stmt);
			view.add(rc3.mainPanel);
			Director rc4 = new Director(conn, stmt);
			director.add(rc4.mainPanel);
			More rc5 = new More(conn, stmt);
			more.add(rc5.mainPanel);

			Rate rc6 = new Rate(conn, stmt);
			rate.add(rc6.mainPanel);
			Rate_Num rc7 = new Rate_Num(conn, stmt);
			rateNum.add(rc7.mainPanel);
			Rate_Movie rc8 = new Rate_Movie(conn, stmt);
			rateMovie.add(rc8.mainPanel);

			Staff rc9 = new Staff(conn, stmt);
			staff.add(rc9.mainPanel);

			MyPage rc10 = new MyPage(conn, stmt);
			myPage.add(rc10.mainPanel);

			String cmd = e.getActionCommand();
			switch (cmd) { // 메뉴 아이템의 종류 구분
			case "영화 정보 홈":
				scrollPane.setViewportView(movie);
				break;
			case "랭킹":
				scrollPane.setViewportView(rank);
				break;
			case "추천":
				scrollPane.setViewportView(recommend);
				break;
			case "관람 정보":
				scrollPane.setViewportView(view);
				break;
			case "감독별":
				scrollPane.setViewportView(director);
				break;
			case "기타":
				scrollPane.setViewportView(more);
				break;
			case "영화 평가 홈":
				scrollPane.setViewportView(rate);
				break;
			case "기수별":
				scrollPane.setViewportView(rateNum);
				break;
			case "영화별":
				scrollPane.setViewportView(rateMovie);
				break;
			case "부원 관리 홈":
				scrollPane.setViewportView(staff);
				break;
			case "나의 평가":
				scrollPane.setViewportView(myPage);
				break;
			}
		}
	}

}