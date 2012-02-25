package org.contestorg.views;

import java.awt.GridLayout;
import java.awt.Window;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.contestorg.common.Pair;
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
public class JDMatchPhasesEliminatoires extends JDPattern implements ItemListener
{
	/** Collecteur des informations du latch */
	private ICollector<Triple<Pair<TrackableList<Pair<String, InfosModelObjectifRemporte>>, InfosModelParticipation>, Pair<TrackableList<Pair<String, InfosModelObjectifRemporte>>, InfosModelParticipation>, InfosModelMatchPhasesElims>> collector;
	
	
	/** Résultat du participant A */
	protected JComboBox<String> jcb_resultatA;
	
	/** Résultat du participant B */
	protected JComboBox<String> jcb_resultatB;
	
	/** Panel des objectifs remportés */
	protected JPObjectifs jp_prix;
	
	/** Détails */
	protected JTextArea jta_details;
	
	/**
	 * Constructeur
	 * @param w_parent fenêtre parent
	 * @param collector collecteur des informations du match
	 * @param infos informations du match
	 * @param resultatsEditable résultats éditables ?
	 */
	public JDMatchPhasesEliminatoires(Window w_parent, ICollector<Triple<Pair<TrackableList<Pair<String, InfosModelObjectifRemporte>>, InfosModelParticipation>, Pair<TrackableList<Pair<String, InfosModelObjectifRemporte>>, InfosModelParticipation>, InfosModelMatchPhasesElims>> collector, Triple<Triple<String, ArrayList<Pair<String, InfosModelObjectifRemporte>>, InfosModelParticipation>, Triple<String, ArrayList<Pair<String, InfosModelObjectifRemporte>>, InfosModelParticipation>, InfosModelMatchPhasesElims> infos, boolean resultatsEditable) {
		// Appeller le constructeur du parent
		super(w_parent, "Editer un match");
		
		// Retenir le collector
		this.collector = collector;
		
		// Participants
		this.jp_contenu.add(ViewHelper.title(ContestOrg.get().getTypeParticipants() == InfosModelConcours.PARTICIPANTS_EQUIPES ? "Equipes" : "Joueurs", ViewHelper.H1));
		JPanel jp_participants = new JPanel(new GridLayout(1,2));
		
		JComboBox<String> jcb_participantA = new JComboBox<String>();
		jcb_participantA.addItem(infos.getFirst().getFirst());
		jcb_participantA.setEnabled(false);
		
		JComboBox<String> jcb_participantB = new JComboBox<String>();
		jcb_participantB.addItem(infos.getSecond().getFirst());
		jcb_participantB.setEnabled(false);
		
		jp_participants.add(jcb_participantA);
		jp_participants.add(jcb_participantB);
		
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
		
		// Détails
		this.jp_contenu.add(ViewHelper.title("Détails", ViewHelper.H1));
		
		this.jta_details = new JTextArea(infos.getThird().getDetails());
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
			this.collector.collect(new Triple<Pair<TrackableList<Pair<String,InfosModelObjectifRemporte>>,InfosModelParticipation>, Pair<TrackableList<Pair<String,InfosModelObjectifRemporte>>,InfosModelParticipation>, InfosModelMatchPhasesElims>(participationA , participationB, new InfosModelMatchPhasesElims(null,details)));
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
	
}
