package org.contestorg.views;


import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.contestorg.common.Triple;
import org.contestorg.controlers.ContestOrg;
import org.contestorg.infos.InfosModelChemin;
import org.contestorg.infos.InfosModelExportation;
import org.contestorg.infos.InfosModelTheme;
import org.contestorg.interfaces.IOperation;



@SuppressWarnings("serial")
public class JDExportations extends JDPattern
{
	// Exportations
	ArrayList<Triple<InfosModelExportation, InfosModelChemin, InfosModelTheme>> exportations;
	
	// Boutons
	private ArrayList<JButton> jbs_lancer = new ArrayList<JButton>();
	
	// Constructeur
	public JDExportations(Window w_parent) {
		// Appeller le constructeur du parent
		super(w_parent, "Exporter");
		
		// Masquer le bouton annuler
		this.jb_annuler.setVisible(false);
		
		// Titre
		this.jp_contenu.add(ViewHelper.title("Exportation des informations du concours",ViewHelper.H1));
		
		// Retenir les exportations
		this.exportations = ContestOrg.get().getCtrlOut().getExportations();
		
		// Vérifier s'il y a des exportations
		if(exportations.size() > 0) {
			// Créer la contrainte pour le label
			GridBagConstraints contrainte_labels = new GridBagConstraints();
			contrainte_labels.gridx = 0;
			contrainte_labels.weightx = 1;
			contrainte_labels.insets = new Insets(0, 0, 5, 0);
			contrainte_labels.fill = GridBagConstraints.HORIZONTAL;

			// Créer la contrainte pour le bouton
			GridBagConstraints contrainte_lancer = new GridBagConstraints();
			contrainte_lancer.gridx = 1;
			contrainte_lancer.weightx = 0;
			contrainte_lancer.insets = new Insets(0, 0, 5, 0);

			// Ajouter la liste des exportations
			JPanel panel = new JPanel(new GridBagLayout());
			for(int i=0;i<this.exportations.size();i++) {
				// Ajouter le label
				panel.add(new JLabel(this.exportations.get(i).getFirst().getNom()),contrainte_labels);
				
				// Ajouter le bouton lancer
				this.jbs_lancer.add(new JButton("Lancer",new ImageIcon("img/farm/16x16/control_play_blue.png")));
				panel.add(this.jbs_lancer.get(i),contrainte_lancer);
				
				// Ecouter le bouton lancer
				this.jbs_lancer.get(i).addActionListener(this);
			}
			this.jp_contenu.add(panel);
		} else {
			// Pas encore de diffusions
			this.jp_contenu.add(Box.createVerticalStrut(5));
			this.jp_contenu.add(ViewHelper.pwarning("Il n'y a pas encore d'exportations disponibles."));
		}
		
		// Information
		this.jp_contenu.add(Box.createVerticalStrut(5));
		this.jp_contenu.add(ViewHelper.pinformation("Rendez-vous dans \"Configurer > Exportations et diffusions\" pour éditer la liste des exportations."));

		// Pack
		this.pack();
	}
	
	// Implémentation de ActionListener
	public void actionPerformed (ActionEvent event) {
		if(this.jbs_lancer.contains(event.getSource())) {
			// Récupérer l'exportation
			Triple<InfosModelExportation, InfosModelChemin, InfosModelTheme> exportation = this.exportations.get(this.jbs_lancer.indexOf(event.getSource()));
			
			// Récupérer l'opération
			IOperation operation = ContestOrg.get().getCtrlOut().getLancerExportationOperation(exportation.getFirst().getNom());
			
			// Créer la fenêtre associée à l'opération
			JDOperation jd_operation = new JDOperation(this, "Exporter", operation, true, true);
			
			operation.operationStart();		// Démarrer l'opération
			jd_operation.setVisible(true);	// Afficher la fenêtre
		} else {
			// Appeller le actionPerformed du parent
			super.actionPerformed(event);
		}
	}

	// Implémentation de ok
	@Override
	protected void ok () {
		// Masquer la fenêtre
		this.setVisible(false);
	}
	
	// Implémentation de quit
	@Override
	protected void quit () {
		// Masquer la fenêtre
		this.setVisible(false);
	}
	
}
