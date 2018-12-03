package ClassObject;

public class Subject {
	private int subjectCode;
	private String subjectName;
	private double score;
	
	public Subject(int subjectCode, String subjectName, double score) {
		
		this.subjectCode = subjectCode;
		this.subjectName = subjectName;
		this.score = score;
	}

	public int getSubjectCode() {
		return subjectCode;
	}

	public void setSubjectCode(int subjectCode) {
		this.subjectCode = subjectCode;
	}

	public String getSubjectName() {
		return subjectName;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}
	
	
}
