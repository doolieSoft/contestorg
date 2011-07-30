package interfaces;

/**
 * Interface à implémenter si une classe souhaite etre considérée comme une opération
 */
public interface IOperation
{
	// Demarrer l'opération
	public void operationStart();
	
	// Arreter l'opération
	public void operationStop();
	
	// Ajouter un listener
	public void addListener(IOperationListener listener);
	
	// Supprimer un listener
	public void removeListener(IOperationListener listener);
}
