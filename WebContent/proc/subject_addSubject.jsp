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
				"������� ������ϴ�",
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
				"������ ���ڿ��� �մϴ�.",
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
					"�ùٸ��� ���� ������Դϴ�.",
					rp_subj_name,
					null);
			return;
			
		case INVALID_SCORE:
			OurProcResp.printResp(
					out,
					"�ùٸ��� ���� �����Դϴ�.",
					rp_score,
					null);
			return;
			
		case COLLISION_SUBJNAME:
			OurProcResp.printResp(
					out,
					"�̹� ������ ������� �ֽ��ϴ�.",
					rp_subj_name,
					null);
			return;
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
// ���� �߰��� �� �� �̷������.
// data ����
request.setCharacterEncoding("euc-kr");
makeMyResponse(request, out);

%>