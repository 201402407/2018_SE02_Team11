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
<% request.setCharacterEncoding("euc-kr"); %>

<%!
private void makeMyResponse(HttpServletRequest req, JspWriter out) throws IOException
{	
	int sid;
	final String rp_sid = "sid";
	
	// 1. Install Parameters (DAO에 넣을 수 있게)
	try {
		sid = Integer.parseInt( req.getParameter(rp_sid) );
	} catch (Exception e) {
		//형변환실패
		OurProcResp.printResp(out, "학번을 제대로 입력해주세요.", rp_sid, null);
		return;
	}
	
	// 2. do DAO Job
	try
	{
		LectureDAO dao = new LectureDAO();
		List<LectureDetail> list = dao.getApplyLectureList(sid);
		
		if(list == null)
		{
			OurProcResp.printResp(out, "현재 휴학중이시거나 조회가능기간이 아닙니다.", null, null);
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
		OurProcResp.printResp(out, "DB오류가 발생하였습니다. 학번이 제대로 되어있습니까?", null, null);
		return;
	}
}
%>

<%
request.setCharacterEncoding("euc-kr");
makeMyResponse(request, out);
%>