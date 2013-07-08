package org.contestorg.out;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.filechooser.FileSystemView;

import org.contestorg.common.ContestOrgWarningException;
import org.contestorg.common.Tools;
import org.contestorg.infos.InfosModele;
import org.contestorg.infos.InfosModeleVariante;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

/**
 * Classe d'outils d'accès aux configurations des modèles
 */
public class ConfigurationModele
{
	/**
	 * Récupérer la liste des modèles
	 * @param chemin chemin du répertoire des modèles
	 * @return liste des modèles
	 * @throws ContestOrgOutException
	 */
	public static ArrayList<InfosModele> getModeles (String chemin) throws ContestOrgWarningException {
		return ConfigurationModele.getModeles(chemin, null);
	}
	
	/**
	 * Récupérer la liste des modèles
	 * @param chemin chemin du répertoire des modèles
	 * @param categorie catégorie
	 * @return liste des modèles
	 * @throws ContestOrgOutException
	 */
	@SuppressWarnings("rawtypes")
	public static ArrayList<InfosModele> getModeles (String chemin, String categorie) throws ContestOrgWarningException {
		// Récupérer la vue sur le système de fichiers
		FileSystemView view = FileSystemView.getFileSystemView();
		
		// Lister les fichiers
		File[] files = view.getFiles(view.createFileObject(chemin), false);
		
		// Créer les modèles
		ArrayList<InfosModele> modeles = new ArrayList<InfosModele>();
		for (File file : files) {
			// Vérifier s'il ne s'agit pas d'un fichier caché
			if (!file.isHidden() && !file.getName().startsWith(".") && file.isDirectory()) {
				// Récupérer le chemin relatif
				String cheminRelatif = null;
				try {
					cheminRelatif = Tools.getRelativePath(file, new File("."));
				} catch (IOException e) {
					throw new ContestOrgWarningException("Erreur de la récupération du chemin du modèle.");
				}
				
				// Récupérer la configuration du modèle
				Element configuration = ConfigurationModele.getConfiguration(cheminRelatif);
				
				// Vérifier si le fichier de configuration a été trouvé
				if (configuration != null) {
					// Extraire la catégorie
					String modeleCategorie = configuration.getAttributeValue("categorie");
					if(modeleCategorie == null || modeleCategorie.equals("")) {
						modeleCategorie = InfosModele.CATEGORIE_DIVERS;
					}
					
					// Vérifier si la catégorie correspond
					if(categorie == null || categorie.equals(modeleCategorie)) {
						// Extraire le chemin de l'image
						String modeleImage = configuration.getAttributeValue("image");
						
						// Extraire la description
						String modeleDescription = null;
						if(configuration.getChild("description") != null) {
							modeleDescription = configuration.getChild("description").getTextTrim();
						}
						
						// Extraire les variantes
						ArrayList<InfosModeleVariante> modeleVariantes = new ArrayList<InfosModeleVariante>();
						if (configuration.getChild("variantes") != null) {
							List listeParticipants = configuration.getChild("variantes").getChildren("variante");
							Iterator i = listeParticipants.iterator();
							while (i.hasNext()) {
								// Récupérer la variante
								Element variante = (Element)i.next();
								
								// Récupérer les données de la variante
								String varianteNom = variante.getAttributeValue("nom");
								String varianteFichier = variante.getAttributeValue("fichier");
								
								// Ajouter la variante
								modeleVariantes.add(new InfosModeleVariante(varianteNom, varianteFichier));
							}
						} else {
							throw new ContestOrgWarningException("Le fichier de configuration \"configuration.xml\" du modèle \""+cheminRelatif+"\" n'est pas valide.");
						}
						
						// Ajouter le modèle
						modeles.add(new InfosModele(modeleCategorie, cheminRelatif, modeleImage, modeleDescription, modeleVariantes));
					}
				}
			}
		}
		
		// Retourner les modèles
		return modeles;
	}
	
	/**
	 * Parser le fichier fichier de configuration d'un modèle
	 * @param chemin chemin du modèle
	 * @return fichier de configuration parsé
	 * @throws ContestOrgOutException
	 */
	protected static Element getConfiguration (String chemin) throws ContestOrgWarningException {
		// Récupérer le fichier de configuration
		File configuration = new File(chemin + File.separator + "configuration.xml");
		if (!configuration.exists()) {
			throw new ContestOrgWarningException("Le fichier de configuration \"configuration.xml\" du modèle \""+chemin+"\" n'existe pas.");
		}
		
		// Parser le fichier de configuration
		Document document = null;
		try {
			SAXBuilder builder = new SAXBuilder();
			document = builder.build(configuration);
		} catch (JDOMException e) {
			throw new ContestOrgWarningException("Le fichier de configuration \"configuration.xml\" du modèle \""+chemin+"\" n'est pas valide.");
		} catch (IOException e) {
			throw new ContestOrgWarningException("Le fichier de configuration \"configuration.xml\" du modèle \""+chemin+"\" est inaccessible.");
		}
		
		// Retourner
		return document.getRootElement();
	}
}
