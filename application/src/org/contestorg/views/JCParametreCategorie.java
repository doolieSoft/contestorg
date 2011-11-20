package org.contestorg.views;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

import javax.swing.JComboBox;

import org.contestorg.common.Pair;
import org.contestorg.controlers.ContestOrg;
import org.contestorg.infos.InfosModelCategorie;
import org.contestorg.infos.InfosModelParticipant;
import org.contestorg.infos.InfosModelPoule;
import org.contestorg.infos.Parametre;
import org.contestorg.interfaces.IChangeable;
import org.contestorg.interfaces.IChangeableListener;



@SuppressWarnings("serial")
public class JCParametreCategorie extends JCParametreAbstract implements IChangeable<Integer>, ItemListener
{
	// Liste associée au composant
	private JComboBox jcb_categorie = new JComboBox();

	// Listeners
	private ArrayList<IChangeableListener<Integer>> listeners = new ArrayList<IChangeableListener<Integer>>();
	
	// Ids des catégories
	private ArrayList<Integer> idsCategories = new  ArrayList<Integer>();

	// Constructeur
	public JCParametreCategorie(Parametre parametre) {
		// Appel du constructeur parent
		super(parametre);
		
		// Ajouter la liste dans le panel
		this.setLayout(new BorderLayout());
		this.add(this.jcb_categorie);
		
		// Remplir la liste
		if(ContestOrg.get().is(ContestOrg.STATE_OPEN)) {
			this.jcb_categorie.addItem("Veuillez séléctionner une catégorie");
			for(Pair<InfosModelCategorie, ArrayList<Pair<InfosModelPoule, ArrayList<InfosModelParticipant>>>> categorie : ContestOrg.get().getCtrlParticipants().getListeCategoriesPoulesParticipants()) {
				this.jcb_categorie.addItem(categorie.getFirst().getNom());
				this.idsCategories.add(categorie.getFirst().getId());
			}
			if(this.jcb_categorie.getItemCount() == 2) {
				this.jcb_categorie.setSelectedIndex(1);
			}
		} else {
			this.jcb_categorie.addItem("Pas encore de catégories");
			this.jcb_categorie.setBackground(new Color(250, 90, 90));
		}
		
		// Ecouter la liste
		this.jcb_categorie.addItemListener(this);
	}
	
	// Récupérer l'id de la catégorie séléctionnée
	private Integer getIdCategorie() {
		return this.jcb_categorie.getItemCount() == 0 || this.jcb_categorie.getSelectedIndex() == 0 ? null : this.idsCategories.get(this.jcb_categorie.getSelectedIndex()-1);
	}

	// Implémentation de getValeur
	@Override
	public String getValeur () {
		Integer idCategorie = this.getIdCategorie();
		return idCategorie == null ? null : String.valueOf(idCategorie);
	}
	
	// Implémentation de setValeur
	@Override
	public void setValeur (String valeur) {
		try {
			this.jcb_categorie.setSelectedIndex(valeur == null ? 0 : this.idsCategories.indexOf(Integer.parseInt(valeur))+1);
		} catch(NumberFormatException e) { }
	}
	
	// Implémentation de getError
	@Override
	public String getError () {
		// Vérifier si le paramètre est optionnel
		if(!this.parametre.isOptionnel()) {
			// Vérifier si le concours est bien ouvert
			if(!ContestOrg.get().is(ContestOrg.STATE_OPEN)) {
				return "Le paramètre \""+this.parametre.getNom()+"\" ne peut pas être défini tant que le concours n'est pas créé.";
			}
			
			// Vérifier si la valeur n'est pas vide
			if(this.getValeur() == null || this.getValeur().isEmpty()) {
				return "Le paramètre \""+this.parametre.getNom()+"\" est obligatoire.";
			}
		}
		
		// Retourner null
		return null;
	}

	// Implémentation de link
	@Override
	public void link (JCParametreAbstract[] composants) {
	}
	
	// Implémentation de IChangeable
	@Override
	public void addListener (IChangeableListener<Integer> listener) {
		this.listeners.add(listener);
		listener.change(this.getIdCategorie());
	}
	@Override
	public void removeListener (IChangeableListener<Integer> listener) {
		this.listeners.remove(listener);
	}

	// Implémentation de ItemListener
	@Override
	public void itemStateChanged (ItemEvent event) {
		if(event.getStateChange() == ItemEvent.SELECTED) {
			// Fire des listeners
			for(IChangeableListener<Integer> listener : this.listeners) {
				listener.change(this.getIdCategorie());
			}
		}
	}
	
}
