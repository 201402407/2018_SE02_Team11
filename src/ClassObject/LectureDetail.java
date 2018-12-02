package ClassObject;

import java.time.DayOfWeek;
import java.time.LocalTime;

public class LectureDetail {
	private int lectureCode;
	private String subjectName;
	private String profName;
	private int registerTerm;
	private int applyNum;
	private int allNum;
	private DayOfWeek dayOfWeek;
	private LocalTime startTime;
	private LocalTime endTime;
	private double score;
	
	public LectureDetail(int lectureCode, String subjectName, String profName, int registerTerm, int applyNum,
			int allNum, DayOfWeek dayOfWeek, LocalTime startTime, LocalTime endTime, double score) {
		super();
		this.lectureCode = lectureCode;
		this.subjectName = subjectName;
		this.profName = profName;
		this.registerTerm = registerTerm;
		this.applyNum = applyNum;
		this.allNum = allNum;
		this.dayOfWeek = dayOfWeek;
		this.startTime = startTime;
		this.endTime = endTime;
		this.score = score;
	}

	public int getLectureCode() {
		return lectureCode;
	}

	public void setLectureCode(int lectureCode) {
		this.lectureCode = lectureCode;
	}

	public String getSubjectName() {
		return subjectName;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}

	public String getProfName() {
		return profName;
	}

	public void setProfName(String profName) {
		this.profName = profName;
	}

	public int getRegisterTerm() {
		return registerTerm;
	}

	public void setRegisterTerm(int registerTerm) {
		this.registerTerm = registerTerm;
	}

	public int getApplyNum() {
		return applyNum;
	}

	public void setApplyNum(int applyNum) {
		this.applyNum = applyNum;
	}

	public int getAllNum() {
		return allNum;
	}

	public void setAllNum(int allNum) {
		this.allNum = allNum;
	}

	public DayOfWeek getDayOfWeek() {
		return dayOfWeek;
	}

	public void setDayOfWeek(DayOfWeek dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}

	public LocalTime getStartTime() {
		return startTime;
	}

	public void setStartTime(LocalTime startTime) {
		this.startTime = startTime;
	}

	public LocalTime getEndTime() {
		return endTime;
	}

	public void setEndTime(LocalTime endTime) {
		this.endTime = endTime;
	}

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}
	
}
