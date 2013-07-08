package org.contestorg.models;

import java.util.ArrayList;

import org.contestorg.common.ContestOrgErrorException;
import org.contestorg.common.Pair;
import org.contestorg.common.Quintuple;
import org.contestorg.common.TrackableList;
import org.contestorg.infos.InfosModelParticipant;
import org.contestorg.infos.InfosModelParticipation;
import org.contestorg.infos.InfosModelProprietePossedee;
import org.contestorg.interfaces.ILinker;
import org.contestorg.interfaces.IUpdater;
import org.contestorg.log.Log;

/**
 * Participant
 */
public class ModelParticipant extends ModelAbstract
{
	
	// Attributs objets
	
	/** Poule */
	private ModelPoule poule;
	
	/** Liste des participations */
	private ArrayList<ModelParticipation> participations = new ArrayList<ModelParticipation>();
	
	/** Liste des prix */
	private ArrayList<ModelPrix> prix = new ArrayList<ModelPrix>();
	
	/** Liste des propriétés possédées */
	private ArrayList<ModelProprietePossedee> proprietesPossedees = new ArrayList<ModelProprietePossedee>();
	
	// Attributs scalaires
	
	/** Stand */
	private String stand;
	
	/** Nom */
	private String nom;
	
	/** Ville */
	private String ville;
	
	/** Statut */
	private InfosModelParticipant.Statut statut;
	
	/** Détails */
	private String details;
	
	// Constructeurs
	
	/**
	 * Constructeur
	 * @param poule poule
	 * @param infos informations du participant
	 */
	public ModelParticipant(ModelPoule poule, InfosModelParticipant infos) {
		// Retenir la poule
		this.poule = poule;
		
		// Enregistrer les informations
		this.setInfos(infos);
	}
	
	/**
	 * Constructeur par copie
	 * @param poule poule
	 * @param participant participant
	 */
	protected ModelParticipant(ModelPoule poule, ModelParticipant participant) {
		// Appeller le constructeur principal
		this(poule, participant.getInfos());
		
		// Récupérer l'id
		this.setId(participant.getId());
	}
	
	// Getters
	
	/**
	 * Récupérer le stand
	 * @return stand
	 */
	public String getStand () {
		return this.stand;
	}
	
	/**
	 * Récupérer le nom
	 * @return nom
	 */
	public String getNom () {
		return this.nom;
	}
	
	/**
	 * Récupérer la ville
	 * @return ville
	 */
	public String getVille () {
		return this.ville;
	}
	
	/**
	 * Récupérer le statut
	 * @return statut
	 */
	public InfosModelParticipant.Statut getStatut () {
		return this.statut;
	}
	
	/**
	 * Récupérer les détails
	 * @return détails
	 */
	public String getDetails () {
		return this.details;
	}
	
	/**
	 * Récupérer la poule
	 * @return poule
	 */
	public ModelPoule getPoule () {
		return this.poule;
	}
	
	/**
	 * Récupérer la liste des participations
	 * @return liste des participations
	 */
	public ArrayList<ModelParticipation> getParticipations () {
		return new ArrayList<ModelParticipation>(this.participations);
	}
	
	/**
	 * Récupérer la liste des prix
	 * @return liste des prix
	 */
	public ArrayList<ModelPrix> getPrix () {
		return new ArrayList<ModelPrix>(this.prix);
	}
	
	/**
	 * Récupérer la liste des propriétés possédées
	 * @return liste des propriétés possédées
	 */
	public ArrayList<ModelProprietePossedee> getProprietesPossedees () {
		return new ArrayList<ModelProprietePossedee>(this.proprietesPossedees);
	}
	
	/**
	 * Récupérer le rang aux phases qualificatives
	 * @return rang aux phases qualificatives
	 */
	public int getRangPhasesQualifs () {
		return this.poule.getRang(this);
	}
	
	/**
	 * Récupérer le rang aux phases qualificatives
	 * @param phaseQualifMax numéro de phase qualificative à ne pas dépasser (-1 pour ignorer le numéro de phase)
	 * @return rang aux phases qualificatives
	 */
	public int getRangPhasesQualifs (int phaseQualifMax) {
		return this.poule.getRang(this,phaseQualifMax);
	}
	
	/**
	 * Récupérer le rang aux phases éliminatoires
	 * @return rang aux phases éliminatoires
	 */
	public int getRangPhasesElims () {
		return this.poule.getCategorie().getPhasesEliminatoires() == null ? 1 : this.poule.getCategorie().getPhasesEliminatoires().getRang(this);
	}
	
	/**
	 * Récupérer le nombre de victoires
	 * @param phasesEliminatoires prendre en compte les matchs des phases éliminatoires ?
	 * @param phasesQualificatives prendre en compte les matchs des phases qualificatives ?
	 * @param phaseQualifMax numéro de phase qualificative à ne pas dépasser (-1 pour ignorer le numéro de phase)
	 * @return nombre de victoires
	 */
	public int getNbVictoires (boolean phasesEliminatoires, boolean phasesQualificatives, int phaseQualifMax) {
		return this.getNbParticipationsParResultat(phasesEliminatoires, phasesQualificatives, phaseQualifMax, InfosModelParticipation.RESULTAT_VICTOIRE);
	}
	
	/**
	 * Récupérer le nombre d'égalités
	 * @param phasesEliminatoires prendre en compte les matchs des phases éliminatoires ?
	 * @param phasesQualificatives prendre en compte les matchs des phases qualificatives ?
	 * @param phaseQualifMax numéro de phase qualificative à ne pas dépasser (-1 pour ignorer le numéro de phase)
	 * @return nombre d'égalités
	 */
	public int getNbEgalites (boolean phasesEliminatoires, boolean phasesQualificatives, int phaseQualifMax) {
		return this.getNbParticipationsParResultat(phasesEliminatoires, phasesQualificatives, phaseQualifMax, InfosModelParticipation.RESULTAT_EGALITE);
	}
	
