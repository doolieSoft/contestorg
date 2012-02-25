package org.contestorg.models;

import java.util.ArrayList;

import org.contestorg.common.Pair;
import org.contestorg.common.TrackableList;
import org.contestorg.common.Triple;
import org.contestorg.infos.InfosModelMatchPhasesElims;
import org.contestorg.infos.InfosModelObjectifRemporte;
import org.contestorg.infos.InfosModelParticipation;
import org.contestorg.interfaces.IUpdater;
import org.contestorg.log.Log;

/**
 * Match des phases éliminatoires
 */
public class ModelMatchPhasesElims extends ModelMatchAbstract
{
	
	// Rôles
	
	/** Match suivant */
	public static final int MATCH_SUIVANT = 1;
	
	/** Match précédant A */
	public static final int MATCH_PRECEDANT_A = 2;
	
	/** Match précédant B */
	public static final int MATCH_PRECEDANT_B = 3;
	
	// Attributs objets
	
	/** Phases éliminatoires */
	private ModelPhasesEliminatoires phasesEliminatoires;
	
	/** Match suivant */
	private ModelMatchPhasesElims matchSuivant;
	
	/** Match précédant A */
	private ModelMatchPhasesElims matchPrecedantA;
	
	/** Match précédant B */
	private ModelMatchPhasesElims matchPrecedantB;
	
	// Constructeurs
	
	/**
	 * Constructeur
	 * @param phasesEliminatoires phases éliminatoires
	 * @param matchPrecedantA match précédant A
	 * @param matchPrecedantB match précédant V
	 * @param infos informations du match des phases éliminatoires
	 */
	public ModelMatchPhasesElims(ModelPhasesEliminatoires phasesEliminatoires, ModelMatchPhasesElims matchPrecedantA, ModelMatchPhasesElims matchPrecedantB, InfosModelMatchPhasesElims infos) {
		// Retenir la phase éliminatoire et les deux matchs précédant
		this.phasesEliminatoires = phasesEliminatoires;
		this.matchPrecedantA = matchPrecedantA;
		this.matchPrecedantB = matchPrecedantB;
		
		// Enregistrer les informations
		this.setInfos(infos);
	}
	
	/**
	 * Constructeur par copie
	 * @param phasesEliminatoires phases éliminatoires
	 * @param matchPrecedantA match précédant A
	 * @param matchPrecedantB match précédant B
	 * @param match match de phases éliminatoires
	 */
	protected ModelMatchPhasesElims(ModelPhasesEliminatoires phasesEliminatoires, ModelMatchPhasesElims matchPrecedantA, ModelMatchPhasesElims matchPrecedantB, ModelMatchPhasesElims match) {
		// Appeller le constructeur principal
		this(phasesEliminatoires, matchPrecedantA, matchPrecedantB, match.getInfos());
		
		// Récupérer l'id
		this.setId(match.getId());
	}
	
	// Getters
	
	/**
	 * Récupérer les phases éliminatoires
	 * @return phases éliminatoires
	 */
	public ModelPhasesEliminatoires getPhasesEliminatoire () {
		return this.phasesEliminatoires;
	}
	
	/**
	 * Récupérer le match suivant
	 * @return match suivant
	 */
	public ModelMatchPhasesElims getMatchSuivant () {
		return this.matchSuivant;
	}
	
	/**
	 * Récupérer le match précédant A
	 * @return match précédant A
	 */
	public ModelMatchPhasesElims getMatchPrecedantA () {
		return this.matchPrecedantA;
	}
	
	/**
	 * Récuérer le match précédant B
	 * @return match précédant B
	 */
	public ModelMatchPhasesElims getMatchPrecedantB () {
		return this.matchPrecedantB;
	}
	
	/**
	 * Récupérer la finale
	 * @return finale
	 */
	public ModelMatchPhasesElims getFinale () {
		return this.matchSuivant == null ? this : this.matchSuivant.getFinale();
	}
	
