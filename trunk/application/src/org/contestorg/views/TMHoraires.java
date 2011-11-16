package org.contestorg.views;


import java.awt.Window;

import org.contestorg.infos.InfosModelHoraire;

public class TMHoraires extends TMAbstract<InfosModelHoraire>
{

	// Constructeur
	public TMHoraires(Window w_parent) {
		// Constructeur
		super(w_parent);
	}
	
	// Méthodes à implémenter
	@Override
	public Window getAddWindow () {
		return new JDHoraireCreer(this.w_parent, this);
	}
	@Override
	public Window getUpdateWindow (InfosModelHoraire infos) {
		return new JDHoraireEditer(this.w_parent, this, infos);
	}
	@Override
	public boolean acceptDelete (InfosModelHoraire infos) {
		// Demander la confirmation à l'utilisateur
		if (ViewHelper.confirmation(this.w_parent, "Désirez-vous vraiment supprimer l'emplacement \"" + InfosModelHoraire.getJoursHumain(infos.getJours()) + ", " + InfosModelHoraire.getHeureHumain(infos.getDebut()) + " - " + InfosModelHoraire.getHeureHumain(infos.getFin()) + "\" ?")) {
			return true;
		} else {
			return false;
		}
	}

	// Implémentation manquante de TableModel
	@Override
	public Class<?> getColumnClass (int column) {
		return String.class;
	}
	@Override
	public int getColumnCount () {
		return 2;
	}
	@Override
	public String getColumnName (int column) {
		switch(column) {
			case 0 : return "Jour";
			case 1 : return "Heures";
		}
		return null;
	}
	@Override
	public Object getValueAt (int row, int column) {
		switch(column) {
			case 0 : return InfosModelHoraire.getJoursHumain(this.get(row).getJours());
			case 1 : return InfosModelHoraire.getHeureHumain(this.get(row).getDebut())+" - "+InfosModelHoraire.getHeureHumain(this.get(row).getFin());
		}
		return null;
	}
	@Override
	public boolean isCellEditable (int row, int column) {
		return true;
	}
	@Override
	public void setValueAt (Object object, int row, int column) {
		// Caster l'objet en string
		String string = (String)object;
		
		// Quel colonne ?
		switch(column) {
			case 0 : // Jours
				// Récupérer le AND binaire sur la valeur des jours
				int jours = InfosModelHoraire.getJoursBinaire(string);
				if(jours != 0) {
					// Mettre à jour le TableModel
					this.update(row, new InfosModelHoraire(jours, this.get(row).getDebut(), this.get(row).getFin()));
				} else {
					// Message d'erreur
					ViewHelper.derror(this.w_parent, "Il faut au moins un jour valide pour un horaire.");
				}
				break;
			case 1 : // Heures
				// Spliter les heures sur le tiret
				String[] heures = string.split("-");
				
				// Vérifier s'il y a bien deux heures
				if(heures.length == 2) {
					// Récupérer l'heure de début
					int debut = InfosModelHoraire.getHeureMinutes(heures[0]);
					
					// Récupérer l'heure de fin
					int fin = InfosModelHoraire.getHeureMinutes(heures[1]);
					
					// Vérifier si les heures sont correctes
					if(debut != -1 && fin != -1) {
						// Mettre à jour le TableModel
						this.update(row, new InfosModelHoraire(this.get(row).getJours(), debut, fin));
					} else {
						// Message d'erreur
						ViewHelper.derror(this.w_parent, "Les heures de début et de fin d'un horaire doivent être données sous la forme \"??h?? - ??h??\".");
					}
				} else {
					// Message d'erreur
					ViewHelper.derror(this.w_parent, "Les heures de début et de fin d'un horaire doivent être données sous la forme \"??h?? - ??h??\".");
				}
				break;
		}
	}

}
