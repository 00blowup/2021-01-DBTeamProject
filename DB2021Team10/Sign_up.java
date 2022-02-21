package DB2021Team10;

import java.sql.*;
import java.util.InputMismatchException;

import javax.swing.*;
import java.awt.event.*;

public class Sign_up extends JFrame {
   private JButton btn;
   private JTextField IDText, nameText, passwordText, inceptionText;
   private JPanel panel = new JPanel();
   private JRadioButton role1 = new JRadioButton("회장");
   private JRadioButton role2 = new JRadioButton("부회장");
   private JRadioButton role3 = new JRadioButton("부원");
   private JRadioButton role4 = new JRadioButton("비활동");
   private ButtonGroup roleGroup = new ButtonGroup();

   // 필드 정의
   Connection conn;

   public Sign_up(Connection conn, Statement stmt) {
      this.conn = conn;
      setTitle("회원가입하기");
      setSize(340, 300);
      setLocation(450, 200);
      insertPanel(panel);
      this.add(panel);
      setVisible(true);
   }

   //입력 양식 구현
   public void insertPanel(JPanel panel) {

      panel.setLayout(null);

      // 학번 입력창
      JLabel insertID = new JLabel("학번");
      insertID.setBounds(10, 10, 80, 25);
      panel.add(insertID);

      IDText = new JTextField(40);
      IDText.setBounds(165, 10, 160, 25);
      panel.add(IDText);

      // 이름 입력창
      JLabel insertname = new JLabel("이름");
      insertname.setBounds(10, 40, 80, 25);
      panel.add(insertname);

      nameText = new JTextField(40);
      nameText.setBounds(165, 40, 160, 25);
      panel.add(nameText);

      // 비밀번호 입력창
      JLabel insertPassword = new JLabel("비밀번호");
      insertPassword.setBounds(10, 70, 80, 25);
      panel.add(insertPassword);

      passwordText = new JTextField(40);
      passwordText.setBounds(165, 70, 160, 25);
      panel.add(passwordText);

      // 기수 입력창
      JLabel insertInception = new JLabel("기수");
      insertInception.setBounds(10, 100, 80, 25);
      panel.add(insertInception);

      inceptionText = new JTextField(40);
      inceptionText.setBounds(165, 100, 160, 25);
      panel.add(inceptionText);

      // 직책 입력창
      JLabel insertRole = new JLabel("직책 (미입력시 '비활동'지정)");
      insertRole.setBounds(10, 130, 200, 25);
      panel.add(insertRole);

      // 직책 radio 버튼 구현
      role1.setBounds(165, 130, 80, 25);
      role2.setBounds(245, 130, 80, 25);
      role3.setBounds(165, 160, 80, 25);
      role4.setBounds(245, 160, 80, 25);

      panel.add(role1);
      panel.add(role2);
      panel.add(role3);
      panel.add(role4);

      roleGroup.add(role1);
      roleGroup.add(role2);
      roleGroup.add(role3);
      roleGroup.add(role4);

      // 회원가입 버튼
      btn = new JButton("등록하기");
      btn.setBounds(220, 200, 100, 25);
      panel.add(btn);
      btn.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            insertMember();
         }
      });
   }

   //DB에 회원정보 삽입하기
   public void insertMember() {

      PreparedStatement pstmt, pstmt2 = null;

      ResultSet rs = null;
      
      // DB에서 가져올 값
      String ID = null;
      
      // 입력받은 값
      String updateID = IDText.getText();
      String updateName = nameText.getText();
      String updatePassword = passwordText.getText();
      String roleResult = null;
      
      //선택 안 할시엔 비활동으로 입력됨 
      if (role1.isSelected()) {
         roleResult = "회장";
      } else if (role2.isSelected()) {
         roleResult = "부회장";
      } else if (role3.isSelected()) {
         roleResult = "부원";
      } else{
         roleResult = "비활동";
      }

      //not null 조건 팝업
      if (updateID.length() < 1 || updatePassword.length() < 1) {
         JOptionPane.showMessageDialog(null, "학번과 비밀번호는 필수 입력!");
         IDText.setText("");
         nameText.setText("");
         passwordText.setText("");
         inceptionText.setText("");
         return;
      } else if(updateID.length() != 7) { //학번은 7자리이어야 함
         JOptionPane.showMessageDialog(null, "학번은 7자리로 입력해야 합니다.");
         IDText.setText("");
         nameText.setText("");
         passwordText.setText("");
         inceptionText.setText("");
         return;
      } else if(updateName.length() < 1) { //이름 필수 입력
         JOptionPane.showMessageDialog(null, "이름을 입력해주세요");
         IDText.setText("");
         nameText.setText("");
         passwordText.setText("");
         inceptionText.setText("");
         return;
      } else if(updateName.length() < 1) { //이름 필수 입력
         JOptionPane.showMessageDialog(null, "이름을 입력해주세요");
         IDText.setText("");
         nameText.setText("");
         passwordText.setText("");
         inceptionText.setText("");
         return;
      }

      String sqlExistMember = "SELECT 학번 FROM DB2021_부원 WHERE 학번 = ?";
      String insertMember = "INSERT INTO DB2021_부원 VALUES (?,?,?,?,?)";

      try {
         pstmt = conn.prepareStatement(sqlExistMember);
         pstmt.setString(1, updateID);
         rs = pstmt.executeQuery();

         if (rs.next()) {
            ID = rs.getString("학번");
            
            //primary key 조건 확인
            if (ID.equals(updateID)) {
               JOptionPane.showMessageDialog(null, "이미 존재하는 사용자입니다.");
               IDText.setText("");
               nameText.setText("");
               passwordText.setText("");
               inceptionText.setText("");
            }
         } else {
            pstmt2 = conn.prepareStatement(insertMember);
            pstmt2.setString(1, updateID);
            pstmt2.setString(2, updateName);
            pstmt2.setString(3, updatePassword);
            pstmt2.setInt(4, Integer.parseInt(inceptionText.getText()));
            pstmt2.setString(5, roleResult);

            pstmt2.executeUpdate();

            JOptionPane.showMessageDialog(null, "회원가입 완료.\n앞으로 돌아가서 로그인 해주세요");
            this.dispose();
         }
      } catch (SQLException e) {
         System.out.println("INSERT 구문 에러!");
      }catch (NumberFormatException ime) {
         JOptionPane.showMessageDialog(null, "기수는 숫자만 입력해야 합니다.(필수 입력)\n 수정 후 다시 시도해주세요.");
         IDText.setText("");
         nameText.setText("");
         passwordText.setText("");
         inceptionText.setText("");
      } 
   }
}