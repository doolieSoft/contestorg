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

@SuppressWarnings("serial")
public class JPTable<T> extends JPanel implements ActionListener, ListSelectionListener, TableModelListener
{

	// Parent
	private Window w_parent;

	// Boutons
	private JButton jb_ajouter = new JButton("Ajouter", new ImageIcon("img/farm/16x16/add.png"));
	private JButton jb_editer = new JButton("Editer", new ImageIcon("img/farm/16x16/pencil.png"));;
	private JButton jb_monter = new JButton("Monter", new ImageIcon("img/farm/16x16/resultset_up.png"));;
	private JButton jb_baisser = new JButton("Baisser", new ImageIcon("img/farm/16x16/resultset_down.png"));;
	private JButton jb_supprimer = new JButton("Supprimer", new ImageIcon("img/farm/16x16/cross.png"));

	// JTable
	private JTable jtable;

	// TableModel
	private TMAbstract<T> tablemodel;

	// Constructeur
	public JPTable(Window w_parent, TMAbstract<T> tm) {
		this(w_parent, tm, true, true, false, false, true);
	}
	public JPTable(Window w_parent, TMAbstract<T> tm, int visibleRows) {
		this(w_parent, tm, true, true, false, false, true, visibleRows);
	}
	public JPTable(Window w_parent, TMAbstract<T> tm, boolean addButton, boolean editButton, boolean moveUpButton, boolean moveDownButton, boolean deleteButton) {
		this(w_parent, tm, addButton, editButton, moveUpButton, moveDownButton, deleteButton, 6);
	}
	public JPTable(Window w_parent, TMAbstract<T> tm, boolean addButton, boolean editButton, boolean moveUpButton, boolean moveDownButton, boolean deleteButton, int visibleRows) {
		// Retenir le parent et le TableModel
		this.w_parent = w_parent;
		this.tablemodel = tm;

		// Panel de contenu
		JPanel jp_contenu = new JPanel();
		jp_contenu.setLayout(new BoxLayout(jp_contenu, BoxLayout.Y_AXIS));
		this.setLayout(new BorderLayout());
		this.add(jp_contenu, BorderLayout.NORTH);

		// Ajouter la table
		this.jtable = new JTable(tm);
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

	// Implémentation de ActionListener
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
	
	// Mettre à jour les boutons
	private void updateButtons() {
		// Mettre à jour les boutons
		this.jb_editer.setEnabled(this.jtable.getSelectedRowCount() == 1);
		this.jb_monter.setEnabled(this.jtable.getSelectedRowCount() == 1 && this.jtable.getSelectedRow() != 0);
		this.jb_baisser.setEnabled(this.jtable.getSelectedRowCount() == 1 && this.jtable.getSelectedRow() != this.jtable.getRowCount()-1);
		this.jb_supprimer.setEnabled(this.jtable.getSelectedRowCount() == 1);
	}
	
	// Implémentation de ListSelectionListener
	@Override
	public void valueChanged (ListSelectionEvent event) {
		// Mettre à jour les boutons
		this.updateButtons();
	}
	
	// Implémentation de TableModelListener
	@Override
	public void tableChanged (TableModelEvent event) {
		// Mettre à jour les boutons
		this.updateButtons();
	}

}
