package infos;

/**
 * Cette classe est un conteneur d'information pour la création ou la modification d'un objet emplacement
 */
public class InfosModelEmplacement extends InfosModelAbstract
{

	// Attributs
	private String nom;
	private String description;

	// Constructeur
	public InfosModelEmplacement(String nom, String description) {
		this.nom = nom;
		this.description = description;
	}

	// Getters
	public String getNom () {
		return nom;
	}
	public String getDescription () {
		return description;
	}

	// Informations par défaut
	public static InfosModelEmplacement defaut () {
		return new InfosModelEmplacement("", "");
	}

}
