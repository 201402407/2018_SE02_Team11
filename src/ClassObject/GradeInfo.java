package ClassObject;

public class GradeInfo {
	private double grade;
	private boolean visibleGrade;
	
	public GradeInfo(double grade, boolean visibleGrade) {
		super();
		this.grade = grade;
		this.visibleGrade = visibleGrade;
	}
	public double getGrade() {
		return grade;
	}
	public void setGrade(double grade) {
		this.grade = grade;
	}
	public boolean isVisibleGrade() {
		return visibleGrade;
	}
	public void setVisibleGrade(boolean visibleGrade) {
		this.visibleGrade = visibleGrade;
	}
}
