package org.contestorg.models;


import java.util.ArrayList;

import org.contestorg.common.Pair;
import org.contestorg.common.TrackableList;
import org.contestorg.infos.InfosModelDiffusion;
import org.contestorg.infos.InfosModelTheme;
import org.contestorg.interfaces.IListValidator;
import org.contestorg.interfaces.IUpdater;


public class ModelDiffusion extends ModelAbstract
{
	
	// Attributs objets
	private ModelConcours concours;
	private ModelTheme theme;
	
	// Attributs scalaires
	private String nom;
	private int port;
	
	// Constructeur
	public ModelDiffusion(ModelConcours concours, ModelTheme theme, InfosModelDiffusion infos) {
		// Retenir le concours et le theme
		this.concours = concours;
		this.theme = theme;
		
		// Enregistrer les informations
		this.setInfos(infos);
	}
	protected ModelDiffusion(ModelConcours concours, ModelTheme theme, ModelDiffusion diffusion) {
		// Appeller le constructeur principal
		this(concours, theme, diffusion.toInformation());
		
		// Récupérer l'id
		this.setId(diffusion.getId());
	}
	
	// Getters
	public String getNom() {
		return this.nom;
	}
	public int getPort () {
		return this.port;
	}
	public ModelTheme getTheme () {
		return this.theme;
	}
	public ModelConcours getConcours () {
		return this.concours;
	}
	
	// Setters
	protected void setInfos (InfosModelDiffusion infos) {
		// Appeller le setInfos du parent
		super.setInfos(infos);
		
		// Enregistrer les informations
		this.nom = infos.getNom();
		this.port = infos.getPort();
		
		// Fire update
		this.fireUpdate();
	}
	protected void setTheme (ModelTheme after) throws ContestOrgModelException {
		ModelTheme before = this.theme;
		this.theme = after;
		before.delete(this);
	}
	
	// Clone
	protected ModelDiffusion clone (ModelConcours concours, ModelTheme theme) {
		return new ModelDiffusion(concours, theme, this);
	}
	
	// ToInformation
	public InfosModelDiffusion toInformation () {
		InfosModelDiffusion infos = new InfosModelDiffusion(this.nom,this.port);
		infos.setId(this.getId());
		return infos;
	}
	
	// Remove
	protected void delete (ArrayList<ModelAbstract> removers) throws ContestOrgModelException {
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
	
	// Classe pour mettre à jour la liste des diffusions d'un concours
	protected static class UpdaterForConcours implements IUpdater<Pair<InfosModelDiffusion, InfosModelTheme>, ModelDiffusion>
	{
		// Concours
		private ModelConcours concours;
		
		// Constructeur
		protected UpdaterForConcours(ModelConcours concours) {
			this.concours = concours;
		}
		
		// Implémentation de create
		@Override
		public ModelDiffusion create (Pair<InfosModelDiffusion, InfosModelTheme> infos) {
			return new ModelDiffusion(this.concours, new ModelTheme(infos.getSecond()), infos.getFirst());
		}
		
		// Implémentation de update
		@Override
		public void update (ModelDiffusion diffusion, Pair<InfosModelDiffusion, InfosModelTheme> infos) {
			diffusion.setInfos(infos.getFirst());
			diffusion.getTheme().setInfos(infos.getSecond());
		}
		
	}
	
	// Classe pour valider les opérations sur la liste des diffusions d'un concours
	protected static class ValidatorForConcours implements IListValidator<Pair<InfosModelDiffusion, InfosModelTheme>>
	{
		// Implémentation de IListValidator
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
