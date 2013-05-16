<?xml version='1.0' encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
				xmlns:fo="http://www.w3.org/1999/XSL/Format"
				version="1.0">

	<!-- Importation de la feuille de style par défaut -->
	<xsl:import href="docbook-xsl-1.76.1/htmlhelp/htmlhelp.xsl"/>
	
	<!-- Paramètres -->
	<xsl:param name="chunk.first.sections" select="1"/>
	<xsl:param name="html.stylesheet" select="'documentation.css'"/>
	
</xsl:stylesheet>