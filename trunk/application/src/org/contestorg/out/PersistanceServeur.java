package org.contestorg.out;

import org.contestorg.infos.InfosConnexionServeur;
import org.contestorg.models.ModelConcours;

/**
 * Persistance sur serveur
 */
public class PersistanceServeur extends PersistanceAbstract
{
	/**
	 * Constructeur
	 * @param infos informations de la connexion au serveur
	 */
	public PersistanceServeur(InfosConnexionServeur infos) {
		// TODO
	}

	/**
	 * @see PersistanceAbstract#load()
	 */
	@Override
	public ModelConcours load () {
		return null; // TODO
	}

	/**
	 * @see PersistanceAbstract#save()
	 */
	@Override
	public boolean save () {
		return false; // TODO
	}
}
