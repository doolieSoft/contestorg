package org.contestorg.controllers;


import java.util.ArrayList;
import java.util.HashMap;

import org.contestorg.common.ContestOrgWarningException;
import org.contestorg.common.Pair;
import org.contestorg.common.Triple;
import org.contestorg.events.Event;
import org.contestorg.events.EventDelete;
import org.contestorg.events.EventUpdate;
import org.contestorg.infos.InfosConnexionFTP;
import org.contestorg.infos.InfosModelChemin;
import org.contestorg.infos.InfosModelDiffusion;
import org.contestorg.infos.InfosModelExportation;
import org.contestorg.infos.InfosModelParticipant;
import org.contestorg.infos.InfosModelProprietePossedee;
import org.contestorg.infos.InfosModelTheme;
import org.contestorg.infos.InfosModele;
import org.contestorg.infos.InfosTheme;
import org.contestorg.interfaces.IEventListener;
import org.contestorg.interfaces.IOperation;
import org.contestorg.interfaces.IStartStopListener;
import org.contestorg.log.Log;
import org.contestorg.models.FrontModel;
import org.contestorg.models.ModelDiffusion;
import org.contestorg.models.ModelExportation;
import org.contestorg.out.ConfigurationModele;
import org.contestorg.out.ConfigurationTheme;
import org.contestorg.out.DiffusionHTTP;
import org.contestorg.out.ExportationAbstract;
import org.contestorg.out.ExportationLocale;
import org.contestorg.out.FTPHelper;
import org.contestorg.out.HTTPHelper;
import org.contestorg.out.PersistanceXML;

/**
 * Controleur pour les traitements avec l'extérieur (exportations, importations, ...)
 */
public class CtrlOut
{

	// ==== Importations
	
	/**
	 * Importer des participants
	 * @param chemin chemin du fichier des participants
	 * @return liste des informations des participants trouvés avec leurs propriétés possédées ainsi qu'une liste des erreurs rencontrées
	 */
	public Pair<ArrayList<String>,ArrayList<Pair<InfosModelParticipant,ArrayList<Pair<String,InfosModelProprietePossedee>>>>> importerParticipants(String chemin) {
		return PersistanceXML.loadParticipants(chemin);
	}
	
	// ==== Modèles
	
	/**
	 * Récupérer la liste des modèles
	 * @return liste des modèles
	 */
	public ArrayList<InfosModele> getModeles() {
		return this.getModeles(null);
	}
	
	/**
	 * Récupérer la liste des modèles d'une catégorie
	 * @param categorie catégorie
	 * @return liste des modèles de la catégorie
	 */
	public ArrayList<InfosModele> getModeles(String categorie) {
		// Retourner les modèles disponibles
		try {
			return ConfigurationModele.getModeles("templates",categorie);
		} catch (Exception e) {
			Log.getLogger().error("Erreur lors de la récupération des modèles disponibles.",e);
			return null; 
		}
	}
	
	// ==== Exportations
	
	/**
	 * Récupérer la liste des exportations
	 * @return liste des exportations
	 */
	public ArrayList<Triple<InfosModelExportation, InfosModelChemin, InfosModelTheme>> getListeExportations() {
		return FrontModel.get().getFrontModelConfiguration().getListeExportations();
	}
	
	/**
	 * Lancer une exportation prédéfinie
	 * @param nom nom de l'exportation à lancer
	 * @return opération de lancement de l'exportation
	 */
	public IOperation getLancerExportationOperation(String nom) {
		// Récupérer le modèle de l'exportation
		ModelExportation exportation = FrontModel.get().getConcours().getExportationByNom(nom);
		
		// Retourner l'opération de lancement de l'exportation
		return ExportationAbstract.export(exportation);
	}
	
	/**
	 * Lancer une exportation à la volée
	 * @param chemin chemin du répertoire de destination
	 * @param theme informations sur le thème d'exportation
	 * @return opération de lancement de l'exportation
	 */
	public IOperation getLancerExportationOperation(String chemin, InfosModelTheme theme) {
		// Créer l'exportation locale et retourner son opération de lancement
		return new ExportationLocale(theme, chemin).export();
	}
		
	/**
	 * Tester une connexion FTP
	 * @param infos informations sur la connexion FTP
	 * @return opération de test de la connexion FTP
	 */
	public IOperation getTesterExportationFtpOperation (InfosConnexionFTP infos) {
		// Créer et retourner le testeur
		return new FTPHelper.Tester(infos);
	}

	/**
	 * Récupérer la liste des thèmes d'exportation disponibles
	 * @return liste des thèmes d'exportation disponibles
	 */
	public ArrayList<InfosTheme> getThemesExportation () {
		return this.getThemesExportation(null);
	}
	
	/**
	 * Récupérer la liste des thèmes d'exportation disponibles pour une catégorie donnée 
	 * @param categorie catégorie
	 * @return liste des thèmes d'exportation disponibles pour la catégorie
	 */
	public ArrayList<InfosTheme> getThemesExportation (String categorie) {
		// Retourner les thèmes disponibles
		try {
			return ConfigurationTheme.getThemes("themes/exportations",categorie);
		} catch (Exception e) {
			Log.getLogger().error("Erreur lors de la récupération des thèmes disponibles pour les exportations.",e);
			return null; 
		}
	}
		
