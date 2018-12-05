<%@page import="ClassObject.LectureDetail"%>
<%@page import="DAO.LectureDAO"%>
<%@page import="DAO.LectureDAO.AddLectureResult"%>
<%@ page import="java.util.Stack"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.time.DayOfWeek"%>
<%@ page import="java.time.LocalTime"%>
<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<%@ page import="java.sql.*" %>
<% request.setCharacterEncoding("euc-kr"); %>

<%
	int scode = Integer.valueOf(request.getParameter("subjectCode"));
	int pcode = Integer.valueOf(request.getParameter("professorCode"));
	int dcode = Integer.valueOf(request.getParameter("departmentCode"));
	int registerterm = Integer.valueOf(request.getParameter("registerTerm"));
	int allnum = Integer.valueOf(request.getParameter("allNum"));
	DayOfWeek dayofweek = DayOfWeek.valueOf(request.getParameter("dayOfWeek"));
	
	// 시작시간
	String startTimeStr = request.getParameter("startTime");
	int starthour = Integer.valueOf(startTimeStr.substring(0, 2)); // 시간 쪼개기
	int startminute = Integer.valueOf(startTimeStr.substring(2, 4)); // 시간 쪼개기
	LocalTime start = LocalTime.of(starthour, startminute);
	
	// 종료시간
	String endTimeStr = request.getParameter("endTime");
	int endhour = Integer.valueOf(startTimeStr.substring(0, 2)); // 시간 쪼개기
	int endminute = Integer.valueOf(startTimeStr.substring(2, 4)); // 시간 쪼개기
	LocalTime end = LocalTime.of(endhour, endminute);
	
	String syltext = request.getParameter("syllabusText");
	
	LectureDAO lectureDAO = new LectureDAO();
	AddLectureResult result;
   
	try {
		result = lectureDAO.addLecture(scode, pcode, dcode, registerterm,
				allnum, dayofweek, start, end, syltext);
	 
		switch(result) {
		case SUCCESS: // 성공
			break;
		case NULL_IN_DB: // DB에 데이터가 없을 때
			break;
		case INVALID_ALLNUM: // 유효하지 않은 전체인원
			break;
		case INVALID_DAY_OF_WEEK: // 유효하지 않은 요일
			break;
		case INVALID_S_E_TIME: // 유효하지 않은 시간
			break;
		case INVALID_TERM: // 유효하지 않은 기간
			break;
		case NOT_FOUND_DEPCODE: // 등록된 학과코드를 찾을 수 없을 때
			break;
		case NOT_FOUND_PROFCODE: // 등록된 교수코드를 찾을 수 없을 때
			break;
		case NOT_FOUND_SUBJCODE: // 등록된 과목코드를 찾을 수 없을 때
			break;
		}
   }
   catch(SQLException e) {
	   e.printStackTrace();
   }
%>