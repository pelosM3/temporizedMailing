package es.horus.temporizedMailing.beans;

public class Contacto {
	private String nombre, ape1, ape2, email;
	
	public Contacto(String nombre, String ape1, String ape2, String email) {
		super();
		this.nombre = nombre;
		this.ape1 = ape1;
		this.ape2 = ape2;
		this.email = email;
	}

	public Contacto() {
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApe1() {
		return ape1;
	}

	public void setApe1(String ape1) {
		this.ape1 = ape1;
	}

	public String getApe2() {
		return ape2;
	}

	public void setApe2(String ape2) {
		this.ape2 = ape2;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(ape1+ " ");
		sb.append((ape2 != null ? ape2 : "") + ", ");
		sb.append(nombre);
		sb.append(" ("+email+")");
		
		return sb.toString();
	}

	
	@Override
	public boolean equals(Object obj) {
		if(obj == null) return false;
		if(obj instanceof Contacto) {
			return email.equals(((Contacto)obj).email);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return email.hashCode();
	}
	
}
