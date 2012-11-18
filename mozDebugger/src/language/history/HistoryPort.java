package language.history;

public class HistoryPort implements IHistory {

	private String port_name;
	
	public HistoryPort(String port_name) {
		super();
		this.port_name = port_name;
	}

	public String getPort_name() {
		return port_name;
	}

	public void setPort_name(String port_name) {
		this.port_name = port_name;
	}

	@Override
	public HistoryType getType() {
		return HistoryType.PORT;
	}

	public String toString ()
	{
		return "new port "+port_name;
	}
}
