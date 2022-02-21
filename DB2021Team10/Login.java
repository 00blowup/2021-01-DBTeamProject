package DB2021Team10;

import java.sql.*;
import javax.swing.*;
import java.awt.event.*;

public class Login extends JFrame {
	private JButton btnLogin;
	private JButton btnInit;
	private JButton btnSignIn;
	private JPasswordField passText;
	private JTextField userText;
	private boolean loginCheck;

	static String myID;
	static String myPWD;

	Connection conn;
	Statement stmt;

	public Login(Connection conn, Statement stmt) {
		
		this.conn = conn;
		this.stmt = stmt;
		
		// 로그인 창 세팅
		setTitle("로그인");
		setSize(280, 200);
		setResizable(false);
		setLocation(450, 200);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		// 패널 생성
		JPanel panel = new JPanel();

		placeLoginPanel(panel);
		add(panel); // 프레임에 패널 부착

		setVisible(true); // 보이게 설정
	}

	// 로그인 입력 패널 구성
	public void placeLoginPanel(JPanel panel) {
		
		panel.setLayout(null);
		
		JLabel userLabel = new JLabel("학번");
		userLabel.setBounds(10, 10, 80, 25);
		panel.add(userLabel); // 패널에 레이블 부착

		JLabel passLabel = new JLabel("비밀번호");
		passLabel.setBounds(10, 40, 80, 25);
		panel.add(passLabel);

		userText = new JTextField(20);
		userText.setBounds(100, 10, 160, 25);
		panel.add(userText);

		passText = new JPasswordField(20);
		passText.setBounds(100, 40, 160, 25);
		panel.add(passText);
		
		// 비밀번호 입력창 엔터 누르면 로그인 시도
		passText.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				isLoginCheck();
			}
		});

		btnInit = new JButton("전체 삭제");
		btnInit.setBounds(10, 80, 100, 25);
		panel.add(btnInit);
		btnInit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				userText.setText("");
				passText.setText("");
			}
		});

		btnLogin = new JButton("로그인");
		btnLogin.setBounds(160, 80, 100, 25);
		panel.add(btnLogin);
		btnLogin.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				isLoginCheck();
			}
		});

		btnSignIn = new JButton("회원가입");
		btnSignIn.setBounds(10, 120, 250, 25);
		panel.add(btnSignIn);
		btnSignIn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				new Sign_up(conn, stmt);
			}

		});

	}

	// 로그인 메소드
	public void isLoginCheck() {
		// boolean flag = false;

		PreparedStatement pstmt = null;
		ResultSet rs = null;

		String sql = "select 비밀번호 from DB2021_부원 where 학번 = ?";
		String getPass = null;

		// 사용자가 입력한 학번,비밀번호
		String id = userText.getText();
		String pwd = new String(passText.getPassword());

		try {
			// select문 실행
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id); // 첫번째 ?의 값은 id에서 들어온 값
			rs = pstmt.executeQuery(); // 쿼리 실행

			// 쿼리 결과값과 뷰에서 받아온 pwd값을 비교
			if (rs.next()) {
				getPass = rs.getString("비밀번호");
				if (getPass.equals(pwd)) {
					JOptionPane.showMessageDialog(null, "로그인 성공");
					loginCheck = true;
					
					// 로그인 성공이라면 메인프레임 띄우기
					if (isLogin()) {
						myID = id;
						myPWD = pwd;
						new MainFrame(conn, stmt); // 메인창 메소드를 이용해 창 띄우기
					}
					
					this.dispose();
					
				} else {
					JOptionPane.showMessageDialog(null, "로그인 실패 (비밀 번호가 옳지 않습니다.)");
				}

			} else {
				JOptionPane.showMessageDialog(null, "로그인 실패 (등록 되지 않은 학번입니다.)");
			}

		} catch (SQLException sqle) {
			System.out.println("SQLException : " + sqle);
		}

	}

	// 로그인 성공 여부
	public boolean isLogin() {
		
		return loginCheck;
		
	}

}