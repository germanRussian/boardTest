package mvc.dao;

import java.sql.*;
import java.util.ArrayList;

import mvc.domain.Bbs;

public class BbsDAO {
	
	private Connection conn;
	private PreparedStatement stmt;
	private ResultSet rs;

	public BbsDAO() {
		try {
			String url = "jdbc:mysql://localhost:3306/bbs?characterEncoding=UTF-8&serverTimezone=Asia/Seoul";
			String user = "root";
			String password = "smart";
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection(url, user, password);			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void dbClose() {
		/*
		 * try { if(rs != null) rs.close(); if(stmt != null) stmt.close(); if(conn !=
		 * null) conn.close(); } catch (SQLException e) { // TODO Auto-generated catch
		 * block e.printStackTrace(); }
		 */
	}
	
	public int write(Bbs bbs) {
		String sql = "insert into bbs values(null, ?, ?, now(), ?, ?)";
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, bbs.getBbsTitle());
			stmt.setString(2, bbs.getUserID());
			stmt.setString(3, bbs.getBbsContent());
			stmt.setInt(4, 1);
			
			return stmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbClose();
		}		
		return -1;
	}
	
	public int getNext() {
		String sql = "SELECT bbsID from bbs order by bbsID desc";
		try {
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			if(rs.next()) {
				return rs.getInt("bbsID")+1;
			}
			return 1;
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return -1;
	}
	
	public ArrayList<Bbs> getList(int pageNumber) {
		String sql = "SELECT * from bbs WHERE bbsID < ? and bbsAvailable = 1 order by bbsID desc limit 10";
		
		ArrayList<Bbs> list = new ArrayList<Bbs>();
		int bbsId = getNext() - (pageNumber -1)*10;
		
		try {
			stmt = conn.prepareStatement(sql);
			
			stmt.setInt(1, bbsId);
			rs = stmt.executeQuery();
			while(rs.next()) {
				Bbs bbs = new Bbs();
				bbs.setBbsID(rs.getInt("bbsID"));
				bbs.setBbsTitle(rs.getString("bbsTitle"));
				bbs.setUserID(rs.getString("userID"));
				bbs.setBbsDate(rs.getString("bbsDate"));
				
				list.add(bbs);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbClose();
		}
		
		return list;
	}
	
	public boolean nextPage(int pageNumber) {
		String sql = "SELECT * from bbs WHERE bbsID < ? and bbsAvailable = 1";
		int bbsID = getNext() - (pageNumber -1)*10;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, bbsID);
			rs = stmt.executeQuery();
			if(rs.next()) {
				return true;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	public Bbs getBbs(int bbsID) {
		String sql = "SELECT * from bbs WHERE bbsID = ?";
		Bbs bbs = null;
		try {
			stmt = conn.prepareStatement(sql);
			
			stmt.setInt(1, bbsID);
			rs = stmt.executeQuery();
			
			if(rs.next()) {
				bbs = new Bbs();
				bbs.setBbsID(rs.getInt("bbsID"));
				bbs.setBbsTitle(rs.getString("bbsTitle"));
				bbs.setUserID(rs.getString("userID"));
				bbs.setBbsDate(rs.getString("bbsDate"));
				bbs.setBbsContent(rs.getString("bbsContent"));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbClose();
		}
		
		return bbs;
	}

	public int update(Bbs bbs) {
		String sql = "UPDATE bbs SET bbsTitle=?, bbsContent=? WHERE bbsID = ?";
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, bbs.getBbsTitle());
			stmt.setString(2, bbs.getBbsContent());
			stmt.setInt(3, bbs.getBbsID());
			
			return stmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbClose();
		}		
		return -1;
	}

	public int delete(int bbsID) {
		String sql = "UPDATE bbs SET bbsAvailable = 0 WHERE bbsID = ?";
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, bbsID);
			
			return stmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbClose();
		}		
		return -1;
	}
	
}
