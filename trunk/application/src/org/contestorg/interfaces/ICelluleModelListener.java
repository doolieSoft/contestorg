package interfaces;

/**
 * Interface à implémenter si une classe souhaite etre tenu au courant des opérations sur un modèle de cellule de graphe pyramidal
 */
public interface ICelluleModelListener
{
	// Recoit une notification de modification de la cellule
	public void updateCellule();
}
