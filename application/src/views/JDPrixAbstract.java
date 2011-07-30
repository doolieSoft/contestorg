package views;

import infos.InfosModelPrix;
import interfaces.ICollector;

import java.awt.Window;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public abstract class JDPrixAbstract extends JDPattern
{

	// Collector d'informations sur le prix
	protected ICollector<InfosModelPrix> collector;
	
	// Entrées
	protected JTextField jtf_nom = new JTextField();

	// Constructeur
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

	// Implémentation de ok
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
			this.collector.accept(new InfosModelPrix(nom));
		}
	}

	// Implémentation de quit
	@Override
	protected void quit () {
		// Annuler
		this.collector.cancel();
	}

}
