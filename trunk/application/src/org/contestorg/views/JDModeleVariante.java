package org.contestorg.views;

import java.awt.Window;

import javax.swing.JComboBox;

import org.contestorg.infos.InfosModele;
import org.contestorg.infos.InfosModeleVariante;
import org.contestorg.interfaces.ICollector;

/**
 * Boîte de dialogue de séléction de variante de modèle
 */
@SuppressWarnings("serial")
public class JDModeleVariante extends JDPattern
{
	
	/** Liste des variantes */
	private JComboBox jcb_variantes;

	/** Collecteur de la variante */
	private ICollector<Integer> collector;
	
	/**
	 * Constructeur
	 * @param w_parent
	 * @param modele modèle
	 * @param collector collecteur de la variante
	 */
	public JDModeleVariante(Window w_parent, InfosModele modele, ICollector<Integer> collector) {
		// Appeler le constructeur parent
		super(w_parent, "Nouveau concours");
		
		// Retenir le collecteur
		this.collector = collector;
		
		// Ajouter la liste des variantes
		this.jp_contenu.add(ViewHelper.title("Variante",ViewHelper.H1));
		this.jcb_variantes = new JComboBox();
		this.jp_contenu.add(this.jcb_variantes);
		
		// Ajouter les variantes
		for(InfosModeleVariante variante : modele.getVariantes()) {
			this.jcb_variantes.addItem(variante.getNom());
		}
		
		// Pack
		this.pack();
	}

	/**
	 * @see JDPattern#ok()
	 */
	@Override
	protected void ok () {
		// Retourner les informations
		this.collector.collect(this.jcb_variantes.getSelectedIndex());
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
