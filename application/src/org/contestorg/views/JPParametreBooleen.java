package org.contestorg.views;

import java.awt.BorderLayout;

import javax.swing.JComboBox;

import org.contestorg.infos.InfosThemeParametre;

/**
 * Panel d'un paramètre de thème d'exportation/diffusion de type "Booléen"
 */
@SuppressWarnings("serial")
public class JPParametreBooleen extends JPParametreAbstract
{
	/** Choix */
	private JComboBox jcb_choix = new JComboBox();
	
	/**
	 * Constructeur
	 * @param parametre paramètre
	 */
	public JPParametreBooleen(InfosThemeParametre parametre) {
		// Appel du constructeur parent
		super(parametre);
		
		// Ajouter la liste dans le panel
		this.setLayout(new BorderLayout());
		this.add(this.jcb_choix);
		
		// Remplir la liste
		this.jcb_choix.addItem("Veuillez faire votre choix");
		this.jcb_choix.addItem("Oui");
		this.jcb_choix.addItem("Non");
	}
	
	/**
	 * @see JPParametreAbstract#getValeur()
	 */
	@Override
	public String getValeur () {
		switch(this.jcb_choix.getSelectedIndex()) {
			case 1 : return "1";
			case 2 : return "0";
		}
		return null;
	}
	
	/**
	 * @see JPParametreAbstract#setValeur(String)
	 */
	@Override
	public void setValeur (String valeur) {
		this.jcb_choix.setSelectedIndex(valeur == null ? 0 : (valeur.equals("1") ? 1 : 2));
	}
	
	/**
	 * @see JPParametreAbstract#getError()
	 */
	@Override
	public String getError () {
		return !this.parametre.isOptionnel() && this.jcb_choix.getSelectedIndex() == 0 ? "Le paramètre \""+this.parametre.getNom()+"\" est obligatoire." : null;
	}
	
	/**
	 * @see JPParametreAbstract#link(JPParametreAbstract[])
	 */
	@Override
	public void link (JPParametreAbstract[] panels) {
	}
	
}
