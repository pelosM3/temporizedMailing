package es.horus.temporizedMailing.utilities;

import java.util.List;
import java.util.TimerTask;

import es.horus.temporizedMailing.beans.AvisoBE;
import es.horus.temporizedMailing.beans.ProgramacionBE;
import es.horus.temporizedMailing.dao.AvisosDAO;

public class AvisosManager extends TimerTask {



	@Override
	public void run() {
		AvisosDAO aviDao=new AvisosDAO();
		List<AvisoBE> listaAvisos=aviDao.cargaBBDD();

		System.out.println("------------- TIMER ENVIAR TODO "+Utiles.getFechaHoy()+"---------------------");
		for(AvisoBE aviso:listaAvisos) {
			System.out.println("Aver si tenemos que enviar: "+aviso);
			for(ProgramacionBE p:aviso.getProgramaciones()) {
				if(p.getEnviado()==ProgramacionBE.ESTADO_PENDIENTE && p.getFecha()<Utiles.getFechaHoy()) {
					if(enviarAviso(aviso)) {
						p.setEnviado(ProgramacionBE.ESTADO_ENVIADO);
						System.out.println("Enviado OK (marco como enviado): "+aviso);
						aviDao.guarda(aviso);
						break;//me salgo del primer bucle, este aviso queda hecho
					}else {
						System.out.println("Error, no se envia el email: "+aviso);
					}
					
				}else {
					System.out.println("No hay que enviar.");
				}
			}
		}

		System.out.println("------------- TIMER ENVIAR TODO "+Utiles.getFechaHoy()+" fin -----------------");
	}
	
	private boolean enviarAviso(AvisoBE a) {
		SendEmailUtility seu=new SendEmailUtility(a.getDestinatario(), a.getAsunto(), a.getMensaje());
		
		return seu.sendEmail();
	}
}
