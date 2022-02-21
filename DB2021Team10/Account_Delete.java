package DB2021Team10;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Account_Delete extends JFrame {

	Connection conn;
	Statement stmt;

	private JPanel panel = new JPanel();
	private JButton btnDelete = new JButton();
	private JTextField passwordText;

	public Account_Delete(Connection conn, Statement stmt) {

		this.conn = conn;
		this.stmt = stmt;
		setSize(320, 150);
		setTitle("계정 삭제하기(회원 정보)");
		setResizable(false);
		setLocation(450, 200);

		deletePanel(panel);
		add(panel);

		setVisible(true);

	}

	// 양식 출력
	public void deletePanel(JPanel panel) {

		panel.setLayout(null);

		JLabel deleteMsg = new JLabel("*계정을 삭제할 시,내가 쓴 평가도 모두 삭제됩니다.");
		deleteMsg.setBounds(10, 10, 275, 25);
		panel.add(deleteMsg);

		JLabel passwordCheck = new JLabel("비밀번호 입력");
		passwordCheck.setBounds(10, 40, 100, 25);
		panel.add(passwordCheck);

		passwordText = new JTextField(40);
		passwordText.setBounds(125, 40, 160, 25);
		panel.add(passwordText);

		btnDelete = new JButton("계정 삭제하기");
		btnDelete.setBounds(160, 80, 125, 25);
		panel.add(btnDelete);

		btnDelete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				delete();
			}
		});
	}

	public void delete() {
		PreparedStatement member = null;
		PreparedStatement review = null;

		String passwordInput = passwordText.getText();

		// 로그인 정보
		String ID = Login.myID;
		String PWD = Login.myPWD;

		String memberQuery = "DELETE FROM DB2021_부원 WHERE 학번 = ?";
		String reviewQuery = "DELETE FROM DB2021_평가 WHERE 학번 = ?";
		try {

			try {

				conn.setAutoCommit(false);

				if (PWD.equals(passwordInput)) {
					// 해당 아이디 평가 삭제
					review = conn.prepareStatement(reviewQuery);
					review.setString(1, ID);
					review.executeUpdate();

					// 해당 아이디 부원 삭제
					member = conn.prepareStatement(memberQuery);
					member.setString(1, ID);
					member.executeUpdate();

					JOptionPane.showMessageDialog(null, "계정 정보 삭제 완료.\n 더 이상 해당 프로그램을 사용할 수 없습니다.");
					setDefaultCloseOperation(EXIT_ON_CLOSE);
					// this.dispose();

				} else {
					JOptionPane.showMessageDialog(null, "비밀번호가 틀렸습니다.");
					passwordText.setText("");
				}

				conn.commit();

			} catch (SQLException e) {
				JOptionPane.showMessageDialog(null, "DELETE 구문 에러!");
				try {
					if (conn != null)
						conn.rollback();
				} catch (SQLException se2) {
					se2.printStackTrace();
				}
			}

			conn.setAutoCommit(true);

		} catch (SQLException se) {
			se.printStackTrace();
		}
	}

}
