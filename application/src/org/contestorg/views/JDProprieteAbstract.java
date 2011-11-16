package org.contestorg.views;


import java.awt.GridLayout;
import java.awt.Window;

import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import org.contestorg.infos.InfosModelPropriete;
import org.contestorg.interfaces.ICollector;

@SuppressWarnings("serial")
public abstract class JDProprieteAbstract extends JDPattern
{
	
	// Collector
	private ICollector<InfosModelPropriete> collector;
	
	// Labels des types
	protected static String LABEL_TYPE_INT = "Nombre entier";
	protected static String LABEL_TYPE_FLOAT = "Nombre décimal";
	protected static String LABEL_TYPE_STRING = "Texte";
	
	// Entrées
	protected JTextField jtf_nom = new JTextField();
	protected JRadioButton jrb_obligatoire_oui = new JRadioButton("Oui", true);
	protected JRadioButton jrb_obligatoire_non = new JRadioButton("Non");
	protected JComboBox jcb_types;

	// Constructeur
	public JDProprieteAbstract(Window w_parent, String titre, ICollector<InfosModelPropriete> collector) {
		// Appeller le constructeur du parent
		super(w_parent, titre);
		
		// Retenir le collector
		this.collector = collector;
		
		// Informations sur la propriété
		this.jp_contenu.add(ViewHelper.title("Informations sur la propriété", ViewHelper.H1));
		
		String[] types = {JDProprieteAbstract.LABEL_TYPE_INT,JDProprieteAbstract.LABEL_TYPE_FLOAT,JDProprieteAbstract.LABEL_TYPE_STRING};
		this.jcb_types = new JComboBox(types);
		
		ButtonGroup bg_obligatoire = new ButtonGroup();
		bg_obligatoire.add(this.jrb_obligatoire_oui);
		bg_obligatoire.add(this.jrb_obligatoire_non);
		JPanel jp_automatique = new JPanel(new GridLayout(1, 2));
		jp_automatique.add(this.jrb_obligatoire_oui);
		jp_automatique.add(this.jrb_obligatoire_non);
		
		JLabel[] jls = {new JLabel("Nom : "),new JLabel("Obligatoire : "),new JLabel("Type : ")};
		JComponent[] jcs = {this.jtf_nom,jp_automatique,this.jcb_types};
		this.jp_contenu.add(ViewHelper.inputs(jls, jcs));
		
		// Pack
		this.pack();
	}

	// Implémentation de ok
	@Override
	protected void ok () {
		// Récupérer les données
		String nom = this.jtf_nom.getText().trim();
		int type = -1;
		if(this.jcb_types.getSelectedItem().equals(JDProprieteAbstract.LABEL_TYPE_INT)) { type = InfosModelPropriete.TYPE_INT; }
		else if(this.jcb_types.getSelectedItem().equals(JDProprieteAbstract.LABEL_TYPE_FLOAT)) { type = InfosModelPropriete.TYPE_FLOAT; }
		else if(this.jcb_types.getSelectedItem().equals(JDProprieteAbstract.LABEL_TYPE_STRING)) { type = InfosModelPropriete.TYPE_STRING; }
		boolean obligatoire = this.jrb_obligatoire_oui.isSelected();
		
		// Vérifier les données
		boolean erreur = false;
		if(nom.isEmpty()) {
			// Message d'erreur
			erreur = true;
			ViewHelper.derror(this, "Le nom de la propriété n'a pas été spécifié.");
		}
		
		// Envoyer les données au collector
		if(!erreur) {
			this.collector.accept(new InfosModelPropriete(nom, type, obligatoire));
		}
	}

	// Implémentation de quit
	@Override
	protected void quit () {
		// Annuler
		this.collector.cancel();
	}

}
