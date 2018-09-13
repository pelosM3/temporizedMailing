package es.horus.temporizedMailing.dao;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public abstract class DAO {
	protected String TAG;
	private File file;
	private Document db;
	private Class clazz;
	protected List<String> FIELDS;
	
	public DAO(Class clazz, Path path) throws ParserConfigurationException, SAXException, IOException {
		this.clazz = clazz;
		file = path.toFile();
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		db = dBuilder.parse(file);
		db.getDocumentElement().normalize();
	}
	
	public Set getRows() throws InstantiationException, IllegalAccessException {
		Set rows = new HashSet<>();
		NodeList list = db.getElementsByTagName(TAG);
		for (int row = 0; row < list.getLength(); row++) {
			Node node = list.item(row);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element element = (Element) node;
				Object o = clazz.newInstance();
				for (String s : FIELDS) {
					try {
						Field field = clazz.getDeclaredField(s);
						field.setAccessible(true);
						field.set(o, element.getElementsByTagName(field.getName()).item(0).getTextContent());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				rows.add(o);
			}
		}
		return rows;
	}
	public boolean insertRow(Object o) throws NoSuchFieldException, SecurityException {
		Node parentNode = db.createElement(TAG);
		for (String s : FIELDS) {
			Field field = clazz.getDeclaredField(s);
			Node child = db.createElement(field.getName());
			try {
				field.setAccessible(true);
				child.setTextContent(field.get(o).toString());
				parentNode.appendChild(child);
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		if(parentNode.getChildNodes().getLength() != FIELDS.size()) return false;
		db.getDocumentElement().appendChild(parentNode);
		try {
			writeToFile();
		} catch (TransformerException e) {
			System.err.printf("No se pudo guardar el contacto en el fichero : %s.\n",file.getAbsolutePath());
			return false;
		}
		return true;
	}
	private  void writeToFile() throws TransformerException {
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		DOMSource source = new DOMSource(db);
        StreamResult result = new StreamResult(file.getPath());
        transformer.transform(source, result);
	}

	public abstract Object getRow();
}
