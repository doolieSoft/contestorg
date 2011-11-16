package org.contestorg.views;


import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.contestorg.common.Pair;
import org.contestorg.controlers.ContestOrg;
import org.contestorg.infos.InfosModelCategorie;
import org.contestorg.infos.InfosModelPoule;



@SuppressWarnings("serial")
public class JPCategoriePoule extends JPanel implements ItemListener
{
	// Entrées
	protected JComboBox jcb_categorie = new JComboBox();
	protected JComboBox jcb_poule = new JComboBox();
	
	// Constructeur
	public JPCategoriePoule() {
		// Configurer le panel
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		// Récupérer les catégories et leurs poules
		ArrayList<Pair<InfosModelCategorie, ArrayList<InfosModelPoule>>> categoriesPoules = ContestOrg.get().getCtrlEquipes().getListeCategoriesPoules();
		
		// Récupérer le nombre de catégories et le nombre de poules
		int nbCategories = 0; int nbPoules = 0;
		for(int i=0;i<categoriesPoules.size();i++) {
			if(categoriesPoules.get(i).getSecond().size() != 0) {
				this.jcb_categorie.addItem(categoriesPoules.get(i).getFirst().getNom());
				ArrayList<InfosModelPoule> poules = categoriesPoules.get(i).getSecond();
				if(nbCategories == 0) {
					for(int j=0;j<poules.size();j++) {
						this.jcb_poule.addItem(poules.get(j).getNom());
					}
				}
				nbCategories++;
				nbPoules += poules.size();
			}
		}
		
		// Remplir le panel
		if(nbCategories > 1) {
			this.add(ViewHelper.title("Catégorie et poule", ViewHelper.H1));
			JLabel[] jls_categorie = { new JLabel("Catégorie : "), new JLabel("Poule : ") };
			JComponent[] jcs_categorie = { this.jcb_categorie, this.jcb_poule };
			this.add(ViewHelper.inputs(jls_categorie, jcs_categorie));
		} else  if(nbPoules > 1) {
			this.add(ViewHelper.title("Poule", ViewHelper.H1));
			JLabel[] jls_poule = { new JLabel("Poule : ") };
			JComponent[] jcs_poule = { this.jcb_poule };
			this.add(ViewHelper.inputs(jls_poule, jcs_poule));
		}
		
		// Ecouter la liste des catégories
		this.jcb_categorie.addItemListener(this);
	}
	
	// Récupérer la catégorie/poule séléctionnée
	public String getCategorie() {
		return (String)this.jcb_categorie.getSelectedItem();
	}
	public String getPoule() {
		return (String)this.jcb_poule.getSelectedItem();
	}
	
	// Définir la catégorie/poule séléctionnée
	public void setCategorie(String nomCategorie) {
		this.jcb_categorie.setSelectedItem(nomCategorie);
	}
	public void setPoule(String nomPoule) {
		this.jcb_poule.setSelectedItem(nomPoule);
	}

	// Implémentation de ItemListener
	@Override
	public void itemStateChanged (ItemEvent event) {
		// Vider la liste des poules
		this.jcb_poule.removeAllItems();
		
		// Récupérer le nom de la catégorie séléctionnée
		String nomCategorie = (String)event.getItem();
		
		// Remplir la liste des poules avec les poules de la catégorie
		for(InfosModelPoule poule : ContestOrg.get().getCtrlEquipes().getListePoules(nomCategorie)) {
			this.jcb_poule.addItem(poule.getNom());
		}
	}
}
