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
import org.contestorg.common.TrackableList;
import org.contestorg.common.Triple;
import org.contestorg.controlers.ContestOrg;
import org.contestorg.infos.InfosModelCategorie;
import org.contestorg.infos.InfosModelParticipant;
import org.contestorg.infos.InfosModelMatchPhasesElims;
import org.contestorg.infos.InfosModelParticipation;
import org.contestorg.infos.InfosModelParticipationObjectif;
import org.contestorg.interfaces.ICelluleModel;
import org.jgraph.JGraph;
import org.jgraph.graph.GraphCellEditor;



@SuppressWarnings("serial")
public class GraphVertexEditorPhasesElimsMatch extends AbstractCellEditor implements GraphCellEditor {

	// Modèle associé à l'éditeur
	private ICelluleModel<InfosModelCategorie, InfosModelParticipant> model;

	// fenêtre parent
	private Window w_parent;
	
	// Label d'édition
	private JLabel jl_edition;
	
	// Edition des résultats ?
	private boolean resultatsEditable;
	
	// Petite finale ?
	private boolean petiteFinale;

	// Constructeur
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
	
	// Implémentation de getCellEditorValue
	@Override
	public Object getCellEditorValue() {
		// Retourner le numéro de la cellule
		return this.model.getGraphe().indexOf(this.model);
	}

	// Implémentation de getGraphCellEditorComponent
	@Override
	public Component getGraphCellEditorComponent(JGraph graph, Object value, boolean isSelected) {
		// Demander à Swing de remettre à plus tard la création et l'affichage de la fenêtre d'édition
		SwingUtilities.invokeLater(new Edit());
			
		// Retourner le label d'édition 
		return this.jl_edition;
	}
	
	// Méthode permettant l'affichage de la fenêtre d'édition
	private class Edit implements Runnable {
		// Implémentation de run
		@Override
		public void run() {
			// Récupérer le nom de la catégorie et le numéro du match
			String nomCategorie = model.getGraphe().getObject().getNom();
			int numeroMatch = model.getGraphe().indexOf(model)-model.getGraphe().size();
			
			// Récupérer le collector du match
			CollectorAbstract<Triple<Pair<TrackableList<Pair<String, InfosModelParticipationObjectif>>, InfosModelParticipation>, Pair<TrackableList<Pair<String, InfosModelParticipationObjectif>>, InfosModelParticipation>, InfosModelMatchPhasesElims>> collector = petiteFinale ? new CollectorMatchPetiteFinale(nomCategorie) : new CollectorMatchPhasesElims(nomCategorie, numeroMatch);
			
			// Récupérer les informations sur le match
			Triple<Triple<String, ArrayList<Pair<String, InfosModelParticipationObjectif>>, InfosModelParticipation>, Triple<String, ArrayList<Pair<String, InfosModelParticipationObjectif>>, InfosModelParticipation>, InfosModelMatchPhasesElims> infos = petiteFinale ? ContestOrg.get().getCtrlPhasesEliminatoires().getInfosMatchPetiteFinale(nomCategorie) : ContestOrg.get().getCtrlPhasesEliminatoires().getInfosMatchPhasesElims(nomCategorie,numeroMatch);
			
			// Créer et afficher la fenêtre d'édition de la fenêtre
			JDialog jd_match = new JDMatchPhasesEliminatoires(w_parent, collector, infos, resultatsEditable);
			collector.setWindow(jd_match);
			jd_match.setVisible(true);
			
			// Arreter l'édition de la cellule
			stopCellEditing();
		}
		
	}

}
