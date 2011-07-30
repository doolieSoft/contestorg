package views;

import infos.InfosModelCategorie;
import infos.InfosModelConcours;
import infos.InfosModelEquipe;
import infos.InfosModelPoule;
import infos.Theme;
import interfaces.IClosableTableModel;
import interfaces.IMoody;
import interfaces.ITreeNode;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.ListSelectionModel;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import common.Pair;

import controlers.ContestOrg;

@SuppressWarnings("serial")
public class JPPrincipalEquipes extends JPPrincipalAbstract implements TreeSelectionListener, ListSelectionListener, ActionListener, ItemListener, MouseListener
{
	
	// Panneau du haut
	private JButton jb_nouvelle;
	private JButton jb_categories;
	private JButton jb_poules;
	private JButton jb_importer;
	private JButton jb_exporter;
	
	// Panneau du bas
	private JComboBox jcb_statut;
	private JButton jb_editer;
	private JButton jb_supprimer;
	
	// JTree des catégorie/poules
	private JTree jtree;
	
	// JTable des équipes
	private JTable jtable;
	
	// Constructeur
	public JPPrincipalEquipes(Window w_parent) {
		// Appeller le constructeur du parent
		super(w_parent);
		
		// Ecouter le controleur principal
		ContestOrg.get().addListener(this);
		
		// Panneau du haut
		this.jb_categories = new JButton("Catégories", new ImageIcon("img/farm/16x16/folders.png"));
		this.jb_poules = new JButton("Poules", new ImageIcon("img/farm/16x16/table_multiple.png"));
		this.jb_nouvelle = new JButton("Nouvelle équipe", new ImageIcon("img/farm/16x16/add.png"));
		this.jb_importer = new JButton("Importer équipes", new ImageIcon("img/farm/16x16/application_get.png"));
		this.jb_exporter = new JButton("Exporter", new ImageIcon("img/farm/16x16/application_go.png"));
		
		this.jb_categories.setEnabled(false);
		this.jb_poules.setEnabled(false);
		this.jb_nouvelle.setEnabled(false);
		this.jb_importer.setEnabled(false);
		this.jb_exporter.setEnabled(false);
		
		this.jb_categories.setToolTipText("Configurer les catégories");
		this.jb_poules.setToolTipText("Configurer les poules");
		this.jb_nouvelle.setToolTipText("Ajouter une équipe");
		this.jb_importer.setToolTipText("Importer une liste d'équipes");
		this.jb_exporter.setToolTipText("Exporter des informations sur les équipes");
		
		this.jp_haut.add(this.jb_categories);
		this.jp_haut.add(this.jb_poules);
		this.jp_haut.add(this.jb_nouvelle);
		this.jp_haut.add(this.jb_importer);
		this.jp_haut.add(this.jb_exporter);
		
		// Panneau de contenu
		this.jtree = new JTree(ContestOrg.get().getCtrlEquipes().getTreeModelPoules());
		this.jtree.setCellRenderer(new TreeCellRendererEquipes());
		this.jtree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		this.jtree.addTreeSelectionListener(this);
		this.jtree.setBorder(new LineBorder(new Color(254, 254, 254), 4));
		this.jtree.setToggleClickCount(-1);
		
		this.jtable = new JTable(ContestOrg.get().getCtrlEquipes().getTableModelEquipes());
		this.jtable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		this.jtable.getSelectionModel().addListSelectionListener(this);
		
		this.jtable.getColumnModel().getColumn(4 - 9 + this.jtable.getColumnCount()).setCellRenderer(new TableCellRenderers.Rang());
		this.jtable.getColumnModel().getColumn(5 - 9 + this.jtable.getColumnCount()).setCellRenderer(new TableCellRenderers.NbPoints());
		this.jtable.getColumnModel().getColumn(6 - 9 + this.jtable.getColumnCount()).setCellRenderer(new TableCellRenderers.NbVictoires());
		this.jtable.getColumnModel().getColumn(8 - 9 + this.jtable.getColumnCount()).setCellRenderer(new TableCellRenderers.Statut());
		
		TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(this.jtable.getModel());
		sorter.setStringConverter(new TableStringConverterEquipes());
		this.jtable.setRowSorter(sorter);
		
		this.jtable.addMouseListener(this);
		
		JScrollPane jsp = new JScrollPane(this.jtree);
		jsp.setPreferredSize(new Dimension(180, jsp.getPreferredSize().height));
		this.jp_contenu.add(new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, jsp, new JScrollPane(this.jtable)));
		
