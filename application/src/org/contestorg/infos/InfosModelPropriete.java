package org.contestorg.infos;

public class InfosModelPropriete extends InfosModelAbstract
{

	// Attributs
	private String nom;
	private int type;
	private boolean obligatoire;

	// Types de propriété
	public static final int TYPE_INT = 1;
	public static final int TYPE_FLOAT = 2;
	public static final int TYPE_STRING = 3;

	// Constructeur
	public InfosModelPropriete(String nom, int type, boolean obligatoire) {
		this.nom = nom;
		this.type = type;
		this.obligatoire = obligatoire;
	}

	// Getters
	public String getNom () {
		return this.nom;
	}
	public int getType () {
		return this.type;
	}
	public boolean isObligatoire () {
		return this.obligatoire;
	}

	// Informations par défaut
	public static InfosModelPropriete defaut () {
		return new InfosModelPropriete("", InfosModelPropriete.TYPE_INT, false);
	}
}
