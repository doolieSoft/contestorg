package infos;

/**
 * Cette classe est un conteneur d'information pour la création ou la modification d'un objet prix
 */
public class InfosModelPrix extends InfosModelAbstract
{

	// Attributs
	private String nom;

	// Constructeur
	public InfosModelPrix(String nom) {
		this.nom = nom;
	}

	// Getters
	public String getNom () {
		return this.nom;
	}

	// Informations par défaut
	public static InfosModelPrix defaut () {
		return new InfosModelPrix("");
	}

}