	/**
	 * Récupérer le nombre de défaites
	 * @param phasesEliminatoires prendre en compte les matchs des phases éliminatoires ?
	 * @param phasesQualificatives prendre en compte les matchs des phases qualificatives ?
	 * @param phaseQualifMax numéro de phase qualificative à ne pas dépasser (-1 pour ignorer le numéro de phase)
	 * @return nombre de défaites
	 */
	public int getNbDefaites (boolean phasesEliminatoires, boolean phasesQualificatives, int phaseQualifMax) {
		return this.getNbParticipationsParResultat(phasesEliminatoires, phasesQualificatives, phaseQualifMax, InfosModelParticipation.RESULTAT_DEFAITE);
	}
	
	/**
	 * Récupérer le nombre de forfaits
	 * @param phasesEliminatoires prendre en compte les matchs des phases éliminatoires ?
	 * @param phasesQualificatives prendre en compte les matchs des phases qualificatives ?
	 * @param phaseQualifMax numéro de phase qualificative à ne pas dépasser (-1 pour ignorer le numéro de phase)
	 * @return nombre de forfaits
	 */
	public int getNbForfaits (boolean phasesEliminatoires, boolean phasesQualificatives, int phaseQualifMax) {
		return this.getNbParticipationsParResultat(phasesEliminatoires, phasesQualificatives, phaseQualifMax, InfosModelParticipation.RESULTAT_FORFAIT);
	}
	
	/**
	 * Récupérer le nombre de participations pour un résultat donné
	 * @param phasesEliminatoires prendre en compte les matchs des phases éliminatoires ?
	 * @param phasesQualificatives prendre en compte les matchs des phases qualificatives ?
	 * @param phaseQualifMax numéro de phase qualificative à ne pas dépasser (-1 pour ignorer le numéro de phase)
	 * @param resultat résultat
	 * @return nombre de participations correspondant au résultat
	 */
	public int getNbParticipationsParResultat (boolean phasesEliminatoires, boolean phasesQualificatives, int phaseQualifMax, int resultat) {
		// Initialiser le nombre de participations
		int nbParticipations = 0;
		
		// Comptabiliser les participations
		for (ModelParticipation participation : this.participations) {
			// Initialiser le booléen indiquant s'il faut prendre en compte la participation
			boolean comptabiliser = true;
			
			// Vérifier s'il faut prendre en compte la participation
			if (phasesEliminatoires && phasesQualificatives || !phasesEliminatoires && participation.getMatch() instanceof ModelMatchPhasesQualifs || !phasesQualificatives && participation.getMatch() instanceof ModelMatchPhasesElims) {
				if (participation.getMatch() instanceof ModelMatchPhasesQualifs && phaseQualifMax != -1) {
					if (((ModelMatchPhasesQualifs)participation.getMatch()).getPhaseQualificative().getNumero() > phaseQualifMax) {
						comptabiliser = false;
					}
				}
			} else {
				comptabiliser = false;
			}
			
			// Comptabiliser la participation
			if (comptabiliser && participation.getResultat() == resultat) {
				nbParticipations++;
			}
		}
		
		// Retourner le nombre de participations
		return nbParticipations;
	}
	
	/**
	 * Récupérer le nombre de victoires d'un adversaire
	 * @param adversaire adversaire
	 * @param phasesEliminatoires prendre en compte les matchs des phases éliminatoires ?
	 * @param phasesQualificatives prendre en compte les matchs des phases qualificatives ?
	 * @param phaseQualifMax numéro de phase qualificative à ne pas dépasser (-1 pour ignorer le numéro de phase)
	 * @return nombre de victoires de l'adversaire
	 */
	public int getNbVictoiresParAdversaire (ModelParticipant adversaire, boolean phasesEliminatoires, boolean phasesQualificatives, int phaseQualifMax) {
		return this.getNbParticipationsParResultatParAdversaire(adversaire, phasesEliminatoires, phasesQualificatives, phaseQualifMax, InfosModelParticipation.RESULTAT_VICTOIRE);
	}
	
	/**
	 * Récupérer le nombre d'égalités d'un adversaire
	 * @param adversaire adversaire
	 * @param phasesEliminatoires prendre en compte les matchs des phases éliminatoires ?
	 * @param phasesQualificatives prendre en compte les matchs des phases qualificatives ?
	 * @param phaseQualifMax numéro de phase qualificative à ne pas dépasser (-1 pour ignorer le numéro de phase)
	 * @return nombre d'égalités de l'adversaire
	 */
	public int getNbEgalitesParAdversaire (ModelParticipant adversaire, boolean phasesEliminatoires, boolean phasesQualificatives, int phaseQualifMax) {
		return this.getNbParticipationsParResultatParAdversaire(adversaire, phasesEliminatoires, phasesQualificatives, phaseQualifMax, InfosModelParticipation.RESULTAT_EGALITE);
	}
	
	/**
	 * Récupérer le nombre de défaites d'un adversaire
	 * @param adversaire adversaire
	 * @param phasesEliminatoires prendre en compte les matchs des phases éliminatoires ?
	 * @param phasesQualificatives prendre en compte les matchs des phases qualificatives ?
	 * @param phaseQualifMax numéro de phase qualificative à ne pas dépasser (-1 pour ignorer le numéro de phase)
	 * @return nombre de défaites de l'adversaire
	 */
	public int getNbDefaitesParAdversaire (ModelParticipant adversaire, boolean phasesEliminatoires, boolean phasesQualificatives, int phaseQualifMax) {
		return this.getNbParticipationsParResultatParAdversaire(adversaire, phasesEliminatoires, phasesQualificatives, phaseQualifMax, InfosModelParticipation.RESULTAT_DEFAITE);
	}
	
	/**
	 * Récupérer le nombre de forfaits d'un adversaire
	 * @param adversaire adversaire
	 * @param phasesEliminatoires prendre en compte les matchs des phases éliminatoires ?
	 * @param phasesQualificatives prendre en compte les matchs des phases qualificatives ?
	 * @param phaseQualifMax numéro de phase qualificative à ne pas dépasser (-1 pour ignorer le numéro de phase)
	 * @return nombre de forfaits de l'adversaire
	 */
	public int getNbForfaitsParAdversaire (ModelParticipant adversaire, boolean phasesEliminatoires, boolean phasesQualificatives, int phaseQualifMax) {
		return this.getNbParticipationsParResultatParAdversaire(adversaire, phasesEliminatoires, phasesQualificatives, phaseQualifMax, InfosModelParticipation.RESULTAT_FORFAIT);
	}
	
