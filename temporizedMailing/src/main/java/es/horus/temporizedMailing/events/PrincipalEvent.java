package es.horus.temporizedMailing.events;

public class PrincipalEvent {
	private Integer id;
	private Object[] params;
	
	public PrincipalEvent(Integer id) {
		super();
		this.id = id;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Object[] getParams() {
		return params;
	}
	public void setParams(Object[] params) {
		this.params = params;
	}
}
