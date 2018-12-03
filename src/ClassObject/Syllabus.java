package ClassObject;

public class Syllabus {
	private int syllabusCode;
	private String text;
	public Syllabus(int syllabusCode, String text) {
		super();
		this.syllabusCode = syllabusCode;
		this.text = text;
	}
	
	public int getSyllabusCode() {
		return syllabusCode;
	}
	public void setSyllabusCode(int syllabusCode) {
		this.syllabusCode = syllabusCode;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	
	
	
}
