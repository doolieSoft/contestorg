package org.contestorg.infos;

/**
 * Conteneur d'informations pour la création ou la modification d'une propriété
 */
public class InfosModelPropriete extends InfosModelAbstract
{

	/** Nom */
	private String nom;
	
	/** Type */
	private int type;
	
	/** Obligatoire */
	private boolean obligatoire;
	
	/** Publique */
	private boolean publique;

	// Types de propriété
	
	/** Type entier */
	public static final int TYPE_INT = 1;
	
	/** Type décimal */
	public static final int TYPE_FLOAT = 2;
	
	/** Type texte */
	public static final int TYPE_STRING = 3;

	/**
	 * Constructeur
	 * @param nom nom
	 * @param type type
	 * @param obligatoire obligatoire
	 * @param publique publique
	 */
	public InfosModelPropriete(String nom, int type, boolean obligatoire, boolean publique) {
		this.nom = nom;
		this.type = type;
		this.obligatoire = obligatoire;
		this.publique = publique;
	}

	/**
	 * Récupérer le nom
	 * @return nom
	 */
	public String getNom () {
		return this.nom;
	}
	
	/**
	 * Récupérer le type
	 * @return type
	 */
	public int getType () {
		return this.type;
	}
	
	/**
	 * Savoir si la propriété est obligatoire
	 * @return propriété obligatoire ?
	 */
	public boolean isObligatoire () {
		return this.obligatoire;
	}
	
	/**
	 * Savoir si la propriété peut être affichée publiquement ?
	 * @return propriété pouvant être affichée publiquement ?
	 */
	public boolean isPublique () {
		return this.publique;
	}

	/**
	 * Récupérer les données par défaut
	 * @return données par défaut
	 */
	public static InfosModelPropriete defaut () {
		return new InfosModelPropriete("", InfosModelPropriete.TYPE_INT, false, false);
	}
}
