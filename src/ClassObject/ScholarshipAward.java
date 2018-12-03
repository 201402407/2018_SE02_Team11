package ClassObject;

import java.sql.Date;

public class ScholarshipAward {
	private int awardNum;
	private int awardMoney;
	private Date awardDate;
	private int scholarshipNum;
	private int studentID;
	
	public ScholarshipAward(int awardNum, int awardMoney, Date awardDate, int scholarshipNum, int studentID) {
		
		this.awardNum = awardNum;
		this.awardMoney = awardMoney;
		this.awardDate = awardDate;
		this.scholarshipNum = scholarshipNum;
		this.studentID = studentID;
	}
	
	public int getAwardNum() {
		return awardNum;
	}
	public void setAwardNum(int awardNum) {
		this.awardNum = awardNum;
	}
	public int getAwardMoney() {
		return awardMoney;
	}
	public void setAwardMoney(int awardMoney) {
		this.awardMoney = awardMoney;
	}
	public Date getAwardDate() {
		return awardDate;
	}
	public void setAwardDate(Date awardDate) {
		this.awardDate = awardDate;
	}
	public int getScholarshipNum() {
		return scholarshipNum;
	}
	public void setScholarshipNum(int scholarshipNum) {
		this.scholarshipNum = scholarshipNum;
	}
	public int getStudentID() {
		return studentID;
	}
	public void setStudentID(int studentID) {
		this.studentID = studentID;
	}
	
	
}
