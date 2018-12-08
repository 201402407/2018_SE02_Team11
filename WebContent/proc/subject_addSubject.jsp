<%@page import="org.json.simple.JSONArray"%>
<%@page import="Util.OurProcResp"%>
<%@page import="ClassObject.Subject"%>
<%@page import="DAO.SubjectDAO.AddSubjectResult"%>
<%@ page language="java" contentType="application/json; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<%@ page import="java.sql.*" %>
<%
	request.setCharacterEncoding("euc-kr");
%>

<%
// Parameter: subj_name=과목명, score=평점
// 과목에 추가가 이루어진다.
// data = true/false

String error = null;
String attention_field = null;
Object data = null;

// 1.
String sName = request.getParameter("subj_name");
if(OurProcResp.reqParamVoidString(sName))
{
	error = "과목명이 비었습니다.";
	attention_field = "subj_name";
}

double score = 0.0;
try {score = Double.parseDouble(request.getParameter("score"));}
catch(Exception e){
	error = "평점은 숫자여야 합니다.";
	attention_field = "score";
}

if (error != null)
{
	
}
else
{
	
}

/*
SubjectDAO subjectDAO = new SubjectDAO();

AddSubjectResult result;

try {
	result = subjectDAO.addSubject(sName, score);
	
	 // NOT_IN_DB
	if(result == AddSubjectResult.NULL_IN_DB) {
	 
	}
	 
	// 중복이름 존재할 때
	if(result == AddSubjectResult.COLLISION_SUBJNAME) {
	 
	}
	
	// 성공
	if(result == AddSubjectResult.SUCCESS) {
	 
	}
}

catch(SQLException e) {
	e.printStackTrace();
}
*/
%>