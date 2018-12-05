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
	
	// ���۽ð�
	String startTimeStr = request.getParameter("startTime");
	int starthour = Integer.valueOf(startTimeStr.substring(0, 2)); // �ð� �ɰ���
	int startminute = Integer.valueOf(startTimeStr.substring(2, 4)); // �ð� �ɰ���
	LocalTime start = LocalTime.of(starthour, startminute);
	
	// ����ð�
	String endTimeStr = request.getParameter("endTime");
	int endhour = Integer.valueOf(startTimeStr.substring(0, 2)); // �ð� �ɰ���
	int endminute = Integer.valueOf(startTimeStr.substring(2, 4)); // �ð� �ɰ���
	LocalTime end = LocalTime.of(endhour, endminute);
	
	String syltext = request.getParameter("syllabusText");
	
	LectureDAO lectureDAO = new LectureDAO();
	AddLectureResult result;
   
	try {
		result = lectureDAO.addLecture(scode, pcode, dcode, registerterm,
				allnum, dayofweek, start, end, syltext);
	 
		switch(result) {
		case SUCCESS: // ����
			break;
		case NULL_IN_DB: // DB�� �����Ͱ� ���� ��
			break;
		case INVALID_ALLNUM: // ��ȿ���� ���� ��ü�ο�
			break;
		case INVALID_DAY_OF_WEEK: // ��ȿ���� ���� ����
			break;
		case INVALID_S_E_TIME: // ��ȿ���� ���� �ð�
			break;
		case INVALID_TERM: // ��ȿ���� ���� �Ⱓ
			break;
		case NOT_FOUND_DEPCODE: // ��ϵ� �а��ڵ带 ã�� �� ���� ��
			break;
		case NOT_FOUND_PROFCODE: // ��ϵ� �����ڵ带 ã�� �� ���� ��
			break;
		case NOT_FOUND_SUBJCODE: // ��ϵ� �����ڵ带 ã�� �� ���� ��
			break;
		}
   }
   catch(SQLException e) {
	   e.printStackTrace();
   }
%>