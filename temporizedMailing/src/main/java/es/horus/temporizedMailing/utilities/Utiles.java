package es.horus.temporizedMailing.utilities;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Utiles {

	public static Date getFechaActualMasHoras(int horas) {
		Date actual = new Date();
		for (int iCnt = 0; iCnt < horas; iCnt++) {
			actual.setTime(actual.getTime() + (1000 * 60 * 60));
		}
		return actual;
	}
	
	public static Date getFechaActualMasDias(int dias) {
		Date actual = new Date();
		for (int iCnt = 0; iCnt < dias; iCnt++) {
			actual.setTime(actual.getTime() + (1000 * 60 * 60 * 24));
		}
		return actual;
	}

	public static long getFechaHoy() {
		return getFechaAsLong(new Date());
	}
	
	public static long getFechaAsLong(Date dia) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		return Long.parseLong(sdf.format(dia));
	}
	
	public static Date clearDateBelowDay(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(date.getTime());
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}
}
