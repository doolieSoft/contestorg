package org.contestorg.models;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.contestorg.common.Pair;
import org.contestorg.common.TrackableList;
import org.contestorg.infos.InfosModelPoule;
import org.contestorg.interfaces.IListValidator;
import org.contestorg.interfaces.IUpdater;
import org.contestorg.log.Log;



/**
 * Conteneur d'équipes
 */
public class ModelPoule extends ModelAbstract
{
	
	// Méthodes de génération
	protected final static int MODE_BASIQUE = 1;
	protected final static int MODE_AVANCE = 2;
	
	// Attributs scalaires
	private String nom;
	
	// Attributs objets
	private ModelCategorie categorie;
	private ArrayList<ModelEquipe> equipes = new ArrayList<ModelEquipe>();
	private ArrayList<ModelPhaseQualificative> phasesQualificatives = new ArrayList<ModelPhaseQualificative>();
	
	// Constructeur
	public ModelPoule(ModelCategorie categorie, InfosModelPoule infos) {
		// Retenir la catégorie
		this.categorie = categorie;
		
		// Enregistrer les informations
		this.setInfos(infos);
	}
	protected ModelPoule(ModelCategorie categorie, ModelPoule poule) {
		// Appeller le constructeur principal
		this(categorie, poule.toInformation());
		
		// Récupérer l'id
		this.setId(poule.getId());
	}
	
	// Getters
	public String getNom () {
		return this.nom;
	}
	public ModelCategorie getCategorie () {
		return this.categorie;
	}
	public ArrayList<ModelEquipe> getEquipes () {
		return new ArrayList<ModelEquipe>(this.equipes);
	}
	public int getNbEquipes() {
		return this.equipes.size();
	}
	public ModelEquipe getEquipeByNom (String nom) {
		for (ModelEquipe equipe : this.equipes) {
			if (equipe.getNom().equals(nom)) {
				return equipe;
			}
		}
		return null;
	}
	public ArrayList<ModelPhaseQualificative> getPhasesQualificatives () {
		return this.phasesQualificatives;
	}
	public ArrayList<ModelMatchPhasesQualifs> getMatchsPhasesQualifs () {
		ArrayList<ModelMatchPhasesQualifs> matchs = new ArrayList<ModelMatchPhasesQualifs>();
		for (ModelPhaseQualificative phase : this.phasesQualificatives) {
			matchs.addAll(phase.getMatchs());
		}
		return matchs;
	}
	@SuppressWarnings("unchecked")
	public ArrayList<ModelEquipe> getClassement () {
		// Trier et retourner la liste des équipes
		Collections.sort(this.equipes, this.categorie.getConcours().getComparateurPhasesQualificatives());
		return this.equipes;
	}
	public int getRang (ModelEquipe equipeA) {
		return this.getRang(equipeA, -1);
	}
	@SuppressWarnings("unchecked")
	public int getRang (ModelEquipe equipeA, int phaseQualifMax) {
		// Retourner -1 si l'équipe ne fait pas partie de la poule
		if (!this.equipes.contains(equipeA)) {
			return -1;
		}
		
		// Initialiser le rang
		int rang = 1;
		
		// Récupérer le comparateur des phases qualificatives
		Comparator<ModelEquipe> comparateur = this.categorie.getConcours().getComparateurPhasesQualificatives(phaseQualifMax);
		
		// Comptabiliser le nombre d'équipe ayant un rang supérieur à l'équipe spécifiée
		for (ModelEquipe equipeB : this.equipes) {
			if (comparateur.compare(equipeA, equipeB) < 0) {
				rang++;
			}
		}
		
		// Retourner le rang
		return rang;
	}
	public ArrayList<ModelEquipe> getEquipesParticipantes () {
		// Créer la liste des équipes qui peuvent participer
		ArrayList<ModelEquipe> participantes = new ArrayList<ModelEquipe>();
		for (ModelEquipe equipe : this.equipes) {
			if (equipe.getStatut().isParticipante()) {
				participantes.add(equipe);
			}
		}
		
		// Retourner la liste des participants
		return participantes;
	}
	public int getNumero () {
		return this.categorie.getPoules().indexOf(this);
	}
	
	// Setters
	protected void setInfos (InfosModelPoule infos) {
		// Appeller le setInfos du parent
		super.setInfos(infos);
		
		// Retenir les informations
		this.nom = infos.getNom();
		
		// Fire update
		this.fireUpdate();
	}
	
	// Adders
	public void addEquipe (ModelEquipe equipe) throws ContestOrgModelException {
		if (!this.equipes.contains(equipe)) {
			// Ajouter l'équipe
			this.equipes.add(equipe);
			
			// Fire add
			this.fireAdd(equipe, this.equipes.size() - 1);
		} else {
			throw new ContestOrgModelException("L'équipe existe déjà dans la poule");
		}
	}
	public void addPhaseQualificative (ModelPhaseQualificative phaseQualificative) throws ContestOrgModelException {
		if (!this.phasesQualificatives.contains(phaseQualificative)) {
			// Ajouter la phase qualificative
			this.phasesQualificatives.add(phaseQualificative);
			
			// Fire add
			this.fireAdd(phaseQualificative, this.phasesQualificatives.size() - 1);
		} else {
			throw new ContestOrgModelException("La phase qualificative existe déjà dans la poule");
		}
	}
	
