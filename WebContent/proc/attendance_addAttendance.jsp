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
	int lcode;
	final String rp_lcode = "lcode";
	int sid;
	final String rp_sid = "sid";
	
	// 1. Install Parameters (DAO에 넣을 수 있게)
	try {
		lcode = Integer.parseInt( req.getParameter(rp_lcode) );
	} catch (Exception e) {
		//형변환실패
		OurProcResp.printResp(out, "분반코드를 제대로 입력해주세요.", rp_lcode, null);
		return;
	}
	try {
		sid = Integer.parseInt( req.getParameter(rp_sid) );
	} catch (Exception e) {
		//형변환실패
		OurProcResp.printResp(out, "학번을 제대로 입력해주세요.", rp_sid, null);
		return;
	}
	
	// 2. do DAO Job
	try
	{
		AttendanceDAO dao = new AttendanceDAO();
		switch(dao.addAttendance(lcode, sid))
		{
		case SUCCESS:
			OurProcResp.printResp(out, null, null, null);
			return;
		case NO_SUCH_STUDENT:
			OurProcResp.printResp(out, "존재하지 않는 학번입니다.", null, null);
			return;
		case UNABLE_ADD_ATTENDANCE:
			OurProcResp.printResp(out, "수강신청 불가기간이거나 휴학중이십니다.", null, null);
			return;
		case NOT_ENOUGH_NUM:
			OurProcResp.printResp(out, "더 이상 여석이 없습니다. 죄송합니다.", null, null);
			return;
		case NOT_ENOUGH_SCORE:
			OurProcResp.printResp(out, "학점 제한으로 인해 더 이상의 수강은 불가합니다.", null, null);
			return;
		case COLLISION_TIMETABLE:
			OurProcResp.printResp(out, "시간표 충돌!", null, null);
			return;
		}
	}
	catch(Exception e)
	{
		e.printStackTrace();
		OurProcResp.printResp(out, "DB오류가 발생하였습니다.", null, null);
		return;
	}
}
%>

<%
request.setCharacterEncoding("euc-kr");
makeMyResponse(request, out);
%>