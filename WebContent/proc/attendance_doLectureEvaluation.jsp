<%@page import="java.io.IOException"%>
<%@page import="Util.*"%>
<%@page import="ClassObject.*"%>
<%@page import="DAO.*"%>
<%@ page language="java" contentType="application/json; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<%@ page import="java.sql.SQLException" %>

<%!
private void makeMyResponse(HttpServletRequest req, JspWriter out) throws IOException
{	
	int attNum;
	final String rp_attNum = "attNum";
	String evaltext;
	final String rp_evaltext = "evaltext";
	
	// 1. Install Parameters (DAO에 넣을 수 있게)
	try {
		attNum = Integer.parseInt( req.getParameter(rp_attNum) );
	} catch (Exception e) {
		//형변환실패
		OurProcResp.printResp(out, "수강번호를 제대로 입력해주세요.", rp_attNum, null);
		return;
	}
	evaltext = req.getParameter(rp_evaltext);
	if(OurProcResp.reqParamVoidString(evaltext))
	{
		//문자열공백
		OurProcResp.printResp(out, "강의평가 텍스트가 비었습니다", rp_evaltext, null);
		return;
	}
	
	// 2. do DAO Job
	try
	{
		AttendanceDAO dao = new AttendanceDAO();
		if( dao.doLectureEvaluation(attNum, evaltext) )
		{
			OurProcResp.printResp(out, null, null, null);
			return;
		}
		else
		{
			OurProcResp.printResp(out, "작성에 실패했습니다. 정해진 길이 내로 제출했는지 확인해 주세요.", null, null);
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
request.setCharacterEncoding("utf-8");
makeMyResponse(request, out);
%>