		// Panneau du bas
		this.jcb_statut = new JComboBox();
		for (InfosModelEquipe.Statut statut : InfosModelEquipe.Statut.values()) {
			this.jcb_statut.addItem(statut.getNomEquipe());
		}
		this.jb_editer = new JButton("Editer équipe", new ImageIcon("img/farm/16x16/pencil.png"));
		this.jb_supprimer = new JButton("Supprimer équipes", new ImageIcon("img/farm/16x16/delete.png"));
		
		this.jcb_statut.setEnabled(false);
		this.jb_editer.setEnabled(false);
		this.jb_supprimer.setEnabled(false);
		
		this.jcb_statut.setToolTipText("Modifier le statut des équipes séléctionnées");
		this.jb_editer.setToolTipText("Editer l'équipe séléctionnée");
		this.jb_supprimer.setToolTipText("Supprimer les équipes séléctionnées");
		
		this.jp_bas.add(this.jcb_statut);
		this.jp_bas.add(this.jb_editer);
		this.jp_bas.add(this.jb_supprimer);
		
		// Ecouter les boutons
		this.jb_categories.addActionListener(this);
		this.jb_poules.addActionListener(this);
		this.jb_nouvelle.addActionListener(this);
		this.jb_importer.addActionListener(this);
		this.jb_exporter.addActionListener(this);
		this.jb_editer.addActionListener(this);
		this.jb_supprimer.addActionListener(this);
		
