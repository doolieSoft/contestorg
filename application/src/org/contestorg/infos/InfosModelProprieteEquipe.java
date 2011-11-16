package org.contestorg.infos;

public class InfosModelProprieteEquipe extends InfosModelAbstract
{

	// Attributs
	private String value;

	// Constructeur
	public InfosModelProprieteEquipe(String value) {
		this.value = value;
	}

	// Getters
	public String getValue () {
		return this.value;
	}

	// Informations par défaut
	public static InfosModelProprieteEquipe defaut () {
		return new InfosModelProprieteEquipe("");
	}

}
