package org.contestorg.models;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;

import org.contestorg.common.ContestOrgErrorException;
import org.contestorg.common.GenerationAbstract;
import org.contestorg.common.GenerationConfigurations;
import org.contestorg.common.GenerationRunnableAbstract;
import org.contestorg.comparators.CompPhasesQualifs;
import org.contestorg.infos.InfosConfiguration;
import org.contestorg.infos.InfosConfigurationCouple;
import org.contestorg.infos.InfosModelMatchPhasesQualifs;
import org.contestorg.infos.InfosModelParticipation;
import org.contestorg.infos.InfosModelPhaseQualificative;
import org.contestorg.interfaces.IGeneration;

/**
 * Phase qualificative
 */
public class ModelPhaseQualificative extends ModelAbstract
{
	
	// Attributs objets
	
	/** Poule */
	private ModelPoule poule;
	
	/** Liste des matchs */
	private ArrayList<ModelMatchPhasesQualifs> matchs = new ArrayList<ModelMatchPhasesQualifs>();
	
	// Constructeurs
	
	/**
	 * Constructeur
	 * @param poule poule
	 * @param infos informations de la phase qualificative
	 */
	public ModelPhaseQualificative(ModelPoule poule, InfosModelPhaseQualificative infos) {
		// Retenir la poule
		this.poule = poule;
		
		// Enregistrer les informations
		this.setInfos(infos);
	}
	
	/**
	 * Constructeur
	 * @param poule poule
	 * @param infosMatchs informations des matchs
	 * @param configuration configuration de participants
	 * @param infos informations de la phase qualificative
	 * @throws ContestOrgErrorException
	 */
	protected ModelPhaseQualificative(ModelPoule poule, InfosModelMatchPhasesQualifs infosMatchs, InfosConfiguration<ModelParticipant> configuration, InfosModelPhaseQualificative infos) throws ContestOrgErrorException {
		// Appeller le constructeur principal
		this(poule, infos);
		
		// Ajouter les couples de la configuration
		if (configuration != null) {
			this.setConfiguration(infosMatchs, configuration);
		}
	}
	
	/**
	 * Constructeur
	 * @param poule poule
	 * @param phaseQualificative phase qualificative
	 */
	protected ModelPhaseQualificative(ModelPoule poule, ModelPhaseQualificative phaseQualificative) {
		// Appeller le constructeur principal
		this(poule, phaseQualificative.getInfos());
		
		// Récupérer l'id
		this.setId(phaseQualificative.getId());
	}
	
	// Getters
	
	/**
	 * Récupérer la poule
	 * @return poule
	 */
	public ModelPoule getPoule () {
		return this.poule;
	}
	
	/**
	 * Récupérer la liste des matchs
	 * @return liste des matchs
	 */
	public ArrayList<ModelMatchPhasesQualifs> getMatchs () {
		return new ArrayList<ModelMatchPhasesQualifs>(this.matchs);
	}
	
	/**
	 * Récupérer la liste des participants
	 * @return liste des participants
	 */
	public ArrayList<ModelParticipant> getParticipants () {
		// Initialiser la liste des participants
		ArrayList<ModelParticipant> participants = new ArrayList<ModelParticipant>();
		
		// Récupérer la liste des participants
		for (ModelMatchPhasesQualifs match : this.matchs) {
			if (match.getParticipationA() != null) {
				participants.add(match.getParticipationA().getParticipant());
			}
			if (match.getParticipationB() != null) {
				participants.add(match.getParticipationB().getParticipant());
			}
		}
		
		// Retourner la liste des participants
		return participants;
	}
	
