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
// Parameter: subj_name=�����, score=����
// ���� �߰��� �̷������.
// data = true/false

String error = null;
String attention_field = null;
Object data = null;

// 1.
String sName = request.getParameter("subj_name");
if(OurProcResp.reqParamVoidString(sName))
{
	error = "������� ������ϴ�.";
	attention_field = "subj_name";
}

double score = 0.0;
try {score = Double.parseDouble(request.getParameter("score"));}
catch(Exception e){
	error = "������ ���ڿ��� �մϴ�.";
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
	 
	// �ߺ��̸� ������ ��
	if(result == AddSubjectResult.COLLISION_SUBJNAME) {
	 
	}
	
	// ����
	if(result == AddSubjectResult.SUCCESS) {
	 
	}
}

catch(SQLException e) {
	e.printStackTrace();
}
*/
%>