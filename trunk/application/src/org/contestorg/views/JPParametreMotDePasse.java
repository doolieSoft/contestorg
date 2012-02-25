package org.contestorg.views;

import java.awt.BorderLayout;

import javax.swing.JPasswordField;

import org.contestorg.infos.Parametre;

/**
 * Panel d'un paramètre de thème d'exportation/diffusion de type "Mot de passe"
 */
@SuppressWarnings("serial")
public class JPParametreMotDePasse extends JPParametreAbstract
{
	
	/** Mot de passe */
	private JPasswordField jpf_valeur = new JPasswordField();

	/**
	 * Constructeur
	 * @param parametre paramètre
	 */
	public JPParametreMotDePasse(Parametre parametre) {
		// Appel du constructeur parent
		super(parametre);
		
		// Ajouter l'entée dans le panel
		this.setLayout(new BorderLayout());
		this.add(this.jpf_valeur,BorderLayout.CENTER);
	}

	/**
	 * @see JPParametreAbstract#getValeur()
	 */
	@Override
	public String getValeur () {
		return new String(this.jpf_valeur.getPassword()).trim();
	}
	
	/**
	 * @see JPParametreAbstract#setValeur(String)
	 */
	@Override
	public void setValeur (String valeur) {
		this.jpf_valeur.setText(valeur);
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
