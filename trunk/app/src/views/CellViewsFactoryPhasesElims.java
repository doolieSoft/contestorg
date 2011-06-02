package views;

import java.awt.Window;

import org.jgraph.graph.CellView;
import org.jgraph.graph.DefaultCellViewFactory;
import org.jgraph.graph.GraphModel;



@SuppressWarnings("serial")
public class CellViewsFactoryPhasesElims extends DefaultCellViewFactory {
	
		
		// Fenetre parant
		private Window w_parent;
		
		// Graphe
		private GraphPhasesElims graphe;

		// Constructeur
		public CellViewsFactoryPhasesElims(Window w_parent,GraphPhasesElims graphe) {
			// Retenir la fenetre parent et le graphe
			this.w_parent = w_parent;
			this.graphe = graphe;
		}
		
		// Redéfinition de la méthode createView
		public CellView createView(GraphModel model, Object cell) {
			// S'il s'agit d'un port
			if (model.isPort(cell)) {
				return new GraphPortViewPhasesElims(cell);
			}
			
			// S'il s'agit d'un lien
			if (model.isEdge(cell)) {
				return new GraphEdgeViewPhasesElims(cell);
			}

			// S'il s'agit d'une cellule
			return new GraphVertexViewPhasesElims(cell,this.w_parent,this.graphe);
		}
}
