package org.contestorg.views;

import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;

import org.contestorg.common.Triple;
import org.contestorg.controllers.ContestOrg;
import org.contestorg.interfaces.ICollector;

/**
 * Boîte de dialogue de séléction d'une catégorie, d'une poule et d'une phase qualificative
 */
@SuppressWarnings("serial")
public class JDCategoriePoulePhaseQualif extends JDPattern
{

	/** Collecteur */
	private ICollector<Triple<String, String, Integer>> collector;
	
	// Entrées

	/** Panel des catégories et poules */
	protected JPCategoriePoule jp_categoriePoule = new JPCategoriePoule();
	
	/** Phase qualificative */
	protected JComboBox<String> jcb_phaseQualif = new JComboBox<String>();

	/**
	 * Constructeur
	 * @param w_parent fenêtre parent
	 */
	public JDCategoriePoulePhaseQualif(Window w_parent, ICollector<Triple<String,String,Integer>> collector, String nomCategorie, String nomPoule) {
		// Appeller le constructeur parent
		super(w_parent, "Choix de la phase qualificative");
		
		// Retenir le collecteur
		this.collector = collector;
		
		// Catégorie et poule
		this.jp_contenu.add(this.jp_categoriePoule);
		this.jp_categoriePoule.setNomCategorie(nomCategorie);
		this.jp_categoriePoule.setNomPoule(nomPoule);

		// Ecoute les changements de catégorie et de poule
		this.jp_categoriePoule.addItemListener(new ItemListener() {
			/**
			 * @see ItemListener#itemStateChanged(ItemEvent)
			 */
			@Override
			public void itemStateChanged (ItemEvent event) {
				// Réinitialiser la liste des phases qualificatives
				jcb_phaseQualif.removeAllItems();
				
				// Ajouter la liste des phases qualificatives
				int nbPhasesQualifs = ContestOrg.get().getCtrlPhasesQualificatives().getNbPhasesQualifs(jp_categoriePoule.getNomCategorie(), jp_categoriePoule.getNomPoule());
				for(int i=1;i<=nbPhasesQualifs;i++) {
					jcb_phaseQualif.addItem("Phase qualificative "+i);
				}
			}
		});
		
		// Phase qualificative
		this.jp_contenu.add(ViewHelper.title("Phase qualificative", ViewHelper.H1));
		JLabel[] jls_phaseQualif = { new JLabel("Phase qualificative : ") };
		this.jcb_phaseQualif.setPreferredSize(new Dimension(160, (int)this.jcb_phaseQualif.getPreferredSize().getHeight()));
		JComponent[] jcs_phaseQualif = { this.jcb_phaseQualif };
		this.jp_contenu.add(ViewHelper.inputs(jls_phaseQualif, jcs_phaseQualif));
		
		// Ajouter la liste des phases qualificatives
		int nbPhasesQualifs = ContestOrg.get().getCtrlPhasesQualificatives().getNbPhasesQualifs(jp_categoriePoule.getNomCategorie(), jp_categoriePoule.getNomPoule());
		for(int i=1;i<=nbPhasesQualifs;i++) {
			jcb_phaseQualif.addItem("Phase qualificative "+i);
		}
		
		// Pack
		this.pack();
	}

	/**
	 * @see JDPattern#ok()
	 */
	@Override
	protected void ok () {
		// Vérifier si une phase qualificative a été séléctionnée
		if(this.jcb_phaseQualif.getItemCount() == 0) {
			ViewHelper.derror(this, "La phase qualificative est obligatoire.");
		} else {
			// Créer et retourner les informations
			this.collector.collect(new Triple<String, String, Integer>(this.jp_categoriePoule.getNomCategorie(), this.jp_categoriePoule.getNomPoule(), this.jcb_phaseQualif.getSelectedIndex()));
		}
	}

	/**
	 * @see JDPattern#quit()
	 */
	@Override
	protected void quit () {
		// Annuler
		this.collector.cancel();
	}
	
}
