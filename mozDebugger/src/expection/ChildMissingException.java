package expection;


public class ChildMissingException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8770333969465630446L;
	String msg;
	String child;
	
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getChild() {
		return child;
	}
	public void setChild(String child) {
		this.child = child;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public ChildMissingException(String msg, String child) {
		super();
		this.msg = msg;
		this.child = child;
	}
	
}
