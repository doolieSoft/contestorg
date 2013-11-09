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
	private JComboBox<String> jcb_resultatA;
	
	/** Résultat du participant B */
	private JComboBox<String> jcb_resultatB;
	
	/** Panel des objectifs remportés */
	private JPObjectifs jp_prix;
	
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
		String[] resultats = { "Attente", "Victoire", "Défaite" };
		this.jcb_resultatA = new JComboBox<String>(resultats);
		this.jcb_resultatB = new JComboBox<String>(resultats);
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
		}

		this.jcb_resultatA.addItemListener(this);
		this.jcb_resultatB.addItemListener(this);

		this.jcb_resultatA.setEnabled(resultatsEditable);
		this.jcb_resultatB.setEnabled(resultatsEditable);
		
		// Prix remportés
		this.jp_contenu.add(Box.createVerticalStrut(5));
		this.jp_prix = new JPObjectifs();
		this.jp_contenu.add(this.jp_prix);
		
		this.jp_prix.setObjectifsRemportesA(infos.getFirst().getSecond());
		this.jp_prix.setObjectifsRemportesB(infos.getSecond().getSecond());
		
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
		
		// Transmettre les données au collector
		if(!erreur) {
			// Récupérer la liste des prix remportés
			if(resultatA == InfosModelParticipation.RESULTAT_ATTENTE || resultatB == InfosModelParticipation.RESULTAT_ATTENTE) {
				this.jp_prix.clear();
			} else {
				this.jp_prix.collect();
			}
			TrackableList<Pair<String, InfosModelObjectifRemporte>> objectifsRemportesA = this.jp_prix.getObjectifsRemportesA();
			TrackableList<Pair<String, InfosModelObjectifRemporte>> objectifsRemportesB = this.jp_prix.getObjectifsRemportesB();
			
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
				JComboBox<String> jcb_event = event.getSource() == this.jcb_resultatA ? this.jcb_resultatA : this.jcb_resultatB;
				JComboBox<String> jcb_other = event.getSource() == this.jcb_resultatA ? this.jcb_resultatB : this.jcb_resultatA;
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
						
				}
			}
		}
	}

	/**
	 * @see ChangeListener#stateChanged(ChangeEvent)
	 */
	@Override
	public void stateChanged (ChangeEvent event) {
		if(this.jcb_date.isSelected() != this.jp_datePicker.isVisible()) {
			this.jp_datePicker.setVisible(this.jcb_date.isSelected());
			this.pack();
		}
	}
	
}
