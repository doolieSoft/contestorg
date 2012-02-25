package org.contestorg.models;

import java.util.ArrayList;
import java.util.HashMap;

import org.contestorg.infos.InfosModelTheme;

/**
 * Thème d'exportation/diffusion
 */
public class ModelTheme extends ModelAbstract
{
	// Attributs scalaires
	
	/** Chemin */
	private String chemin;
	
	/** Liste des paramètres */
	private HashMap<String, String> parametres;
	
	/** Liste des fichiers */
	private HashMap<String, String> fichiers;
	
	// Constructeurs
	
	/**
	 * Constructeur
	 * @param infos informations du thème d'exportation/diffusion
	 */
	public ModelTheme(InfosModelTheme infos) {
		// Enregistrer les informations
		this.setInfos(infos);
	}
	
	/**
	 * Constructeur par copie
	 * @param theme thème d'exportation/diffusion
	 */
	protected ModelTheme(ModelTheme theme) {
		// Appeller le constructeur principal
		this(theme.getInfos());
		
		// Récupérer l'id
		this.setId(theme.getId());
	}
	
	// Getters
	
	/**
	 * Récupérer le chemin
	 * @return chemin
	 */
	public String getChemin () {
		return chemin;
	}
	
	/**
	 * Récupérer la liste des paramètres
	 * @return liste des paramètres
	 */
	public HashMap<String, String> getParametres () {
		return this.parametres == null ? null : new HashMap<String, String>(this.parametres);
	}
	
	/**
	 * Récupérer la liste des fichiers
	 * @return liste des fichiers
	 */
	public HashMap<String, String> getFichiers () {
		return this.fichiers == null ? null : new HashMap<String, String>(this.fichiers);
	}
	
	// Setters
	
	/**
	 * Définir les informations du thème d'exportation/diffusion
	 * @param infos informations du thème d'exportation/diffusion
	 */
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
	
	/**
	 * Cloner le thème d'exportation/diffusion
	 */
	protected ModelTheme clone () {
		return new ModelTheme(this);
	}
	
	/**
	 * @see ModelAbstract#getInfos()
	 */
	public InfosModelTheme getInfos () {
		InfosModelTheme infos = new InfosModelTheme(this.chemin, this.parametres, this.fichiers);
		infos.setId(this.getId());
		return infos;
	}
	
	/**
	 * @see ModelAbstract#delete(ArrayList)
	 */
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
