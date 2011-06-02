package views;

import infos.InfosModelEmplacement;
import interfaces.ICollector;

import java.awt.Window;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public abstract class JDEmplacementAbstract extends JDPattern
{	
	// Collector
	private ICollector<InfosModelEmplacement> collector;
	
	// Entrées
	protected JTextField jtf_nom = new JTextField();
	protected JTextArea jta_description = new JTextArea();

	// Constructeur
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

	// Implémentation de ok
	@Override
	protected void ok () {
		// Récupérer les données
		String nom = this.jtf_nom.getText().trim();
		String description = this.jta_description.getText().trim();
		
		// Envoyer les informations au collector
		if(!nom.isEmpty()) {
			collector.accept(new InfosModelEmplacement(nom, description));
		} else {
			// Message d'erreur
			ViewHelper.derror(this, "Le nom de l'emplacment est vide.");
		}
	}

	// Implémentation de quit
	@Override
	protected void quit () {
		// Annuler
		this.collector.cancel();
	}

}
