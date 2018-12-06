package ClassObject;

public class GradeInfoOfTerm {
	
	private boolean visibleGrade; //평점보여짐여부
	private double grade; //평점
	private boolean isRetake; //재수강여부
	private double gradeBefore; //이전수강당시평점. (재수강하지 않았으면 0.0)
	private String subjectName; //과목명
	
	public GradeInfoOfTerm(boolean visibleGrade, double grade, boolean isRetake, double gradeBefore,
			String subjectName) {
		super();
		this.visibleGrade = visibleGrade;
		this.grade = grade;
		this.isRetake = isRetake;
		this.gradeBefore = gradeBefore;
		this.subjectName = subjectName;
	}
	
	public String getSubjectName() {
		return subjectName;
	}
	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}
	public boolean isVisibleGrade() {
		return visibleGrade;
	}
	public void setVisibleGrade(boolean visibleGrade) {
		this.visibleGrade = visibleGrade;
	}
	public double getGrade() {
		return grade;
	}
	public void setGrade(double grade) {
		this.grade = grade;
	}
	public boolean isRetake() {
		return isRetake;
	}
	public void setRetake(boolean isRetake) {
		this.isRetake = isRetake;
	}
	public double getGradeBefore() {
		return gradeBefore;
	}
	public void setGradeBefore(double gradeBefore) {
		this.gradeBefore = gradeBefore;
	}
}
