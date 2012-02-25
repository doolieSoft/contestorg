package org.contestorg.infos;

/**
 * Conteneur d'informations pour la création ou la modification d'un participant
 */
public class InfosModelParticipant extends InfosModelAbstract
{

	/** Stand */
	private String stand;
	
	/** Nom */
	private String nom;
	
	/** Ville */
	private String ville;
	
	/** Statut */
	private Statut statut;
	
	/** Détails */
	private String details;

	/**
	 * Statut d'un participation
	 */
	public static enum Statut {
		// Statuts possibles
		ABSENTE("absente","Absente","Absent",false),
		PRESENTE("presente","Présente","Présent",false),
		HOMOLOGUEE("homologuee","Homologuée","Homologué",true),
		FORFAIT("forfait","Forfait","Forfait",false),
		DISQUALIFIE("disqualifiee","Disqualifiée","Disqualifié",false);
		
		/** Id utilisé pour la persistance */
		private String id;
		
		/** Nom pour une équipe */
		private String nomEquipe;
		
		/** Nom pour un joueur */
		private String nomJoueur;
		
		/** Autorise la participation à un match ? */
		private boolean participation;
		
		/**
		 * Constructeur
		 * @param id id utilisé pour la persistance
		 * @param nomEquipe nom pour une équipe
		 * @param nomJoueur nom pour un joueur
		 * @param participation autorise la participation à un match ?
		 */
		Statut(String id,String nomEquipe,String nomJoueur,boolean participation) {
			this.id = id;
			this.nomEquipe = nomEquipe;
			this.nomJoueur = nomJoueur;
			this.participation = participation;
		}
		
		/**
		 * Récupérer un statut d'après son id utilisé pour la pesistance
		 * @param id id utilisé pour la persistance
		 * @return statut trouvé
		 */
		public static Statut search(String id) {
			for(Statut statut : Statut.values()) {
				if(statut.getId().equals(id)) {
					return statut;
				}
			}
			return Statut.ABSENTE;
		}
		
		/**
		 * Récupérer l'id utilisé pour la persistance
		 * @return id utilisé pour la persistance
		 */
		public String getId() {
			return this.id;
		}
		
		/**
		 * Récupérer le nom pour une équipe
		 * @return nom pour une équipe
		 */
		public String getNomEquipe() {
			return this.nomEquipe;
		}
		
		/**
		 * Récupérer le nom pour un joueur
		 * @return nom pour un joueur
		 */
		public String getNomJoueur() {
			return this.nomJoueur;
		}
		
		/**
		 * Savoir si le statut autorise la participation à un match ?
		 * @return autorise la participation à un match ?
		 */
		public boolean isParticipation() {
			return this.participation;
		}
	};

	/**
	 * Constructeur
	 * @param stand stand
	 * @param nom nom
	 * @param ville ville
	 * @param statut statut
	 * @param details détails
	 */
	public InfosModelParticipant(String stand, String nom, String ville, Statut statut, String details) {
		this.stand = stand;
		this.nom = nom;
		this.ville = ville;
		this.statut = statut;
		this.details = details;
	}

	/**
	 * Récupérer le stand
	 * @return stand
	 */
	public String getStand () {
		return this.stand;
	}
	
	/**
	 * Récupérer le nom
	 * @return nom
	 */
	public String getNom () {
		return this.nom;
	}
	
	/**
	 * Récupérer la ville
	 * @return ville
	 */
	public String getVille () {
		return this.ville;
	}
	
	/**
	 * Récupérer le statut
	 * @return statut
	 */
	public Statut getStatut () {
		return this.statut;
	}
	
	/**
	 * Récupérer les détails
	 * @return détails
	 */
	public String getDetails () {
		return this.details;
	}

	/**
	 * Récupérer les données par défaut
	 * @return données par défaut
	 */
	public static InfosModelParticipant defaut () {
		return new InfosModelParticipant("", "", "", Statut.ABSENTE, "");
	}

}
