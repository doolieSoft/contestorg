package views;

import infos.InfosModelCategorie;
import infos.InfosModelConcours;
import infos.InfosModelEquipe;
import infos.InfosModelPoule;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
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

import common.TrackableList;
import common.Pair;

import controlers.ContestOrg;

@SuppressWarnings("serial")
public class JDPoules extends JDPattern implements ItemListener, ChangeListener
{
	
	// Poules et affectations
	private ArrayList<Pair<String, TrackableList<Pair<InfosModelPoule, ArrayList<String>>>>> categoriesPoulesEquipes = new ArrayList<Pair<String,TrackableList<Pair<InfosModelPoule,ArrayList<String>>>>>();
	
	// Entrées
	private JComboBox jcb_categorie = new JComboBox();
	private JTextField jtf_nbEquipesCategorie = new JTextField(5);
	private JSpinner js_nbEquipesMaxPoule = new JSpinner(new SpinnerNumberModel(0, 0, 9999, 1));
	private JSpinner js_nbPoulesCategorie = new JSpinner(new SpinnerNumberModel(0, 0, 9999, 1));
	private JComboBox jcb_poule = new JComboBox();
	
	// Tableaux
	private TMPoules tm_poules;
	
	private JTable jt_equipesDisponibles;
	private TMString tm_equipesDisponibles = new TMString("Nom");
	
	private JTable jt_equipesPoule;
	private TMString tm_equipesPoule = new TMString("Nom");

	// Boutons
	private JButton jb_creerPoules = new JButton("Création automatique",new ImageIcon("img/farm/16x16/control_play_blue.png"));
	private JButton jb_affecterEquipes = new JButton("Affectation automatique",new ImageIcon("img/farm/16x16/control_play_blue.png"));
	private JButton jb_ajouterEquipe = new JButton("Ajouter",new ImageIcon("img/farm/16x16/add.png"));
	private JButton jb_retirerEquipe = new JButton("Retirer",new ImageIcon("img/farm/16x16/delete.png"));
	
