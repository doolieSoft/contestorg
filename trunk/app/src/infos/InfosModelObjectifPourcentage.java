package infos;

public class InfosModelObjectifPourcentage extends InfosModelObjectif
{

	// Attributs
	private double pourcentage;
	private Double borneParticipation;
	private Double borneAugmentation;

	// Constructeur
	public InfosModelObjectifPourcentage(String nom, double pourcentage, Double borneParticipation, Double borneAugmentation) {
		// Appeller le constructeur parent
		super(nom);

		// Enregistrer les informations
		this.pourcentage = pourcentage;
		this.borneParticipation = borneParticipation;
		this.borneAugmentation = borneAugmentation;
	}

	// Getters
	public double getPourcentage () {
		return this.pourcentage;
	}
	public Double getBorneParticipation () {
		return this.borneParticipation;
	}
	public Double getBorneAugmentation () {
		return this.borneAugmentation;
	}

	// Informations par défaut
	public static InfosModelObjectif defaut () {
		return new InfosModelObjectifPourcentage("", 0, null, null);
	}

}
