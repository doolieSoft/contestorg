package org.contestorg.infos;

/**
 * Conteneur d'informations pour la création ou la modification d'un critère de classement prenant en compte le goal-average
 */
public class InfosModelCritereClassementGoalAverage extends InfosModelCritereClassementAbstract
{
	/** Type */
	private int type;

	/** Méthode utilisée */
	private int methode;
	
	/** Donnée utilisée */
	private int donnee;
	
	/** Informations de l'objectif associé */
	private InfosModelObjectif objectif;
	
	// Types
	
	/** Particulier */
	public static final int TYPE_PARTICULIER = 1;
	
	/** Général */
	public static final int TYPE_GENERAL = 2;
	
	// Méthodes utilisées
	
	/** Différence */
	public static final int METHODE_DIFFERENCE = 1;
	
	/** Division */
	public static final int METHODE_DIVISION = 2;
	
	// Données utilisées

	/** Points */
	public static final int DONNEE_POINTS = 1;
	
	/** Résultat */
	public static final int DONNEE_RESULTAT = 2;
	
	/** Quantité remportée d'un objectif */
	public static final int DONNEE_QUANTITE_OBJECTIF = 3;
	
	/**
	 * Constructeur
	 * @param isInverse est-ce que le critère est inversé ?
	 * @param type type
	 * @param methode méthode utilisée
	 * @param donnee donnée utilisée
	 */
	public InfosModelCritereClassementGoalAverage(boolean isInverse,int type,int methode,int donnee) {
		this(isInverse,type,methode,donnee,null);
	}
	
	/**
	 * Constructeur
	 * @param isInverse est-ce que le critère est inversé ?
	 * @param type type
	 * @param methode méthode utilisée
	 * @param donnee donnée utilisée 
	 * @param objectif informations de l'objectif associé
	 */
	public InfosModelCritereClassementGoalAverage(boolean isInverse,int type,int methode,int donnee,InfosModelObjectif objectif) {
		super(isInverse);
		this.type = type;
		this.methode = methode;
		this.donnee = donnee;
		this.objectif = objectif;
	}
	
	/**
	 * Récupérer le type
	 * @return type
	 */
	public int getType() {
		return this.type;
	}
	
	/**
	 * Récupérer la méthode utilisée
	 * @return méthode utilisée
	 */
	public int getMethode() {
		return this.methode;
	}
	
	/**
	 * Récupérer la donnée utilisée
	 * @return donnée utilisée
	 */
	public int getDonnee() {
		return this.donnee;
	}
	
	/**
	 * Récupérer les informations de l'objectif associé
	 * @return informations de l'objectif associé
	 */
	public InfosModelObjectif getObjectif() {
		return this.objectif;
	}

	/**
	 * @see InfosModelCritereClassementAbstract#equals(InfosModelCritereClassementAbstract)
	 */
	@Override
	public boolean equals (InfosModelCritereClassementAbstract infos) {
		if(infos == null) {
			return false;
		}
		if(!(infos instanceof InfosModelCritereClassementGoalAverage)) {
			return false;
		}
		InfosModelCritereClassementGoalAverage cast = (InfosModelCritereClassementGoalAverage)infos;
		if(cast.getType() != this.getType() || cast.getMethode() != this.getMethode() || cast.getDonnee() != this.getDonnee()) {
			return false;
		}
		if(cast.getDonnee() == InfosModelCritereClassementGoalAverage.DONNEE_QUANTITE_OBJECTIF) {
			return cast.getObjectif().getNom().equals(this.getObjectif().getNom());
		}
		return true;
	}

	/**
	 * @see InfosModelCritereClassementAbstract#isUtilisablePlusieursFois()
	 */
	@Override
	public boolean isUtilisablePlusieursFois () {
		return this.type == InfosModelCritereClassementGoalAverage.TYPE_PARTICULIER;
	}
	
}
