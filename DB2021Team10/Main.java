package DB2021Team10;

import java.sql.*;
import java.util.*;

public class Main {

	static final String DB_URL = "jdbc:mysql://localhost:3306/DB2021Team10";

	static final String USER = "root";
	static final String PASS = "s50985"; // project 디비와 연결

	public static void main(String[] args) {
		System.out.println("Connecting to database...\n");

		try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);

				Statement stmt = conn.createStatement();

		) {
			conn.setAutoCommit(true);

			Login login = new Login(conn, stmt);

			Scanner sc = new Scanner(System.in);

			System.out.print("그만하고 싶을 땐 아무키나 입력해보세요! : ");

			String stop = sc.nextLine();

		} catch (SQLException se) {
			System.out.println("SQLExeption : " + se);
		}
		System.out.println("프로그램 사용이 종료되었습니다.");

	}

}
