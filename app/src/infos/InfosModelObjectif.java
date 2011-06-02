package infos;

/**
 * Cette classe est un conteneur d'information pour la création ou la modification d'un objet objectif
 */
public abstract class InfosModelObjectif extends InfosModelAbstract
{

	// Attributs
	private String nom;

	// Constructeur
	public InfosModelObjectif(String nom) {
		this.nom = nom;
	}

	// Getters
	public String getNom () {
		return this.nom;
	}

}
