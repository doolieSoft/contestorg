package org.contestorg.views;


import java.awt.BorderLayout;

import javax.swing.JPasswordField;

import org.contestorg.infos.Parametre;

@SuppressWarnings("serial")
public class JCParametreMotDePasse extends JCParametreAbstract
{
	
	// Entrée associée au composant
	private JPasswordField jpf_valeur = new JPasswordField();

	// Constructeur
	public JCParametreMotDePasse(Parametre parametre) {
		// Appel du constructeur parent
		super(parametre);
		
		// Ajouter l'entée dans le panel
		this.setLayout(new BorderLayout());
		this.add(this.jpf_valeur,BorderLayout.CENTER);
	}

	// Implémentation de getValeur
	@Override
	public String getValeur () {
		return new String(this.jpf_valeur.getPassword()).trim();
	}
	
	// Implémentation de setValeur
	@Override
	public void setValeur (String valeur) {
		this.jpf_valeur.setText(valeur);
	}
	
	// Implémentation de getError
	@Override
	public String getError () {
		// Vérifier si la valeur n'est pas vide
		if(!this.parametre.isOptionnel() && this.getValeur().trim().isEmpty()) {
			return "Le paramètre \""+this.parametre.getNom()+"\" est obligatoire.";
		}
		return null;
	}

	// Implémentation de link
	@Override
	public void link (JCParametreAbstract[] composants) {
	}
	
}
