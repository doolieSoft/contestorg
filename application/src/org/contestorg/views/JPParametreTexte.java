package org.contestorg.views;

import java.awt.BorderLayout;

import javax.swing.JTextField;

import org.contestorg.infos.Parametre;

/**
 * Panel d'un paramètre de thème d'exportation/diffusion de type "Texte"
 */
@SuppressWarnings("serial")
public class JPParametreTexte extends JPParametreAbstract
{
	
	/** Texte */
	private JTextField jtf_valeur = new JTextField();

	/**
	 * Constructeur
	 * @param parametre paramètre
	 */
	public JPParametreTexte(Parametre parametre) {
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
		return this.jtf_valeur.getText().trim();
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
		if(!this.parametre.isOptionnel() && this.getValeur().trim().isEmpty()) {
			return "Le paramètre \""+this.parametre.getNom()+"\" est obligatoire.";
		}
		return null;
	}

	/**
	 * @see JPParametreAbstract#link(JPParametreAbstract[])
	 */
	@Override
	public void link (JPParametreAbstract[] panels) {
	}
	
}
