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
		sid = Integer.parseInt(req.getParameter(rp_sid));
	} catch (Exception e) {
		//형변환실패
		OurProcResp.printResp(out, "학번을 제대로 입력해주세요.", rp_sid, null);
		return;
	}
	
	// 2. do DAO Job
	try
	{
		ChangeRecordDAO dao = new ChangeRecordDAO();
		List<ChangeRecord> list = dao.getChangeRecordListBySID(sid);
				
		JSONArray listJson = new JSONArray();
		for(ChangeRecord elem : list)
		{
			int changeType;
			switch(elem.getChangeType())
			{
			case TAKEOFF:
				changeType = 0;
				break;
			case RESUME:
				changeType = 1;
				break;
			default:
				changeType = -1;
			}
			
			JSONObject elemJson = new JSONObject();
			elemJson.put("changerecordNum", elem.getChangerecordNum());
			elemJson.put("changeDate", elem.getChangeDate().toString());
			elemJson.put("changeType", changeType);
			elemJson.put("startSemester", elem.getStartSemester());
			elemJson.put("endSemester", elem.getEndSemester());
			elemJson.put("reason", elem.getReason());
			elemJson.put("studentID", elem.getStudentID());
			
			listJson.add(elemJson);
		}
		OurProcResp.printResp(out, null, null, listJson);
		return;
	}
	catch(SQLException sqle)
	{
		sqle.printStackTrace();
		OurProcResp.printResp(out, "DB오류가 발생하였습니다.", null, null);
		return;
	}
}
%>

<%
request.setCharacterEncoding("euc-kr");
makeMyResponse(request, out);
%>