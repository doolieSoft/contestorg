package infos;

/**
 * Cette classe est un conteneur d'information pour la création ou la modification d'un objet categorie
 */
public class InfosModelCategorie extends InfosModelAbstract
{

	// Attributs
	private String nom;

	// Constructeur
	public InfosModelCategorie(String nom) {
		this.nom = nom;
	}

	// Getters
	public String getNom () {
		return this.nom;
	}

	// Informations par défaut
	public static InfosModelCategorie defaut () {
		return new InfosModelCategorie("");
	}

}
