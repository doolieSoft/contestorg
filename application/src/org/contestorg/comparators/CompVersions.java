package org.contestorg.comparators;

import java.util.Comparator;

public class CompVersions implements Comparator<String>
{
	/**
	 * @see Comparator#compare(Object,Object)
	 */
	@Override
	public int compare (String versionA, String versionB) {
		// Vérifier si les versions sont vides
		boolean versionAVide = versionA == null || versionA.isEmpty();
		boolean versionBVide = versionB == null || versionB.isEmpty();
		if(!versionAVide && versionBVide) {
			return 1;
		}
		if(versionAVide && !versionBVide) {
			return -1;
		}
		if(versionAVide && versionBVide) {
			return 0;
		}
		
		// Extraire les informations de la version A
		String[] informationsVersionA = versionA.split("\\.");
		int versionPrincipaleA = -1;
		try {
			versionPrincipaleA = Integer.parseInt(informationsVersionA[0]);
		} catch(NumberFormatException e) {}
		int fonctionnalitesDeveloppeesA = -1;
		try {
			fonctionnalitesDeveloppeesA = Integer.parseInt(informationsVersionA[1]);
		} catch(NumberFormatException e) {}
		int anomaliesCorrigeesA = -1;
		try {
			anomaliesCorrigeesA = Integer.parseInt(informationsVersionA[2]);
		} catch(NumberFormatException e) {}

		// Extraire les informations de la version B
		String[] informationsVersionB = versionB.split("\\.");
		int versionPrincipaleB = -1;
		try {
			versionPrincipaleB = Integer.parseInt(informationsVersionB[0]);
		} catch(NumberFormatException e) {}
		int fonctionnalitesDeveloppeesB = -1;
		try {
			fonctionnalitesDeveloppeesB = Integer.parseInt(informationsVersionB[1]);
		} catch(NumberFormatException e) {}
		int anomaliesCorrigeesB = -1;
		try {
			anomaliesCorrigeesB = Integer.parseInt(informationsVersionB[2]);
		} catch(NumberFormatException e) {}
		
		// Comparer les information de version
		if(versionPrincipaleA == versionPrincipaleB) {
			if(fonctionnalitesDeveloppeesA == fonctionnalitesDeveloppeesB) {
				if(anomaliesCorrigeesA == anomaliesCorrigeesB) {
					return 0;
				} else {
					return anomaliesCorrigeesA < anomaliesCorrigeesB ? -1 : 1;
				}
			} else {
				return fonctionnalitesDeveloppeesA < fonctionnalitesDeveloppeesB ? -1 : 1;
			}
		} else {
			return versionPrincipaleA < versionPrincipaleB ? -1 : 1;
		}
	}
}
