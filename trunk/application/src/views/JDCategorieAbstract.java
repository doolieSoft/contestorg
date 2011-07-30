package views;

import infos.InfosModelCategorie;
import interfaces.ICollector;

import java.awt.Window;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public abstract class JDCategorieAbstract extends JDPattern
{
	// Collector
	private ICollector<InfosModelCategorie> collector;
	
	// Entrées
	protected JTextField jtf_nom = new JTextField();
	
	// Constructeur
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

	// Implémentation de ok
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
			this.collector.accept(new InfosModelCategorie(nom));
		}
	}
	
	// Implémentation de quit
	@Override
	protected void quit () {
		// Annuler
		this.collector.cancel();
	}
	
}
