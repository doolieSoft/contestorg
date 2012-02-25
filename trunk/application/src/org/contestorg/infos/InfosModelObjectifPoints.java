package org.contestorg.infos;
/**
 * 
 * Conteneur d'informations pour la création ou la modification d'un objectif à points
 */
public class InfosModelObjectifPoints extends InfosModelObjectif
{

	/** Nombre de points */
	private double points;
	
	/** Borne de participation */
	private Double borneParticipation;

	/**
	 * Constructeur
	 * @param nom nom
	 * @param points nombre de points
	 * @param borneParticipation borne de participation
	 */
	public InfosModelObjectifPoints(String nom, double points, Double borneParticipation) {
		// Appeller le constructeur parent
		super(nom);

		// Enregistrer les informations
		this.points = points;
		this.borneParticipation = borneParticipation;
	}

	/**
	 * Récupérer le nombre de points
	 * @return nombre de points
	 */
	public double getPoints () {
		return this.points;
	}
	
	/**
	 * Récupérer la borne de participation
	 * @return borne de participation
	 */
	public Double getBorneParticipation () {
		return this.borneParticipation;
	}

	/**
	 * Récupérer les données par détaut
	 * @return données par défaut
	 */
	public static InfosModelObjectif defaut () {
		return new InfosModelObjectifPoints("", 0, null);
	}

}
