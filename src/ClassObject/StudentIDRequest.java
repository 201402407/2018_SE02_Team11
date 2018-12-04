package ClassObject;

import java.sql.Date;
import java.time.LocalDate;

public class StudentIDRequest {
	private int reqSIDnum;
	private LocalDate reqSIDdate;
	private String accountID;
	
	public int getReqSIDnum() {
		return reqSIDnum;
	}
	public void setReqSIDnum(int reqSIDnum) {
		this.reqSIDnum = reqSIDnum;
	}
	public LocalDate getReqSIDdate() {
		return reqSIDdate;
	}
	public void setReqSIDdate(LocalDate reqSIDdate) {
		this.reqSIDdate = reqSIDdate;
	}
	public String getAccountID() {
		return accountID;
	}
	public void setAccountID(String accountID) {
		this.accountID = accountID;
	}
}
