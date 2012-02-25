package org.contestorg.infos;

import java.io.File;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Conteneur d'informations pour la création ou la modification d'un thème d'exportation/diffusion
 */
public class InfosModelTheme extends InfosModelAbstract
{
	/** Chemin */
	private String chemin;
	
	/** Paramètres */
	private HashMap<String,String> parametres;
	
	/** Fichiers */
	private HashMap<String,String> fichiers;
	
	/**
	 * Constructeur
	 * @param chemin chemin
	 * @param parametres paramètres
	 * @param fichiers fichiers
	 */
	public InfosModelTheme(String chemin, HashMap<String,String> parametres, HashMap<String,String> fichiers) {
		this.chemin = chemin;
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
	 * Récupérer le chemin
	 * @return chemin
	 */
	public String getChemin () {
		return this.chemin;
	}
	
	/**
	 * Récupérer les paramètres
	 * @return paramètres
	 */
	public HashMap<String,String> getParametres () {
		return this.parametres;
	}
	
	/**
	 * Récupérer les fichiers
	 * @return fichiers
	 */
	public HashMap<String, String> getFichiers () {
		return this.fichiers;
	}

	/**
	 * Récupérer les données par défaut
	 * @return données par défaut
	 */
	public static InfosModelTheme defaut () {
		return new InfosModelTheme("", null, null);
	}
	
	/**
	 * Vérifier si la valeur peut etre assimilée à un entier
	 * @param valeur valeur
	 * @return valeur assimilable à un entier ?
	 */
	public static boolean isInteger(String valeur) {
		// Vérifier s'il s'agit d'un simple entier
		try {
			// Parser la valeur
			Integer.parseInt(valeur);
			
			// Il s'agit d'un simple entier
			return true;
		} catch(NumberFormatException e) {
			// Vérifier s'il s'agit d'un paramètre spécial
			
			// Id de la dernière phase dans une poule
			Matcher idDernierePhase = Pattern.compile("%idDernierePhase\\([0-9]+,[0-9]+\\)%").matcher(valeur);
			if(idDernierePhase.find()) {
				return true;
			}
			
			// Numéro de la dernière phase dans une poule indicée de 1 à N
			Matcher numeroDernierePhase = Pattern.compile("%numeroDernierePhase\\([0-9]+,[0-9]+\\)%").matcher(valeur);
			if(numeroDernierePhase.find()) {
				return true;
			}
			
			// Il ne s'agit pas d'un paramètre spécial
			return false;
		}
	}
}
