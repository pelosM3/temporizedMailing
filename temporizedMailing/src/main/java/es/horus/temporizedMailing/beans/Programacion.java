package es.horus.temporizedMailing.beans;

import java.util.Timer;
import java.util.UUID;

public class Programacion {
	public enum CUANDO {
		UNA_VEZ ("Una vez"),
		SEMANALMENTE ("Semanalmente"),
		MENSUALMENTE ("Mensualmente"),
		ANUALMENTE ("Anualmente"),
		PERSONALIZADO ("Personalizado");
		
		private String title;
		CUANDO (String title) {
			this.title = title;
		}
		
		@Override
		public String toString() {
			return title;
		}
		
	}

	private Timer timer;
	private UUID uuid;
	private CUANDO cuando;
	
	public Programacion(Timer timer, UUID uuid, CUANDO cuando) {
		super();
		this.timer = timer;
		this.uuid = uuid;
		this.cuando = cuando;
	}
	public Timer getTimer() {
		return timer;
	}
	public void setTimer(Timer timer) {
		this.timer = timer;
	}
	public UUID getUuid() {
		return uuid;
	}
	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}
	public CUANDO getCuando() {
		return cuando;
	}
	public void setCuando(CUANDO cuando) {
		this.cuando = cuando;
	}
}
