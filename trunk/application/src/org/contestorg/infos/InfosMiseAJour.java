package org.contestorg.infos;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Conteneur d'informations pour une mise à jour
 */
public class InfosMiseAJour
{
	/**
	 * Téléchargement
	 */
	public static class Telechargement {
		/** Nom */
		private String nom;
		
		/** URL */
		private String url;

		/**
		 * Constructeur
		 * @param nom nom
		 * @param url URL
		 */
		public Telechargement(String nom, String url) {
			this.nom = nom;
			this.url = url;
		}

		/**
		 * Récupérer le nom
		 * @return nom
		 */
		public String getNom () {
			return nom;
		}

		/**
		 * Récupérer l'URL
		 * @return url
		 */
		public String getURL () {
			return url;
		}
	}
	
	/**
	 * Modification
	 */
	public static class Modification {
		/** Description */
		private String description;
		
		/** URL du ticket */
		private String ticket;
		
		/**
		 * Constructeur
		 * @param description description
		 * @param ticket ticket
		 */
		public Modification(String description, String ticket) {
			this.description = description;
			this.ticket = ticket;
		}
		
		/**
		 * Récupérer la description
		 * @return description
		 */
		public String getDescription() {
			return this.description;
		}
		
		/**
		 * Récupérer l'URL du ticket
		 * @return URL du ticket
		 */
		public String getTicket() {
			return this.ticket;
		}
	}

	/** Numéro de version */
	private String numero;
	
	/** Date de publication */
	private Calendar date;
	
	/** Liste des téléchargements */
	private ArrayList<Telechargement> telechargements;
	
	/** Liste des évolutions développées */
	private ArrayList<Modification> evolutions;
	
	/** Liste des anomalies corrigées */
	private ArrayList<Modification> anomalies;
	
	/** Liste des revues effectuées */
	private ArrayList<Modification> revues;

	/** Liste des tâches effectuées */
	private ArrayList<Modification> taches;
	
	/**
	 * Constructeur
	 * @param numero numéro de version
	 * @param date date de publication
	 * @param telechargements liste des téléchargements
	 * @param evolutions liste des évolutions développées
	 * @param anomalies liste des anomalies corrigées
	 * @param revues liste des revues effectuées
	 * @param taches liste des tâches effectuées
	 */
	public InfosMiseAJour(String numero, Calendar date, ArrayList<Telechargement> telechargements, ArrayList<Modification> evolutions, ArrayList<Modification> anomalies, ArrayList<Modification> revues, ArrayList<Modification> taches) {
		this.numero = numero;
		this.date = date;
		this.telechargements = telechargements;
		this.evolutions = evolutions;
		this.anomalies = anomalies;
		this.revues = revues;
		this.taches = taches;
	}

	/**
	 * Récupérer le numéro de version
	 * @return numéro de version
	 */
	public String getNumero () {
		return this.numero;
	}

	/**
	 * Récupérer la date de publication
	 * @return date de publication
	 */
	public Calendar getDate () {
		return this.date;
	}

	/**
	 * Récupérer la liste des téléchargements
	 * @return liste des téléchargements
	 */
	public ArrayList<Telechargement> getTelechargements () {
		return this.telechargements;
	}

	/**
	 * Récupérer la liste des évolutions développées
	 * @return liste des évolutions développées
	 */
	public ArrayList<Modification> getEvolutions () {
		return new ArrayList<Modification>(this.evolutions);
	}

	/**
	 * Récupérer la liste des anomalies corrigées
	 * @return liste des anomalies corrigées
	 */
	public ArrayList<Modification> getAnomalies () {
		return new ArrayList<Modification>(this.anomalies);
	}

	/**
	 * Récupérer la liste des revues effectuées
	 * @return liste des revues effectuées
	 */
	public ArrayList<Modification> getRevues () {
		return new ArrayList<Modification>(this.revues);
	}

	/**
	 * Récupérer la liste des tâches effectuées
	 * @return liste des tâches effectuées
	 */
	public ArrayList<Modification> getTaches () {
		return new ArrayList<Modification>(this.taches);
	}
	
}
