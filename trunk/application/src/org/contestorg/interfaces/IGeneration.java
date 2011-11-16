package org.contestorg.interfaces;

/**
 * Interface à implémenter si une classe souhaite etre considérée comme une génération d'un objet de classe T
 */
public interface IGeneration<T>
{
	// Demarrer l'opération
	public void generationStart();
	
	// Arreter l'opération
	public void generationStop();
	
	// Annuler l'opération
	public void generationCancel();
	
	// Ajouter un listener
	public void addListener(IGenerationListener<T> listener);
	
	// Supprimer un listener
	public void removeListener(IGenerationListener<T> listener);
	
}