	/**
	 * Récupérer le nombre de participations pour un résultat donné d'un adversaire
	 * @param adversaire adversaire
	 * @param phasesEliminatoires prendre en compte les matchs des phases éliminatoires ?
	 * @param phasesQualificatives prendre en compte les matchs des phases qualificatives ?
	 * @param phaseQualifMax numéro de phase qualificative à ne pas dépasser (-1 pour ignorer le numéro de phase)
	 * @param resultat résultat
	 * @return nombre de participations correspondant au résultat de l'adversaire
	 */
	public int getNbParticipationsParResultatParAdversaire (ModelParticipant adversaire, boolean phasesEliminatoires, boolean phasesQualificatives, int phaseQualifMax, int resultat) {
		// Initialiser le nombre de participations
		int nbParticipations = 0;
		
		// Comptabiliser les participations
		for (ModelParticipation participation : this.participations) {
			// Initialiser le booléen indiquant s'il faut prendre en compte la participation
			boolean comptabiliser = true;
			
			// Vérifier s'il faut prendre en compte la participation
			if (phasesEliminatoires && phasesQualificatives || !phasesEliminatoires && participation.getMatch() instanceof ModelMatchPhasesQualifs || !phasesQualificatives && participation.getMatch() instanceof ModelMatchPhasesElims) {
				if (participation.getMatch() instanceof ModelMatchPhasesQualifs && phaseQualifMax != -1) {
					if (((ModelMatchPhasesQualifs)participation.getMatch()).getPhaseQualificative().getNumero() > phaseQualifMax) {
						comptabiliser = false;
					}
				}
			} else {
				comptabiliser = false;
			}
			ModelParticipation participationAdversaire = participation.getMatch().getAdversaire(participation.getParticipant());
			if(comptabiliser && (adversaire == null && participationAdversaire.getParticipant() != null || adversaire != null && !adversaire.equals(participationAdversaire.getParticipant()))) {
				comptabiliser = false;
			}
			
			// Comptabiliser la participation
			if (comptabiliser && participationAdversaire.getResultat() == resultat) {
				nbParticipations++;
			}
		}
		
		// Retourner le nombre de participations
		return nbParticipations;
	}
	
	/**
	 * Récupérer le nombre de victoires contre un adversaire
	 * @param adversaire adversaire
	 * @param phasesEliminatoires prendre en compte les matchs des phases éliminatoires ?
	 * @param phasesQualificatives prendre en compte les matchs des phases qualificatives ?
	 * @param phaseQualifMax numéro de phase qualificative à ne pas dépasser (-1 pour ignorer le numéro de phase)
	 * @return nombre de victoires contre l'adversaire
	 */
	public int getNbVictoiresContreAdversaire (ModelParticipant adversaire, boolean phasesEliminatoires, boolean phasesQualificatives, int phaseQualifMax) {
		return this.getNbParticipationsParResultatContreAdversaire(adversaire, phasesEliminatoires, phasesQualificatives, phaseQualifMax, InfosModelParticipation.RESULTAT_VICTOIRE);
	}
	
	/**
	 * Récupérer le nombre d'égalités contre un adversaire
	 * @param adversaire adversaire
	 * @param phasesEliminatoires prendre en compte les matchs des phases éliminatoires ?
	 * @param phasesQualificatives prendre en compte les matchs des phases qualificatives ?
	 * @param phaseQualifMax numéro de phase qualificative à ne pas dépasser (-1 pour ignorer le numéro de phase)
	 * @return nombre d'égalités contre l'adversaire
	 */
	public int getNbEgalitesContreAdversaire (ModelParticipant adversaire, boolean phasesEliminatoires, boolean phasesQualificatives, int phaseQualifMax) {
		return this.getNbParticipationsParResultatContreAdversaire(adversaire, phasesEliminatoires, phasesQualificatives, phaseQualifMax, InfosModelParticipation.RESULTAT_EGALITE);
	}
	
	/**
	 * Récupérer le nombre de défaites contre un adversaire
	 * @param adversaire adversaire
	 * @param phasesEliminatoires prendre en compte les matchs des phases éliminatoires ?
	 * @param phasesQualificatives prendre en compte les matchs des phases qualificatives ?
	 * @param phaseQualifMax numéro de phase qualificative à ne pas dépasser (-1 pour ignorer le numéro de phase)
	 * @return nombre de défaites contre l'adversaire
	 */
	public int getNbDefaitesContreAdversaire (ModelParticipant adversaire, boolean phasesEliminatoires, boolean phasesQualificatives, int phaseQualifMax) {
		return this.getNbParticipationsParResultatContreAdversaire(adversaire, phasesEliminatoires, phasesQualificatives, phaseQualifMax, InfosModelParticipation.RESULTAT_DEFAITE);
	}
	
	/**
	 * Récupérer le nombre de forfaits contre un adversaire
	 * @param adversaire adversaire
	 * @param phasesEliminatoires prendre en compte les matchs des phases éliminatoires ?
	 * @param phasesQualificatives prendre en compte les matchs des phases qualificatives ?
	 * @param phaseQualifMax numéro de phase qualificative à ne pas dépasser (-1 pour ignorer le numéro de phase)
	 * @return nombre de forfaits contre l'adversaire
	 */
	public int getNbForfaitsContreAdversaire (ModelParticipant adversaire, boolean phasesEliminatoires, boolean phasesQualificatives, int phaseQualifMax) {
		return this.getNbParticipationsParResultatContreAdversaire(adversaire, phasesEliminatoires, phasesQualificatives, phaseQualifMax, InfosModelParticipation.RESULTAT_FORFAIT);
	}
	