	// Constructeur
	public JDPoules(Window w_parent) {
		// Appeller le constructeur du parent
		super(w_parent, "Gestion des poules");
		
		// Récupérer les poules et les affectations
		for(Pair<InfosModelCategorie, ArrayList<Pair<InfosModelPoule, ArrayList<InfosModelEquipe>>>> categorie : ContestOrg.get().getCtrlEquipes().getListeCategoriesPoulesEquipes()) {
			int nbEquipes = 0;
			ArrayList<Pair<InfosModelPoule,ArrayList<String>>> poules = new ArrayList<Pair<InfosModelPoule,ArrayList<String>>>();
			for(Pair<InfosModelPoule, ArrayList<InfosModelEquipe>> poule : categorie.getSecond()) {
				ArrayList<String> equipes = new ArrayList<String>();
				for(InfosModelEquipe equipe : poule.getSecond()) {
					equipes.add(equipe.getNom());
				}
				poules.add(new Pair<InfosModelPoule, ArrayList<String>>(poule.getFirst(), equipes));
				nbEquipes += equipes.size();
			}
			if(nbEquipes >= 4) {
				TrackableList<Pair<InfosModelPoule, ArrayList<String>>> list = new TrackableList<Pair<InfosModelPoule,ArrayList<String>>>(poules);
				list.addValidator(ContestOrg.get().getPoulesValidator());
				this.categoriesPoulesEquipes.add(new Pair<String, TrackableList<Pair<InfosModelPoule,ArrayList<String>>>>(categorie.getFirst().getNom(),list));
			}
		}
		
		// Catégorie
		if(categoriesPoulesEquipes.size() > 1) {
			this.jp_contenu.add(ViewHelper.title("Catégorie", ViewHelper.H1));
			this.jp_contenu.add(this.jcb_categorie);
			this.jp_contenu.add(Box.createVerticalStrut(5));
		}
		
		for(Pair<String, TrackableList<Pair<InfosModelPoule, ArrayList<String>>>> categorie : this.categoriesPoulesEquipes) {
			this.jcb_categorie.addItem(categorie.getFirst());
		}
		
		// Création des poules
		this.jp_contenu.add(ViewHelper.title("Création des poules", ViewHelper.H1));
		
		// Automatique
		this.jtf_nbEquipesCategorie.setEditable(false);
		JLabel[] jls_creation = {
			new JLabel(ContestOrg.get().getTypeParticipants() == InfosModelConcours.PARTICIPANTS_EQUIPES ? "Nombre d'équipes de la catégorie : " : "Nombre de joueurs de la catégorie : "),
			new JLabel(ContestOrg.get().getTypeParticipants() == InfosModelConcours.PARTICIPANTS_EQUIPES ? "Nombre d'équipes maximal par poule : " : "Nombre de joueurs maximal par poule : "),
			new JLabel("Nombre de poules dans la catégorie : ")
		};
		JComponent[] jcs_creation = {this.jtf_nbEquipesCategorie, this.js_nbEquipesMaxPoule, this.js_nbPoulesCategorie };
		this.jp_contenu.add(ViewHelper.inputs(jls_creation, jcs_creation));
		
		this.jp_contenu.add(ViewHelper.left(this.jb_creerPoules));
		this.jp_contenu.add(Box.createVerticalStrut(5));
		
		// Manuelle
		this.tm_poules = new TMPoules(this);
		this.tm_poules.addValidator(ContestOrg.get().getPoulesValidator());
		this.jp_contenu.add(new JPTable<Pair<InfosModelPoule, ArrayList<String>>>(this, this.tm_poules, true, true, true, true, true, 5));
		this.jp_contenu.add(Box.createVerticalStrut(5));
		
		// Affectation des équipes
		this.jp_contenu.add(ViewHelper.title(ContestOrg.get().getTypeParticipants() == InfosModelConcours.PARTICIPANTS_EQUIPES ? "Affectation des équipes" : "Affectation des joueurs", ViewHelper.H1));
		
		// Automatique
		this.jp_contenu.add(ViewHelper.left(this.jb_affecterEquipes));
		this.jp_contenu.add(Box.createVerticalStrut(5));
		
		// Manuelle
		this.jp_contenu.add(ViewHelper.title("Poule", ViewHelper.H2));
		this.jp_contenu.add(this.jcb_poule);
		this.jp_contenu.add(Box.createVerticalStrut(5));

		this.jcb_poule.setModel(this.tm_poules);
		
		JPanel jp_affectation = new JPanel(new GridLayout(1,2));
		this.jp_contenu.add(jp_affectation);

		this.jt_equipesDisponibles = new JTable(this.tm_equipesDisponibles);
		this.jt_equipesPoule = new JTable(this.tm_equipesPoule);
		
		JPanel jp_equipesDisponibles = new JPanel();
		jp_equipesDisponibles.setBorder(new EmptyBorder(0, 0, 0, 2));
		jp_equipesDisponibles.setLayout(new BoxLayout(jp_equipesDisponibles, BoxLayout.Y_AXIS));
		jp_equipesDisponibles.add(ViewHelper.title((ContestOrg.get().getTypeParticipants() == InfosModelConcours.PARTICIPANTS_EQUIPES ? "Equipes" : "Joueurs")+" disponibles", ViewHelper.H2));
		jp_equipesDisponibles.add(new JScrollPane(this.jt_equipesDisponibles));
		jp_equipesDisponibles.add(Box.createVerticalStrut(5));
		jp_equipesDisponibles.add(ViewHelper.left(this.jb_ajouterEquipe));
		jp_affectation.add(jp_equipesDisponibles);
		
		JPanel jp_equipesPoule = new JPanel();
		jp_equipesPoule.setBorder(new EmptyBorder(0, 2, 0, 0));
		jp_equipesPoule.setLayout(new BoxLayout(jp_equipesPoule, BoxLayout.Y_AXIS));
		jp_equipesPoule.add(ViewHelper.title((ContestOrg.get().getTypeParticipants() == InfosModelConcours.PARTICIPANTS_EQUIPES ? "Equipes" : "Joueurs")+" de la poule", ViewHelper.H2));
		jp_equipesPoule.add(new JScrollPane(this.jt_equipesPoule));
		jp_equipesPoule.add(Box.createVerticalStrut(5));
		jp_equipesPoule.add(ViewHelper.left(this.jb_retirerEquipe));
		jp_affectation.add(jp_equipesPoule);
		
		this.jp_contenu.add(Box.createVerticalStrut(5));
		this.jp_contenu.add(ViewHelper.pinformation("L'affectation automatique des "+(ContestOrg.get().getTypeParticipants() == InfosModelConcours.PARTICIPANTS_EQUIPES ? "équipes" : "joueurs")+" s'effectue de manière totalement aléatoire."));
		
		// Redimensionner les tableaux
		int height, row;
	    for(height=0, row=0; row< 6; row++) {
	    	height += this.jt_equipesDisponibles.getRowHeight(row);
	    }
		this.jt_equipesDisponibles.setPreferredScrollableViewportSize(new Dimension(250, height));
	    for(height=0, row=0; row< 6; row++) {
	    	height += this.jt_equipesPoule.getRowHeight(row);
	    }
		this.jt_equipesPoule.setPreferredScrollableViewportSize(new Dimension(250, height));
		
		// Ecouter les champs
		this.js_nbEquipesMaxPoule.addChangeListener(this);
		this.js_nbPoulesCategorie.addChangeListener(this);
		
		// Ecouter les listes
		this.jcb_categorie.addItemListener(this);
		this.jcb_poule.addItemListener(this);
		
		// Ecouter les boutons
		this.jb_creerPoules.addActionListener(this);
		this.jb_affecterEquipes.addActionListener(this);
		this.jb_ajouterEquipe.addActionListener(this);
		this.jb_retirerEquipe.addActionListener(this);
		
		// Rafraichir les champs
		this.refreshTop();
		this.refreshBottom();
		
		// Pack
		this.pack();
	}

