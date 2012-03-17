<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<!-- Cible XML -->
	<xsl:output method="xml" indent="yes" encoding="utf-8" />
	
	<!-- Template du concours -->
	<xsl:template match="concours">
		<concours version="2.0.0" id="-1" nom="{./@nom}" site="{./@site}" lieu="{./@lieu}" email="{./@email}" telephone="{./@telephone}" description="{./@description}" participants="{./@participants}" qualifications="{./@qualifications}">
			<xsl:apply-templates />
		</concours>
	</xsl:template>
	
	<!-- Template d'une liste de participants -->
	<xsl:template match="listeEquipes">
		<listeParticipants>
			<xsl:apply-templates />
		</listeParticipants>
	</xsl:template>
	
	<!-- Template d'un participant -->
	<xsl:template match="equipe">
		<participant id="{./@id}" nom="{./@nom}" ville="{./@ville}" statut="{./@statut}" stand="{./@stand}" details="{./@details}" rangPhasesQualifs="{./@rangPhasesQualifs}" pointsPhasesQualifs="{./@pointsPhasesQualifs}" rangPhasesElims="{./@rangPhasesElims}" pointsPhasesElims="{./@pointsPhasesElims}">		
			<xsl:apply-templates />
		</participant>
	</xsl:template>
	
	<!-- Template d'une participation -->
	<xsl:template match="participation">
		<participation id="{./@id}" refParticipant="{./@refEquipe}" resultat="{./@resultat}" points="{./@points}">
			<xsl:apply-templates />
		</participation>
	</xsl:template>
	
	<!-- Template d'une propriété de participant -->
	<xsl:template match="propriete">
		<xsl:variable name="type">
			<xsl:choose>
				<xsl:when test="./@type = 'caracteres'">text</xsl:when>
				<xsl:otherwise><xsl:value-of select="./@type" /></xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<propriete id="{./@id}" nom="{./@nom}" type="{$type}" obligatoire="{./@obligatoire}">
			<xsl:apply-templates />
		</propriete>
	</xsl:template>
	
	<!-- Template d'un classement de participants -->
	<xsl:template match="listeClassementEquipes">
		<listeClassementParticipants>
			<xsl:apply-templates />
		</listeClassementParticipants>
	</xsl:template>
	
	<!-- Template d'une classement de participant -->
	<xsl:template match="classementEquipe">
		<classementParticipant refParticipant="{./@refEquipe}" rang="{./@rang}">
			<xsl:apply-templates />
		</classementParticipant>
	</xsl:template>
	
	<!-- Template pour les autres balises -->
	<xsl:template match="*">
	    <xsl:copy>
	        <xsl:copy-of select="@*"/>
	        <xsl:apply-templates />
    	</xsl:copy>
	</xsl:template> 
</xsl:stylesheet>