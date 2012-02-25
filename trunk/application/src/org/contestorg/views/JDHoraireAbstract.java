package org.contestorg.views;

import java.awt.Window;

import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import org.contestorg.infos.InfosModelHoraire;
import org.contestorg.interfaces.ICollector;

/**
 * Boîte de dialogue de création/édition d'un horaire
 */
@SuppressWarnings("serial")
public class JDHoraireAbstract extends JDPattern
{
	
	/** Collecteur des informations de l'horaire */
	private ICollector<InfosModelHoraire> collector;
	
	// Entrées
	
	/** Heure de début */
	protected JTextField jtf_heure_debut = new JTextField();
	
	/** Heure de fin */
	protected JTextField jtf_heure_fin = new JTextField();
	
	/** Liste des jours */
	protected JList<String> jl_jours;

	/**
	 * Constructeur
	 * @param w_parent fenêtre parent
	 * @param titre titre de la boîte de dialogue
	 * @param collector colecteur des informations de l'horaire
	 */
	public JDHoraireAbstract(Window w_parent, String titre, ICollector<InfosModelHoraire> collector) {
		// Appeller le constructeur du parent
		super(w_parent, titre);
		
		// Retenir le collector
		this.collector = collector;
		
		// Informations sur l'horaire
		this.jp_contenu.add(ViewHelper.title("Informations sur l'horaire",ViewHelper.H1));
		
		this.jtf_heure_debut.setToolTipText("Heure de début de l'horaire doit être de la forme \"??h??\" ou \"??h\"");
		this.jtf_heure_fin.setToolTipText("Heure de fin de l'horaire doit être de la forme \"??h??\" ou \"??h\"");
		
		String[] jours = {"Lundi","Mardi","Mercredi","Jeudi","Vendredi","Samedi","Dimanche"};
		this.jl_jours = new JList<String>(jours);
		this.jl_jours.setVisibleRowCount(7);
		this.jl_jours.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		JLabel[] jls = {new JLabel("Heure de début : "), new JLabel("Heure de fin : "), new JLabel("Jours : ")};
		JComponent[] jcs = {this.jtf_heure_debut,this.jtf_heure_fin,new JScrollPane(this.jl_jours)};
		this.jp_contenu.add(ViewHelper.inputs(jls, jcs));
		this.jp_contenu.add(Box.createVerticalStrut(5));
		this.jp_contenu.add(ViewHelper.pinformation("Maintenez \"ctrl\" enfoncée pour séléctionner plusieurs jours."));
		
		// Pack
		this.pack();
	}

	/**
	 * @see JDPattern#ok()
	 */
	@Override
	protected void ok () {
		// Récupérer les données
		String debut = this.jtf_heure_debut.getText().trim();
		String fin = this.jtf_heure_fin.getText().trim();
		int[] jours = this.jl_jours.getSelectedIndices();
		
		// Vérifier les données
		int debutParse = -1, finParse = -1, joursParse = 0;
		if(debut.isEmpty()) {
			// Message d'erreur
			ViewHelper.derror(this, "L'heure de début n'est pas précisé.");
		} else {
			// Parser l'heure de début
			debutParse = InfosModelHoraire.getHeureMinutes(debut);
			
			// Vérifier si le parse a bien fonctionné
			if(debutParse == -1) {
				// Message d'erreur
				ViewHelper.derror(this, "L'heure de début doit être de la forme \"??h??\" ou \"??h\".");
			}
		}
		if(fin.isEmpty()) {
			// Message d'erreur
			ViewHelper.derror(this, "L'heure de fin n'est pas précisé.");
		} else {
			// Parser l'heure de fin
			finParse = InfosModelHoraire.getHeureMinutes(fin);
			
			// Vérifier si le parse a bien fonctionné
			if(finParse == -1) {
				// Message d'erreur
				ViewHelper.derror(this, "L'heure de fin doit être de la forme \"??h??\" ou \"??h\".");
			}
		}
		if(debutParse != -1 && finParse != -1 && debutParse >= finParse) {
			// Remettre à -1 l'heure de début et de fin
			debutParse = -1;
			finParse = -1;
			
			// Message d'erreur
			ViewHelper.derror(this, "L'heure de début doit précéder l'heure de fin.");
		}
		if(jours.length == 0) {
			// Message d'erreur
			ViewHelper.derror(this, "Il faut séléctionner au moins un jour de la semaine.");
		} else {
			// Parser les jours
			joursParse = InfosModelHoraire.getJoursBinaire(jours);
		}
		
		// Envoyer les données au collector
		if(debutParse != -1 && finParse != -1 && joursParse != 0) {
			this.collector.collect(new InfosModelHoraire(joursParse, debutParse, finParse));
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
