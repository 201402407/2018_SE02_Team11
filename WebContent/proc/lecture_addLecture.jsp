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
	
	// 1. Install Parameters (DAO에 넣을 수 있게)
	try {
		subjcode = Integer.parseInt( req.getParameter(rp_subjcode) );
	} catch (Exception e) {
		//형변환실패
		OurProcResp.printResp(out, "과목코드를 제대로 입력해주세요.", rp_subjcode, null);
		return;
	}
	
	try {
		profcode = Integer.parseInt( req.getParameter(rp_profcode) );
	} catch (Exception e) {
		//형변환실패
		OurProcResp.printResp(out, "교수등록번호를 제대로 입력해주세요.", rp_profcode, null);
		return;
	}
	
	try {
		depcode = Integer.parseInt( req.getParameter(rp_depcode) );
	} catch (Exception e) {
		//형변환실패
		OurProcResp.printResp(out, "학과코드를 제대로 입력해주세요.", rp_depcode, null);
		return;
	}
	
	try {
		registerterm = OurProcResp.getValidSemester( req.getParameter(rp_registerterm) );
	} catch (Exception e) {
		//형변환실패
		OurProcResp.printResp(out, "등록학기를 제대로 입력해주세요.", rp_registerterm, null);
		return;
	}
	
	try {
		allnum = Integer.parseInt( req.getParameter(rp_allnum) );
	} catch (Exception e) {
		//형변환실패
		OurProcResp.printResp(out, "전체인원을 제대로 입력해주세요.", rp_allnum, null);
		return;
	}
	
	try {
		dayofweek = OurProcResp.getValidDayOfWeek( req.getParameter(rp_dayofweek) );
	} catch (Exception e) {
		//형변환실패
		OurProcResp.printResp(out, "요일을 제대로 입력해주세요.", rp_dayofweek, null);
		return;
	}
	
	try {
		starttime = OurProcResp.getValidLocalTime( req.getParameter(rp_starttime) );
	} catch (Exception e) {
		//형변환실패
		OurProcResp.printResp(out, "강의시작시각을 제대로 입력해주세요.", rp_starttime, null);
		return;
	}
	
	try {
		endtime = OurProcResp.getValidLocalTime( req.getParameter(rp_endtime) );
	} catch (Exception e) {
		//형변환실패
		OurProcResp.printResp(out, "강의종료시각을 제대로 입력해주세요.", rp_endtime, null);
		return;
	}
	
	syltext = req.getParameter(rp_syltext);
	// syltext = 강의계획서는 null이어도, ""이어도 된다.
	
	
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
			OurProcResp.printResp(out, "없는 과목명입니다.", rp_subjcode, null);
			return;
		case NOT_FOUND_DEPCODE:
			OurProcResp.printResp(out, "없는 학과코드입니다.", rp_depcode, null);
			return;
		case NOT_FOUND_PROFCODE:
			OurProcResp.printResp(out, "없는 교수등록번호입니다.", rp_profcode, null);
			return;
		case INVALID_TERM:
			OurProcResp.printResp(out, "등록학기가 잘돗되었습니다.", rp_registerterm, null);
			return;
		case INVALID_ALLNUM:
			OurProcResp.printResp(out, "전체인원 수가 잘못되었습니다.", rp_allnum, null);
			return;
		case INVALID_DAY_OF_WEEK:
			OurProcResp.printResp(out, "요일이 잘못되었습니다.", rp_dayofweek, null);
			return;
		case INVALID_S_E_TIME:
			OurProcResp.printResp(out, "시각시각과 종료시각을 다시 확인해주세요.", rp_starttime, null);
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