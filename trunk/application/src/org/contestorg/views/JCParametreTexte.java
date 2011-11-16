package org.contestorg.views;

import java.awt.BorderLayout;

import javax.swing.JTextField;

import org.contestorg.infos.Parametre;


@SuppressWarnings("serial")
public class JCParametreTexte extends JCParametreAbstract
{
	
	// Entrée associée au composant
	private JTextField jtf_valeur = new JTextField();

	// Constructeur
	public JCParametreTexte(Parametre parametre) {
		// Appel du constructeur parent
		super(parametre);
		
		// Ajouter l'entée dans le panel
		this.setLayout(new BorderLayout());
		this.add(this.jtf_valeur,BorderLayout.CENTER);
	}

	// Implémentation de getValeur
	@Override
	public String getValeur () {
		return this.jtf_valeur.getText().trim();
	}
	
	// Implémentation de setValeur
	@Override
	public void setValeur (String valeur) {
		this.jtf_valeur.setText(valeur);
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
