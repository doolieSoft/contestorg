package org.contestorg.models;

import java.util.ArrayList;

import org.contestorg.common.ContestOrgErrorException;
import org.contestorg.common.Pair;
import org.contestorg.common.TrackableList;
import org.contestorg.common.Triple;
import org.contestorg.infos.InfosModelChemin;
import org.contestorg.infos.InfosModelCritereClassementAbstract;
import org.contestorg.infos.InfosModelConcours;
import org.contestorg.infos.InfosModelDiffusion;
import org.contestorg.infos.InfosModelEmplacement;
import org.contestorg.infos.InfosModelExportation;
import org.contestorg.infos.InfosModelHoraire;
import org.contestorg.infos.InfosModelLieu;
import org.contestorg.infos.InfosModelObjectif;
import org.contestorg.infos.InfosModelPrix;
import org.contestorg.infos.InfosModelPropriete;
import org.contestorg.infos.InfosModelTheme;
import org.contestorg.interfaces.ITrackableListValidator;

/**
 * Point d'entrée aux modèles concernant la configuration
 */
public class FrontModelConfiguration
{
	// Point d'entrée principal aux modèles
	private FrontModel frontModel;
	
	/**
	 * Constructeur
	 * @param frontModel point d'entrée principal aux modèles
	 */
	public FrontModelConfiguration(FrontModel frontModel) {
		this.frontModel = frontModel;
	}
	
	/**
	 * Configurer le concours
	 * @param infos informations du concours
	 * @param objectifs liste des objectifs
	 * @param criteresClassement liste des critères de classement
	 * @param exportations liste des exportations
	 * @param publication indice de l'exportation qui fait office de publication
	 * @param diffusions liste des diffusions
	 * @param prix liste des prix
	 * @param lieux liste des lieux
	 * @param proprietes liste des propriétés
	 * @throws ContestOrgErrorException
	 */
	public void configurerConcours (InfosModelConcours infos, TrackableList<InfosModelObjectif> objectifs, TrackableList<InfosModelCritereClassementAbstract> criteresClassement, TrackableList<Triple<InfosModelExportation, InfosModelChemin, InfosModelTheme>> exportations, int publication, TrackableList<Pair<InfosModelDiffusion, InfosModelTheme>> diffusions, TrackableList<InfosModelPrix> prix, TrackableList<Triple<InfosModelLieu, TrackableList<InfosModelEmplacement>, TrackableList<InfosModelHoraire>>> lieux, TrackableList<InfosModelPropriete> proprietes) throws ContestOrgErrorException {
		// Démarrer l'action de configuration
		this.frontModel.getHistory().start("Configuration du concours");
		
		// Configurer le concours
		this.frontModel.getConcours().setInfos(infos);
		
		// Mettre à jour les données du concours
		this.frontModel.getConcours().updateObjectifs(objectifs);
		this.frontModel.getConcours().updateCriteresClassement(criteresClassement);
		this.frontModel.getConcours().updateExportations(exportations);
		this.frontModel.getConcours().setPublication(publication == -1 ? null : this.frontModel.getConcours().getExportations().get(publication));
		this.frontModel.getConcours().updateDiffusions(diffusions);
		this.frontModel.getConcours().updatePrix(prix);
		this.frontModel.getConcours().updateLieux(lieux);
		this.frontModel.getConcours().updateProprietes(proprietes);
		
		// Fermer l'action de configuration
		this.frontModel.getHistory().close();
	}
	
	// Récupérer des données uniques
	
	/**
	 * Récupérer l'indice de l'exportation qui fait office de publication
	 * @return indice de l'exportation qui fait office de publication
	 */
	public int getPublicationIndex () {
		ModelConcours concours = this.frontModel.getConcours();
		if (concours.getPublication() != null) {
			return concours.getExportations().indexOf(concours.getPublication());
		}
		return -1;
	}
	
