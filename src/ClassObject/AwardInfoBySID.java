package ClassObject;

import java.time.LocalDate;

public class AwardInfoBySID {
	private String scholarshipName;
	private int awardMoney;
	private LocalDate awardDate;
	
	public AwardInfoBySID(String scholarshipName, int awardMoney, LocalDate awardDate) {
		super();
		this.scholarshipName = scholarshipName;
		this.awardMoney = awardMoney;
		this.awardDate = awardDate;
	}
	public String getScholarshipName() {
		return scholarshipName;
	}
	public void setScholarshipName(String scholarshipName) {
		this.scholarshipName = scholarshipName;
	}
	public int getAwardMoney() {
		return awardMoney;
	}
	public void setAwardMoney(int awardMoney) {
		this.awardMoney = awardMoney;
	}
	public LocalDate getAwardDate() {
		return awardDate;
	}
	public void setAwardDate(LocalDate awardDate) {
		this.awardDate = awardDate;
	}
	
	
}
