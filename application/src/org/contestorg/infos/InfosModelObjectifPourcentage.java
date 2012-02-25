package org.contestorg.infos;

/**
 * Conteneur d'informations pour la création ou la modification d'un objectif à pourcentage
 */
public class InfosModelObjectifPourcentage extends InfosModelObjectif
{

	/** Pourcentage */
	private double pourcentage;
	
	/** Borne de participation */
	private Double borneParticipation;
	
	/** Borne d'augmentation */
	private Double borneAugmentation;

	/**
	 * Constructeur
	 * @param nom nom
	 * @param pourcentage pourcentage
	 * @param borneParticipation borne de participation
	 * @param borneAugmentation borne d'augmentation
	 */
	public InfosModelObjectifPourcentage(String nom, double pourcentage, Double borneParticipation, Double borneAugmentation) {
		// Appeller le constructeur parent
		super(nom);

		// Enregistrer les informations
		this.pourcentage = pourcentage;
		this.borneParticipation = borneParticipation;
		this.borneAugmentation = borneAugmentation;
	}

	/**
	 * Récupérer le pourcentage
	 * @return pourcentage
	 */
	public double getPourcentage () {
		return this.pourcentage;
	}
	
	/**
	 * Récupérer la borne de participation
	 * @return borne de participation
	 */
	public Double getBorneParticipation () {
		return this.borneParticipation;
	}
	
	/**
	 * Récupérer la borne d'augmentation
	 * @return borne d'augmentation
	 */
	public Double getBorneAugmentation () {
		return this.borneAugmentation;
	}

	/**
	 * Récupérer les données par défaut
	 * @return données par défaut
	 */
	public static InfosModelObjectif defaut () {
		return new InfosModelObjectifPourcentage("", 0, null, null);
	}

}
