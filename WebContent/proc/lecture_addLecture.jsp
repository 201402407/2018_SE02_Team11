<%@page import="java.time.LocalTime"%>
<%@page import="java.time.DayOfWeek"%>
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
	
	int subjcode;
	final String rp_subjcode = "subjcode";
	int profcode;
	final String rp_profcode = "profcode";
	int depcode;
	final String rp_depcode = "depcode";
	int registerterm;
	final String rp_registerterm = "registerterm";
	int allnum;
	final String rp_allnum = "allnum";
	DayOfWeek dayofweek;
	final String rp_dayofweek = "dayofweek";
	LocalTime starttime;
	final String rp_starttime = "starttime";
	LocalTime endtime;
	final String rp_endtime = "endtime";
	String syltext;
	final String rp_syltext = "syltext";
	
	// 1. Install Parameters (DAO�� ���� �� �ְ�)
	try {
		subjcode = Integer.parseInt( req.getParameter(rp_subjcode) );
	} catch (Exception e) {
		//����ȯ����
		OurProcResp.printResp(out, "�����ڵ带 ����� �Է����ּ���.", rp_subjcode, null);
		return;
	}
	
	try {
		profcode = Integer.parseInt( req.getParameter(rp_profcode) );
	} catch (Exception e) {
		//����ȯ����
		OurProcResp.printResp(out, "������Ϲ�ȣ�� ����� �Է����ּ���.", rp_profcode, null);
		return;
	}
	
	try {
		depcode = Integer.parseInt( req.getParameter(rp_depcode) );
	} catch (Exception e) {
		//����ȯ����
		OurProcResp.printResp(out, "�а��ڵ带 ����� �Է����ּ���.", rp_depcode, null);
		return;
	}
	
	try {
		registerterm = OurProcResp.getValidSemester( req.getParameter(rp_registerterm) );
	} catch (Exception e) {
		//����ȯ����
		OurProcResp.printResp(out, "����б⸦ ����� �Է����ּ���.", rp_registerterm, null);
		return;
	}
	
	try {
		allnum = Integer.parseInt( req.getParameter(rp_allnum) );
	} catch (Exception e) {
		//����ȯ����
		OurProcResp.printResp(out, "��ü�ο��� ����� �Է����ּ���.", rp_allnum, null);
		return;
	}
	
	try {
		dayofweek = OurProcResp.getValidDayOfWeek( req.getParameter(rp_dayofweek) );
	} catch (Exception e) {
		//����ȯ����
		OurProcResp.printResp(out, "������ ����� �Է����ּ���.", rp_dayofweek, null);
		return;
	}
	
	try {
		starttime = OurProcResp.getValidLocalTime( req.getParameter(rp_starttime) );
	} catch (Exception e) {
		//����ȯ����
		OurProcResp.printResp(out, "���ǽ��۽ð��� ����� �Է����ּ���.", rp_starttime, null);
		return;
	}
	
	try {
		endtime = OurProcResp.getValidLocalTime( req.getParameter(rp_endtime) );
	} catch (Exception e) {
		//����ȯ����
		OurProcResp.printResp(out, "��������ð��� ����� �Է����ּ���.", rp_endtime, null);
		return;
	}
	
	syltext = req.getParameter(rp_syltext);
	// syltext = ���ǰ�ȹ���� null�̾, ""�̾ �ȴ�.
	
	
	// 2. do DAO Job
	try
	{
		LectureDAO dao = new LectureDAO();
		switch (dao.addLecture(subjcode, profcode, depcode, registerterm, allnum, dayofweek, starttime, endtime, syltext) )
		{
		case SUCCESS:
			OurProcResp.printResp(out, null, null, null);
			return;
		case NOT_FOUND_SUBJCODE:
			OurProcResp.printResp(out, "���� ������Դϴ�.", rp_subjcode, null);
			return;
		case NOT_FOUND_DEPCODE:
			OurProcResp.printResp(out, "���� �а��ڵ��Դϴ�.", rp_depcode, null);
			return;
		case NOT_FOUND_PROFCODE:
			OurProcResp.printResp(out, "���� ������Ϲ�ȣ�Դϴ�.", rp_profcode, null);
			return;
		case INVALID_TERM:
			OurProcResp.printResp(out, "����бⰡ �ߵ��Ǿ����ϴ�.", rp_registerterm, null);
			return;
		case INVALID_ALLNUM:
			OurProcResp.printResp(out, "��ü�ο� ���� �߸��Ǿ����ϴ�.", rp_allnum, null);
			return;
		case INVALID_DAY_OF_WEEK:
			OurProcResp.printResp(out, "������ �߸��Ǿ����ϴ�.", rp_dayofweek, null);
			return;
		case INVALID_S_E_TIME:
			OurProcResp.printResp(out, "�ð��ð��� ����ð��� �ٽ� Ȯ�����ּ���.", rp_starttime, null);
			return;
		}
	}
	catch(Exception e)
	{
		e.printStackTrace();
		OurProcResp.printResp(out, "DB������ �߻��Ͽ����ϴ�.", null, null);
		return;
	}
}
%>

<%
request.setCharacterEncoding("euc-kr");
makeMyResponse(request, out);
%>