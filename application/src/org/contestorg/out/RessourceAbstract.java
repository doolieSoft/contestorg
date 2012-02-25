package org.contestorg.out;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.activation.MimetypesFileTypeMap;
import javax.swing.filechooser.FileSystemView;

import org.contestorg.common.Tools;
import org.contestorg.events.Action;
import org.contestorg.infos.Fichier;
import org.contestorg.infos.InfosModelTheme;
import org.contestorg.infos.Parametre;
import org.contestorg.infos.Theme;
import org.contestorg.interfaces.IHistoryListener;
import org.contestorg.models.FrontModel;
import org.contestorg.models.ModelAbstract;
import org.contestorg.models.ModelPoule;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

/**
 * Ressource abstraite
 */
public abstract class RessourceAbstract implements IHistoryListener
{
	/**
	 * Récupérer la liste des thèmes d'exportation/diffusion
	 * @param chemin chemin du répertoire des thèmes d'exportation/diffusion
	 * @return liste des thèmes d'exportation/diffusion
	 * @throws ContestOrgOutException
	 */
	public static ArrayList<Theme> getThemes (String chemin) throws ContestOrgOutException {
		return RessourceAbstract.getThemes(chemin, null);
	}
	
	/**
	 * Récupérer la liste des thèmes d'exportation/diffusion d'une catégorie
	 * @param chemin chemin du répertoire des thèmes d'exportation/diffusion
	 * @param categorie catégorie
	 * @return liste des thèmes d'exportation/diffusion
	 * @throws ContestOrgOutException
	 */
	@SuppressWarnings("rawtypes")
	public static ArrayList<Theme> getThemes (String chemin, String categorie) throws ContestOrgOutException {
		// Récupérer la vue sur le système de fichiers
		FileSystemView view = FileSystemView.getFileSystemView();
		
		// Lister les fichiers
		File[] files = view.getFiles(view.createFileObject(chemin), false);
		
		// Créer les thèmes
		ArrayList<Theme> themes = new ArrayList<Theme>();
		for (File file : files) {
			// Vérifier s'il ne s'agit pas d'un fichier caché
			if (!file.isHidden() && !file.getName().startsWith(".") && file.isDirectory()) {
				// Récupérer la configuration du thème
				Element configuration = RessourceAbstract.getConfiguration(file.getPath());
				
				// Vérifier si le fichier de configuration a été trouvé
				if (configuration != null) {
					// Extraire la catégorie
					String themeCategorie = configuration.getAttributeValue("categorie") == null ? "" : configuration.getAttributeValue("categorie");
					
					// Vérifier si la catégorie correspond
					if(categorie == null || categorie.equals(themeCategorie)) {
						// Extraire la description
						String themeDescription = configuration.getAttributeValue("description") == null ? "" : configuration.getAttributeValue("description");
						
						// Extraire les paramètres
						ArrayList<Parametre> themeParametres = new ArrayList<Parametre>();
						if (configuration.getChild("parametres") != null) {
							List listeParticipants = configuration.getChild("parametres").getChildren("parametre");
							Iterator i = listeParticipants.iterator();
							while (i.hasNext()) {
								// Récupérer le paramètre
								Element parametre = (Element)i.next();
								
								// Récupérer l'id, le nom, la description et l'optionnalité du paramètre
								String parametreId = parametre.getAttributeValue("id");
								String parametreNom = parametre.getAttributeValue("nom");
								String parametreDescription = parametre.getAttributeValue("description");
								boolean parametreOptionnel = parametre.getAttributeValue("optionnel") == null ? false : parametre.getAttributeValue("optionnel").equals("oui");
								String dependance = parametre.getAttributeValue("dependance");
								String defaut = parametre.getAttributeValue("defaut");
								
								// Récupérer et parser le type
								int parametreType;
								switch (Tools.StringCase(parametre.getAttributeValue("type"), "entier", "reel", "texte", "idCategorie", "idPoule", "idPhase", "motDePasse")) {
									case 0:
										parametreType = Parametre.TYPE_ENTIER;
										break;
									case 1:
										parametreType = Parametre.TYPE_REEL;
										break;
									case 2:
										parametreType = Parametre.TYPE_TEXTE;
										break;
									case 3:
										parametreType = Parametre.TYPE_CATEGORIE;
										break;
									case 4:
										parametreType = Parametre.TYPE_POULE;
										break;
									case 5:
										parametreType = Parametre.TYPE_PHASE;
										break;
									case 6:
										parametreType = Parametre.TYPE_MOTDEPASSE;
										break;
									default:
										throw new ContestOrgOutException("Le type du paramètre \"" + parametreId + "\" du thème \"" + file.getName() + "\" n'est pas valide.");
								}
								
								// Ajouter le paramètre
								themeParametres.add(new Parametre(parametreId, parametreNom, parametreType, parametreDescription, parametreOptionnel, dependance, defaut));
							}
						}
						
						// Extraire les fichiers
						ArrayList<Fichier> themeFichiers = new ArrayList<Fichier>();
						if (configuration.getChild("fichiers") != null) {
							List listeParticipants = configuration.getChild("fichiers").getChildren("fichier");
							Iterator i = listeParticipants.iterator();
							while (i.hasNext()) {
								// Récupérer le fichier
								Element fichier = (Element)i.next();
								
								// Récupérer la source, la cible, le formateur et s'il s'agit du fichier principal
								String id = fichier.getAttributeValue("id");
								String nom = fichier.getAttributeValue("nom");
								String source = fichier.getAttributeValue("source");
								String cible = fichier.getAttributeValue("cible");
								if(cible == null) {
									cible = source;
								}
								String transformation = fichier.getAttributeValue("transformation");
								boolean principal = fichier.getAttributeValue("formatteur") != null && fichier.getAttributeValue("formatteur").equals("oui");
								boolean fixe = fichier.getAttributeValue("fixe") != null && fichier.getAttributeValue("fixe").equals("oui");
								String description = fichier.getAttributeValue("description");
								
								// Ajouter le fichier
								themeFichiers.add(new Fichier(id, nom, source, cible, transformation, principal, fixe, description));
							}
						}
						
						// Récupérer le chemin relatif
						String relatif = null;
						try {
							relatif = Tools.getRelativePath(file, new File("."));
						} catch (IOException e) {
							throw new ContestOrgOutException("Erreur de la récupération du chemin relatif au thème.");
						}
						
						// Ajouter le thème
						themes.add(new Theme(themeCategorie, relatif, themeDescription, themeParametres, themeFichiers));
					}
				}
			}
		}
		
		// Retourner les thèmes
		return themes;
	}
	