		// Ecouter la liste des statuts
		this.jcb_statut.addItemListener(this);
	}
	
	// Implémentation de moodyChanged
	@Override
	public void moodyChanged (IMoody moody) {
		// Rafraichir les boutons
		this.refreshButtons();
		
		// Changer le nom de de certains bouton en fonction du type de participants
		if (moody.is(ContestOrg.STATE_OPEN)) {
			// Equipe/Joueur
			this.jb_nouvelle.setText(ContestOrg.get().getTypeParticipants() == InfosModelConcours.PARTICIPANTS_EQUIPES ? "Nouvelle équipe" : "Nouveau joueur");
			this.jb_importer.setText(ContestOrg.get().getTypeParticipants() == InfosModelConcours.PARTICIPANTS_EQUIPES ? "Importer équipes" : "Importer joueurs");

			this.jb_nouvelle.setToolTipText(ContestOrg.get().getTypeParticipants() == InfosModelConcours.PARTICIPANTS_EQUIPES ? "Ajouter une équipe" : "Ajouter un joueur");
			this.jb_importer.setToolTipText(ContestOrg.get().getTypeParticipants() == InfosModelConcours.PARTICIPANTS_EQUIPES ? "Importer une liste d'équipes" : "Importer une liste de joueurs");
			this.jb_exporter.setToolTipText(ContestOrg.get().getTypeParticipants() == InfosModelConcours.PARTICIPANTS_EQUIPES ? "Exporter des informations sur les équipes" : "Exporter des informations sur les joueurs");

			this.jb_editer.setText(ContestOrg.get().getTypeParticipants() == InfosModelConcours.PARTICIPANTS_EQUIPES ? "Editer équipe" : "Editer joueur");
			this.jb_supprimer.setText(ContestOrg.get().getTypeParticipants() == InfosModelConcours.PARTICIPANTS_EQUIPES ? "Supprimer équipes" : "Supprimer joueurs");
			
			this.jcb_statut.setToolTipText(ContestOrg.get().getTypeParticipants() == InfosModelConcours.PARTICIPANTS_EQUIPES ? "Modifier le statut des équipes séléctionnées" : "Modifier le statut des joueurs séléctionnés");
			this.jb_editer.setToolTipText(ContestOrg.get().getTypeParticipants() == InfosModelConcours.PARTICIPANTS_EQUIPES ? "Editer l'équipe séléctionnée" : "Editer le joueur séléctionné");
			this.jb_supprimer.setToolTipText(ContestOrg.get().getTypeParticipants() == InfosModelConcours.PARTICIPANTS_EQUIPES ? "Supprimer les équipes séléctionnées" : "Supprimer les joueurs séléctionnés");
			
			// Statuts d'équipe
			this.jcb_statut.removeItemListener(this);
			this.jcb_statut.removeAllItems();
			for (InfosModelEquipe.Statut statut : InfosModelEquipe.Statut.values()) {
				this.jcb_statut.addItem(ContestOrg.get().getTypeParticipants() == InfosModelConcours.PARTICIPANTS_EQUIPES ? statut.getNomEquipe() : statut.getNomJoueur());
			}
			this.jcb_statut.addItemListener(this);
		}
	}
	
	// Implémentation de TreeSelectionListener
	@Override
	public void valueChanged (TreeSelectionEvent event) {
		// Récupérer et fermer le tablemodel actuel
		IClosableTableModel tablemodel = (IClosableTableModel)this.jtable.getModel();
		tablemodel.close();
		
		// Placer un nouveau tablemodel
		TreePath path = event.getNewLeadSelectionPath();
		if (path != null) {
			String nomCategorie, nomPoule;
			ITreeNode node = (ITreeNode)path.getLastPathComponent();
			switch (node.getLevel()) {
				case 1: // Catégorie
					// Modifier le mode de la jtable
					nomCategorie = ((InfosModelCategorie)node.getObject()).getNom();
					this.jtable.setModel(ContestOrg.get().getCtrlEquipes().getTableModelEquipes(nomCategorie));
					break;
				case 2: // Poule
					// Modifier le mode de la jtable
					nomCategorie = ((InfosModelCategorie)node.getParent().getObject()).getNom();
					nomPoule = ((InfosModelPoule)node.getObject()).getNom();
					this.jtable.setModel(ContestOrg.get().getCtrlEquipes().getTableModelEquipes(nomCategorie, nomPoule));
					break;
				default: // Concours
					// Modifier le mode de la jtable
					this.jtable.setModel(ContestOrg.get().getCtrlEquipes().getTableModelEquipes());
			}
		} else {
			this.jtable.setModel(ContestOrg.get().getCtrlEquipes().getTableModelEquipes());
		}
		
		// Redéfinir le renderer de certaines colonnes
		this.jtable.getColumnModel().getColumn(4 - 9 + this.jtable.getColumnCount()).setCellRenderer(new TableCellRenderers.Rang());
		this.jtable.getColumnModel().getColumn(5 - 9 + this.jtable.getColumnCount()).setCellRenderer(new TableCellRenderers.NbPoints());
		this.jtable.getColumnModel().getColumn(6 - 9 + this.jtable.getColumnCount()).setCellRenderer(new TableCellRenderers.NbVictoires());
		this.jtable.getColumnModel().getColumn(8 - 9 + this.jtable.getColumnCount()).setCellRenderer(new TableCellRenderers.Statut());
		
		// Rédéfinir le trie des colonnes
		TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(this.jtable.getModel());
		sorter.setStringConverter(new TableStringConverterEquipes());
		this.jtable.setRowSorter(sorter);
		
		// Rafraichir les boutons
		this.refreshButtons();
	}
	
	// Implémentation de ListSelectionListener
	@Override
	public void valueChanged (ListSelectionEvent event) {
		// Rafraichir les boutons
		this.refreshButtons();
		
		// Changer le statut de la liste
		if (this.jtable.getSelectedRow() != -1) {
			this.jcb_statut.removeItemListener(this);
			this.jcb_statut.setSelectedIndex(((InfosModelEquipe.Statut)this.jtable.getModel().getValueAt(this.jtable.getRowSorter().convertRowIndexToModel(this.jtable.getSelectedRow()), this.jtable.getModel().getColumnCount() - 1)).ordinal());
			this.jcb_statut.addItemListener(this);
		}
	}
	
	// Implémentation de ActionListener
	@Override
	public void actionPerformed (ActionEvent event) {
		// Conserver la sélection du jtree actuelle
		TreePath path = this.jtree.getSelectionPath();
		
		if (event.getSource() == this.jb_categories) {
			// Créer et afficher la fenetre de gestion des catégories
			new JDCategories(this.w_parent).setVisible(true);
		} else if (event.getSource() == this.jb_poules) {
			// Vérifier s'il y a suffisement d'équipes
			if(ContestOrg.get().getCtrlEquipes().getNbEquipes() >= 4) {
				// Créer et afficher la fenetre de gestion des poules
				new JDPoules(this.w_parent).setVisible(true);
			} else {
				// Erreur
				ViewHelper.derror(this.w_parent, "Il faut au moins 4 "+(ContestOrg.get().getTypeParticipants() == InfosModelConcours.PARTICIPANTS_EQUIPES ? "équipes" : "joueurs")+" pour pouvoir gérer les poules.");
			}
		} else if (event.getSource() == this.jb_nouvelle) {
			// Récupérer la catégorie et la poule séléctionnées
			Pair<String,String> selection = this.getSelection();
			
			// Créer et afficher la fenetre de création d'équipe
			CollectorEquipeCreer collector = new CollectorEquipeCreer();
			JDialog jd_equipe = new JDEquipeCreer(this.w_parent, collector, selection.getFirst(), selection.getSecond());
			collector.setWindow(jd_equipe);
			jd_equipe.setVisible(true);
		} else if (event.getSource() == this.jb_importer) {
			// Récupérer la catégorie et la poule séléctionnées
			Pair<String,String> selection = this.getSelection();

			// Créer et afficher la fenetre d'importation
			new JDImporterEquipes(this.w_parent, selection.getFirst(), selection.getSecond()).setVisible(true);
		} else if (event.getSource() == this.jb_exporter) {
			// Récupérer la catégorie et la poule séléctionnées
			Pair<String,String> selection = this.getSelection();

			// Créer et afficher la fenetre de gestion d'exportation
			new JDExporter(this.w_parent, Theme.CATEGORIE_EQUIPES, selection.getFirst(), selection.getSecond(), null).setVisible(true);
		} else if (event.getSource() == this.jb_editer) {
			// Vérifier s'il y a une ligne de séléctionnée
			if (this.jtable.getSelectedRow() != -1) {
				// Récupérer le nom de l'équipe séléctionné
				String nomEquipe = this.getNomEquipe();
				
				// Créer et afficher la fenetre de modification d'équipe
				CollectorEquipeEditer collector = new CollectorEquipeEditer(nomEquipe);
				JDialog jd_equipe = new JDEquipeEditer(this.w_parent, collector, ContestOrg.get().getCtrlEquipes().getInfosEquipe(nomEquipe));
				collector.setWindow(jd_equipe);
				jd_equipe.setVisible(true);
			} else {
				// Message d'erreur
				ViewHelper.derror(this.w_parent, "Veuillez sélectionner "+(ContestOrg.get().getTypeParticipants() == InfosModelConcours.PARTICIPANTS_EQUIPES ? "l'équipe" : "le joueur")+" que vous désirez éditer.");
			}
		} else if (event.getSource() == this.jb_supprimer) {
			// Vérifier s'il y a une ligne de séléctionnée
			if (this.jtable.getSelectedRow() != -1) {
				if (this.jtable.getSelectedRowCount() == 1) {
					// Récupérer le nom de l'équipe séléctionné
					String nomEquipe = this.getNomEquipe();
					
					// Demander la suppression de l'équipe
					if (ViewHelper.confirmation(this.w_parent, "Désirez-vous vraiment supprimer "+(ContestOrg.get().getTypeParticipants() == InfosModelConcours.PARTICIPANTS_EQUIPES ? "l'équipe" : "le joueur")+" \"" + nomEquipe + "\" ?")) {
						ContestOrg.get().getCtrlEquipes().removeEquipe(nomEquipe);
					}
				} else {
					// Récupérer les noms des équipes séléctionnées
					ArrayList<String> nomEquipes = new ArrayList<String>();
					for (int index : this.jtable.getSelectedRows()) {
						nomEquipes.add((String)this.jtable.getModel().getValueAt(this.jtable.getRowSorter().convertRowIndexToModel(index), this.jtable.getModel().getColumnCount() - 6));
					}
					
					// Demander la suppression des équipes
					if (ViewHelper.confirmation(this.w_parent, "Désirez-vous vraiment supprimer "+(ContestOrg.get().getTypeParticipants() == InfosModelConcours.PARTICIPANTS_EQUIPES ? "les équipe séléctionnées" : "les joueurs séléctionnés")+" ?")) {
						ContestOrg.get().getCtrlEquipes().removeEquipes(nomEquipes);
					}
				}
			} else {
				// Message d'erreur
				ViewHelper.derror(this.w_parent, "Veuillez sélectionner "+(ContestOrg.get().getTypeParticipants() == InfosModelConcours.PARTICIPANTS_EQUIPES ? "l'équipe" : "le joueur")+" que vous désirez supprimer.");
			}
		}
		
		// Remettre la sélection du jtree initiale
		this.jtree.setSelectionPath(path);
	}
	
	// Implémentation de itemListener
	@Override
	public void itemStateChanged (ItemEvent event) {
		if (event.getStateChange() == ItemEvent.SELECTED) {
			if (this.jtable.getSelectedRowCount() == 1) {
				// Récupérer le nom de l'équipe séléctionné
				String nomEquipe = this.getNomEquipe();
				
				// Demander la modification du statut de l'équipe
				ContestOrg.get().getCtrlEquipes().updateEquipe(nomEquipe, InfosModelEquipe.Statut.values()[this.jcb_statut.getSelectedIndex()]);
			} else {
				// Récupérer les noms des équipes séléctionnées
				ArrayList<String> nomEquipes = new ArrayList<String>();
				for (int index : this.jtable.getSelectedRows()) {
					nomEquipes.add(this.getEquipe(index));
				}
				
				// Demander la modification du statut de l'équipe
				ContestOrg.get().getCtrlEquipes().updateEquipes(nomEquipes, InfosModelEquipe.Statut.values()[this.jcb_statut.getSelectedIndex()]);
			}
		}
	}
	
	// Rafraichir les boutons
	private void refreshButtons() {
		// Compter le nombre de catégories et de poules
		int nbCategories = 0, nbPoules = 0;
		ITreeNode root = (ITreeNode)this.jtree.getModel().getRoot();
		for(int i=0;i<root.getChildCount();i++) {
			nbCategories++;
			nbPoules += ((ITreeNode)root.getChildAt(i)).getChildCount();
		}
		
		// Activer/Desactiver les boutons
		this.jcb_statut.setEnabled(ContestOrg.get().is(ContestOrg.STATE_EDIT) && this.jtable.getSelectedRowCount() != 0);
		this.jb_editer.setEnabled(ContestOrg.get().is(ContestOrg.STATE_EDIT) && this.jtable.getSelectedRowCount() == 1);
		this.jb_supprimer.setEnabled(ContestOrg.get().is(ContestOrg.STATE_EDIT) && this.jtable.getSelectedRowCount() != 0);
		this.jb_exporter.setEnabled(ContestOrg.get().is(ContestOrg.STATE_OPEN));
		this.jb_categories.setEnabled(ContestOrg.get().is(ContestOrg.STATE_EDIT));
		this.jb_poules.setEnabled(ContestOrg.get().is(ContestOrg.STATE_EDIT) && ContestOrg.get().getCtrlEquipes().getNbEquipes() >= 4);
		this.jb_nouvelle.setEnabled(ContestOrg.get().is(ContestOrg.STATE_EDIT) && nbPoules != 0);
		this.jb_importer.setEnabled(ContestOrg.get().is(ContestOrg.STATE_EDIT));
	}
	
	// Récuperer la catégorie et la poule séléctionnées
	private Pair<String,String> getSelection() {
		// Initialiser la catégorie et la poule
		String nomCategorie = null, nomPoule = null; int nbPoules = 0;
		
		// Récupérer la catégorie et la poule uniques
		ITreeNode root = (ITreeNode)this.jtree.getModel().getRoot();
		for(int i=0;i<root.getChildCount();i++) {
			ITreeNode child = (ITreeNode)root.getChildAt(i);
			if(child.getChildCount() != 0) {
				nomCategorie = ((InfosModelCategorie)child.getObject()).getNom();
				for(int j=0;j<child.getChildCount();j++) {
					ITreeNode granchild = (ITreeNode)child.getChildAt(j);
					nomPoule = ((InfosModelPoule)granchild.getObject()).getNom();
					nbPoules++;
				}
			}
		}
		if(nbPoules > 1) {
			nomCategorie = null;
			nomPoule = null;
		}
		
		// Récupérer la catégorie et la poule séléctionnées
		TreePath path = this.jtree.getSelectionPath();
		if(nomCategorie == null && nomPoule == null) {
			if(path != null) {
				ITreeNode node = (ITreeNode)path.getLastPathComponent();
				if(node.getLevel() == 2) {
					nomCategorie = ((InfosModelCategorie)node.getParent().getObject()).getNom();
					nomPoule = ((InfosModelPoule)node.getObject()).getNom();
				} else if(node.getLevel() == 1) {
					nomCategorie = ((InfosModelCategorie)node.getObject()).getNom();
					if(node.getChildCount() == 1) {
						nomPoule = ((InfosModelPoule)((ITreeNode)node.getChildAt(0)).getObject()).getNom();
					}
				}
			}
		}
		
		// Retourner la catégorie et la poule
		return new Pair<String, String>(nomCategorie, nomPoule);
	}
	
	// Récuperer l'équipe séléctionnée
	private String getNomEquipe() {
		return this.getEquipe(this.jtable.getSelectedRow());
	}
	private String getEquipe(int index) {
		return (String)this.jtable.getModel().getValueAt(this.jtable.getRowSorter().convertRowIndexToModel(index), this.jtable.getModel().getColumnCount() - 6);
	}
	
	// Implémentation de MouseListener
	@Override
	public void mouseClicked (MouseEvent event) {
		if(event.getSource() == this.jtable) {
			// Vérifier s'il s'agit d'un double click
			if(event.getClickCount() == 2) {
				// Vérifier s'il y a une ligne de séléctionnée
				if (this.jtable.getSelectedRow() != -1) {
					// Récupérer le nom de l'équipe séléctionnée
					String nomEquipe = this.getNomEquipe();
					
					// Créer et afficher la fenetre de modification d'équipe
					CollectorEquipeEditer collector = new CollectorEquipeEditer(nomEquipe);
					JDialog jd_equipe = new JDEquipeEditer(this.w_parent, collector, ContestOrg.get().getCtrlEquipes().getInfosEquipe(nomEquipe));
					collector.setWindow(jd_equipe);
					jd_equipe.setVisible(true);
				}
			}
		}
	}
	@Override
	public void mouseEntered (MouseEvent event) { }
	@Override
	public void mouseExited (MouseEvent event) { }
	@Override
	public void mousePressed (MouseEvent event) { }
	@Override
	public void mouseReleased (MouseEvent event) { }
	
}
