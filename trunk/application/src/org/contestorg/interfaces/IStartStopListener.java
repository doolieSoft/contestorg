package org.contestorg.interfaces;

/**
 * Interface à implémenter si une classe souhaite être tenue au courant du demarrage et de l'arret d'une opération
 */
public interface IStartStopListener
{
	/**
	 * Recevoir une notification comme quoi l'opération est démarrée
	 */
	public void start();
	
	/**
	 * Recevoir une notification comme quoi l'opération est terminée
	 */
	public void stop();
}
