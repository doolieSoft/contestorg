package org.contestorg.views;

import java.awt.Window;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextField;

import org.contestorg.infos.InfosModelCategorie;
import org.contestorg.interfaces.ICollector;

/**
 * Boîte de dialogue de création/édition d'une catégorie
 */
@SuppressWarnings("serial")
public abstract class JDCategorieAbstract extends JDPattern
{
	/** Collecteur des informations de la catégorie */
	private ICollector<InfosModelCategorie> collector;
	
	// Entrées
	
	/** Entrée pour le nom */
	protected JTextField jtf_nom = new JTextField();
	
	/**
	 * Constructeur
	 * @param w_parent fenêtre parent
	 * @param titre titre de la boîte de dialogue
	 * @param collector collecteur des informations de la catégorie
	 */
	public JDCategorieAbstract(Window w_parent, String titre, ICollector<InfosModelCategorie> collector) {
		// Appeller le constructeur parent
		super(w_parent, titre);
		
		// Retenir le collector
		this.collector = collector;
		
		// Informations de la catégorie
		this.jp_contenu.add(ViewHelper.title("Informations de la catégorie : ", ViewHelper.H1));
		JLabel[] jls_categorie = {new JLabel("Nom : ")};
		JComponent[] jcs_categorie = {this.jtf_nom};
		this.jp_contenu.add(ViewHelper.inputs(jls_categorie, jcs_categorie));
		
		// Pack
		this.pack();
	}

	/**
	 * @see JDPattern#ok()
	 */
	@Override
	protected void ok () {
		// Récupérer les données
		String nom = this.jtf_nom.getText();
		
		// Vérifier les données
		boolean erreur = false;
		if(nom.isEmpty()) {
			ViewHelper.derror(this, "Le nom de la catégorie n'est pas précisé.");
			erreur = true;
		}

		// Créer et retourner les informations
		if(!erreur) {
			this.collector.collect(new InfosModelCategorie(nom));
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
