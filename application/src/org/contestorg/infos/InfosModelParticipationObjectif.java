package org.contestorg.infos;

public class InfosModelParticipationObjectif extends InfosModelAbstract
{

	// Attributs
	private int quantite;

	// Constructeur
	public InfosModelParticipationObjectif(int quantite) {
		this.quantite = quantite;
	}

	// Getters
	public int getQuantite () {
		return this.quantite;
	}

	// Informations par défaut
	public static InfosModelParticipationObjectif defaut () {
		return new InfosModelParticipationObjectif(0);
	}

}
