package org.contestorg.interfaces;

/**
 * Interface à implémenter si une classe souhaite être tenue au courant d'une progression
 */
public interface IProgressionListener
{

	// Recevoir l'ancement de la génération
	public void progressionAvancement (double progression);

	// Recevoir l'étape de la génération
	public void progressionMessage (String message);

	// Recevoir l'information comme quoi la génération est terminée
	public void progressionFin ();
}
