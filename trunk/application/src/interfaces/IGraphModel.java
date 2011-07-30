package interfaces;

/**
 * Interface à implémenter si une classe souhaite etre considérée comme un modèle de graphe pyramidal
 * @param L classe de l'objet associé au graphe
 * @param M classe des objets associés aux cellules du graphe pyramidal
 */
public interface IGraphModel<L,M>
{
	// Récupérer l'objet associé au graphe
	public L getObject();
	
	// Récupérer une cellule
	public ICelluleModel<L,M> getCellule(int index);
	
	// Récupérer l'index d'une cellule
	public int indexOf(ICelluleModel<L,M> cellule);
	
	// Récupérer le nombre de cellules à la base du graphe pyramidal
	public int size();
	
	// Ajouter/Retirer un listener
	public void addListener(IGraphModelListener listener);
	public void removeListener(IGraphModelListener listener);
	
	// Fermer le modèle
	public void close();
}