	/**
	 * Récupérer le nombre de participations pour un résultat donné contre un adversaire
	 * @param adversaire adversaire
	 * @param phasesEliminatoires prendre en compte les matchs des phases éliminatoires ?
	 * @param phasesQualificatives prendre en compte les matchs des phases qualificatives ?
	 * @param phaseQualifMax numéro de phase qualificative à ne pas dépasser (-1 pour ignorer le numéro de phase)
	 * @param resultat résultat
	 * @return nombre de participations correspondant au résultat contre l'adversaire
	 */
	public int getNbParticipationsParResultatContreAdversaire (ModelParticipant adversaire, boolean phasesEliminatoires, boolean phasesQualificatives, int phaseQualifMax, int resultat) {
		// Initialiser le nombre de participations
		int nbParticipations = 0;
		
		// Comptabiliser les participations
		for (ModelParticipation participation : this.participations) {
			// Initialiser le booléen indiquant s'il faut prendre en compte la participation
			boolean comptabiliser = true;
			
			// Vérifier s'il faut prendre en compte la participation
			if (phasesEliminatoires && phasesQualificatives || !phasesEliminatoires && participation.getMatch() instanceof ModelMatchPhasesQualifs || !phasesQualificatives && participation.getMatch() instanceof ModelMatchPhasesElims) {
				if (participation.getMatch() instanceof ModelMatchPhasesQualifs && phaseQualifMax != -1) {
					if (((ModelMatchPhasesQualifs)participation.getMatch()).getPhaseQualificative().getNumero() > phaseQualifMax) {
						comptabiliser = false;
					}
				}
			} else {
				comptabiliser = false;
			}
			ModelParticipation participationAdversaire = participation.getMatch().getAdversaire(participation.getParticipant());
			if(comptabiliser && (adversaire == null && participationAdversaire.getParticipant() != null || adversaire != null && !adversaire.equals(participationAdversaire.getParticipant()))) {
				comptabiliser = false;
			}
			
			// Comptabiliser la participation
			if (comptabiliser && participation.getResultat() == resultat) {
				nbParticipations++;
			}
		}
		
		// Retourner le nombre de participations
		return nbParticipations;
	}
	
	/**
	 * Récupérer la quantité d'un objectif remportée
	 * @param objectif objectif
	 * @param phasesEliminatoires prendre en compte les matchs des phases éliminatoires ?
	 * @param phasesQualificatives prendre en compte les matchs des phases qualificatives ?
	 * @param phaseQualifMax numéro de phase qualificative à ne pas dépasser (-1 pour ignorer le numéro de phase)
	 * @return quantité d'un objectif remportée
	 */
	public int getQuantiteObjectifRemportee (ModelObjectif objectif, boolean phasesEliminatoires, boolean phasesQualificatives, int phaseQualifMax) {
		// Initialiser la quantité
		int quantite = 0;
		
		// Comptabiliser les quantités
		for (ModelParticipation participation : this.getParticipations()) {
			// Initialiser le booléen indiquant s'il faut prendre en compte la participation
			boolean comptabiliser = true;
			
			// Vérifier s'il faut prendre en compte la participation
			if (phasesEliminatoires && phasesQualificatives || !phasesEliminatoires && participation.getMatch() instanceof ModelMatchPhasesQualifs || !phasesQualificatives && participation.getMatch() instanceof ModelMatchPhasesElims) {
				if (participation.getMatch() instanceof ModelMatchPhasesQualifs && phaseQualifMax != -1) {
					if (((ModelMatchPhasesQualifs)participation.getMatch()).getPhaseQualificative().getNumero() > phaseQualifMax) {
						comptabiliser = false;
					}
				}
			} else {
				comptabiliser = false;
			}
			
			// Comptabiliser le nombre d'objectifs remportés
			if (comptabiliser) {
				ModelObjectifRemporte objectifRemporte = participation.getObjectifRemporte(objectif);
				if (objectifRemporte != null) {
					quantite += objectifRemporte.getQuantite();
				}
			}
		}
		
		// Retourner la quantité
		return quantite;
	}
	
	/**
	 * Récupérer la quantité d'un objectif remportée par les adversaires
	 * @param objectif objectif
	 * @param phasesEliminatoires prendre en compte les matchs des phases éliminatoires ?
	 * @param phasesQualificatives prendre en compte les matchs des phases qualificatives ?
	 * @param phaseQualifMax numéro de phase qualificative à ne pas dépasser (-1 pour ignorer le numéro de phase)
	 * @return quantité de l'objectif remportée par les adversaires
	 */
	public int getQuantiteObjectifRemporteeAdversaires (ModelObjectif objectif, boolean phasesEliminatoires, boolean phasesQualificatives, int phaseQualifMax) {
		// Initialiser la quantité
		int quantite = 0;
		
		// Comptabiliser les quantités
		for (ModelParticipation participation : this.getParticipations()) {
			// Initialiser le booléen indiquant s'il faut prendre en compte la participation
			boolean comptabiliser = true;
			
			// Vérifier s'il faut prendre en compte la participation
			if (phasesEliminatoires && phasesQualificatives || !phasesEliminatoires && participation.getMatch() instanceof ModelMatchPhasesQualifs || !phasesQualificatives && participation.getMatch() instanceof ModelMatchPhasesElims) {
				if (participation.getMatch() instanceof ModelMatchPhasesQualifs && phaseQualifMax != -1) {
					if (((ModelMatchPhasesQualifs)participation.getMatch()).getPhaseQualificative().getNumero() > phaseQualifMax) {
						comptabiliser = false;
					}
				}
			} else {
				comptabiliser = false;
			}
			
			// Comptabiliser le nombre d'objectifs remportés de l'adversaire
			if (comptabiliser) {
				ModelObjectifRemporte objectifRemporte = participation.getMatch().getAdversaire(participation.getParticipant()).getObjectifRemporte(objectif);
				if (objectifRemporte != null) {
					quantite += objectifRemporte.getQuantite();
				}
			}
		}
		
		// Retourner la quantité
		return quantite;
	}
	
