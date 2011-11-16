﻿package views;

import infos.InfosModelEmplacement;
import infos.InfosModelHoraire;
import infos.InfosModelLieu;

import java.awt.Window;
import java.util.ArrayList;

import common.TrackableList;
import common.Triple;

import controlers.ContestOrg;

@SuppressWarnings("serial")
public class JPConfigurationLieux extends JPConfigurationAbstract
{

	// TableModel de la liste des lieux
	private TMLieux tm_lieux;
	
	// Constructeur JPConcoursLieux
	public JPConfigurationLieux(Window w_parent) {
		// Appeller le constructeur du parent
		super(w_parent);

		// Liste des lieux
		this.jp_contenu.add(ViewHelper.title("Liste des lieux", ViewHelper.H1));
		this.tm_lieux = new TMLieux(this.w_parent);
		this.tm_lieux.addValidator(ContestOrg.get().getLieuxValidator());
		this.jp_contenu.add(new JPTable<Triple<InfosModelLieu,TrackableList<InfosModelEmplacement>,TrackableList<InfosModelHoraire>>>(this.w_parent, this.tm_lieux));
	}
	
	// Getters
	public TrackableList<Triple<InfosModelLieu,TrackableList<InfosModelEmplacement>,TrackableList<InfosModelHoraire>>> getLieux() {
		return new TrackableList<Triple<InfosModelLieu,TrackableList<InfosModelEmplacement>,TrackableList<InfosModelHoraire>>>(this.tm_lieux);
	}
	
	// Setters
	public void setLieux(ArrayList<Triple<InfosModelLieu,ArrayList<InfosModelEmplacement>,ArrayList<InfosModelHoraire>>> lieux) {
		this.tm_lieux.fill(this.getArrayListsToLists(lieux));
	}

	// Transformer les ArrayList<InfosModelEmplacement>/ArrayList<InfosModelHoraire> en List<InfosModelEmplacement>/List<InfosModelHoraire>
	private ArrayList<Triple<InfosModelLieu,TrackableList<InfosModelEmplacement>,TrackableList<InfosModelHoraire>>> getArrayListsToLists(ArrayList<Triple<InfosModelLieu,ArrayList<InfosModelEmplacement>,ArrayList<InfosModelHoraire>>> arraylists) {
		// Initialiser la List
		ArrayList<Triple<InfosModelLieu,TrackableList<InfosModelEmplacement>,TrackableList<InfosModelHoraire>>> lieux = new ArrayList<Triple<InfosModelLieu,TrackableList<InfosModelEmplacement>,TrackableList<InfosModelHoraire>>>();
		
		// Modifier les ArrayLists en Lists
		if(arraylists != null) {
			// Pour chaque ArrayList
			for(Triple<InfosModelLieu,ArrayList<InfosModelEmplacement>,ArrayList<InfosModelHoraire>> lieu : arraylists) {
				// Modifier l'ArrayList des emplacements en List
				TrackableList<InfosModelEmplacement> emplacements = new TrackableList<InfosModelEmplacement>(lieu.getSecond());

				// Modifier l'ArrayList des horaires en List
				TrackableList<InfosModelHoraire> horaires = new TrackableList<InfosModelHoraire>(lieu.getThird());
				
				// Ajouter le lieu dans la List
				lieux.add(new Triple<InfosModelLieu, TrackableList<InfosModelEmplacement>, TrackableList<InfosModelHoraire>>(lieu.getFirst(), emplacements, horaires));
			}
		}
		
		// Retourner la List
		return lieux;
	}

	// Vérifier la validité des données
	public boolean check () {
		return true;
	}
}
