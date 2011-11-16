package infos;

/**
 * Cette classe est un conteneur d'information pour la création ou la modification d'un objet equipe
 */
public class InfosModelEquipe extends InfosModelAbstract
{

	// Attributs
	private String stand;
	private String nom;
	private String ville;
	private Statut statut;
	private String membres;
	private String details;

	// Statuts
	public static enum Statut {
		// Statuts possibles
		ABSENTE("absente","Absente","Absent",false),
		PRESENTE("presente","Présente","Présent",false),
		HOMOLOGUEE("homologuee","Homologuée","Homologué",true),
		FORFAIT("forfait","Forfait","Forfait",false),
		DISQUALIFIE("disqualifiee","Disqualifiée","Disqualifié",false);
		
		// Attributs
		private String id; // Utile pour la persistance 
		private String nomEquipe; // Nom du statut pour une équipe 
		private String nomJoueur; // Nom du statut pour un joueur
		private boolean participation; // Est-ce que ce statut autorise la participant à un match ?
		
		// Constructeur
		Statut(String id,String nomEquipe,String nomJoueur,boolean participation) {
			this.id = id;
			this.nomEquipe = nomEquipe;
			this.nomJoueur = nomJoueur;
			this.participation = participation;
		}
		
		// Récupérer un statut d'après son id
		public static Statut search(String id) {
			for(Statut statut : Statut.values()) {
				if(statut.getId().equals(id)) {
					return statut;
				}
			}
			return Statut.ABSENTE;
		}
		
		// Getters
		public String getId() {
			return this.id;
		}
		public String getNomEquipe() {
			return this.nomEquipe;
		}
		public String getNomJoueur() {
			return this.nomJoueur;
		}
		public boolean isParticipante() {
			return this.participation;
		}
	};
	public static final int STATUT_ABSENTE = 1;
	public static final int STATUT_ARRIVEE = 2;
	public static final int STATUT_HOMOLOGUEE = 3;
	public static final int STATUT_FORFAIT = 4;
	public static final int STATUT_DISQUALIFIEE = 5;

	// Constructeur
	public InfosModelEquipe(String stand, String nom, String ville, Statut statut, String membres, String details) {
		this.stand = stand;
		this.nom = nom;
		this.ville = ville;
		this.statut = statut;
		this.membres = membres;
		this.details = details;
	}

	// Getters
	public String getStand () {
		return this.stand;
	}
	public String getNom () {
		return this.nom;
	}
	public String getVille () {
		return this.ville;
	}
	public Statut getStatut () {
		return this.statut;
	}
	public String getMembres () {
		return this.membres;
	}
	public String getDetails () {
		return this.details;
	}

	// Informations par défaut
	public static InfosModelEquipe defaut () {
		return new InfosModelEquipe("", "", "", Statut.ABSENTE, "", "");
	}

}
