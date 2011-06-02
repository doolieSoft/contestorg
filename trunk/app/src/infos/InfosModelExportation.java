package infos;


public class InfosModelExportation extends InfosModelAbstract
{

	// Attributs
	private String nom;
	private boolean auto;

	// Constructeur
	public InfosModelExportation(String nom, boolean auto) {
		this.nom = nom;
		this.auto = auto;
	}

	// Getters
	public String getNom () {
		return this.nom;
	}
	public boolean isAuto () {
		return this.auto;
	}

}
