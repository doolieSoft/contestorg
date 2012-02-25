package org.contestorg.views;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

import javax.swing.AbstractCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import org.contestorg.controllers.ContestOrg;
import org.contestorg.infos.InfosModelCategorie;
import org.contestorg.infos.InfosModelParticipant;
import org.contestorg.interfaces.ICelluleModel;
import org.jgraph.JGraph;
import org.jgraph.graph.GraphCellEditor;

/**
 * Editeur de cellule du graphe des phases éliminatoires pour une cellule de participant 
 */
@SuppressWarnings("serial")
public class GraphVertexEditorPhasesElimsParticipant extends AbstractCellEditor implements GraphCellEditor
{

	/** Modèle associé à l'éditeur */
	private ICelluleModel<InfosModelCategorie, InfosModelParticipant> model;
	
	/** Panel d'édition */
	private JPanel jp_edition;
	
	/** Liste d'édition */
	private JComboBox<String> jcb_edition;
	
	/**
	 * Constructeur
	 * @param model modèle associé à l'éditeur
	 * @param width largeur du panel d'édition
	 * @param height hauteur du panel d'édition
	 */
	public GraphVertexEditorPhasesElimsParticipant(ICelluleModel<InfosModelCategorie, InfosModelParticipant> model, int width, int height) {
		// Retenir le modèle associé à l'éditeur 
		this.model = model;

		// Créer la contrainte pour le label
		GridBagConstraints contrainte_label = new GridBagConstraints();
		contrainte_label.gridx = 0;
		contrainte_label.weightx = 0;

		// Créer la contrainte pour la liste
		GridBagConstraints contrainte_liste = new GridBagConstraints();
		contrainte_liste.gridx = 1;
		contrainte_liste.weightx = 1;
		contrainte_liste.insets = new Insets(0, 2, 0, 0);
		contrainte_liste.fill = GridBagConstraints.HORIZONTAL;
		
		// Créer le pabel d'édition
		this.jp_edition = new JPanel(new GridBagLayout());
		this.jp_edition.setOpaque(true);
		this.jp_edition.setBackground(new Color(247,210,109));
		this.jp_edition.setBorder(new LineBorder(new Color(247,210,109), 5));
		this.jp_edition.setPreferredSize(new Dimension(width, height));
		
		JLabel jl_edition = new JLabel(new ImageIcon("img/farm/32x32/pencil.png"));
		this.jp_edition.add(jl_edition,contrainte_label);
		
		ArrayList<String> participants = ContestOrg.get().getCtrlPhasesEliminatoires().getListeParticipants(this.model.getGraph().getObject().getNom());
		
		this.jcb_edition = new JComboBox<String>(participants.toArray(new String[participants.size()]));
		this.jp_edition.add(this.jcb_edition,contrainte_liste);
		
		if(participants.contains(this.model.getObject().getNom())) {
			this.jcb_edition.setSelectedIndex(participants.indexOf(this.model.getObject().getNom()));
		} else {
			this.jcb_edition.addItem(this.model.getObject().getNom());
			this.jcb_edition.setSelectedIndex(this.jcb_edition.getItemCount()-1);
		}
	}
	
	/**
	 * @see GraphCellEditor#getCellEditorValue()
	 */
	@Override
	public Object getCellEditorValue () {
		// Retourner le numéro de cellule
		return this.model.getGraph().indexOf(this.model);
	}

	/**
	 * @see GraphCellEditor#getGraphCellEditorComponent(JGraph, Object, boolean)
	 */
	@Override
	public Component getGraphCellEditorComponent (JGraph graph, Object value, boolean isSelected) {	
		// Arrêter l'édition de la cellule au changement de participant
		this.jcb_edition.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged (ItemEvent event) {
				if(event.getStateChange() == ItemEvent.SELECTED) {
					// Demander le changement du participant de la cellule
					ContestOrg.get().getCtrlPhasesEliminatoires().setParticipant(model.getGraph().getObject().getNom(), model.getGraph().indexOf(model), (String)event.getItem());
					
					// Arrêter l'édition
					stopCellEditing();
				}
			}
		});
		
		// Retourner le label d'édition
		return this.jp_edition;
	}
}
