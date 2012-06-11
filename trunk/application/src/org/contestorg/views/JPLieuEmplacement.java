package org.contestorg.views;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.contestorg.common.Triple;
import org.contestorg.controllers.ContestOrg;
import org.contestorg.infos.InfosModelEmplacement;
import org.contestorg.infos.InfosModelHoraire;
import org.contestorg.infos.InfosModelLieu;

/**
 * Panel de séléction d'un emplacement d'un lieu
 */
@SuppressWarnings("serial")
public class JPLieuEmplacement extends JPanel implements ItemListener, ChangeListener
{
	/** Listeners */
	private ArrayList<ItemListener> listeners = new ArrayList<ItemListener>();
	
	/** Lieux et emplacements */
	private ArrayList<Triple<InfosModelLieu, ArrayList<InfosModelEmplacement>, ArrayList<InfosModelHoraire>>> lieuxEmplacements;
	
	// Entrées
	
	/** Lieu */
	protected JComboBox<String> jcb_lieu = new JComboBox<String>();
	
	/** Emplacement */
	protected JComboBox<String> jcb_emplacement = new JComboBox<String>();
	
	/** Spécifier le lieu et l'emplacement ? */
	protected JCheckBox jcb_lieuEmplacement;
	
	/**
	 * Constructeur
	 */
	public JPLieuEmplacement() {
		// Configurer le panel
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		// Récupérer les lieux et leurs emplacements
		this.lieuxEmplacements = ContestOrg.get().getListeLieuxEmplacement();
		
		// Remplir les listes
		int nbLieux = 0; int nbEmplacements = 0;
		for(int i=0;i<this.lieuxEmplacements.size();i++) {
			if(this.lieuxEmplacements.get(i).getSecond().size() != 0) {
				this.jcb_lieu.addItem(this.lieuxEmplacements.get(i).getFirst().getNom());
				ArrayList<InfosModelEmplacement> emplacements = this.lieuxEmplacements.get(i).getSecond();
				if(nbLieux == 0) {
					for(int j=0;j<emplacements.size();j++) {
						this.jcb_emplacement.addItem(emplacements.get(j).getNom());
					}
				}
				nbLieux++;
				nbEmplacements += emplacements.size();
			}
		}
		
		// Désactiver les listes par défaut
		this.jcb_lieu.setEnabled(false);
		this.jcb_emplacement.setEnabled(false);
		
		// Remplir le panel
		if(nbLieux > 1) {
			// Ajouter la case à cocher
			this.add(ViewHelper.title("Lieu et emplacement", ViewHelper.H1));
			this.jcb_lieuEmplacement = new JCheckBox("Spécifier le lieu et l'emplacement ?",false);
			this.add(ViewHelper.left(this.jcb_lieuEmplacement));
			this.jcb_lieuEmplacement.addChangeListener(this);
			
			// Ajouter les listes
			JLabel[] jls_lieu = { new JLabel("Lieu : "), new JLabel("Emplacement : ") };
			JComponent[] jcs_lieu = { this.jcb_lieu, this.jcb_emplacement };
			this.add(ViewHelper.inputs(jls_lieu, jcs_lieu));
		} else  if(nbEmplacements > 1) {
			// Ajouter la case à coucher
			this.add(ViewHelper.title("Emplacement", ViewHelper.H1));
			this.jcb_lieuEmplacement = new JCheckBox("Spécifier l'emplacement ?",false);
			this.add(ViewHelper.left(this.jcb_lieuEmplacement));
			this.jcb_lieuEmplacement.addChangeListener(this);
			
			// Ajouter la liste
			JLabel[] jls_emplacement = { new JLabel("Emplacement : ") };
			JComponent[] jcs_emplacement = { this.jcb_emplacement };
			this.add(ViewHelper.inputs(jls_emplacement, jcs_emplacement));
		}
		
		// Ecouter la liste des lieux
		this.jcb_lieu.addItemListener(this);
	}
	
	/**
	 * Récupérer le nom du lieu séléctionné
	 * @return nom du lieu séléctionné
	 */
	public String getNomLieu() {
		return this.jcb_lieuEmplacement.isSelected() ? (String)this.jcb_lieu.getSelectedItem() : null;
	}
	
	/**
	 * Récupérer le nom de l'emplacement séléctionné
	 * @return nom de l'emplacement séléctionné
	 */
	public String getNomEmplacement() {
		return this.jcb_lieuEmplacement.isSelected() ? (String)this.jcb_emplacement.getSelectedItem() : null;
	}
	
	/**
	 * Définir le nom du lieu séléctionné
	 * @param nomLieu nom du lieu
	 */
	public void setNomLieu(String nomLieu) {
		if(nomLieu != null) {
			this.jcb_lieu.setSelectedItem(nomLieu);
			this.jcb_lieuEmplacement.setSelected(true);
			this.jcb_lieu.setEnabled(true);
			this.jcb_emplacement.setEnabled(true);
		} else {
			this.jcb_lieuEmplacement.setSelected(false);
			this.jcb_lieu.setEnabled(false);
			this.jcb_emplacement.setEnabled(false);
		}
	}
	
	/**
	 * Définir l'emplacement séléctionné
	 * @param nomEmplacement nom de l'emplacement
	 */
	public void setNomEmplacement(String nomEmplacement) {
		if(nomEmplacement != null) {
			this.jcb_emplacement.setSelectedItem(nomEmplacement);
			this.jcb_lieuEmplacement.setSelected(true);
			this.jcb_lieu.setEnabled(true);
			this.jcb_emplacement.setEnabled(true);
		} else {
			this.jcb_lieuEmplacement.setSelected(false);
			this.jcb_lieu.setEnabled(false);
			this.jcb_emplacement.setEnabled(false);
		}
	}

	/**
	 * @see ItemListener#itemStateChanged(ItemEvent)
	 */
	@Override
	public void itemStateChanged (ItemEvent event) {
		// Supprimer les listeners
		for(ItemListener listener : this.listeners) {
			this.jcb_emplacement.removeItemListener(listener);
		}
		
		// Vider la liste des emplacements
		this.jcb_emplacement.removeAllItems();
		
		// Récupérer le nom du lieu séléctionné
		String nomLieu = (String)event.getItem();
		
		// Remplir la liste des emplacements avec les emplacements du lieu
		for(int i=0;i<this.lieuxEmplacements.size();i++) {
			if(this.lieuxEmplacements.get(i).getFirst().getNom().equals(nomLieu)) {
				for(int j=0;j<this.lieuxEmplacements.get(i).getSecond().size();j++) {
					this.jcb_emplacement.addItem(this.lieuxEmplacements.get(i).getSecond().get(j).getNom());
				}
			}
		}
		
		// Propager l'événement
		for(ItemListener listener : this.listeners) {
			listener.itemStateChanged(event);
			this.jcb_emplacement.addItemListener(listener);
		}
	}
	
	/**
	 * @see JComboBox#addItemListener(ItemListener)
	 */
	public void addItemListener(ItemListener listener) {
		this.jcb_emplacement.addItemListener(listener);
		this.listeners.add(listener);
	}

	/**
	 * @see ChangeListener#stateChanged(ChangeEvent)
	 */
	@Override
	public void stateChanged (ChangeEvent event) {
		this.jcb_lieu.setEnabled(this.jcb_lieuEmplacement.isSelected());
		this.jcb_emplacement.setEnabled(this.jcb_lieuEmplacement.isSelected());
	}
}
