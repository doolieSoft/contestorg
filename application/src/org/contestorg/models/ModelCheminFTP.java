package org.contestorg.models;

import java.util.ArrayList;

import org.contestorg.infos.InfosConnexionFTP;
import org.contestorg.infos.InfosModelCheminFTP;

/**
 * Chemin d'exportation FTP
 */
public class ModelCheminFTP extends ModelCheminAbstract
{
	
	// Attributs scalaires
	
	/** Hôte */
	private String host;
	
	/** Port */
	private int port;
	
	/** Nom d'utilisateur */
	private String username;
	
	/** Mot de passe */
	private String password;
	
	/** Chemin */
	private String path;
	
	/** Mode de connexion */
	private int mode;
	
	// Constructeurs
	
	/**
	 * Constructeur
	 * @param infos informations du chemin d'exportation FTP
	 */
	public ModelCheminFTP(InfosModelCheminFTP infos) {
		// Enregistrer les informations
		this.setInfos(infos);
	}
	
	/**
	 * Constructeur par copie
	 * @param chemin chemin d'exportation FTP
	 */
	protected ModelCheminFTP(ModelCheminFTP chemin) {
		// Appeller le constructeur principal
		this(chemin.getInfos());
		
		// Récupérer l'id
		this.setId(chemin.getId());
	}
	
	// Getters
	
	/**
	 * Récupérer l'hôte
	 * @return hôte
	 */
	public String getHost () {
		return this.host;
	}
	
	/**
	 * Récupérer le port
	 * @return port
	 */
	public int getPort () {
		return this.port;
	}
	
	/**
	 * Récupérer le nom d'utilisateur
	 * @return nom d'utilisateur
	 */
	public String getUsername () {
		return this.username;
	}
	
	/**
	 * Récupérer le mot de passe
	 * @return mot de passe
	 */
	public String getPassword () {
		return this.password;
	}
	
	/**
	 * Chemin
	 * @return chemin
	 */
	public String getPath () {
		return this.path;
	}
	
	/**
	 * Récupérer le mode de connexion
	 * @return mode de connexion
	 */
	public int getMode () {
		return this.mode;
	}
	
	// Setters
	
	/**
	 * Définir les informations du chemin d'exportation FTP
	 * @param infos informations du chemin d'exportation FTP
	 */
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
	
	/**
	 * @see ModelCheminAbstract#clone()
	 */
	protected ModelCheminFTP clone () {
		return new ModelCheminFTP(this);
	}
	
	/**
	 * @see ModelCheminAbstract#getInfos()
	 */
	public InfosModelCheminFTP getInfos () {
		InfosModelCheminFTP infos = new InfosModelCheminFTP(new InfosConnexionFTP(this.host, this.port, this.username, this.password, this.path, this.mode));
		infos.setId(this.getId());
		return infos;
	}
	
	/**
	 * @see ModelAbstract#delete(ArrayList)
	 */
	protected void delete (ArrayList<ModelAbstract> removers) throws ContestOrgModelException {
		if (!removers.contains(this)) {
			// Appeller le remove du parent
			super.delete(removers);
			
			// Fire delete
			this.fireDelete();
		}
	}
	
}
