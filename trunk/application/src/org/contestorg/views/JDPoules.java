package org.contestorg.views;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import org.contestorg.common.Pair;
import org.contestorg.common.TrackableList;
import org.contestorg.controllers.ContestOrg;
import org.contestorg.infos.InfosModelCategorie;
import org.contestorg.infos.InfosModelConcours;
import org.contestorg.infos.InfosModelParticipant;
import org.contestorg.infos.InfosModelPoule;

/**
 * Boîte de dialogue d'édition des poules
 */
@SuppressWarnings("serial")
public class JDPoules extends JDPattern implements ItemListener, ChangeListener
{
	
	/** Liste des poules et leurs participants */
	private ArrayList<Pair<String, TrackableList<Pair<InfosModelPoule, ArrayList<String>>>>> categoriesPoulesParticipants = new ArrayList<Pair<String,TrackableList<Pair<InfosModelPoule,ArrayList<String>>>>>();
	
	// Entrées
	
	/** Catégorie */
	private JComboBox<String> jcb_categorie = new JComboBox<String>();
	
	/** Nombre de participants de la catégorie */
	private JTextField jtf_nbParticipantsCategorie = new JTextField(5);
	
	/** Nombre de participants maximal par poule */
	private JSpinner js_nbParticipantsMaxPoule = new JSpinner(new SpinnerNumberModel(0, 0, 9999, 1));
	
	/** Nombre de poules de la catégorie */
	private JSpinner js_nbPoulesCategorie = new JSpinner(new SpinnerNumberModel(0, 0, 9999, 1));
	
	/** Poule */
	private JComboBox<String> jcb_poule = new JComboBox<String>();
	
	// Tableaux
	
	/** TableModel des poules */
	private TMPoules tm_poules;
	
	/** Tableau des participants disponibles */
	private JTable jt_participantsDisponibles;
	/** TableModel des participants disponibles */
	private TMString tm_participantsDisponibles = new TMString("Nom");
	
	/** Tableau des participants de la poule */
	private JTable jt_participantsPoule;
	/** TableModel des participants de la poule */
	private TMString tm_participantsPoule = new TMString("Nom");

	// Boutons
	
	/** Bouton "Création automatique" */
	private JButton jb_creerPoules = new JButton("Création automatique",new ImageIcon("img/farm/16x16/control_play_blue.png"));
	
	/** Bouton "Affectation automatique" */
	private JButton jb_affecterParticipants = new JButton("Affectation automatique",new ImageIcon("img/farm/16x16/control_play_blue.png"));
	
	/** Bouton "Ajouter" */
	private JButton jb_ajouterParticipant = new JButton("Ajouter",new ImageIcon("img/farm/16x16/add.png"));
	
	/** Bouton "Retirer" */
	private JButton jb_retirerParticipant = new JButton("Retirer",new ImageIcon("img/farm/16x16/delete.png"));
	
