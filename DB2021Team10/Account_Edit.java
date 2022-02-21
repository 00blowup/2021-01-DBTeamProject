package DB2021Team10;

import java.sql.*;

import javax.swing.*;
import java.awt.event.*;

public class Account_Edit extends JFrame {
   Connection conn;
   Statement stmt;

   private JPanel panel = new JPanel();

   private JButton btnFinish; // 완료 버튼
   private JButton btnName, btnPassword, btnInception, btnRole; // 각각 수정 버튼
   JRadioButton role1,role2,role3,role4;
   private String roleResult;
   private PreparedStatement pstmt = null;
   private String name, password, inception, role;

   String ID = Login.myID;

   public Account_Edit(Connection conn, Statement stmt) {

      this.conn = conn;
      this.stmt = stmt;
      setTitle("부원 정보 수정하기");
      setSize(380, 350);
      setLocation(450, 250);

      getInfo(panel);
      add(panel);

      setVisible(true);

   }

   // DB에서 내 계정 정보를 가지고 옴
   public void getInfo(JPanel panel) {
      JPanel infoPanel = new JPanel();
      infoPanel = panel;
      infoPanel.setLayout(null);

      PreparedStatement member = null;

      ResultSet rsMember = null;

      String memberQuery = "SELECT 학번, 이름, 비밀번호, 기수, 직책 FROM DB2021_부원 WHERE 학번 = ?";

      try {
         member = conn.prepareStatement(memberQuery);
         member.setString(1, ID);
         rsMember = member.executeQuery();

         if (rsMember.next()) {
            name = rsMember.getString("이름");
            password = rsMember.getString("비밀번호");
            inception = Integer.toString(rsMember.getInt("기수"));
            role = rsMember.getString("직책");
         }

         editAccount(infoPanel);
         add(infoPanel);
         infoPanel.setVisible(true);

      } catch (SQLException e) {
         JOptionPane.showMessageDialog(null, "UPDATE 구문 에러!");
      }
   }

