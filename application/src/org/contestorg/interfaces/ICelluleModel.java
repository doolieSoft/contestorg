package org.contestorg.interfaces;

/**
 * Interface à implémenter si une classe souhaite être considérée comme un modèle de cellule de graphe pyramidal
 * @param <L> classe de l'objet associé au graphe pyramidal
 * @param <M> classe des objets associés aux cellules du graphe pyramidal
 */
public interface ICelluleModel<L,M>
{
	/**
	 * Récupèrer les données associées à la cellule
	 * @return données associées à la cellule
	 */
	public M getObject();
	
	/**
	 * Savoir si la cellule est éditable
	 * @return cellule éditable ?
	 */
	public boolean isEditable();
	
	/**
	 * Récupérer le graphe
	 * @return graphe
	 */
	public IGraphModel<L,M> getGraph();
	
	/**
	 * Ajouter un listener
	 * @param listener listener à ajouter
	 */
	public void addListener(ICelluleModelListener listener);
	
	/**
	 * Retirer un listener
	 * @param listener listener à retirer
	 */
	public void removeListener(ICelluleModelListener listener);
	
	/**
	 * Fermer le modèle
	 */
	public void close();
}
