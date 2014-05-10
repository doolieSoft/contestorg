package org.contestorg.views;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Window;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.contestorg.common.Pair;
import org.contestorg.common.Quadruple;
import org.contestorg.common.TrackableList;
import org.contestorg.common.Triple;
import org.contestorg.controllers.ContestOrg;
import org.contestorg.infos.InfosModelConcours;
import org.contestorg.infos.InfosModelMatchPhasesElims;
import org.contestorg.infos.InfosModelObjectifRemporte;
import org.contestorg.infos.InfosModelParticipation;
import org.contestorg.interfaces.ICollector;

/**
 * Boîte de dialogue d'édition d'un match des phases éliminatoires
 */
@SuppressWarnings("serial")
public class JDMatchPhasesEliminatoires extends JDPattern implements ItemListener, ChangeListener
{
	/** Collecteur des informations du latch */
	private ICollector<Quadruple<Pair<TrackableList<Pair<String, InfosModelObjectifRemporte>>, InfosModelParticipation>, Pair<TrackableList<Pair<String, InfosModelObjectifRemporte>>, InfosModelParticipation>, Pair<String, String>, InfosModelMatchPhasesElims>> collector;
	
	
	/** Résultat du participant A */
	private JComboBox jcb_resultatA;
	
	/** Résultat du participant B */
	private JComboBox jcb_resultatB;
	
	/** Panel des objectifs remportés */
	private JPObjectifs jp_objectifs;
	
	/** Points remportés par le participant A */
	private JTextField jtf_pointsA;

	/** Points remportés par le participant B */
	private JTextField jtf_pointsB;
	
	/** Spécifier la date du match ? */
	protected JCheckBox jcb_date;
	
	/** Panel de la date */
	protected JPDatePicker jp_datePicker;
	
	/** Lieu et emplacement */
	protected JPLieuEmplacement jp_lieuEmplacement;
	
	/** Détails */
	private JTextArea jta_details;
	
