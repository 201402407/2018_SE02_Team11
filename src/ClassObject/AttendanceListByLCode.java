package ClassObject;

public class AttendanceListByLCode {
	private String subjectName;
	private int attendanceNum;
	private boolean isRetake;
	private int studentID;
	private String studentName;
	
	public AttendanceListByLCode(String subjectName, int attendanceNum, boolean isRetake, int studentID,
			String studentName) {
		super();
		this.subjectName = subjectName;
		this.attendanceNum = attendanceNum;
		this.isRetake = isRetake;
		this.studentID = studentID;
		this.studentName = studentName;
	}

	public String getSubjectName() {
		return subjectName;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}

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

	public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

}
