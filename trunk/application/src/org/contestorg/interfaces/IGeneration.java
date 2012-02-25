package org.contestorg.interfaces;

/**
 * Interface à implémenter si une classe souhaite être considérée comme une génération d'un objet
 * @param <T> classe de l'objet généré
 */
public interface IGeneration<T>
{
	/** Demarrer la génération */
	public void generationStart();
	
	/** Arrêter la la génération */
	public void generationStop();
	
	/** Annuler la génération */
	public void generationCancel();
	
	/**
	 * Ajouter un listener
	 * @param listener listener à ajouter
	 */
	public void addListener(IGenerationListener<T> listener);
	
	/**
	 * Retirer un listener
	 * @param listener listener à retirer
	 */
	public void removeListener(IGenerationListener<T> listener);
	
}