	/**
	 * Constructeur
	 * @param w_parent fenêtre parent
	 */
	public JDPoules(Window w_parent) {
		// Appeller le constructeur du parent
		super(w_parent, "Gestion des poules");
		
		// Récupérer les poules et les affectations
		for(Pair<InfosModelCategorie, ArrayList<Pair<InfosModelPoule, ArrayList<InfosModelParticipant>>>> categorie : ContestOrg.get().getCtrlParticipants().getListeCategoriesPoulesParticipants()) {
			int nbParticipants = 0;
			ArrayList<Pair<InfosModelPoule,ArrayList<String>>> poules = new ArrayList<Pair<InfosModelPoule,ArrayList<String>>>();
			for(Pair<InfosModelPoule, ArrayList<InfosModelParticipant>> poule : categorie.getSecond()) {
				ArrayList<String> participants = new ArrayList<String>();
				for(InfosModelParticipant participant : poule.getSecond()) {
					participants.add(participant.getNom());
				}
				poules.add(new Pair<InfosModelPoule, ArrayList<String>>(poule.getFirst(), participants));
				nbParticipants += participants.size();
			}
			if(nbParticipants >= 4) {
				TrackableList<Pair<InfosModelPoule, ArrayList<String>>> list = new TrackableList<Pair<InfosModelPoule,ArrayList<String>>>(poules);
				list.addValidator(ContestOrg.get().getPoulesValidator());
				this.categoriesPoulesParticipants.add(new Pair<String, TrackableList<Pair<InfosModelPoule,ArrayList<String>>>>(categorie.getFirst().getNom(),list));
			}
		}
		
		// Catégorie
		if(this.categoriesPoulesParticipants.size() > 1) {
			this.jp_contenu.add(ViewHelper.title("Catégorie", ViewHelper.H1));
			this.jp_contenu.add(this.jcb_categorie);
			this.jp_contenu.add(Box.createVerticalStrut(5));
		}
		
		for(Pair<String, TrackableList<Pair<InfosModelPoule, ArrayList<String>>>> categorie : this.categoriesPoulesParticipants) {
			this.jcb_categorie.addItem(categorie.getFirst());
		}
		
		// Création des poules
		this.jp_contenu.add(ViewHelper.title("Création des poules", ViewHelper.H1));
		
		// Automatique
		this.jtf_nbParticipantsCategorie.setEditable(false);
		JLabel[] jls_creation = {
			new JLabel(ContestOrg.get().getTypeParticipants() == InfosModelConcours.PARTICIPANTS_EQUIPES ? "Nombre d'équipes de la catégorie : " : "Nombre de joueurs de la catégorie : "),
			new JLabel(ContestOrg.get().getTypeParticipants() == InfosModelConcours.PARTICIPANTS_EQUIPES ? "Nombre d'équipes maximal par poule : " : "Nombre de joueurs maximal par poule : "),
			new JLabel("Nombre de poules dans la catégorie : ")
		};
		JComponent[] jcs_creation = {this.jtf_nbParticipantsCategorie, this.js_nbParticipantsMaxPoule, this.js_nbPoulesCategorie };
		this.jp_contenu.add(ViewHelper.inputs(jls_creation, jcs_creation));
		
		this.jp_contenu.add(ViewHelper.left(this.jb_creerPoules));
		this.jp_contenu.add(Box.createVerticalStrut(5));
		
		// Manuelle
		this.tm_poules = new TMPoules(this);
		this.tm_poules.addValidator(ContestOrg.get().getPoulesValidator());
		this.jp_contenu.add(new JPTable<Pair<InfosModelPoule, ArrayList<String>>>(this, this.tm_poules, true, true, true, true, true, 5));
		this.jp_contenu.add(Box.createVerticalStrut(5));

		this.tm_poules.addTableModelListener(new TableModelListener() {
			@Override
			public void tableChanged (TableModelEvent event) {
				refreshBottom();
			}
		});
		
		// Affectation des participants
		this.jp_contenu.add(ViewHelper.title(ContestOrg.get().getTypeParticipants() == InfosModelConcours.PARTICIPANTS_EQUIPES ? "Affectation des équipes" : "Affectation des joueurs", ViewHelper.H1));
		
		// Automatique
		this.jp_contenu.add(ViewHelper.left(this.jb_affecterParticipants));
		this.jp_contenu.add(Box.createVerticalStrut(5));
		
		// Manuelle
		this.jp_contenu.add(ViewHelper.title("Poule", ViewHelper.H2));
		this.jp_contenu.add(this.jcb_poule);
		this.jp_contenu.add(Box.createVerticalStrut(5));

		this.jcb_poule.setModel(this.tm_poules);
		
		JPanel jp_affectation = new JPanel(new GridLayout(1,2));
		this.jp_contenu.add(jp_affectation);

		this.jt_participantsDisponibles = new JTable(this.tm_participantsDisponibles);
		this.jt_participantsPoule = new JTable(this.tm_participantsPoule);
		
		JPanel jp_participantsDisponibles = new JPanel();
		jp_participantsDisponibles.setBorder(new EmptyBorder(0, 0, 0, 2));
		jp_participantsDisponibles.setLayout(new BoxLayout(jp_participantsDisponibles, BoxLayout.Y_AXIS));
		jp_participantsDisponibles.add(ViewHelper.title((ContestOrg.get().getTypeParticipants() == InfosModelConcours.PARTICIPANTS_EQUIPES ? "Equipes" : "Joueurs")+" disponibles", ViewHelper.H2));
		jp_participantsDisponibles.add(new JScrollPane(this.jt_participantsDisponibles));
		jp_participantsDisponibles.add(Box.createVerticalStrut(5));
		jp_participantsDisponibles.add(ViewHelper.left(this.jb_ajouterParticipant));
		jp_affectation.add(jp_participantsDisponibles);
		
		JPanel jp_participantsPoule = new JPanel();
		jp_participantsPoule.setBorder(new EmptyBorder(0, 2, 0, 0));
		jp_participantsPoule.setLayout(new BoxLayout(jp_participantsPoule, BoxLayout.Y_AXIS));
		jp_participantsPoule.add(ViewHelper.title((ContestOrg.get().getTypeParticipants() == InfosModelConcours.PARTICIPANTS_EQUIPES ? "Equipes" : "Joueurs")+" de la poule", ViewHelper.H2));
		jp_participantsPoule.add(new JScrollPane(this.jt_participantsPoule));
		jp_participantsPoule.add(Box.createVerticalStrut(5));
		jp_participantsPoule.add(ViewHelper.left(this.jb_retirerParticipant));
		jp_affectation.add(jp_participantsPoule);
		
		this.jp_contenu.add(Box.createVerticalStrut(5));
		this.jp_contenu.add(ViewHelper.pinformation("L'affectation automatique des "+(ContestOrg.get().getTypeParticipants() == InfosModelConcours.PARTICIPANTS_EQUIPES ? "équipes" : "joueurs")+" s'effectue de manière totalement aléatoire."));
		
		// Redimensionner les tableaux
		int height, row;
	    for(height=0, row=0; row< 6; row++) {
	    	height += this.jt_participantsDisponibles.getRowHeight(row);
	    }
		this.jt_participantsDisponibles.setPreferredScrollableViewportSize(new Dimension(250, height));
	    for(height=0, row=0; row< 6; row++) {
	    	height += this.jt_participantsPoule.getRowHeight(row);
	    }
		this.jt_participantsPoule.setPreferredScrollableViewportSize(new Dimension(250, height));
		
		// Ecouter les champs
		this.js_nbParticipantsMaxPoule.addChangeListener(this);
		this.js_nbPoulesCategorie.addChangeListener(this);
		
		// Ecouter les listes
		this.jcb_categorie.addItemListener(this);
		this.jcb_poule.addItemListener(this);
		
		// Ecouter les boutons
		this.jb_creerPoules.addActionListener(this);
		this.jb_affecterParticipants.addActionListener(this);
		this.jb_ajouterParticipant.addActionListener(this);
		this.jb_retirerParticipant.addActionListener(this);
		
		// Rafraichir les champs
		this.refreshTop();
		this.refreshBottom();
		
		// Pack
		this.pack();
	}

