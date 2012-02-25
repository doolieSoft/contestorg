package org.contestorg.interfaces;

/**
 * Interface à implémenter si une classe souhaite être tenue au courant de l'avancement d'une opération 
 */
public interface IOperationListener extends IProgressionListener
{
	// Recevoir un message comme quoi l'opération est une réussie
	public void operationReussite ();

	// Recevoir un message comme quoi l'opération a échoué
	public void operationEchec ();

	// Recevoir un message comme quoi l'opération a été arreté
	public void operationArret ();
}
