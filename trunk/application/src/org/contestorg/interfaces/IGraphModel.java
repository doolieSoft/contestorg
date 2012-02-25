package org.contestorg.interfaces;

/**
 * Interface à implémenter si une classe souhaite être considérée comme un modèle de données pour graphe pyramidal
 * @param <L> classe de l'objet associé au graphe pyramidal
 * @param <M> classe des objets associés aux cellules du graphe pyramidal
 */
public interface IGraphModel<L,M>
{
	/**
	 * Récupérer l'objet associé au graphe pyramidal
	 * @return objet associé au graphe pyramidal
	 */
	public L getObject();
	
	/**
	 * Récupérer le modèle de données d'une cellule
	 * @param index indice de la cellule
	 * @return cellule
	 */
	public ICelluleModel<L,M> getCellule(int index);
	
	/**
	 * Récupérer l'indice d'une cellule
	 * @param cellule cellule
	 * @return indice
	 */
	public int indexOf(ICelluleModel<L,M> cellule);
	
	/**
	 * Récupérer le nombre de cellules à la base du graphe pyramidal
	 * @return nombre de cellules à la base du graphe pyramidal
	 */
	public int size();
	
	/**
	 * Ajouter un listener
	 * @param listener listener à ajouter
	 */
	public void addListener(IGraphModelListener listener);
	
	/**
	 * Retirer un listener
	 * @param listener listener à retirer
	 */
	public void removeListener(IGraphModelListener listener);
	
	/**
	 * Fermer le modèle
	 */
	public void close();
}
