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
	ChangeType changeType;
	final String rp_changeType = "changeType";
	int startSemester;
	final String rp_startSemester = "startSemester";
	int endSemester;
	final String rp_endSemester = "endSemester";
	String reason;
	final String rp_reason = "reason";
	int sid;
	final String rp_sid = "sid";
	
	// 1. Install Parameters (DAO에 넣을 수 있게)
	try {
		changeType = OurProcResp.getValidChangeType(
				req.getParameter(rp_changeType)
				);
	} catch (Exception e) {
		//형변환실패
		OurProcResp.printResp(out, "휴복학구분을 제대로 입력해주세요.", rp_changeType, null);
		return;
	}
	
	try {
		startSemester = OurProcResp.getValidSemester(req.getParameter(rp_startSemester));
	} catch (Exception e) {
		//형변환실패
		OurProcResp.printResp(out, "시작학기를 제대로 입력해주세요.", rp_startSemester, null);
		return;
	}
	
	try {
		endSemester = OurProcResp.getValidSemester( req.getParameter(rp_endSemester) );
	} catch (Exception e) {
		//형변환실패
		OurProcResp.printResp(out, "종료학기를 제대로 입력해주세요.", rp_endSemester, null);
		return;
	}
	
	reason = req.getParameter(rp_reason);
	if(OurProcResp.reqParamVoidString(reason))
	{
		//문자열공백
		OurProcResp.printResp(out, "학적변동의 사유가 비었습니다", rp_reason, null);
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
		ChangeRecordDAO dao = new ChangeRecordDAO();
		switch(dao.requestTimeOnOffs(changeType, startSemester, endSemester, reason, sid))
		{
		case SUCCESS:
			OurProcResp.printResp(out, null, null, null);
			return;
		case CURRENTLY_UNAVAILABLE:
			OurProcResp.printResp(out, "학기중에는 휴복학신청이 불가합니다.", null, null);
			return;
		case INVALID_SEMESTER:
			OurProcResp.printResp(out, "종료학기와 시작학기를 다시 확인해주세요.", null, null);
			return;
		case RESUME_ON_TIMEON:
			OurProcResp.printResp(out, "재학중에는 복학신청이 불가합니다.", null, null);
			return;
		case TAKEOFF_ON_TIMEOFF:
			OurProcResp.printResp(out, "휴학중에는 휴학신청이 불가합니다.", null, null);
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
request.setCharacterEncoding("euc-kr");
makeMyResponse(request, out);
%>