package org.contestorg.views;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

/**
 * Panel d'un tableau
 * @param <T> classe des objets du modèle de données
 */
@SuppressWarnings("serial")
public class JPTable<T> extends JPanel implements ActionListener, ListSelectionListener, TableModelListener
{

	/** Fenêtre parent */
	private Window w_parent;

	/** Tableau */
	private JTable jtable;

	/** Modèle de données */
	private TMAbstract<T> tablemodel;

	// Boutons
	
	/** Bouton "Ajouter" */
	private JButton jb_ajouter = new JButton("Ajouter", new ImageIcon("img/farm/16x16/add.png"));
	
	/** Bouton "Editer" */
	private JButton jb_editer = new JButton("Editer", new ImageIcon("img/farm/16x16/pencil.png"));;
	
	/** Bouton "Monter" */
	private JButton jb_monter = new JButton("Monter", new ImageIcon("img/farm/16x16/resultset_up.png"));;
	
	/** Bouton "Baisser" */
	private JButton jb_baisser = new JButton("Baisser", new ImageIcon("img/farm/16x16/resultset_down.png"));;
	
	/** Bouton "Supprimer */
	private JButton jb_supprimer = new JButton("Supprimer", new ImageIcon("img/farm/16x16/cross.png"));

	// Constructeurs
	
	/**
	 * Constructeur
	 * @param w_parent fenêtre parent
	 * @param tablemodel modèle de données
	 */
	public JPTable(Window w_parent, TMAbstract<T> tablemodel) {
		this(w_parent, tablemodel, true, true, false, false, true);
	}
	
	/**
	 * Constructeur
	 * @param w_parent fenêtre parent
	 * @param tablemodel modèle de données
	 * @param visibleRows nombre de lignes visibles
	 */
	public JPTable(Window w_parent, TMAbstract<T> tablemodel, int visibleRows) {
		this(w_parent, tablemodel, true, true, false, false, true, visibleRows);
	}
	
	/**
	 * Constructeur
	 * @param w_parent fenêtre parent
	 * @param tablemodel modèle de données
	 * @param addButton afficher le bouton "Ajouter" ?
	 * @param editButton afficher le bouton "Editer" ?
	 * @param moveUpButton afficher le bouton "Monter" ?
	 * @param moveDownButton afficher le bouton "Baisser" ?
	 * @param deleteButton afficher le bouton "Supprimer" ?
	 */
	public JPTable(Window w_parent, TMAbstract<T> tablemodel, boolean addButton, boolean editButton, boolean moveUpButton, boolean moveDownButton, boolean deleteButton) {
		this(w_parent, tablemodel, addButton, editButton, moveUpButton, moveDownButton, deleteButton, 6);
	}
	
	/**
	 * Constructeur
	 * @param w_parent fenêtre parent
	 * @param tablemodel modèle de données
	 * @param addButton afficher le bouton "Ajouter" ?
	 * @param editButton afficher le bouton "Editer" ?
	 * @param moveUpButton afficher le bouton "Monter" ?
	 * @param moveDownButton afficher le bouton "Baisser" ?
	 * @param deleteButton afficher le bouton "Supprimer" ?
	 * @param visibleRows nombre de lignes visibles
	 */
	public JPTable(Window w_parent, TMAbstract<T> tablemodel, boolean addButton, boolean editButton, boolean moveUpButton, boolean moveDownButton, boolean deleteButton, int visibleRows) {
		// Retenir le parent et le TableModel
		this.w_parent = w_parent;
		this.tablemodel = tablemodel;

		// Panel de contenu
		JPanel jp_contenu = new JPanel();
		jp_contenu.setLayout(new BoxLayout(jp_contenu, BoxLayout.Y_AXIS));
		this.setLayout(new BorderLayout());
		this.add(jp_contenu, BorderLayout.NORTH);

		// Ajouter la table
		this.jtable = new JTable(tablemodel);
		this.jtable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		int height = 0; 
	    for(int row=0; row< visibleRows; row++) {
	    	height += this.jtable.getRowHeight(row);
	    }
		this.jtable.setPreferredScrollableViewportSize(new Dimension(this.jtable.getPreferredScrollableViewportSize().width, height));
		this.jtable.setColumnSelectionAllowed(false);
		jp_contenu.add(new JScrollPane(this.jtable));

		// Boutons d'ajout, d'édition et de suppression
		JPanel jp_objectifs = new JPanel(new FlowLayout(FlowLayout.CENTER));
		if(addButton) jp_objectifs.add(this.jb_ajouter);
		if(editButton) jp_objectifs.add(this.jb_editer);
		if(moveUpButton) jp_objectifs.add(this.jb_monter);
		if(moveDownButton) jp_objectifs.add(this.jb_baisser);
		if(deleteButton) jp_objectifs.add(this.jb_supprimer);
		jp_contenu.add(jp_objectifs);
		
		// Désactiver certains boutons
		this.jb_editer.setEnabled(false);
		this.jb_monter.setEnabled(false);
		this.jb_baisser.setEnabled(false);
		this.jb_supprimer.setEnabled(false);

		// Ecouter les boutons
		this.jb_ajouter.addActionListener(this);
		this.jb_editer.addActionListener(this);
		this.jb_monter.addActionListener(this);
		this.jb_baisser.addActionListener(this);
		this.jb_supprimer.addActionListener(this);
		
		// Ecouter la table
		this.jtable.getSelectionModel().addListSelectionListener(this);
		this.tablemodel.addTableModelListener(this);
	}

