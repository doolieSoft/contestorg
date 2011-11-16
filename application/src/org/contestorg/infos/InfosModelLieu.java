package org.contestorg.infos;

/**
 * Cette classe est un conteneur d'information pour la création ou la modification d'un objet lieu
 */
public class InfosModelLieu extends InfosModelAbstract
{

	// Attributs
	private String nom;
	private String lieu;
	private String telephone;
	private String email;
	private String description;

	// Constructeur
	public InfosModelLieu(String nom, String lieu, String telephone, String email, String description) {
		this.nom = nom;
		this.lieu = lieu;
		this.telephone = telephone;
		this.email = email;
		this.description = description;
	}

	// Getters
	public String getNom () {
		return this.nom;
	}
	public String getLieu () {
		return this.lieu;
	}
	public String getTelephone () {
		return this.telephone;
	}
	public String getEmail () {
		return this.email;
	}
	public String getDescription () {
		return this.description;
	}

	// Informations par défaut
	public static InfosModelLieu defaut () {
		return new InfosModelLieu("", "", "", "", "");
	}

}
