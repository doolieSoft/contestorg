package org.contestorg.views;


import java.awt.Window;
import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextField;

import org.contestorg.common.Pair;
import org.contestorg.infos.InfosModelPoule;
import org.contestorg.interfaces.ICollector;


@SuppressWarnings("serial")
public abstract class JDPouleAbstract extends JDPattern
{
	// Collector
	private ICollector<Pair<InfosModelPoule, ArrayList<String>>> collector;
	
	// Entrées
	protected JTextField jtf_nom = new JTextField();
	
	// Liste des équipes de la poule
	private ArrayList<String> equipes = new ArrayList<String>();
	
	// Constructeur
	public JDPouleAbstract(Window w_parent, String titre, ICollector<Pair<InfosModelPoule, ArrayList<String>>> collector) {
		// Appeller le constructeur du parent
		super(w_parent, titre);
		
		// Retenir le collector
		this.collector = collector;
		
		// Informations sur la poule
		this.jp_contenu.add(ViewHelper.title("Informations sur la poule", ViewHelper.H1));
		JLabel[] jls_poule = {new JLabel("Nom : ")};
		JComponent[] jcs_poule = {this.jtf_nom};
		this.jp_contenu.add(ViewHelper.inputs(jls_poule, jcs_poule));
		
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
			ViewHelper.derror(this, "Le nom de la poule n'est pas précisé.");
			erreur = true;
		}

		// Créer et retourner les informations
		if(!erreur) {
			this.collector.accept(new Pair<InfosModelPoule, ArrayList<String>>(new InfosModelPoule(nom), this.equipes));
		}
	}
	
	// Implémentation de quit
	@Override
	protected void quit () {
		// Annuler
		this.collector.cancel();
	}
	
}
