package org.contestorg.views;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Window;

import javax.swing.ImageIcon;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import org.contestorg.infos.InfosModelConcours;

/**
 * Boîte de dialogue de création/édition de concours
 */
@SuppressWarnings("serial")
abstract public class JDConcoursAbstract extends JDPattern
{
	
	// Panels
	
	/** Panel de configuration général */
	protected JPConfigurationGeneral jp_general;
	
	/** Panel de configuration des points */
	protected JPConfigurationPoints jp_points;
	
	/** Panel de configuration des exportations et diffusions */
	protected JPConfigurationExportations jp_exportations;
	
	/** Panel de configuration des prix */
	protected JPConfigurationPrix jp_prix;
	
	/** Panel de configuration des lieux */
	protected JPConfigurationLieux jp_lieux;
	
	/** Panel de configuration des informations de programmation */
	protected JPConfigurationProgrammation jp_programmation;
	
	/** Panel de configuration des propriétés de participant */
	protected JPConfigurationProprietes jp_proprietes;

	/**
	 * Constructeur
	 * @param w_parent fenêtre parent
	 * @param titre titre de la boîte de dialogue
	 */
	public JDConcoursAbstract(Window w_parent, String titre) {
		// Appeler le constructeur du parent
		super(w_parent, titre);

		// Panel à onglet au centre de la fenêtre
		JTabbedPane jtb_onglets = new JTabbedPane();
		this.add(jtb_onglets, BorderLayout.CENTER);

		// Masquer les espacements à gauche et à droite
		this.hs_gauche.setVisible(false);
		this.hs_droite.setVisible(false);

		// Créer les panels
		this.jp_general = new JPConfigurationGeneral(this);
		this.jp_points = new JPConfigurationPoints(this);
		this.jp_exportations = new JPConfigurationExportations(this);
		this.jp_prix = new JPConfigurationPrix(this);
		this.jp_lieux = new JPConfigurationLieux(this);
		this.jp_programmation = new JPConfigurationProgrammation(this);
		this.jp_proprietes = new JPConfigurationProprietes(this);

		// Ajouter les onglets
		jtb_onglets.addTab("Général", new ImageIcon("img/farm/16x16/cog.png"), new JScrollPane(this.jp_general),"Configurer les propriétés générales du concours");
		jtb_onglets.addTab("Points et classement", new ImageIcon("img/farm/16x16/table.png"), new JScrollPane(this.jp_points),"Configurer les points acquis au cours des matchs et l'ordre de classement des participants");
		jtb_onglets.addTab("Exportations et diffusions", new ImageIcon("img/farm/16x16/arrow_right.png"), new JScrollPane(this.jp_exportations),"Configurer les exportations et les diffusions");
		jtb_onglets.addTab("Prix à remporter", new ImageIcon("img/farm/16x16/award_star_gold_2.png"), new JScrollPane(this.jp_prix),"Configurer les prix que peuvent remporter les participants");
		jtb_onglets.addTab("Lieux et emplacements", new ImageIcon("img/farm/16x16/house.png"), new JScrollPane(this.jp_lieux),"Configuration les lieux et emplacements où se déroulent les matchs");
		jtb_onglets.addTab("Programmation des matchs", new ImageIcon("img/farm/16x16/time.png"), new JScrollPane(this.jp_programmation),"Configuration les informations de programmation des matchs");
		jtb_onglets.addTab("Propriétés des participants", new ImageIcon("img/farm/16x16/group.png"), new JScrollPane(this.jp_proprietes),"Configurer les propriétés que peuvent posséder les participants");

		// Redimensionner la zone des onglets
		jtb_onglets.setPreferredSize(new Dimension(600, jtb_onglets.getPreferredSize().height+20));

		// Pack
		this.pack();
	}

	/**
	 * Récupérer les informations du concours
	 * @return informations du concours
	 */
	public InfosModelConcours getInfosModelConcours() {
		return new InfosModelConcours(
			this.jp_general.getConcoursNom(), this.jp_general.getConcoursSite(), this.jp_general.getConcoursLieu(), this.jp_general.getConcoursEmail(), this.jp_general.getConcoursTelephone(), this.jp_general.getConcoursDescription(),
			this.jp_general.getOrganismeNom(), this.jp_general.getOrganismeSite(), this.jp_general.getOrganismeLieu(), this.jp_general.getOrganismeEmail(), this.jp_general.getOrganismeTelephone(), this.jp_general.getOrganismeDescription(),
			this.jp_general.getTypeQualifications(), this.jp_general.getTypeParticipants(),
			this.jp_points.getPointsVictoire(), this.jp_points.getPointsEgalite(), this.jp_points.getPointsDefaite(),
			this.jp_programmation.getProgrammationDuree(), this.jp_programmation.getProgrammationInterval(), this.jp_programmation.getProgrammationPause()
		);
	}
	
	/**
	 * Vérifier la validité des données
	 * @return données valides ?
	 */
	protected boolean check () {
		// Initialiser le résultat
		boolean result = true;

		// Valider chacun des panels
		result = this.jp_general.check() && result;
		result = this.jp_points.check() && result;
		result = this.jp_exportations.check() && result;
		result = this.jp_prix.check() && result;
		result = this.jp_lieux.check() && result;
		result = this.jp_programmation.check() && result;
		result = this.jp_proprietes.check() && result;

		// Retourner le resultat
		return result;
	}

}
