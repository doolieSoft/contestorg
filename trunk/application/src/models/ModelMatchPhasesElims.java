package models;

import infos.InfosModelMatchPhasesElims;
import infos.InfosModelParticipation;
import infos.InfosModelParticipationObjectif;
import interfaces.IUpdater;

import java.util.ArrayList;

import log.Log;

import common.TrackableList;
import common.Pair;
import common.Triple;

/**
 * Match de phase éliminatoire
 */
public class ModelMatchPhasesElims extends ModelMatchAbstract
{
	
	// Roles
	public static final int MATCH_SUIVANT = 1;
	public static final int MATCH_PRECEDANT_A = 2;
	public static final int MATCH_PRECEDANT_B = 3;
	
	// Attributs objets
	private ModelPhasesEliminatoires phasesEliminatoires;
	private ModelMatchPhasesElims matchSuivant;
	private ModelMatchPhasesElims matchPrecedantA;
	private ModelMatchPhasesElims matchPrecedantB;
	
	// Constructeur
	public ModelMatchPhasesElims(ModelPhasesEliminatoires phasesEliminatoires, ModelMatchPhasesElims matchPrecedantA, ModelMatchPhasesElims matchPrecedantB, InfosModelMatchPhasesElims infos) {
		// Retenir la phase éliminatoire et les deux matchs précédant
		this.phasesEliminatoires = phasesEliminatoires;
		this.matchPrecedantA = matchPrecedantA;
		this.matchPrecedantB = matchPrecedantB;
		
		// Enregistrer les informations
		this.setInfos(infos);
	}
	protected ModelMatchPhasesElims(ModelPhasesEliminatoires phasesEliminatoires, ModelMatchPhasesElims matchPrecedantA, ModelMatchPhasesElims matchPrecedantB, ModelMatchPhasesElims match) {
		// Appeller le constructeur principal
		this(phasesEliminatoires, matchPrecedantA, matchPrecedantB, match.toInformation());
		
		// Récupérer l'id
		this.setId(match.getId());
	}
	
	// Getters
	public ModelPhasesEliminatoires getPhasesEliminatoire () {
		return this.phasesEliminatoires;
	}
	public ModelMatchPhasesElims getMatchSuivant () {
		return this.matchSuivant;
	}
	public ModelMatchPhasesElims getMatchPrecedantA () {
		return this.matchPrecedantA;
	}
	public ModelMatchPhasesElims getMatchPrecedantB () {
		return this.matchPrecedantB;
	}
	public ModelMatchPhasesElims getFinale () {
		return this.matchSuivant == null ? this : this.matchSuivant.getFinale();
	}
	public int getRang (ModelEquipe equipe) {
		// Equipe jouant à ce match
		if (this.participationA != null && this.participationA.getEquipe().equals(equipe)) {
			return this.participationA.getResultat() == InfosModelParticipation.RESULTAT_VICTOIRE ? 1 : 2;
		}
		if (this.participationB != null && this.participationB.getEquipe().equals(equipe)) {
			return this.participationB.getResultat() == InfosModelParticipation.RESULTAT_VICTOIRE ? 1 : 2;
		}
		
		// Equipe jouant dans les matchs précédant
		if (this.matchPrecedantA != null && this.matchPrecedantB != null) {
			int rangA = this.matchPrecedantA.getRang(equipe);
			int rangB = this.matchPrecedantB.getRang(equipe);
			if (rangA < 0 && rangB < 0) {
				return -1;
			}
			return Math.max(rangA, rangB) * 2;
		}
		
		// Retourner -1 si equipe ne jouant pas aux phases éliminatoires
		return -1;
	}
	public ArrayList<ModelEquipe> getEquipes () {
		// Initialiser la liste des équipes
		ArrayList<ModelEquipe> equipes = new ArrayList<ModelEquipe>();
		
		// Ajouter les équipes
		if (this.matchPrecedantA == null && this.matchPrecedantB == null) {
			equipes.add(this.participationA.getEquipe());
			equipes.add(this.participationB.getEquipe());
		} else {
			equipes.addAll(this.matchPrecedantA.getEquipes());
			equipes.addAll(this.matchPrecedantB.getEquipes());
		}
		
		// Retourner la liste des équipes
		return equipes;
	}
	public boolean isGrandeFinale () {
		return this.equals(this.phasesEliminatoires.getGrandeFinale());
	}
	public boolean isPetiteFinale () {
		return this.equals(this.phasesEliminatoires.getPetiteFinale());
	}
	
