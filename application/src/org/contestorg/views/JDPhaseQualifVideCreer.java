package org.contestorg.views;

import java.awt.Window;

import org.contestorg.common.Triple;
import org.contestorg.infos.InfosModelPhaseQualificative;
import org.contestorg.interfaces.ICollector;

/**
 * Boîte de dialogue de création d'une phase qualificative vide
 */
@SuppressWarnings("serial")
public class JDPhaseQualifVideCreer extends JDPattern
{
	/** Collecteur des informations de la phase qualificative */
	private ICollector<Triple<String,String,InfosModelPhaseQualificative>> collector;
	
	// Choix de la catégorie et de la poule

	/** Panel des catégories et poules */
	protected JPCategoriePoule jp_categoriePoule = new JPCategoriePoule();
	
	/**
	 * Constructeur
	 * @param w_parent fenêtre parent
	 * @param collector collecteur des informations de la phase qualificative
	 * @param nomCategorie nom de la catégorie de destination
	 * @param nomPoule nom de la poule de destination
	 */
	public JDPhaseQualifVideCreer(Window w_parent, ICollector<Triple<String,String,InfosModelPhaseQualificative>> collector, String nomCategorie) {
		// Appeller le constructeur du parent
		super(w_parent, "Ajouter une phase qualificative");
		
		// Retenir le collector
		this.collector = collector;
		
		// Catégorie et poule
		this.jp_contenu.add(this.jp_categoriePoule);
		this.jp_categoriePoule.setNomCategorie(nomCategorie);
		
		// Pack
		this.pack();
	}

	/**
	 * @see JDPhaseQualifVideCreer#ok()
	 */
	@Override
	protected void ok () {
		// Transférer les informations au collector
		this.collector.collect(new Triple<String, String, InfosModelPhaseQualificative>(this.jp_categoriePoule.getNomCategorie(),this.jp_categoriePoule.getNomPoule(), new InfosModelPhaseQualificative()));
	}

	/**
	 * @see JDPhaseQualifVideCreer#quit()
	 */
	@Override
	protected void quit () {
		// Annuler
		this.collector.cancel();
	}
}
