package com.sist.dao;

import java.util.*;

import org.apache.ibatis.annotations.Results;

import com.sist.vo.BoardVO;

import oracle.jdbc.internal.OracleTypes;

import java.sql.*;

public class FreeBoardDAO {
	private Connection conn;
	private CallableStatement cs; // procedure 호출할때 쓰는 함수
	private final String URL = "jdbc:oracle:thin:@localhost:1521:XE";

	public FreeBoardDAO() {
		try {
			// 드라이버 등록
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	// SqlSession session=ssf.openSession()
	// 연결
	public void getConnection() {
		try {
			conn = DriverManager.getConnection(URL, "hr", "happy");
		} catch (Exception e) {
		}
	}

	// session.close()
	// 해제
	public void disConnection() {
		try {
			if (cs != null)
				cs.close();
			if (conn != null)
				conn.close();
		} catch (Exception e) {
		}
	}

	/*
	 * CREATE OR REPLACE PROCEDURE boardlistdata( pstart NUMBER, pend NUMBER,
	 * presult OUT SYS_REFCURSOR )
	 */
	// session.selectList()
	public List<BoardVO> freeboardListData(int page) {
		List<BoardVO> list = new ArrayList<BoardVO>();
		try {
			getConnection();
			int rowSize = 10;
			int start = (rowSize * page) - (rowSize - 1);
			int end = rowSize * page;

			String sql = "{CALL boardlistdata(?,?,?)}";
			cs = conn.prepareCall(sql); // 함수 호출

			// ?에 값채우기
			cs.setInt(1, start);
			cs.setInt(2, end);
			cs.registerOutParameter(3, OracleTypes.CURSOR);

			// 실행 요청
			cs.executeUpdate();// boardlistdata(?,?,?) 호출

			// object
			// 결과값
			ResultSet rs = (ResultSet) cs.getObject(3); // CURSOR를 ResultSet으로
														// 받음
			while (rs.next()) {
				BoardVO vo = new BoardVO();
				vo.setNo(rs.getInt(1));
				vo.setSubject(rs.getString(2));
				vo.setName(rs.getString(3));
				vo.setRegdate(rs.getDate(4));
				vo.setDbday(rs.getString(5));
				vo.setHit(rs.getInt(6));
				list.add(vo);
			}
			rs.close();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			disConnection();
		}
		return list;
	}

	// 총 페이지 구하기
	/*
	 * CREATE OR REPLACE PROCEDURE boardtotalpage( ptotal OUT NUMBER
	 */
	public int freeboardTotalPage() {
		int total = 0;
		try {
			getConnection();
			String sql="{CALL boardtotalpage(?)}";
			cs=conn.prepareCall(sql);
			cs.registerOutParameter(1, OracleTypes.INTEGER); // 정수 저장할 수 있는 공간(메모리) 만들어라
			
			//실행
			cs.executeUpdate();
			//메모리에서 결과값 가져오기
			total=cs.getInt(1);
		} catch (Exception e) {
			e.printStackTrace();// TODO: handle exception
		} finally {
			disConnection();	
		}
		return total;
	}
	/*
	 * CREATE OR REPLACE PROCEDURE boardInsert(
		 pname board.name%TYPE,
		 psubject board.subject%TYPE,
		 pcontent board.content%TYPE,
		 ppwd board.pwd%TYPE
)
	 */
	public void freeboardInsert(BoardVO vo){
		try {
			getConnection();
			String sql="{CALL boardInsert(?,?,?,?)}";
			cs=conn.prepareCall(sql);
			//?에 값 채우기
			cs.setString(1, vo.getName());
			cs.setString(2, vo.getSubject());
			cs.setString(3, vo.getContent());
			cs.setString(4, vo.getPwd());
			
			//실행
			cs.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			disConnection();
		}
		/*
		 * CREATE OR REPLACE PROCEDURE boarddetaildata(
 pno board.no%TYPE,
 ptype NUMBER,
 presult OUT SYS_REFCURSOR
)
		 */
	}
	public BoardVO freeboardInfoData(int no,int type){
		
		BoardVO vo=new BoardVO();
		try {
			getConnection();
			String sql="{CALL boarddetaildata(?,?,?)}";
			cs=conn.prepareCall(sql);
			//?값을 채운다
			cs.setInt(1, no);
			cs.setInt(2, type);
			cs.registerOutParameter(3, OracleTypes.CURSOR); // register 임시저장공간
			
			//실행
			cs.executeUpdate();
			
			ResultSet rs=(ResultSet)cs.getObject(3);
			rs.next();
			
			vo.setNo(rs.getInt(1));
			vo.setName(rs.getString(2));
			vo.setSubject(rs.getString(3));
			vo.setContent(rs.getString(4));
			vo.setRegdate(rs.getDate(5));
			vo.setHit(rs.getInt(6));
			
			rs.close();
			
		} catch (Exception e) {
			e.printStackTrace();// TODO: handle exception
		}finally{
			disConnection();
		}
		return vo;
	}
	/*
	 *   pname board.name%TYPE,
		 psubject board.subject%TYPE,
		 pcontent board.content%TYPE,
		 ppwd board.pwd%TYPE,
		 pno board.no%TYPE,
		 presult OUT VARCHAR2
)
	 */
	//수정하기
	public boolean freeboardUpdate(BoardVO vo){
			boolean bCheck=false;
			try {
				getConnection();
				//함수 호출
				String sql="{CALL boardupdate(?,?,?,?,?,?)}";
				cs=conn.prepareCall(sql);
				//?값채우기
				cs.setInt(1, vo.getNo());
				cs.setString(2, vo.getName());
				cs.setString(3, vo.getSubject());
				cs.setString(4, vo.getContent());
				cs.setString(5, vo.getPwd());
				cs.registerOutParameter(6, OracleTypes.VARCHAR);
				
				//실행
				cs.executeUpdate();
				String result=cs.getString(6);
				
				bCheck=Boolean.parseBoolean(result);
			} catch (Exception e) {
				e.printStackTrace();// TODO: handle exception
			}finally{
				disConnection();
			}
			System.out.println("비체크"+bCheck);
			return bCheck;
	}
	public boolean freeboardDelete(int no, String pwd){
		boolean bCheck=false;
		try {
			getConnection();
			//함수 호출
			String sql="{CALL boarddelete(?,?,?)}";
			cs=conn.prepareCall(sql);
			//?값채우기
			cs.setInt(1, no);
			cs.setString(2, pwd);
			cs.registerOutParameter(3, OracleTypes.VARCHAR);
			
			//실행
			cs.executeUpdate();
			String result=cs.getString(3);
			
			bCheck=Boolean.parseBoolean(result);
		} catch (Exception e) {
			e.printStackTrace();// TODO: handle exception
		}finally{
			disConnection();
		}
		return bCheck;
}
}
