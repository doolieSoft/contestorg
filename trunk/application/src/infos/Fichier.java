package infos;

public class Fichier
{
	// Transformations
	public static final String TRANSFORMATION_XSLT = "xslt";
	public static final String TRANSFORMATION_FOP = "fop-pdf"; 
	
	// Attributs
	private String id;
	private String nom;
	private String source;
	private String cible;
	private String transformation;
	private boolean principal;
	private boolean fixe;
	private String description;
	
	// Constructeur
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
	
	// Getters
	public String getId() {
		return this.id;
	}
	public String getNom() {
		return this.nom;
	}
	public String getSource () {
		return this.source;
	}
	public String getCible () {
		return this.cible;
	}
	public String getTransformation () {
		return this.transformation;
	}
	public boolean isPrincipal () {
		return this.principal;
	}
	public boolean isFixe() {
		return this.fixe;
	}
	public String getDescription() {
		return this.description;
	}
	
	
}
