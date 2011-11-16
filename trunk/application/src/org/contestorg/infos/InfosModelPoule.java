package infos;

public class InfosModelPoule extends InfosModelAbstract
{

	// Attributs
	private String nom;
	
	// Constructeur
	public InfosModelPoule(String nom) {
		this.nom = nom;
	}
	
	// Getters
	public String getNom () {
		return this.nom;
	}

}
