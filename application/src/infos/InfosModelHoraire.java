package infos;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Cette classe est un conteneur d'information pour la création ou la modification d'un objet horaire
 */
public class InfosModelHoraire extends InfosModelAbstract
{

	// Jours
	public static final int JOUR_LUNDI = 1;
	public static final int JOUR_MARDI = 2;
	public static final int JOUR_MERCREDI = 4;
	public static final int JOUR_JEUDI = 8;
	public static final int JOUR_VENDREDI = 16;
	public static final int JOUR_SAMEDI = 32;
	public static final int JOUR_DIMANCHE = 64;

	// Attributs
	private int jours; // Jours concernés
	private int debut; // En minutes
	private int fin; // En minutes

	// Constructeur
	public InfosModelHoraire(int jours, int debut, int fin) {
		this.jours = jours;
		this.debut = debut;
		this.fin = fin;
	}

	// Getters
	public int getJours () {
		return this.jours;
	}
	public int getDebut () {
		return this.debut;
	}
	public int getFin () {
		return this.fin;
	}

	// Informations par défaut
	public static InfosModelHoraire defaut () {
		return new InfosModelHoraire(0, 9 * 3600, 17 * 3600);
	}
	
	// Récupérer l'heure au format humain à partir du nombre de minutes depuis le début de la journée
	public static String getHeureHumain(int minutes) {
		// Initialiser l'heure
		StringBuilder heure = new StringBuilder();
		
		// Ajouter les heures
		heure.append(((int)minutes/60)+"h");
		
		// Ajouter les minutes
		if(minutes%60 != 0) {
			NumberFormat formatter = new DecimalFormat("##00");
			heure.append(formatter.format(minutes%60));
		}
		
		// Retourner l'heure
		return heure.toString();
	}
	
	// Récupérer le nombre de minutes depuis le début de la journée à partir de l'heure au format humain (-1 si erreur)
	public static int getHeureMinutes(String heure) {
		// Parser l'heure
		Pattern pattern = Pattern.compile("([0-9]{1,2})h([0-9]{2,2})?",Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(heure.trim());
		if(matcher.find()) {
			try {
				// Récupérer les heures et les minutes
				int heures = Integer.parseInt(matcher.group(1));
				int minutes = matcher.group(2) != null ? Integer.parseInt(matcher.group(2)) : 0;

				// Vérifier si les heures et les minutes sont correctes
				if(0 <= heures && heures < 24 && 0 <= minutes && minutes < 60) {
					return heures*60 + minutes;
				}
			} catch(NumberFormatException e) { }
		}
		return -1; // Retourner -1 si erreur
	}
	
	// Récupérer les jours au format humain à partir du OR binaire sur la valeur des jours
	public static String getJoursHumain(int jours) {
		StringBuffer result = new StringBuffer();
		if((InfosModelHoraire.JOUR_LUNDI & jours) != 0) {
			if(result.length() != 0) result.append(", ");
			result.append("Lundi");
		}
		if((InfosModelHoraire.JOUR_MARDI & jours) != 0) {
			if(result.length() != 0) result.append(", ");
			result.append("Mardi");
		}
		if((InfosModelHoraire.JOUR_MERCREDI & jours) != 0) {
			if(result.length() != 0) result.append(", ");
			result.append("Mercredi");
		}
		if((InfosModelHoraire.JOUR_JEUDI & jours) != 0) {
			if(result.length() != 0) result.append(", ");
			result.append("Jeudi");
		}
		if((InfosModelHoraire.JOUR_VENDREDI & jours) != 0) {
			if(result.length() != 0) result.append(", ");
			result.append("Vendredi");
		}
		if((InfosModelHoraire.JOUR_SAMEDI & jours) != 0) {
			if(result.length() != 0) result.append(", ");
			result.append("Samedi");
		}
		if((InfosModelHoraire.JOUR_DIMANCHE & jours) != 0) {
			if(result.length() != 0) result.append(", ");
			result.append("Dimanche");
		}
		return result.toString();
	}
	
	// Récupérer la liste des jours (indexation de 0 à 6) à partir du OR binaire sur la valeur des jours
	public static int[] getJoursIndex(int jours) {
		// Placer les jours dans un ArrayList
		ArrayList<Integer> arraylist = new ArrayList<Integer>();
		if((InfosModelHoraire.JOUR_LUNDI & jours) != 0) arraylist.add(0);
		if((InfosModelHoraire.JOUR_MARDI & jours) != 0) arraylist.add(1);
		if((InfosModelHoraire.JOUR_MERCREDI & jours) != 0) arraylist.add(2);
		if((InfosModelHoraire.JOUR_JEUDI & jours) != 0) arraylist.add(3);
		if((InfosModelHoraire.JOUR_VENDREDI & jours) != 0) arraylist.add(4);
		if((InfosModelHoraire.JOUR_SAMEDI & jours) != 0) arraylist.add(5);
		if((InfosModelHoraire.JOUR_DIMANCHE & jours) != 0) arraylist.add(6);
		
		// Transformer l'ArrayList en Array
		int[] array = new int[arraylist.size()];
		for(int i=0;i<arraylist.size();i++) {
			array[i] = arraylist.get(i);
		}
		
		// Retourner l'Array
		return array;
	}
	
	// Récupérer le OR binaire sur la valeur des jours à partir des jours au format humain (0 si erreur)
	public static int getJoursBinaire(String jours) {
		// Passer en minuscule la chaine
		jours = jours.toLowerCase();
		
		// Rechercher les jours dans le string
		int result = 0;
		if(jours.indexOf("lundi") != -1) result += InfosModelHoraire.JOUR_LUNDI;
		if(jours.indexOf("mardi") != -1) result += InfosModelHoraire.JOUR_MARDI;
		if(jours.indexOf("mercredi") != -1) result += InfosModelHoraire.JOUR_MERCREDI;
		if(jours.indexOf("jeudi") != -1) result += InfosModelHoraire.JOUR_JEUDI;
		if(jours.indexOf("vendredi") != -1) result += InfosModelHoraire.JOUR_VENDREDI;
		if(jours.indexOf("samedi") != -1) result += InfosModelHoraire.JOUR_SAMEDI;
		if(jours.indexOf("dimanche") != -1) result += InfosModelHoraire.JOUR_DIMANCHE;
		
		// Retourner le résultat
		return result;
	}
	
	// Récupérer le OR binaire sur la valeur des jours à partir de la liste des jours (indexation de 0 à 6)
	public static int getJoursBinaire(int[] jours) {
		int result = 0;
		for(int jour : jours) {
			result += Math.pow(2, jour);
		}
		return result;
	}
	
	// Savoir si l'horaire chevauche un autre horaire
	public boolean chevauche(InfosModelHoraire horaire) {
		return
			(this.getJours() & horaire.getJours()) > 0 && // Il y a des jours en commun entre les deux horaires
			(
				this.getDebut() <= horaire.getDebut() && horaire.getDebut() <= this.getFin() ||	// Début de l'horaire "horaire" compris dans l'horaire "this"
				this.getDebut() <= horaire.getFin() && horaire.getFin() <= this.getFin() ||		// Fin de l'horaire "horaire" compris dans l'horaire "this"
				horaire.getDebut() <= this.getDebut() && this.getFin() <= horaire.getFin()		// Horaire "this" compris dans l'horaire "horaire" 
			);
	}

}
