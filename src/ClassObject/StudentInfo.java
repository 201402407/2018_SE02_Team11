package ClassObject;

public class StudentInfo {
	private String studentName;
	private String departmentName;
	private int year;
	private int semester;
	private boolean isTimeOff;
	private boolean isGraduate;
	
	public StudentInfo(String studentName, String departmentName, int year, int semester, boolean isTimeOff,
			boolean isGraduate) {
		this.studentName = studentName;
		this.departmentName = departmentName;
		this.year = year;
		this.semester = semester;
		this.isTimeOff = isTimeOff;
		this.isGraduate = isGraduate;
	}
	
	public String getStudentName() {
		return studentName;
	}
	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}
	public String getDepartmentName() {
		return departmentName;
	}
	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public int getSemester() {
		return semester;
	}
	public void setSemester(int semester) {
		this.semester = semester;
	}
	public boolean isTimeOff() {
		return isTimeOff;
	}
	public void setTimeOff(boolean isTimeOff) {
		this.isTimeOff = isTimeOff;
	}
	public boolean isGraduate() {
		return isGraduate;
	}
	public void setGraduate(boolean isGraduate) {
		this.isGraduate = isGraduate;
	}
}
