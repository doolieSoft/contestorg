package org.contestorg.models;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

import org.contestorg.common.GenerationAbstract;
import org.contestorg.common.GenerationRunnableAbstract;
import org.contestorg.common.Permutations;
import org.contestorg.infos.Configuration;
import org.contestorg.infos.Couple;
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
	 * @throws ContestOrgModelException
	 */
	protected ModelPhaseQualificative(ModelPoule poule, InfosModelMatchPhasesQualifs infosMatchs, Configuration<ModelParticipant> configuration, InfosModelPhaseQualificative infos) throws ContestOrgModelException {
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
		// Récupérer, trier et retourner la liste des participants
		ArrayList<ModelParticipant> participants = this.getParticipants();
		Collections.sort(participants, this.poule.getCategorie().getConcours().getComparateurPhasesQualificatives(this.getNumero()));
		return participants;
	}
	
	/**
	 * Récupérer le rang d'un participant
	 * @param participant participant
	 * @return rang du participant
	 */
	@SuppressWarnings("unchecked")
	public int getRang (ModelParticipant participant) {
		// Récupérer les participants
		ArrayList<ModelParticipant> participants = this.getParticipants();
		
		// Retourner -1 si le participant ne fait pas partie de la poule
		if (!participants.contains(participant)) {
			return -1;
		}
		
		// Initialiser le rang
		int rang = 1;
		
		// Récupérer le comparateur des phases qualificatives
		Comparator<ModelParticipant> comparateur = this.poule.getCategorie().getConcours().getComparateurPhasesQualificatives(this.getNumero());
		
		// Comptabiliser le nombre de participants ayant un rang supérieur au participant spécifiée
		for (ModelParticipant concurrent : participants) {
			if (comparateur.compare(participant, concurrent) < 0) {
				rang++;
			}
		}
		
		// Retourner le rang
		return rang;
	}
	
	/**
	 * Récupérer le numéro
	 * @return numéro
	 */
	public int getNumero () {
		return this.poule.getPhasesQualificatives().indexOf(this);
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
	 * @throws ContestOrgModelException
	 */
	protected void setConfiguration (InfosModelMatchPhasesQualifs infosMatchs, Configuration<ModelParticipant> configuration) throws ContestOrgModelException {
		// Supprimer tout les matchs de la phase qualificative
		for (ModelMatchPhasesQualifs match : this.matchs) {
			match.delete(this);
		}
		this.matchs.clear();
		this.fireClear(ModelMatchPhasesQualifs.class);
		
		// Ajouter tous les couples de la configuration
		for (Couple<ModelParticipant> couple : configuration.getCouples()) {
			// Créer le match
			ModelMatchPhasesQualifs match = new ModelMatchPhasesQualifs(this, infosMatchs);
			
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
	 * @throws ContestOrgModelException
	 */
	public void addMatch (ModelMatchPhasesQualifs match) throws ContestOrgModelException {
		if (!this.matchs.contains(match)) {
			// Ajouter le match
			this.matchs.add(match);
			
			// Fire add
			this.fireAdd(match, this.matchs.size() - 1);
		} else {
			throw new ContestOrgModelException("Le match existe déjà dans la phase qualificative");
		}
	}
	
	// Removers
	
	/**
	 * Supprimer un match
	 * @param match match
	 * @throws ContestOrgModelException
	 */
	protected void removeMatch (ModelMatchPhasesQualifs match) throws ContestOrgModelException {
		// Retirer le match
		int index;
		if ((index = this.matchs.indexOf(match)) != -1) {
			// Remove
			this.matchs.remove(match);
			
			// Fire remove
			this.fireRemove(match, index);
		} else {
			throw new ContestOrgModelException("Le match n'existe pas dans la phase qualificative");
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
	 * @see ModelAbstract#getInfos()
	 */
	public InfosModelPhaseQualificative getInfos () {
		InfosModelPhaseQualificative infos = new InfosModelPhaseQualificative();
		infos.setId(this.getId());
		return infos;
	}
	
	/**
	 * @see ModelAbstract#delete(ArrayList)
	 */
	protected void delete (ArrayList<ModelAbstract> removers) throws ContestOrgModelException {
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
	protected static IGeneration<Configuration<ModelParticipant>> genererConfigurationAvance (final ModelParticipant[] participants, final Comparator<Configuration<ModelParticipant>> comparateur) {
		// Créer et retourner la génération
		return new GenerationAbstract<Configuration<ModelParticipant>>() {
			@Override
			public GenerationRunnableAbstract<Configuration<ModelParticipant>> getRunnable () {
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
	protected static IGeneration<Configuration<ModelParticipant>> genererConfigurationBasique (final ModelParticipant[] participants, final Comparator<Couple<ModelParticipant>> comparateur) {
		// Créer et retourner la génération
		return new GenerationAbstract<Configuration<ModelParticipant>>() {
			@Override
			public GenerationRunnableAbstract<Configuration<ModelParticipant>> getRunnable () {
				return new GenerationBasique(participants, comparateur);
			}
		};
	}
	
	/**
	 * Classe permettant d'effectuer une génération en mode avancé
	 */
	private static class GenerationAvancee extends GenerationRunnableAbstract<Configuration<ModelParticipant>>
	{
		/** Liste des participants */
		private ModelParticipant[] participants;
		
		/** Comparateur de configurations de participants */
		private Comparator<Configuration<ModelParticipant>> comparateur;
		
		/**
		 * Constructeur
		 * @param participants liste des participants
		 * @param comparateur comparateur de configurations de participants
		 */
		public GenerationAvancee(ModelParticipant[] participants, Comparator<Configuration<ModelParticipant>> comparateur) {
			this.participants = participants;
			this.comparateur = comparateur;
		}
		
		/**
		 * @see Runnable#run()
		 */
		@Override
		public void run () {
			// Initialiser la meilleure configuration
			Configuration<ModelParticipant> max = null;
			
			// Calcul de la factorielle
			double factorielle = Permutations.factorial(this.participants.length);
			
			// Trouver la meilleure configuration
			DecimalFormat format = new DecimalFormat("0.00E00");
			String total = format.format(factorielle);
			for (double i = 0; i < factorielle; i++) {
				// Construire la configuration
				Configuration<ModelParticipant> configuration = new Configuration<ModelParticipant>((ModelParticipant[])Permutations.permutation(i, this.participants));

				// Comparer la configuration à la meilleure configuration trouvée jusque là
				if (max == null || this.comparateur.compare(max, configuration) < 0) {
					// Enregistrer la meilleure configuration
					max = configuration;
					
					// Prévenir les listeners d'une nouvelle configuration
					this.fireMax(configuration);
				}
				
				// Informer les listeners de l'avancement de la génération
				this.fireAvancement((double)i / factorielle);
				
				// Vérifier s'il faut arrêter ou annuler la génération
				if (this.arret || this.annuler) {
					i = factorielle;
				}
				
				// Informer les listeners de l'avancement
				if(i%100 == 0) {
					this.fireMessage("Traitement de la configuration "+format.format(i)+" sur les "+total+" configurations possibles ...");
				}
			}
			
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
	private static class GenerationBasique extends GenerationRunnableAbstract<Configuration<ModelParticipant>>
	{
		/** Liste des participants */
		private ModelParticipant[] participants;
		
		/** Comparateur de configurations de participants */
		private Comparator<Couple<ModelParticipant>> comparateur;
		
		/**
		 * Constructeur
		 * @param participants liste des participants
		 * @param comparateur comparateur de configurations de participants
		 */
		public GenerationBasique(ModelParticipant[] participants, Comparator<Couple<ModelParticipant>> comparateur) {
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
			Couple<ModelParticipant>[] couples = Couple.genererCouples(participants);
			
			// Informer les listeners de la génération des couples
			this.fireAvancement(0.5);
			this.fireMessage("Trie des couples en fonction de l'affinité des participants");
			
			// Trier les couples
			Arrays.sort(couples, comparateur);
			
			// Informer les listeners de la construction de la configuration
			this.fireAvancement(0.9);
			this.fireMessage("Construction de la configuration");
			
			// Créer la configuration
			Configuration<ModelParticipant> configuration = new Configuration<ModelParticipant>(participants.length / 2);
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
