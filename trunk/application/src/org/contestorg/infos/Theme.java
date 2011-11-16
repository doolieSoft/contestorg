package infos;

import java.io.File;
import java.util.ArrayList;

public class Theme
{	
	// Catégories
	public static final String CATEGORIE_EQUIPES = "equipes";
	public static final String CATEGORIE_PHASES_QUALIFICATIVES = "phasesQualificatives";
	public static final String CATEGORIE_PHASES_ELIMINATOIRES = "phasesEliminatoires";
	
	// Attributs
	private String categorie;
	private String chemin;
	private String description;
	private ArrayList<Parametre> parametres;
	private ArrayList<Fichier> fichiers;
	
	// Constructeur
	public Theme(String categorie, String chemin, String description, ArrayList<Parametre> parametres, ArrayList<Fichier> fichiers) {
		this.categorie = categorie;
		this.chemin = chemin;
		this.description = description;
		this.parametres = parametres;
		this.fichiers = fichiers;
	}

	// Getters
	public String getNom() {
		String[] repertoires = this.chemin.split(File.separator.equals("/") ? "/" : "\\\\");
		return repertoires[repertoires.length - 1];
	}
	public String getCategorie() {
		return this.categorie;
	}
	public String getChemin () {
		return this.chemin;
	}
	public String getDescription () {
		return this.description;
	}
	public ArrayList<Parametre> getParametres() {
		return this.parametres;
	}
	public Parametre getParametre(String id) {
		for(Parametre parametre : this.parametres) {
			if(parametre.getId().equals(id)) {
				return parametre;
			}
		}
		return null;
	}
	public ArrayList<Fichier> getFichiers() {
		return this.fichiers;
	}
	public Fichier getFichier(String id) {
		for(Fichier fichier : this.fichiers) {
			if(fichier.getId().equals(id)) {
				return fichier;
			}
		}
		return null;
	}
	
}