	/**
	 * @see JDPattern#ok()
	 */
	@Override
	protected void ok () {
		// Transmettre les poules au controleur
		ContestOrg.get().getCtrlParticipants().updatePoules(this.categoriesPoulesParticipants);
		
		// Masquer la fenêtre
		this.setVisible(false);
	}
	
	/**
	 * @see JDPattern#quit()
	 */
	@Override
	protected void quit () {
		// Masquer la fenêtre
		this.setVisible(false);
	}

	/**
	 * @see ItemListener#itemStateChanged(ItemEvent)
	 */
	@Override
	public void itemStateChanged (ItemEvent event) {
		if(event.getSource() == this.jcb_categorie) {
			// Rafraichir les champs du haut
			this.refreshTop();
		}
		
		// Rafraichir les champs du bas
		this.refreshBottom();
	}

	/**
	 * Rafraichir la liste des poules d'après la catégorie séléctionnée
	 */
	private void refreshTop() {
		// Se retirer des écouteurs des spinners
		this.js_nbParticipantsMaxPoule.removeChangeListener(this);
		this.js_nbPoulesCategorie.removeChangeListener(this);
		
		// Nombre de poules et de participants dans la catégorie
		int nbParticipantsCategorie = 0;
		Pair<String, TrackableList<Pair<InfosModelPoule, ArrayList<String>>>> categorie = this.categoriesPoulesParticipants.get(this.jcb_categorie.getSelectedIndex());
		for(Pair<InfosModelPoule, ArrayList<String>> poule : categorie.getSecond()) {
			nbParticipantsCategorie += poule.getSecond().size();
		}
		int nbPoulesCategorie = categorie.getSecond().size();
		this.jtf_nbParticipantsCategorie.setText(String.valueOf(nbParticipantsCategorie));
		this.js_nbPoulesCategorie.setValue(nbPoulesCategorie-1);
		
		// Nombre de participants maximal par poule
		if(nbPoulesCategorie > 1) {
			this.js_nbParticipantsMaxPoule.setValue(new Double(Math.ceil((double)nbParticipantsCategorie/(nbPoulesCategorie-1))).intValue());
		} else {
			this.js_nbParticipantsMaxPoule.setValue(0);
		}
		
		// Ecouter à nouveau les spinners
		this.js_nbParticipantsMaxPoule.addChangeListener(this);
		this.js_nbPoulesCategorie.addChangeListener(this);
		
		// Lier la liste des poules au tableau
		this.tm_poules.link(this.categoriesPoulesParticipants.get(this.jcb_categorie.getSelectedIndex()).getSecond());
	}
	
