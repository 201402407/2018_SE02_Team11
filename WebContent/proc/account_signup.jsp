<%@page import="java.time.format.DateTimeParseException"%>
<%@page import="java.time.LocalDate"%>
<%@page import="java.io.IOException"%>
<%@page import="Util.*"%>
<%@page import="ClassObject.*"%>
<%@page import="DAO.*"%>
<%@ page language="java" contentType="application/json; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<%@ page import="java.sql.SQLException" %>
<% request.setCharacterEncoding("euc-kr"); %>

<%!
private void makeMyResponse(HttpServletRequest req, JspWriter out) throws IOException
{	
	String id;
	final String rp_id = "id";
	String pwd;
	final String rp_pwd= "pwd";
	String name;
	final String rp_name = "name";
	LocalDate birth;
	final String rp_birth = "birth";
	
	// 1. Install Parameters (문자열 공백검사 + 형변환실패 검사)
	id = req.getParameter(rp_id);
	if(OurProcResp.reqParamVoidString(id))
	{
		OurProcResp.printResp(out, "아이디가 비었습니다.", rp_id, null);
		return;
	}
	pwd = req.getParameter(rp_pwd);
	if(OurProcResp.reqParamVoidString(pwd))
	{
		OurProcResp.printResp(out, "패스워드가 비었습니다.", rp_pwd, null);
		return;
	}
	name = req.getParameter(rp_name);
	if(OurProcResp.reqParamVoidString(name))
	{
		OurProcResp.printResp(out, "이름이 비었습니다.", rp_name, null);
		return;
	}
	name = req.getParameter(rp_name);
	if(OurProcResp.reqParamVoidString(name))
	{
		OurProcResp.printResp(out, "이름이 비었습니다.", rp_name, null);
		return;
	}
	try {
		birth = LocalDate.parse( req.getParameter(rp_birth) );
	} catch (Exception e) {
		OurProcResp.printResp(out, "생일을 제대로 입력해주세요.", rp_birth, null);
		return;
	}
	
	// 2. do DAO Job
	try
	{
		AccountDAO dao = new AccountDAO();
		switch( dao.signUp(id, pwd, name, birth) )
		{
		case SUCCESS:
			OurProcResp.printResp(out, null, null, null);
			return;
		case INVALID_ID:
			OurProcResp.printResp(out, "아이디가 올바르지 않습니다.", rp_id, null);
			return;
		case INVALID_PWD:
			OurProcResp.printResp(out, "패스워드가 올바르지 않습니다.", rp_pwd, null);
			return;
		case INVALID_NAME:
			OurProcResp.printResp(out, "이름이 올바르지 않습니다.", rp_name, null);
			return;
		case INVALID_BIRTH:
			OurProcResp.printResp(out, "생일이 올바르지 않습니다.", rp_birth, null);
			return;
		}
	}
	catch(SQLException sqle)
	{
		sqle.printStackTrace();
		OurProcResp.printResp(out, "DB오류가 발생하였습니다. 혹시 아이디 중복일 수 있습니다.", null, null);
		return;
	}
}
%>

<%
// Parameter: id, pwd, name, birth
// 계정 테이블에 하나 추가된다. 학번요청 테이블에 하나 추가된다.
// data 없음
request.setCharacterEncoding("euc-kr");
makeMyResponse(request, out);
%>