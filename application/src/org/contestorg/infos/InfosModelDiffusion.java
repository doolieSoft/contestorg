package infos;

public class InfosModelDiffusion extends InfosModelAbstract
{	
	// Attributs
	private String nom;
	private int port;

	// Constructeur
	public InfosModelDiffusion(String nom,int port) {
		this.nom = nom;
		this.port = port;
	}

	// Getters
	public String getNom() {
		return this.nom;
	}
	public int getPort () {
		return this.port;
	}

	// Informations par défaut
	public static InfosModelDiffusion defaut () {
		return new InfosModelDiffusion("",80);
	}
}
