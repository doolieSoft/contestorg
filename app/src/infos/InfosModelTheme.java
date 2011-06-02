package infos;

import java.io.File;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InfosModelTheme extends InfosModelAbstract
{
	// Attributs
	private String chemin;
	private HashMap<String,String> parametres;
	private HashMap<String,String> fichiers;
	
	// Constructeur
	public InfosModelTheme(String chemin, HashMap<String,String> parametres, HashMap<String,String> fichiers) {
		this.chemin = chemin;
		this.parametres = parametres;
		this.fichiers = fichiers;
	}

	// Getters
	public String getNom() {
		String[] repertoires = this.chemin.split(File.separator.equals("/") ? "/" : "\\\\");
		return repertoires[repertoires.length - 1];
	}
	public String getChemin () {
		return this.chemin;
	}
	public HashMap<String,String> getParametres () {
		return this.parametres;
	}
	public HashMap<String, String> getFichiers () {
		return this.fichiers;
	}

	// Informations par défaut
	public static InfosModelTheme defaut () {
		return new InfosModelTheme("", null, null);
	}
	
	// Vérifier si la valeur peut etre assimilée à un entier
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
