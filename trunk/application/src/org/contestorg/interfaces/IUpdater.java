package org.contestorg.interfaces;

/**
 * Interface à implémenter si une classe souhaite gérer la création et la modification d'une classe d'objet
 * @param <I> classe des informations des objets à créer/modifier
 * @param <O> classe des objets à créer/modifier
 */
public interface IUpdater<I,O>
{
	/**
	 * Créer objet à partir d'informations
	 * @param infos informations
	 * @return objet créé
	 */
	public O create(I infos);
	
	/**
	 * Mettre à jour un objet à partir d'informations 
	 * @param objet objet à mettre à jour
	 * @param infos informations
	 */
	public void update(O objet, I infos);
}
