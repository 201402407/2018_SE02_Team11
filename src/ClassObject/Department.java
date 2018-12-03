package ClassObject;

public class Department {
	private int departmentCode;
	private String departmentName;
	private int tuition;
	
	public Department(int departmentCode, String departmentName, int tuition) {
		super();
		this.departmentCode = departmentCode;
		this.departmentName = departmentName;
		this.tuition = tuition;
	}
	
	public int getDepartmentCode() {
		return departmentCode;
	}
	public void setDepartmentCode(int departmentCode) {
		this.departmentCode = departmentCode;
	}
	public String getDepartmentName() {
		return departmentName;
	}
	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}
	public int getTuition() {
		return tuition;
	}
	public void setTuition(int tuition) {
		this.tuition = tuition;
	}
	
	
}
