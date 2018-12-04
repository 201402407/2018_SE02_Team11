package ClassObject;

import java.time.LocalDate;

public class TimeoffRequest {
	int reqNum;
	LocalDate reqDate;
	ChangeType changeType;
	String reason;
	int startSemester;
	int endSemester;
	int studentID;
	
	public TimeoffRequest() {
		
	}
	
	public TimeoffRequest(int reqNum, LocalDate reqDate, ChangeType changeType, String reason, int startSemester,
			int endSemester, int studentID) {
		super();
		this.reqNum = reqNum;
		this.reqDate = reqDate;
		this.changeType = changeType;
		this.reason = reason;
		this.startSemester = startSemester;
		this.endSemester = endSemester;
		this.studentID = studentID;
	}

	public int getReqNum() {
		return reqNum;
	}

	public void setReqNum(int reqNum) {
		this.reqNum = reqNum;
	}

	public LocalDate getReqDate() {
		return reqDate;
	}

	public void setReqDate(LocalDate reqDate) {
		this.reqDate = reqDate;
	}

	public ChangeType getChangeType() {
		return changeType;
	}

	public void setChangeType(ChangeType changeType) {
		this.changeType = changeType;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public int getStartSemester() {
		return startSemester;
	}

	public void setStartSemester(int startSemester) {
		this.startSemester = startSemester;
	}

	public int getEndSemester() {
		return endSemester;
	}

	public void setEndSemester(int endSemester) {
		this.endSemester = endSemester;
	}

	public int getStudentID() {
		return studentID;
	}

	public void setStudentID(int studentID) {
		this.studentID = studentID;
	}
	
	
}
