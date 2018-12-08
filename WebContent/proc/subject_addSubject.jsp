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
	String subj_name;
	String rp_subj_name = "subj_name";
	double score;
	String rp_score = "score";
	
	// 1. Install Parameters
	subj_name = req.getParameter(rp_subj_name);
	if(OurProcResp.reqParamVoidString(subj_name))
	{
		OurProcResp.printResp(
				out,
				"과목명이 비었습니다",
				rp_subj_name,
				null);
		return;
	}
	
	try {
		score = Double.parseDouble(req.getParameter(rp_score));
	}
	catch(Exception e){
		OurProcResp.printResp(
				out,
				"학점은 숫자여야 합니다.",
				rp_score,
				null);
		return;
	}
	
	// 2. Do DAO Job
	try {
		SubjectDAO subjDao = new SubjectDAO();
		switch(subjDao.addSubject(subj_name, score))
		{
		case SUCCESS:
			OurProcResp.printResp(
					out,
					null,
					null,
					null);
			return;
			
		case INVALID_SUBJNAME:
			OurProcResp.printResp(
					out,
					"올바르지 못한 과목명입니다.",
					rp_subj_name,
					null);
			return;
			
		case INVALID_SCORE:
			OurProcResp.printResp(
					out,
					"올바르지 못한 학점입니다.",
					rp_score,
					null);
			return;
			
		case COLLISION_SUBJNAME:
			OurProcResp.printResp(
					out,
					"이미 동일한 과목명이 있습니다.",
					rp_subj_name,
					null);
			return;
		}
		
	}
	catch(SQLException sqle)
	{ 
		OurProcResp.printResp(
				out,
				"DB오류가 발생하였습니다.",
				null,
				null);
		sqle.printStackTrace();
	}
	
}
%>
<%
// Parameter: subj_name=과목명, score=평점
// 과목에 추가가 한 줄 이루어진다.
// data 없음
request.setCharacterEncoding("euc-kr");
makeMyResponse(request, out);

%>