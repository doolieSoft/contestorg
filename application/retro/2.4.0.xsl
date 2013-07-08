<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<!-- Cible XML -->
	<xsl:output method="xml" indent="yes" encoding="utf-8" />
	
	<!-- Template des points remportés en fonction du résultat -->
	<xsl:template match="concours">
		<concours id="{./@id}" nom="{./@nom}" site="{./@site}" lieu="{./@lieu}" email="{./@email}" telephone="{./@telephone}" description="{./@description}" participants="{./@participants}" qualifications="{./@qualifications}" statutHomologueActive="oui">
			<xsl:apply-templates />
		</concours>
	</xsl:template>
	
	<!-- Template des points remportés en fonction du résultat -->
	<xsl:template match="points">
		<points victoire="{./@victoire}" egalite="{./@egalite}" egaliteActivee="oui" defaite="{./@defaite}" forfait="0.0" />
	</xsl:template>
	
	<!-- Template d'un critère de classement prenant en compte la quantité d'un objectif -->
	<xsl:template match="critereNbObjectifs">
		<critereQuantiteObjectif refObjectif="{./@refObjectif}" />
	</xsl:template>
	
	<!-- Template pour les autres balises -->
	<xsl:template match="*">
	    <xsl:copy>
	        <xsl:copy-of select="@*"/>
	        <xsl:apply-templates />
    	</xsl:copy>
	</xsl:template> 
</xsl:stylesheet>