	/**
	 * Récupérer les informations de l'exportation qui fait office de publication
	 * @return informations de l'exportation qui fait office de publication
	 */
	public Triple<InfosModelExportation, InfosModelChemin, InfosModelTheme> getInfosPublication () {
		ModelConcours concours = this.frontModel.getConcours();
		if (concours.getPublication() != null) {
			return new Triple<InfosModelExportation, InfosModelChemin, InfosModelTheme>(concours.getPublication().getInfos(), concours.getPublication().getChemin().getInfos(), concours.getPublication().getTheme().getInfos());
		}
		return null;
	}
	
	// Récupérer des données liste
	
	/**
	 * Récupérer la liste des objectifs
	 * @return liste des objectifs
	 */
	public ArrayList<InfosModelObjectif> getListeObjectifs () {
		ArrayList<InfosModelObjectif> objectifs = new ArrayList<InfosModelObjectif>();
		for (ModelObjectif objectif : this.frontModel.getConcours().getObjectifs()) {
			objectifs.add(objectif.getInfos());
		}
		return objectifs;
	}
	
	/**
	 * Récupérer la liste des exportations
	 * @return liste des exportations
	 */
	public ArrayList<Triple<InfosModelExportation, InfosModelChemin, InfosModelTheme>> getListeExportations () {
		ArrayList<Triple<InfosModelExportation, InfosModelChemin, InfosModelTheme>> exportations = new ArrayList<Triple<InfosModelExportation, InfosModelChemin, InfosModelTheme>>();
		for (ModelExportation exportation : this.frontModel.getConcours().getExportations()) {
			exportations.add(new Triple<InfosModelExportation, InfosModelChemin, InfosModelTheme>(exportation.getInfos(), exportation.getChemin().getInfos(), exportation.getTheme().getInfos()));
		}
		return exportations;
	}
	
	/**
	 * Récupérer la liste des diffusions
	 * @return liste des diffusions
	 */
	public ArrayList<Pair<InfosModelDiffusion, InfosModelTheme>> getListeDiffusions () {
		ArrayList<Pair<InfosModelDiffusion, InfosModelTheme>> diffusions = new ArrayList<Pair<InfosModelDiffusion, InfosModelTheme>>();
		for (ModelDiffusion diffusion : this.frontModel.getConcours().getDiffusions()) {
			diffusions.add(new Pair<InfosModelDiffusion, InfosModelTheme>(diffusion.getInfos(), diffusion.getTheme().getInfos()));
		}
		return diffusions;
	}
	
	/**
	 * Récupérer la liste des prix
	 * @return liste des prix
	 */
	public ArrayList<InfosModelPrix> getListePrix () {
		ArrayList<InfosModelPrix> prixs = new ArrayList<InfosModelPrix>();
		for (ModelPrix prix : this.frontModel.getConcours().getPrix()) {
			prixs.add(prix.getInfos());
		}
		return prixs;
	}
	
	/**
	 * Récupérer la structure des lieux et emplacements
	 * @return structure des lieux et emplacements
	 */
	public ArrayList<Triple<InfosModelLieu, ArrayList<InfosModelEmplacement>, ArrayList<InfosModelHoraire>>> getListeLieuxEmplacements () {
		ArrayList<Triple<InfosModelLieu, ArrayList<InfosModelEmplacement>, ArrayList<InfosModelHoraire>>> lieux = new ArrayList<Triple<InfosModelLieu, ArrayList<InfosModelEmplacement>, ArrayList<InfosModelHoraire>>>();
		for (ModelLieu lieu : this.frontModel.getConcours().getLieux()) {
			ArrayList<InfosModelEmplacement> emplacements = new ArrayList<InfosModelEmplacement>();
			for (ModelEmplacement emplacement : lieu.getEmplacements()) {
				emplacements.add(emplacement.getInfos());
			}
			ArrayList<InfosModelHoraire> horaires = new ArrayList<InfosModelHoraire>();
			for (ModelHoraire horaire : lieu.getHoraires()) {
				horaires.add(horaire.getInfos());
			}
			lieux.add(new Triple<InfosModelLieu, ArrayList<InfosModelEmplacement>, ArrayList<InfosModelHoraire>>(lieu.getInfos(), emplacements, horaires));
		}
		return lieux;
	}
	
