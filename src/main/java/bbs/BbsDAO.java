package bbs;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class BbsDAO {
//������ ������ ����
	
	
	private Connection conn; //�ڹٿ� �����ͺ��̽��� ���� 
	private ResultSet rs; //����� �޾ƿ���
	
	//�⺻ ������
	//UserDAO�� ����Ǹ� �ڵ����� �����Ǵ� �κ�
	//�޼ҵ帶�� �ݺ��Ǵ� �ڵ带 �̰��� ������ �ڵ尡 ����ȭ�ȴ�
	public BbsDAO() {
		try {
			/*
			 
      String jdbc_driver = "com.mysql.jdbc.Driver";
      String jdbc_url = "jdbc:mysql://localhost:3306/bbs?serverTimezone=UTC&useSSL=false";

			 */
			String dbURL = "jdbc:mysql://localhost:3306/bbs?serverTimezone=UTC&useSSL=false"; 
			String dbID = "root";
			String dbPassword = "root";
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(dbURL, dbID, dbPassword);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
		public String getDate() {
			 String SQL= "SELECT NOW()";// ������ �ð��� �������� MYSQL ���
			 try {
				 PreparedStatement pstmt= conn.prepareStatement(SQL);
				 rs=pstmt.executeQuery();
				 if(rs.next()) {
					 return rs.getString(1);
				 }
				 
			 }catch(Exception e) {
				 e.printStackTrace();
			 }
			 return "";//�����ͺ��̽� ���� 
		}
		public int getNext() {
			 String SQL= "SELECT bbsID FROM BBS ORDER BY bbsID DESC";// ������ �ð��� �������� MYSQL ���
			 try {
				 PreparedStatement pstmt= conn.prepareStatement(SQL);
				 rs=pstmt.executeQuery();
				 if(rs.next()) {
					 return rs.getInt(1) +1;
				 }
				 return 1; //ù���� �Խù��� ���
			 }catch(Exception e) {
				 e.printStackTrace();
			 }
			 return -1;//�����ͺ��̽� ���� 
		}
		public int write(String bbsTitle, String userID , String bbsContent) {
			 String SQL= "INSERT INTO BBS VALUES( ? , ? , ? , ? , ? , ? )";// ������ �ð��� �������� MYSQL ���
			 try {
				 PreparedStatement pstmt= conn.prepareStatement(SQL);
				 pstmt.setInt(1, getNext());
				 pstmt.setString(2, bbsTitle);
				 pstmt.setString(3, userID);
				 pstmt.setString(4, getDate());
				 pstmt.setString(5, bbsContent);
				 pstmt.setInt(6,1);
				 //rs=pstmt.executeQuery(); insert�� ��ü�� update ������ �۵��ϱ� ������ �������� �ʿ䰡����
				 return pstmt.executeUpdate() ; 
			 
			 }catch(Exception e) {
				 e.printStackTrace();
			 }
			 return -1;//�����ͺ��̽� ���� 
		}
		public ArrayList<Bbs> getList(int pageNumber){
			 String SQL= "SELECT * FROM BBS WHERE bbsID < ? AND bbsAvailable = 1 ORDER BY bbsID DESC LIMIT 10";
			 ArrayList<Bbs> list = new ArrayList<Bbs>();
			try {
				 PreparedStatement pstmt= conn.prepareStatement(SQL);
				 pstmt.setInt(1,getNext()-(pageNumber -1 ) * 10); //getnext �� �� �������� �ۼ��� ���� ��ȣ�� �Է��ϴµ� 
				 rs=pstmt.executeQuery();
				 while(rs.next()) {
					 Bbs bbs= new Bbs();
					 bbs.setBbsID(rs.getInt(1));
					 bbs.setBbsTitle(rs.getString(2));
					 bbs.setUserID(rs.getString(3));
					 bbs.setBbsDate(rs.getString(4));
					 bbs.setBbsContent(rs.getString(5));
					 bbs.setBbsAvailable(rs.getInt(6));
					 list.add(bbs);
				 } //�Խñ� 10���� ��Ÿ���� �κ� 
			}catch(Exception e) {
				 e.printStackTrace();
			 }
			 return list;//list �� ��ȯ
			 
		}
		public boolean nextPage(int pageNumber) {//�Խñ��� 10���̻� �� ������ �ؽ�Ʈ �������� ���ٴ°� ǥ��
			 String SQL= "SELECT * FROM BBS WHERE bbsID < ? AND bbsAvailable = 1 ORDER BY bbsID DESC LIMIT 10" ;
			try {
				 PreparedStatement pstmt= conn.prepareStatement(SQL);
				 pstmt.setInt(1,getNext()-(pageNumber -1 ) * 10); //getnext �� �� �������� �ۼ��� ���� ��ȣ�� �Է��ϴµ� 
				 rs=pstmt.executeQuery();
				 if(rs.next()) {
				 return true;
				 } //�Խñ� 10���� ��Ÿ���� �κ� 
			}catch(Exception e) {
				 e.printStackTrace();
			 }
			 return false;//list �� ��ȯ
}
		public Bbs getBbs(int bbsID) {
			 String SQL= "SELECT * FROM BBS WHERE bbsID =?" ;
				try {
					 PreparedStatement pstmt= conn.prepareStatement(SQL);
					 pstmt.setInt(1,bbsID);
					 rs=pstmt.executeQuery();
					 if(rs.next()) {
						 Bbs bbs= new Bbs();
						 bbs.setBbsID(rs.getInt(1));
						 bbs.setBbsTitle(rs.getString(2));
						String title1= rs.getString(2);
					//	System.out.println("title: "+title1);
						bbs.setBbsTitle(title1);
					
							 
						 bbs.setUserID(rs.getString(3));
						 bbs.setBbsDate(rs.getString(4));
						 bbs.setBbsContent(rs.getString(5));
						 bbs.setBbsAvailable(rs.getInt(6));
						 return bbs;
						 

					 } //�Խñ� 10���� ��Ÿ���� �κ� 
				}catch(Exception e) {
					 e.printStackTrace();
				 }
				 return null;//list �� ��ȯ
		}

	public  int update(int bbsID, String bbsTitle,String bbsContent) {
		 String SQL= "UPDATE BBS SET bbsTitle=?, bbsContent =? WHERE bbsID = ?";// ������ �ð��� �������� MYSQL ���
		 try {
			 PreparedStatement pstmt= conn.prepareStatement(SQL);
			 pstmt.setString(1, bbsTitle);
			 pstmt.setString(2, bbsContent);
			 pstmt.setInt(3,bbsID);
			 //rs=pstmt.executeQuery(); insert�� ��ü�� update ������ �۵��ϱ� ������ �������� �ʿ䰡����
			 return pstmt.executeUpdate() ; 
		 
		 }catch(Exception e) {
			 e.printStackTrace();
		 }
		 return -1;//�����ͺ��̽� ���� 
	}
	public int update1(int bbsID, String bbsTitle, String bbsContent) {
		 String SQL= "UPDATE BBS SET bbsTitle= ? , bbsContent = ? WHERE bbsID=?";// ������ �ð��� �������� MYSQL ���
		 try {
			 PreparedStatement pstmt= conn.prepareStatement(SQL);			 
			 pstmt.setString(1, bbsTitle);
			 pstmt.setString(2, bbsContent);
			 pstmt.setInt(3, bbsID);
			 //rs=pstmt.executeQuery(); insert�� ��ü�� update ������ �۵��ϱ� ������ �������� �ʿ䰡����
			 return pstmt.executeUpdate() ; 
		 
		 }catch(Exception e) {
			 e.printStackTrace();
		 }
		 return -1;//�����ͺ��̽� ���� 
	}
	public int delete(int bbsID) {
		 String SQL= "UPDATE BBS SET bbsAvailable = 0 WHERE bbsID=?";// ������ �ð��� �������� MYSQL ���
		 try {
			 PreparedStatement pstmt= conn.prepareStatement(SQL);			 
			 pstmt.setInt(1, bbsID);
			 
			 //rs=pstmt.executeQuery(); insert�� ��ü�� update ������ �۵��ϱ� ������ �������� �ʿ䰡����
			 return pstmt.executeUpdate() ; 
		 // ����� �������̶�� 0 �̻��� �� ���ж��ϸ� -1�� �����Ѵ�
		 }catch(Exception e) {
			 e.printStackTrace();
		 }
		 return -1;//�����ͺ��̽� ���� 
	}
}
