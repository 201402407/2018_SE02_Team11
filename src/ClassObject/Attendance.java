package ClassObject;

public class Attendance {
	
	private int attendanceNum;
	private boolean isRetake;
	private int studentID;
	private int lectureCode;
	
	public int getAttendanceNum() {
		return attendanceNum;
	}
	public void setAttendanceNum(int attendanceNum) {
		this.attendanceNum = attendanceNum;
	}
	public boolean isRetake() {
		return isRetake;
	}
	public void setRetake(boolean isRetake) {
		this.isRetake = isRetake;
	}
	public int getStudentID() {
		return studentID;
	}
	public void setStudentID(int studentID) {
		this.studentID = studentID;
	}
	public int getLectureCode() {
		return lectureCode;
	}
	public void setLectureCode(int lectureCode) {
		this.lectureCode = lectureCode;
	}
	
}
