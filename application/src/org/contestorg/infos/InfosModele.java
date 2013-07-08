package org.contestorg.infos;

import java.io.File;
import java.util.ArrayList;

/**
 * Modèle de concours
 */
public class InfosModele
{
	/** Catégorie */
	private String categorie;
	
	/** Chemin du modèle */
	private String chemin;
	
	/** Chemin de l'image */
	private String image;
	
	/** Description */
	private String description;
	
	/** Variantes */
	private ArrayList<InfosModeleVariante> variantes;
	
	// Catégories
	
	/** Catégorie des sports */
	public static final String CATEGORIE_SPORTS = "sports";
	
	/** Catégorie des jeux de société */
	public static final String CATEGORIE_JEUX_DE_SOCIETE = "jeuxDeSociete";
	
	/** Catégorie des jeux vidéo */
	public static final String CATEGORIE_JEUX_VIDEO = "jeuxVideo";
	
	/** Catégorie divers */
	public static final String CATEGORIE_DIVERS = "divers";

	/**
	 * Constructeur
	 * @param categorie catégorie
	 * @param chemin chemin du modèle
	 * @param image chemin de l'image
	 * @param description description
	 * @param variantes variantes
	 */
	public InfosModele(String categorie, String chemin, String image, String description, ArrayList<InfosModeleVariante> variantes) {
		this.categorie = categorie;
		this.chemin = chemin;
		this.image = image;
		this.description = description;
		this.variantes = variantes;
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
	public String getCategorie () {
		return this.categorie;
	}
	
	/**
	 * Récupérer le chemin du modèle
	 * @return chemin du modèle
	 */
	public String getChemin () {
		return this.chemin;
	}

	/**
	 * Récupérer le chemin de l'image
	 * @return chemin de l'image
	 */
	public String getImage () {
		return this.image;
	}

	/**
	 * Récupérer la description
	 * @return description
	 */
	public String getDescription () {
		return this.description;
	}

	/**
	 * Récupérer les variantes
	 * @return variantes
	 */
	public ArrayList<InfosModeleVariante> getVariantes () {
		return this.variantes;
	}
	
	
}