	// Implémentation de ok
	@Override
	protected void ok () {
		// Transmettre les poules au controleur
		ContestOrg.get().getCtrlEquipes().updatePoules(this.categoriesPoulesEquipes);
		
		// Masquer la fenetre
		this.setVisible(false);
	}
	
	// Implémentation de quit
	@Override
	protected void quit () {
		// Masquer la fenetre
		this.setVisible(false);
	}

	// Implémentation de ItemListener
	@Override
	public void itemStateChanged (ItemEvent event) {
		if(event.getSource() == this.jcb_categorie) {
			// Rafraichir les champs du haut
			this.refreshTop();
		}
		
		// Rafraichir les champs du bas
		this.refreshBottom();
	}
	
	// Rafraichir les champs en fonction de la catégorie et de la poule séléctionnée
	private void refreshTop() {
		// Se retirer des écouteurs des spinners
		this.js_nbEquipesMaxPoule.removeChangeListener(this);
		this.js_nbPoulesCategorie.removeChangeListener(this);
		
		// Nombre de poules et d'équipes dans la catégorie
		int nbEquipesCategorie = 0;
		Pair<String, TrackableList<Pair<InfosModelPoule, ArrayList<String>>>> categorie = this.categoriesPoulesEquipes.get(this.jcb_categorie.getSelectedIndex());
		for(Pair<InfosModelPoule, ArrayList<String>> poule : categorie.getSecond()) {
			nbEquipesCategorie += poule.getSecond().size();
		}
		int nbPoulesCategorie = categorie.getSecond().size();
		this.jtf_nbEquipesCategorie.setText(String.valueOf(nbEquipesCategorie));
		this.js_nbPoulesCategorie.setValue(nbPoulesCategorie-1);
		
		// Nombre d'équipes maximal par poule
		if(nbPoulesCategorie > 1) {
			this.js_nbEquipesMaxPoule.setValue(new Double(Math.ceil((double)nbEquipesCategorie/(nbPoulesCategorie-1))).intValue());
		} else {
			this.js_nbEquipesMaxPoule.setValue(0);
		}
		
		// Ecouter à nouveau les spinners
		this.js_nbEquipesMaxPoule.addChangeListener(this);
		this.js_nbPoulesCategorie.addChangeListener(this);
		
		// Lier la liste des poules au tableau
		this.tm_poules.link(this.categoriesPoulesEquipes.get(this.jcb_categorie.getSelectedIndex()).getSecond());
	}
	private void refreshBottom() {
		// Lier la liste des équipes disponibles
		this.tm_equipesDisponibles.link(this.categoriesPoulesEquipes.get(this.jcb_categorie.getSelectedIndex()).getSecond().get(0).getSecond());
		
		// Forcer le repaint du tableau
		this.jt_equipesDisponibles.revalidate();
		
		// Lier la liste des équipes dans la poule
		if(this.categoriesPoulesEquipes.get(this.jcb_categorie.getSelectedIndex()).getSecond().size() > 1) {
			this.tm_equipesPoule.link(this.categoriesPoulesEquipes.get(this.jcb_categorie.getSelectedIndex()).getSecond().get(this.jcb_poule.getSelectedIndex()+1).getSecond());
		} else {
			this.tm_equipesPoule.link(new ArrayList<String>());
		}
	}
	
