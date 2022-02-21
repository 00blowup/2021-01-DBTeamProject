package DB2021Team10;

import java.sql.*;
import javax.swing.*;
import java.awt.event.*;

public class Rate_Delete extends JFrame {

	Connection conn;
	Statement stmt;

	private JPanel panel = new JPanel();
	private JButton btnDelete = new JButton();
	private JTextField movieText, directorText;

	public Rate_Delete(Connection conn, Statement stmt) {

		this.conn = conn;
		this.stmt = stmt;
		setSize(330, 160);
		setTitle("평가 삭제하기");
		setResizable(false);
		setLocation(450, 200);

		deletePanel(panel);
		add(panel);

		setVisible(true);

	}

	// 삭제할 영화 평가의 제목과 감독 입력 받기
	public void deletePanel(JPanel panel) {

		panel.setLayout(null);

		JLabel deleteMovie = new JLabel("삭제할 평가의 영화제목");
		deleteMovie.setBounds(10, 10, 140, 25);
		panel.add(deleteMovie);

		movieText = new JTextField(40);
		movieText.setBounds(160, 10, 140, 25);
		panel.add(movieText);

		JLabel deleteDirector = new JLabel("삭제할 평가의 영화감독");
		deleteDirector.setBounds(10, 50, 140, 25);
		panel.add(deleteDirector);

		directorText = new JTextField(40);
		directorText.setBounds(160, 50, 140, 25);
		panel.add(directorText);

		btnDelete = new JButton("삭제하기");
		btnDelete.setBounds(200, 80, 100, 25);
		panel.add(btnDelete);

		btnDelete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				delete();
				JOptionPane.showMessageDialog(null, "삭제 완료!");
			}
		});
	}

	public void delete() {

		PreparedStatement deleteRate = null;

		String deleteMovie = movieText.getText();
		String deleteDirector = directorText.getText();
		String ID = Login.myID;

		// 평가에서 내용 삭제
		String rateDelete = "DELETE FROM DB2021_평가 WHERE 제목=? and 감독=? and 학번=?";

		// 위에서 입력한 값이 없을 경우, 요청 후 return
		if (deleteMovie.length() < 1 || deleteDirector.length() < 1) {
			JOptionPane.showMessageDialog(null, "삭제할 영화 제목과 감독은 필수 입력!");
			movieText.setText("");
			directorText.setText("");
			return;
		}

		try {
			deleteRate = conn.prepareStatement(rateDelete);
			deleteRate.setString(1, deleteMovie);
			deleteRate.setString(2, deleteDirector);
			deleteRate.setString(3, ID);
			deleteRate.executeUpdate();

		} catch (SQLException se) {
			se.printStackTrace();
		}

	}
}
