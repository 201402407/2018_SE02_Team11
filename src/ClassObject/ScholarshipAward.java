package ClassObject;

import java.sql.Date;
import java.time.LocalDate;

public class ScholarshipAward {
	private int awardNum;
	private int awardMoney;
	private LocalDate awardDate;
	private int scholarshipNum;
	private int studentID;
	
	public ScholarshipAward() {
		
	}
	public ScholarshipAward(int awardNum, int awardMoney, LocalDate awardDate, int scholarshipNum, int studentID) {
		
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
	public LocalDate getAwardDate() {
		return awardDate;
	}
	public void setAwardDate(LocalDate awardDate) {
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
