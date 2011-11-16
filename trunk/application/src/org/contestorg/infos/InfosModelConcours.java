package infos;

/**
 * Cette classe est un conteneur d'information pour la création ou la modification d'un objet concours
 */
public class InfosModelConcours extends InfosModelAbstract
{

	// Attributs
	private String concoursNom;
	private String concoursSite;
	private String concoursLieu;
	private String concoursEmail;
	private String concoursTelephone;
	private String concoursDescription;

	private String organisateurNom;
	private String organisateurSite;
	private String organisateurLieu;
	private String organisateurEmail;
	private String organisateurTelephone;
	private String organisateurDescription;

	private int typeQualifications; // Type de qualifications
	private int typeParticipants; // Type de participants

	private double pointsVictoire;
	private double pointsEgalite;
	private double pointsDefaite;

	private Double programmationDuree; // En minutes
	private Double programmationInterval; // En minutes
	private Double programmationPause; // En minutes

	// Types de qualifications
	public static final int QUALIFICATIONS_PHASES = 1;
	public static final int QUALIFICATIONS_GRILLE = 2;

	// Types de participants
	public static final int PARTICIPANTS_EQUIPES = 1;
	public static final int PARTICIPANTS_JOUEURS = 2;

	// Constructeur
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

	// Getters
	public String getConcoursNom () {
		return this.concoursNom;
	}
	public String getConcoursSite () {
		return this.concoursSite;
	}
	public String getConcoursLieu () {
		return this.concoursLieu;
	}
	public String getConcoursEmail () {
		return this.concoursEmail;
	}
	public String getConcoursTelephone () {
		return this.concoursTelephone;
	}
	public String getConcoursDescription () {
		return this.concoursDescription;
	}

	public String getOrganisateurNom () {
		return this.organisateurNom;
	}
	public String getOrganisateurSite () {
		return this.organisateurSite;
	}
	public String getOrganisateurLieu () {
		return this.organisateurLieu;
	}
	public String getOrganisateurEmail () {
		return this.organisateurEmail;
	}
	public String getOrganisateurTelephone () {
		return this.organisateurTelephone;
	}
	public String getOrganisateurDescription () {
		return this.organisateurDescription;
	}

	public int getTypeQualifications () {
		return this.typeQualifications;
	}
	public int getTypeParticipants () {
		return this.typeParticipants;
	}

	public double getPointsVictoire () {
		return this.pointsVictoire;
	}
	public double getPointsEgalite () {
		return this.pointsEgalite;
	}
	public double getPointsDefaite () {
		return this.pointsDefaite;
	}

	public Double getProgrammationDuree () {
		return this.programmationDuree;
	}
	public Double getProgrammationInterval () {
		return this.programmationInterval;
	}
	public Double getProgrammationPause () {
		return this.programmationPause;
	}

	// Informations par défaut
	public static InfosModelConcours defaut () {
		return new InfosModelConcours("", "", "", "", "", "", "", "", "", "", "", "", InfosModelConcours.QUALIFICATIONS_PHASES, InfosModelConcours.PARTICIPANTS_EQUIPES, 4, 2, 1, null, null, null);
	}

}