	/**
	 * Constructeur
	 * @param w_parent fenêtre parent
	 * @param collector collecteur des informations du match
	 * @param infos informations du match
	 * @param resultatsEditable résultats éditables ?
	 */
	public JDMatchPhasesEliminatoires(Window w_parent, ICollector<Quadruple<Pair<TrackableList<Pair<String, InfosModelObjectifRemporte>>, InfosModelParticipation>, Pair<TrackableList<Pair<String, InfosModelObjectifRemporte>>, InfosModelParticipation>, Pair<String, String>, InfosModelMatchPhasesElims>> collector, Quadruple<Triple<String, ArrayList<Pair<String, InfosModelObjectifRemporte>>, InfosModelParticipation>, Triple<String, ArrayList<Pair<String, InfosModelObjectifRemporte>>, InfosModelParticipation>, Pair<String,String>, InfosModelMatchPhasesElims> infos, boolean resultatsEditable) {
		// Appeller le constructeur du parent
		super(w_parent, "Editer un match");
		
		// Retenir le collector
		this.collector = collector;
		
		// Participants
		this.jp_contenu.add(ViewHelper.title(ContestOrg.get().getCtrlParticipants().getTypeParticipants() == InfosModelConcours.PARTICIPANTS_EQUIPES ? "Equipes" : "Joueurs", ViewHelper.H1));
		JPanel jp_participants = new JPanel(new GridLayout(1,2));
		
		JLabel jl_participantA = new JLabel(infos.getFirst().getFirst());
		jl_participantA.setBorder(new EmptyBorder(5, 5, 5, 5));
		JPanel jp_participantA = new JPanel(new BorderLayout());
		jp_participantA.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		jp_participantA.add(jl_participantA, BorderLayout.CENTER);
		
		JLabel jl_participantB = new JLabel(infos.getSecond().getFirst());
		jl_participantB.setBorder(new EmptyBorder(5, 5, 5, 5));
		JPanel jp_participantB = new JPanel(new BorderLayout());
		jp_participantB.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		jp_participantB.add(jl_participantB, BorderLayout.CENTER);
		
		jp_participants.add(jp_participantA);
		jp_participants.add(jp_participantB);
		
		this.jp_contenu.add(jp_participants);
		
		// Résultats
		this.jp_contenu.add(Box.createVerticalStrut(5));
		this.jp_contenu.add(ViewHelper.title("Résultats", ViewHelper.H1));
		
		JPanel jp_resultat = new JPanel(new GridLayout(1,2));
		String[] resultats = { "Attente", "Victoire", "Défaite", "Forfait" };
		this.jcb_resultatA = new JComboBox(resultats);
		this.jcb_resultatB = new JComboBox(resultats);
		jp_resultat.add(this.jcb_resultatA);
		jp_resultat.add(this.jcb_resultatB);
		this.jp_contenu.add(jp_resultat);

		switch(infos.getFirst().getThird().getResultat()) {
			case InfosModelParticipation.RESULTAT_ATTENTE:
				this.jcb_resultatA.setSelectedIndex(0);
				break;
			case InfosModelParticipation.RESULTAT_VICTOIRE:
				this.jcb_resultatA.setSelectedIndex(1);
				break;
			case InfosModelParticipation.RESULTAT_DEFAITE:
				this.jcb_resultatA.setSelectedIndex(2);
				break;
			case InfosModelParticipation.RESULTAT_FORFAIT:
				this.jcb_resultatA.setSelectedIndex(3);
				break;
		}
		switch(infos.getSecond().getThird().getResultat()) {
			case InfosModelParticipation.RESULTAT_ATTENTE:
				this.jcb_resultatB.setSelectedIndex(0);
				break;
			case InfosModelParticipation.RESULTAT_VICTOIRE:
				this.jcb_resultatB.setSelectedIndex(1);
				break;
			case InfosModelParticipation.RESULTAT_DEFAITE:
				this.jcb_resultatB.setSelectedIndex(2);
				break;
			case InfosModelParticipation.RESULTAT_FORFAIT:
				this.jcb_resultatB.setSelectedIndex(3);
				break;
		}

		this.jcb_resultatA.addItemListener(this);
		this.jcb_resultatB.addItemListener(this);

		this.jcb_resultatA.setEnabled(resultatsEditable);
		this.jcb_resultatB.setEnabled(resultatsEditable);
		
		// Objectifs remportés
		this.jp_contenu.add(Box.createVerticalStrut(5));
		this.jp_objectifs = new JPObjectifs();
		this.jp_objectifs.addChangeListener(this);
		this.jp_contenu.add(this.jp_objectifs);
		
		// Points
		this.jp_contenu.add(ViewHelper.title("Points", ViewHelper.H1));

		this.jtf_pointsA = new JTextField(String.format("%.2f", (double)0));
		this.jtf_pointsB = new JTextField(String.format("%.2f", (double)0));

		this.jtf_pointsA.setEditable(false);
		this.jtf_pointsB.setEditable(false);
		
		JPanel jp_points = new JPanel(new GridLayout(1,2));
		jp_points.add(this.jtf_pointsA);
		jp_points.add(this.jtf_pointsB);
		
		this.jp_contenu.add(jp_points);
		
		// Définir les objectifs remportés
		this.jp_objectifs.setObjectifsRemportesA(infos.getFirst().getSecond());
		this.jp_objectifs.setObjectifsRemportesB(infos.getSecond().getSecond());
		
		// Date
		this.jp_contenu.add(ViewHelper.title("Date", ViewHelper.H1));
		
		this.jcb_date = new JCheckBox("Définir la date du match ?");
		this.jcb_date.setSelected(false);
		this.jcb_date.addChangeListener(this);
		this.jp_contenu.add(ViewHelper.left(this.jcb_date));
		
		this.jp_datePicker = new JPDatePicker();
		this.jp_contenu.add(this.jp_datePicker);
		
		this.jp_datePicker.setVisible(infos.getFourth().getDate() != null);
		if(infos.getFourth().getDate() != null) {
			this.jp_datePicker.setDate(infos.getFourth().getDate());
		}
		this.jcb_date.setSelected(infos.getFourth().getDate() != null);
		
		// Lieu et emplacement
		this.jp_lieuEmplacement = new JPLieuEmplacement();
		if(infos.getThird() != null) {
			this.jp_lieuEmplacement.setNomLieu(infos.getThird().getFirst());
			this.jp_lieuEmplacement.setNomEmplacement(infos.getThird().getSecond());
		}
		this.jp_contenu.add(this.jp_lieuEmplacement);
		
		// Détails
		this.jp_contenu.add(ViewHelper.title("Détails", ViewHelper.H1));
		
		this.jta_details = new JTextArea(infos.getFourth().getDetails());
		this.jta_details.setLineWrap(true);
		this.jta_details.setWrapStyleWord(true);
		this.jta_details.setRows(5);
		this.jp_contenu.add(new JScrollPane(this.jta_details));
		
		// Pack
		this.pack();
	}

