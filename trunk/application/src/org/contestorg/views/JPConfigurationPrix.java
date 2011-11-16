package org.contestorg.views;


import java.awt.Window;
import java.util.ArrayList;

import org.contestorg.common.TrackableList;
import org.contestorg.controlers.ContestOrg;
import org.contestorg.infos.InfosModelPrix;



@SuppressWarnings("serial")
public class JPConfigurationPrix extends JPConfigurationAbstract
{

	// TableModel de la liste des prix
	private TMPrix tm_prix;
	
	// Constructeur
	public JPConfigurationPrix(Window w_parent) {
		// Appeller le constructeur du parent
		super(w_parent);

		// Liste des prix
		this.jp_contenu.add(ViewHelper.title("Liste des prix à remporter", ViewHelper.H1));
		this.tm_prix = new TMPrix(this.w_parent);
		this.tm_prix.addValidator(ContestOrg.get().getPrixValidator());
		this.jp_contenu.add(new JPTable<InfosModelPrix>(this.w_parent, this.tm_prix));
	}
	
	// Getters
	public TrackableList<InfosModelPrix> getPrix() {
		return new TrackableList<InfosModelPrix>(this.tm_prix);
	}
	
	// Setters
	public void setPrix(ArrayList<InfosModelPrix> prix) {
		this.tm_prix.fill(prix);
	}

	// Vérifier la validité des données
	public boolean check () {
		return true;
	}
}
