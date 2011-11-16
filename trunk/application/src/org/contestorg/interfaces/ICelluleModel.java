package org.contestorg.interfaces;

/**
 * Interface à implémenter si une classe souhaite etre considérée comme un modèle de cellule de graphe pyramidal
 * @param L classe de l'objet associé au graphe
 * @param M classe des objets associés aux cellules du graphe pyramidal
 */
public interface ICelluleModel<L,M>
{
	// Récupère les données associées à la cellule
	public M getObject();
	
	// Savoir si la cellule est éditable
	public boolean isEditable();
	
	// Récupérer le graphe
	public IGraphModel<L,M> getGraphe();
	
	// Ajouter/Retirer un listener
	public void addListener(ICelluleModelListener listener);
	public void removeListener(ICelluleModelListener listener);
	
	// Fermer le modèle
	public void close();
}
