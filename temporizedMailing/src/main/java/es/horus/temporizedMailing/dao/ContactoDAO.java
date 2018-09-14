package es.horus.temporizedMailing.dao;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import es.horus.temporizedMailing.beans.Contacto;

public class ContactoDAO extends DAO<Contacto> {
	
	private static ContactoDAO instance;
	
	private ContactoDAO() throws ParserConfigurationException, SAXException, IOException {
		super(Contacto.class,Paths.get(System.getProperty("user.home"),"contactos.xml"));
		this.TAG = "contacto";
		this.FIELDS = Arrays.asList("nombre","ape1","ape2","email");
	}
	
	public static ContactoDAO get() throws ParserConfigurationException, SAXException, IOException {
		if(instance == null) {
			instance = new ContactoDAO();
		}
		return instance;
	}
}
