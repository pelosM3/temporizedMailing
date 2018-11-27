package es.horus.temporizedMailing.utilities;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Utiles {


	public static Date getFechaActualMasDias(int dias) {
		Date actual = new Date();
		for (int iCnt = 0; iCnt < dias; iCnt++) {
			actual.setTime(actual.getTime() + (1000 * 60 * 60 * 24));
		}
		return actual;
	}
	
	public static long getFechaAsLong(Date dia) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		return Long.parseLong(sdf.format(dia));
	}
}
