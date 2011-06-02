package interfaces;

/**
 * Interface à implémenter si une classe souhaite etre tenue au courant d'une progression
 */
public interface IProgressionListener
{

	// Recoit l'ancement de la génération
	public void progressionAvancement (double progression);

	// Recoit l'étape de la génération
	public void progressionMessage (String message);

	// Recoit l'information comme quoi la génération est terminée
	public void progressionFin ();
}
