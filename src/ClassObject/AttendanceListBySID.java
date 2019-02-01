package ClassObject;

import java.time.DayOfWeek;
import java.time.LocalTime;

public class AttendanceListBySID {
	private int attNum;
	private int lcode;
	private String subjectName;
	private boolean isRetake;
	private int registerTerm;
	private DayOfWeek dayOfWeek;
	private LocalTime startTime;
	private LocalTime endTime;
	private double score;
	
	public AttendanceListBySID(int attNum, int lcode, String subjectName, boolean isRetake, int registerTerm,
	DayOfWeek dayOfWeek, LocalTime startTime, LocalTime endTime, double score) {
		setAttNum(attNum);
		setLcode(lcode);
		setSubjectName(subjectName);
		setRetake(isRetake);
		setRegisterTerm(registerTerm);
		setDayOfWeek(dayOfWeek);
		setStartTime(startTime);
		setEndTime(endTime);
		setScore(score);
	}
	
	public int getAttNum() {
		return attNum;
	}
	public void setAttNum(int attNum) {
		this.attNum = attNum;
	}
	public int getLcode() {
		return lcode;
	}
	public void setLcode(int lcode) {
		this.lcode = lcode;
	}
	public String getSubjectName() {
		return subjectName;
	}
	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}
	public boolean isRetake() {
		return isRetake;
	}
	public void setRetake(boolean isRetake) {
		this.isRetake = isRetake;
	}
	public int getRegisterTerm() {
		return registerTerm;
	}
	public void setRegisterTerm(int registerTerm) {
		this.registerTerm = registerTerm;
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
