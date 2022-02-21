package DB2021Team10;

import java.sql.*;
import javax.swing.*;
import java.awt.event.*;

public class Movie_Delete extends JFrame {

	Connection conn;
	Statement stmt;

	private JPanel panel = new JPanel();
	private JButton btnDelete = new JButton();
	private JTextField movieText, directorText;

	public Movie_Delete(Connection conn, Statement stmt) {

		this.conn = conn;
		this.stmt = stmt;

		// frame 디자인 설정
		setSize(280, 150);
		setTitle("영화 삭제하기");
		setResizable(false);
		setLocation(450, 200);

		// 새로운 panel 부착
		deletePanel(panel);
		add(panel);

		setVisible(true);

	}

	public void deletePanel(JPanel panel) {

		panel.setLayout(null);

		// 삭제할 영화의 제목
		JLabel deleteMovie = new JLabel("삭제할 영화의 제목");
		deleteMovie.setBounds(10, 10, 100, 25);
		panel.add(deleteMovie);

		movieText = new JTextField(40);
		movieText.setBounds(125, 10, 140, 25);
		panel.add(movieText);

		// 삭제할 영화의 감독
		JLabel deleteDirector = new JLabel("삭제할 영화의 감독");
		deleteDirector.setBounds(10, 50, 100, 25);
		panel.add(deleteDirector);

		directorText = new JTextField(40);
		directorText.setBounds(125, 50, 140, 25);
		panel.add(directorText);

		btnDelete = new JButton("삭제하기");
		btnDelete.setBounds(160, 80, 100, 25);
		panel.add(btnDelete);

		// 버튼 클릭시, delete 메소드 호출
		btnDelete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				delete();
				JOptionPane.showMessageDialog(null, "삭제 완료!");
			}
		});
	}

	public void delete() {

		PreparedStatement movie = null;
		PreparedStatement circulate = null;
		PreparedStatement streaming = null;
		PreparedStatement revenue = null;
		PreparedStatement grade = null;
		PreparedStatement actor = null;

		// 위에서 받아온 입력값 저장 (영화 제목, 영화 감독)
		String deleteMovie = movieText.getText();
		String deleteDirector = directorText.getText();

		// 각 테이블에게 전달될 delete 쿼리들
		String movieQuery = "DELETE FROM DB2021_영화 WHERE 제목=? and 감독=?";
		String circulateQuery = "DELETE FROM DB2021_유통 WHERE 제목=? and 감독=?";
		String streamingQuery = "DELETE FROM DB2021_스트리밍 WHERE 제목=? and 감독=?";
		String revenueQuery = "DELETE FROM DB2021_수익 WHERE 제목=? and 감독=?";
		String gradeQuery = "DELETE FROM DB2021_평점 WHERE 제목=? and 감독=?";
		String actorQuery = "DELETE FROM DB2021_배우 WHERE 제목=? and 감독=?";

		// 위에서 입력한 값이 없을 경우, 요청 후 return
		if (deleteMovie.length() < 1 || deleteDirector.length() < 1) {
			JOptionPane.showMessageDialog(null, "삭제할 영화 제목과 감독은 필수 입력!");
			movieText.setText("");
			directorText.setText("");
			return;
		}

		try {

			try {

				// auto commit 중지
				conn.setAutoCommit(false);

				// 영화 테이블 행 삭제
				movie = conn.prepareStatement(movieQuery);
				movie.setString(1, deleteMovie);
				movie.setString(2, deleteDirector);
				movie.executeUpdate();

				// 유통 테이블 행 삭제
				circulate = conn.prepareStatement(circulateQuery);
				circulate.setString(1, deleteMovie);
				circulate.setString(2, deleteDirector);
				circulate.executeUpdate();

				// 스트리밍 테이블 행 삭제
				streaming = conn.prepareStatement(streamingQuery);
				streaming.setString(1, deleteMovie);
				streaming.setString(2, deleteDirector);
				streaming.executeUpdate();

				// 수익 테이블 행 삭제
				revenue = conn.prepareStatement(revenueQuery);
				revenue.setString(1, deleteMovie);
				revenue.setString(2, deleteDirector);
				revenue.executeUpdate();

				// 평점 테이블 행 삭제
				grade = conn.prepareStatement(gradeQuery);
				grade.setString(1, deleteMovie);
				grade.setString(2, deleteDirector);
				grade.executeUpdate();

				// 배우 테이블 행 삭제
				actor = conn.prepareStatement(actorQuery);
				actor.setString(1, deleteMovie);
				actor.setString(2, deleteDirector);
				actor.executeUpdate();

				// 수동 commit
				conn.commit();

			} catch (SQLException e) {
				JOptionPane.showMessageDialog(null, "DELETE 구문 에러!");
				try {
					if (conn != null)
						conn.rollback(); // 문제 발생시, rollback
				} catch (SQLException se2) {
					se2.printStackTrace();
				}
			}

			// 다시 auto commit 실행
			conn.setAutoCommit(true);

		} catch (SQLException se) {
			se.printStackTrace();
		}
	}

}