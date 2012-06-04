<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<!-- Importation des templates communs -->
	<xsl:import href="../../common.xsl"/>
	<xsl:import href="../common.xsl"/>
	
	<!-- Paramètres-->
	<xsl:param name="idCategorie" />
	
	<!-- Cible HTML -->
	<xsl:output method="html" encoding="utf-8" />
  
	<!-- Template principal -->
	<xsl:template match="/">
		<html>
			<head>
				<title>Phases éliminatoires</title>
				<link rel="shortcut icon" href="favicon.png" type="image/x-icon" />
				<link href="common.css" rel="stylesheet" type="text/css" />
				<link href="style.css" rel="stylesheet" type="text/css" />
				<script type="text/javascript" src="common.js"></script>
			</head>
			<body onload="setTimeout('scrollit()', 3000);">
				<!-- Titre -->
				<xsl:choose>
					<xsl:when test="count(/concours/listeCategories/categorie) > 1">
						<h1><xsl:value-of select="/concours/listeCategories/categorie[@id=$idCategorie]/@nom" /></h1>
					</xsl:when>
					<xsl:otherwise>
						<h1>Phases éliminatoires</h1>
					</xsl:otherwise>
				</xsl:choose>
				
		
				<!-- Podim -->
				<xsl:apply-templates select="/concours/listeCategories/categorie[@id=$idCategorie]/classementCategoriePhasesEliminatoires/listeClassementParticipants">
				</xsl:apply-templates>
			</body>
		</html>
	</xsl:template>
	
	<!-- Template d'un classement -->
	<xsl:template match="listeClassementParticipants">
		<div id="steps">
			<div id="step2nd">
				<xsl:call-template name="marche">
					<xsl:with-param name="rang" select="2" />
				</xsl:call-template>
				<img src="medal-silver.png" alt="Medaille d'argent" />
			</div>
			<div id="step1st">
				<xsl:call-template name="marche">
					<xsl:with-param name="rang" select="1" />
				</xsl:call-template>
				<img src="medal-gold.png" alt="Medaille d'or" />
			</div>
			<div id="step3rd">
				<xsl:call-template name="marche">
					<xsl:with-param name="rang" select="3" />
				</xsl:call-template>
				<img src="medal-bronze.png" alt="Medaille de bronze" />
			</div>
		</div>
	</xsl:template>
	
	<!-- Template d'une marche -->
	<xsl:template name="marche">
		<xsl:param name="rang" />
		<p class="equipe">
			<xsl:choose>
				<xsl:when test="count(classementParticipant[@rang=$rang]) = 1 and ($rang != 2 or count(classementParticipant[@rang=1]) = 1)">
					<xsl:call-template name="participant">
						<xsl:with-param name="id" select="classementParticipant[@rang=$rang]/@refParticipant" />
					</xsl:call-template>
				</xsl:when>
				<xsl:otherwise>
					...
				</xsl:otherwise>
			</xsl:choose>
		</p>
	</xsl:template>
	
	<!-- Template d'un participant -->
	<xsl:template name="participant">
		<xsl:param name="id" />
		<xsl:value-of select="//participant[@id=$id]/@nom" />
	</xsl:template>
</xsl:stylesheet>