	// Removers
	protected void removeEquipe (ModelEquipe equipe) throws ContestOrgModelException {
		// Retirer l'équipe
		int index;
		if ((index = this.equipes.indexOf(equipe)) != -1) {
			// Remove
			this.equipes.remove(equipe);
			
			// Fire remove
			this.fireRemove(equipe, index);
		} else {
			throw new ContestOrgModelException("L'équipe n'existe pas dans la poule");
		}
	}
	protected void removePhaseQualificative (ModelPhaseQualificative phaseQualificative) throws ContestOrgModelException {
		// Retirer la phase qualificative
		int index;
		if ((index = this.phasesQualificatives.indexOf(phaseQualificative)) != -1) {
			// Remove
			this.phasesQualificatives.remove(phaseQualificative);
			
			// Fire remove
			this.fireRemove(phaseQualificative, index);
		} else {
			throw new ContestOrgModelException("La phase qualificative n'existe pas dans la poule");
		}
	}
	
	// Clone
	protected ModelPoule clone (ModelCategorie categorie) {
		return new ModelPoule(categorie, this);
	}
	
	// ToInformation
	public InfosModelPoule toInformation () {
		InfosModelPoule infos = new InfosModelPoule(this.nom);
		infos.setId(this.getId());
		return infos;
	}
	
	// Remove
	@Override
	protected void delete (ArrayList<ModelAbstract> removers) throws ContestOrgModelException {
		if (!removers.contains(this)) {
			// Ajouter la poule à la liste des removers
			removers.add(this);
			
			// Supprimer les équipes de la poule ou les déplacer dans la première poule
			for (ModelEquipe equipe : new ArrayList<ModelEquipe>(this.equipes)) {
				if (!removers.contains(equipe)) {
					ModelPoule poule = this.categorie.getPoules().get(0);
					if(poule.equals(this)) {
						equipe.delete(removers);
					} else {
						equipe.setPoule(poule);
					}
				}
			}
			this.equipes.clear();
			this.fireClear(ModelEquipe.class);
			
			// Retirer la poule de la categorie
			if (this.categorie != null) {
				if (!removers.contains(this.categorie)) {
					this.categorie.removePoule(this);
				}
				this.categorie = null;
				this.fireClear(ModelCategorie.class);
			}
			
			
			// Supprimer les phases qualificatives de la poule
			for (ModelPhaseQualificative phaseQualificative : this.phasesQualificatives) {
				if (!removers.contains(phaseQualificative)) {
					phaseQualificative.delete(removers);
				}
			}
			this.phasesQualificatives.clear();
			this.fireClear(ModelPhaseQualificative.class);
			
			// Fire delete
			this.fireDelete();
		}
	}
	
	// Classe pour mettre à jour la liste des poules d'une catégorie et les affectations d'équipe
	protected static class UpdaterForCategorie implements IUpdater<Pair<InfosModelPoule, ArrayList<String>>, ModelPoule>
	{
		// Catégorie
		private ModelCategorie categorie;
		
		// Constructeur
		public UpdaterForCategorie(ModelCategorie categorie) {
			this.categorie = categorie;
		}
		
		// Implémentation de create
		@Override
		public ModelPoule create (Pair<InfosModelPoule, ArrayList<String>> infos) {
			try {
				// Créer la poule
				ModelPoule poule = new ModelPoule(this.categorie, infos.getFirst());
				
				// Déplacer les équipes dans la poule
				for (String equipe : infos.getSecond()) {
					this.categorie.getEquipeByNom(equipe).setPoule(poule);
				}
				
				// Retourner la poule
				return poule;
			} catch (ContestOrgModelException e) {
				Log.getLogger().fatal("Erreur lors de la création d'une poule.",e);
				return null;
			}
		}
		
		// Implémentation de update
		@Override
		public void update (ModelPoule poule, Pair<InfosModelPoule, ArrayList<String>> infos) {
			try {
				// Modifier la poule
				poule.setInfos(infos.getFirst());
				
				// Déplacer les équipes dans la poule
				for (String equipe : infos.getSecond()) {
					if (poule.getEquipeByNom(equipe) == null) {
						this.categorie.getEquipeByNom(equipe).setPoule(poule);
					}
				}
			} catch (ContestOrgModelException e) {
				Log.getLogger().fatal("Erreur lors de la modification d'une poule.",e);
			}
		}
		
	}
	
	// Classe pour valider les opérations sur la liste des poules d'une catégorie et les affectations d'équipe
	protected static class ValidatorForCategorie implements IListValidator<Pair<InfosModelPoule, ArrayList<String>>> {

		// Implémentation de IListValidator
		@Override
		public String validateAdd (Pair<InfosModelPoule, ArrayList<String>> infos, TrackableList<Pair<InfosModelPoule, ArrayList<String>>> list) {
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).getFirst().getNom().equals(infos.getFirst().getNom())) {
					return "Une poule existante possède déjà le même nom.";
				}
			}
			return null;
		}
		@Override
		public String validateUpdate (int row, Pair<InfosModelPoule, ArrayList<String>> infos, TrackableList<Pair<InfosModelPoule, ArrayList<String>>> list) {
			if(row == 0) {
				return "La poule \""+list.get(row).getFirst().getNom()+"\" ne peut pas être modifiée.";
			}
			if(infos.getFirst().getNom().isEmpty()) {
				return "Le nom d'une poule ne peut pas être vide.";
			}
			for (int i = 0; i < list.size(); i++) {
				if (i != row && list.get(i).getFirst().getNom().equals(infos.getFirst().getNom())) {
					return "Une poule existante possède déjà le même nom.";
				}
			}
			return null;
		}
		@Override
		public String validateDelete (int row, TrackableList<Pair<InfosModelPoule, ArrayList<String>>> list) {
			return row == 0 ? "La poule \""+list.get(row).getFirst().getNom()+"\" ne peut pas être supprimée." : null;
		}
		@Override
		public String validateMove (int row, int movement, TrackableList<Pair<InfosModelPoule, ArrayList<String>>> list) {
			return row == 0 ? "La poule \""+list.get(row).getFirst().getNom()+"\" ne peut pas être déplacée." : null;
		}
	}
}
