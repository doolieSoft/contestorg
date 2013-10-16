package org.contestorg.views;

import java.awt.Component;
import java.awt.GridLayout;
import java.awt.Window;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.Box;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.sourceforge.jdatepicker.DateModel;
import net.sourceforge.jdatepicker.JDateComponentFactory;
import net.sourceforge.jdatepicker.JDatePanel;

import org.contestorg.common.Pair;
import org.contestorg.common.Quadruple;
import org.contestorg.common.TrackableList;
import org.contestorg.common.Triple;
import org.contestorg.controllers.ContestOrg;
import org.contestorg.infos.InfosModelConcours;
import org.contestorg.infos.InfosModelMatchPhasesQualifs;
import org.contestorg.infos.InfosModelObjectifRemporte;
import org.contestorg.infos.InfosModelParticipation;
import org.contestorg.interfaces.ICollector;

/**
 * Boîte de dialogue de création/édition d'un match des phases qualificatives
 */
@SuppressWarnings("serial")
public abstract class JDMatchPhasesQualifsAbstract extends JDPattern implements ItemListener, ChangeListener
{
	/** Collecteur des informations du match */
	private ICollector<Quadruple<Triple<String, TrackableList<Pair<String, InfosModelObjectifRemporte>>, InfosModelParticipation>, Triple<String, TrackableList<Pair<String, InfosModelObjectifRemporte>>, InfosModelParticipation>, Pair<String, String>, InfosModelMatchPhasesQualifs>> collector;
	
	// Entrées
	
	/** Nom de la catégorie de destination */
	private String nomCategorie;
	
	/** Nom de la poule de destination */
	private String nomPoule;
	
	/** Numéro de la phase qualificative de destination */
	private int numeroPhase;
	
	/** Participant A */
	protected JComboBox<String> jcb_participantA;
	
	/** Participant B */
	protected JComboBox<String> jcb_participantB;
	
	/** Résultat du participant A */
	protected JComboBox<String> jcb_resultatA;
	
	/** Résultat du participant B */
	protected JComboBox<String> jcb_resultatB;
	
	/** Panel des objectifs remportés */
	protected JPObjectifs jp_objectifs;
	
	/** Spécifier la date du match ? */
	protected JCheckBox jcb_date;
	
	/** Panel de la date */
	protected JDatePanel jdp_date;
	
	/** Modèle de la date */
	protected DateModel<Date> dm_date;
	
	/** Lieu et emplacement */
	protected JPLieuEmplacement jp_lieuEmplacement;
	
	/** Détails */
	protected JTextArea jta_details;
	
	// Index des résultats

	/** Attente */
	protected int index_attente;
	/** Victoire */
	protected int index_victoire;
	/** Egalité */
	protected int index_egalite;
	/** Défaite */
	protected int index_defaite;
	/** Forfait */
	protected int index_forfait;
	
