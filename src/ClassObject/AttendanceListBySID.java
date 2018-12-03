package ClassObject;

public class AttendanceListBySID {
	private int reqSIDnum;
	private int lcode;
	private String subjectName;
	private boolean isRetake;
	private int registerTerm;
	private String dayOfWeek;
	private String startTime;
	private String endTime;
	private double score;
	
	public AttendanceListBySID(int reqSIDnum, int lcode, String subjectName, boolean isRetake, int registerTerm,
	String dayOfWeek, String startTime, String endTime, double score) {
		setReqSIDnum(reqSIDnum);
		setLcode(lcode);
		setSubjectName(subjectName);
		setRetake(isRetake);
		setRegisterTerm(registerTerm);
		setDayOfWeek(dayOfWeek);
		setStartTime(startTime);
		setEndTime(endTime);
		setScore(score);
	}
	
	public int getReqSIDnum() {
		return reqSIDnum;
	}
	public void setReqSIDnum(int reqSIDnum) {
		this.reqSIDnum = reqSIDnum;
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
	public String getDayOfWeek() {
		return dayOfWeek;
	}
	public void setDayOfWeek(String dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public double getScore() {
		return score;
	}
	public void setScore(double score) {
		this.score = score;
	}
	

}