	/**
	 * Récupérer le rang d'un participant
	 * @param participant participant
	 * @return rang du participant
	 */
	public int getRang (ModelParticipant participant) {
		// Participant jouant à ce match
		if (this.participationA != null && this.participationA.getParticipant().equals(participant)) {
			return this.participationA.getResultat() == InfosModelParticipation.RESULTAT_VICTOIRE ? 1 : 2;
		}
		if (this.participationB != null && this.participationB.getParticipant().equals(participant)) {
			return this.participationB.getResultat() == InfosModelParticipation.RESULTAT_VICTOIRE ? 1 : 2;
		}
		
		// Participant jouant dans les matchs précédant
		if (this.matchPrecedantA != null && this.matchPrecedantB != null) {
			int rangA = this.matchPrecedantA.getRang(participant);
			int rangB = this.matchPrecedantB.getRang(participant);
			if (rangA < 0 && rangB < 0) {
				return -1;
			}
			return Math.max(rangA, rangB) * 2;
		}
		
		// Retourner -1 si participant ne jouant pas aux phases éliminatoires
		return -1;
	}
	
	/**
	 * Récupérer la liste des participants
	 * @return liste des participants
	 */
	public ArrayList<ModelParticipant> getParticipants () {
		// Initialiser la liste des participants
		ArrayList<ModelParticipant> participants = new ArrayList<ModelParticipant>();
		
		// Ajouter les participants
		if (this.matchPrecedantA == null && this.matchPrecedantB == null) {
			participants.add(this.participationA.getParticipant());
			participants.add(this.participationB.getParticipant());
		} else {
			participants.addAll(this.matchPrecedantA.getParticipants());
			participants.addAll(this.matchPrecedantB.getParticipants());
		}
		
		// Retourner la liste des participants
		return participants;
	}
	
	/**
	 * Vérifier s'il s'agit de la grande finale ?
	 * @return grande finale ?
	 */
	public boolean isGrandeFinale () {
		return this.equals(this.phasesEliminatoires.getGrandeFinale());
	}
	
	/**
	 * Vérifier s'il s'agit de la petite finale ?
	 * @return petite finale ?
	 */
	public boolean isPetiteFinale () {
		return this.equals(this.phasesEliminatoires.getPetiteFinale());
	}
	
	// Setters
	
	/**
	 * Définir les informations du match des phases éliminatoires
	 * @param infos informations du match des phases éliminatoires
	 */
	protected void setInfos (InfosModelMatchPhasesElims infos) {
		// Appeller le setInfos du parent
		super.setInfos(infos);
		
		// Fire update
		this.fireUpdate();
	}
	
	/**
	 * Définir la participation A
	 * @param participation participant A
	 * @throws ContestOrgModelException
	 */
	public void setParticipationA (ModelParticipation participation) throws ContestOrgModelException {
		// Vérifier si le match suivant n'a pas déjà été joué
		if (participation == null && this.matchSuivant != null && (this.matchSuivant.getParticipationA() != null || this.matchSuivant.getParticipationB() != null)) {
			throw new ContestOrgModelException("Il n'est pas possible de retirer la participation d'un participant à un match de phases éliminatoires si le match suivant a été joué.");
		}
		super.setParticipationA(participation);
	}
	
	/**
	 * Définir la participation B
	 * @param participation participation B
	 * @throws ContestOrgModelException
	 */
	public void setParticipationB (ModelParticipation participation) throws ContestOrgModelException {
		if (participation == null && this.matchSuivant != null && (this.matchSuivant.getParticipationA() != null || this.matchSuivant.getParticipationB() != null)) {
			throw new ContestOrgModelException("Il n'est pas possible de retirer la participation d'un participant à un match de phases éliminatoires si le match suivant a été joué.");
		}
		super.setParticipationB(participation);
	}
	
	/**
	 * Définir le match suivant
	 * @param matchSuivant match suivant
	 */
	public void setMatchSuivant (ModelMatchPhasesElims matchSuivant) {
		// Retenir le match suivant
		this.matchSuivant = matchSuivant;
		
		// Fire update
		this.fireUpdate();
	}
	
