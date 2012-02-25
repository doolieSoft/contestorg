package org.contestorg.views;

import java.io.File;

import javax.swing.filechooser.FileFilter;

/**
 * Filtre de fichiers pour l'explorateur de fichiers
 */
public class FiltreFichier extends FileFilter
{

	/** Extension autorisée */
	private String extension;
	
	/** Description de l'extension autorisée */
	private String description;

	// Extension et descriptions prédéfinies
	
	/** Extension CO */
	public static String ext_co = "co";
	/**  Description de l'extension CO */
	public static String des_co = "Fichier ContestOrg";
	
	/** Extension XML */
	public static String ext_xml = "xml";
	/** Description de l'extension XML */
	public static String des_xml = "Fichier XML";
	
	/** Extension PDF */
	public static String ext_pdf = "pdf";
	/** Description de l'extension PDF */
	public static String des_pdf = "Fichier PDF";

	/**
	 * Constructeur
	 * @param extension extension autorisée
	 * @param description description de l'extension autorisée
	 */
	public FiltreFichier(String extension, String description) {
		this.extension = extension;
		this.description = description;
	}

	/**
	 * @see FileFilter#accept(File)
	 */
	@Override
	public boolean accept (File f) {
		if (f.isDirectory() || f.getName().toLowerCase().endsWith("." + this.extension)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * @see FileFilter#getDescription()
	 */
	@Override
	public String getDescription () {
		return this.description;
	}
}