	/**
	 * Lancer les exportations automatiques
	 * @return opération de lancement des exportations automatiques
	 */
	public IOperation getLancerExportationsAutomatiquesOperation() {
		// Récupérer la liste des exportations automatiques
		ArrayList<ModelExportation> exportations = new ArrayList<ModelExportation>();
		for(ModelExportation exportation : FrontModel.get().getConcours().getExportations()) {
			// Vérifier s'il s'agit d'une exportation automatique
			if(exportation.isAuto()) {
				// Ajouter l'exportation à la liste des exportations automatiques
				exportations.add(exportation);
			}
		}
		
		// Récupérer et retourner l'opération
		if(exportations.size() != 0) {
			return ExportationAbstract.export(exportations);
		} else {
			return null;
		}
	}
	
	// ==== Publication
	
	/**
	 * Récupérer la publication
	 * @return publication
	 */
	public Triple<InfosModelExportation, InfosModelChemin, InfosModelTheme> getPublication() {
		return FrontModel.get().getFrontModelConfiguration().getInfosPublication();
	}
	
	/**
	 * Récupérer l'indice de l'exportation qui fait office de publication
	 * @return indice de l'exportation qui fait office de publication
	 */
	public int getPublicationIndex() {
		return FrontModel.get().getFrontModelConfiguration().getPublicationIndex();
	}
	
	/**
	 * Lancer la publication
	 * @return opération de lancement de la publication
	 */
	public IOperation getLancerPublicationOperation() {
		return this.getLancerExportationOperation(FrontModel.get().getFrontModelConfiguration().getInfosPublication().getFirst().getNom());
	}
	
	// ==== Diffusions
	
	/** Diffusions démarrées */
	public HashMap<Integer,DiffusionHTTP> diffusions = new HashMap<Integer,DiffusionHTTP>();
	
	/**
	 * Récupérer la liste des diffusions
	 * @return liste des diffusions
	 */
	public ArrayList<Pair<InfosModelDiffusion,InfosModelTheme>> getListeDiffusions() {
		return FrontModel.get().getFrontModelConfiguration().getListeDiffusions();
	}
	
	/**
	 * Démarrer une diffusion
	 * @param port numéro de port de la diffusion
	 * @return opération de démarrage de la diffusion
	 */
	public IOperation getDemarrerDiffusionOperation(final int port) {
		// Récupérer le modèle de la diffusion
		final ModelDiffusion model = FrontModel.get().getConcours().getDiffusionByPort(port);
		
		// Créer la diffusion
		final DiffusionHTTP diffusion = new DiffusionHTTP(model.getTheme().getInfos(),model.getPort());
		
		// Ecouter le modèle
		final IEventListener listener = new IEventListener() {
			// Implémentation de event
			@Override
			public void event (Event event) {
				if(event instanceof EventDelete || event instanceof EventUpdate) {
					// Arreter la diffusion
					diffusion.getStopOperation().operationStart();
				}
			}
		};
		model.addListener(listener);

		// Ecouter le démarrage et l'arret de la diffusion
		diffusion.addListener(new IStartStopListener() {
			// Implémentation de start
			@Override
			public void start () {
				// Ajouter la diffusion dans les diffusions démarrées
				diffusions.put(port, diffusion);
			}
			
			// Implémentation de stop
			@Override
			public void stop () {
				// Ne plus écouter le modèle
				model.removeListener(listener);
				
				// Retirer la diffusion des diffusions démarrées
				diffusions.remove(port);
			}
		});
		
		// Retourner l'opération de démarrage
		return diffusion.getStartOperation();
	}
	
	/**
	 * Récupérer l'état d'une diffusion
	 * @param port numéro de port de la diffusion
	 * @return diffusion démarrée ?
	 */
	public boolean isDiffusionDemarree(int port) {
		// Vérifier si la diffusion est présent dans les diffusions démarrées
		return this.diffusions.get(port) != null;
	}
	
	/**
	 * Arrêter une diffusion
	 * @param port numéro de port de la diffusion
	 * @return opération d'arrêt de la diffusion
	 */
	public IOperation getArreterDiffusionOperation(int port) {
		// Récupérer l'opération d'arret 
		return this.diffusions.get(port).getStopOperation();
	}
	
	/**
	 * Tester une diffusion sur un numéro de port donné
	 * @param port numéro de port
	 * @return operation de test de la diffusion 
	 */
	public IOperation getTesterDiffusionOperation (int port) {
		// Créer et retourner le testeur
		return new HTTPHelper.Tester(port);
	}

	/**
	 * Récupérer la liste des thèmes de diffusion disponibles
	 * @return liste des thèmes de diffusion disponibles
	 */
	public ArrayList<InfosTheme> getThemesDiffusion () {
		// Retourner les thèmes disponibles
		try {
			return ConfigurationTheme.getThemes("themes/diffusions");
		} catch (ContestOrgWarningException e) {
			Log.getLogger().error("Erreur lors de la récupération des thèmes disponibles pour les diffusions.",e);
			return null;
		}
	}
}
