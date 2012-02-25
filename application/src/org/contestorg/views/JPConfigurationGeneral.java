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

/**
 * Panel de configuration général
 */
@SuppressWarnings("serial")
public class JPConfigurationGeneral extends JPConfigurationAbstract
{
	// Entrées
	
	/** Nom du concours */
	protected JTextField jtf_concours_nom = new JTextField();
	/** Lieu du concours */
	protected JTextField jtf_concours_lieu = new JTextField();
	/** Site web du concours */
	protected JTextField jtf_concours_site = new JTextField();
	/** Email du concours */
	protected JTextField jtf_concours_email = new JTextField();
	/** Téléphone du concours */
	protected JTextField jtf_concours_telephone = new JTextField();
	/** Description du concours */
	protected JTextArea jta_concours_description = new JTextArea();

	/** Nom de l'organisateur */
	protected JTextField jtf_organisateur_nom = new JTextField();
	/** Lieu de l'organisateur */
	protected JTextField jtf_organisateur_lieu = new JTextField();
	/** Site web de l'organisateur */
	protected JTextField jtf_organisateur_site = new JTextField();
	/** Email de l'organisateur */
	protected JTextField jtf_organisateur_email = new JTextField();
	/** Téléphone de l'organisateur */
	protected JTextField jtf_organisateur_telephone = new JTextField();
	/** Description de l'organisateur */
	protected JTextArea jta_organisateur_description = new JTextArea();

	/** Type des participants */
	protected JTextField jtf_participants = new JTextField();
	/** Type des participants "Equipes" */
	protected JRadioButton jrb_participants_equipes = new JRadioButton("Equipes", true);
	/** Type des participants "Joueurs" */
	protected JRadioButton jrb_participants_joueurs = new JRadioButton("Joueurs");
	/** Type des qualifications */
	protected JTextField jtf_qualifications = new JTextField();
	/** Type des qualifications "Phases" */
	protected JRadioButton jrb_qualifications_phases = new JRadioButton("Phases", true);
	/** Type des qualificatiions "Grille" */
	protected JRadioButton jrb_qualifications_grille = new JRadioButton("Grille");

	/**
	 * Constructeur
	 * @param w_parent fenêtre parent
	 */
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
	
	/**
	 * Récupérer le nom du concours
	 * @return nom du concours
	 */
	public String getConcoursNom() {
		return this.jtf_concours_nom.getText().trim();
	}
	
	/**
	 * Récupérer le lieu du concours
	 * @return lieu du concours
	 */
	public String getConcoursLieu() {
		return this.jtf_concours_lieu.getText().trim();
	}
	
	/**
	 * Récupérer le site web du concours
	 * @return site web du concours
	 */
	public String getConcoursSite() {
		return this.jtf_concours_site.getText().trim();
	}
	
	/**
	 * Récupérer l'email du concours
	 * @return email du concours
	 */
	public String getConcoursEmail() {
		return this.jtf_concours_email.getText().trim();
	}
	
	/**
	 * Récupérer le téléphone du concours
	 * @return téléphone du concours
	 */
	public String getConcoursTelephone() {
		return this.jtf_concours_telephone.getText().trim();
	}
	
	/**
	 * Récupérer la description du concours
	 * @return description du concours
	 */
	public String getConcoursDescription() {
		return this.jta_concours_description.getText().trim();
	}
	
	/**
	 * Récupérer le nom de l'organisme
	 * @return nom de l'organisme
	 */
	public String getOrganismeNom() {
		return this.jtf_organisateur_nom.getText().trim();
	}
	
	/**
	 * Récupérer le lieu de l'organisme
	 * @return lieu de l'organisme
	 */
	public String getOrganismeLieu() {
		return this.jtf_organisateur_lieu.getText().trim();
	}
	
	/**
	 * Récupérer le site web de l'organisme
	 * @return site web de l'organisme
	 */
	public String getOrganismeSite() {
		return this.jtf_organisateur_site.getText().trim();
	}
	
	/**
	 * Récupérer l'email de l'organisme
	 * @return email de l'organisme
	 */
	public String getOrganismeEmail() {
		return this.jtf_organisateur_email.getText().trim();
	}
	
	/**
	 * Récupérer le téléphone de l'organisme
	 * @return téléphone de l'organisme
	 */
	public String getOrganismeTelephone() {
		return this.jtf_organisateur_telephone.getText().trim();
	}
	