	/**
	 * Récupérer la liste des propriétés
	 * @return liste des propriétés
	 */
	public ArrayList<InfosModelPropriete> getListeProprietes () {
		ArrayList<InfosModelPropriete> proprietes = new ArrayList<InfosModelPropriete>();
		for (ModelPropriete propriete : this.frontModel.getConcours().getProprietes()) {
			proprietes.add(propriete.getInfos());
		}
		return proprietes;
	}
	
	/**
	 * Récupérer la liste des critères de classement
	 * @return liste des critères de classement
	 */
	public ArrayList<InfosModelCritereClassementAbstract> getListeCriteresClassement () {
		ArrayList<InfosModelCritereClassementAbstract> criteresClassement = new ArrayList<InfosModelCritereClassementAbstract>();
		for (ModelCritereClassementAbstract critereClassement : this.frontModel.getConcours().getCriteresClassement()) {
			criteresClassement.add(critereClassement.getInfos());
		}
		return criteresClassement;
	}
	
	// Récupérer des validateurs de listes
	
	/**
	 * Récupérer un validateur de liste d'objectifs
	 * @return validateur de liste d'objectifs
	 */
	public ITrackableListValidator<InfosModelObjectif> getObjectifsValidator () {
		return new ModelObjectif.ValidatorForConcours();
	}
	
	/**
	 * Récupérer validateur de liste de critères de classement
	 * @return validateur de liste de critères de classement
	 */
	public ITrackableListValidator<InfosModelCritereClassementAbstract> getCriteresClassementValidator () {
		return new ModelCritereClassementAbstract.ValidatorForConcours();
	}
	
	/**
	 * Récupérer un validateur de liste d'exportations 
	 * @return validateur de liste d'exportations
	 */
	public ITrackableListValidator<Triple<InfosModelExportation, InfosModelChemin, InfosModelTheme>> getExportationsValidator () {
		return new ModelExportation.ValidatorForConcours();
	}
	
	/**
	 * Récupérer un validateur de liste de diffusions
	 * @return validateur de liste de diffusions
	 */
	public ITrackableListValidator<Pair<InfosModelDiffusion, InfosModelTheme>> getDiffusionsValidator () {
		return new ModelDiffusion.ValidatorForConcours();
	}
	
	/**
	 * Récupérer validateur de liste de prix
	 * @return validateur de liste de prix
	 */
	public ITrackableListValidator<InfosModelPrix> getPrixValidator () {
		return new ModelPrix.ValidatorForConcours();
	}
	
	/**
	 * Récupérer un validateur de liste de lieux
	 * @return validateur de liste de lieux
	 */
	public ITrackableListValidator<Triple<InfosModelLieu, TrackableList<InfosModelEmplacement>, TrackableList<InfosModelHoraire>>> getLieuxValidator () {
		return new ModelLieu.ValidatorForConcours();
	}
	
	/**
	 * Récupérer un validateur de liste d'emplacements
	 * @return validateur de liste d'emplacements
	 */
	public ITrackableListValidator<InfosModelEmplacement> getEmplacementsValidator () {
		return new ModelEmplacement.ValidatorForLieu();
	}
	
	/**
	 * Récupérer un validateur de liste d'horaires
	 * @return validateur de liste d'horaires
	 */
	public ITrackableListValidator<InfosModelHoraire> getHorairesValidator () {
		return new ModelHoraire.ValidatorForLieu();
	}
	
	/**
	 * Récupérer un validateur de liste de propriétés
	 * @return validateur de liste de propriétés
	 */
	public ITrackableListValidator<InfosModelPropriete> getProprietesValidator () {
		return new ModelPropriete.ValidatorForConcours();
	}
	
}
