package org.contestorg.views;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTree;
import javax.swing.border.LineBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import org.contestorg.controllers.ContestOrg;
import org.contestorg.infos.InfosModelCategorie;
import org.contestorg.infos.InfosModelConcours;
import org.contestorg.infos.InfosModelMatchPhasesElims;
import org.contestorg.infos.InfosModelParticipant;
import org.contestorg.infos.InfosModelPhasesEliminatoires;
import org.contestorg.infos.InfosModelPoule;
import org.contestorg.infos.InfosTheme;
import org.contestorg.interfaces.IMoody;
import org.contestorg.interfaces.IMoodyListener;
import org.contestorg.interfaces.ITreeNode;

/**
 * Panel des phases éliminatoires pour la fenêtre principale
 */
@SuppressWarnings("serial")
public class JPPrincipalPhasesEliminatoires extends JPPrincipalAbstract implements TreeSelectionListener, ActionListener
{

	// Panneau du haut
	
	/** Liste des participants */
	private JComboBox jcb_participants;
	
	/** Bouton "Générer" */
	private JButton jb_generer;
	
	/** Bouton "Réinitialiser" */
	private JButton jb_reinitialiser;
	
	/** Bouton "Exporter" */
	private JButton jb_exporter;
	
	// Panneau de contenu
	
	/** Arborescence des catégories */
	private JTree jtree;
	
	/** Graphe de la grande finale */
	private JPGraphPhasesElims grapheGrandeFinale;
	
	/** Graphe de la grande finale */
	private JPGraphPhasesElims graphePetiteFinale;

	/**
	 * Constructeur
	 * @param w_parent fenêtre parent
	 */
	public JPPrincipalPhasesEliminatoires(Window w_parent) {
		// Appeller le constructeur du parent
		super(w_parent);
		
		// Ecouter le controleur principal
		ContestOrg.get().addListener(this);

		// Panneau du haut
		this.jcb_participants = new JComboBox();
		this.jb_generer = new JButton("Générer", new ImageIcon("img/farm/16x16/arrow_refresh.png"));
		this.jb_reinitialiser = new JButton("Réinitialiser", new ImageIcon("img/farm/16x16/arrow_undo.png"));
		this.jb_exporter = new JButton("Exporter", new ImageIcon("img/farm/16x16/application_go.png"));

		this.jcb_participants.addItem("04 participants");
		this.jcb_participants.addItem("08 participants");
		this.jcb_participants.addItem("16 participants");
		this.jcb_participants.addItem("32 participants");
		this.jcb_participants.addItem("64 participants");

		this.jcb_participants.setToolTipText("Nombre de participants aux phases éliminatoires dans la catégorie séléctionnée");
		this.jb_generer.setToolTipText("Générer les phases éliminatoires dans la catégorie séléctionnée");
		this.jb_reinitialiser.setToolTipText("Réinitialiser les phases éliminatoires de la catégorie séléctionnée");
		this.jb_exporter.setToolTipText("Exporter des informations sur les phases éliminatoires");

		this.jcb_participants.setEnabled(false);
		this.jb_generer.setEnabled(false);
		this.jb_reinitialiser.setEnabled(false);
		this.jb_exporter.setEnabled(false);

		this.jp_haut.add(this.jcb_participants);
		this.jp_haut.add(this.jb_generer);
		this.jp_haut.add(this.jb_reinitialiser);
		this.jp_haut.add(this.jb_exporter);
		
		// Panneau de contenu
		this.jtree = new JTree(ContestOrg.get().getCtrlPhasesEliminatoires().getTreeModelCategories());
		this.jtree.setCellRenderer(new TreeCellRendererParticipants());
		this.jtree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		this.jtree.addTreeSelectionListener(this);
		this.jtree.setBorder(new LineBorder(new Color(254, 254, 254), 4));
		this.jtree.setToggleClickCount(-1);
		
		JTabbedPane jtb_graphes = new JTabbedPane(JTabbedPane.BOTTOM);
		
		this.grapheGrandeFinale = new JPGraphPhasesElims(this.w_parent, true);
		jtb_graphes.addTab("Grande finale", this.grapheGrandeFinale);

		this.graphePetiteFinale = new JPGraphPhasesElims(this.w_parent, false);
		jtb_graphes.addTab("Petite finale", this.graphePetiteFinale);

		JScrollPane jsp = new JScrollPane(this.jtree);
		jsp.setPreferredSize(new Dimension(180,jsp.getPreferredSize().height));
		this.jp_contenu.add(new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, jsp, jtb_graphes));

		// Panneau du bas
		this.jp_bas.setVisible(false);
		