	// Setters
	protected void setInfos (InfosModelMatchPhasesElims infos) {
		// Appeller le setInfos du parent
		super.setInfos(infos);
		
		// Fire update
		this.fireUpdate();
	}
	public void setParticipationA (ModelParticipation participation) throws ContestOrgModelException {
		// Vérifier si le match suivant n'a pas déjà été joué
		if (participation == null && this.matchSuivant != null && (this.matchSuivant.getParticipationA() != null || this.matchSuivant.getParticipationB() != null)) {
			throw new ContestOrgModelException("Il n'est pas possible de retirer la participation d'une équipe à un match de phases éliminatoires si le match suivant a été joué.");
		}
		super.setParticipationA(participation);
	}
	public void setParticipationB (ModelParticipation participation) throws ContestOrgModelException {
		if (participation == null && this.matchSuivant != null && (this.matchSuivant.getParticipationA() != null || this.matchSuivant.getParticipationB() != null)) {
			throw new ContestOrgModelException("Il n'est pas possible de retirer la participation d'une équipe à un match de phases éliminatoires si le match suivant a été joué.");
		}
		super.setParticipationB(participation);
	}
	public void setMatchSuivant (ModelMatchPhasesElims matchSuivant) {
		// Retenir le match suivant
		this.matchSuivant = matchSuivant;
		
		// Fire update
		this.fireUpdate();
	}
	
	// Clone
	protected ModelMatchPhasesElims clone (ModelPhasesEliminatoires phasesEliminatoires, ModelMatchPhasesElims matchPrecedantA, ModelMatchPhasesElims matchPrecedantB) {
		return new ModelMatchPhasesElims(phasesEliminatoires, matchPrecedantA, matchPrecedantB, this);
	}
	
	// ToInformation
	public InfosModelMatchPhasesElims toInformation () {
		InfosModelMatchPhasesElims infos = new InfosModelMatchPhasesElims(this.getDate(), this.getDetails());
		infos.setId(this.getId());
		return infos;
	}
	
	// Remove
	@Override
	protected void delete (ArrayList<ModelAbstract> removers) throws ContestOrgModelException {
		if (!removers.contains(this)) {
			// Appeller le remove du parent
			super.delete(removers);
			
			// Supprimer le match suivant
			if (this.matchSuivant != null) {
				if (!removers.contains(this.matchSuivant)) {
					this.matchSuivant.delete(removers);
				}
				this.matchSuivant = null;
				this.fireClear(ModelMatchPhasesElims.class, MATCH_SUIVANT);
			}
			
			// Supprimer le match précédant A
			if (this.matchPrecedantA != null) {
				if (!removers.contains(this.matchPrecedantA)) {
					this.matchPrecedantA.delete(removers);
				}
				this.matchPrecedantA = null;
				this.fireClear(ModelMatchPhasesElims.class, MATCH_PRECEDANT_A);
			}
			
			// Supprimer le match précédant B
			if (this.matchPrecedantB != null) {
				if (!removers.contains(this.matchPrecedantB)) {
					this.matchPrecedantB.delete(removers);
				}
				this.matchPrecedantB = null;
				this.fireClear(ModelMatchPhasesElims.class, MATCH_PRECEDANT_B);
			}
			
			// Fire delete
			this.fireDelete();
		}
	}
	
	// Classe pour mettre à jour une liste d'équipe indépendament de son conteneur
	protected static class Updater implements IUpdater<Triple<Pair<TrackableList<Pair<String, InfosModelParticipationObjectif>>, InfosModelParticipation>, Pair<TrackableList<Pair<String, InfosModelParticipationObjectif>>, InfosModelParticipation>, InfosModelMatchPhasesElims>, ModelMatchPhasesElims>
	{
		