   // 정보 보여주는 페널
   public void editAccount(JPanel panel) {

      panel.setLayout(null);

      // 학번 조회, 수정 불가능
      JLabel IDTag = new JLabel("학번");
      IDTag.setBounds(13, 15, 105, 25);
      panel.add(IDTag);

      JLabel IDResult = new JLabel(ID);
      IDResult.setBounds(125, 15, 125, 25);
      panel.add(IDResult);

      // 이름 수정
      JLabel nameTag = new JLabel("이름");
      nameTag.setBounds(13, 45, 105, 25);
      panel.add(nameTag);

      JLabel nameResult = new JLabel(name);
      nameResult.setBounds(125, 45, 125, 25);
      panel.add(nameResult);

      btnName = new JButton("수정");
      btnName.setBounds(270, 45, 80, 25);
      panel.add(btnName);

      btnName.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            editInput("이름");
         }
      });

      // 비밀번호 수정
      JLabel passwordTag = new JLabel("비밀번호");
      passwordTag.setBounds(13, 75, 105, 25);
      panel.add(passwordTag);

      JLabel passwordResult = new JLabel(password);
      passwordResult.setBounds(125, 75, 125, 25);
      panel.add(passwordResult);

      btnPassword = new JButton("수정");
      btnPassword.setBounds(270, 75, 80, 25);
      panel.add(btnPassword);

      btnPassword.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            editInput("비밀번호");
         }
      });

      // 기수 수정
      JLabel inceptionTag = new JLabel("기수");
      inceptionTag.setBounds(13, 105, 105, 25);
      panel.add(inceptionTag);

      JLabel inceptionResult = new JLabel(inception);
      inceptionResult.setBounds(125, 105, 125, 25);
      panel.add(inceptionResult);

      btnInception = new JButton("수정");
      btnInception.setBounds(270, 105, 80, 25);
      panel.add(btnInception);

      btnInception.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            editInput("기수");
         }
      });

      // 직책 수정
      JLabel roleTag = new JLabel("직책");
      roleTag.setBounds(13, 135, 105, 25);
      panel.add(roleTag);

      JLabel roleResult = new JLabel(role);
      roleResult.setBounds(125, 135, 70, 25);
      panel.add(roleResult);

      btnRole = new JButton("수정");
      btnRole.setBounds(270, 135, 80, 25);
      panel.add(btnRole);

      btnRole.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            editInputRole();
         }
      });
   }

   // 수정 정보 입력 받는 frame
   public void editInput(String attr) {
      JFrame frame = new JFrame();

      frame.setSize(400, 300);
      frame.setLocation(470, 250);
      frame.setLayout(null);
      frame.setVisible(true);

      JLabel edit = new JLabel("수정할 내용을 입력해주세요.");
      edit.setBounds(100, 20, 320, 50);
      frame.add(edit);

      JTextField input = new JTextField();
      input.setBounds(100, 70, 160, 50);
      frame.add(input);

      btnFinish = new JButton("완료");
      btnFinish.setBounds(100, 150, 80, 25);
      frame.add(btnFinish);

      btnFinish.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            if (input.getText().equals("") && attr.equals("비밀번호")) {
               JOptionPane.showMessageDialog(null, "비밀번호는 필수 입력!");
               return;
            } else
               edit(attr, input.getText());

            frame.dispose();
         }
      });

   }

   // 입력 받은 값 DB에 업데이트(반영)
   public void edit(String attr, String input) {

      String query = null;

      try {

         switch (attr) {
         case "이름":
            query = "UPDATE DB2021_부원 SET 이름 = ? WHERE 학번 = ?";
            pstmt = conn.prepareStatement(query);
            pstmt.setString(1, input);
            pstmt.setString(2, ID);
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "정보 수정이 완료되었습니다\n창을 닫아주세요.");
            break;

         case "비밀번호":
            query = "UPDATE DB2021_부원 SET 비밀번호 = ? WHERE 학번 = ?";
            pstmt = conn.prepareStatement(query);
            pstmt.setString(1, input);
            pstmt.setString(2, ID);
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "정보 수정이 완료되었습니다\n창을 닫아주세요.");
            break;

         case "기수":
            query = "UPDATE DB2021_부원 SET 기수 = ? WHERE 학번 = ?";
            pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, Integer.parseInt(input));
            pstmt.setString(2, ID);
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "정보 수정이 완료되었습니다\n창을 닫아주세요.");
            break;
         }
      } catch (SQLException se) {
         System.out.println("에러!");
      } catch (NumberFormatException sen) {
         JOptionPane.showMessageDialog(null, "숫자만 입력해주세요.");
      }
   }

   // 직책 수정 값 입력 받을 frame
   public void editInputRole() {
      JFrame frameRole = new JFrame();

      role1 = new JRadioButton("회장");
      role2 = new JRadioButton("부회장");
      role3 = new JRadioButton("부원");
      role4 = new JRadioButton("비활동");
      ButtonGroup roleGroup = new ButtonGroup();
      
      frameRole.setSize(400, 300);
      frameRole.setLocation(470, 50);
      frameRole.setLayout(null);
      frameRole.setVisible(true);

      JLabel edit = new JLabel("직책을 선택해 주세요");
      edit.setBounds(100, 20, 320, 50);
      frameRole.add(edit);

      role1.setBounds(100, 90, 80, 25);
      role2.setBounds(100, 140, 80, 25);
      role3.setBounds(200, 90, 80, 25);
      role4.setBounds(200, 140, 80, 25);

      frameRole.add(role1);
      frameRole.add(role2);
      frameRole.add(role3);
      frameRole.add(role4);

      roleGroup.add(role1);
      roleGroup.add(role2);
      roleGroup.add(role3);
      roleGroup.add(role4);

      btnFinish = new JButton("완료");
      btnFinish.setBounds(200, 200, 80, 25);
      frameRole.add(btnFinish);

      btnFinish.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            editRole();
            frameRole.dispose();
         }
      });

   }

   // 입력 받은 직책 값 DB에 업데이트(반영)
   public void editRole() {

      PreparedStatement pstmt = null;

      String query = "UPDATE DB2021_부원 SET 직책 = ? WHERE 학번 = ?";

      if (role1.isSelected()) {
         roleResult = "회장";
      } else if (role2.isSelected()) {
         roleResult = "부회장";
      } else if (role3.isSelected()) {
         roleResult = "부원";
      } else if (role4.isSelected()) {
         roleResult = "비활동";
      }

      try {
         pstmt = conn.prepareStatement(query);
         pstmt.setString(1, roleResult);
         pstmt.setString(2, ID);
         pstmt.executeUpdate();
         JOptionPane.showMessageDialog(null, "정보 수정이 완료되었습니다\n 창을 닫아주세요.");
      } catch (SQLException e) {
         JOptionPane.showMessageDialog(null, "선택된 항목이 없습니다.");
      }

   }

}