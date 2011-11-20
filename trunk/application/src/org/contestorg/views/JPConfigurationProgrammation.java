package org.contestorg.views;

import java.awt.Window;

import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class JPConfigurationProgrammation extends JPConfigurationAbstract
{

	// Entrées
	protected JTextField jtf_programmation_duree = new JTextField();
	protected JTextField jtf_programmation_intervalle = new JTextField();
	protected JTextField jtf_programmation_pause = new JTextField();

	// Constructeur
	public JPConfigurationProgrammation(Window w_parent) {
		// Appeller le constructeur du parent
		super(w_parent);

		// Informations de programmation
		this.jp_contenu.add(ViewHelper.title("Informations de programmation des matchs", ViewHelper.H1));
		JLabel[] jls_programmation = { new JLabel("Durée d'un match : "), new JLabel("Intervalle minimal entre les matchs : "), new JLabel("Pause minimale d'un participant : ") };
		JComponent[] jcs_programmation = { this.jtf_programmation_duree, this.jtf_programmation_intervalle, this.jtf_programmation_pause };
		this.jp_contenu.add(ViewHelper.inputs(jls_programmation, jcs_programmation));
		this.jp_contenu.add(Box.createVerticalStrut(8));
		this.jp_contenu.add(ViewHelper.pinformation("Ces valeurs sont approximatives. L'unité de temps est la minute."));
	}
	
	// Getters
	public Double getProgrammationDuree() {
		String duree = this.jtf_programmation_duree.getText().trim();
		return duree.isEmpty() ? null : Double.parseDouble(duree);
	}
	public Double getProgrammationInterval() {
		String interval = this.jtf_programmation_intervalle.getText().trim();
		return interval.isEmpty() ? null : Double.parseDouble(interval);
	}
	public Double getProgrammationPause() {
		String pause = this.jtf_programmation_pause.getText().trim();
		return pause.isEmpty() ? null : Double.parseDouble(pause);
	}
	
	// Setters
	public void setProgrammationDuree(Double duree) {
		this.jtf_programmation_duree.setText(duree == null ? "" : String.valueOf(duree));
	}
	public void setProgrammationInterval(Double interval) {
		this.jtf_programmation_intervalle.setText(interval == null ? "" : String.valueOf(interval));
	}
	public void setProgrammationPause(Double pause) {
		this.jtf_programmation_pause.setText(pause == null ? "" : String.valueOf(pause));
	}

	// Vérifier la validité des données
	public boolean check () {
		// Initialiser le booléen d'erreur
		boolean erreur = false;
		
		// Récupérer les données
		String duree = this.jtf_programmation_duree.getText().trim();
		String interval = this.jtf_programmation_intervalle.getText().trim();
		String pause = this.jtf_programmation_pause.getText().trim();

		// Valider les données de programmation 
		if(!duree.isEmpty()) {
			try {
				Double.parseDouble(duree);
			} catch(NumberFormatException exception) {
				// Message d'erreur
				erreur = true;
				ViewHelper.derror(this.w_parent, "Programmation de matchs", "La durée d'un match doit être un nombre décimale.");
			}
		} 
		if(!interval.isEmpty()) {
			try {
				Double.parseDouble(interval);
			} catch(NumberFormatException exception) {
				// Message d'erreur
				erreur = true;
				ViewHelper.derror(this.w_parent, "Programmation de matchs", "L'interval minimal entre chaque match d'un match doit être un nombre décimale.");
			}
		} 
		if(!pause.isEmpty()) {
			try {
				Double.parseDouble(pause);
			} catch(NumberFormatException exception) {
				// Message d'erreur
				erreur = true;
				ViewHelper.derror(this.w_parent, "Programmation de matchs", "La pause minimal d'un participant doit être un nombre décimale.");
			}
		}

		// Retourner true si les données sont correctes
		return !erreur;
	}
}
