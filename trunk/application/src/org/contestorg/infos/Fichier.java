package org.contestorg.infos;

/**
 * Fichier de thème d'exportation/diffusion
 */
public class Fichier
{
	// Transformations
	public static final String TRANSFORMATION_XSLT = "xslt";
	public static final String TRANSFORMATION_FOP = "fop-pdf";
	
	/** Identifiant du fichier */
	private String id;
	
	/** Nom du fichier */
	private String nom;
	
	/** Chemin de la source */
	private String source;
	
	/** Chemin de la cible */
	private String cible;
	
	/** Transformation à appliquer */
	private String transformation;
	
	/** Fichier principal ? */
	private boolean principal;
	
	/** Fichier fixe ? */
	private boolean fixe;
	
	/** Description du fichier */
	private String description;
	
	/**
	 * Constructeur
	 * @param id identifiant du fichier 
	 * @param nom nom du fichier
	 * @param source chemin de la source
	 * @param cible chemin de la cible
	 * @param transformation transformation à appliquer
	 * @param principal fichier principal ?
	 * @param fixe fichier fixe ?
	 * @param description description du fichier
	 */
	public Fichier(String id, String nom, String source, String cible, String transformation, boolean principal, boolean fixe, String description) {
		this.id = id;
		this.nom = nom;
		this.source = source;
		this.cible = cible;
		this.transformation = transformation;
		this.principal = principal;
		this.fixe = fixe;
		this.description = description;
	}
	
	/**
	 * Récupérer l'identifiant du fichier
	 * @return identifiant du fichier
	 */
	public String getId() {
		return this.id;
	}
	
	/**
	 * Récupérer le nom du fichier
	 * @return nom du fichier
	 */
	public String getNom() {
		return this.nom;
	}
	
	/**
	 * Récupérer le chemin de la source
	 * @return chemin de la source
	 */
	public String getSource () {
		return this.source;
	}
	
	/**
	 * Récupérer le chemin de la cible
	 * @return chemin ce la cible
	 */
	public String getCible () {
		return this.cible;
	}
	
	/**
	 * Récupérer la transformation à appliquer
	 * @return transformation à appliquer
	 */
	public String getTransformation () {
		return this.transformation;
	}
	
	/**
	 * Vérifier s'il s'agit d'un fichier principal
	 * @return fichier principal ?
	 */
	public boolean isPrincipal () {
		return this.principal;
	}
	
	/**
	 * Vérifier s'il s'agit d'un fichier fixe
	 * @return fichier fixe ?
	 */
	public boolean isFixe() {
		return this.fixe;
	}
	
	/**
	 * Récupérer la description du fichier
	 * @return description du fichier
	 */
	public String getDescription() {
		return this.description;
	}
	
	
}
