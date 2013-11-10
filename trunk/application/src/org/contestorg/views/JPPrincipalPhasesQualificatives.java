package org.contestorg.views;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
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

import org.contestorg.common.Pair;
import org.contestorg.common.Quadruple;
import org.contestorg.common.Triple;
import org.contestorg.controllers.ContestOrg;
import org.contestorg.infos.InfosConfiguration;
import org.contestorg.infos.InfosModelCategorie;
import org.contestorg.infos.InfosModelMatchPhasesQualifs;
import org.contestorg.infos.InfosModelObjectifRemporte;
import org.contestorg.infos.InfosModelParticipation;
import org.contestorg.infos.InfosModelPhaseQualificative;
import org.contestorg.infos.InfosModelPoule;
import org.contestorg.infos.InfosTheme;
import org.contestorg.interfaces.IClosableTableModel;
import org.contestorg.interfaces.ICollector;
import org.contestorg.interfaces.IMoody;
import org.contestorg.interfaces.IMoodyListener;
import org.contestorg.interfaces.ITreeNode;

/**
 * Panel des phases qualificatives pour la fenêtre principale
 */
@SuppressWarnings("serial")
public class JPPrincipalPhasesQualificatives extends JPPrincipalAbstract implements TreeSelectionListener, ListSelectionListener, ActionListener, MouseListener
{
	
	// Panneau du haut
	
	/** Bouton "Nouvelle phase" */
	private JButton jb_nouvellePhase;
	
	/** Bouton "Générer phase" */
	private JButton jb_genererPhase;
	
	/** Bouton "Editer phase" */
	private JButton jb_editerPhase;
	
	/** Bouton "Supprimer phase" */
	private JButton jb_supprimerPhase;
	
	/** Bouton "Exporter" */
	private JButton jb_exporter;
	
	// Panneau de contenu

	/** Arborescence des catégorie/poules/séries */
	private JTree jtree;
	
	/** Tableaux des matchs */
	private JTable jtable;
	
	// Panneau du bas
	
	/** Bouton "Nouveau match" */
	private JButton jb_nouveauMatch;
	
	/** Bouton "Editer match" */
	private JButton jb_editerMatch;
	
	/** Bouton "Supprimer match" */
	private JButton jb_supprimerMatch;
	
