package views;

import infos.InfosModelPropriete;

import java.awt.Window;
import java.util.ArrayList;

import common.TrackableList;

import controlers.ContestOrg;

@SuppressWarnings("serial")
public class JPConfigurationProprietes extends JPConfigurationAbstract
{
	// TableModel des propriétés
	private TMProprietes tm_proprietes;
	
	// Constructeur
	public JPConfigurationProprietes(Window w_parent) {
		// Appeller le constructeur du parent
		super(w_parent);

		// Liste des propriétés
		this.jp_contenu.add(ViewHelper.title("Liste des propriétés des participants", ViewHelper.H1));
		this.tm_proprietes = new TMProprietes(this.w_parent);
		this.tm_proprietes.addValidator(ContestOrg.get().getProprietesValidator());
		this.jp_contenu.add(new JPTable<InfosModelPropriete>(this.w_parent, this.tm_proprietes));
	}
	
	// Getters
	public TrackableList<InfosModelPropriete> getProprietes() {
		return new TrackableList<InfosModelPropriete>(this.tm_proprietes);
	}
	
	// Setters
	public void setProprietes(ArrayList<InfosModelPropriete> proprietes) {
		this.tm_proprietes.fill(proprietes);
	}

	// Vérifier la validité des données
	public boolean check () {
		return true;
	}
}
