package org.contestorg.views;

import java.awt.BorderLayout;

import javax.swing.JTextField;

import org.contestorg.infos.Parametre;


@SuppressWarnings("serial")
public class JCParametreEntier extends JCParametreAbstract
{
	
	// Entrée associée au composant
	private JTextField jtf_valeur = new JTextField();

	// Constructeur
	public JCParametreEntier(Parametre parametre) {
		// Appel du constructeur parent
		super(parametre);
		
		// Ajouter l'entée dans le panel
		this.setLayout(new BorderLayout());
		this.add(this.jtf_valeur,BorderLayout.CENTER);
	}

	// Implémentation de getValeur
	@Override
	public String getValeur () {
		return this.jtf_valeur.getText();
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

	// Implémentation de link
	@Override
	public void link (JCParametreAbstract[] composants) {
	}
	
}
