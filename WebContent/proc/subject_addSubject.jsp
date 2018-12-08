<%@page import="java.io.IOException"%>
<%@page import="Util.*"%>
<%@page import="ClassObject.*"%>
<%@page import="DAO.*"%>
<%@ page language="java" contentType="application/json; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<%@ page import="java.sql.*" %>
<%!

private void makeMyResponse(HttpServletRequest req, JspWriter out) throws IOException
{	
	String subj_name;
	double score;
	
	// 1. Install Parameters
	subj_name = req.getParameter("subj_name");
	if(OurProcResp.reqParamVoidString(subj_name))
	{
		OurProcResp.printResp(
				out,
				"������� ������ϴ�",
				"subj_name",
				null);
		return;
	}
	
	try {
		score = Double.parseDouble(req.getParameter("score"));
	}
	catch(Exception e){
		OurProcResp.printResp(
				out,
				"������ ���ڿ��� �մϴ�.",
				"score",
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
			break;
			
		case INVALID_SUBJNAME:
			OurProcResp.printResp(
					out,
					"�ùٸ��� ���� ������Դϴ�.",
					"subj_name",
					null);
			break;
			
		case INVALID_SCORE:
			OurProcResp.printResp(
					out,
					"�ùٸ��� ���� �����Դϴ�.",
					"score",
					null);
			break;
			
		case COLLISION_SUBJNAME:
			OurProcResp.printResp(
					out,
					"�̹� ������ ������� �ֽ��ϴ�.",
					"subj_name",
					null);
			break;
		}
		
	}
	catch(SQLException sqle)
	{ 
		OurProcResp.printResp(
				out,
				"DB������ �߻��Ͽ����ϴ�.",
				null,
				null);
		sqle.printStackTrace();
	}
	
}
%>
<%
// Parameter: subj_name=�����, score=����
// ���� �߰��� �̷������.
// data ����
request.setCharacterEncoding("euc-kr");
makeMyResponse(request, out);

%>