package org.contestorg.views;


import java.awt.GridLayout;
import java.awt.Window;

import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.contestorg.infos.InfosModelConcours;

@SuppressWarnings("serial")
public class JPConfigurationGeneral extends JPConfigurationAbstract
{
	// Entrées pour le concours
	protected JTextField jtf_concours_nom = new JTextField();
	protected JTextField jtf_concours_lieu = new JTextField();
	protected JTextField jtf_concours_site = new JTextField();
	protected JTextField jtf_concours_email = new JTextField();
	protected JTextField jtf_concours_telephone = new JTextField();
	protected JTextArea jta_concours_description = new JTextArea();

	// Entrées pour l'organisateur
	protected JTextField jtf_organisateur_nom = new JTextField();
	protected JTextField jtf_organisateur_lieu = new JTextField();
	protected JTextField jtf_organisateur_site = new JTextField();
	protected JTextField jtf_organisateur_email = new JTextField();
	protected JTextField jtf_organisateur_telephone = new JTextField();
	protected JTextArea jta_organisateur_description = new JTextArea();

	// Entrées diverses
	protected JTextField jtf_participants = new JTextField();
	protected JRadioButton jrb_participants_equipes = new JRadioButton("Equipes", true);
	protected JRadioButton jrb_participants_joueurs = new JRadioButton("Joueurs");
	protected JTextField jtf_qualifications = new JTextField();
	protected JRadioButton jrb_qualifications_phases = new JRadioButton("Phases", true);
	protected JRadioButton jrb_qualifications_grille = new JRadioButton("Grille");

	// Constructeur
	public JPConfigurationGeneral(Window w_parent) {
		// Appeller le constructeur du parent
		super(w_parent);

		// Informations sur le concours
		this.jp_contenu.add(ViewHelper.title("Informations sur le concours", ViewHelper.H1));
		this.jta_concours_description.setLineWrap(true);
		this.jta_concours_description.setWrapStyleWord(true);
		this.jta_concours_description.setRows(5);
		JLabel[] jls_concours = { new JLabel("Nom : "), new JLabel("Lieu(s) : "), new JLabel("Site web : "), new JLabel("Email(s) : "), new JLabel("Téléphone(s) : "), new JLabel("Description : ") };
		JComponent[] jcs_concours = { this.jtf_concours_nom, this.jtf_concours_lieu, this.jtf_concours_site, this.jtf_concours_email, this.jtf_concours_telephone, new JScrollPane(this.jta_concours_description) };
		this.jp_contenu.add(ViewHelper.inputs(jls_concours, jcs_concours));

		// Informations sur l'organisateur
		this.jp_contenu.add(ViewHelper.title("Informations sur l'organisateur", ViewHelper.H1));
		this.jta_organisateur_description.setLineWrap(true);
		this.jta_organisateur_description.setWrapStyleWord(true);
		this.jta_organisateur_description.setRows(5);
		JLabel[] jls_organisme = { new JLabel("Nom : "), new JLabel("Lieu(s) : "), new JLabel("Site web : "), new JLabel("Email(s) : "), new JLabel("Téléphone(s) : "), new JLabel("Description : ") };
		JComponent[] jcs_organisme = { this.jtf_organisateur_nom, this.jtf_organisateur_lieu, this.jtf_organisateur_site, this.jtf_organisateur_email, this.jtf_organisateur_telephone, new JScrollPane(this.jta_organisateur_description) };
		this.jp_contenu.add(ViewHelper.inputs(jls_organisme, jcs_organisme));

		// Informations diverses
		this.jp_contenu.add(ViewHelper.title("Informations diverses", ViewHelper.H1));

		ButtonGroup bg_participants = new ButtonGroup();
		bg_participants.add(this.jrb_participants_equipes);
		bg_participants.add(this.jrb_participants_joueurs);
		JPanel jp_participants = new JPanel(new GridLayout(1, 2));
		jp_participants.add(this.jrb_participants_equipes);
		jp_participants.add(this.jrb_participants_joueurs);

		ButtonGroup bg_qualifications = new ButtonGroup();
		bg_qualifications.add(this.jrb_qualifications_phases);
		bg_qualifications.add(this.jrb_qualifications_grille);
		JPanel jp_qualifications = new JPanel(new GridLayout(1, 2));
		jp_qualifications.add(this.jrb_qualifications_phases);
		jp_qualifications.add(this.jrb_qualifications_grille);

		// FIXME Prendre en charge les qualifications en mode grille
		//JLabel[] jls_divers = { new JLabel("Participants : "), new JLabel("Qualifications : ") };
		//JComponent[] jcs_divers = { jp_participants, jp_qualifications };
		JLabel[] jls_divers = { new JLabel("Participants : ") };
		JComponent[] jcs_divers = { jp_participants };
		this.jp_contenu.add(ViewHelper.inputs(jls_divers, jcs_divers));
	}

