package org.contestorg.infos;

public class InfosModelObjectifPoints extends InfosModelObjectif
{

	// Attributs
	private double points;
	private Double borneParticipation;

	// Constructeur
	public InfosModelObjectifPoints(String nom, double points, Double borneParticipation) {
		// Appeller le constructeur parent
		super(nom);

		// Enregistrer les informations
		this.points = points;
		this.borneParticipation = borneParticipation;
	}

	// Getters
	public double getPoints () {
		return this.points;
	}
	public Double getBorneParticipation () {
		return this.borneParticipation;
	}

	// Informations par défaut
	public static InfosModelObjectif defaut () {
		return new InfosModelObjectifPoints("", 0, null);
	}

}
