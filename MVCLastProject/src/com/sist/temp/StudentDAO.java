package com.sist.temp;
import java.util.*;

import oracle.jdbc.internal.OracleTypes;

import java.sql.*;
public class StudentDAO {
	private Connection conn;
	private CallableStatement cs; // Procedure 불러오는 메소드
	private final String URL="jdbc:oracle:thin:@localhost:1521:XE";
	
	public StudentDAO(){
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch (Exception e) {
			System.out.println(e.getMessage());// TODO: handle exception
		}
	}
	public void getConnection(){
		try {
			conn=DriverManager.getConnection(URL,"hr","happy");
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	public void disConnection(){
		try {
			if(cs!=null) cs.close();
			if(conn!=null) conn.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	public StudentVO studentInfoData(int no){
		StudentVO vo=new StudentVO();
		try {
			getConnection();
			String sql="{CALL pro_test_select(?,?,?,?,?)}";
			cs=conn.prepareCall(sql);
			cs.setInt(1, no);
			cs.registerOutParameter(2, OracleTypes.VARCHAR);
			cs.registerOutParameter(3, OracleTypes.INTEGER);
			cs.registerOutParameter(4, OracleTypes.INTEGER);
			cs.registerOutParameter(5, OracleTypes.INTEGER);
			// procedure 실행 => executeUpdate();
			cs.executeUpdate();
			
			String name=cs.getString(2);
			int kor=cs.getInt(3);
			int eng=cs.getInt(4);
			int math=cs.getInt(5);
			
			vo.setName(name);
			vo.setKor(kor);
			vo.setEng(eng);
			vo.setMath(math);
			
		} catch (Exception e) {
			e.printStackTrace();// TODO: handle exception
		}finally{
			disConnection();
		}
		return vo;
	}
}