	/**
	 * Rafraichir les listes des participants d'après la catégorie et la poule séléctionnées
	 */
	private void refreshBottom() {
		// Lier la liste des participants disponibles
		this.tm_participantsDisponibles.link(this.categoriesPoulesParticipants.get(this.jcb_categorie.getSelectedIndex()).getSecond().get(0).getSecond());
		
		// Forcer le repaint du tableau
		this.jt_participantsDisponibles.revalidate();
		
		// Lier la liste des participants dans la poule
		if(this.categoriesPoulesParticipants.get(this.jcb_categorie.getSelectedIndex()).getSecond().size() > 1) {
			this.tm_participantsPoule.link(this.categoriesPoulesParticipants.get(this.jcb_categorie.getSelectedIndex()).getSecond().get(this.jcb_poule.getSelectedIndex()+1).getSecond());
		} else {
			this.tm_participantsPoule.link(new ArrayList<String>());
		}
	}
	
	/**
	 * @see ActionListener#actionPerformed(ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent event) {
		// Récupérer les poules
		TrackableList<Pair<InfosModelPoule, ArrayList<String>>> poules = this.categoriesPoulesParticipants.get(this.jcb_categorie.getSelectedIndex()).getSecond();
		
		if(event.getSource() == this.jb_creerPoules) {
			// Récupérer le nombre de poules à créer
			int nbPoules = (Integer)this.js_nbPoulesCategorie.getValue();
			
			// Vérifier si le nombre de poules est correcte
			if(nbPoules != 0) {
				if(new Double(Math.ceil(Integer.parseInt(this.jtf_nbParticipantsCategorie.getText())/nbPoules)).intValue() >= 2) {					
					// Supprimer toutes les poules excepté la première et placer leurs participants dans la première poule
					while(poules.size() > 1) {
						// Placer les participants de la poule dans la première poule
						poules.get(0).getSecond().addAll(poules.get(1).getSecond());
						
						// Supprimer la deuxième poule
						poules.remove(1);
					}
					
					// Créer les poules
					String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
					for(int i=0;i<nbPoules;i++) {
						// Trouver le numéro de la poule
						StringBuilder numero = new StringBuilder();
						int j = i;
						while(j >= 0) {
							numero.append(alphabet.charAt(j));
							j -= 26;
						}
						
						// Ajouter la poule
						poules.add(new Pair<InfosModelPoule, ArrayList<String>>(new InfosModelPoule("Poule "+numero), new ArrayList<String>()));
					}
					
					// Lier la nouvelle liste des poules au tableau
					this.tm_poules.link(poules);
				} else {
					// Erreur
					ViewHelper.derror(this, "Il faut au moins 2 "+(ContestOrg.get().getTypeParticipants() == InfosModelConcours.PARTICIPANTS_EQUIPES ? "équipes" : "joueurs")+" par poule.");
				}
			} else {
				// Erreur
				ViewHelper.derror(this, "Le nombre de poules ne peut pas être égal à zéro.");
			}
		} else if(event.getSource() == this.jb_affecterParticipants) {
			// Vérifier si le nombre de poules est correcte
			if(poules.size() > 1) {
				// Récupérer la liste des participants disponibles
				ArrayList<String> participants = new ArrayList<String>();
				for(Pair<InfosModelPoule, ArrayList<String>> poule : poules) {
					while(poule.getSecond().size() != 0) {
						participants.add(poule.getSecond().remove(0));
					}
				}
				
				// Mélanger les participants
				Collections.shuffle(participants);
				
				// Répartir les participants dans les poules
				for(int i=0;participants.size()>0;i=(i+1)%poules.size()) {
					if(i != 0) {
						// Transférer le participant
						poules.get(i).getSecond().add(participants.remove(0));
						
						// Informer la liste des poules du transfert
						poules.update(0);
						poules.update(i);
					}
				}
				
				// Rafraichir le bas des champs
				this.refreshBottom();
			} else {
				// Erreur
				ViewHelper.derror(this, "Il faut au moins une poule pour lancer l'affectation automatique.");
			}
		} else if(event.getSource() == this.jb_ajouterParticipant) {
			// Vérifier le nombre de lignes séléctionnées
			if(this.jt_participantsDisponibles.getSelectedRowCount() > 0) {
				// Vérifier si le nombre de poules est correcte
				if(poules.size() > 1) {
					// Récupérer les index des participants séléctionnés et inverser la liste
					int[] indexes = this.jt_participantsDisponibles.getSelectedRows();
					for(int i=0;i<indexes.length/2;i++) {
						int swap = indexes[i];
						indexes[i] = indexes[indexes.length-i-1];
						indexes[indexes.length-i-1] = swap;
					}
					
					// Ajouter les participants séléctionnés
					for(int index : indexes) {
						// Transférer le participant
						this.tm_participantsPoule.add(this.tm_participantsDisponibles.remove(index));
						
						// Informer la liste des poules du transfert
						poules.update(0);
						poules.update(this.jcb_poule.getSelectedIndex()+1);
					}
				} else {
					// Erreur
					ViewHelper.derror(this, "La poule de destination n'est pas précisé.");
				}
			} else {
				// Erreur
				ViewHelper.derror(this, "Veuillez séléctionner "+(ContestOrg.get().getTypeParticipants() == InfosModelConcours.PARTICIPANTS_EQUIPES ? "l'équipe" : "le joueur")+" que vous désirez ajouter.");
			}
		} else if(event.getSource() == this.jb_retirerParticipant) {
			// Vérifier le nombre de lignes séléctionnées
			if(this.jt_participantsPoule.getSelectedRowCount() > 0) {
				// Récupérer les index des participants séléctionnés et inverse la liste
				int[] indexes = this.jt_participantsPoule.getSelectedRows();
				for(int i=0;i<indexes.length/2;i++) {
					int swap = indexes[i];
					indexes[i] = indexes[indexes.length-i-1];
					indexes[indexes.length-i-1] = swap;
				}
				
				// Retirer les participants séléctionnés
				for(int index : indexes) {
					// Transférer le participant
					this.tm_participantsDisponibles.add(this.tm_participantsPoule.remove(index));
					
					// Informer la liste des poules du transfert
					poules.update(0);
					poules.update(this.jcb_poule.getSelectedIndex()+1);
				}
			} else {
				// Erreur
				ViewHelper.derror(this, "Veuillez séléctionner "+(ContestOrg.get().getTypeParticipants() == InfosModelConcours.PARTICIPANTS_EQUIPES ? "l'équipe" : "le joueur")+" que vous désirez retirer.");
			}
		} else {
			// Appeller le actionPerformed du parent
			super.actionPerformed(event);
		}
	}

	/**
	 * @see ChangeListener#stateChanged(ChangeEvent)
	 */
	@Override
	public void stateChanged (ChangeEvent event) {
		// Nombre de participants dans la catégorie
		int nbParticipantsCategorie = 0;
		for(Pair<InfosModelPoule, ArrayList<String>> poule : this.categoriesPoulesParticipants.get(this.jcb_categorie.getSelectedIndex()).getSecond()) {
			nbParticipantsCategorie += poule.getSecond().size();
		}
		
		// Se retirer des écouteurs des spinners
		this.js_nbParticipantsMaxPoule.removeChangeListener(this);
		this.js_nbPoulesCategorie.removeChangeListener(this);
		
		// Vérifier la source de l'évenement
		if(event.getSource() == this.js_nbParticipantsMaxPoule) {
			if((Integer)this.js_nbParticipantsMaxPoule.getValue() != 0) {
				this.js_nbPoulesCategorie.setValue(new Double(Math.ceil((double)nbParticipantsCategorie/((Integer)this.js_nbParticipantsMaxPoule.getValue()))).intValue());
			} else {
				this.js_nbPoulesCategorie.setValue(0);
			}
		} else if(event.getSource() == this.js_nbPoulesCategorie) {
			if((Integer)this.js_nbPoulesCategorie.getValue() != 0) {
				this.js_nbParticipantsMaxPoule.setValue(new Double(Math.ceil((double)nbParticipantsCategorie/((Integer)this.js_nbPoulesCategorie.getValue()))).intValue());
			} else {
				this.js_nbParticipantsMaxPoule.setValue(0);
			}
		}
		
		// Ecouter à nouveau les spinners
		this.js_nbParticipantsMaxPoule.addChangeListener(this);
		this.js_nbPoulesCategorie.addChangeListener(this);
	}
	
}
