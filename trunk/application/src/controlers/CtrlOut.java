package controlers;

import infos.InfosConnexionFTP;
import infos.InfosModelChemin;
import infos.InfosModelDiffusion;
import infos.InfosModelEquipe;
import infos.InfosModelExportation;
import infos.InfosModelTheme;
import infos.Theme;
import interfaces.IEventListener;
import interfaces.IOperation;
import interfaces.IStartStopListener;

import java.util.ArrayList;
import java.util.HashMap;

import log.Log;
import models.FrontModel;
import models.ModelDiffusion;
import models.ModelExportation;
import out.ContestOrgOutException;
import out.DiffusionHTTP;
import out.ExportationAbstract;
import out.ExportationLocal;
import out.FTPHelper;
import out.HTTPHelper;
import out.PersistanceXML;
import out.RessourceAbstract;

import common.Pair;
import common.Triple;

import events.Event;
import events.EventDelete;
import events.EventUpdate;

public class CtrlOut
{

	// ==== Importations
	
	// Importer des équipes
	public ArrayList<InfosModelEquipe> importerEquipes(String chemin) {
		return PersistanceXML.loadEquipes(chemin);
	}
	
	// ==== Exportations
	
	// Récupérer la liste des exportations
	public ArrayList<Triple<InfosModelExportation, InfosModelChemin, InfosModelTheme>> getExportations() {
		return FrontModel.get().getListeExportations();
	}
	
	// Lancer une exportation prédéfinie
	public IOperation getLancerExportationOperation(String nom) {
		// Récupérer le modèle de l'exportation
		ModelExportation exportation = FrontModel.get().getConcours().getExportationByNom(nom);
		
		// Retourner l'opération de lancement de l'exportation
		return ExportationAbstract.export(exportation);
	}
	
	// Lancer une exportation locale
	public IOperation getLancerExportationOperation(String chemin, InfosModelTheme theme) {
		return new ExportationLocal(theme, chemin).export();
	}
		
	// Tester une connexion FTP
	public IOperation getTesterExportationFtpOperation (InfosConnexionFTP infos) {
		// Créer et retourner le testeur
		return new FTPHelper.Tester(infos);
	}

	// Récupérer la liste des ressources disponibles pour les exportations
	public ArrayList<Theme> getThemesExportation () {
		return this.getThemesExportation(null);
	}
	public ArrayList<Theme> getThemesExportation (String categorie) {
		// Retourner les thèmes disponibles
		try {
			return RessourceAbstract.getThemes("themes/exportations",categorie);
		} catch (Exception e) {
			Log.getLogger().error("Erreur lors de la récupération des thèmes disponibles pour les exportations.",e);
			return null; 
		}
	}
		
	// Lancer les exportations automatiques
	public IOperation getLancerExportationsAutomatiquesOperation() {
		// Récupérer la liste des exportations automatiques
		ArrayList<ModelExportation> exportations = new ArrayList<ModelExportation>();
		for(ModelExportation exportation : FrontModel.get().getConcours().getExportations()) {
			// Vérifier s'il s'agit d'une exportation automatique
			if(exportation.isAutomatique()) {
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
	
	// Récupérer la publication
	public Triple<InfosModelExportation, InfosModelChemin, InfosModelTheme> getPublication() {
		return FrontModel.get().getInfosPublication();
	}
	
	// Lancer la publication
	public IOperation getLancerPublicationOperation() {
		return this.getLancerExportationOperation(FrontModel.get().getInfosPublication().getFirst().getNom());
	}
	
	// ==== Diffusions
	
	// Diffusions démarrées
	public HashMap<Integer,DiffusionHTTP> diffusions = new HashMap<Integer,DiffusionHTTP>();
	
	// Récupérer la liste des diffusions
	public ArrayList<Pair<InfosModelDiffusion,InfosModelTheme>> getDiffusions() {
		return FrontModel.get().getListeDiffusions();
	}
	
	// Démarrer une diffusion
	public IOperation getDemarrerDiffusionOperation(final int port) {
		// Récupérer le modèle de la diffusion
		final ModelDiffusion model = FrontModel.get().getConcours().getDiffusionByPort(port);
		
		// Créer la diffusion
		final DiffusionHTTP diffusion = new DiffusionHTTP(model.getTheme().toInformation(),model.getPort());
		
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
	
	// Récupérer l'état d'une diffusion
	public boolean isDiffusionDemarree(int port) {
		// Vérifier si la diffusion est présent dans les diffusions démarrées
		return this.diffusions.get(port) != null;
	}
	
	// Arreter une diffusion
	public IOperation getArreterDiffusionOperation(int port) {
		// Récupérer l'opération d'arret 
		return this.diffusions.get(port).getStopOperation();
	}
	
	// Tester une diffusion HTTP
	public IOperation getTesterDiffusionOperation (int port) {
		// Créer et retourner le testeur
		return new HTTPHelper.Tester(port);
	}

	// Récupérer la liste des ressources disponibles pour les diffusions
	public ArrayList<Theme> getThemesDiffusion () {
		// Retourner les thèmes disponibles
		try {
			return RessourceAbstract.getThemes("themes/diffusions");
		} catch (ContestOrgOutException e) {
			Log.getLogger().error("Erreur lors de la récupération des thèmes disponibles pour les diffusions.",e);
			return null;
		}
	}
}