	/**
	 * Parser le fichier fichier de configuration d'un thème
	 * @param chemin chemin du fichier de configuration
	 * @return fichier de configuration parsé
	 * @throws ContestOrgOutException
	 */
	private static Element getConfiguration (String chemin) throws ContestOrgOutException {
		// Récupérer le fichier de configuration
		File configuration = new File(chemin + File.separator + "configuration.xml");
		if (!configuration.exists()) {
			throw new ContestOrgOutException("Le fichier de configuration \"configuration.xml\" n'existe pas.");
		}
		
		// Parser le fichier de configuration
		Document document = null;
		try {
			SAXBuilder builder = new SAXBuilder();
			document = builder.build(configuration);
		} catch (JDOMException e) {
			throw new ContestOrgOutException("Le fichier de configuration \"configuration.xml\" n'est pas valide.");
		} catch (IOException e) {
			throw new ContestOrgOutException("Le fichier de configuration \"configuration.xml\" est inaccessible.");
		}
		
		// Retourner
		return document.getRootElement();
	}
	
	/**
	 * Créer les ressources associées à un thème
	 * @param theme thème
	 * @param refresh autoriser la mise à jour automatique des ressources ?
	 * @return liste des ressources du thème
	 * @throws ContestOrgOutException
	 */
	@SuppressWarnings("rawtypes")
	public static ArrayList<RessourceAbstract> getRessources (InfosModelTheme theme, boolean refresh) throws ContestOrgOutException {
		// Vérifier si le thème existe
		if(!(new File(theme.getChemin()).exists())) {
			throw new ContestOrgOutException("Le thème \""+theme.getNom()+"\" n'a pas été retrouvé.");
		}
		
		// Initialiser les ressources
		ArrayList<RessourceAbstract> ressources = new ArrayList<RessourceAbstract>();
		
		// Ajouter les ressources
		Element configuration = RessourceAbstract.getConfiguration(theme.getChemin());
		if (configuration.getChild("fichiers") != null) {
			List listeParticipants = configuration.getChild("fichiers").getChildren("fichier");
			Iterator i = listeParticipants.iterator();
			if (!i.hasNext()) {
				throw new ContestOrgOutException("Aucun fichier n'a été défini dans le fichier de configuration du thème.");
			} else {
				while (i.hasNext()) {
					// Récupérer le fichier dans le DOM
					Element file = (Element)i.next();
					
					// Récupérer les paramètres du fichier
					String source = file.getAttributeValue("source");
					String cible = file.getAttributeValue("cible");
					if (cible == null) {
						cible = source;
					}
					boolean principal = file.getAttributeValue("principal") != null && file.getAttributeValue("principal").equals("oui");
					String transformation = file.getAttributeValue("transformation");
					
					// Utiliser la cible définie par l'utilisateur si nécéssaire
					if(theme.getFichiers().containsKey(file.getAttributeValue("id"))) {
						cible = theme.getFichiers().get(file.getAttributeValue("id"));
					}

					// Transformer les paramètres spéciaux dans la cible
					cible = RessourceAbstract.transform(cible, theme.getParametres());
					
					// Récupérer le fichier associé à la ressource
					File fichier = new File(theme.getChemin() + File.separator + source);
					
					// Créer et ajouter la ressource
					switch (Tools.StringCase(transformation, Fichier.TRANSFORMATION_XSLT, Fichier.TRANSFORMATION_FOP)) {
						case 0: // Ressource XSLT
							ressources.add(new RessourceXSLT(cible, principal, theme.getParametres(), theme.getFichiers(), fichier));
							break;
						case 1: // Ressource FOP
							ressources.add(new RessourceFOP(cible, principal, theme.getParametres(), theme.getFichiers(), fichier));
							break;
						default: // Ressource fichier
							ressources.add(new RessourceFichier(cible, principal, theme.getParametres(), theme.getFichiers(), fichier));
					}
				}
			}
		} else {
			// Erreur
			throw new ContestOrgOutException("Aucun fichier n'a été défini dans le fichier de configuration du thème.");
		}
		
		// Autoriser la mise à jour automatique si nécéssaire
		if(refresh) {
			for(RessourceAbstract ressource : ressources) {
				ressource.setAutoRefresh();
			}
		}
		
		// Retourner les ressources
		return ressources;
	}
	