	/**
	 * Récupérer la quantité d'un objectif remportée contre un adversaire
	 * @param adversaire adversaire
	 * @param objectif objectif
	 * @param phasesEliminatoires prendre en compte les matchs des phases éliminatoires ?
	 * @param phasesQualificatives prendre en compte les matchs des phases qualificatives ?
	 * @param phaseQualifMax numéro de phase qualificative à ne pas dépasser (-1 pour ignorer le numéro de phase)
	 * @return quantité de l'objectif remportée contre un adversaire
	 */
	public int getQuantiteObjectifRemporteeContreAdversaire (ModelParticipant adversaire, ModelObjectif objectif, boolean phasesEliminatoires, boolean phasesQualificatives, int phaseQualifMax) {
		// Initialiser la quantité
		int quantite = 0;
		
		// Comptabiliser les quantités
		for (ModelParticipation participation : this.getParticipations()) {
			// Initialiser le booléen indiquant s'il faut prendre en compte la participation
			boolean comptabiliser = true;
			
			// Vérifier s'il faut prendre en compte la participation
			if (phasesEliminatoires && phasesQualificatives || !phasesEliminatoires && participation.getMatch() instanceof ModelMatchPhasesQualifs || !phasesQualificatives && participation.getMatch() instanceof ModelMatchPhasesElims) {
				if (participation.getMatch() instanceof ModelMatchPhasesQualifs && phaseQualifMax != -1) {
					if (((ModelMatchPhasesQualifs)participation.getMatch()).getPhaseQualificative().getNumero() > phaseQualifMax) {
						comptabiliser = false;
					}
				}
			} else {
				comptabiliser = false;
			}
			ModelParticipation participationAdversaire = participation.getMatch().getAdversaire(participation.getParticipant());
			if(comptabiliser && (adversaire == null && participationAdversaire.getParticipant() != null || adversaire != null && !adversaire.equals(participationAdversaire.getParticipant()))) {
				comptabiliser = false;
			}
			
			// Comptabiliser le nombre d'objectifs remportés de l'adversaire
			if (comptabiliser) {
				ModelObjectifRemporte objectifRemporte = participation.getObjectifRemporte(objectif);
				if (objectifRemporte != null) {
					quantite += objectifRemporte.getQuantite();
				}
			}
		}
		
		// Retourner la quantité
		return quantite;
	}
	
	/**
	 * Récupérer la quantité d'un objectif remportée par un adversaire
	 * @param adversaire adversaire
	 * @param objectif objectif
	 * @param phasesEliminatoires prendre en compte les matchs des phases éliminatoires ?
	 * @param phasesQualificatives prendre en compte les matchs des phases qualificatives ?
	 * @param phaseQualifMax numéro de phase qualificative à ne pas dépasser (-1 pour ignorer le numéro de phase)
	 * @return quantité de l'objectif remportée par un adversaire
	 */
	public int getQuantiteObjectifRemporteeParAdversaire (ModelParticipant adversaire, ModelObjectif objectif, boolean phasesEliminatoires, boolean phasesQualificatives, int phaseQualifMax) {
		// Initialiser la quantité
		int quantite = 0;
		
		// Comptabiliser les quantités
		for (ModelParticipation participation : this.getParticipations()) {
			// Initialiser le booléen indiquant s'il faut prendre en compte la participation
			boolean comptabiliser = true;
			
			// Vérifier s'il faut prendre en compte la participation
			if (phasesEliminatoires && phasesQualificatives || !phasesEliminatoires && participation.getMatch() instanceof ModelMatchPhasesQualifs || !phasesQualificatives && participation.getMatch() instanceof ModelMatchPhasesElims) {
				if (participation.getMatch() instanceof ModelMatchPhasesQualifs && phaseQualifMax != -1) {
					if (((ModelMatchPhasesQualifs)participation.getMatch()).getPhaseQualificative().getNumero() > phaseQualifMax) {
						comptabiliser = false;
					}
				}
			} else {
				comptabiliser = false;
			}
			ModelParticipation participationAdversaire = participation.getMatch().getAdversaire(participation.getParticipant());
			if(comptabiliser && (adversaire == null && participationAdversaire.getParticipant() != null || adversaire != null && !adversaire.equals(participationAdversaire.getParticipant()))) {
				comptabiliser = false;
			}
			
			// Comptabiliser le nombre d'objectifs remportés de l'adversaire
			if (comptabiliser) {
				ModelObjectifRemporte objectifRemporte = participationAdversaire.getObjectifRemporte(objectif);
				if (objectifRemporte != null) {
					quantite += objectifRemporte.getQuantite();
				}
			}
		}
		
		// Retourner la quantité
		return quantite;
	}
	
	/**
	 * Récupérer le nombre de points remportés
	 * @param phasesEliminatoires prendre en compte les matchs des phases éliminatoires ?
	 * @param phasesQualificatives prendre en compte les matchs des phases qualificatives ?
	 * @param phaseQualifMax numéro de phase qualificative à ne pas dépasser (-1 pour ignorer le numéro de phase)
	 * @return nombre de points remportés
	 */
	public double getPoints (boolean phasesEliminatoires, boolean phasesQualificatives, int phaseQualifMax) {
		// Initialiser le compteur de points
		double points = 0;
		
		// Comptabiliser les points
		for (ModelParticipation participation : this.participations) {
			// Initialiser le booléen indiquant s'il faut prendre en compte la participation
			boolean comptabiliser = true;
			
			// Vérifier s'il faut prendre en compte la participation
			if (phasesEliminatoires && phasesQualificatives || !phasesEliminatoires && participation.getMatch() instanceof ModelMatchPhasesQualifs || !phasesQualificatives && participation.getMatch() instanceof ModelMatchPhasesElims) {
				if (participation.getMatch() instanceof ModelMatchPhasesQualifs && phaseQualifMax != -1) {
					if (((ModelMatchPhasesQualifs)participation.getMatch()).getPhaseQualificative().getNumero() > phaseQualifMax) {
						comptabiliser = false;
					}
				}
			} else {
				comptabiliser = false;
			}
			
			// Comptabiliser le nombre de points
			if (comptabiliser) {
				points += participation.getPoints();
			}
		}
		
		// Retourner le nombre de points
		return points;
	}
	
