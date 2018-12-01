package ClassObject;

import java.sql.Date;

public class StudentIDRequest {
	private int reqSIDnum;
	private Date reqSIDdate;
	private String accountID;
	
	public int getReqSIDnum() {
		return reqSIDnum;
	}
	public void setReqSIDnum(int reqSIDnum) {
		this.reqSIDnum = reqSIDnum;
	}
	public Date getReqSIDdate() {
		return reqSIDdate;
	}
	public void setReqSIDdate(Date reqSIDdate) {
		this.reqSIDdate = reqSIDdate;
	}
	public String getAccountID() {
		return accountID;
	}
	public void setAccountID(String accountID) {
		this.accountID = accountID;
	}
}
