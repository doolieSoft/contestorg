package org.contestorg.interfaces;

/**
 * Interface à implémenter si une classe souhaite etre tenue au courant de l'avancement d'une opération 
 */
public interface IOperationListener extends IProgressionListener
{
	// Recoit un message comme quoi l'opération est une réussie
	public void operationReussite ();

	// Recoit un message comme quoi l'opération a échoué
	public void operationEchec ();

	// Recoit un message comme quoi l'opération a été arreté
	public void operationArret ();
}
