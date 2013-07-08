package org.contestorg.infos;

import java.io.File;
import java.util.ArrayList;

/**
 * Thème d'exportation/diffusion
 */
public class InfosTheme
{
	/** Catégorie */
	private String categorie;
	
	/** Chemin */
	private String chemin;
	
	/** Description */
	private String description;
	
	/** Paramètres */
	private ArrayList<InfosThemeParametre> parametres;
	
	/** Fichiers */
	private ArrayList<InfosThemeFichier> fichiers;
	
	// Catégories
	
	/** Catégorie des participants */
	public static final String CATEGORIE_PARTICIPANTS = "participants";
	
	/** Catégorie des phases qualificatives */
	public static final String CATEGORIE_PHASES_QUALIFICATIVES = "phasesQualificatives";
	
	/** Catégorie des phases éliminatoires */
	public static final String CATEGORIE_PHASES_ELIMINATOIRES = "phasesEliminatoires";
	
	/**
	 * Constructeur
	 * @param categorie catégorie
	 * @param chemin chemin
	 * @param description description
	 * @param parametres paramètres
	 * @param fichiers fichiers
	 */
	public InfosTheme(String categorie, String chemin, String description, ArrayList<InfosThemeParametre> parametres, ArrayList<InfosThemeFichier> fichiers) {
		this.categorie = categorie;
		this.chemin = chemin;
		this.description = description;
		this.parametres = parametres;
		this.fichiers = fichiers;
	}

	/**
	 * Récupérer le nom
	 * @return nom
	 */
	public String getNom() {
		String[] repertoires = this.chemin.split(File.separator.equals("/") ? "/" : "\\\\");
		return repertoires[repertoires.length - 1];
	}
	
	/**
	 * Récupérer la catégorie
	 * @return catégorie
	 */
	public String getCategorie() {
		return this.categorie;
	}
	
	/**
	 * Récupérer le chemin
	 * @return chemin
	 */
	public String getChemin () {
		return this.chemin;
	}
	
	/**
	 * Récupérer la description
	 * @return description
	 */
	public String getDescription () {
		return this.description;
	}
	
	/**
	 * Récupérer les paramètres
	 * @return paramètres
	 */
	public ArrayList<InfosThemeParametre> getParametres() {
		return this.parametres;
	}
	
	/**
	 * Récupérer un paramètre d'après son id
	 * @param id id
	 * @return paramètre
	 */
	public InfosThemeParametre getParametre(String id) {
		for(InfosThemeParametre parametre : this.parametres) {
			if(parametre.getId().equals(id)) {
				return parametre;
			}
		}
		return null;
	}
	
	/**
	 * Récupérer les fichiers
	 * @return fichiers
	 */
	public ArrayList<InfosThemeFichier> getFichiers() {
		return this.fichiers;
	}
	
	/**
	 * Récupérer un fichier d'après son id
	 * @param id id
	 * @return fichier
	 */
	public InfosThemeFichier getFichier(String id) {
		for(InfosThemeFichier fichier : this.fichiers) {
			if(fichier.getId().equals(id)) {
				return fichier;
			}
		}
		return null;
	}
	
}
