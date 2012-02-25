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
	 */
	public InfosModelPropriete(String nom, int type, boolean obligatoire) {
		this.nom = nom;
		this.type = type;
		this.obligatoire = obligatoire;
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
	 * Récupérer les données par défaut
	 * @return données par défaut
	 */
	public static InfosModelPropriete defaut () {
		return new InfosModelPropriete("", InfosModelPropriete.TYPE_INT, false);
	}
}
