package es.horus.temporizedMailing.dao;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

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

public abstract class DAO<T> {
	protected String TAG;
	protected List<String> FIELDS;
	private Class<? extends T> clazz;
	
	private File file;
	private Document db;
	
	private Set<T> rows;
	
	public DAO(Class<? extends T> clazz, Path path) throws ParserConfigurationException, SAXException, IOException {
		this.clazz = clazz;
		file = path.toFile();
		file.createNewFile();
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		try {
			db = dBuilder.parse(file);
		} catch (Exception e) {
			// El fichero no tiene un formato XML valido: lo ignoramos y creamos uno nuevo
		}
		if (db == null) {
			db = dBuilder.newDocument();
			db.appendChild(db.createElement("datos"));
			
		}
		db.getDocumentElement().normalize();
	}
	
	public Set<T> getRows() throws InstantiationException, IllegalAccessException {
		if (rows == null) {
			rows = new HashSet<>();
			NodeList list = db.getElementsByTagName(TAG);
			for (int row = 0; row < list.getLength(); row++) {
				Node node = list.item(row);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					Element element = (Element) node;
					T o = clazz.newInstance();
					for (String s : FIELDS) {
						try {
							Field field = clazz.getDeclaredField(s);
							field.setAccessible(true);
							String value = element.getElementsByTagName(field.getName()).item(0).getTextContent();
							if(field.getType().isAssignableFrom(UUID.class)) {
								field.set(o, UUID.fromString(value));
							} else {
								field.set(o, value);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					rows.add(o);
				}
			} 
		}
		return rows;
	}
	public boolean insertRow(T o) throws NoSuchFieldException, FileAlreadyExistsException, InstantiationException, IllegalAccessException {
		if(rows.contains(o)) {
			throw new FileAlreadyExistsException(null);
		}
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
		if(parentNode.getChildNodes().getLength() != FIELDS.size()) {
			return false;
		}
		db.getDocumentElement().appendChild(parentNode);
		try {
			writeToFile();
		} catch (TransformerException e) {
			e.printStackTrace();
			return false;
		}
		rows.add(o);
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
}
