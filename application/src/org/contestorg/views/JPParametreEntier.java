package org.contestorg.views;

import java.awt.BorderLayout;

import javax.swing.JTextField;

import org.contestorg.infos.InfosThemeParametre;

/**
 * Panel d'un paramètre de thème d'exportation/diffusion de type "Entier"
 */
@SuppressWarnings("serial")
public class JPParametreEntier extends JPParametreAbstract
{
	/** Nombre entier */
	private JTextField jtf_valeur = new JTextField();

	/**
	 * Constructeur
	 * @param parametre paramètre
	 */
	public JPParametreEntier(InfosThemeParametre parametre) {
		// Appel du constructeur parent
		super(parametre);
		
		// Ajouter l'entée dans le panel
		this.setLayout(new BorderLayout());
		this.add(this.jtf_valeur,BorderLayout.CENTER);
	}

	/**
	 * @see JPParametreAbstract#getValeur()
	 */
	@Override
	public String getValeur () {
		return this.jtf_valeur.getText();
	}
	
	/**
	 * @see JPParametreAbstract#setValeur(String)
	 */
	@Override
	public void setValeur (String valeur) {
		this.jtf_valeur.setText(valeur);
	}
	
	/**
	 * @see JPParametreAbstract#getError()
	 */
	@Override
	public String getError () {
		// Vérifier si la valeur n'est pas vide
		if(!this.parametre.isOptionnel() && this.getValeur().isEmpty()) {
			return "Le paramètre \""+this.parametre.getNom()+"\" est obligatoire.";
		}
		
		try {
			// Essayer de parser la valeur
			Integer.parseInt(this.getValeur());
			
			// Le parse a bien fonctionné
			return null;
		} catch(NumberFormatException e) {
			// Le parse n'a pas bien fonctionné
			return "Le paramètre \""+this.parametre.getNom()+"\" doit être un nombre entier.";
		}
	}

	/**
	 * @see JPParametreAbstract#link(JPParametreAbstract[])
	 */
	@Override
	public void link (JPParametreAbstract[] panels) {
	}
	
}