	// Getters
	public String getConcoursNom() {
		return this.jtf_concours_nom.getText().trim();
	}
	public String getConcoursLieu() {
		return this.jtf_concours_lieu.getText().trim();
	}
	public String getConcoursSite() {
		return this.jtf_concours_site.getText().trim();
	}
	public String getConcoursEmail() {
		return this.jtf_concours_email.getText().trim();
	}
	public String getConcoursTelephone() {
		return this.jtf_concours_telephone.getText().trim();
	}
	public String getConcoursDescription() {
		return this.jta_concours_description.getText().trim();
	}
	public String getOrganismeNom() {
		return this.jtf_organisateur_nom.getText().trim();
	}
	public String getOrganismeLieu() {
		return this.jtf_organisateur_lieu.getText().trim();
	}
	public String getOrganismeSite() {
		return this.jtf_organisateur_site.getText().trim();
	}
	public String getOrganismeEmail() {
		return this.jtf_organisateur_email.getText().trim();
	}
	public String getOrganismeTelephone() {
		return this.jtf_organisateur_telephone.getText().trim();
	}
	public String getOrganismeDescription() {
		return this.jta_organisateur_description.getText().trim();
	}
	public int getTypeParticipants() {
		return this.jrb_participants_equipes.isSelected() ? InfosModelConcours.PARTICIPANTS_EQUIPES :InfosModelConcours.PARTICIPANTS_JOUEURS; 
	}
	public int getTypeQualifications() {
		return this.jrb_qualifications_phases.isSelected() ? InfosModelConcours.QUALIFICATIONS_PHASES : InfosModelConcours.QUALIFICATIONS_GRILLE;
	}
	
	// Setters
	public void setConcoursNom(String nom) {
		this.jtf_concours_nom.setText(nom);
	}
	public void setConcoursLieu(String nom) {
		this.jtf_concours_lieu.setText(nom);
	}
	public void setConcoursSite(String nom) {
		this.jtf_concours_site.setText(nom);
	}
	public void setConcoursEmail(String nom) {
		this.jtf_concours_email.setText(nom);
	}
	public void setConcoursTelephone(String nom) {
		this.jtf_concours_telephone.setText(nom);
	}
	public void setConcoursDescription(String nom) {
		this.jta_concours_description.setText(nom);
	}
	public void setOrganismeNom(String nom) {
		this.jtf_organisateur_nom.setText(nom);
	}
	public void setOrganismeLieu(String nom) {
		this.jtf_organisateur_lieu.setText(nom);
	}
	public void setOrganismeSite(String nom) {
		this.jtf_organisateur_site.setText(nom);
	}
	public void setOrganismeEmail(String nom) {
		this.jtf_organisateur_email.setText(nom);
	}
	public void setOrganismeTelephone(String nom) {
		this.jtf_organisateur_telephone.setText(nom);
	}
	public void setOrganismeDescription(String nom) {
		this.jta_organisateur_description.setText(nom);
	}
	public void setTypeParticipants(int type) {
		this.jrb_participants_equipes.setSelected(type == InfosModelConcours.PARTICIPANTS_EQUIPES);
		this.jrb_participants_joueurs.setSelected(type == InfosModelConcours.PARTICIPANTS_JOUEURS);
	}
	public void setTypeQualifications(int type) {
		this.jrb_qualifications_phases.setSelected(type == InfosModelConcours.QUALIFICATIONS_PHASES);
		this.jrb_qualifications_grille.setSelected(type == InfosModelConcours.QUALIFICATIONS_GRILLE);
	}
	
	// Vérifier la validité des données
	public boolean check () {
		// Vérifier si le concours a un nom
		if(this.jtf_concours_nom.getText().trim().isEmpty()) {
			// Message d'erreur
			ViewHelper.derror(this.w_parent, "Général", "Le concours doit avoir un nom.");
			return false;
		}
		
		// Les données sont correctes
		return true;
	}
}
