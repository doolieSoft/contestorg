package org.contestorg.infos;

/**
 * Conteneur d'informations pour la création ou la modification d'un concours
 */
public class InfosModelConcours extends InfosModelAbstract
{
	
	// Informations sur le concours

	/** Nom du concours */
	private String concoursNom;
	/** Site web du concours */
	private String concoursSite;
	/** Lieu du concours */
	private String concoursLieu;
	/** Email du concours */
	private String concoursEmail;
	/** Téléphone du concours */
	private String concoursTelephone;
	/** Description du concours */
	private String concoursDescription;
	
	// Informations sur l'organisateur

	/** Nom de l'organisateur */
	private String organisateurNom;
	/** Site web de l'organisateur */
	private String organisateurSite;
	/** Lieu de l'organisateur */
	private String organisateurLieu;
	/** Email de l'organisateur */
	private String organisateurEmail;
	/** Téléphone de l'organisateur */
	private String organisateurTelephone;
	/** Description de l'organisateur */
	private String organisateurDescription;
	
	// Informations diverses

	/** Type de qualifications */
	private int typeQualifications;
	/** Type de participants */
	private int typeParticipants;
	
	// Informations sur les points

	/** Points de victoires */
	private double pointsVictoire;
	/** Points d'égalité */
	private double pointsEgalite;
	/** Points de défaite */
	private double pointsDefaite;
	
	// Informations de programmation

	/** Durée d'un match (en minutes) */
	private Double programmationDuree;
	/** Interval minimal entre deux matchs (en minutes) */
	private Double programmationInterval;
	/** Pause minimal d'un participant entre deux matchs (en minutes) */
	private Double programmationPause;

	// Types des qualifications
	
	/** Qualification sous la forme de phases */
	public static final int QUALIFICATIONS_PHASES = 1;
	/** Qualification sous la forme d'une grille */
	public static final int QUALIFICATIONS_GRILLE = 2;

	// Types des participants
	
	/** Les participants sont des équipes */
	public static final int PARTICIPANTS_EQUIPES = 1;
	/** Les participants sont des joueurs */
	public static final int PARTICIPANTS_JOUEURS = 2;

	/**
	 * Constructeur
	 * @param concoursNom nom du concours
	 * @param concoursSite site web du concours
	 * @param concoursLieu lieu du concours
	 * @param concoursEmail email du concours
	 * @param concoursTelephone téléphone du concours
	 * @param concoursDescription description du concours
	 * @param organisateurNom nom de l'organisateur
	 * @param organisateurSite site web de l'organisateur
	 * @param organisateurLieu lieu de l'organisateur
	 * @param organisateurEmail email de l'organisateur
	 * @param organisateurTelephone téléphone de l'organisateur
	 * @param organisateurDescription description de l'organisateur
	 * @param typeQualifications type des qualifications
	 * @param typeParticipants type des participants
	 * @param pointsVictoire points de victoire
	 * @param pointsEgalite points d'égalité
	 * @param pointsDefaite points de défaite
	 * @param programmationDuree durée d'un match (en minutes)
	 * @param programmationInterval interval minimal entre deux matchs (en minutes)
	 * @param programmationPause pause minimal d'un participant entre deux matchs (en minutes)
	 */
	public InfosModelConcours(String concoursNom, String concoursSite, String concoursLieu, String concoursEmail, String concoursTelephone, String concoursDescription, String organisateurNom, String organisateurSite, String organisateurLieu, String organisateurEmail, String organisateurTelephone, String organisateurDescription, int typeQualifications, int typeParticipants, double pointsVictoire, double pointsEgalite, double pointsDefaite, Double programmationDuree, Double programmationInterval, Double programmationPause) {
		this.concoursNom = concoursNom;
		this.concoursSite = concoursSite;
		this.concoursEmail = concoursEmail;
		this.concoursLieu = concoursLieu;
		this.concoursTelephone = concoursTelephone;
		this.concoursDescription = concoursDescription;

		this.organisateurNom = organisateurNom;
		this.organisateurSite = organisateurSite;
		this.organisateurEmail = organisateurEmail;
		this.organisateurLieu = organisateurLieu;
		this.organisateurTelephone = organisateurTelephone;
		this.organisateurDescription = organisateurDescription;

		this.typeQualifications = typeQualifications;
		this.typeParticipants = typeParticipants;

		this.pointsVictoire = pointsVictoire;
		this.pointsEgalite = pointsEgalite;
		this.pointsDefaite = pointsDefaite;

		this.programmationDuree = programmationDuree;
		this.programmationInterval = programmationInterval;
		this.programmationPause = programmationPause;
	}
	
