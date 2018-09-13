package es.horus.temporizedMailing.dao;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import es.horus.temporizedMailing.beans.Programacion;

public class ProgramacionDAO extends DAO{

	public ProgramacionDAO() throws ParserConfigurationException, SAXException, IOException {
		super(Programacion.class,Paths.get(System.getProperty("user.home"),"programaciones.xml"));
		this.TAG = "programacion";
		this.FIELDS = Arrays.asList("uuid","cuando");
	}	
	@Override
	public Object getRow() {
		// TODO Auto-generated method stub
		return null;
	}
}