	/**
	 * Constructeur
	 * @param w_parent fenêtre parent
	 */
	public JPPrincipalPhasesQualificatives(Window w_parent) {
		// Appeller le constructeur du parent
		super(w_parent);
		
		// Ecouter le controleur principal
		ContestOrg.get().addListener(this);
		
		// Panneau du haut
		this.jb_nouvellePhase = new JButton("Nouvelle phase", new ImageIcon("img/farm/16x16/add.png"));
		this.jb_genererPhase = new JButton("Générer phase", new ImageIcon("img/farm/16x16/arrow_refresh.png"));
		this.jb_nouveauMatch = new JButton("Nouveau match", new ImageIcon("img/farm/16x16/add.png"));
		this.jb_editerPhase = new JButton("Editer phase", new ImageIcon("img/farm/16x16/pencil.png"));
		this.jb_supprimerPhase = new JButton("Supprimer phase", new ImageIcon("img/farm/16x16/delete.png"));
		this.jb_exporter = new JButton("Exporter", new ImageIcon("img/farm/16x16/application_go.png"));

		this.jb_nouvellePhase.setEnabled(false);
		this.jb_genererPhase.setEnabled(false);
		this.jb_nouveauMatch.setEnabled(false);
		this.jb_editerPhase.setEnabled(false);
		this.jb_supprimerPhase.setEnabled(false);
		this.jb_exporter.setEnabled(false);

		this.jb_nouvellePhase.setToolTipText("Ajouter une phase qualificative dans la poule séléctionnée");
		this.jb_genererPhase.setToolTipText("Générer une phase qualificative dans la poule séléctionnée");
		this.jb_nouveauMatch.setToolTipText("Ajouter un match dans la phase qualificative séléctionnée");
		this.jb_editerPhase.setToolTipText("Editer la phase qualificative séléctionnée");
		this.jb_supprimerPhase.setToolTipText("Supprimer la phase qualificative séléctionnée");
		this.jb_exporter.setToolTipText("Exporter des informations sur les phases qualificatives");

		this.jp_haut.add(this.jb_nouvellePhase);
		this.jp_haut.add(this.jb_genererPhase);
		this.jp_haut.add(this.jb_nouveauMatch);
		// Suppression du bouton en attendant une amélioration de la fonctionnalité (pas de suppression de la phase mais seulement des matchs)
		// this.jp_haut.add(this.jb_editerPhase);
		this.jp_haut.add(this.jb_supprimerPhase);
		this.jp_haut.add(this.jb_exporter);
		
		// Panel de contenu
		this.jtree = new JTree(ContestOrg.get().getCtrlPhasesQualificatives().getTreeModelPhasesQualifs());
		this.jtree.setCellRenderer(new TreeCellRendererPhasesQualifs());
		this.jtree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		this.jtree.addTreeSelectionListener(this);
		this.jtree.setBorder(new LineBorder(new Color(254, 254, 254), 4));
		this.jtree.setToggleClickCount(-1);
		
		this.jtable = new JTable(ContestOrg.get().getCtrlPhasesQualificatives().getTableModelMatchsPhasesQualifs());
		this.jtable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.jtable.getSelectionModel().addListSelectionListener(this);
		
		if (this.jtable.getColumnCount() >= 7) {
			this.jtable.getColumnModel().getColumn(2 - 9 + this.jtable.getColumnCount()).setCellRenderer(new TableCellRenderers.Phase());
		}
		this.jtable.getColumnModel().getColumn(3 - 9 + this.jtable.getColumnCount()).setCellRenderer(new TableCellRenderers.Participant());
		this.jtable.getColumnModel().getColumn(4 - 9 + this.jtable.getColumnCount()).setCellRenderer(new TableCellRenderers.Resultat());
		this.jtable.getColumnModel().getColumn(5 - 9 + this.jtable.getColumnCount()).setCellRenderer(new TableCellRenderers.NbPoints());
		this.jtable.getColumnModel().getColumn(6 - 9 + this.jtable.getColumnCount()).setCellRenderer(new TableCellRenderers.Participant());
		this.jtable.getColumnModel().getColumn(7 - 9 + this.jtable.getColumnCount()).setCellRenderer(new TableCellRenderers.Resultat());
		this.jtable.getColumnModel().getColumn(8 - 9 + this.jtable.getColumnCount()).setCellRenderer(new TableCellRenderers.NbPoints());
		
		TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(this.jtable.getModel());
		sorter.setStringConverter(new TableStringConverterPhasesQualifs());
		this.jtable.setRowSorter(sorter);
		
		this.jtable.addMouseListener(this);
		
		JScrollPane jsp = new JScrollPane(this.jtree);
		jsp.setPreferredSize(new Dimension(240, jsp.getPreferredSize().height));
		this.jp_contenu.add(new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, jsp, new JScrollPane(this.jtable)));
		
		// Panneau du bas
		this.jb_editerMatch = new JButton("Résultats du match", new ImageIcon("img/farm/16x16/flag_finish.png"));
		this.jb_supprimerMatch = new JButton("Supprimer match", new ImageIcon("img/farm/16x16/delete.png"));
		
		this.jb_editerMatch.setEnabled(false);
		this.jb_supprimerMatch.setEnabled(false);
		
		this.jb_editerMatch.setToolTipText("Renseigner les résultats du match séléctionné");
		this.jb_supprimerMatch.setToolTipText("Supprimer le match séléctionné");
		
		this.jp_bas.add(this.jb_editerMatch);
		this.jp_bas.add(this.jb_supprimerMatch);
		
		// Ecouter les boutons
		this.jb_nouvellePhase.addActionListener(this);
		this.jb_genererPhase.addActionListener(this);
		this.jb_nouveauMatch.addActionListener(this);
		this.jb_editerPhase.addActionListener(this);
		this.jb_supprimerPhase.addActionListener(this);
		this.jb_exporter.addActionListener(this);
		
