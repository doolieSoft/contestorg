package org.contestorg.infos;

public class Parametre
{
	// Types de paramètre
	public static final int TYPE_ENTIER = 1;
	public static final int TYPE_REEL = 2;
	public static final int TYPE_TEXTE = 3;
	public static final int TYPE_CATEGORIE = 4;
	public static final int TYPE_POULE = 5;
	public static final int TYPE_PHASE = 6;
	public static final int TYPE_MOTDEPASSE = 7;
	
	// Attributs
	private String id;
	private String nom;
	private int type;
	private String description;
	private boolean optionnel;
	private String dependance;
	private String defaut;
	
	// Constructeur
	public Parametre(String id, String nom, int type, String description, boolean optionnel, String dependance, String defaut) {
		this.id = id;
		this.nom = nom;
		this.type = type;
		this.description = description;
		this.optionnel = optionnel;
		this.dependance = dependance; 
		this.defaut = defaut; 
	}

	// Getters
	public String getId () {
		return this.id;
	}
	public String getNom () {
		return this.nom;
	}
	public int getType () {
		return this.type;
	}
	public String getDescription () {
		return this.description;
	}
	public boolean isOptionnel() {
		return this.optionnel;
	}
	public String getDependance() {
		return this.dependance;
	}
	public String getDefaut() {
		return this.defaut;
	}
	
}