	/**
	 * Récupérer le type de contenu d'un fichier
	 * @param fichier fichier
	 * @return type de contenu du fichier
	 */
	public static String getContentType(String fichier) {
		MimetypesFileTypeMap map = new MimetypesFileTypeMap();
		map.addMimeTypes("application/pdf pdf PDF");
		map.addMimeTypes("application/javascript js JS");
		map.addMimeTypes("text/css css CSS");
		map.addMimeTypes("text/html html HTML htm HTM");
		map.addMimeTypes("text/xml xml XML");
		return map.getContentType(fichier);
	}
	
	/**
	 * Transformer la valeur d'un paramètre en une chaine de caractères à partir de la liste des paramètres spéciaux
	 * @param valeur valeur du paramètre
	 * @param parametres liste des paramètres spéciaux
	 * @return chaine de caractère
	 */
	public static String transform(String valeur, HashMap<String, String> parametres) {
		// Id de la dernière phase dans une poule
		Matcher idDernierePhase = Pattern.compile("%idDernierePhase\\(([0-9]+),([0-9]+)\\)%").matcher(valeur);
		if (idDernierePhase.find()) {
			// Récupérer le model
			ModelAbstract model = ModelAbstract.search(Integer.parseInt(idDernierePhase.group(2)));
			
			// Vérifier si le model est bien une poule
			if(model != null && model instanceof ModelPoule) {
				// Caster le model en poule
				ModelPoule poule = (ModelPoule)model;
				
				// Remplacer le paramètre spécial par sa valeur
				if(poule.getPhasesQualificatives().size() != 0) {
					valeur = valeur.replace(idDernierePhase.group(0), String.valueOf(poule.getPhasesQualificatives().get(poule.getPhasesQualificatives().size()-1).getId()));
				} else {
					valeur = valeur.replace(idDernierePhase.group(0), "");
				}
			}
		}
		
		// Numéro de la dernière phase dans une poule indicé de 1 à N
		Matcher numeroDernierePhase = Pattern.compile("%numeroDernierePhase\\(([0-9]+),([0-9]+)\\)%").matcher(valeur);
		if (numeroDernierePhase.find()) {

			// Récupérer le model
			ModelAbstract model = ModelAbstract.search(Integer.parseInt(numeroDernierePhase.group(2)));
			
			// Vérifier si le model est bien une poule
			if(model != null && model instanceof ModelPoule) {
				// Caster le model en poule
				ModelPoule poule = (ModelPoule)model;
				
				// Remplacer le paramètre spécial par sa valeur
				valeur = valeur.replace(numeroDernierePhase.group(0), String.valueOf(poule.getPhasesQualificatives().size()));
			}
		}
		
		// Retourner la valeur
		return valeur;
	}
	
