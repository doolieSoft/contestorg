package org.contestorg.models;


import java.util.ArrayList;

import org.contestorg.infos.InfosConnexionFTP;
import org.contestorg.infos.InfosModelCheminFTP;

public class ModelCheminFTP extends ModelCheminAbstract
{
	
	// Attributs scalaires
	private String host;
	private int port;
	private String username;
	private String password;
	private String path;
	private int mode;
	
	// Constructeur
	public ModelCheminFTP(InfosModelCheminFTP infos) {
		// Enregistrer les informations
		this.setInfos(infos);
	}
	protected ModelCheminFTP(ModelCheminFTP chemin) {
		// Appeller le constructeur principal
		this(chemin.toInfos());
		
		// Récupérer l'id
		this.setId(chemin.getId());
	}
	
	// Getters
	public String getHost () {
		return this.host;
	}
	public int getPort () {
		return this.port;
	}
	public String getUsername () {
		return this.username;
	}
	public String getPassword () {
		return this.password;
	}
	public String getPath () {
		return this.path;
	}
	public int getMode () {
		return this.mode;
	}
	
	// Setters
	protected void setInfos (InfosModelCheminFTP infos) {
		// Appeller le setInfos du parent
		super.setInfos(infos);
		
		// Enregistrer les informations
		this.host = infos.getInfosFTP().getHost();
		this.port = infos.getInfosFTP().getPort();
		this.username = infos.getInfosFTP().getUsername();
		this.password = infos.getInfosFTP().getPassword();
		this.path = infos.getInfosFTP().getPath();
		this.mode = infos.getInfosFTP().getMode();
		
		// Fire update
		this.fireUpdate();
	}
	
	// Clone
	protected ModelCheminFTP clone () {
		return new ModelCheminFTP(this);
	}
	
	// ToInformation
	public InfosModelCheminFTP toInfos () {
		InfosModelCheminFTP infos = new InfosModelCheminFTP(new InfosConnexionFTP(this.host, this.port, this.username, this.password, this.path, this.mode));
		infos.setId(this.getId());
		return infos;
	}
	
	// Remove
	protected void delete (ArrayList<ModelAbstract> removers) throws ContestOrgModelException {
		if (!removers.contains(this)) {
			// Appeller le remove du parent
			super.delete(removers);
			
			// Fire delete
			this.fireDelete();
		}
	}
	
}
