package ClassObject;

import java.sql.Date;

public class ChangeRecord {
	private int changerecordNum;
	private Date changeDate;
	private ChangeType changeType;
	private int startSemester;
	private int endSemester;
	private String reason;
	private int studentID;
	
	public enum ChangeType {
		TAKEOFF,
		RESUME
	}

	public ChangeRecord(int changerecordNum, Date changeDate, ChangeType changeType, int startSemester, int endSemester,
			String reason, int studentID) {
		
		this.changerecordNum = changerecordNum;
		this.changeDate = changeDate;
		this.changeType = changeType;
		this.startSemester = startSemester;
		this.endSemester = endSemester;
		this.reason = reason;
		this.studentID = studentID;
	}

	public int getChangerecordNum() {
		return changerecordNum;
	}

	public void setChangerecordNum(int changerecordNum) {
		this.changerecordNum = changerecordNum;
	}

	public Date getChangeDate() {
		return changeDate;
	}

	public void setChangeDate(Date changeDate) {
		this.changeDate = changeDate;
	}

	public ChangeType getChangeType() {
		return changeType;
	}

	public void setChangeType(ChangeType changeType) {
		this.changeType = changeType;
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

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public int getStudentID() {
		return studentID;
	}

	public void setStudentID(int studentID) {
		this.studentID = studentID;
	}
	
	
}