	/**
	 * Récupérer le classement des participants
	 * @return classement des participants
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<ModelParticipant> getClassement () {
		// Trier et retourner la liste des participants
		ArrayList<ModelParticipant> participants = this.getParticipants();
		CompPhasesQualifs comparateur = this.poule.getCategorie().getConcours().getComparateurPhasesQualificatives(this.getNumero());
		comparateur.etablirClassement(participants);
		Collections.sort(participants, comparateur);
		return participants;
	}
	
	/**
	 * Récupérer le rang d'un participant
	 * @param participant participant
	 * @return rang du participant
	 */
	public int getRang (ModelParticipant participant) {
		// Récupérer les participants
		ArrayList<ModelParticipant> participants = this.getParticipants();
		
		// Retourner -1 si le participant ne fait pas partie de la poule
		if (!participants.contains(participant)) {
			return -1;
		}
		
		// Récupérer le comparateur des phases qualificatives
		CompPhasesQualifs comparateur = this.poule.getCategorie().getConcours().getComparateurPhasesQualificatives(this.getNumero());
		
		// Etablir le classement
		Map<ModelParticipant, Integer> classement = comparateur.etablirClassement(participants);
		
		// Retourner le rang
		return classement.get(participant);
	}
	
	/**
	 * Récupérer le numéro
	 * @return numéro
	 */
	public int getNumero () {
		return this.poule.getPhasesQualificatives().indexOf(this);
	}
	
	/**
	 * @see ModelAbstract#getInfos()
	 */
	public InfosModelPhaseQualificative getInfos () {
		InfosModelPhaseQualificative infos = new InfosModelPhaseQualificative();
		infos.setId(this.getId());
		return infos;
	}
	
	// Setters
	
	/**
	 * Définir les informations de la phase qualificative
	 * @param infos informations de la phase qualificative
	 */
	protected void setInfos (InfosModelPhaseQualificative infos) {
		// Appeller le setInfos du parent
		super.setInfos(infos);
		
		// Fire update
		this.fireUpdate();
	}
	
	/**
	 * Modifier la liste des matchs
	 * @param infosMatchs informations des matchs
	 * @param configuration configuration de participants
	 * @throws ContestOrgErrorException
	 */
	protected void setConfiguration (InfosModelMatchPhasesQualifs infosMatchs, InfosConfiguration<ModelParticipant> configuration) throws ContestOrgErrorException {
		// Supprimer tout les matchs de la phase qualificative
		for (ModelMatchPhasesQualifs match : this.matchs) {
			match.delete(this);
		}
		this.matchs.clear();
		this.fireClear(ModelMatchPhasesQualifs.class);
		
		// Ajouter tous les couples de la configuration
		for (InfosConfigurationCouple<ModelParticipant> couple : configuration.getCouples()) {
			// Créer le match
			ModelMatchPhasesQualifs match = new ModelMatchPhasesQualifs(this, null, infosMatchs);
			
			// Créer la participation A et l'ajouter au participant et au match
			ModelParticipant participantA = couple.getParticipantA();
			ModelParticipation participationA = new ModelParticipation(participantA, match, new InfosModelParticipation(InfosModelParticipation.RESULTAT_ATTENTE));
			if (participantA != null) {
				participantA.addParticipation(participationA);
			}
			match.setParticipationA(participationA);
			
			// Créer la participation A et l'ajouter au participant
			ModelParticipant participantB = couple.getParticipantB();
			ModelParticipation participationB = new ModelParticipation(participantB, match, new InfosModelParticipation(InfosModelParticipation.RESULTAT_ATTENTE));
			if (participantB != null) {
				participantB.addParticipation(participationB);
			}
			match.setParticipationB(participationB);
			
			// Ajouter le match à la phase qualificative
			this.addMatch(match);
		}
	}
	
	// Adders
	
	/**
	 * Ajouter un match
	 * @param match match
	 * @throws ContestOrgErrorException
	 */
	public void addMatch (ModelMatchPhasesQualifs match) throws ContestOrgErrorException {
		if (!this.matchs.contains(match)) {
			// Ajouter le match
			this.matchs.add(match);
			
			// Fire add
			this.fireAdd(match, this.matchs.size() - 1);
		} else {
			throw new ContestOrgErrorException("Le match existe déjà dans la phase qualificative");
		}
	}
	
	// Removers
	
