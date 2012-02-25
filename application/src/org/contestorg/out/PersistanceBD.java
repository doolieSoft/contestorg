package org.contestorg.out;

import org.contestorg.infos.InfosConnexionBD;
import org.contestorg.models.ModelConcours;

/**
 * Persistance sur base de données
 */
public class PersistanceBD extends PersistanceAbstract
{
	/**
	 * Constructeur
	 * @param infos informations de la connexion à la base de données
	 */
	public PersistanceBD(InfosConnexionBD infos) {
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
	 * PersistanceAbstract.save()
	 */
	@Override
	public boolean save () {
		return false; // TODO
	}
}