	/**
	 * Récupérer le nombre de points remportés par les adversaires
	 * @param phasesEliminatoires prendre en compte les matchs des phases éliminatoires ?
	 * @param phasesQualificatives prendre en compte les matchs des phases qualificatives ?
	 * @param phaseQualifMax numéro de phase qualificative à ne pas dépasser (-1 pour ignorer le numéro de phase)
	 * @return nombre de points remportés par les adversaires
	 */
	public double getPointsAdversaires (boolean phasesEliminatoires, boolean phasesQualificatives, int phaseQualifMax) {
		// Initialiser le compteur de points
		double points = 0;
		
		// Comptabiliser les points
		for (ModelParticipation participation : this.participations) {
			// Initialiser le booléen indiquant s'il faut prendre en compte la participation
			boolean comptabiliser = true;
			
			// Vérifier s'il faut prendre en compte la participation
			if (phasesEliminatoires && phasesQualificatives || !phasesEliminatoires && participation.getMatch() instanceof ModelMatchPhasesQualifs || !phasesQualificatives && participation.getMatch() instanceof ModelMatchPhasesElims) {
				if (participation.getMatch() instanceof ModelMatchPhasesQualifs && phaseQualifMax != -1) {
					if (((ModelMatchPhasesQualifs)participation.getMatch()).getPhaseQualificative().getNumero() > phaseQualifMax) {
						comptabiliser = false;
					}
				}
			} else {
				comptabiliser = false;
			}
			
			// Comptabiliser le nombre de points
			if (comptabiliser) {
				points += participation.getMatch().getAdversaire(participation.getParticipant()).getPoints();
			}
		}
		
		// Retourner le nombre de points
		return points;
	}
	
	/**
	 * Récupérer le nombre de points remportés contre un adversaire
	 * @param adversaire adversaire
	 * @param phasesEliminatoires prendre en compte les matchs des phases éliminatoires ?
	 * @param phasesQualificatives prendre en compte les matchs des phases qualificatives ?
	 * @param phaseQualifMax numéro de phase qualificative à ne pas dépasser (-1 pour ignorer le numéro de phase)
	 * @return nombre de points remportés contre l'adversaire
	 */
	public double getPointsContreAdversaire (ModelParticipant adversaire, boolean phasesEliminatoires, boolean phasesQualificatives, int phaseQualifMax) {
		// Initialiser le compteur de points
		double points = 0;
		
		// Comptabiliser les points
		for (ModelParticipation participation : this.participations) {
			// Initialiser le booléen indiquant s'il faut prendre en compte la participation
			boolean comptabiliser = true;
			
			// Vérifier s'il faut prendre en compte la participation
			if (phasesEliminatoires && phasesQualificatives || !phasesEliminatoires && participation.getMatch() instanceof ModelMatchPhasesQualifs || !phasesQualificatives && participation.getMatch() instanceof ModelMatchPhasesElims) {
				if (participation.getMatch() instanceof ModelMatchPhasesQualifs && phaseQualifMax != -1) {
					if (((ModelMatchPhasesQualifs)participation.getMatch()).getPhaseQualificative().getNumero() > phaseQualifMax) {
						comptabiliser = false;
					}
				}
			} else {
				comptabiliser = false;
			}
			ModelParticipation participationAdversaire = participation.getMatch().getAdversaire(participation.getParticipant());
			if(comptabiliser && (adversaire == null && participationAdversaire.getParticipant() != null || adversaire != null && !adversaire.equals(participationAdversaire.getParticipant()))) {
				comptabiliser = false;
			}
			
			// Comptabiliser le nombre de points
			if (comptabiliser) {
				points += participation.getPoints();
			}
		}
		
		// Retourner le nombre de points
		return points;
	}
	
	/**
	 * Récupérer le nombre de points remportés par un adversaire
	 * @param adversaire adversaire
	 * @param phasesEliminatoires prendre en compte les matchs des phases éliminatoires ?
	 * @param phasesQualificatives prendre en compte les matchs des phases qualificatives ?
	 * @param phaseQualifMax numéro de phase qualificative à ne pas dépasser (-1 pour ignorer le numéro de phase)
	 * @return nombre de points remportés par l'adversaire
	 */
	public double getPointsParAdversaire (ModelParticipant adversaire, boolean phasesEliminatoires, boolean phasesQualificatives, int phaseQualifMax) {
		// Initialiser le compteur de points
		double points = 0;
		
		// Comptabiliser les points
		for (ModelParticipation participation : this.participations) {
			// Initialiser le booléen indiquant s'il faut prendre en compte la participation
			boolean comptabiliser = true;
			
			// Vérifier s'il faut prendre en compte la participation
			if (phasesEliminatoires && phasesQualificatives || !phasesEliminatoires && participation.getMatch() instanceof ModelMatchPhasesQualifs || !phasesQualificatives && participation.getMatch() instanceof ModelMatchPhasesElims) {
				if (participation.getMatch() instanceof ModelMatchPhasesQualifs && phaseQualifMax != -1) {
					if (((ModelMatchPhasesQualifs)participation.getMatch()).getPhaseQualificative().getNumero() > phaseQualifMax) {
						comptabiliser = false;
					}
				}
			} else {
				comptabiliser = false;
			}
			ModelParticipation participationAdversaire = participation.getMatch().getAdversaire(participation.getParticipant());
			if(comptabiliser && (adversaire == null && participationAdversaire.getParticipant() != null || adversaire != null && !adversaire.equals(participationAdversaire.getParticipant()))) {
				comptabiliser = false;
			}
			
			// Comptabiliser le nombre de points
			if (comptabiliser) {
				points += participationAdversaire.getPoints();
			}
		}
		
		// Retourner le nombre de points
		return points;
	}
	
