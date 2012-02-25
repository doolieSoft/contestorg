package org.contestorg.views;

import org.jgraph.graph.EdgeView;

/**
 * Lien entre cellules du graphe des phases éliminatoires
 */
@SuppressWarnings("serial")
public class GraphEdgeViewPhasesElims extends EdgeView {

	/**
	 * Constructeur
	 * @param edge modèle de données associé au lien
	 */
	public GraphEdgeViewPhasesElims(Object edge) {
		// Appeller le constructeur du parent
		super(edge);
	}
	
}
