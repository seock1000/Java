package myDB;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.ResultSetMetaData;
import java.sql.DatabaseMetaData;

public class myDB {

	public static void main(String[] args) {
		// connect with database  
		Connection con = null;
		try { 
			Class.forName("com.mysql.cj.jdbc.Driver");
			String url = "jdbc:mysql://localhost/university";
			String user = "root", passwd = "0512";
			con = DriverManager.getConnection(url, user, passwd);
			System.out.println(con);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		/** update database
		Statement stmt = null;
		
		try {
			stmt = con.createStatement();
			String update = "update instructor set salary = salary * 1.1 where dept_name = 'Physics'";
			int count =stmt.executeUpdate(update);
			System.out.println(count);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		try {
			if(stmt != null && !stmt.isClosed()) stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		*/
		
		/* //retrieving result of query
		Statement stmt = null;
		ResultSet rs = null;
		
		try {
			stmt = con.createStatement();
			String sql = "select name, salary from instructor";
			rs = stmt.executeQuery(sql);
			
			while(rs.next()) {
				String name = rs.getString(1);
				if(rs.wasNull()) name = "null";
				String course_id = rs.getString(2);
				if(rs.wasNull()) course_id = "null";
				System.out.println(name+"\t"+course_id);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		try {
			if(stmt != null && !stmt.isClosed()) stmt.close();
			if(rs != null && !rs.isClosed()) rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		/** update database using variables in program
		PreparedStatement pstmt = null;
		
		try {
			String psql = "insert into instructor values (?, ?, ?, ?)";
			pstmt =con.prepareStatement(psql);
			
			String id = "12345", name = "Neumann", dept_name = "Biology";
			int salary = 98000;
			pstmt.setString(1, id);
			pstmt.setString(2, name);
			pstmt.setString(3, dept_name);
			pstmt.setInt(4, salary);
			
			int count = pstmt.executeUpdate();
			System.out.println(count);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		try {
			if(pstmt != null && !pstmt.isClosed()) pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		*/
		
		/*// metadata 1
		Statement stmt = null;
		ResultSet rs = null;
		
		try {
			stmt = con.createStatement();
			String sql = "select * from instructor";
			rs = stmt.executeQuery(sql);
			
			ResultSetMetaData rsmd = rs.getMetaData();
			for(int i = 1; i <= rsmd.getColumnCount(); i++) {
				System.out.print(rsmd.getColumnName(i)+" (");
				System.out.print(rsmd.getColumnTypeName(i)+")\t");
			}
			System.out.println();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		try {
			if(stmt != null && !stmt.isClosed()) stmt.close();
			if(rs != null && !rs.isClosed()) rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		*/
		
		// check metadata using database
		DatabaseMetaData dbmd = null;
		ResultSet mdrs = null;
		try {
			dbmd = con.getMetaData();
			mdrs = dbmd.getColumns("university", null, "student", null);
			String[] column = {"COLUMN_NAME", "TYPE_NAME"};
			System.out.println(column[0]+"\t"+column[1]);
			
			while(mdrs.next()) {
				String column_name = mdrs.getString(column[0]);
				String type_name = mdrs.getString(column[1]);
				System.out.println(column_name + "\t" + type_name);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		try {
			if(con != null && !con.isClosed()) con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