	/**
	 * Récupérer la description de l'organisme
	 * @return description de l'organisme
	 */
	public String getOrganismeDescription() {
		return this.jta_organisateur_description.getText().trim();
	}
	
	/**
	 * Récupérer le type des participants
	 * @return type des participants
	 */
	public int getTypeParticipants() {
		return this.jrb_participants_equipes.isSelected() ? InfosModelConcours.PARTICIPANTS_EQUIPES :InfosModelConcours.PARTICIPANTS_JOUEURS; 
	}
	
	/**
	 * Récupérer le type des qualifications
	 * @return type des qualifications
	 */
	public int getTypeQualifications() {
		return this.jrb_qualifications_phases.isSelected() ? InfosModelConcours.QUALIFICATIONS_PHASES : InfosModelConcours.QUALIFICATIONS_GRILLE;
	}
	
	// Setters
	
	/**
	 * Définir le nom du concours
	 * @param nom nom du concours
	 */
	public void setConcoursNom(String nom) {
		this.jtf_concours_nom.setText(nom);
	}
	
	/**
	 * Définir le lieu du concours
	 * @param nom lieu du concours
	 */
	public void setConcoursLieu(String nom) {
		this.jtf_concours_lieu.setText(nom);
	}
	
	/**
	 * Définir le site web du concours
	 * @param nom site web du concours
	 */
	public void setConcoursSite(String nom) {
		this.jtf_concours_site.setText(nom);
	}
	
	/**
	 * Définir l'email du concours
	 * @param nom email du concours
	 */
	public void setConcoursEmail(String nom) {
		this.jtf_concours_email.setText(nom);
	}
	
	/**
	 * Définir le téléphone du concours
	 * @param nom téléphone du concours
	 */
	public void setConcoursTelephone(String nom) {
		this.jtf_concours_telephone.setText(nom);
	}
	
	/**
	 * Définir la description du concours
	 * @param nom description du concours
	 */
	public void setConcoursDescription(String nom) {
		this.jta_concours_description.setText(nom);
	}
	
	/**
	 * Définir le nom de l'organisme
	 * @param nom nom de l'organisme
	 */
	public void setOrganismeNom(String nom) {
		this.jtf_organisateur_nom.setText(nom);
	}
	
	/**
	 * Définir le lieu de l'organisme
	 * @param lieu lieu de l'organisme
	 */
	public void setOrganismeLieu(String lieu) {
		this.jtf_organisateur_lieu.setText(lieu);
	}
	
	/**
	 * Définir le site web de l'organisme
	 * @param site site web de l'organisme
	 */
	public void setOrganismeSite(String site) {
		this.jtf_organisateur_site.setText(site);
	}
	
	/**
	 * Définir l'email de l'organisme
	 * @param email email de l'organisme
	 */
	public void setOrganismeEmail(String email) {
		this.jtf_organisateur_email.setText(email);
	}
	
	/**
	 * Définir le téléphone de l'organisme
	 * @param telephone téléphone de l'organisme
	 */
	public void setOrganismeTelephone(String telephone) {
		this.jtf_organisateur_telephone.setText(telephone);
	}
	
	/**
	 * Définir la description de l'organisle
	 * @param description description de l'organisle
	 */
	public void setOrganismeDescription(String description) {
		this.jta_organisateur_description.setText(description);
	}
	
	/**
	 * Définir le type de participants
	 * @param type type de participants
	 */
	public void setTypeParticipants(int type) {
		this.jrb_participants_equipes.setSelected(type == InfosModelConcours.PARTICIPANTS_EQUIPES);
		this.jrb_participants_joueurs.setSelected(type == InfosModelConcours.PARTICIPANTS_JOUEURS);
	}
	
	/**
	 * Définir le type de qualifications
	 * @param type type de qualifications
	 */
	public void setTypeQualifications(int type) {
		this.jrb_qualifications_phases.setSelected(type == InfosModelConcours.QUALIFICATIONS_PHASES);
		this.jrb_qualifications_grille.setSelected(type == InfosModelConcours.QUALIFICATIONS_GRILLE);
	}
	
	/**
	 * @see JPConfigurationAbstract#check()
	 */
	@Override
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
