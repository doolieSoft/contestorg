package interfaces;

/**
 * Interface à implémenter si une classe souhaite etre tenu au courant des opérations sur un modèle de graphe pyramidal
 */
public interface IGraphModelListener
{
	// Recoit une notification de reconstruction du graphe pyramidal
	public void reloadGraphe();
	
}
