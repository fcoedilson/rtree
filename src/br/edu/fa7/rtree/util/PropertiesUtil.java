package br.edu.fa7.rtree.util;

import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.Hashtable;
import java.util.Properties;

public final class PropertiesUtil {	

	private Hashtable<String, Properties> propertyFiles = null;

	private static PropertiesUtil instance = null;

	private PropertiesUtil() {
	}

	public static synchronized PropertiesUtil getInstance() {
		if (instance == null) {
			instance = new PropertiesUtil();
		}
		return instance;
	}

	private void loadPropertiesFile(final String fileName) {

		Properties prop;

		// tests if the properties is null.
		if (propertyFiles == null) {
			propertyFiles = new Hashtable<String, Properties>();
		}

		try {
			// Gets the classloader for the container
			ClassLoader classLoader = Thread.currentThread()
			.getContextClassLoader();

			if (classLoader == null) {
				classLoader = getClass().getClassLoader();
			}
			InputStream input = classLoader.getResourceAsStream(fileName);
			if (input != null) {
				prop = new Properties();

				try {
					prop.load(input);
					propertyFiles.put(fileName, prop);

				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Returns the property name the propery name.
	 * @param rscBdlFileName - The property file name.
	 * @param propName - The property name.
	 * @return - The property value.
	 */
	public String getProperty(final String rscBdlFileName
			, final String propName) {

		Properties prop = null;
		String propValue = null;

		// Verify if the propety file was loaded
		if (propertyFiles == null
				|| propertyFiles.get(rscBdlFileName) == null) {
			loadPropertiesFile(rscBdlFileName);
		}

		if (propertyFiles != null
				&& propertyFiles.get(rscBdlFileName) != null) {
			prop = (Properties) propertyFiles.get(rscBdlFileName);
			propValue = prop.getProperty(propName);

			if (propValue == null) {
				return "";
			}
		}

		return propValue;
	}

	/**
	 * Returns the property name the propery name.
	 * @param rscBdlFileName - The property file name.
	 * @param propName - The property name.
	 * @return - The property value.
	 */
	public String getProperty(final String rscBdlFileName
			, final String propName, Object[] parametros) {

		Properties prop = null;
		String propValue = null;

		// Verify if the propety file was loaded
		if (propertyFiles == null
				|| propertyFiles.get(rscBdlFileName) == null) {
			loadPropertiesFile(rscBdlFileName);
		}

		if (propertyFiles != null
				&& propertyFiles.get(rscBdlFileName) != null) {
			prop = (Properties) propertyFiles.get(rscBdlFileName);
			propValue = prop.getProperty(propName);

			if (propValue == null) {
				return "";
			}
		}
		propValue = MessageFormat.format (propValue, parametros);        	
		return propValue;
	}    


	/**
	 * @param rscBdlFileName
	 * @param key
	 * @return The key's description
	 */
	public boolean containsKey(final String rscBdlFileName, String key){
		Properties prop = null;

		// Verify if the propety file was loaded
		if (propertyFiles == null
				|| propertyFiles.get(rscBdlFileName) == null) {
			loadPropertiesFile(rscBdlFileName);
		}

		if (propertyFiles != null
				&& propertyFiles.get(rscBdlFileName) != null) {
			prop = (Properties) propertyFiles.get(rscBdlFileName);
			return prop.containsKey(key);
		} 
		return false;
	}    

}
