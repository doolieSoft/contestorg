package org.contestorg.views;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Window;
import java.util.ArrayList;

import javax.swing.AbstractCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;

import org.contestorg.common.Pair;
import org.contestorg.common.Quadruple;
import org.contestorg.common.TrackableList;
import org.contestorg.common.Triple;
import org.contestorg.controllers.ContestOrg;
import org.contestorg.infos.InfosModelCategorie;
import org.contestorg.infos.InfosModelMatchPhasesElims;
import org.contestorg.infos.InfosModelObjectifRemporte;
import org.contestorg.infos.InfosModelParticipant;
import org.contestorg.infos.InfosModelParticipation;
import org.contestorg.interfaces.ICelluleModel;
import org.jgraph.JGraph;
import org.jgraph.graph.GraphCellEditor;

/**
 * Editeur de cellule du graphe des phases éliminatoires pour une cellule de match 
 */
@SuppressWarnings("serial")
public class GraphVertexEditorPhasesElimsMatch extends AbstractCellEditor implements GraphCellEditor {

	/** Modèle associé à la cellule */
	private ICelluleModel<InfosModelCategorie, InfosModelParticipant> model;

	/** Fenêtre parent */
	private Window w_parent;
	
	/** Label d'édition */
	private JLabel jl_edition;
	
	/** Résultats éditables ? */
	private boolean resultatsEditable;
	
	/** Petite finale ? */
	private boolean petiteFinale;

	/**
	 * Constructeur
	 * @param model modèle associé à la cellule
	 * @param w_parent fenêtre parent
	 * @param width largeur de l'éditeur
	 * @param height hauteur de l'éditeur
	 * @param resultatsEditable résultats éditable ?
	 * @param petiteFinale petite finale ?
	 */
	public GraphVertexEditorPhasesElimsMatch(ICelluleModel<InfosModelCategorie, InfosModelParticipant> model, Window w_parent, int width, int height, boolean resultatsEditable, boolean petiteFinale) {
		// Retenir le modèle associé à l'éditeur
		this.model = model;

		// Retenir la fenêtre parent
		this.w_parent = w_parent;
		
		// Retenir si les résultats sont éditable et s'il s'agit de la petiteFinale
		this.resultatsEditable = resultatsEditable;
		this.petiteFinale = petiteFinale;
		
		// Créer le label d'édition
		this.jl_edition = new JLabel("Edition",new ImageIcon("img/farm/32x32/pencil.png"),SwingConstants.LEFT);
		this.jl_edition.setOpaque(true);
		this.jl_edition.setBackground(new Color(247,210,109));
		this.jl_edition.setBorder(new LineBorder(new Color(247,210,109), 5));
		this.jl_edition.setPreferredSize(new Dimension(width, height));
	}
	
	/**
	 * @see GraphCellEditor#getCellEditorValue()
	 */
	@Override
	public Object getCellEditorValue() {
		// Retourner le numéro de la cellule
		return this.model.getGraph().indexOf(this.model);
	}

	/**
	 * @see GraphCellEditor#getGraphCellEditorComponent(JGraph, Object, boolean)
	 */
	@Override
	public Component getGraphCellEditorComponent(JGraph graph, Object value, boolean isSelected) {
		// Demander à Swing de remettre à plus tard la création et l'affichage de la fenêtre d'édition
		SwingUtilities.invokeLater(new Edit());
			
		// Retourner le label d'édition 
		return this.jl_edition;
	}
	
	/**
	 * Classe permettant l'affichage de la fenêtre d'édition
	 */
	private class Edit implements Runnable {
		/**
		 * @see Runnable#run()
		 */
		@Override
		public void run() {
			// Récupérer le nom de la catégorie et le numéro du match
			String nomCategorie = model.getGraph().getObject().getNom();
			int numeroMatch = model.getGraph().indexOf(model)-model.getGraph().size();
			
			// Récupérer le collector du match
			CollectorAbstract<Quadruple<Pair<TrackableList<Pair<String, InfosModelObjectifRemporte>>, InfosModelParticipation>, Pair<TrackableList<Pair<String, InfosModelObjectifRemporte>>, InfosModelParticipation>, Pair<String, String>, InfosModelMatchPhasesElims>> collector = petiteFinale ? new CollectorMatchPetiteFinale(nomCategorie) : new CollectorMatchPhasesElims(nomCategorie, numeroMatch);
			
			// Récupérer les informations sur le match
			Quadruple<Triple<String, ArrayList<Pair<String, InfosModelObjectifRemporte>>, InfosModelParticipation>, Triple<String, ArrayList<Pair<String, InfosModelObjectifRemporte>>, InfosModelParticipation>, Pair<String, String>, InfosModelMatchPhasesElims> infos = petiteFinale ? ContestOrg.get().getCtrlPhasesEliminatoires().getInfosMatchPetiteFinale(nomCategorie) : ContestOrg.get().getCtrlPhasesEliminatoires().getInfosMatch(nomCategorie,numeroMatch);
			
			// Créer et afficher la fenêtre d'édition de la fenêtre
			JDialog jd_match = new JDMatchPhasesEliminatoires(w_parent, collector, infos, resultatsEditable);
			collector.setWindow(jd_match);
			jd_match.setVisible(true);
			
			// Arreter l'édition de la cellule
			stopCellEditing();
		}
	}

}