	/**
	 * Constructeur
	 * @param w_parent fenêtre parent
	 * @param titre titre de la boîte de dialogue
	 * @param collector collecteur des informations du match
	 * @param nomCategorie nom de la catégorie de destination
	 * @param nomPoule nom de la poule de destination
	 * @param numeroPhase numéro de la phase qualificative de destination
	 */
	@SuppressWarnings("unchecked")
	public JDMatchPhasesQualifsAbstract(Window w_parent, String titre, ICollector<Quadruple<Triple<String, TrackableList<Pair<String, InfosModelObjectifRemporte>>, InfosModelParticipation>, Triple<String, TrackableList<Pair<String, InfosModelObjectifRemporte>>, InfosModelParticipation>, Pair<String, String>, InfosModelMatchPhasesQualifs>> collector, String nomCategorie, String nomPoule, int numeroPhase) {
		// Appeller le constructeur du parent
		super(w_parent, titre);
		
		// Retenir le collector, la catégorie, la poule et la phase qualificative
		this.collector = collector;
		this.nomCategorie = nomCategorie;
		this.nomPoule = nomPoule;
		this.numeroPhase = numeroPhase;
		
		// Participants
		this.jp_contenu.add(ViewHelper.title(ContestOrg.get().getCtrlParticipants().getTypeParticipants() == InfosModelConcours.PARTICIPANTS_EQUIPES ? "Equipes" : "Joueurs", ViewHelper.H1));
		
		ArrayList<String> participants = ContestOrg.get().getCtrlPhasesQualificatives().getListeParticipantsParticipants(this.nomCategorie,this.nomPoule);
		JPanel jp_participants = new JPanel(new GridLayout(1,2));
		this.jcb_participantA = new JComboBox<String>(participants.toArray(new String[participants.size()]));
		this.jcb_participantA.addItem(ContestOrg.get().getCtrlParticipants().getTypeParticipants() == InfosModelConcours.PARTICIPANTS_EQUIPES ? "Equipe fantome" : "Joueur fantome");
		this.jcb_participantB = new JComboBox<String>(participants.toArray(new String[participants.size()]));
		this.jcb_participantB.addItem(ContestOrg.get().getCtrlParticipants().getTypeParticipants() == InfosModelConcours.PARTICIPANTS_EQUIPES ? "Equipe fantome" : "Joueur fantome");
		jp_participants.add(this.jcb_participantA);
		jp_participants.add(this.jcb_participantB);
		this.jp_contenu.add(jp_participants);
		
		// Résultats
		this.jp_contenu.add(Box.createVerticalStrut(5));
		this.jp_contenu.add(ViewHelper.title("Résultats", ViewHelper.H1));
		
		JPanel jp_resultat = new JPanel(new GridLayout(1,2));
		if(ContestOrg.get().getCtrlPhasesQualificatives().isEgaliteActivee()) {
			String[] resultats = { "Attente", "Victoire", "Egalité", "Défaite", "Forfait" };
			this.jcb_resultatA = new JComboBox<String>(resultats);
			this.jcb_resultatB = new JComboBox<String>(resultats);
			this.index_attente = 0;
			this.index_victoire = 1;
			this.index_egalite = 2;
			this.index_defaite = 3;
			this.index_forfait = 4;
		} else {
			String[] resultats = { "Attente", "Victoire", "Défaite", "Forfait" };
			this.jcb_resultatA = new JComboBox<String>(resultats);
			this.jcb_resultatB = new JComboBox<String>(resultats);
			this.index_attente = 0;
			this.index_victoire = 1;
			this.index_egalite = -1;
			this.index_defaite = 2;
			this.index_forfait = 3;
		}
		jp_resultat.add(this.jcb_resultatA);
		jp_resultat.add(this.jcb_resultatB);
		this.jp_contenu.add(jp_resultat);

		this.jcb_resultatA.addItemListener(this);
		this.jcb_resultatB.addItemListener(this);
		
		// Objectifs remportés
		this.jp_contenu.add(Box.createVerticalStrut(5));
		this.jp_objectifs = new JPObjectifs();
		this.jp_contenu.add(this.jp_objectifs);
		
		// Date
		this.jp_contenu.add(ViewHelper.title("Date", ViewHelper.H1));
		
		this.jcb_date = new JCheckBox("Définir la date du match ?");
		this.jcb_date.setSelected(false);
		this.jcb_date.addChangeListener(this);
		this.jp_contenu.add(ViewHelper.left(this.jcb_date));
		
		this.dm_date = (DateModel<Date>)JDateComponentFactory.createDateModel(Date.class);
		this.jdp_date = JDateComponentFactory.createJDatePanel(this.dm_date);
		((JComponent)this.jdp_date).setVisible(false);
		this.jp_contenu.add((Component)jdp_date);
		
		// Lieu et emplacement
		this.jp_lieuEmplacement = new JPLieuEmplacement();
		this.jp_contenu.add(this.jp_lieuEmplacement);
		
		// Détails
		this.jp_contenu.add(ViewHelper.title("Détails", ViewHelper.H1));
		
		this.jta_details = new JTextArea();
		this.jta_details.setLineWrap(true);
		this.jta_details.setWrapStyleWord(true);
		this.jta_details.setRows(5);
		this.jp_contenu.add(new JScrollPane(this.jta_details));
	}
	
	/**
	 * Ajouter le résultat "égalité" (dans le cas où l'égalité est désactivée et si l'on souhaite quand même en disposer)
	 */
	protected void addResultatEgalite() {
		this.jcb_resultatA.insertItemAt("Egalité", 2);
		this.jcb_resultatB.insertItemAt("Egalité", 2);
		this.index_egalite = 2;
		this.index_defaite = 3;
		this.index_forfait = 4;
	}