	/**
	 * Récupérer le nombre de rencontres avec un participant
	 * @param participant participant
	 * @param phasesEliminatoires prendre en compte les matchs des phases éliminatoires ?
	 * @param phasesQualificatives prendre en compte les matchs des phases qualificatives ?
	 * @return nombre de rencontres avec le participant
	 */
	public int getNbRencontres (ModelParticipant participant, boolean phasesEliminatoires, boolean phasesQualificatives) {
		// Initialiser le nombre de rencontres
		int nbRencontres = 0;
		
		// Comptabiliser le nombre de rencontres
		for (ModelParticipation participation : this.participations) {
			if (phasesEliminatoires && phasesQualificatives || !phasesEliminatoires && participation.getMatch() instanceof ModelMatchPhasesQualifs || !phasesQualificatives && participation.getMatch() instanceof ModelMatchPhasesElims) {
				// Récupérer le match
				ModelMatchAbstract match = participation.getMatch();
				
				// Récupérer le participant adverse
				ModelParticipant adversaire = participation.equals(match.getParticipationA()) ? match.getParticipationB().getParticipant() : match.getParticipationA().getParticipant();
				
				// Vérifier si le participant passé en paramètre est le participant adverse
				if (participant == null && adversaire == null || participant != null && participant.equals(adversaire)) {
					// Incrémenter le nombre de rencontres
					nbRencontres++;
				}
			}
		}
		
		// Retourner le nombre de rencontres
		return nbRencontres;
	}
	
	/**
	 * @see ModelAbstract#getInfos()
	 */
	public InfosModelParticipant getInfos () {
		InfosModelParticipant infos = new InfosModelParticipant(this.stand, this.nom, this.ville, this.statut, this.details);
		infos.setId(this.getId());
		return infos;
	}
	
	// Setters
	
	/**
	 * Définir les informations du participant
	 * @param infos informations du participant
	 */
	protected void setInfos (InfosModelParticipant infos) {
		// Appeller le setInfos du parent
		super.setInfos(infos);
		
		// Enregistrer les informations
		this.stand = infos.getStand();
		this.nom = infos.getNom();
		this.ville = infos.getVille();
		this.statut = infos.getStatut();
		this.details = infos.getDetails();
		
		// Fire update
		this.fireUpdate();
	}
	
	/**
	 * Définir le statut
	 * @param statut statut
	 */
	protected void setStatut (InfosModelParticipant.Statut statut) {
		// Enregistrer le statut
		this.statut = statut;
		
		// Fire update
		this.fireUpdate();
	}
	
	/**
	 * Définir la poule
	 * @param poule poule
	 * @throws ContestOrgErrorException
	 */
	protected void setPoule (ModelPoule poule) throws ContestOrgErrorException {
		// Modifier la poule
		this.poule.removeParticipant(this);
		this.poule = poule;
		this.poule.addParticipant(this);
		
		// Fire update
		this.fireUpdate();
	}
	
	// Adders
	
	/**
	 * Ajouter une participation
	 * @param participation participation
	 * @throws ContestOrgErrorException
	 */
	public void addParticipation (ModelParticipation participation) throws ContestOrgErrorException {
		if (!this.participations.contains(participation)) {
			// Ajouter la participation
			this.participations.add(participation);
			
			// Fire add
			this.fireAdd(participation, this.participations.size() - 1);
		} else {
			throw new ContestOrgErrorException("La participation existe déjà dans le participant");
		}
	}
	
	/**
	 * Ajouter un prix
	 * @param prix prix
	 * @throws ContestOrgErrorException
	 */
	public void addPrix (ModelPrix prix) throws ContestOrgErrorException {
		if (!this.prix.contains(prix)) {
			// Ajouter le prix
			this.prix.add(prix);
			
			// Fire add
			this.fireAdd(prix, this.prix.size() - 1);
		} else {
			throw new ContestOrgErrorException("La participation existe déjà dans le participant");
		}
	}
	
	/**
	 * Ajouter une propriété possédée
	 * @param proprietePossedee propriété possédée
	 * @throws ContestOrgErrorException
	 */
	public void addProprietePossedee (ModelProprietePossedee proprietePossedee) throws ContestOrgErrorException {
		if (!this.proprietesPossedees.contains(proprietePossedee)) {
			// Ajouter la propriété possédée
			this.proprietesPossedees.add(proprietePossedee);
			
			// Fire add
			this.fireAdd(proprietePossedee, this.proprietesPossedees.size() - 1);
		} else {
			throw new ContestOrgErrorException("La participation existe déjà dans le participant");
		}
	}
	
	// Removers
	
	/**
	 * Supprimer une participation
	 * @param participation participation
	 * @throws ContestOrgErrorException
	 */
	protected void removeParticipation (ModelParticipation participation) throws ContestOrgErrorException {
		// Retirer la participation
		int index;
		if ((index = this.participations.indexOf(participation)) != -1) {
			// Remove
			this.participations.remove(participation);
			
			// Fire remove
			this.fireRemove(participation, index);
		} else {
			throw new ContestOrgErrorException("La participation n'existe pas dans le participant");
		}
	}
	
	/**
	 * Supprimer un prix
	 * @param prix prix
	 * @throws ContestOrgErrorException
	 */
	protected void removePrix (ModelPrix prix) throws ContestOrgErrorException {
		// Retirer le prix
		int index;
		if ((index = this.prix.indexOf(prix)) != -1) {
			// Remove
			this.prix.remove(prix);
			
			// Fire remove
			this.fireRemove(prix, index);
		} else {
			throw new ContestOrgErrorException("La participation n'existe pas dans le participant");
		}
	}
	
	/**
	 * Supprimer une propriété possédée
	 * @param proprietePossedee propriété possédée
	 * @throws ContestOrgErrorException
	 */
	protected void removeProprietePossedee (ModelProprietePossedee proprietePossedee) throws ContestOrgErrorException {
		// Retirer la propriété possédée
		int index;
		if ((index = this.proprietesPossedees.indexOf(proprietePossedee)) != -1) {
			// Remove
			this.proprietesPossedees.remove(proprietePossedee);
			
			// Fire remove
			this.fireRemove(proprietePossedee, index);
		} else {
			throw new ContestOrgErrorException("La participation n'existe pas dans le paricipant");
		}
	}
	
	// Updaters
	