	// Surcharge de actionPerformed
	public void actionPerformed(ActionEvent event) {
		// Récupérer les poules
		TrackableList<Pair<InfosModelPoule, ArrayList<String>>> poules = this.categoriesPoulesEquipes.get(this.jcb_categorie.getSelectedIndex()).getSecond();
		
		if(event.getSource() == this.jb_creerPoules) {
			// Récupérer le nombre de poules à créer
			int nbPoules = (Integer)this.js_nbPoulesCategorie.getValue();
			
			// Vérifier si le nombre de poules est correcte
			if(nbPoules != 0) {
				if(new Double(Math.ceil(Integer.parseInt(this.jtf_nbEquipesCategorie.getText())/nbPoules)).intValue() >= 2) {
					// Supprimer toutes les poules excepté la première et placer leurs équipes dans la première poule
					while(poules.size() > 1) {
						// Placer les équipes de la poule dans la première poule
						poules.get(0).getSecond().addAll(poules.get(1).getSecond());
						
						// Supprimer la poule
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
		} else if(event.getSource() == this.jb_affecterEquipes) {
			// Vérifier si le nombre de poules est correcte
			if(poules.size() > 1) {
				// Récupérer la liste des équipes disponibles
				ArrayList<String> equipes = new ArrayList<String>();
				for(Pair<InfosModelPoule, ArrayList<String>> poule : poules) {
					while(poule.getSecond().size() != 0) {
						equipes.add(poule.getSecond().remove(0));
					}
				}
				
				// Mélanger les équipes
				Collections.shuffle(equipes);
				
				// Répartir les équipes dans les poules
				for(int i=0;equipes.size()>0;i=(i+1)%poules.size()) {
					if(i != 0) {
						// Transférer l'équipe
						poules.get(i).getSecond().add(equipes.remove(0));
						
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
		} else if(event.getSource() == this.jb_ajouterEquipe) {
			// Vérifier le nombre de lignes séléctionnées
			if(this.jt_equipesDisponibles.getSelectedRowCount() > 0) {
				// Vérifier si le nombre de poules est correcte
				if(poules.size() > 1) {
					// Récupérer les index des équipes séléctionnées et inverser la liste
					int[] indexes = this.jt_equipesDisponibles.getSelectedRows();
					for(int i=0;i<indexes.length/2;i++) {
						int swap = indexes[i];
						indexes[i] = indexes[indexes.length-i-1];
						indexes[indexes.length-i-1] = swap;
					}
					
					// Ajouter les équipes séléctionnées
					for(int index : indexes) {
						// Transférer l'équipe
						this.tm_equipesPoule.add(this.tm_equipesDisponibles.remove(index));
						
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
				ViewHelper.derror(this, "Veuillez séléctionner l'équipe que vous désirez ajouter.");
			}
		} else if(event.getSource() == this.jb_retirerEquipe) {
			// Vérifier le nombre de lignes séléctionnées
			if(this.jt_equipesPoule.getSelectedRowCount() > 0) {
				// Récupérer les index des équipes séléctionnées et inverse la liste
				int[] indexes = this.jt_equipesPoule.getSelectedRows();
				for(int i=0;i<indexes.length/2;i++) {
					int swap = indexes[i];
					indexes[i] = indexes[indexes.length-i-1];
					indexes[indexes.length-i-1] = swap;
				}
				
				// Retirer les équipes séléctionnées
				for(int index : indexes) {
					// Transférer l'équipe
					this.tm_equipesDisponibles.add(this.tm_equipesPoule.remove(index));
					
					// Informer la liste des poules du transfert
					poules.update(0);
					poules.update(this.jcb_poule.getSelectedIndex()+1);
					
				}
			} else {
				// Erreur
				ViewHelper.derror(this, "Veuillez séléctionner l'équipe que vous désirez retirer.");
			}
		} else {
			// Appeller le actionPerformed du parent
			super.actionPerformed(event);
		}
	}

	// Implémentation de ChangeListener
	@Override
	public void stateChanged (ChangeEvent event) {
		// Nombre d'équipes dans la catégorie
		int nbEquipesCategorie = 0;
		for(Pair<InfosModelPoule, ArrayList<String>> poule : this.categoriesPoulesEquipes.get(this.jcb_categorie.getSelectedIndex()).getSecond()) {
			nbEquipesCategorie += poule.getSecond().size();
		}
		
		// Se retirer des écouteurs des spinners
		this.js_nbEquipesMaxPoule.removeChangeListener(this);
		this.js_nbPoulesCategorie.removeChangeListener(this);
		
		// Vérifier la source de l'évenement
		if(event.getSource() == this.js_nbEquipesMaxPoule) {
			if((Integer)this.js_nbEquipesMaxPoule.getValue() != 0) {
				this.js_nbPoulesCategorie.setValue(new Double(Math.ceil((double)nbEquipesCategorie/((Integer)this.js_nbEquipesMaxPoule.getValue()))).intValue());
			} else {
				this.js_nbPoulesCategorie.setValue(0);
			}
		} else if(event.getSource() == this.js_nbPoulesCategorie) {
			if((Integer)this.js_nbPoulesCategorie.getValue() != 0) {
				this.js_nbEquipesMaxPoule.setValue(new Double(Math.ceil((double)nbEquipesCategorie/((Integer)this.js_nbPoulesCategorie.getValue()))).intValue());
			} else {
				this.js_nbEquipesMaxPoule.setValue(0);
			}
		}
		
		// Ecouter à nouveau les spinners
		this.js_nbEquipesMaxPoule.addChangeListener(this);
		this.js_nbPoulesCategorie.addChangeListener(this);
	}
	
}
