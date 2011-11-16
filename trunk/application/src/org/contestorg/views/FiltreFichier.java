package org.contestorg.views;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class FiltreFichier extends FileFilter
{

	// Attributs
	private String extension;
	private String description;

	// Extension et descriptions predefinies
	public static String ext_co = "co";
	public static String des_co = "Fichier ContestOrg";
	public static String ext_xml = "xml";
	public static String des_xml = "Fichier XML";
	public static String ext_pdf = "pdf";
	public static String des_pdf = "Fichier PDF";

	// Constructeurs
	public FiltreFichier(String extension, String description) {
		this.extension = extension;
		this.description = description;
	}

	// Méthodes d'un FileFilter
	@Override
	public boolean accept (File f) {
		if (f.isDirectory() || f.getName().toLowerCase().endsWith("." + this.extension)) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public String getDescription () {
		return this.description;
	}
}
