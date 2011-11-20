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

import org.contestorg.controlers.ContestOrg;
import org.contestorg.infos.InfosModelCategorie;
import org.contestorg.infos.InfosModelParticipant;
import org.contestorg.interfaces.ICelluleModel;
import org.jgraph.JGraph;
import org.jgraph.graph.GraphCellEditor;


@SuppressWarnings("serial")
public class GraphVertexEditorPhasesElimsParticipant extends AbstractCellEditor implements GraphCellEditor
{

	// Modèle associé à l'éditeur
	private ICelluleModel<InfosModelCategorie, InfosModelParticipant> model;
	
	// Panel d'édition et ComboBox associée
	private JPanel jp_edition;
	private JComboBox jcb_edition;
	
	// Constructeur
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
		
		ArrayList<String> participants = ContestOrg.get().getCtrlPhasesEliminatoires().getListeParticipants(this.model.getGraphe().getObject().getNom());
		
		this.jcb_edition = new JComboBox(participants.toArray(new String[participants.size()]));
		this.jp_edition.add(this.jcb_edition,contrainte_liste);
		
		if(participants.contains(this.model.getObject().getNom())) {
			this.jcb_edition.setSelectedIndex(participants.indexOf(this.model.getObject().getNom()));
		} else {
			this.jcb_edition.addItem(this.model.getObject().getNom());
			this.jcb_edition.setSelectedIndex(this.jcb_edition.getItemCount()-1);
		}
	}
	
	// Implémentation de getCellEditor
	@Override
	public Object getCellEditorValue () {
		// Retourner le numéro de cellule
		return this.model.getGraphe().indexOf(this.model);
	}

	// Implémentation de getGraphCellEditorComponent
	@Override
	public Component getGraphCellEditorComponent (JGraph graph, Object value, boolean isSelected) {	
		// Arrêter l'édition de la cellule au changement de participant
		this.jcb_edition.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged (ItemEvent event) {
				if(event.getStateChange() == ItemEvent.SELECTED) {
					// Demander le changement du participant de la cellule
					ContestOrg.get().getCtrlPhasesEliminatoires().setParticipantPhasesElims(model.getGraphe().getObject().getNom(), model.getGraphe().indexOf(model), (String)event.getItem());
					
					// Arreter l'édition
					stopCellEditing();
				}
			}
		});
		
		// Retourner le label d'édition
		return this.jp_edition;
	}
}
