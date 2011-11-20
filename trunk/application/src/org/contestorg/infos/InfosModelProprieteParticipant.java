package org.contestorg.infos;

public class InfosModelProprieteParticipant extends InfosModelAbstract
{

	// Attributs
	private String value;

	// Constructeur
	public InfosModelProprieteParticipant(String value) {
		this.value = value;
	}

	// Getters
	public String getValue () {
		return this.value;
	}

	// Informations par défaut
	public static InfosModelProprieteParticipant defaut () {
		return new InfosModelProprieteParticipant("");
	}

}
