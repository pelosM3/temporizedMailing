package es.horus.temporizedMailing.dao;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import es.horus.temporizedMailing.beans.Programacion;

public class ProgramacionDAO extends DAO<Programacion>{

	private static ProgramacionDAO instance;

	private ProgramacionDAO() throws ParserConfigurationException, SAXException, IOException {
		super(Programacion.class,Paths.get(System.getProperty("user.home"),"programaciones.xml"));
		this.TAG = "programacion";
		this.FIELDS = Arrays.asList("uuid","cuando");
	}
	
	public static ProgramacionDAO get() throws ParserConfigurationException, SAXException, IOException {
		if(instance == null) {
			instance = new ProgramacionDAO();
		}
		return instance;
	}
}
