package org.contestorg.models;


import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

import org.contestorg.common.GenerationAbstract;
import org.contestorg.common.GenerationRunnableAbstract;
import org.contestorg.infos.Configuration;
import org.contestorg.infos.Couple;
import org.contestorg.infos.InfosModelMatchPhasesQualifs;
import org.contestorg.infos.InfosModelParticipation;
import org.contestorg.infos.InfosModelPhaseQualificative;
import org.contestorg.infos.Permutations;
import org.contestorg.interfaces.IGeneration;


/**
 * Conteneur de matchs de phases qualificatives
 */
public class ModelPhaseQualificative extends ModelAbstract
{
	
	// Attributs objets
	private ModelPoule poule;
	private ArrayList<ModelMatchPhasesQualifs> matchs = new ArrayList<ModelMatchPhasesQualifs>();
	
	// Constructeurs
	public ModelPhaseQualificative(ModelPoule poule, InfosModelPhaseQualificative infos) {
		// Retenir la poule
		this.poule = poule;
		
		// Enregistrer les informations
		this.setInfos(infos);
	}
	protected ModelPhaseQualificative(ModelPoule poule, InfosModelMatchPhasesQualifs infosMatchs, Configuration<ModelEquipe> configuration, InfosModelPhaseQualificative infos) throws ContestOrgModelException {
		// Appeller le constructeur principal
		this(poule, infos);
		
		// Ajouter les couples de la configuration
		if (configuration != null) {
			this.setConfiguration(infosMatchs, configuration);
		}
	}
	protected ModelPhaseQualificative(ModelPoule poule, ModelPhaseQualificative phaseQualificative) {
		// Appeller le constructeur principal
		this(poule, phaseQualificative.toInformation());
		
		// Récupérer l'id
		this.setId(phaseQualificative.getId());
	}
	
	// Getters
	public ModelPoule getPoule () {
		return this.poule;
	}
	public ArrayList<ModelMatchPhasesQualifs> getMatchs () {
		return new ArrayList<ModelMatchPhasesQualifs>(this.matchs);
	}
	public ArrayList<ModelEquipe> getEquipes () {
		// Initialiser la liste d'équipes
		ArrayList<ModelEquipe> equipes = new ArrayList<ModelEquipe>();
		
		// Récupérer la liste des équipes
		for (ModelMatchPhasesQualifs match : this.matchs) {
			if (match.getParticipationA() != null) {
				equipes.add(match.getParticipationA().getEquipe());
			}
			if (match.getParticipationB() != null) {
				equipes.add(match.getParticipationB().getEquipe());
			}
		}
		
		// Retourner la liste des équipes
		return equipes;
	}
	@SuppressWarnings("unchecked")
	public ArrayList<ModelEquipe> getClassement () {
		// Récupérer, trier et retourner la liste des équipes
		ArrayList<ModelEquipe> equipes = this.getEquipes();
		Collections.sort(equipes, this.poule.getCategorie().getConcours().getComparateurPhasesQualificatives(this.getNumero()));
		return equipes;
	}
	@SuppressWarnings("unchecked")
	public int getRang (ModelEquipe equipeA) {
		// Récupérer les équipes
		ArrayList<ModelEquipe> equipes = this.getEquipes();
		
		// Retourner -1 si l'équipe ne fait pas partie de la poule
		if (!equipes.contains(equipeA)) {
			return -1;
		}
		
		// Initialiser le rang
		int rang = 1;
		
		// Récupérer le comparateur des phases qualificatives
		Comparator<ModelEquipe> comparateur = this.poule.getCategorie().getConcours().getComparateurPhasesQualificatives(this.getNumero());
		
		// Comptabiliser le nombre d'équipe ayant un rang supérieur à l'équipe spécifiée
		for (ModelEquipe equipeB : equipes) {
			if (comparateur.compare(equipeA, equipeB) < 0) {
				rang++;
			}
		}
		
		// Retourner le rang
		return rang;
	}
	public int getNumero () {
		return this.poule.getPhasesQualificatives().indexOf(this);
	}
	
