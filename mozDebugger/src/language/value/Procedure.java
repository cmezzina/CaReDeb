package language.value;

import java.util.ArrayList;
import language.statement.IStatement;

public class Procedure implements IValue {

	ArrayList<String> params;
	IStatement body;
	public ArrayList<String> getParams() {
		return params;
	}
	public void setParams(ArrayList<String> params) {
		this.params = params;
	}
	public IStatement getBody() {
		return body;
	}
	public void setBody(IStatement body) {
		this.body = body;
	}
	public Procedure(ArrayList<String> params, IStatement body) {
		super();
		this.params = params;
		this.body = body;
	}
	@Override
	public ValueType getType() {
		return ValueType.PROCEDURE;
	}
	
	public String toString()
	{
		String ret= " proc {";
		for (String param : params) {
			ret +=" "+param; 
		}
		return ret+" } " + body.toString() + " end";
	}
	@Override
	public void rename(String old_id, String new_id) {
		if(params.contains(old_id))
			return;
		body.rename(old_id, new_id);
	}
	
	@SuppressWarnings("unchecked")
	public Procedure clone()
	{
		return new Procedure((ArrayList<String>) params.clone(), body.clone());
	}
}