	/**
	 * Cloner le match des phases éliminatoires
	 * @param phasesEliminatoires phases éliminatoires
	 * @param matchPrecedantA match précédant A
	 * @param matchPrecedantB match précédant B
	 * @return clone du match des phases éliminatoires
	 */
	protected ModelMatchPhasesElims clone (ModelPhasesEliminatoires phasesEliminatoires, ModelMatchPhasesElims matchPrecedantA, ModelMatchPhasesElims matchPrecedantB) {
		return new ModelMatchPhasesElims(phasesEliminatoires, matchPrecedantA, matchPrecedantB, this);
	}
	
	/**
	 * @see ModelAbstract#getInfos()
	 */
	public InfosModelMatchPhasesElims getInfos () {
		InfosModelMatchPhasesElims infos = new InfosModelMatchPhasesElims(this.getDate(), this.getDetails());
		infos.setId(this.getId());
		return infos;
	}
	
	/**
	 * @see ModelMatchAbstract#delete(ArrayList)
	 */
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
	
	/**
	 * Classe pour mettre à jour une liste de participants indépendament de son conteneur
	 */
	protected static class Updater implements IUpdater<Triple<Pair<TrackableList<Pair<String, InfosModelObjectifRemporte>>, InfosModelParticipation>, Pair<TrackableList<Pair<String, InfosModelObjectifRemporte>>, InfosModelParticipation>, InfosModelMatchPhasesElims>, ModelMatchPhasesElims>
	{
		
		/**
		 * @see IUpdater#create(Object)
		 */
		@Override
		public ModelMatchPhasesElims create (Triple<Pair<TrackableList<Pair<String, InfosModelObjectifRemporte>>, InfosModelParticipation>, Pair<TrackableList<Pair<String, InfosModelObjectifRemporte>>, InfosModelParticipation>, InfosModelMatchPhasesElims> infos) {
			return null; // La création est effectuée par les phases éliminatoires
		}
		