	/**
	 * @see ActionListener#actionPerformed(ActionEvent)
	 */
	@Override
	public void actionPerformed (ActionEvent event) {
		if (event.getSource() == this.jb_ajouter) {
			// Appeller la méthode add du TableModel
			this.tablemodel.launchAddWindow();
		} else if (event.getSource() == this.jb_editer) {
			// Vérifier si une ligne est bien séléctionnée
			if (this.jtable.getSelectedRow() == -1) {
				ViewHelper.derror(this.w_parent, "Veuillez séléctionner l'élément que vous désirez éditer.");
			} else {
				// Appeller la méthode update du TableModel
				this.tablemodel.launchUpdateWindow(this.jtable.getSelectedRow());
			}
		} else if (event.getSource() == this.jb_monter) {
			// Vérifier si une ligne est bien séléctionnée
			if (this.jtable.getSelectedRow() == -1) {
				ViewHelper.derror(this.w_parent, "Veuillez séléctionner l'élément que vous désirez monter.");
			} else {
				// Appeller la méthode moveUp du TableModel
				if(this.tablemodel.moveUp(this.jtable.getSelectedRow())) {
					// Déplacer la séléction avec la ligne
					this.jtable.getSelectionModel().setSelectionInterval(this.jtable.getSelectedRow()-1, this.jtable.getSelectedRow()-1);
				}
			}
		} else if (event.getSource() == this.jb_baisser) {
			// Vérifier si une ligne est bien séléctionnée
			if (this.jtable.getSelectedRow() == -1) {
				ViewHelper.derror(this.w_parent, "Veuillez séléctionner l'élément que vous désirez baisser.");
			} else {
				// Appeller la méthode moveDown du TableModel
				if(this.tablemodel.moveDown(this.jtable.getSelectedRow())) {
					// Déplacer la séléction avec la ligne
					this.jtable.getSelectionModel().setSelectionInterval(this.jtable.getSelectedRow()+1, this.jtable.getSelectedRow()+1);
				}
			}
		} else if (event.getSource() == this.jb_supprimer) {
			// Vérifier si une ligne est bien séléctionnée
			if (this.jtable.getSelectedRow() == -1) {
				ViewHelper.derror(this.w_parent, "Veuillez séléctionner l'élément que vous désirez supprimer.");
			} else {
				// Appeller la méthode delete du TableModel
				this.tablemodel.launchDeleteWindow(this.jtable.getSelectedRow());
			}
		}
	}
	
	/**
	 * Mettre à jour les boutons
	 */
	private void updateButtons() {
		// Mettre à jour les boutons
		this.jb_editer.setEnabled(this.jtable.getSelectedRowCount() == 1);
		this.jb_monter.setEnabled(this.jtable.getSelectedRowCount() == 1 && this.jtable.getSelectedRow() != 0);
		this.jb_baisser.setEnabled(this.jtable.getSelectedRowCount() == 1 && this.jtable.getSelectedRow() != this.jtable.getRowCount()-1);
		this.jb_supprimer.setEnabled(this.jtable.getSelectedRowCount() == 1);
	}
	
	/**
	 * @see ListSelectionListener#valueChanged(ListSelectionEvent)
	 */
	@Override
	public void valueChanged (ListSelectionEvent event) {
		// Mettre à jour les boutons
		this.updateButtons();
	}
	
	/**
	 * @see TableModelListener#tableChanged(TableModelEvent)
	 */
	@Override
	public void tableChanged (TableModelEvent event) {
		// Mettre à jour les boutons
		this.updateButtons();
	}

}
