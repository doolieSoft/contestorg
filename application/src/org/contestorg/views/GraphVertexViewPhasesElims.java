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

import org.contestorg.controllers.ContestOrg;
import org.contestorg.infos.InfosModelCategorie;
import org.contestorg.infos.InfosModelConcours;
import org.contestorg.infos.InfosModelParticipant;
import org.contestorg.interfaces.ICelluleModel;
import org.contestorg.interfaces.ICelluleModelListener;
import org.jgraph.JGraph;
import org.jgraph.graph.AbstractCellView;
import org.jgraph.graph.GraphCellEditor;
import org.jgraph.graph.VertexView;

/**
 * Cellule du graphe des phases éliminatoires 
 */
@SuppressWarnings("serial")
public class GraphVertexViewPhasesElims extends VertexView implements ICelluleModelListener {
	
	/** Modèle de données associé à la cellule */
	private ICelluleModel<InfosModelCategorie,InfosModelParticipant> model;

	/** Fenêtre parent */
	private Window w_parent;
	
	/** Label */
	private JLabel jl_label;
	
	/** Panel du graphe */
	private JPGraphPhasesElims jp_graph;

	/**
	 * Constructeur
	 * @param cell modèle de données associé à la cellule
	 * @param w_parent fenêtre parent
	 * @param jp_graph panel du graphe
	 */
	public GraphVertexViewPhasesElims(Object cell, Window w_parent, JPGraphPhasesElims jp_graph) {
		// Appeller le constructeur du parent
		super(cell);
		
		// Retenir la fenêtre parent et le panel du graphe
		this.w_parent = w_parent;
		this.jp_graph = jp_graph;
		
		// Récupérer/Retenir/Ecouter le modèle associé à la cellule
		this.model = this.jp_graph.getModel().getCellule(Integer.parseInt(cell.toString()));
		this.model.addListener(this);
		
		// Créer le label
		this.jl_label = (JLabel) GraphVertexViewPhasesElims.getRenderComponent(this.model);
	}
	
	/**
	 * @see AbstractCellView#getEditor()
	 */
	public GraphCellEditor getEditor() {
		// Vérifier si la cellule est éditable
		if(!this.model.isEditable()) {
			return new GraphVertexEditorNull(this.model.getGraph().indexOf(this.model), this.jp_graph.getLargeurMaxCellule(), this.jl_label.getPreferredSize().height);
		}

		// Vérifier s'il s'agit d'une cellule associée à un participant ou à un match
		if(this.model.getGraph().indexOf(this.model) < this.model.getGraph().size()) {
			// Retourner l'editor
			return new GraphVertexEditorPhasesElimsParticipant(this.model, this.jp_graph.getLargeurMaxCellule(), this.jl_label.getPreferredSize().height);
		} else {
			// Vérifier si les résultats sont éditables
			boolean resultatsEditables = !this.jp_graph.isGrandeFinale() || ContestOrg.get().getCtrlPhasesEliminatoires().isMatchResultatsEditables(this.model.getGraph().getObject().getNom(), this.model.getGraph().indexOf(this.model)-this.model.getGraph().size());

			// Retourner l'editor
			return new GraphVertexEditorPhasesElimsMatch(this.model, this.w_parent, this.jp_graph.getLargeurMaxCellule(), this.jl_label.getPreferredSize().height, resultatsEditables, !this.jp_graph.isGrandeFinale());
		}
	}
	
	/**
	 * @see AbstractCellView#getBounds()
	 */
	public Rectangle2D getBounds() {
		// Retourner les dimensions du label
		return new Rectangle2D.Double(super.getBounds().getX(), super.getBounds().getY(), this.jp_graph.getLargeurMaxCellule(), this.jl_label.getPreferredSize().getHeight());
	}
	
	/**
	 * @see AbstractCellView#getRendererComponent(JGraph, boolean, boolean, boolean)
	 */
	public Component getRendererComponent(JGraph graph, boolean selected, boolean focus, boolean preview) {
		// Rafraichir le label
		GraphVertexViewPhasesElims.refreshRenderComponent(this.jl_label, this.model);
		
		// Retourner le label
		return this.jl_label;
	}
	
	/**
	 * Créer le composant associé à la cellule
	 * @param model modèle de données associé à la cellule
	 * @return composant associé à la cellule
	 */
	public static JComponent getRenderComponent(ICelluleModel<InfosModelCategorie,InfosModelParticipant> model) {
		// Créer le label
		JLabel label = new JLabel("",new ImageIcon(ContestOrg.get().getCtrlParticipants().getTypeParticipants() == InfosModelConcours.PARTICIPANTS_EQUIPES ? "img/farm/32x32/group.png" : "img/farm/32x32/user_green.png"),SwingConstants.LEFT);
		label.setOpaque(true);
		
		// Raraichir le label
		GraphVertexViewPhasesElims.refreshRenderComponent(label, model);
		
		// Retourner le label
		return label;
	}
	
	/**
	 * Rafraichir le composant associé à la cellule
	 * @param label label associé à la cellule
	 * @param model modèle de données associé à la cellule
	 */
	private static void refreshRenderComponent(JLabel label,ICelluleModel<InfosModelCategorie,InfosModelParticipant> model) {
		// Mettre à jour le label
		label.setText(model.getObject() == null ? "   ...   " : model.getObject().getNom());
		if(model.getObject() != null && model.getGraph().indexOf(model) < model.getGraph().size()) {
			int nbParticipantsSimilaires = 0;
			for (int i=0;i<model.getGraph().size();i++) {
				if (model.getGraph().getCellule(i).getObject() != null && model.getGraph().getCellule(i).getObject().getNom().equals(model.getObject().getNom())) {
					nbParticipantsSimilaires++;
				}
			}
			if(nbParticipantsSimilaires > 1) {
				label.setBackground(new Color(250, 90, 90));
				label.setBorder(new LineBorder(new Color(250, 90, 90), 5));
			} else {
				label.setBackground(new Color(119,171,243));
				label.setBorder(new LineBorder(new Color(119,171,243), 5));
			}
		} else {
			label.setBackground(new Color(119,171,243));
			label.setBorder(new LineBorder(new Color(119,171,243), 5));
		}
	}
	
	/**
	 * @see ICelluleModelListener#updateCellule()
	 */
	@Override
	public void updateCellule () {
		// Rafraichir le label
		GraphVertexViewPhasesElims.refreshRenderComponent(this.jl_label, this.model);
		
		// Prévenir le graphe
		this.jp_graph.updateCellule(this);
	}
	
	/**
	 * Fermer la cellule
	 */
	public void close() {
		// Ne plus écouter le modèle associée à la cellule
		this.model.removeListener(this);
	}
}