	/**
	 * Supprimer un match
	 * @param match match
	 * @throws ContestOrgErrorException
	 */
	protected void removeMatch (ModelMatchPhasesQualifs match) throws ContestOrgErrorException {
		// Retirer le match
		int index;
		if ((index = this.matchs.indexOf(match)) != -1) {
			// Remove
			this.matchs.remove(match);
			
			// Fire remove
			this.fireRemove(match, index);
		} else {
			throw new ContestOrgErrorException("Le match n'existe pas dans la phase qualificative");
		}
	}
	
	/**
	 * Cloner la phase qualificative
	 * @param poule poule
	 * @return clone de la phase qualificative
	 */
	protected ModelPhaseQualificative clone (ModelPoule poule) {
		return new ModelPhaseQualificative(poule, this);
	}
	
	/**
	 * @see ModelAbstract#delete(ArrayList)
	 */
	protected void delete (ArrayList<ModelAbstract> removers) throws ContestOrgErrorException {
		if (!removers.contains(this)) {
			// Ajouter la phase qualificative à la liste des removers
			removers.add(this);
			
			// Retirer la phase qualificative de la poule
			if (this.poule != null) {
				if (!removers.contains(this.poule)) {
					this.poule.removePhaseQualificative(this);
				}
				this.poule = null;
				this.fireClear(ModelPoule.class);
			}
			
			// Supprimer les matchs de la phase qualificative
			for (ModelMatchAbstract match : this.matchs) {
				if (!removers.contains(match)) {
					match.delete(removers);
				}
			}
			this.matchs.clear();
			this.fireClear(ModelMatchPhasesQualifs.class);
			
			// Fire delete
			this.fireDelete();
		}
	}
	
	// Générations
	
	/**
	 * Générer une phase qualificative en mode avancé
	 * @param participants liste des participants
	 * @param comparateur comparateur de configurations de participants
	 * @return génération de phase qualificative en mode avancé
	 */
	protected static IGeneration<InfosConfiguration<ModelParticipant>> genererConfigurationAvance (final ModelParticipant[] participants, final Comparator<InfosConfiguration<ModelParticipant>> comparateur) {
		// Créer et retourner la génération
		return new GenerationAbstract<InfosConfiguration<ModelParticipant>>() {
			@Override
			public GenerationRunnableAbstract<InfosConfiguration<ModelParticipant>> getRunnable () {
				return new GenerationAvancee(participants, comparateur);
			}
		};
	}
	
	/**
	 * Générer une phase qualificative en mode basique
	 * @param participants liste des participants
	 * @param comparateur comparateur de configurations de participants
	 * @return génération de phase qualificative en mode basique
	 */
	protected static IGeneration<InfosConfiguration<ModelParticipant>> genererConfigurationBasique (final ModelParticipant[] participants, final Comparator<InfosConfigurationCouple<ModelParticipant>> comparateur) {
		// Créer et retourner la génération
		return new GenerationAbstract<InfosConfiguration<ModelParticipant>>() {
			@Override
			public GenerationRunnableAbstract<InfosConfiguration<ModelParticipant>> getRunnable () {
				return new GenerationBasique(participants, comparateur);
			}
		};
	}
	
	/**
	 * Classe permettant d'effectuer une génération en mode avancé
	 */
	private static class GenerationAvancee extends GenerationRunnableAbstract<InfosConfiguration<ModelParticipant>>
	{
		/** Liste des participants */
		private ModelParticipant[] participants;
		
		/** Comparateur de configurations de participants */
		private Comparator<InfosConfiguration<ModelParticipant>> comparateur;
		
		// Initialiser la meilleure configuration
		private InfosConfiguration<ModelParticipant> max = null;
		
		/**
		 * Constructeur
		 * @param participants liste des participants
		 * @param comparateur comparateur de configurations de participants
		 */
		public GenerationAvancee(ModelParticipant[] participants, Comparator<InfosConfiguration<ModelParticipant>> comparateur) {
			this.participants = participants;
			this.comparateur = comparateur;
		}
		