		this.jb_editerMatch.addActionListener(this);
		this.jb_supprimerMatch.addActionListener(this);
	}
	
	/**
	 * @see IMoodyListener#moodyChanged(IMoody)
	 */
	@Override
	public void moodyChanged (IMoody moody) {
		// Rafraichir les boutons
		this.refreshButtons();
	}
	
	/**
	 * @see TreeSelectionListener#valueChanged(TreeSelectionEvent)
	 */
	@Override
	public void valueChanged (TreeSelectionEvent event) {
		// Récupérer et fermer le tablemodel actuel
		IClosableTableModel tablemodel = (IClosableTableModel)this.jtable.getModel();
		tablemodel.close();
		
		// Changer le tablemodel
		TreePath path = event.getNewLeadSelectionPath();
		if (path != null) {
			String nomCategorie, nomPoule; int numeroPhase;
			ITreeNode node = (ITreeNode)path.getLastPathComponent();
			switch (node.getLevel()) {
				case 1: // Catégorie
					nomCategorie = ((InfosModelCategorie)node.getObject()).getNom();
					this.jtable.setModel(ContestOrg.get().getCtrlPhasesQualificatives().getTableModelMatchsPhasesQualifs(nomCategorie));
					break;
				case 2: // Poule
					nomCategorie = ((InfosModelCategorie)node.getParent().getObject()).getNom();
					nomPoule = ((InfosModelPoule)node.getObject()).getNom();
					this.jtable.setModel(ContestOrg.get().getCtrlPhasesQualificatives().getTableModelMatchsPhasesQualifs(nomCategorie, nomPoule));
					break;
				case 3: // Phase
					nomCategorie = ((InfosModelCategorie)node.getParent().getParent().getObject()).getNom();
					nomPoule = ((InfosModelPoule)node.getParent().getObject()).getNom();
					numeroPhase = node.getParent().getIndex(node);
					this.jtable.setModel(ContestOrg.get().getCtrlPhasesQualificatives().getTableModelMatchsPhasesQualifs(nomCategorie, nomPoule, numeroPhase));
					break;
				default: // Racine
					this.jtable.setModel(ContestOrg.get().getCtrlPhasesQualificatives().getTableModelMatchsPhasesQualifs());
			}
		} else {
			this.jtable.setModel(ContestOrg.get().getCtrlPhasesQualificatives().getTableModelMatchsPhasesQualifs());
		}
		
		// Redéfinir le renderer de certaines colonnes
		if (this.jtable.getColumnCount() >= 7) {
			this.jtable.getColumnModel().getColumn(2 - 9 + this.jtable.getColumnCount()).setCellRenderer(new TableCellRenderers.Phase());
		}
		this.jtable.getColumnModel().getColumn(3 - 9 + this.jtable.getColumnCount()).setCellRenderer(new TableCellRenderers.Participant());
		this.jtable.getColumnModel().getColumn(4 - 9 + this.jtable.getColumnCount()).setCellRenderer(new TableCellRenderers.Resultat());
		this.jtable.getColumnModel().getColumn(5 - 9 + this.jtable.getColumnCount()).setCellRenderer(new TableCellRenderers.NbPoints());
		this.jtable.getColumnModel().getColumn(6 - 9 + this.jtable.getColumnCount()).setCellRenderer(new TableCellRenderers.Participant());
		this.jtable.getColumnModel().getColumn(7 - 9 + this.jtable.getColumnCount()).setCellRenderer(new TableCellRenderers.Resultat());
		this.jtable.getColumnModel().getColumn(8 - 9 + this.jtable.getColumnCount()).setCellRenderer(new TableCellRenderers.NbPoints());
		
		// Rédéfinir le trie des colonnes
		TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(this.jtable.getModel());
		sorter.setStringConverter(new TableStringConverterPhasesQualifs());
		this.jtable.setRowSorter(sorter);
		
		// Rafraichir les boutons
		this.refreshButtons();
	}
	
	/**
	 * @see ListSelectionListener#valueChanged(ListSelectionEvent)
	 */
	@Override
	public void valueChanged (ListSelectionEvent event) {
		// Rafraichir les boutons
		this.refreshButtons();
	}
	
	/**
	 * @see ActionListener#actionPerformed(ActionEvent)
	 */
	@Override
	public void actionPerformed (ActionEvent event) {
		// Conserver la sélection du jtree
		TreePath path = this.jtree.getSelectionPath();
		
		if (event.getSource() == this.jb_nouvellePhase) {
			// Récupérer la catégorie et la poule séléctionnées
			Triple<String, String, Integer> selection = this.getSelection(-1, null, true);
			
			// Vérifier si la séléction est suffisante pour ne pas afficher la fenêtre de séléction
			if(selection.getFirst() == null || selection.getSecond() == null) {
				// Créer et afficher la fenêtre de création
				CollectorPhaseQualifVideCreer collector = new CollectorPhaseQualifVideCreer();
				JDialog jd_phase = new JDPhaseQualifVideCreer(this.w_parent, collector, selection.getFirst());
				collector.setWindow(jd_phase);
				jd_phase.setVisible(true);
			} else {
				// Créer la phase qualificative vide
				ContestOrg.get().getCtrlPhasesQualificatives().addPhaseQualifVide(selection.getFirst(), selection.getSecond(), new InfosModelPhaseQualificative());
			}
		} else if (event.getSource() == this.jb_genererPhase) {
			// Récupérer la catégorie et la poule séléctionnées
			Triple<String, String, Integer> selection = this.getSelection(-1, null, true);
			
			// Créer et afficher la fenêtre de création
			CollectorPhaseQualifCreer collector = new CollectorPhaseQualifCreer();
			JDialog jd_phase = new JDPhaseQualifCreer(this.w_parent, collector, selection.getFirst(), selection.getSecond());
			collector.setWindow(jd_phase);
			jd_phase.setVisible(true);
		} else if (event.getSource() == this.jb_nouveauMatch) {
			// Récupérer la catégorie, la poule et la phase qualificative séléctionnées
			Triple<String, String, Integer> selection = this.getSelection(-1, null, true);
			
			// Vérifier si la séléction est correcte
			if (selection.getFirst() != null && selection.getSecond() != null && selection.getThird() != null) {
				// Créer et afficher la fenêtre de création
				CollectorMatchPhasesQualifsCreer collector = new CollectorMatchPhasesQualifsCreer(selection.getFirst(), selection.getSecond(), selection.getThird());
				JDialog jd_match = new JDMatchPhasesQualifsCreer(this.w_parent, collector, selection.getFirst(), selection.getSecond(), selection.getThird());
				collector.setWindow(jd_match);
				jd_match.setVisible(true);
			} else {
				// Créer et afficher la fenêtre de création
				CollectorAbstract<Triple<String,String,Integer>> collector = new CollectorAbstract<Triple<String,String,Integer>>() {
					/**
					 * @see ICollector#collect(Object)
					 */
					@Override
					public void collect (Triple<String, String, Integer> selection) {
						// Fermer le collecteur
						this.close();
						
						// Créer et afficher la fenêtre de création
						CollectorMatchPhasesQualifsCreer collector = new CollectorMatchPhasesQualifsCreer(selection.getFirst(), selection.getSecond(), selection.getThird());
						JDialog jd_match = new JDMatchPhasesQualifsCreer(w_parent, collector, selection.getFirst(), selection.getSecond(), selection.getThird());
						collector.setWindow(jd_match);
						jd_match.setVisible(true);
					}
				};
				JDialog jd_categoriePoulePhaseQualif = new JDCategoriePoulePhaseQualif(this.w_parent, collector, selection.getFirst(), selection.getSecond());
				collector.setWindow(jd_categoriePoulePhaseQualif);
				jd_categoriePoulePhaseQualif.setVisible(true);
			}
		} else if (event.getSource() == this.jb_editerPhase) {
			// Récupérer la catégorie, la poule et la phase qualificative
			Triple<String, String, Integer> selection = this.getSelection(-1, null, true);
			
			// Vérifier si la séléction est correcte
			if (selection.getFirst() != null && selection.getSecond() != null && selection.getThird() != null) {
				// Demander confirmation à l'utilisateur
				if (ViewHelper.confirmation(this.w_parent, "En éditant la phase qualificative "+(selection.getThird()+1)+" de la poule \""+selection.getSecond()+"\" de la catégorie \""+selection.getFirst()+"\" de cette manière, tous les matchs qu'elle possède seront perdus. Désirez-vous continuer ? *\n* Il est préférable d'éditer les matchs manuellement avec les opérations d'ajout, de modification et de suppression", true)) {
					// Récupérer les informations de la phase qualificative à éditer
					Triple<InfosConfiguration<String>, InfosModelPhaseQualificative, InfosModelMatchPhasesQualifs> infos = ContestOrg.get().getCtrlPhasesQualificatives().getInfosPhaseQualif(selection.getFirst(), selection.getSecond(), selection.getThird());
					
					// Créer et afficher la fenêtre d'édition (en prenant soin de supprimer la phase qualificative)
					CollectorPhaseQualifEditer collector = new CollectorPhaseQualifEditer(selection.getFirst(), selection.getSecond(), selection.getThird());
					JDialog jd_phase = new JDPhaseQualifEditer(this.w_parent, collector, selection.getFirst(), selection.getSecond(), infos);
					collector.setWindow(jd_phase);
					jd_phase.setVisible(true);
					
					// Perdre la sélection du jtree
					path = null;
				}
			} else {
				// Créer et afficher la fenêtre de création
				CollectorAbstract<Triple<String,String,Integer>> collector = new CollectorAbstract<Triple<String,String,Integer>>() {
					/**
					 * @see ICollector#collect(Object)
					 */
					@Override
					public void collect (Triple<String, String, Integer> selection) {
						// Fermer le collecteur
						this.close();

						// Demander confirmation à l'utilisateur
						if (ViewHelper.confirmation(w_parent, "En éditant la phase qualificative "+(selection.getThird()+1)+" de la poule \""+selection.getSecond()+"\" de la catégorie \""+selection.getFirst()+"\" de cette manière, tous les matchs qu'elle possède seront perdus. Désirez-vous continuer ? *\n* Il est préférable d'éditer les matchs manuellement avec les opérations d'ajout, de modification et de suppression", true)) {
							// Récupérer les informations de la phase qualificative à éditer
							Triple<InfosConfiguration<String>, InfosModelPhaseQualificative, InfosModelMatchPhasesQualifs> infos = ContestOrg.get().getCtrlPhasesQualificatives().getInfosPhaseQualif(selection.getFirst(), selection.getSecond(), selection.getThird());
							
							// Créer et afficher la fenêtre d'édition (en prenant soin de supprimer la phase qualificative)
							CollectorPhaseQualifEditer collector = new CollectorPhaseQualifEditer(selection.getFirst(), selection.getSecond(), selection.getThird());
							JDialog jd_phase = new JDPhaseQualifEditer(w_parent, collector, selection.getFirst(), selection.getSecond(), infos);
							collector.setWindow(jd_phase);
							jd_phase.setVisible(true);
						}
					}
				};
				JDialog jd_categoriePoulePhaseQualif = new JDCategoriePoulePhaseQualif(this.w_parent, collector, selection.getFirst(), selection.getSecond());
				collector.setWindow(jd_categoriePoulePhaseQualif);
				jd_categoriePoulePhaseQualif.setVisible(true);
			}
		} else if (event.getSource() == this.jb_supprimerPhase) {
			// Récupérer la catégorie, la poule et la phase qualificative
			Triple<String, String, Integer> selection = this.getSelection(-1, null, true);
			
			// Vérifier si la séléction est correcte
			if (selection.getFirst() != null && selection.getSecond() != null && selection.getThird() != null) {
				// Demander la confirmation de l'utilisateur
				if (ViewHelper.confirmation(this.w_parent, "Désirez-vous vraiment supprimer la phase qualificative "+(selection.getThird()+1)+" de la poule \""+selection.getSecond()+"\" de la catégorie \""+selection.getFirst()+"\" ?", true)) {
					// Demander la suppression de la phase qualificative
					ContestOrg.get().getCtrlPhasesQualificatives().removePhaseQualif(selection.getFirst(), selection.getSecond(), selection.getThird());
					
					// Perdre la sélection du jtree
					path = null;
				}
			} else {
				// Créer et afficher la fenêtre de création
				CollectorAbstract<Triple<String,String,Integer>> collector = new CollectorAbstract<Triple<String,String,Integer>>() {
					/**
					 * @see ICollector#collect(Object)
					 */
					@Override
					public void collect (Triple<String, String, Integer> selection) {
						// Fermer le collecteur
						this.close();

						// Demander la confirmation de l'utilisateur
						if (ViewHelper.confirmation(w_parent, "Désirez-vous vraiment supprimer la phase qualificative "+(selection.getThird()+1)+" de la poule \""+selection.getSecond()+"\" de la catégorie \""+selection.getFirst()+"\" ?", true)) {
							// Demander la suppression de la phase qualificative
							ContestOrg.get().getCtrlPhasesQualificatives().removePhaseQualif(selection.getFirst(), selection.getSecond(), selection.getThird());
						}
					}
				};
				JDialog jd_categoriePoulePhaseQualif = new JDCategoriePoulePhaseQualif(this.w_parent, collector, selection.getFirst(), selection.getSecond());
				collector.setWindow(jd_categoriePoulePhaseQualif);
				jd_categoriePoulePhaseQualif.setVisible(true);
			}
		} else if (event.getSource() == this.jb_exporter) {
			// Récupérer la catégorie, la poule et la phase qualificative
			Triple<String, String, Integer> selection = this.getSelection(-1, null, true);

			// Créer et afficher la fenêtre de gestion d'exportation
			new JDExporter(this.w_parent, InfosTheme.CATEGORIE_PHASES_QUALIFICATIVES, selection.getFirst(), selection.getSecond(), null).setVisible(true);
		} else if (event.getSource() == this.jb_editerMatch) {
			// Récupérer la catégorie, la poule et la phase qualificative
			Triple<String, String, Integer> selection = this.getSelection(4, "Veuillez séléctionner le match que vous désirez editer.", false);
			
			// Vérifier si la séléction est correcte
			if (selection != null) {
				// Résoudre les informations manquantes
				Quadruple<String, String, Integer, Integer> resolution = ContestOrg.get().getCtrlPhasesQualificatives().getCategoriePoulePhase(selection.getFirst(), selection.getSecond(), selection.getThird(), this.jtable.getRowSorter().convertRowIndexToModel(this.jtable.getSelectedRow()));
				
				// Récupérer les informations du match à éditer
				Quadruple<Triple<String, ArrayList<Pair<String, InfosModelObjectifRemporte>>, InfosModelParticipation>, Triple<String, ArrayList<Pair<String, InfosModelObjectifRemporte>>, InfosModelParticipation>, Pair<String, String>, InfosModelMatchPhasesQualifs> infos = ContestOrg.get().getCtrlPhasesQualificatives().getInfosMatch(resolution.getFirst(), resolution.getSecond(), resolution.getThird(), resolution.getFourth());
				
				// Créer et afficher la fenêtre d'édition
				CollectorMatchPhasesQualifsEditer collector = new CollectorMatchPhasesQualifsEditer(resolution.getFirst(), resolution.getSecond(), resolution.getThird(), resolution.getFourth());
				JDialog jd_match = new JDMatchPhasesQualifsEditer(this.w_parent, collector, resolution.getFirst(), resolution.getSecond(), resolution.getThird(), infos);
				collector.setWindow(jd_match);
				jd_match.setVisible(true);
			}
		} else if (event.getSource() == this.jb_supprimerMatch) {
			// Récupérer la catégorie, la poule et la phase qualificative
			Triple<String, String, Integer> selection = this.getSelection(4, "Veuillez séléctionner le match que vous désirez supprimer.", false);
			
			// Vérifier si la séléction est correcte
			if (selection != null) {
				// Résoudre les informations manquantes
				Quadruple<String, String, Integer, Integer> resolution = ContestOrg.get().getCtrlPhasesQualificatives().getCategoriePoulePhase(selection.getFirst(), selection.getSecond(), selection.getThird(), this.jtable.getRowSorter().convertRowIndexToModel(this.jtable.getSelectedRow()));
				
				// Demander la confirmation de l'utilisateur
				if (ViewHelper.confirmation(this.w_parent, "Désirez-vous vraiment supprimer ce match ?")) {
					// Demander la suppression du match
					ContestOrg.get().getCtrlPhasesQualificatives().removeMatch(resolution.getFirst(), resolution.getSecond(), resolution.getThird(), resolution.getFourth());
				}
			}
		}
		
		// Remettre la sélection du jtree initiale
		this.jtree.setSelectionPath(path);
	}
	
	/**
	 * Récupérer la catégorie, la poule et la phase qualificative séléctionnées
	 * @param level niveau désiré (1 = catégorie, 2 = poule, 3 = phase qualificative, -1 pour pas de niveau désiré)
	 * @param message message d'erreur affiché si la séléction de l'utilisateur n'est pas suffisante
	 * @param implicite deviner au possible la séléction au niveau désiré si la séléction de l'utilisateur n'est pas suffisante
	 * @return catégorie, la poule et la phase qualificative séléctionnées
	 */
	private Triple<String, String, Integer> getSelection (int level, String message, boolean implicite) {
		// Récupérer les nodes séléctionnées (explicitement et implicitement si nécéssaire)
		ITreeNode categorie = null, poule = null, phase = null;
		TreePath path = this.jtree.getSelectionPath();
		if (path != null) {
			// Récupérer la node séléctionnée
			ITreeNode node = (ITreeNode)path.getLastPathComponent();
			
			// Récupérer les nodes de la catégorie, de la poule et de la phase
			switch (node.getLevel()) {
				case 0: // Racine
					if (implicite && node.getChildCount() == 1) { // Est-ce qu'il y a qu'une seule catégorie ?
						categorie = (ITreeNode)node.getChildAt(0);
						if (categorie.getChildCount() == 1) { // Est-ce qu'il y a qu'une seule poule ?
							poule = (ITreeNode)categorie.getChildAt(0);
							if (poule.getChildCount() == 1) { // Est-ce qu'il y a qu'une seule phase qualificative ?
								phase = (ITreeNode)poule.getChildAt(0);
							}
						}
					}
					break;
				case 1: // Catégorie
					categorie = node;
					if (implicite && categorie.getChildCount() == 1) { // Est-ce qu'il y a qu'une seule poule ?
						poule = (ITreeNode)categorie.getChildAt(0);
						if (poule.getChildCount() == 1) { // Est-ce qu'il y a qu'une seule phase qualificative ?
							phase = (ITreeNode)poule.getChildAt(0);
						}
					}
					break;
				case 2: // Poule
					categorie = node.getParent();
					poule = node;
					if (implicite && poule.getChildCount() == 1) { // Est-ce qu'il y a qu'une seule phase qualificative ?
						phase = (ITreeNode)poule.getChildAt(0);
					}
					break;
				case 3: // Phase
					categorie = node.getParent().getParent();
					poule = node.getParent();
					phase = node;
					break;
			}
		} else if (implicite) {
			ITreeNode root = (ITreeNode)this.jtree.getModel().getRoot();
			if (root.getChildCount() == 1) { // Est-ce qu'il y a qu'une seule catégorie ?
				categorie = (ITreeNode)root.getChildAt(0);
				if (categorie.getChildCount() == 1) { // Est-ce qu'il y a qu'une seule poule ?
					poule = (ITreeNode)categorie.getChildAt(0);
					if (poule.getChildCount() == 1) { // Est-ce qu'il y a qu'une seule phase qualificative ?
						phase = (ITreeNode)poule.getChildAt(0);
					}
				}
			}
		}
		
		// Récupérer le nom de la catégorie, le nom de la poule et numéro de la phase
		String nomCategorie = categorie == null ? null : ((InfosModelCategorie)categorie.getObject()).getNom();
		String nomPoule = poule == null ? null : ((InfosModelPoule)poule.getObject()).getNom();
		Integer numeroPhase = phase == null ? null : poule.getIndex(phase);
		
		// Récupérer si toutes les données sont disponibles
		if (level == -1 || level == 1 && nomCategorie != null || level == 2 && nomPoule != null || level == 3 && numeroPhase != null || level == 4 && this.jtable.getSelectedRow() != -1) {
			// Retourner les informations
			return new Triple<String, String, Integer>(nomCategorie, nomPoule, numeroPhase);
		} else {
			// Erreur
			if(message != null) {
				ViewHelper.derror(this.w_parent, message);
			}
			
			// Retourner null
			return null;
		}
	}
	
	/**
	 * Rafraichir les boutons
	 */
	private void refreshButtons () {
		// Rafraichir les boutons
		this.jb_exporter.setEnabled(ContestOrg.get().is(ContestOrg.STATE_EDIT));
		this.jb_nouvellePhase.setEnabled(ContestOrg.get().is(ContestOrg.STATE_EDIT));
		this.jb_genererPhase.setEnabled(ContestOrg.get().is(ContestOrg.STATE_EDIT));
		this.jb_editerMatch.setEnabled(ContestOrg.get().is(ContestOrg.STATE_EDIT) && this.jtable.getSelectedRowCount() == 1);
		this.jb_supprimerMatch.setEnabled(ContestOrg.get().is(ContestOrg.STATE_EDIT) && this.jtable.getSelectedRowCount() == 1);
		this.jb_exporter.setEnabled(ContestOrg.get().is(ContestOrg.STATE_OPEN));
		this.jb_nouveauMatch.setEnabled(ContestOrg.get().is(ContestOrg.STATE_EDIT));
		this.jb_editerPhase.setEnabled(ContestOrg.get().is(ContestOrg.STATE_EDIT));
		this.jb_supprimerPhase.setEnabled(ContestOrg.get().is(ContestOrg.STATE_EDIT));
	}
	
	// Implémentation de MouseListener
	@Override
	public void mouseClicked (MouseEvent event) {
		if(event.getSource() == this.jtable) {
			// Vérifier s'il s'agit d'un double click
			if(event.getClickCount() == 2) {
				// Récupérer la catégorie, la poule et la phase qualificative
				Triple<String, String, Integer> selection = this.getSelection(4, null, false);
				
				// Vérifier si la séléction est correcte
				if (selection != null) {
					// Résoudre les informations manquantes
					Quadruple<String, String, Integer, Integer> resolution = ContestOrg.get().getCtrlPhasesQualificatives().getCategoriePoulePhase(selection.getFirst(), selection.getSecond(), selection.getThird(), this.jtable.getRowSorter().convertRowIndexToModel(this.jtable.getSelectedRow()));
					
					// Récupérer les informations du match à éditer
					Quadruple<Triple<String, ArrayList<Pair<String, InfosModelObjectifRemporte>>, InfosModelParticipation>, Triple<String, ArrayList<Pair<String, InfosModelObjectifRemporte>>, InfosModelParticipation>, Pair<String, String>, InfosModelMatchPhasesQualifs> infos = ContestOrg.get().getCtrlPhasesQualificatives().getInfosMatch(resolution.getFirst(), resolution.getSecond(), resolution.getThird(), resolution.getFourth());
					
					// Créer et afficher la fenêtre d'édition
					CollectorMatchPhasesQualifsEditer collector = new CollectorMatchPhasesQualifsEditer(resolution.getFirst(), resolution.getSecond(), resolution.getThird(), resolution.getFourth());
					JDialog jd_match = new JDMatchPhasesQualifsEditer(this.w_parent, collector, resolution.getFirst(), resolution.getSecond(), resolution.getThird(), infos);
					collector.setWindow(jd_match);
					jd_match.setVisible(true);
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
