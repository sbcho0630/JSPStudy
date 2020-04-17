package com.sist.board.model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sist.controller.Controller;
import com.sist.controller.RequestMapping;

import java.text.SimpleDateFormat;
import java.util.*;
import com.sist.dao.*;
import com.sist.vo.BoardVO;

@Controller
/*
 * 						MODEL => DAO,Service,VO
 * 사용자 요청 (페이지) ==========>  MODEL (DAO) =================>JSP출력
 * 							  =======
 * 								@RequestMapping(사용자가 요청한 URI주소)			
 */
public class FreeBoardModel {
	@RequestMapping("freeboard/list.do")
	public String freeboard_list(HttpServletRequest request, HttpServletResponse response) {
		//요청한 page받기
		String page=request.getParameter("page");
		if(page==null)
			page="1";
		int curPage=Integer.parseInt(page);
		FreeBoardDAO dao=new FreeBoardDAO();
		List<BoardVO> list=dao.freeboardListData(curPage);
		int totalPage=dao.freeboardTotalPage();
		
		request.setAttribute("curPage", curPage);
		request.setAttribute("totalPage", totalPage);
		request.setAttribute("list", list);
		request.setAttribute("today", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
		request.setAttribute("main_jsp", "../freeboard/list.jsp");
		return "../main/main.jsp";
	}
	
	@RequestMapping("freeboard/insert.do")
	public String freeboard_insert(HttpServletRequest request, HttpServletResponse response){
		request.setAttribute("main_jsp", "../freeboard/insert.jsp");
		return "../main/main.jsp";
	}
	@RequestMapping("freeboard/insert_ok.do")
	public String freeboard_insert_ok(HttpServletRequest request, HttpServletResponse response){
		// 메소드 => 매개변수가 3개 이상이면 클래스로 묶어서 전송
		//						   =======~VO (구조체)
		//사용자가 보낸 데이터를 받는다
		try {
			request.setCharacterEncoding("UTF-8");
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		String name=request.getParameter("name");
		String subject=request.getParameter("subject");
		String content=request.getParameter("content");
		String pwd=request.getParameter("pwd");
		BoardVO vo=new BoardVO();
		vo.setName(name);
		vo.setSubject(subject);
		vo.setContent(content);
		vo.setPwd(pwd);
		
		//DAO로 전송 => DAO에서 오라클로 보내준다
		FreeBoardDAO dao=new FreeBoardDAO();
		//Insert 요청
		dao.freeboardInsert(vo);
		return "redirect:../freeboard/list.do";
	}

	@RequestMapping("freeboard/detail.do")
	public String freeboard_detail(HttpServletRequest request, HttpServletResponse response){
		String no=request.getParameter("no");
		// no주고 vo받기
		FreeBoardDAO dao=new FreeBoardDAO();
		//vo받기
		BoardVO vo=dao.freeboardInfoData(Integer.parseInt(no), 1);
		//jsp로 보내기
		request.setAttribute("vo", vo);
		request.setAttribute("main_jsp", "../freeboard/detail.jsp");	
		return "../main/main.jsp";
	}
	
	@RequestMapping("freeboard/update.do")
	public String freeboard_update(HttpServletRequest request, HttpServletResponse response){
		String no=request.getParameter("no");
		// no주고 vo받기
		FreeBoardDAO dao=new FreeBoardDAO();
		//vo받기
		BoardVO vo=dao.freeboardInfoData(Integer.parseInt(no), 2);
		//jsp로 보내기
		request.setAttribute("vo", vo);
		request.setAttribute("main_jsp", "../freeboard/update.jsp");	
		return "../main/main.jsp";
	}
	@RequestMapping("freeboard/update_ok.do")
	public String freeboard_update_ok(HttpServletRequest request, HttpServletResponse response){
		// 메소드 => 매개변수가 3개 이상이면 클래스로 묶어서 전송
		//						   =======~VO (구조체)
		//사용자가 보낸 데이터를 받는다
		System.out.println("updateOK");
		try {
			request.setCharacterEncoding("UTF-8");
		} catch (Exception e) {
			e.printStackTrace();// TODO: handle exception
		}
		
		String name=request.getParameter("name");
		String subject=request.getParameter("subject");
		String content=request.getParameter("content");
		String pwd=request.getParameter("pwd");
		String no=request.getParameter("no");

		BoardVO vo=new BoardVO();
		vo.setName(name);
		vo.setSubject(subject);
		vo.setContent(content);
		vo.setPwd(pwd);
		vo.setNo(Integer.parseInt(no));
		//DAO로 전송 => DAO에서 오라클로 보내준다
		FreeBoardDAO dao=new FreeBoardDAO();
		//update 요청
		boolean bCheck=dao.freeboardUpdate(vo);
		request.setAttribute("bCheck", bCheck);
		request.setAttribute("no", no);
		return "../freeboard/update_ok.jsp";
	}
	
	@RequestMapping("freeboard/delete.do")
	public String freeboard_delete(HttpServletRequest request, HttpServletResponse response){
		
		String no=request.getParameter("no");
		request.setAttribute("no", no);
		request.setAttribute("main_jsp", "../freeboard/delete.jsp");
		return "../main/main.jsp";
	}
	
	@RequestMapping("freeboard/delete_ok.do")
	public String freeboard_delete_ok(HttpServletRequest request, HttpServletResponse response){
		String no=request.getParameter("no");
		String pwd=request.getParameter("pwd");
		//DAO연결
		FreeBoardDAO dao=new FreeBoardDAO();
		
		boolean bCheck=dao.freeboardDelete(Integer.parseInt(no), pwd);
		request.setAttribute("bCheck", bCheck);
		return "../freeboard/delete_ok.jsp";
	}
}


	
