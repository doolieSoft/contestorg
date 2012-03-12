package org.contestorg.models;

import java.util.ArrayList;

import org.contestorg.common.ContestOrgErrorException;
import org.contestorg.common.Pair;
import org.contestorg.common.TrackableList;
import org.contestorg.infos.InfosModelDiffusion;
import org.contestorg.infos.InfosModelTheme;
import org.contestorg.interfaces.ITrackableListValidator;
import org.contestorg.interfaces.IUpdater;

/**
 * Diffusion
 */
public class ModelDiffusion extends ModelAbstract
{
	
	// Attributs objets
	
	/** Concours */
	private ModelConcours concours;
	
	/** Thème */
	private ModelTheme theme;
	
	// Attributs scalaires
	
	/** Nom */
	private String nom;
	
	/** Port */
	private int port;
	
	// Constructeurs
	
	/**
	 * Constructeur
	 * @param concours concours
	 * @param theme thème
	 * @param infos informations de la diffusion
	 */
	public ModelDiffusion(ModelConcours concours, ModelTheme theme, InfosModelDiffusion infos) {
		// Retenir le concours et le theme
		this.concours = concours;
		this.theme = theme;
		
		// Enregistrer les informations
		this.setInfos(infos);
	}
	
	/**
	 * Constructeur par copie
	 * @param concours concours
	 * @param theme thème
	 * @param diffusion diffusion
	 */
	protected ModelDiffusion(ModelConcours concours, ModelTheme theme, ModelDiffusion diffusion) {
		// Appeller le constructeur principal
		this(concours, theme, diffusion.getInfos());
		
		// Récupérer l'id
		this.setId(diffusion.getId());
	}
	
	// Getters
	
	/**
	 * Récupérer le nom
	 * @return nom
	 */
	public String getNom() {
		return this.nom;
	}
	
	/**
	 * Récupérer le port
	 * @return port
	 */
	public int getPort () {
		return this.port;
	}
	
	/**
	 * Récupérer le thème
	 * @return thème
	 */
	public ModelTheme getTheme () {
		return this.theme;
	}
	
	/**
	 * Récupérer le concours
	 * @return concours
	 */
	public ModelConcours getConcours () {
		return this.concours;
	}
	
	// Setters
	
	/**
	 * Définir les informations de la diffusion
	 * @param infos informations de la diffusion
	 */
	protected void setInfos (InfosModelDiffusion infos) {
		// Appeller le setInfos du parent
		super.setInfos(infos);
		
		// Enregistrer les informations
		this.nom = infos.getNom();
		this.port = infos.getPort();
		
		// Fire update
		this.fireUpdate();
	}
	
	/**
	 * Définir le thème
	 * @param theme thème
	 * @throws ContestOrgErrorException
	 */
	protected void setTheme (ModelTheme theme) throws ContestOrgErrorException {
		ModelTheme before = this.theme;
		this.theme = theme;
		before.delete(this);
	}
	
	/**
	 * Cloner la diffusion
	 * @param concours concours
	 * @param theme thème
	 * @return clone de la diffusion
	 */
	protected ModelDiffusion clone (ModelConcours concours, ModelTheme theme) {
		return new ModelDiffusion(concours, theme, this);
	}
	
	/**
	 * @see ModelAbstract#getInfos()
	 */
	public InfosModelDiffusion getInfos () {
		InfosModelDiffusion infos = new InfosModelDiffusion(this.nom,this.port);
		infos.setId(this.getId());
		return infos;
	}
	
	/**
	 * @see ModelAbstract#delete(ArrayList)
	 */
	protected void delete (ArrayList<ModelAbstract> removers) throws ContestOrgErrorException {
		if (!removers.contains(this)) {
			// Ajouter la diffusion à la liste des removers
			removers.add(this);
			
			// Retirer la diffusion du concours
			if (this.concours != null) {
				if (!removers.contains(this.concours)) {
					this.concours.removeDiffusion(this);
				}
				this.concours = null;
				this.fireClear(ModelConcours.class);
			}
			
			// Fire delete
			this.fireDelete();
		}
	}
	
	/**
	 * Classe pour mettre à jour la liste des diffusions d'un concours
	 */
	protected static class UpdaterForConcours implements IUpdater<Pair<InfosModelDiffusion, InfosModelTheme>, ModelDiffusion>
	{
		/** Concours */
		private ModelConcours concours;
		
		/**
		 * Constructeur
		 * @param concours concours
		 */
		protected UpdaterForConcours(ModelConcours concours) {
			this.concours = concours;
		}
		
		/**
		 * @see IUpdater#create(Object)
		 */
		@Override
		public ModelDiffusion create (Pair<InfosModelDiffusion, InfosModelTheme> infos) {
			return new ModelDiffusion(this.concours, new ModelTheme(infos.getSecond()), infos.getFirst());
		}
		
		/**
		 * @see IUpdater#update(Object, Object)
		 */
		@Override
		public void update (ModelDiffusion diffusion, Pair<InfosModelDiffusion, InfosModelTheme> infos) {
			diffusion.setInfos(infos.getFirst());
			diffusion.getTheme().setInfos(infos.getSecond());
		}
		
	}
	
	/**
	 * Classe pour valider les opérations sur la liste des diffusions d'un concours
	 */
	protected static class ValidatorForConcours implements ITrackableListValidator<Pair<InfosModelDiffusion, InfosModelTheme>>
	{
		// Implémentation de ITrackableListValidator
		@Override
		public String validateAdd (Pair<InfosModelDiffusion, InfosModelTheme> infos, TrackableList<Pair<InfosModelDiffusion, InfosModelTheme>> list) {
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).getFirst().getNom().equals(infos.getFirst().getNom())) {
					return "Une diffusion existante possède déjà sur le même nom.";
				}
				if (list.get(i).getFirst().getPort() == infos.getFirst().getPort()) {
					return "Une diffusion existante écoute déjà sur le même port.";
				}
			}
			return null;
		}
		@Override
		public String validateUpdate (int row, Pair<InfosModelDiffusion, InfosModelTheme> infos, TrackableList<Pair<InfosModelDiffusion, InfosModelTheme>> list) {
			for (int i = 0; i < list.size(); i++) {
				if (i != row && list.get(i).getFirst().getNom().equals(infos.getFirst().getNom())) {
					return "Une diffusion existante possède déjà sur le même nom.";
				}
				if (i != row && list.get(i).getFirst().getPort() == infos.getFirst().getPort()) {
					return "Une diffusion existante écoute déjà sur le même port.";
				}
			}
			return null;
		}
		@Override
		public String validateDelete (int row, TrackableList<Pair<InfosModelDiffusion, InfosModelTheme>> list) {
			return null;
		}
		@Override
		public String validateMove (int row, int movement, TrackableList<Pair<InfosModelDiffusion, InfosModelTheme>> list) {
			return null;
		}
	}
	
}
