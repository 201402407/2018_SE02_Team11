<%@page import="java.io.IOException"%>
<%@page import="Util.*"%>
<%@page import="ClassObject.*"%>
<%@page import="DAO.*"%>
<%@ page language="java" contentType="application/json; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<%@ page import="java.sql.SQLException" %>

<%!
private void makeMyResponse(HttpServletRequest req, JspWriter out, HttpSession session) throws IOException
{	
	//이미 로그인 상태면 실행불가
	if(session.getAttribute("accountID") != null)
	{
		OurProcResp.printResp(out, "이미 로그인되어 있습니다.", null, null);
		return;
	}
	
	String id;
	final String rp_id = "id";
	String pwd;
	final String rp_pwd = "pwd";
	
	// 1. Install Parameters (문자열 공백검사 + 형변환실패 검사)
	id = req.getParameter(rp_id);
	if(OurProcResp.reqParamVoidString(id))
	{
		//문자열공백
		OurProcResp.printResp(out, "아이디가 비었습니다" + id, rp_id, null);
		return;
	}
	pwd = req.getParameter(rp_pwd);
	if(OurProcResp.reqParamVoidString(pwd))
	{
		//문자열공백
		OurProcResp.printResp(out, "패스워드가 비었습니다", rp_pwd, null);
		return;
	}
	
	// 2. do DAO Job
	try
	{
		AccountDAO dao = new AccountDAO();
		switch (dao.login(id, pwd, session) )
		{
		case SUCCESS_ADMIN:
			OurProcResp.printResp(out, null, null, null);
			return;
		case SUCCESS_STUDENT:
			OurProcResp.printResp(out, null, null, null);
			return;
		case FAIL_STUDENT:
			OurProcResp.printResp(out, "아직 학번부여를 받지 못했습니다.", null, null);
			return;
		case INCORRECT_PWD:
			OurProcResp.printResp(out, "패스워드가 맞지 않습니다.", rp_pwd, null);
			return;
		case NOT_FOUND_ID:
			OurProcResp.printResp(out, "없는 아이디입니다.", rp_id, null);
			return;
		}
		
	}
	catch(SQLException sqle)
	{
		sqle.printStackTrace();
		OurProcResp.printResp(out, "DB오류가 발생하였습니다.", null, null);
		return;
	}
}
%>

<%
request.setCharacterEncoding("utf-8");
makeMyResponse(request, out, session);
%>