		/**
		 * @see IUpdater#update(Object, Object)
		 */
		@Override
		public void update (ModelMatchPhasesElims match, Triple<Pair<TrackableList<Pair<String, InfosModelObjectifRemporte>>, InfosModelParticipation>, Pair<TrackableList<Pair<String, InfosModelObjectifRemporte>>, InfosModelParticipation>, InfosModelMatchPhasesElims> infos) {
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
				
				// Modifier le participant au prochain match et à la petite finale si nécéssaire
				if (match.getMatchSuivant() != null) {
					// Vérifier si le match a un vainqueur
					if (match.getVainqueur() != null) {
						// Modifier/Créer la participation du match suivant
						if (match.getMatchSuivant().getMatchPrecedantA().equals(match)) {
							// Au niveau du match suivant
							if (match.getMatchSuivant().getParticipationA() != null) {
								// Vérifier si le participant doit être modifié
								if (match.getMatchSuivant().getParticipationA().getParticipant() == null || !match.getMatchSuivant().getParticipationA().getParticipant().equals(match.getVainqueur().getParticipant())) {
									// Supprimer la participation de l'ancien participant
									if (match.getMatchSuivant().getParticipationA().getParticipant() != null) {
										match.getMatchSuivant().getParticipationA().getParticipant().removeParticipation(match.getMatchSuivant().getParticipationA());
									}
									
									// Modifier le participant de la participation
									match.getMatchSuivant().getParticipationA().setParticipant(match.getVainqueur().getParticipant());
									
									// Ajouter la participation au nouveau participant
									match.getVainqueur().getParticipant().addParticipation(match.getMatchSuivant().getParticipationA());
								}
							} else {
								// Créer la participation
								ModelParticipation participation = new ModelParticipation(match.getVainqueur().getParticipant(), match.getMatchSuivant(), new InfosModelParticipation(InfosModelParticipation.RESULTAT_ATTENTE));
								match.getMatchSuivant().setParticipationA(participation);
								match.getVainqueur().getParticipant().addParticipation(participation);
							}
							
							// Au niveau de la petite finale si nécéssaire
							if (match.getMatchSuivant().isGrandeFinale()) {
								ModelMatchPhasesElims matchPetiteFinale = match.getPhasesEliminatoire().getPetiteFinale();
								if (matchPetiteFinale.getParticipationA() != null) {
									// Vérifier si le participant doit être modifié
									if (matchPetiteFinale.getParticipationA().getParticipant() == null || !matchPetiteFinale.getParticipationA().getParticipant().equals(match.getPerdant().getParticipant())) {
										// Supprimer la participation de l'ancien participant
										if (matchPetiteFinale.getParticipationA().getParticipant() != null) {
											matchPetiteFinale.getParticipationA().getParticipant().removeParticipation(matchPetiteFinale.getParticipationA());
										}
										
										// Modifier le participant de la participation
										matchPetiteFinale.getParticipationA().setParticipant(match.getPerdant().getParticipant());
										
										// Ajouter la participation au nouveau participant
										match.getPerdant().getParticipant().addParticipation(matchPetiteFinale.getParticipationA());
									}
								} else {
									// Créer la participation
									ModelParticipation participation = new ModelParticipation(match.getPerdant().getParticipant(), matchPetiteFinale, new InfosModelParticipation(InfosModelParticipation.RESULTAT_ATTENTE));
									matchPetiteFinale.setParticipationA(participation);
									match.getPerdant().getParticipant().addParticipation(participation);
								}
							}
						} else {
							// Au niveau du match suivant
							if (match.getMatchSuivant().getParticipationB() != null) {
								// Vérifier si le participant doit être modifié
								if (match.getMatchSuivant().getParticipationB().getParticipant() == null || !match.getMatchSuivant().getParticipationB().getParticipant().equals(match.getVainqueur().getParticipant())) {
									// Supprimer la participation de l'ancien participant
									if (match.getMatchSuivant().getParticipationB().getParticipant() != null) {
										match.getMatchSuivant().getParticipationB().getParticipant().removeParticipation(match.getMatchSuivant().getParticipationB());
									}
									
									// Modifier le participant de la participation
									match.getMatchSuivant().getParticipationB().setParticipant(match.getVainqueur().getParticipant());
									
									// Ajouter la participation au nouveau participant
									match.getVainqueur().getParticipant().addParticipation(match.getMatchSuivant().getParticipationB());
								}
							} else {
								// Créer la participation
								ModelParticipation participation = new ModelParticipation(match.getVainqueur().getParticipant(), match.getMatchSuivant(), new InfosModelParticipation(InfosModelParticipation.RESULTAT_ATTENTE));
								match.getMatchSuivant().setParticipationB(participation);
								match.getVainqueur().getParticipant().addParticipation(participation);
							}
							
							// Au niveau de la petite finale si nécéssaire
							if (match.getMatchSuivant().isGrandeFinale()) {
								ModelMatchPhasesElims matchPetiteFinale = match.getPhasesEliminatoire().getPetiteFinale();
								if (matchPetiteFinale.getParticipationB() != null) {
									// Vérifier si le participant doit être modifié
									if (matchPetiteFinale.getParticipationB().getParticipant() == null || !matchPetiteFinale.getParticipationB().getParticipant().equals(match.getPerdant().getParticipant())) {
										// Supprimer la participation de l'ancien participant
										if (matchPetiteFinale.getParticipationB().getParticipant() != null) {
											matchPetiteFinale.getParticipationB().getParticipant().removeParticipation(matchPetiteFinale.getParticipationB());
										}
										
										// Modifier le participant de la participation
										matchPetiteFinale.getParticipationB().setParticipant(match.getPerdant().getParticipant());
										
										// Ajouter la participation au nouveau participant
										match.getPerdant().getParticipant().addParticipation(matchPetiteFinale.getParticipationB());
									}
								} else {
									// Créer la participation
									ModelParticipation participation = new ModelParticipation(match.getPerdant().getParticipant(), matchPetiteFinale, new InfosModelParticipation(InfosModelParticipation.RESULTAT_ATTENTE));
									matchPetiteFinale.setParticipationB(participation);
									match.getPerdant().getParticipant().addParticipation(participation);
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