	/**
	 * @see JDPattern#ok()
	 */
	@Override
	protected void ok () {
		// Récupérer les données
		String nomParticipantA = this.jcb_participantA.getSelectedIndex() == this.jcb_participantA.getItemCount()-1 ? null : (String)this.jcb_participantA.getSelectedItem();
		String nomParticipantB = this.jcb_participantB.getSelectedIndex() == this.jcb_participantB.getItemCount()-1 ? null : (String)this.jcb_participantB.getSelectedItem();
		int resultatA = 0;
		if(this.jcb_resultatA.getSelectedIndex() == this.index_attente) {
			resultatA = InfosModelParticipation.RESULTAT_ATTENTE;
		} else if(this.jcb_resultatA.getSelectedIndex() == this.index_victoire) {
			resultatA = InfosModelParticipation.RESULTAT_VICTOIRE;
		} else if(this.jcb_resultatA.getSelectedIndex() == this.index_egalite) {
			resultatA = InfosModelParticipation.RESULTAT_EGALITE;
		} else if(this.jcb_resultatA.getSelectedIndex() == this.index_defaite) {
			resultatA = InfosModelParticipation.RESULTAT_DEFAITE;
		} else if(this.jcb_resultatA.getSelectedIndex() == this.index_forfait) {
			resultatA = InfosModelParticipation.RESULTAT_FORFAIT;
		}
		int resultatB = 0;
		if(this.jcb_resultatB.getSelectedIndex() == this.index_attente) {
			resultatB = InfosModelParticipation.RESULTAT_ATTENTE;
		} else if(this.jcb_resultatB.getSelectedIndex() == this.index_victoire) {
			resultatB = InfosModelParticipation.RESULTAT_VICTOIRE;
		} else if(this.jcb_resultatB.getSelectedIndex() == this.index_egalite) {
			resultatB = InfosModelParticipation.RESULTAT_EGALITE;
		} else if(this.jcb_resultatB.getSelectedIndex() == this.index_defaite) {
			resultatB = InfosModelParticipation.RESULTAT_DEFAITE;
		} else if(this.jcb_resultatB.getSelectedIndex() == this.index_forfait) {
			resultatB = InfosModelParticipation.RESULTAT_FORFAIT;
		}
		String details = this.jta_details.getText().trim();
		Date date = this.jcb_date.isSelected() ? this.dm_date.getValue() : null;
		String nomLieu = this.jp_lieuEmplacement.getNomLieu();
		String nomEmplacement = this.jp_lieuEmplacement.getNomEmplacement();
		
		// Vérifier les données
		boolean erreur = false;
		if(nomParticipantA == null && nomParticipantB == null || nomParticipantA != null && nomParticipantA.equals(nomParticipantB)) {
			// Erreur
			erreur = true;
			ViewHelper.derror(this, ContestOrg.get().getCtrlParticipants().getTypeParticipants() == InfosModelConcours.PARTICIPANTS_EQUIPES ? "Une équipe ne peut pas avoir un match avec elle même." : "Un joueur ne peut pas avoir un match avec lui même.");
		} else if(this instanceof JDMatchPhasesQualifsCreer) {
			if(ContestOrg.get().getCtrlPhasesQualificatives().isParticipantPhaseQualif(this.nomCategorie,this.nomPoule,this.numeroPhase,nomParticipantA)) {
				if(!ViewHelper.confirmation(this, (ContestOrg.get().getCtrlParticipants().getTypeParticipants() == InfosModelConcours.PARTICIPANTS_EQUIPES ? "L'équipe" : "Le joueur")+" "+nomParticipantA+" participe déjà à la phase qualificative. Désirez-vous continuer ?")) {
					erreur = true;
				}
			}
			if(!erreur && ContestOrg.get().getCtrlPhasesQualificatives().isParticipantPhaseQualif(this.nomCategorie,this.nomPoule,this.numeroPhase,nomParticipantB)) {
				if(!ViewHelper.confirmation(this, (ContestOrg.get().getCtrlParticipants().getTypeParticipants() == InfosModelConcours.PARTICIPANTS_EQUIPES ? "L'équipe" : "Le joueur")+" "+nomParticipantB+" participe déjà à la phase qualificative. Désirez-vous continuer ?")) {
					erreur = true;
				}
			}
		}
		if(resultatA == InfosModelParticipation.RESULTAT_ATTENTE && resultatB != InfosModelParticipation.RESULTAT_ATTENTE || resultatA != InfosModelParticipation.RESULTAT_ATTENTE && resultatB == InfosModelParticipation.RESULTAT_ATTENTE ) {
			// Erreur
			erreur = true;
			ViewHelper.derror(this, "Le résultat du match n'est pas cohérent.");
		} else if(resultatA == InfosModelParticipation.RESULTAT_EGALITE && resultatB != InfosModelParticipation.RESULTAT_EGALITE || resultatA != InfosModelParticipation.RESULTAT_EGALITE && resultatB == InfosModelParticipation.RESULTAT_EGALITE) {
			// Erreur
			erreur = true;
			ViewHelper.derror(this, "Le résultat du match n'est pas cohérent.");
		}
		if(resultatA == InfosModelParticipation.RESULTAT_VICTOIRE && resultatB == InfosModelParticipation.RESULTAT_VICTOIRE) {
			// Erreur
			erreur = true;
			ViewHelper.derror(this, "Il ne peut y avoir qu'un seul vainqueur.");
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
				this.jp_objectifs.clear();
			} else {
				this.jp_objectifs.collect();
			}
			TrackableList<Pair<String, InfosModelObjectifRemporte>> objectifsRemportesA = this.jp_objectifs.getObjectifsRemportesA();
			TrackableList<Pair<String, InfosModelObjectifRemporte>> objectifsRemportesB = this.jp_objectifs.getObjectifsRemportesB();
			
			// Créer les informations de participation
			Triple<String, TrackableList<Pair<String, InfosModelObjectifRemporte>>, InfosModelParticipation> participationA = new Triple<String, TrackableList<Pair<String,InfosModelObjectifRemporte>>, InfosModelParticipation>(nomParticipantA, objectifsRemportesA, new InfosModelParticipation(resultatA));
			Triple<String, TrackableList<Pair<String, InfosModelObjectifRemporte>>, InfosModelParticipation> participationB = new Triple<String, TrackableList<Pair<String,InfosModelObjectifRemporte>>, InfosModelParticipation>(nomParticipantB, objectifsRemportesB, new InfosModelParticipation(resultatB));
			
			// Transmettre les données au collector
			this.collector.collect(new Quadruple<Triple<String,TrackableList<Pair<String,InfosModelObjectifRemporte>>,InfosModelParticipation>, Triple<String,TrackableList<Pair<String,InfosModelObjectifRemporte>>,InfosModelParticipation>, Pair<String,String>, InfosModelMatchPhasesQualifs>(participationA , participationB, new Pair<String,String>(nomLieu,nomEmplacement), new InfosModelMatchPhasesQualifs(date,details)));
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
			// Modifier la liste non modifiée si nécéssaire
			JComboBox<String> jcb_event = event.getSource() == this.jcb_resultatA ? this.jcb_resultatA : this.jcb_resultatB;
			JComboBox<String> jcb_other = event.getSource() == this.jcb_resultatA ? this.jcb_resultatB : this.jcb_resultatA;
			if(jcb_event.getSelectedIndex() == this.index_attente) {
				jcb_other.setSelectedIndex(this.index_attente);
			} else if(jcb_event.getSelectedIndex() == this.index_victoire) {
				if(jcb_other.getSelectedIndex() == this.index_victoire || jcb_other.getSelectedIndex() == this.index_attente || jcb_other.getSelectedIndex() == this.index_egalite) {
					jcb_other.setSelectedIndex(this.index_defaite);
				}
			} else if(jcb_event.getSelectedIndex() == this.index_egalite) {
				jcb_other.setSelectedIndex(this.index_egalite);
			} else if(jcb_event.getSelectedIndex() == this.index_defaite) {
				if(jcb_other.getSelectedIndex() == this.index_attente || jcb_other.getSelectedIndex() == this.index_egalite) {
					jcb_other.setSelectedIndex(this.index_victoire);
				}
			} else if(jcb_event.getSelectedIndex() == this.index_forfait) {
				if(jcb_other.getSelectedIndex() == this.index_attente || jcb_other.getSelectedIndex() == this.index_egalite) {
					jcb_other.setSelectedIndex(this.index_victoire);
				}
			}
		}
	}

	/**
	 * @see ChangeListener#stateChanged(ChangeEvent)
	 */
	@Override
	public void stateChanged (ChangeEvent event) {
		if(this.jcb_date.isSelected() != ((JComponent)this.jdp_date).isVisible()) {
			this.setDateVisible(this.jcb_date.isSelected());
		}
	}
	
	/**
	 * Définir si le calendrier est visible ou non
	 * @param visible calendrier visible ?
	 */
	protected void setDateVisible(boolean visible) {
		((JComponent)this.jdp_date).setVisible(visible);
		this.pack();
	}
	
}
