package org.contestorg.out;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.filechooser.FileSystemView;

import org.contestorg.common.ContestOrgWarningException;
import org.contestorg.common.Tools;
import org.contestorg.infos.InfosTheme;
import org.contestorg.infos.InfosThemeFichier;
import org.contestorg.infos.InfosThemeParametre;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

/**
 * Classe d'outils d'accès aux configurations des thèmes
 */
public class ConfigurationTheme
{
	/**
	 * Récupérer la liste des thèmes d'exportation/diffusion
	 * @param chemin chemin du répertoire des thèmes d'exportation/diffusion
	 * @return liste des thèmes d'exportation/diffusion
	 * @throws ContestOrgOutException
	 */
	public static ArrayList<InfosTheme> getThemes (String chemin) throws ContestOrgWarningException {
		return ConfigurationTheme.getThemes(chemin, null);
	}
	
	/**
	 * Récupérer la liste des thèmes d'exportation/diffusion d'une catégorie
	 * @param chemin chemin du répertoire des thèmes d'exportation/diffusion
	 * @param categorie catégorie
	 * @return liste des thèmes d'exportation/diffusion
	 * @throws ContestOrgOutException
	 */
	@SuppressWarnings("rawtypes")
	public static ArrayList<InfosTheme> getThemes (String chemin, String categorie) throws ContestOrgWarningException {
		// Récupérer la vue sur le système de fichiers
		FileSystemView view = FileSystemView.getFileSystemView();
		
		// Lister les fichiers
		File[] files = view.getFiles(view.createFileObject(chemin), false);
		
		// Créer les thèmes
		ArrayList<InfosTheme> themes = new ArrayList<InfosTheme>();
		for (File file : files) {
			// Vérifier s'il ne s'agit pas d'un fichier caché
			if (!file.isHidden() && !file.getName().startsWith(".") && file.isDirectory()) {
				// Récupérer le chemin relatif
				String cheminRelatif = null;
				try {
					cheminRelatif = Tools.getRelativePath(file, new File("."));
				} catch (IOException e) {
					throw new ContestOrgWarningException("Erreur de la récupération du chemin du thème.");
				}
				
				// Récupérer la configuration du thème
				Element configuration = ConfigurationTheme.getConfiguration(cheminRelatif);
				
				// Vérifier si le fichier de configuration a été trouvé
				if (configuration != null) {
					// Extraire la catégorie
					String themeCategorie = configuration.getAttributeValue("categorie");
					
					// Vérifier si la catégorie correspond
					if(categorie == null || categorie.equals(themeCategorie)) {
						// Extraire la description
						String themeDescription = configuration.getAttributeValue("description");
						
						// Extraire les paramètres
						ArrayList<InfosThemeParametre> themeParametres = new ArrayList<InfosThemeParametre>();
						if (configuration.getChild("parametres") != null) {
							List listeParticipants = configuration.getChild("parametres").getChildren("parametre");
							Iterator i = listeParticipants.iterator();
							while (i.hasNext()) {
								// Récupérer le paramètre
								Element parametre = (Element)i.next();
								
								// Récupérer les données du paramètre
								String parametreId = parametre.getAttributeValue("id");
								String parametreNom = parametre.getAttributeValue("nom");
								String parametreDescription = parametre.getAttributeValue("description");
								boolean parametreOptionnel = parametre.getAttributeValue("optionnel") == null ? false : parametre.getAttributeValue("optionnel").equals("oui");
								String dependance = parametre.getAttributeValue("dependance");
								String defaut = parametre.getAttributeValue("defaut");
								
								// Récupérer et parser le type
								int parametreType;
								switch (Tools.stringCase(parametre.getAttributeValue("type"), "entier", "reel", "texte", "idCategorie", "idPoule", "idPhase", "motDePasse", "booleen")) {
									case 0:
										parametreType = InfosThemeParametre.TYPE_ENTIER;
										break;
									case 1:
										parametreType = InfosThemeParametre.TYPE_REEL;
										break;
									case 2:
										parametreType = InfosThemeParametre.TYPE_TEXTE;
										break;
									case 3:
										parametreType = InfosThemeParametre.TYPE_CATEGORIE;
										break;
									case 4:
										parametreType = InfosThemeParametre.TYPE_POULE;
										break;
									case 5:
										parametreType = InfosThemeParametre.TYPE_PHASE;
										break;
									case 6:
										parametreType = InfosThemeParametre.TYPE_MOTDEPASSE;
										break;
									case 7:
										parametreType = InfosThemeParametre.TYPE_BOOLEEN;
										break;
									default:
										throw new ContestOrgWarningException("Le type du paramètre \""+parametreId+"\" du thème \""+cheminRelatif+"\" n'est pas valide.");
								}
								
								// Ajouter le paramètre
								themeParametres.add(new InfosThemeParametre(parametreId, parametreNom, parametreType, parametreDescription, parametreOptionnel, dependance, defaut));
							}
						}
						
						// Extraire les fichiers
						ArrayList<InfosThemeFichier> themeFichiers = new ArrayList<InfosThemeFichier>();
						if (configuration.getChild("fichiers") != null) {
							List listeParticipants = configuration.getChild("fichiers").getChildren("fichier");
							Iterator i = listeParticipants.iterator();
							while (i.hasNext()) {
								// Récupérer le fichier
								Element fichier = (Element)i.next();
								
								// Récupérer les données du fichier
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
								themeFichiers.add(new InfosThemeFichier(id, nom, source, cible, transformation, principal, fixe, description));
							}
						}
						
						// Ajouter le thème
						themes.add(new InfosTheme(themeCategorie, cheminRelatif, themeDescription, themeParametres, themeFichiers));
					}
				}
			}
		}
		
		// Retourner les thèmes
		return themes;
	}
	
	/**
	 * Parser le fichier fichier de configuration d'un thème
	 * @param chemin chemin du thème
	 * @return fichier de configuration parsé
	 * @throws ContestOrgOutException
	 */
	protected static Element getConfiguration (String chemin) throws ContestOrgWarningException {
		// Récupérer le fichier de configuration
		File configuration = new File(chemin + File.separator + "configuration.xml");
		if (!configuration.exists()) {
			throw new ContestOrgWarningException("Le fichier de configuration \"configuration.xml\" du thème \""+chemin+"\" n'existe pas.");
		}
		
		// Parser le fichier de configuration
		Document document = null;
		try {
			SAXBuilder builder = new SAXBuilder();
			document = builder.build(configuration);
		} catch (JDOMException e) {
			throw new ContestOrgWarningException("Le fichier de configuration \"configuration.xml\" du thème \""+chemin+"\" du n'est pas valide.");
		} catch (IOException e) {
			throw new ContestOrgWarningException("Le fichier de configuration \"configuration.xml\" du thème \""+chemin+"\" est inaccessible.");
		}
		
		// Retourner
		return document.getRootElement();
	}
}
