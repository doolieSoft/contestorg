package org.contestorg.interfaces;

/**
 * Interface à implémenter si une classe souhaite être tenue au courant de la fin d'une opération
 */
public interface IEndListener
{
	/**
	 * Recevoir une notification comme quoi l'opération est terminée
	 */
	public void end();
}
