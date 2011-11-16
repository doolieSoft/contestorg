package interfaces;

/**
 * Interface à implémenter si une classe souhaite etre tenue au courant du demarrage et de l'arret d'une opération
 */
public interface IStartStopListener
{
	// Recoit une notification comme quoi l'opération est démarrée
	public void start();
	
	// Recoit une notification comme quoi l'opération est terminée
	public void stop();
}
