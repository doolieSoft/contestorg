package org.contestorg.infos;

/**
 * Variante d'un modèle de concours
 */
public class InfosModeleVariante
{
	/** Nom */
	private String nom;
	
	/** Chemin du fichier */
	private String fichier;

	/**
	 * Constructeur
	 * @param nom nom
	 * @param fichier chemin du fichier
	 */
	public InfosModeleVariante(String nom, String fichier) {
		this.nom = nom;
		this.fichier = fichier;
	}

	/**
	 * Récupérer le nom
	 * @return nom
	 */
	public String getNom () {
		return this.nom;
	}

	/*
	 * Récupérer le chemin du fichier
	 * @return chemin du fichier
	 */
	public String getFichier () {
		return this.fichier;
	}
	
	
}