	/**
	 * Récupérer le nom du concours
	 * @return nom du concours
	 */
	public String getConcoursNom () {
		return this.concoursNom;
	}
	
	/**
	 * Récupérer le site web du concours
	 * @return site web du concours
	 */
	public String getConcoursSite () {
		return this.concoursSite;
	}
	
	/**
	 * Récupérer le lieu du concours
	 * @return lieu du concours
	 */
	public String getConcoursLieu () {
		return this.concoursLieu;
	}
	
	/**
	 * Récupérer l'email du concours
	 * @return email du concours
	 */
	public String getConcoursEmail () {
		return this.concoursEmail;
	}
	
	/**
	 * Récupérer le téléphone du concours
	 * @return téléphone du concours
	 */
	public String getConcoursTelephone () {
		return this.concoursTelephone;
	}
	
	/**
	 * Récupérer la description du concours
	 * @return description du concours
	 */
	public String getConcoursDescription () {
		return this.concoursDescription;
	}

	/**
	 * Récupérer le nom de l'organisateur
	 * @return nom de l'organisateur
	 */
	public String getOrganisateurNom () {
		return this.organisateurNom;
	}
	
	/**
	 * Récupérer le site web de l'organisateur
	 * @return site web de l'organisateur
	 */
	public String getOrganisateurSite () {
		return this.organisateurSite;
	}
	
	/**
	 * Récupérer le lieu de l'organisateur
	 * @return lieu de l'organisateur
	 */
	public String getOrganisateurLieu () {
		return this.organisateurLieu;
	}
	
	/**
	 * Récupérer l'email de l'organisateur
	 * @return email de l'organisateur
	 */
	public String getOrganisateurEmail () {
		return this.organisateurEmail;
	}
	
	/**
	 * Récupérer le téléphone de l'organisateur
	 * @return téléphone de l'organisateur
	 */
	public String getOrganisateurTelephone () {
		return this.organisateurTelephone;
	}
	
	/**
	 * Récupérer la description de l'organisateur
	 * @return description de l'organisateur
	 */
	public String getOrganisateurDescription () {
		return this.organisateurDescription;
	}

	/**
	 * Récupérer le type des qualifications
	 * @return type des qualifications
	 */
	public int getTypeQualifications () {
		return this.typeQualifications;
	}
	
	/**
	 * Récupérer le type des participants
	 * @return type des participants
	 */
	public int getTypeParticipants () {
		return this.typeParticipants;
	}

	/**
	 * Récupérer les points de victoire
	 * @return points de victoire
	 */
	public double getPointsVictoire () {
		return this.pointsVictoire;
	}
	
	/**
	 * Récupérer les points d'égalité
	 * @return points d'égalité
	 */
	public double getPointsEgalite () {
		return this.pointsEgalite;
	}
	
	/**
	 * Récupérer les points de défaite
	 * @return points de défaite
	 */
	public double getPointsDefaite () {
		return this.pointsDefaite;
	}

	/**
	 * Récupérer la durée d'un match (en minutes)
	 * @return durée d'un match (en minutes)
	 */
	public Double getProgrammationDuree () {
		return this.programmationDuree;
	}
	 
	/**
	 * Récupérer l'interval minimal entre deux matchs (en minutes)
	 * @return interval minimal entre deux matchs (en minutes)
	 */
	public Double getProgrammationInterval () {
		return this.programmationInterval;
	}
	
	/**
	 * Récupérer la pause minimal d'un participant entre deux matchs (en minutes)
	 * @return pause minimal d'un participant entre deux matchs (en minutes)
	 */
	public Double getProgrammationPause () {
		return this.programmationPause;
	}

	/**
	 * Récupérer les données par défaut
	 * @return données par défaut
	 */
	public static InfosModelConcours defaut () {
		return new InfosModelConcours("", "", "", "", "", "", "", "", "", "", "", "", InfosModelConcours.QUALIFICATIONS_PHASES, InfosModelConcours.PARTICIPANTS_EQUIPES, 4, 2, 1, null, null, null);
	}

}