	/** Cible */
	protected String cible;
	
	/** Ressource principale ? */
	private boolean principale;
	
	/** Liste des paramètres */
	private HashMap<String, String> parametres;
	
	/** Liste des fichiers */
	private HashMap<String, String> fichiers;
	
	/** Mutex d'accès à la ressource */
	private ReentrantLock mutex = new ReentrantLock();
	
	/**
	 * Constructeur
	 * @param cible cible
	 * @param principale ressource principale ?
	 * @param parametres liste des paramètres
	 * @param fichiers liste des fichiers
	 */
	public RessourceAbstract(String cible, boolean principale, HashMap<String, String> parametres, HashMap<String, String> fichiers) {
		// Retenir les informations
		this.cible = cible;
		this.principale = principale;
		this.parametres = parametres;
		this.fichiers = fichiers;
	}
	
	// Getters
	
	/**
	 * Récupérer le fichier associé à la ressource
	 * @return fichier associé à la ressource
	 */
	public abstract File getFichier ();
	
	/**
	 * Récupérer le type de contenu du fichier associé à la ressource
	 * @return type de contenu du fichier associé à la ressource
	 */
	public abstract String getContentType ();
	
	/**
	 * Récupérer la cible
	 * @return cible
	 */
	public String getCible () {
		return this.cible;
	}
	
	/**
	 * Vérifier s'il s'agit d'une ressource principale
	 * @return resource principal
	 */
	public boolean isPrincipale () {
		return this.principale;
	}
	
	/**
	 * Récupérer la liste des paramètres
	 * @return liste des paramètres
	 */
	protected HashMap<String, String> getParametres () {
		// Initialiser la liste des parametres
		HashMap<String, String> parametres = new HashMap<String, String>();
		
		// Parcourir tous les paramètres
		for (Entry<String, String> parametre : this.parametres.entrySet()) {
			// Extraire la valeur
			String valeur = parametre.getValue();
			
			// Transformer la valeur si nécéssaire
			if(valeur != null) {
				valeur = RessourceAbstract.transform(valeur, this.parametres);
			}
			
			// Ajouter le paramètre
			parametres.put(parametre.getKey(), valeur);
		}
		
		// Retourner les paramètres
		return parametres;
	}
	
	/**
	 * Récupérer la liste des fichier
	 * @return liste des fichier
	 */
	protected HashMap<String,String> getFichiers() {
		return this.fichiers;
	}
		
	/**
	 * Nettoyer la ressource
	 */
	public abstract void clean ();
	
	/**
	 * Rafraichir la ressource
	 * @return opération réussie ?
	 */
	protected abstract boolean refresh ();
		
	/**
	 * Vérifier si la ressource est associée à une transformation
	 * @return ressource associée à une transformation ?
	 */
	public abstract boolean isTransformation();
	
	/**
	 * Autoriser la mise à jour automatique de la ressource
	 */
	public void setAutoRefresh() { 
		// Ecouter l'historique
		FrontModel.get().getHistory().addListener(this);
	}
	
	// Implémentation de IHistoryListener
	@Override
	public void historyActionPushed (Action action) {
		if(mutex.getQueueLength() < 1) {
			new Thread(new Refresh()).start();
		}
	}
	@Override
	public void historyActionPoped (Action action) {
		if(mutex.getQueueLength() < 1) {
			new Thread(new Refresh()).start();
		}
	}
	public void historyInit () {
		if(mutex.getQueueLength() < 1) {
			new Thread(new Refresh()).start();
		}
	}
	
	// Vérouiller/Dévérouiller la ressource
	
	/**
	 * Vérouiller la ressource
	 */
	public void lock() {
		this.mutex.lock();
	}
	
	/**
	 * Dévérouiller la ressource
	 */
	public void unlock() {
		this.mutex.unlock();
	}
	
	/**
	 * Classe permettant de rafraichir la ressource sans bloquer l'appel
	 */
	private class Refresh implements Runnable
	{
		/**
		 * @see Runnable#run()
		 */
		@Override
		public void run () {
			// Prendre jeton du mutex
			mutex.lock();
			
			// Nettoyer la ressource
			clean();
			
			// Rafraichir la ressource
			refresh();
			
			// Relacher jeton du mutex
			mutex.unlock();
		}
	}
	
}
