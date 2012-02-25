package org.contestorg.infos;

/**
 * Conteneur d'informations
 */
public abstract class InfosModelAbstract
{
	/** Id du modèle */
	private int id;	
	
	/**
	 * Récupérer l'id du modèle
	 * @return id du modèle
	 */
	public int getId() {
		return this.id;
	}
	
	/**
	 * Définir l'id du modèle
	 * @param id id du modèle
	 */
	public void setId(int id) {
		this.id = id;
	}
}
