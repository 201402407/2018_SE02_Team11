<%@page import="java.time.format.DateTimeFormatter"%>
<%@page import="java.util.List"%>
<%@page import="org.json.simple.JSONArray"%>
<%@page import="org.json.simple.JSONObject"%>
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
	int sid;
	final String rp_sid = "sid";
	String subjectName;
	final String rp_subjectName = "subjectName";
	
	// 1. Install Parameters (DAO�� ���� �� �ְ�)
	try {
		sid = Integer.parseInt( req.getParameter(rp_sid) );
	} catch (Exception e) {
		//����ȯ����
		OurProcResp.printResp(out, "�й��� ����� �Է����ּ���.", rp_sid, null);
		return;
	}
	subjectName = req.getParameter(rp_subjectName);
		if(OurProcResp.reqParamVoidString(subjectName))
		{
			//���ڿ�����
			OurProcResp.printResp(out, "������� ������ϴ�", rp_subjectName, null);
			return;
		}
	
	// 2. do DAO Job
	try
	{
		LectureDAO dao = new LectureDAO();
		List<LectureDetail> list = dao.getLectureByName(sid, subjectName);
		
		if(list == null)
		{
			OurProcResp.printResp(out, "���� �������̽ðų� ��ȸ���ɱⰣ�� �ƴմϴ�.", null, null);
			return;
		}
				
		JSONArray listJson = new JSONArray();
		for(LectureDetail elem : list)
		{
			JSONObject elemJson = new JSONObject();
			elemJson.put("lectureCode", elem.getLectureCode());
			elemJson.put("subjectName", elem.getSubjectName());
			elemJson.put("profName", elem.getProfName());
			elemJson.put("registerTerm", elem.getRegisterTerm());
			elemJson.put("applyNum", elem.getApplyNum());
			elemJson.put("allNum", elem.getAllNum());
			elemJson.put("dayOfWeek", elem.getDayOfWeek().toString());
			elemJson.put("startTime", elem.getStartTime().format(DateTimeFormatter.ISO_TIME));
			elemJson.put("endTime", elem.getEndTime().format(DateTimeFormatter.ISO_TIME));
			elemJson.put("score", elem.getScore());
			
			listJson.add(elemJson);
		}
		OurProcResp.printResp(out, null, null, listJson);
		return;
	}
	catch(Exception e)
	{
		e.printStackTrace();
		OurProcResp.printResp(out, "DB������ �߻��Ͽ����ϴ�. �й��� ����� �Ǿ��ֽ��ϱ�?", null, null);
		return;
	}
}
%>

<%
request.setCharacterEncoding("utf-8");
makeMyResponse(request, out);
%>