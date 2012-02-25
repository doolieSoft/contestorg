package org.contestorg.views;

import java.awt.Window;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextField;

import org.contestorg.infos.InfosModelPrix;
import org.contestorg.interfaces.ICollector;

/**
 * Boîte de dialogue de création/édition d'un prix
 */
@SuppressWarnings("serial")
public abstract class JDPrixAbstract extends JDPattern
{

	/** Collecteur des informations du prix */
	protected ICollector<InfosModelPrix> collector;
	
	// Entrées
	
	/** Nom */
	protected JTextField jtf_nom = new JTextField();

	/**
	 * Constructeur
	 * @param w_parent fenêtre parent
	 * @param titre titre la boîte de dialogue
	 * @param collector collecteur des informations du prix
	 */
	public JDPrixAbstract(Window w_parent, String titre, ICollector<InfosModelPrix> collector) {
		// Appeller le constructeur du parent
		super(w_parent, titre);

		// Retenir le collector
		this.collector = collector;
		
		// Informations du prix
		this.jp_contenu.add(ViewHelper.title("Informations du prix", ViewHelper.H1));
		JLabel[] jls_exportation = { new JLabel("Nom : ") };
		JComponent[] jcs_exportation = { this.jtf_nom };
		this.jp_contenu.add(ViewHelper.inputs(jls_exportation, jcs_exportation));
		
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
			ViewHelper.derror(this, "Le nom du prix n'est pas précisé.");
			erreur = true;
		}

		// Créer et retourner les informations
		if(!erreur) {
			this.collector.collect(new InfosModelPrix(nom));
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
