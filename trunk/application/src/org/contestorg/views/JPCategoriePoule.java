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
import org.contestorg.controllers.ContestOrg;
import org.contestorg.infos.InfosModelCategorie;
import org.contestorg.infos.InfosModelPoule;

/**
 * Panel de séléction d'une poule d'une catégorie
 */
@SuppressWarnings("serial")
public class JPCategoriePoule extends JPanel implements ItemListener
{
	/** Listeners */
	private ArrayList<ItemListener> listeners = new ArrayList<ItemListener>();

	/** Catégories et poules */
	private ArrayList<Pair<InfosModelCategorie, ArrayList<InfosModelPoule>>> categoriesPoules;
	
	// Entrées
	
	/** Catégorie */
	protected JComboBox<String> jcb_categorie = new JComboBox<String>();
	
	/** Poule */
	protected JComboBox<String> jcb_poule = new JComboBox<String>();
	
	/**
	 * Constructeur
	 */
	public JPCategoriePoule() {
		// Configurer le panel
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		// Récupérer les catégories et leurs poules
		this.categoriesPoules = ContestOrg.get().getCtrlParticipants().getListeCategoriesPoules();
		
		// Récupérer le nombre de catégories et le nombre de poules et remplir les listes
		int nbCategories = 0; int nbPoules = 0;
		for(int i=0;i<this.categoriesPoules.size();i++) {
			if(this.categoriesPoules.get(i).getSecond().size() != 0) {
				this.jcb_categorie.addItem(this.categoriesPoules.get(i).getFirst().getNom());
				ArrayList<InfosModelPoule> poules = this.categoriesPoules.get(i).getSecond();
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
	
	/**
	 * Récupérer le nom de la catégorie séléctionnée
	 * @return nom de la catégorie séléctionnée
	 */
	public String getNomCategorie() {
		return (String)this.jcb_categorie.getSelectedItem();
	}
	
	/**
	 * Récupérer le nom de la poule séléctionnée
	 * @return nom de la poule séléctionnée
	 */
	public String getNomPoule() {
		return (String)this.jcb_poule.getSelectedItem();
	}
	
	/**
	 * Définir le nom de la catégorie séléctionnée
	 * @param nomCategorie nom de la catégorie
	 */
	public void setNomCategorie(String nomCategorie) {
		if(nomCategorie != null) {
			this.jcb_categorie.setSelectedItem(nomCategorie);
		}
	}
	
	/**
	 * Définir la poule séléctionnée
	 * @param nomPoule nom de la poule
	 */
	public void setNomPoule(String nomPoule) {
		if(nomPoule != null) {
			this.jcb_poule.setSelectedItem(nomPoule);
		}
	}

	/**
	 * @see ItemListener#itemStateChanged(ItemEvent)
	 */
	@Override
	public void itemStateChanged (ItemEvent event) {
		// Supprimer les listeners
		for(ItemListener listener : this.listeners) {
			this.jcb_poule.removeItemListener(listener);
		}
		
		// Vider la liste des poules
		this.jcb_poule.removeAllItems();
		
		// Récupérer le nom de la catégorie séléctionnée
		String nomCategorie = (String)event.getItem();
		
		// Remplir la liste des poules avec les poules de la catégorie
		for(int i=0;i<this.categoriesPoules.size();i++) {
			if(this.categoriesPoules.get(i).getFirst().getNom().equals(nomCategorie)) {
				for(int j=0;j<this.categoriesPoules.get(i).getSecond().size();j++) {
					this.jcb_poule.addItem(this.categoriesPoules.get(i).getSecond().get(j).getNom());
				}
			}
		}
		
		// Propager l'événement
		for(ItemListener listener : this.listeners) {
			listener.itemStateChanged(event);
			this.jcb_poule.addItemListener(listener);
		}
	}
	
	/**
	 * @see JComboBox#addItemListener(ItemListener)
	 */
	public void addItemListener(ItemListener listener) {
		this.jcb_poule.addItemListener(listener);
		this.listeners.add(listener);
	}
}
