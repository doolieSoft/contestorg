package org.contestorg.infos;

/**
 * Conteneur d'informations pour la création ou la modification d'une diffusion
 */
public class InfosModelDiffusion extends InfosModelAbstract
{	
	/** Nom */
	private String nom;
	
	/** Numéro de port */
	private int port;

	/**
	 * Constructeur
	 * @param nom nom
	 * @param port numéro de port
	 */
	public InfosModelDiffusion(String nom,int port) {
		this.nom = nom;
		this.port = port;
	}

	/**
	 * Récupérer le nom
	 * @return nom
	 */
	public String getNom() {
		return this.nom;
	}
	
	/**
	 * Récupérer le numéro de port
	 * @return numéro de port
	 */
	public int getPort () {
		return this.port;
	}

	/**
	 * Récupérer les données par défaut
	 * @return données par défaut
	 */
	public static InfosModelDiffusion defaut () {
		return new InfosModelDiffusion("",80);
	}
}
