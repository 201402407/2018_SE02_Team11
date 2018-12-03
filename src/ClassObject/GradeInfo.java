package ClassObject;

public class GradeInfo {
	private double grade;
	private int attendanceNum;
	
	public GradeInfo(double grade, int attendanceNum) {
		super();
		this.grade = grade;
		this.attendanceNum = attendanceNum;
	}
	
	public double getGrade() {
		return grade;
	}
	public void setGrade(double grade) {
		this.grade = grade;
	}
	public int getAttendanceNum() {
		return attendanceNum;
	}
	public void setAttendanceNum(int attendanceNum) {
		this.attendanceNum = attendanceNum;
	}
	
}
