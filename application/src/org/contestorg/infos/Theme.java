package org.contestorg.infos;

import java.io.File;
import java.util.ArrayList;

/**
 * Thème d'exportation/diffusion
 */
public class Theme
{
	/** Catégorie */
	private String categorie;
	
	/** Chemin */
	private String chemin;
	
	/** Description */
	private String description;
	
	/** Paramètres */
	private ArrayList<Parametre> parametres;
	
	/** Fichiers */
	private ArrayList<Fichier> fichiers;
	
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
	public Theme(String categorie, String chemin, String description, ArrayList<Parametre> parametres, ArrayList<Fichier> fichiers) {
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
	public ArrayList<Parametre> getParametres() {
		return this.parametres;
	}
	
	/**
	 * Récupérer un paramètre d'après son id
	 * @param id id
	 * @return paramètre
	 */
	public Parametre getParametre(String id) {
		for(Parametre parametre : this.parametres) {
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
	public ArrayList<Fichier> getFichiers() {
		return this.fichiers;
	}
	
	/**
	 * Récupérer un fichier d'après son id
	 * @param id id
	 * @return fichier
	 */
	public Fichier getFichier(String id) {
		for(Fichier fichier : this.fichiers) {
			if(fichier.getId().equals(id)) {
				return fichier;
			}
		}
		return null;
	}
	
}
