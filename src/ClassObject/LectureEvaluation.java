package ClassObject;

public class LectureEvaluation {

	private String text;
	private int attendanceNum;
	
	public LectureEvaluation(String text, int attendanceNum) {
		super();
		this.text = text;
		this.attendanceNum = attendanceNum;
	}
	
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public int getAttendanceNum() {
		return attendanceNum;
	}
	public void setAttendanceNum(int attendanceNum) {
		this.attendanceNum = attendanceNum;
	}
	
}