	/**
	 * Mettre à jour la liste des prix
	 * @param list liste des prix source
	 * @throws ContestOrgErrorException
	 */
	protected void updatePrix (TrackableList<String> list) throws ContestOrgErrorException {
		// Mettre à jour les prix remportés
		this.links(new ModelParticipant.LinkerForPrix(this), list);
	}
	
	/**
	 * Mettre à jour la liste des propriétés possédées
	 * @param list liste des propriétés possédées source
	 * @throws ContestOrgErrorException
	 */
	protected void updateProprietesPossedees (TrackableList<Pair<String, InfosModelProprietePossedee>> list) throws ContestOrgErrorException {
		// Mettre à jour les propriétés de participant
		this.updates(new ModelProprietePossedee.UpdaterForParticipant(this), this.proprietesPossedees, list, true, null);
	}
	
	/**
	 * Cloner le participant
	 * @param poule poule
	 * @return clone du participant
	 */
	protected ModelParticipant clone (ModelPoule poule) {
		return new ModelParticipant(poule, this);
	}
	
	/**
	 * @see ModelAbstract#delete(ArrayList)
	 */
	protected void delete (ArrayList<ModelAbstract> removers) throws ContestOrgErrorException {
		if (!removers.contains(this)) {
			// Ajouter le participant à la liste des removers
			removers.add(this);
			
			// "Supprimer" les participations du participant
			for (ModelParticipation participation : this.participations) {
				if (!removers.contains(participation)) {
					participation.setParticipant(null);
				}
			}
			this.participations.clear();
			this.fireClear(ModelParticipation.class);
			
			// Retirer le participant des prix
			for (ModelPrix prix : this.prix) {
				if (!removers.contains(prix)) {
					prix.removeParticipant(this);
				}
			}
			this.prix.clear();
			this.fireClear(ModelPrix.class);
			
			// Retirer le participant de la poule
			if (this.poule != null) {
				if (!removers.contains(this.poule)) {
					this.poule.removeParticipant(this);
				}
				this.poule = null;
				this.fireClear(ModelPoule.class);
			}
			
			// Fire delete
			this.fireDelete();
		}
	}
	
	/**
	 * Classe pour mettre à jour une liste de participants indépendament de son conteneur
	 */
	protected static class Updater implements IUpdater<Quintuple<String, String, InfosModelParticipant, TrackableList<Pair<String, InfosModelProprietePossedee>>, TrackableList<String>>, ModelParticipant>
	{
		/** Concours */
		private ModelConcours concours;
		
		/**
		 * Constructeur
		 * @param concours concours
		 */
		public Updater(ModelConcours concours) {
			// Retenir le concours
			this.concours = concours;
		}
		
		/**
		 * @see IUpdater#create(Object)
		 */
		@Override
		public ModelParticipant create (Quintuple<String, String, InfosModelParticipant, TrackableList<Pair<String, InfosModelProprietePossedee>>, TrackableList<String>> infos) {
			try {
				// Récupérer la poule
				ModelPoule poule = this.concours.getCategorieByNom(infos.getFirst()).getPouleByNom(infos.getSecond());
				
				// Créer, configurer et retourner le participant
				ModelParticipant participant = new ModelParticipant(poule, infos.getThird());
				poule.addParticipant(participant);
				participant.updateProprietesPossedees(infos.getFourth());
				participant.updatePrix(infos.getFifth());
				return participant;
			} catch (ContestOrgErrorException e) {
				Log.getLogger().fatal("Erreur lors de la création d'un participant.",e);
				return null;
			}
		}
		
		/**
		 * @see IUpdater#update(Object, Object)
		 */
		@Override
		public ModelParticipant update (ModelParticipant participant, Quintuple<String, String, InfosModelParticipant, TrackableList<Pair<String, InfosModelProprietePossedee>>, TrackableList<String>> infos) {
			try {
				// Récupérer la poule
				ModelPoule poule = this.concours.getCategorieByNom(infos.getFirst()).getPouleByNom(infos.getSecond());
				
				// Configurer le participant
				if (!participant.getPoule().equals(poule)) {
					participant.setPoule(poule);
				}
				participant.setInfos(infos.getThird());
				participant.updateProprietesPossedees(infos.getFourth());
				participant.updatePrix(infos.getFifth());
			} catch (ContestOrgErrorException e) {
				Log.getLogger().fatal(e.getMessage());
			}
			return null;
		}
	}
	
	/**
	 * Classe pour lier une liste de prix à un participant
	 */
	protected static class LinkerForPrix implements ILinker<String>
	{
		/** Participant */
		private ModelParticipant participant;
		
		/**
		 * Constructeur
		 * @param participant participant
		 */
		public LinkerForPrix(ModelParticipant participant) {
			this.participant = participant;
		}
		
		/**
		 * @see ILinker#link(Object)
		 */
		@Override
		public void link (String nomPrix) {
			// Récupérer le prix
			ModelPrix prix = this.participant.getPoule().getCategorie().getConcours().getPrixByNom(nomPrix);
			
			// Créer le lien entre le prix et le participant
			if (prix.getParticipants().indexOf(this.participant) == -1 && this.participant.getPrix().indexOf(prix) == -1) {
				try {
					prix.addParticipant(this.participant);
					this.participant.addPrix(prix);
				} catch (ContestOrgErrorException e) {
					Log.getLogger().fatal("Erreur lors de la création du lien entre un prix et un participant.");
				}
			}
		}
		
		/**
		 * @see ILinker#unlink(Object)
		 */
		@Override
		public void unlink (String nomPrix) {
			// Récupérer le prix
			ModelPrix prix = this.participant.getPoule().getCategorie().getConcours().getPrixByNom(nomPrix);
			
			// Supprimer le lien entre le prix et le participant
			if (prix.getParticipants().indexOf(this.participant) != -1 && this.participant.getPrix().indexOf(prix) != -1) {
				try {
					prix.removeParticipant(this.participant);
					this.participant.removePrix(prix);
				} catch (ContestOrgErrorException e) {
					Log.getLogger().fatal("Erreur lors de la suppression du lien entre un prix et un participant.");
				}
			}
		}
	}
	
}
