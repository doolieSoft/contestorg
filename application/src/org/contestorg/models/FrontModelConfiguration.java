package org.contestorg.models;

import java.util.ArrayList;

import org.contestorg.common.Pair;
import org.contestorg.common.TrackableList;
import org.contestorg.common.Triple;
import org.contestorg.infos.InfosModelChemin;
import org.contestorg.infos.InfosModelCompPhasesQualifsAbstract;
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
import org.contestorg.interfaces.IListValidator;

public class FrontModelConfiguration
{
	// Point d'entrée principal aux modèles
	private FrontModel frontModel;
	
	// Constructeur
	public FrontModelConfiguration(FrontModel frontModel) {
		this.frontModel = frontModel;
	}
	
	// Configurer le concours
	public void configurerConcours (InfosModelConcours infos, TrackableList<InfosModelObjectif> objectifs, TrackableList<InfosModelCompPhasesQualifsAbstract> comparateurs, TrackableList<Triple<InfosModelExportation, InfosModelChemin, InfosModelTheme>> exportations, int publication, TrackableList<Pair<InfosModelDiffusion, InfosModelTheme>> diffusions, TrackableList<InfosModelPrix> prix, TrackableList<Triple<InfosModelLieu, TrackableList<InfosModelEmplacement>, TrackableList<InfosModelHoraire>>> lieux, TrackableList<InfosModelPropriete> proprietes) throws ContestOrgModelException {
		// Démarrer l'action de configuration
		this.frontModel.getHistory().start("Configuration du concours");
		
		// Configurer le concours
		this.frontModel.getConcours().setInfos(infos);
		
		// Mettre à jour les données du concours
		this.frontModel.getConcours().updateObjectifs(objectifs);
		this.frontModel.getConcours().updateCompPhasesQualifs(comparateurs);
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
	public int getPublicationIndex () {
		ModelConcours concours = this.frontModel.getConcours();
		if (concours.getPublication() != null) {
			return concours.getExportations().indexOf(concours.getPublication());
		}
		return -1;
	}
	public Triple<InfosModelExportation, InfosModelChemin, InfosModelTheme> getInfosPublication () {
		ModelConcours concours = this.frontModel.getConcours();
		if (concours.getPublication() != null) {
			return new Triple<InfosModelExportation, InfosModelChemin, InfosModelTheme>(concours.getPublication().toInfos(), concours.getPublication().getChemin().toInfos(), concours.getPublication().getTheme().toInfos());
		}
		return null;
	}
	
	// Récupérer des données liste
	public ArrayList<InfosModelObjectif> getListeObjectifs () {
		ArrayList<InfosModelObjectif> objectifs = new ArrayList<InfosModelObjectif>();
		for (ModelObjectif objectif : this.frontModel.getConcours().getObjectifs()) {
			objectifs.add(objectif.toInfos());
		}
		return objectifs;
	}
	public ArrayList<Triple<InfosModelExportation, InfosModelChemin, InfosModelTheme>> getListeExportations () {
		ArrayList<Triple<InfosModelExportation, InfosModelChemin, InfosModelTheme>> exportations = new ArrayList<Triple<InfosModelExportation, InfosModelChemin, InfosModelTheme>>();
		for (ModelExportation exportation : this.frontModel.getConcours().getExportations()) {
			exportations.add(new Triple<InfosModelExportation, InfosModelChemin, InfosModelTheme>(exportation.toInfos(), exportation.getChemin().toInfos(), exportation.getTheme().toInfos()));
		}
		return exportations;
	}
	public ArrayList<Pair<InfosModelDiffusion, InfosModelTheme>> getListeDiffusions () {
		ArrayList<Pair<InfosModelDiffusion, InfosModelTheme>> diffusions = new ArrayList<Pair<InfosModelDiffusion, InfosModelTheme>>();
		for (ModelDiffusion diffusion : this.frontModel.getConcours().getDiffusions()) {
			diffusions.add(new Pair<InfosModelDiffusion, InfosModelTheme>(diffusion.toInfos(), diffusion.getTheme().toInfos()));
		}
		return diffusions;
	}
	public ArrayList<InfosModelPrix> getListePrix () {
		ArrayList<InfosModelPrix> prixs = new ArrayList<InfosModelPrix>();
		for (ModelPrix prix : this.frontModel.getConcours().getPrix()) {
			prixs.add(prix.toInfos());
		}
		return prixs;
	}
	public ArrayList<Triple<InfosModelLieu, ArrayList<InfosModelEmplacement>, ArrayList<InfosModelHoraire>>> getListeLieux () {
		ArrayList<Triple<InfosModelLieu, ArrayList<InfosModelEmplacement>, ArrayList<InfosModelHoraire>>> lieux = new ArrayList<Triple<InfosModelLieu, ArrayList<InfosModelEmplacement>, ArrayList<InfosModelHoraire>>>();
		for (ModelLieu lieu : this.frontModel.getConcours().getLieux()) {
			ArrayList<InfosModelEmplacement> emplacements = new ArrayList<InfosModelEmplacement>();
			for (ModelEmplacement emplacement : lieu.getEmplacements()) {
				emplacements.add(emplacement.toInfos());
			}
			ArrayList<InfosModelHoraire> horaires = new ArrayList<InfosModelHoraire>();
			for (ModelHoraire horaire : lieu.getHoraires()) {
				horaires.add(horaire.toInfos());
			}
			lieux.add(new Triple<InfosModelLieu, ArrayList<InfosModelEmplacement>, ArrayList<InfosModelHoraire>>(lieu.toInfos(), emplacements, horaires));
		}
		return lieux;
	}
	public ArrayList<InfosModelPropriete> getListeProprietes () {
		ArrayList<InfosModelPropriete> proprietes = new ArrayList<InfosModelPropriete>();
		for (ModelPropriete propriete : this.frontModel.getConcours().getProprietes()) {
			proprietes.add(propriete.toInfos());
		}
		return proprietes;
	}
	public ArrayList<InfosModelCompPhasesQualifsAbstract> getListeComparateurs () {
		ArrayList<InfosModelCompPhasesQualifsAbstract> comparateurs = new ArrayList<InfosModelCompPhasesQualifsAbstract>();
		for (ModelCompPhasesQualifsAbstract comparateur : this.frontModel.getConcours().getCompsPhasesQualifs()) {
			comparateurs.add(comparateur.toInfos());
		}
		return comparateurs;
	}
	
	// Récupérer des validateurs de listes
	public IListValidator<InfosModelObjectif> getObjectifsValidator () {
		return new ModelObjectif.ValidatorForConcours();
	}
	public IListValidator<InfosModelCompPhasesQualifsAbstract> getComparateursValidator () {
		return new ModelCompPhasesQualifsAbstract.ValidatorForConcours();
	}
	public IListValidator<Triple<InfosModelExportation, InfosModelChemin, InfosModelTheme>> getExportationsValidator () {
		return new ModelExportation.ValidatorForConcours();
	}
	public IListValidator<Pair<InfosModelDiffusion, InfosModelTheme>> getDiffusionsValidator () {
		return new ModelDiffusion.ValidatorForConcours();
	}
	public IListValidator<InfosModelPrix> getPrixValidator () {
		return new ModelPrix.ValidatorForConcours();
	}
	public IListValidator<Triple<InfosModelLieu, TrackableList<InfosModelEmplacement>, TrackableList<InfosModelHoraire>>> getLieuxValidator () {
		return new ModelLieu.ValidatorForConcours();
	}
	public IListValidator<InfosModelEmplacement> getEmplacementsValidator () {
		return new ModelEmplacement.ValidatorForLieu();
	}
	public IListValidator<InfosModelHoraire> getHorairesValidator () {
		return new ModelHoraire.ValidatorForLieu();
	}
	public IListValidator<InfosModelPropriete> getProprietesValidator () {
		return new ModelPropriete.ValidatorForConcours();
	}
	
}
