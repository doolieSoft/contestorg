package org.contestorg.interfaces;

/**
 * Interface à implémenter si une classe souhaite être tenue au courant d'un objet lunatique
 */
public interface IMoodyListener
{
	// Recevoir une notification comme quoi l'objet lunatique a changé d'états
	public void moodyChanged(IMoody moody);
}
