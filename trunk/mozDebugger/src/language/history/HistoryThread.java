package language.history;

public class HistoryThread implements IHistory {

	private String thread_id;
	public HistoryThread(String thread_id) {
		super();
		this.thread_id = thread_id;
	}
	public String getThread_id() {
		return thread_id;
	}
	public void setThread_id(String thread_id) {
		this.thread_id = thread_id;
	}
	@Override
	public HistoryType getType() {
		return HistoryType.THREAD;
	}
	public String toString()
	{
		return "new thread "+thread_id;
	}
}