		// Implémentation de create
		@Override
		public ModelMatchPhasesElims create (Triple<Pair<TrackableList<Pair<String, InfosModelParticipationObjectif>>, InfosModelParticipation>, Pair<TrackableList<Pair<String, InfosModelParticipationObjectif>>, InfosModelParticipation>, InfosModelMatchPhasesElims> infos) {
			return null; // La création est effectuée par les phases éliminatoires
		}
		
		// Implémentation de update
		@Override
		public void update (ModelMatchPhasesElims match, Triple<Pair<TrackableList<Pair<String, InfosModelParticipationObjectif>>, InfosModelParticipation>, Pair<TrackableList<Pair<String, InfosModelParticipationObjectif>>, InfosModelParticipation>, InfosModelMatchPhasesElims> infos) {
			try {
				// Modifier le match
				match.setInfos(infos.getThird());
				
				// Modifier la participationA
				ModelParticipation participationA = match.getParticipationA();
				participationA.setInfos(infos.getFirst().getSecond());
				participationA.updateObjectifsRemportes(infos.getFirst().getFirst());
				
				// Modifier la participationB
				ModelParticipation participationB = match.getParticipationB();
				participationB.setInfos(infos.getSecond().getSecond());
				participationB.updateObjectifsRemportes(infos.getSecond().getFirst());
				
				// Modifier l'équipe participante au prochain match et à la petite finale si nécéssaire
				if (match.getMatchSuivant() != null) {
					// Vérifier si le match a un vainqueur
					if (match.getVainqueur() != null) {
						// Modifier/Créer la participation du match suivant
						if (match.getMatchSuivant().getMatchPrecedantA().equals(match)) {
							// Au niveau du match suivant
							if (match.getMatchSuivant().getParticipationA() != null) {
								// Vérifier si l'équipe doit être modifié
								if (match.getMatchSuivant().getParticipationA().getEquipe() == null || !match.getMatchSuivant().getParticipationA().getEquipe().equals(match.getVainqueur().getEquipe())) {
									// Supprimer la participation de l'ancienne équipe
									if (match.getMatchSuivant().getParticipationA().getEquipe() != null) {
										match.getMatchSuivant().getParticipationA().getEquipe().removeParticipation(match.getMatchSuivant().getParticipationA());
									}
									
									// Modifier l'équipe de la participation
									match.getMatchSuivant().getParticipationA().setEquipe(match.getVainqueur().getEquipe());
									
									// Ajouter la participation à la nouvelle équipe
									match.getVainqueur().getEquipe().addParticipation(match.getMatchSuivant().getParticipationA());
								}
							} else {
								// Créer la participation
								ModelParticipation participation = new ModelParticipation(match.getVainqueur().getEquipe(), match.getMatchSuivant(), new InfosModelParticipation(InfosModelParticipation.RESULTAT_ATTENTE));
								match.getMatchSuivant().setParticipationA(participation);
								match.getVainqueur().getEquipe().addParticipation(participation);
							}
							
							// Au niveau de la petite finale si nécéssaire
							if (match.getMatchSuivant().isGrandeFinale()) {
								ModelMatchPhasesElims matchPetiteFinale = match.getPhasesEliminatoire().getPetiteFinale();
								if (matchPetiteFinale.getParticipationA() != null) {
									// Vérifier si l'équipe doit être modifié
									if (matchPetiteFinale.getParticipationA().getEquipe() == null || !matchPetiteFinale.getParticipationA().getEquipe().equals(match.getPerdant().getEquipe())) {
										// Supprimer la participation de l'ancienne équipe
										if (matchPetiteFinale.getParticipationA().getEquipe() != null) {
											matchPetiteFinale.getParticipationA().getEquipe().removeParticipation(matchPetiteFinale.getParticipationA());
										}
										
										// Modifier l'équipe de la participation
										matchPetiteFinale.getParticipationA().setEquipe(match.getPerdant().getEquipe());
										
										// Ajouter la participation à la nouvelle équipe
										match.getPerdant().getEquipe().addParticipation(matchPetiteFinale.getParticipationA());
									}
								} else {
									// Créer la participation
									ModelParticipation participation = new ModelParticipation(match.getPerdant().getEquipe(), matchPetiteFinale, new InfosModelParticipation(InfosModelParticipation.RESULTAT_ATTENTE));
									matchPetiteFinale.setParticipationA(participation);
									match.getPerdant().getEquipe().addParticipation(participation);
								}
							}
						} else {
							// Au niveau du match suivant
							if (match.getMatchSuivant().getParticipationB() != null) {
								// Vérifier si l'équipe doit être modifié
								if (match.getMatchSuivant().getParticipationB().getEquipe() == null || !match.getMatchSuivant().getParticipationB().getEquipe().equals(match.getVainqueur().getEquipe())) {
									// Supprimer la participation de l'ancienne équipe
									if (match.getMatchSuivant().getParticipationB().getEquipe() != null) {
										match.getMatchSuivant().getParticipationB().getEquipe().removeParticipation(match.getMatchSuivant().getParticipationB());
									}
									
									// Modifier l'équipe de la participation
									match.getMatchSuivant().getParticipationB().setEquipe(match.getVainqueur().getEquipe());
									
									// Ajouter la participation à la nouvelle équipe
									match.getVainqueur().getEquipe().addParticipation(match.getMatchSuivant().getParticipationB());
								}
							} else {
								// Créer la participation
								ModelParticipation participation = new ModelParticipation(match.getVainqueur().getEquipe(), match.getMatchSuivant(), new InfosModelParticipation(InfosModelParticipation.RESULTAT_ATTENTE));
								match.getMatchSuivant().setParticipationB(participation);
								match.getVainqueur().getEquipe().addParticipation(participation);
							}
							
							// Au niveau de la petite finale si nécéssaire
							if (match.getMatchSuivant().isGrandeFinale()) {
								ModelMatchPhasesElims matchPetiteFinale = match.getPhasesEliminatoire().getPetiteFinale();
								if (matchPetiteFinale.getParticipationB() != null) {
									// Vérifier si l'équipe doit être modifié
									if (matchPetiteFinale.getParticipationB().getEquipe() == null || !matchPetiteFinale.getParticipationB().getEquipe().equals(match.getPerdant().getEquipe())) {
										// Supprimer la participation de l'ancienne équipe
										if (matchPetiteFinale.getParticipationB().getEquipe() != null) {
											matchPetiteFinale.getParticipationB().getEquipe().removeParticipation(matchPetiteFinale.getParticipationB());
										}
										
										// Modifier l'équipe de la participation
										matchPetiteFinale.getParticipationB().setEquipe(match.getPerdant().getEquipe());
										
										// Ajouter la participation à la nouvelle équipe
										match.getPerdant().getEquipe().addParticipation(matchPetiteFinale.getParticipationB());
									}
								} else {
									// Créer la participation
									ModelParticipation participation = new ModelParticipation(match.getPerdant().getEquipe(), matchPetiteFinale, new InfosModelParticipation(InfosModelParticipation.RESULTAT_ATTENTE));
									matchPetiteFinale.setParticipationB(participation);
									match.getPerdant().getEquipe().addParticipation(participation);
								}
							}
						}
					} else {
						// Supprimer la participation du match suivant et de la petite finale si nécéssaire
						if (match.getMatchSuivant().getMatchPrecedantA().equals(match)) {
							if (match.getMatchSuivant().getParticipationA() != null) {
								match.getMatchSuivant().getParticipationA().delete();
							}
							if (match.getMatchSuivant().isGrandeFinale() && match.getPhasesEliminatoire().getPetiteFinale().getParticipationA() != null) {
								match.getPhasesEliminatoire().getPetiteFinale().getParticipationA().delete();
							}
						} else {
							if (match.getMatchSuivant().getParticipationB() != null) {
								match.getMatchSuivant().getParticipationB().delete();
							}
							if (match.getMatchSuivant().isGrandeFinale() && match.getPhasesEliminatoire().getPetiteFinale().getParticipationB() != null) {
								match.getPhasesEliminatoire().getPetiteFinale().getParticipationB().delete();
							}
						}
					}
				}
			} catch (ContestOrgModelException e) {
				Log.getLogger().fatal("Erreur lors de la modification d'un match de phases éliminatoires.",e);
			}
		}
		
	}
	
}
