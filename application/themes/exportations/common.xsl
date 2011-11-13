<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	
	<!-- Template pour le head -->
	<xsl:template name="html-header">
		<div id="header">
			<!-- Barre colorée -->
			<div id="bar">
				<div id="colorA"></div>
				<div id="colorB"></div>
				<div id="colorC"></div>
				<div id="colorD"></div>
				<div id="colorE"></div>
				<div id="colorF"></div>
				<div id="colorG"></div>
				<div id="colorH"></div>
				<div id="colorI"></div>
				<div id="colorJ"></div>
			</div>
			
			<!-- Titre -->
			<h1>
				<xsl:value-of select="/concours/@nom" />
				<xsl:if test="/concours/@lieu != ''">
					(<xsl:value-of select="/concours/@lieu" />)
				</xsl:if>
			</h1>
		</div>
	</xsl:template>
	
	<!-- Template pour le footer -->
	<xsl:template name="html-footer">
		<div id="footer">Page générée à l'aide du logiciel d'organisation de concours <a href="http://www.elfangels.fr/contestorg/">ContestOrg</a></div>
	</xsl:template>
	
	<!-- Template pour le tag d'ouverture PHP -->
	<xsl:template name="php-start">
		<xsl:text disable-output-escaping="yes">&lt;?php</xsl:text>
	</xsl:template>
	
	<!-- Template pour le tag de fermeture PHP -->
	<xsl:template name="php-end">
		<xsl:text disable-output-escaping="yes">?></xsl:text>
	</xsl:template>
	
</xsl:stylesheet>