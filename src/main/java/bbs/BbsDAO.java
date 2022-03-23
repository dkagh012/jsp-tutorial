package bbs;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class BbsDAO {
//데이터 접근의 약자
	
	
	private Connection conn; //자바와 데이터베이스를 연결 
	private ResultSet rs; //결과값 받아오기
	
	//기본 생성자
	//UserDAO가 실행되면 자동으로 생성되는 부분
	//메소드마다 반복되는 코드를 이곳에 넣으면 코드가 간소화된다
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
			 String SQL= "SELECT NOW()";// 현재의 시간의 가져오는 MYSQL 용어
			 try {
				 PreparedStatement pstmt= conn.prepareStatement(SQL);
				 rs=pstmt.executeQuery();
				 if(rs.next()) {
					 return rs.getString(1);
				 }
				 
			 }catch(Exception e) {
				 e.printStackTrace();
			 }
			 return "";//데이터베이스 오류 
		}
		public int getNext() {
			 String SQL= "SELECT bbsID FROM BBS ORDER BY bbsID DESC";// 현재의 시간의 가져오는 MYSQL 용어
			 try {
				 PreparedStatement pstmt= conn.prepareStatement(SQL);
				 rs=pstmt.executeQuery();
				 if(rs.next()) {
					 return rs.getInt(1) +1;
				 }
				 return 1; //첫번쨰 게시물일 경우
			 }catch(Exception e) {
				 e.printStackTrace();
			 }
			 return -1;//데이터베이스 오류 
		}
		public int write(String bbsTitle, String userID , String bbsContent) {
			 String SQL= "INSERT INTO BBS VALUES( ? , ? , ? , ? , ? , ? )";// 현재의 시간의 가져오는 MYSQL 용어
			 try {
				 PreparedStatement pstmt= conn.prepareStatement(SQL);
				 pstmt.setInt(1, getNext());
				 pstmt.setString(2, bbsTitle);
				 pstmt.setString(3, userID);
				 pstmt.setString(4, getDate());
				 pstmt.setString(5, bbsContent);
				 pstmt.setInt(6,1);
				 //rs=pstmt.executeQuery(); insert문 자체는 update 문으로 작동하기 떄문에 쿼리문이 필요가없다
				 return pstmt.executeUpdate() ; 
			 
			 }catch(Exception e) {
				 e.printStackTrace();
			 }
			 return -1;//데이터베이스 오류 
		}
		public ArrayList<Bbs> getList(int pageNumber){
			 String SQL= "SELECT * FROM BBS WHERE bbsID < ? AND bbsAvailable = 1 ORDER BY bbsID DESC LIMIT 10";
			 ArrayList<Bbs> list = new ArrayList<Bbs>();
			try {
				 PreparedStatement pstmt= conn.prepareStatement(SQL);
				 pstmt.setInt(1,getNext()-(pageNumber -1 ) * 10); //getnext 는 그 다음으로 작성될 글의 번호를 입력하는데 
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
				 } //게시글 10개를 나타내는 부분 
			}catch(Exception e) {
				 e.printStackTrace();
			 }
			 return list;//list 를 반환
			 
		}
		public boolean nextPage(int pageNumber) {//게시글을 10개이상 이 넘을때 넥스트 페이지가 없다는걸 표시
			 String SQL= "SELECT * FROM BBS WHERE bbsID < ? AND bbsAvailable = 1 ORDER BY bbsID DESC LIMIT 10" ;
			try {
				 PreparedStatement pstmt= conn.prepareStatement(SQL);
				 pstmt.setInt(1,getNext()-(pageNumber -1 ) * 10); //getnext 는 그 다음으로 작성될 글의 번호를 입력하는데 
				 rs=pstmt.executeQuery();
				 if(rs.next()) {
				 return true;
				 } //게시글 10개를 나타내는 부분 
			}catch(Exception e) {
				 e.printStackTrace();
			 }
			 return false;//list 를 반환
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
						 

					 } //게시글 10개를 나타내는 부분 
				}catch(Exception e) {
					 e.printStackTrace();
				 }
				 return null;//list 를 반환
		}

	public  int update(int bbsID, String bbsTitle,String bbsContent) {
		 String SQL= "UPDATE BBS SET bbsTitle=?, bbsContent =? WHERE bbsID = ?";// 현재의 시간의 가져오는 MYSQL 용어
		 try {
			 PreparedStatement pstmt= conn.prepareStatement(SQL);
			 pstmt.setString(1, bbsTitle);
			 pstmt.setString(2, bbsContent);
			 pstmt.setInt(3,bbsID);
			 //rs=pstmt.executeQuery(); insert문 자체는 update 문으로 작동하기 떄문에 쿼리문이 필요가없다
			 return pstmt.executeUpdate() ; 
		 
		 }catch(Exception e) {
			 e.printStackTrace();
		 }
		 return -1;//데이터베이스 오류 
	}
	public int update1(int bbsID, String bbsTitle, String bbsContent) {
		 String SQL= "UPDATE BBS SET bbsTitle= ? , bbsContent = ? WHERE bbsID=?";// 현재의 시간의 가져오는 MYSQL 용어
		 try {
			 PreparedStatement pstmt= conn.prepareStatement(SQL);			 
			 pstmt.setString(1, bbsTitle);
			 pstmt.setString(2, bbsContent);
			 pstmt.setInt(3, bbsID);
			 //rs=pstmt.executeQuery(); insert문 자체는 update 문으로 작동하기 떄문에 쿼리문이 필요가없다
			 return pstmt.executeUpdate() ; 
		 
		 }catch(Exception e) {
			 e.printStackTrace();
		 }
		 return -1;//데이터베이스 오류 
	}
	public int delete(int bbsID) {
		 String SQL= "UPDATE BBS SET bbsAvailable = 0 WHERE bbsID=?";// 현재의 시간의 가져오는 MYSQL 용어
		 try {
			 PreparedStatement pstmt= conn.prepareStatement(SQL);			 
			 pstmt.setInt(1, bbsID);
			 
			 //rs=pstmt.executeQuery(); insert문 자체는 update 문으로 작동하기 떄문에 쿼리문이 필요가없다
			 return pstmt.executeUpdate() ; 
		 // 결과가 성공적이라면 0 이상의 수 실패라하면 -1을 리턴한다
		 }catch(Exception e) {
			 e.printStackTrace();
		 }
		 return -1;//데이터베이스 오류 
	}
}
