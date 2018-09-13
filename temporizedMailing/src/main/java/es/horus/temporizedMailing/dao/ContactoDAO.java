package es.horus.temporizedMailing.dao;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import es.horus.temporizedMailing.beans.Contacto;

public class ContactoDAO extends DAO{
	
	public ContactoDAO() throws ParserConfigurationException, SAXException, IOException {
		super(Contacto.class,Paths.get(System.getProperty("user.home"),"contactos.xml"));
		this.TAG = "contacto";
		this.FIELDS = Arrays.asList("nombre","ape1","ape2","email");
	}

	@Override
	public Object getRow() {
		// TODO Auto-generated method stub
		return null;
	}
}
