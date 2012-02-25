package org.contestorg.views;

import java.awt.Window;

import org.jgraph.graph.CellView;
import org.jgraph.graph.DefaultCellViewFactory;
import org.jgraph.graph.GraphModel;

/**
 * Fabrique d'éléments du graphe des phases éliminatoires
 */
@SuppressWarnings("serial")
public class CellViewsFactoryPhasesElims extends DefaultCellViewFactory
{	
		
		/** Fenêtre parent */
		private Window w_parent;
		
		/** Graphe des phases éliminatoires */
		private JPGraphPhasesElims graphe;

		/**
		 * Constructeur
		 * @param w_parent fenêtre parent
		 * @param graphe graphe des phases éliminatoires
		 */
		public CellViewsFactoryPhasesElims(Window w_parent,JPGraphPhasesElims graphe) {
			// Retenir la fenêtre parent et le graphe
			this.w_parent = w_parent;
			this.graphe = graphe;
		}
		
		/**
		 * @see DefaultCellViewFactory#createView(GraphModel, Object)
		 */
		@Override
		public CellView createView(GraphModel graph, Object cell) {
			// S'il s'agit d'un port
			if (graph.isPort(cell)) {
				return new GraphPortViewPhasesElims(cell);
			}
			
			// S'il s'agit d'un lien
			if (graph.isEdge(cell)) {
				return new GraphEdgeViewPhasesElims(cell);
			}

			// S'il s'agit d'une cellule
			return new GraphVertexViewPhasesElims(cell,this.w_parent,this.graphe);
		}
}