	/**
	 * @see JDPattern#ok()
	 */
	@Override
	protected void ok () {
		// Récupérer les données
		int resultatA = 0;
		switch(this.jcb_resultatA.getSelectedIndex()) {
			case 0:
				resultatA = InfosModelParticipation.RESULTAT_ATTENTE;
				break;
			case 1:
				resultatA = InfosModelParticipation.RESULTAT_VICTOIRE;
				break;
			case 2:
				resultatA = InfosModelParticipation.RESULTAT_DEFAITE;
				break;
			case 3:
				resultatA = InfosModelParticipation.RESULTAT_DEFAITE;
				break;
		}
		int resultatB = 0;
		switch(this.jcb_resultatB.getSelectedIndex()) {
			case 0:
				resultatB = InfosModelParticipation.RESULTAT_ATTENTE;
				break;
			case 1:
				resultatB = InfosModelParticipation.RESULTAT_VICTOIRE;
				break;
			case 2:
				resultatB = InfosModelParticipation.RESULTAT_DEFAITE;
				break;
			case 3:
				resultatB = InfosModelParticipation.RESULTAT_DEFAITE;
				break;
		}
		String details = this.jta_details.getText().trim();
		Date date = this.jcb_date.isSelected() ? this.jp_datePicker.getDate() : null;
		String nomLieu = this.jp_lieuEmplacement.getNomLieu();
		String nomEmplacement = this.jp_lieuEmplacement.getNomEmplacement();
		
		// Vérifier les données
		boolean erreur = false;
		if(resultatA == InfosModelParticipation.RESULTAT_ATTENTE && resultatB != InfosModelParticipation.RESULTAT_ATTENTE || resultatA != InfosModelParticipation.RESULTAT_ATTENTE && resultatB == InfosModelParticipation.RESULTAT_ATTENTE ) {
			// Erreur
			erreur = true;
			ViewHelper.derror(this, "Le résultat du match n'est pas cohérent.");
		}
		if(resultatA == InfosModelParticipation.RESULTAT_VICTOIRE && resultatB == InfosModelParticipation.RESULTAT_VICTOIRE) {
			// Erreur
			erreur = true;
			ViewHelper.derror(this, "Il ne peut y avoir qu'un seul vainqueur.");
		}
		if(resultatA == InfosModelParticipation.RESULTAT_DEFAITE && resultatB == InfosModelParticipation.RESULTAT_DEFAITE) {
			// Erreur
			erreur = true;
			ViewHelper.derror(this, "Il ne peut pas y avoir une double défaite dans les matchs des phases éliminatoires.");
		}
		if(this.jcb_date.isSelected() && date == null) {
			// Erreur
			erreur = true;
			ViewHelper.derror(this, "La date du match n'a pas été définie.");
		}
		if ((resultatA == InfosModelParticipation.RESULTAT_ATTENTE || resultatB == InfosModelParticipation.RESULTAT_ATTENTE) && (this.jp_objectifs.isObjectifsRemportesA() || this.jp_objectifs.isObjectifsRemportesB())) {
			// Demande de confirmation
			erreur = !ViewHelper.confirmation(this, "Vous n'avez pas renseigné le résultat du match alors que vous avez défini des objectifs remportés. Désirez-vous continuer ?", true);
		}
		
		// Transmettre les données au collector
		if(!erreur) {
			// Récupérer la liste des prix remportés
			if(resultatA == InfosModelParticipation.RESULTAT_ATTENTE || resultatB == InfosModelParticipation.RESULTAT_ATTENTE) {
				this.jp_objectifs.clear();
			}
			TrackableList<Pair<String, InfosModelObjectifRemporte>> objectifsRemportesA = this.jp_objectifs.getObjectifsRemportesA();
			TrackableList<Pair<String, InfosModelObjectifRemporte>> objectifsRemportesB = this.jp_objectifs.getObjectifsRemportesB();
			
			// Créer les informations de participation
			Pair<TrackableList<Pair<String, InfosModelObjectifRemporte>>, InfosModelParticipation> participationA = new Pair<TrackableList<Pair<String,InfosModelObjectifRemporte>>, InfosModelParticipation>(objectifsRemportesA, new InfosModelParticipation(resultatA));
			Pair<TrackableList<Pair<String, InfosModelObjectifRemporte>>, InfosModelParticipation> participationB = new Pair<TrackableList<Pair<String,InfosModelObjectifRemporte>>, InfosModelParticipation>(objectifsRemportesB, new InfosModelParticipation(resultatB));
			
			// Transmettre les données au collector
			this.collector.collect(new Quadruple<Pair<TrackableList<Pair<String,InfosModelObjectifRemporte>>,InfosModelParticipation>, Pair<TrackableList<Pair<String,InfosModelObjectifRemporte>>,InfosModelParticipation>, Pair<String,String>, InfosModelMatchPhasesElims>(participationA , participationB, new Pair<String,String>(nomLieu,nomEmplacement), new InfosModelMatchPhasesElims(date,details)));
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

	/**
	 * @see ItemListener#itemStateChanged(ItemEvent)
	 */
	@Override
	public void itemStateChanged (ItemEvent event) {
		if(event.getStateChange() == ItemEvent.SELECTED) {
			if(event.getSource() == this.jcb_resultatA || event.getSource() == this.jcb_resultatB) {
				// Modifier la liste non modifiée si nécéssaire
				JComboBox jcb_event = event.getSource() == this.jcb_resultatA ? this.jcb_resultatA : this.jcb_resultatB;
				JComboBox jcb_other = event.getSource() == this.jcb_resultatA ? this.jcb_resultatB : this.jcb_resultatA;
				switch(jcb_event.getSelectedIndex()) {
					case 0: // Attente
						jcb_other.setSelectedIndex(0);
						break;
					case 1: // Victoire
						jcb_other.setSelectedIndex(2);
						break;
					case 2: // Défaite
						jcb_other.setSelectedIndex(1);
						break;
					case 3: // Forfait
						jcb_other.setSelectedIndex(1);
						break;
				}
			}
			
			// Modifier les points remportés
			this.jtf_pointsA.setText(String.format("%.2f", this.getPointsA()));
			this.jtf_pointsB.setText(String.format("%.2f", this.getPointsB()));
		}
	}

	/**
	 * @see ChangeListener#stateChanged(ChangeEvent)
	 */
	@Override
	public void stateChanged (ChangeEvent event) {
		if(event.getSource() == this.jcb_date) {
			// Afficher/Masquer la date
			if(this.jcb_date.isSelected() != this.jp_datePicker.isVisible()) {
				this.setDateVisible(this.jcb_date.isSelected());
			}
		} else if (event.getSource() == this.jp_objectifs) {
			// Modifier les points remportés
			this.jtf_pointsA.setText(String.format("%.2f", this.getPointsA()));
			this.jtf_pointsB.setText(String.format("%.2f", this.getPointsB()));
		}
	}
	
	/**
	 * Définir si le calendrier est visible ou non
	 * @param visible calendrier visible ?
	 */
	protected void setDateVisible(boolean visible) {
		this.jp_datePicker.setVisible(visible);
		this.pack();
	}
	
	/**
	 * Récupérer les points remportés par le participant A
	 * @return points remportés par le participant A
	 */
	private double getPointsA() {
		double points = this.jp_objectifs.getPointsA();
		switch(this.jcb_resultatA.getSelectedIndex()) {
			case 0: // Attente
				points = 0;
				break;
			case 1: // Victoire
				points += ContestOrg.get().getConcours().getPointsVictoire();
				break;
			case 2: // Defaite
				points += ContestOrg.get().getConcours().getPointsDefaite();
				break;
			case 3: // Forfait
				points += ContestOrg.get().getConcours().getPointsForfait();
				break;
		}
		return points;
	}

	/**
	 * Récupérer les points remportés par le participant B
	 * @return points remportés par le participant B
	 */
	private double getPointsB() {
		double points = this.jp_objectifs.getPointsB();
		switch(this.jcb_resultatB.getSelectedIndex()) {
			case 0: // Attente
				points = 0;
				break;
			case 1: // Victoire
				points += ContestOrg.get().getConcours().getPointsVictoire();
				break;
			case 2: // Defaite
				points += ContestOrg.get().getConcours().getPointsDefaite();
				break;
			case 3: // Forfait
				points += ContestOrg.get().getConcours().getPointsForfait();
				break;
		}
		return points;
	}
	
}
