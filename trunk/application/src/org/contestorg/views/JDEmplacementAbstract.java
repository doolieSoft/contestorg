package org.contestorg.views;

import java.awt.Window;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.contestorg.infos.InfosModelEmplacement;
import org.contestorg.interfaces.ICollector;

/**
 * Boîte de dialogue de création/édition d'un emplacement
 */
@SuppressWarnings("serial")
public abstract class JDEmplacementAbstract extends JDPattern
{	
	/** Collecteur des informations de l'emplacement */
	private ICollector<InfosModelEmplacement> collector;
	
	// Entrées
	
	/** Nom */
	protected JTextField jtf_nom = new JTextField();
	
	/** Description */
	protected JTextArea jta_description = new JTextArea();

	/**
	 * Constructeur
	 * @param w_parent fenêtre parent
	 * @param titre titre de la boîte de dialogue
	 * @param collector collecteur des informations de l'emplacement
	 */
	public JDEmplacementAbstract(Window w_parent, String titre, ICollector<InfosModelEmplacement> collector) {
		// Appeller le constructeur du parent
		super(w_parent, titre);
		
		// Retenir le collector
		this.collector = collector;
		
		// Informations sur l'emplacement
		this.jp_contenu.add(ViewHelper.title("Informations sur l'emplacment", ViewHelper.H1));
		this.jta_description.setLineWrap(true);
		this.jta_description.setWrapStyleWord(true);
		this.jta_description.setRows(6);
		this.jta_description.setColumns(30);
		JLabel[] jls = {new JLabel("Nom : "),new JLabel("Description : ")};
		JComponent[] jcs = {this.jtf_nom,new JScrollPane(this.jta_description)};
		this.jp_contenu.add(ViewHelper.inputs(jls, jcs)); 
		
		// Pack
		this.pack();
	}

	/**
	 * @see JDPattern#ok()
	 */
	@Override
	protected void ok () {
		// Récupérer les données
		String nom = this.jtf_nom.getText().trim();
		String description = this.jta_description.getText().trim();
		
		// Envoyer les informations au collector
		if(!nom.isEmpty()) {
			collector.collect(new InfosModelEmplacement(nom, description));
		} else {
			// Message d'erreur
			ViewHelper.derror(this, "Le nom de l'emplacment est vide.");
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
