package models;

import infos.InfosModelTheme;

import java.util.ArrayList;
import java.util.HashMap;

public class ModelTheme extends ModelAbstract
{
	// Attributs scalaires
	private String chemin;
	private HashMap<String, String> parametres;
	private HashMap<String, String> fichiers;
	
	// Constructeur
	public ModelTheme(InfosModelTheme infos) {
		// Enregistrer les informations
		this.setInfos(infos);
	}
	protected ModelTheme(ModelTheme ressources) {
		// Appeller le constructeur principal
		this(ressources.toInformation());
		
		// Récupérer l'id
		this.setId(ressources.getId());
	}
	
	// Getters
	public String getChemin () {
		return chemin;
	}
	public HashMap<String, String> getParametres () {
		return this.parametres == null ? null : new HashMap<String, String>(this.parametres);
	}
	public HashMap<String, String> getFichiers () {
		return this.fichiers == null ? null : new HashMap<String, String>(this.fichiers);
	}
	
	// Setters
	protected void setInfos (InfosModelTheme infos) {
		// Appeller le setInfos du parent
		super.setInfos(infos);
		
		// Enregistrer les informations
		this.chemin = infos.getChemin();
		this.parametres = infos.getParametres();
		this.fichiers = infos.getFichiers();
		
		// Fire update
		this.fireUpdate();
	}
	
	// Clone
	protected ModelTheme clone () {
		return new ModelTheme(this);
	}
	
	// ToInformation
	public InfosModelTheme toInformation () {
		InfosModelTheme infos = new InfosModelTheme(this.chemin, this.parametres, this.fichiers);
		infos.setId(this.getId());
		return infos;
	}
	
	// Remove
	@Override
	protected void delete (ArrayList<ModelAbstract> removers) throws ContestOrgModelException {
		if (!removers.contains(this)) {
			// Ajouter le type à la liste des removers
			removers.add(this);
			
			// Fire delete
			this.fireDelete();
		}
	}
	
}