		/**
		 * @see Runnable#run()
		 */
		@Override
		public void run () {
			// Lancement de la génération
			this.fireMessage("Lancement de la génération ...");
			
			// Créer le générateur de configurations
			GenerationConfigurations generateur = new GenerationConfigurations(this.participants.length);
			
			// Récupérer le nombre total de configurations
			final int nbTotalConfigurations = generateur.getNbTotalConfigurations();
			
			// Informations pour le message d'avancement
			final DecimalFormat format = new DecimalFormat("0.00E00");
			final String total = format.format(nbTotalConfigurations);
			
			// Trouver la meilleure configuration
			generateur.generer(new GenerationConfigurations.IGenerationConfigurationsListener() {
				/**
				 * @see GenerationConfigurations.IGenerationConfigurationsListener#nouvelleConfiguration(int,int[])
				 */
				@Override
				public boolean nouvelleConfiguration (int i,int[] configuration) {
					// Construire la configuration
					InfosConfiguration<ModelParticipant> configurationParticipants = new InfosConfiguration<ModelParticipant>(GenerationConfigurations.trier(configuration, participants));
					
					// Comparer la configuration à la meilleure configuration trouvée jusque là
					if (comparateur.compare(max, configurationParticipants) < 0) {
						// Enregistrer la meilleure configuration
						max = configurationParticipants;
						
						// Prévenir les listeners d'une nouvelle configuration
						fireMax(configurationParticipants);
					}
					
					// Informer les listeners de l'avancement
					if(i%200 == 0) {
						fireAvancement((double)i/nbTotalConfigurations);
						fireMessage("Traitement de la configuration "+format.format(i)+" sur les "+total+" configurations possibles ...");
					}
					
					// Continuer ?
					return !arret && !annuler;
				}
			});
			
			// Informer les listeners de la fin, de l'arret ou de l'annulation de la génération
			if (this.annuler) {
				this.fireAnnulation();
			} else if (this.arret) {
				this.fireArret();
			} else {
				this.fireMessage("Configuration générée !");
				this.fireAvancement(1);
				this.fireFin();
			}
		}
		
		
	}
	
	/**
	 * Classe permettant d'effectuer une génération en mode basique
	 */
	private static class GenerationBasique extends GenerationRunnableAbstract<InfosConfiguration<ModelParticipant>>
	{
		/** Liste des participants */
		private ModelParticipant[] participants;
		
		/** Comparateur de configurations de participants */
		private Comparator<InfosConfigurationCouple<ModelParticipant>> comparateur;
		
		/**
		 * Constructeur
		 * @param participants liste des participants
		 * @param comparateur comparateur de configurations de participants
		 */
		public GenerationBasique(ModelParticipant[] participants, Comparator<InfosConfigurationCouple<ModelParticipant>> comparateur) {
			this.participants = participants;
			this.comparateur = comparateur;
		}
		
		/**
		 * @see Runnable#run()
		 */
		@Override
		public void run () {			
			// Informer les listeners de la génération des couples
			this.fireAvancement(0);
			this.fireMessage("Génération de tous les couples de participants possibles");
			
			// Générer les couples
			InfosConfigurationCouple<ModelParticipant>[] couples = InfosConfigurationCouple.genererCouples(participants);
			
			// Informer les listeners de la génération des couples
			this.fireAvancement(0.5);
			this.fireMessage("Trie des couples en fonction de l'affinité des participants");
			
			// Trier les couples
			Arrays.sort(couples, comparateur);
			
			// Informer les listeners de la construction de la configuration
			this.fireAvancement(0.9);
			this.fireMessage("Construction de la configuration");
			
			// Créer la configuration
			InfosConfiguration<ModelParticipant> configuration = new InfosConfiguration<ModelParticipant>(participants.length / 2);
			for (int i = couples.length - 1; !configuration.isComplet(); i--) {
				if (couples[i].isCompatible(configuration)) {
					configuration.addCouple(couples[i]);
				}
			}
			
			// Informer les listeners de la meilleure configuration
			this.fireMax(configuration);
			
			// Informer les listeners de la fin de la génération
			this.fireMessage("Configuration générée !");
			this.fireAvancement(1);
			this.fireFin();
		}
		
	}
	
}