		// Ecouter les boutons
		this.jb_exporter.addActionListener(this);
		this.jb_generer.addActionListener(this);
		this.jb_reinitialiser.addActionListener(this);
	}

	/**
	 * @see IMoodyListener#moodyChanged(IMoody)
	 */
	@Override
	public void moodyChanged (IMoody moody) {
		// Rafraichir les boutons et le graphe
		this.refreshButtons();
		this.refreshGraphe();
	}

	/**
	 * @see TreeSelectionListener#valueChanged(TreeSelectionEvent)
	 */
	@Override
	public void valueChanged (TreeSelectionEvent event) {
		// Rafraichir les boutons et le graphe
		this.refreshButtons();
		this.refreshGraphe();
	}

	/**
	 * @see ActionListener#actionPerformed(ActionEvent)
	 */
	@Override
	public void actionPerformed (ActionEvent event) {
		// Conserver la sélection du jtree
		TreePath path = this.jtree.getSelectionPath();
		
		if(event.getSource() == this.jb_exporter) {
			// Récupérer la catégorie
			String nomCategorie = this.getSelection();

			// Créer et afficher la fenêtre de gestion d'exportation
			new JDExporter(this.w_parent, InfosTheme.CATEGORIE_PHASES_ELIMINATOIRES, nomCategorie, null, null).setVisible(true);
		} else if(event.getSource() == this.jb_generer) {
			// Récupérer le nombre de phases à générer
			int nbPhases = this.jcb_participants.getSelectedIndex()+2;
			
			// Récupérer le nom de la catégorie séléctionnée
			String nomCategorie = this.getSelection();
			
			// Vérifier si l'utilisateur a bien séléctionné une catégorie
			if(nomCategorie != null) {
				// Vérifier si le nombre de phases qualificatives est correct
				if(nbPhases <= ContestOrg.get().getCtrlPhasesEliminatoires().getNbPhasesElimsPossibles(nomCategorie)) {
					// Demander à l'utilisateur s'il souhaite vraiment regénérer les phases éliminatoires
					if(this.grapheGrandeFinale.isClear() || ViewHelper.confirmation(this.w_parent, "En regénérant les phases éliminatoires, les matchs déjà générés seront perdus. Désirez-vous continuer ?", true)) {
						// Vérifier s'il y a des participants ex-aequo à la qualification aux phases éliminatoires
						Map<InfosModelPoule, List<InfosModelParticipant>> participantsExAequos = ContestOrg.get().getCtrlPhasesEliminatoires().verifierExAequo(nomCategorie, nbPhases);
						boolean continuer = true;
						if(participantsExAequos.size() != 0) {
							// Récupérer le nombre de poules de la catégorie
							int nbPoules = ContestOrg.get().getCtrlParticipants().getListePoules(nomCategorie).size();
							
							// Construire le message de confirmation
							StringBuilder message = new StringBuilder("Il y a des participants ex-aequo à la qualification aux phases éliminatoires :");
							for(Entry<InfosModelPoule, List<InfosModelParticipant>> participantsExAequosPoule : participantsExAequos.entrySet()) {
								// Récupérer la liste des participants et leur nombre
								List<InfosModelParticipant> participants = participantsExAequosPoule.getValue();
								int nbParticipants = participants.size();
								
								// Formater la liste des participants
								if(nbPoules == 1) {
									for(int i=0;i<nbParticipants;i++) {
										message.append("\n- "+participants.get(i).getNom());
									}
								} else {
									message.append("\n- "+participantsExAequosPoule.getKey().getNom()+" : ");
									for(int i=0;i<nbParticipants;i++) {
										if(i != 0) {
											message.append(", ");
										}
										message.append(participants.get(i).getNom());
									}
								}
							}
							message.append("\nDésirez-vous continuer ?");
							
							// Demander à l'utilisateur s'il souhaite continuer
							if(!ViewHelper.confirmation(this.w_parent, message.toString(), true)) {
								continuer = false;
							}
						}
						
						// Demander la création
						if(continuer) {
							ContestOrg.get().getCtrlPhasesEliminatoires().genererPhasesElims(nomCategorie, nbPhases, new InfosModelMatchPhasesElims(null,null), new InfosModelPhasesEliminatoires());
						}
					}
				} else {
					// Erreur
					if(ContestOrg.get().getCtrlParticipants().getTypeParticipants() == InfosModelConcours.PARTICIPANTS_EQUIPES) {
						if(!ContestOrg.get().getCtrlParticipants().isStatutHomologueActive()) {
							ViewHelper.derror(this, "Il n'y a pas assez d'équipes pouvant participer *.\n<i>* Pour pouvoir participer, une équipe doit avoir le statut \"Présente\"</i>");
						} else {
							ViewHelper.derror(this, "Il n'y a pas assez d'équipes pouvant participer *.\n<i>* Pour pouvoir participer, une équipe doit avoir le statut \"Homologuée\"</i>");
						}
					} else {
						if(!ContestOrg.get().getCtrlParticipants().isStatutHomologueActive()) {
							ViewHelper.derror(this, "Il n'y a pas assez de joueurs pouvant participer *.\n<i>* Pour pouvoir participer, un joueur doit avoir le statut \"Présent\"</i>");
						} else {
							ViewHelper.derror(this, "Il n'y a pas assez de joueurs pouvant participer *.\n<i>* Pour pouvoir participer, un joueur doit avoir le statut \"Homologué\"</i>");
						}
					}
				}
			} else {
				// Erreur
				ViewHelper.derror(this, "Veuillez séléctionner la catégorie dans laquelle vous désirez générer les phases éliminatoires.");
			}
		} else if(event.getSource() == this.jb_reinitialiser) {
			// Récupérer le nom de la catégorie séléctionnée
			String nomCategorie = this.getSelection();
			
			// Vérifier si l'utilisateur a bien séléctionné une catégorie
			if(nomCategorie != null) {
				// Demander à l'utilisateur s'il souhaite vraiment réinitialiser les phases éliminatoires
				if(ViewHelper.confirmation(this.w_parent, "Désirez-vous vraiment réinitialiser les phases éliminatoires de la catégorie séléctionnée ?", true)) {
					// Demander le reset des phases éliminatoires
					ContestOrg.get().getCtrlPhasesEliminatoires().resetPhasesElims(nomCategorie);
				}
			} else {
				// Erreur
				ViewHelper.derror(this, "Veuillez séléctionner la catégorie à laquelle vous désirez réinitialiser les phases éliminatoires.");
			}
		}
		
		// Remettre la sélection du jtree initiale
		this.jtree.setSelectionPath(path);
	}
	
	/**
	 * Rafraichir les boutons
	 */
	private void refreshButtons () {
		// Récupérer le nom de la catégorie
		String nomCategorie = this.getSelection();
		
		// Rafraichir les boutons
		this.jcb_participants.setEnabled(ContestOrg.get().is(ContestOrg.STATE_EDIT) && nomCategorie != null);
		this.jb_generer.setEnabled(ContestOrg.get().is(ContestOrg.STATE_EDIT) && nomCategorie != null);
		this.jb_reinitialiser.setEnabled(ContestOrg.get().is(ContestOrg.STATE_EDIT) && nomCategorie != null && ContestOrg.get().getCtrlPhasesEliminatoires().getNbPhasesElims(nomCategorie) > 0);
		this.jb_exporter.setEnabled(ContestOrg.get().is(ContestOrg.STATE_EDIT));
	}
	
	/**
	 * Rafraichir le graphe
	 */
	private void refreshGraphe() {
		if(ContestOrg.get().is(ContestOrg.STATE_OPEN)) {
			// Récupérer le nom de la catégorie
			String nomCategorie = this.getSelection();
			
			// Rafraichir le graphe
			if(nomCategorie != null) {
				// Rafraichir les graphes
				this.grapheGrandeFinale.setCategorie(nomCategorie);
				this.graphePetiteFinale.setCategorie(nomCategorie);
			} else {
				// Effacer les graphes
				this.grapheGrandeFinale.clear();
				this.graphePetiteFinale.clear();
			}
		} else {
			// Effacer les graphes
			this.grapheGrandeFinale.clear();
			this.graphePetiteFinale.clear();
		}
	}
	
	/**
	 * Récupère la catégorie séléctionnée
	 * @return catégorie séléctionnée
	 */
	private String getSelection() {
		// Récupérer le nom de la catégorie
		String nomCategorie = null;
		TreePath path = this.jtree.getSelectionPath();
		if(path != null) {
			// Récupérer la node séléctionnée
			ITreeNode node = (ITreeNode)path.getLastPathComponent();
			
			// Récupérer le nom de la catégorie
			switch(node.getLevel()) {
				case 0: // Racine
					// Vérifier s'il n'y a qu'une seule catégorie
					if(node.getChildCount() == 1) {
						nomCategorie = ((InfosModelCategorie)((ITreeNode)node.getChildAt(0)).getObject()).getNom();
					}
					break;
				case 1: // Catégorie
					nomCategorie = ((InfosModelCategorie)node.getObject()).getNom();
					break;
			}
		} else {
			// Vérifier s'il n'y a qu'une seule catégorie
			ITreeNode categories = (ITreeNode)this.jtree.getModel().getRoot();
			if(categories.getChildCount() == 1) {
				nomCategorie = ((InfosModelCategorie)((ITreeNode)categories.getChildAt(0)).getObject()).getNom();
			}
		}
		
		// Retourner le nom de la catégorie sélécctionnée
		return nomCategorie;
	}
	
}
