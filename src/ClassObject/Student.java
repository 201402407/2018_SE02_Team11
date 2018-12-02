package ClassObject;

public class Student {
	private int studentID;
	private String name;
	private int year;
	private int semester;
	private boolean isTimeOff;
	private boolean isGraduate;
	private int accountID;
	private int bankAccountNum;
	private int departmentCode;
	
	public int getStudentID() {
		return studentID;
	}
	public void setStudentID(int studentID) {
		this.studentID = studentID;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	public int getAccountID() {
		return accountID;
	}
	public void setAccountID(int accountID) {
		this.accountID = accountID;
	}
	public int getBankAccountNum() {
		return bankAccountNum;
	}
	public void setBankAccountNum(int bankAccountNum) {
		this.bankAccountNum = bankAccountNum;
	}
	public int getDepartmentCode() {
		return departmentCode;
	}
	public void setDepartmentCode(int departmentCode) {
		this.departmentCode = departmentCode;
	}
}
