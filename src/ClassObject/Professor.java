package ClassObject;

public class Professor {
	private int professorCode;
	private String professorName;
	
	public Professor(int professorCode, String professorName) {
		super();
		this.professorCode = professorCode;
		this.professorName = professorName;
	}

	public int getProfessorCode() {
		return professorCode;
	}

	public void setProfessorCode(int professorCode) {
		this.professorCode = professorCode;
	}

	public String getProfessorName() {
		return professorName;
	}

	public void setProfessorName(String professorName) {
		this.professorName = professorName;
	}
	
}
