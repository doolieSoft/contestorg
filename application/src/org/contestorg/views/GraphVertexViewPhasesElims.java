package org.contestorg.views;


import java.awt.Color;
import java.awt.Component;
import java.awt.Window;
import java.awt.geom.Rectangle2D;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import org.contestorg.controlers.ContestOrg;
import org.contestorg.infos.InfosModelCategorie;
import org.contestorg.infos.InfosModelConcours;
import org.contestorg.infos.InfosModelEquipe;
import org.contestorg.interfaces.ICelluleModel;
import org.contestorg.interfaces.ICelluleModelListener;
import org.jgraph.JGraph;
import org.jgraph.graph.GraphCellEditor;
import org.jgraph.graph.VertexView;



@SuppressWarnings("serial")
public class GraphVertexViewPhasesElims extends VertexView implements ICelluleModelListener {
	
	// Modèle associé à la cellule
	private ICelluleModel<InfosModelCategorie,InfosModelEquipe> model;

	// Parent
	private Window w_parent;
	
	// Label
	private JLabel label;
	
	// Largeur du label
	private GraphPhasesElims graphe;

	// Constructeur
	public GraphVertexViewPhasesElims(Object cell, Window w_parent, GraphPhasesElims graphe) {
		// Appeller le constructeur du parent
		super(cell);
		
		// Retenir la fenetre parent et le graphe
		this.w_parent = w_parent;
		this.graphe = graphe;
		
		// Récupérer/Retenir/Ecouter le modèle associé à la cellule
		this.model = this.graphe.getModel().getCellule(Integer.parseInt(cell.toString()));
		this.model.addListener(this);
		
		// Créer le label
		this.label = (JLabel) GraphVertexViewPhasesElims.getRenderComponent(this.model);
	}
	
	// Redéfinition de méthodes
	public GraphCellEditor getEditor() {
		// Vérifier si la cellule est éditable
		if(!this.model.isEditable()) {
			return new GraphVertexEditorNull(this.model.getGraphe().indexOf(this.model), this.graphe.getLargeurMaxCellule(), this.label.getPreferredSize().height);
		}

		// Vérifier s'il s'agit d'une cellule associée à une équipe ou à un match
		if(this.model.getGraphe().indexOf(this.model) < this.model.getGraphe().size()) {
			// Retourner l'editor
			return new GraphVertexEditorPhasesElimsEquipe(this.model, this.graphe.getLargeurMaxCellule(), this.label.getPreferredSize().height);
		} else {
			// Vérifier si les résultats sont éditables
			boolean resultatsEditables = !this.graphe.isGrandeFinale() || ContestOrg.get().getCtrlPhasesEliminatoires().isMatchPhasesElimsResultatsEditables(this.model.getGraphe().getObject().getNom(), this.model.getGraphe().indexOf(this.model)-this.model.getGraphe().size());

			// Retourner l'editor
			return new GraphVertexEditorPhasesElimsMatch(this.model, this.w_parent, this.graphe.getLargeurMaxCellule(), this.label.getPreferredSize().height, resultatsEditables, !this.graphe.isGrandeFinale());
		}
	}
	public Rectangle2D getBounds() {
		// Retourner les dimensions du label
		return new Rectangle2D.Double(super.getBounds().getX(), super.getBounds().getY(), this.graphe.getLargeurMaxCellule(), this.label.getPreferredSize().getHeight());
	}
	public Component getRendererComponent(JGraph graph, boolean selected, boolean focus, boolean preview) {
		// Rafraichir le label
		GraphVertexViewPhasesElims.refreshRenderComponent(this.label, this.model);
		
		// Retourner le label
		return this.label;
	}
	
	// Créer/Rafraichir le composant associé à la cellule
	public static JComponent getRenderComponent(ICelluleModel<InfosModelCategorie,InfosModelEquipe> model) {
		// Créer le label
		JLabel label = new JLabel("",new ImageIcon(ContestOrg.get().getTypeParticipants() == InfosModelConcours.PARTICIPANTS_EQUIPES ? "img/farm/32x32/group.png" : "img/farm/32x32/user_green.png"),SwingConstants.LEFT);
		label.setOpaque(true);
		label.setBackground(new Color(119,171,243));
		label.setBorder(new LineBorder(new Color(119,171,243), 5));
		
		// Raraichir le label
		GraphVertexViewPhasesElims.refreshRenderComponent(label, model);
		
		// Retourner le label
		return label;
	}
	private static void refreshRenderComponent(JLabel label,ICelluleModel<InfosModelCategorie,InfosModelEquipe> model) {
		// Mettre à jour le label
		label.setText(model.getObject() == null ? "   ...   " : model.getObject().getNom());
	}
	
	// Implémentation de ICelluleListener
	@Override
	public void updateCellule () {
		// Rafraichir le label
		GraphVertexViewPhasesElims.refreshRenderComponent(this.label, this.model);
		
		// Prévenir le graphe
		this.graphe.updateCellule(this);
	}
	
	// Fermer la cellule
	public void close() {
		// Ne plus écouter le modèle associée à la cellule
		this.model.removeListener(this);
	}
}
