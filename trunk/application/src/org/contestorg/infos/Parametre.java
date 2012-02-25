package org.contestorg.infos;

/**
 * Paramètre de thème d'exportation/diffusion
 */
public class Parametre
{	
	/** Identifiant du paramètre */
	private String id;
	
	/** Nom du paramètre */
	private String nom;
	
	/** Type du paramètre */
	private int type;
	
	/** Description du paramètre */
	private String description;
	
	/** Paramètre optionnel ? */
	private boolean optionnel;
	
	/** Identifiant du paramètre lié */
	private String dependance;
	
	/** Valeur par défaut */
	private String defaut;
	
	// Types de paramètre
	
	/** Type entier */
	public static final int TYPE_ENTIER = 1;
	
	/** Type réel */
	public static final int TYPE_REEL = 2;
	
	/** Type texte */
	public static final int TYPE_TEXTE = 3;
	
	/** Type catégorie */
	public static final int TYPE_CATEGORIE = 4;
	
	/** Type poule */
	public static final int TYPE_POULE = 5;
	
	/** Type phase qualificative */
	public static final int TYPE_PHASE = 6;
	
	/** Type mot de passe */
	public static final int TYPE_MOTDEPASSE = 7;
	
	/**
	 * Constructeur
	 * @param id identifiant du paramètre
	 * @param nom nom du paramètre
	 * @param type type du paramètre
	 * @param description description du paramètre
	 * @param optionnel paramètre optionnel ?
	 * @param dependance identifiant du paramètre lié
	 * @param defaut valeur par défaut
	 */
	public Parametre(String id, String nom, int type, String description, boolean optionnel, String dependance, String defaut) {
		this.id = id;
		this.nom = nom;
		this.type = type;
		this.description = description;
		this.optionnel = optionnel;
		this.dependance = dependance; 
		this.defaut = defaut; 
	}
	
	/**
	 * Récupérer l'identifiant du paramètre
	 * @return identifiant du paramètre
	 */
	public String getId () {
		return this.id;
	}
	
	/**
	 * Récupérer le nom du paramètre
	 * @return nom du paramètre
	 */
	public String getNom () {
		return this.nom;
	}
	
	/**
	 * Récupérer le type du paramètre
	 * @return type du paramètre
	 */
	public int getType () {
		return this.type;
	}
	
	/**
	 * Récupérer la description du paramètre
	 * @return description du paramètre
	 */
	public String getDescription () {
		return this.description;
	}
	
	/**
	 * Vérifier s'il s'agit d'un fichier optionnel
	 * @return fichier optionnel ?
	 */
	public boolean isOptionnel() {
		return this.optionnel;
	}
	
	/**
	 * Récupérer l'identifiant du paramètre lié
	 * @return identifiant du paramètre lié
	 */
	public String getDependance() {
		return this.dependance;
	}
	
	/**
	 * Récupérer la valeur par défaut
	 * @return valeur par défaut
	 */
	public String getDefaut() {
		return this.defaut;
	}
	
}
