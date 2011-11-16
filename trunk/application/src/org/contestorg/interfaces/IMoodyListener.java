package org.contestorg.interfaces;

/**
 * Interface à implémenter si une classe souhaite etre tenue au courant d'une instance lunatique
 */
public interface IMoodyListener
{
	// Recoit une notification comme quoi l'instance lunatique a changé d'états
	public void moodyChanged(IMoody moody);
}