	// Setters
	protected void setInfos (InfosModelPhaseQualificative infos) {
		// Appeller le setInfos du parent
		super.setInfos(infos);
		
		// Fire update
		this.fireUpdate();
	}
	protected void setConfiguration (InfosModelMatchPhasesQualifs infosMatchs, Configuration<ModelEquipe> configuration) throws ContestOrgModelException {
		// Supprimer tout les matchs de la phase qualificative
		for (ModelMatchPhasesQualifs match : this.matchs) {
			match.delete(this);
		}
		this.matchs.clear();
		this.fireClear(ModelMatchPhasesQualifs.class);
		
		// Ajouter tous les couples de la configuration
		for (Couple<ModelEquipe> couple : configuration.getCouples()) {
			// Créer le match
			ModelMatchPhasesQualifs match = new ModelMatchPhasesQualifs(this, infosMatchs);
			
			// Créer la participation A et l'ajouter à l'équipe et au match
			ModelEquipe equipeA = couple.getEquipeA();
			ModelParticipation participationA = new ModelParticipation(equipeA, match, new InfosModelParticipation(InfosModelParticipation.RESULTAT_ATTENTE));
			if (equipeA != null) {
				equipeA.addParticipation(participationA);
			}
			match.setParticipationA(participationA);
			
			// Créer la participation A et l'ajouter à l'équipe
			ModelEquipe equipeB = couple.getEquipeB();
			ModelParticipation participationB = new ModelParticipation(equipeB, match, new InfosModelParticipation(InfosModelParticipation.RESULTAT_ATTENTE));
			if (equipeB != null) {
				equipeB.addParticipation(participationB);
			}
			match.setParticipationB(participationB);
			
			// Ajouter le match à la phase qualificative
			this.addMatch(match);
		}
	}
	
	// Adders
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
	
	// Clone
	protected ModelPhaseQualificative clone (ModelPoule poule) {
		return new ModelPhaseQualificative(poule, this);
	}
	
	// ToInformation
	public InfosModelPhaseQualificative toInformation () {
		InfosModelPhaseQualificative infos = new InfosModelPhaseQualificative();
		infos.setId(this.getId());
		return infos;
	}
	
	// Remove
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
	protected static IGeneration<Configuration<ModelEquipe>> genererConfigurationAvance (final ModelEquipe[] equipes, final Comparator<Configuration<ModelEquipe>> comparateur) {
		// Créer et retourner la génération
		return new GenerationAbstract<Configuration<ModelEquipe>>() {
			@Override
			public GenerationRunnableAbstract<Configuration<ModelEquipe>> getRunnable () {
				return new GenerationAvancee(equipes, comparateur);
			}
		};
	}
	protected static IGeneration<Configuration<ModelEquipe>> genererConfigurationBasique (final ModelEquipe[] equipes, final Comparator<Couple<ModelEquipe>> comparateur) {
		// Créer et retourner la génération
		return new GenerationAbstract<Configuration<ModelEquipe>>() {
			@Override
			public GenerationRunnableAbstract<Configuration<ModelEquipe>> getRunnable () {
				return new GenerationBasique(equipes, comparateur);
			}
		};
	}
	
	// Classe permettant d'effectuer une génération avancée
	private static class GenerationAvancee extends GenerationRunnableAbstract<Configuration<ModelEquipe>>
	{
		// Equipes participantes
		private ModelEquipe[] equipes;
		
		// Comparateur pour phases qualificatives
		private Comparator<Configuration<ModelEquipe>> comparateur;
		
		// Constructeur
		public GenerationAvancee(ModelEquipe[] equipes, Comparator<Configuration<ModelEquipe>> comparateur) {
			this.equipes = equipes;
			this.comparateur = comparateur;
		}
		
		// Implémentation de run
		@Override
		public void run () {
			// Initialiser la meilleure configuration
			Configuration<ModelEquipe> max = null;
			
			// Calcul de la factorielle
			double factorielle = Permutations.factorial(this.equipes.length);
			
			// Trouver la meilleure configuration
			DecimalFormat format = new DecimalFormat("0.00E00");
			String total = format.format(factorielle);
			for (double i = 0; i < factorielle; i++) {
				// Construire la configuration
				Configuration<ModelEquipe> configuration = new Configuration<ModelEquipe>((ModelEquipe[])Permutations.permutation(i, this.equipes));

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
	
	// Classe permettant d'effectuer une génération basique
	private static class GenerationBasique extends GenerationRunnableAbstract<Configuration<ModelEquipe>>
	{
		// Equipes participantes
		private ModelEquipe[] equipes;
		
		// Comparateur pour phases qualificatives
		private Comparator<Couple<ModelEquipe>> comparateur;
		
		// Constructeur
		public GenerationBasique(ModelEquipe[] equipes, Comparator<Couple<ModelEquipe>> comparateur) {
			this.equipes = equipes;
			this.comparateur = comparateur;
		}
		
		// Implémentation de run
		@Override
		public void run () {			
			// Informer les listeners de la génération des couples
			this.fireAvancement(0);
			this.fireMessage("Génération de tous les couples d'équipes possibles");
			
			// Générer les couples
			Couple<ModelEquipe>[] couples = Couple.genererCouples(equipes);
			
			// Informer les listeners de la génération des couples
			this.fireAvancement(0.5);
			this.fireMessage("Trie des couples en fonction de l'affinité des équipes");
			
			// Trier les couples
			Arrays.sort(couples, comparateur);
			
			// Informer les listeners de la construction de la configuration
			this.fireAvancement(0.9);
			this.fireMessage("Construction de la configuration");
			
			// Créer la configuration
			Configuration<ModelEquipe> configuration = new Configuration<ModelEquipe>(equipes.length / 2);
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
