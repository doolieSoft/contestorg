<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<!-- Importation des templates communs -->
	<xsl:import href="../../common.xsl"/>
	<xsl:import href="../common.xsl"/>
	
	<!-- Cible HTML -->
	<xsl:output method="html" />
	
	<!-- Paramètres-->
	<xsl:param name="idCategorie" />
	<xsl:param name="idPoule" />
  
	<!-- Template principal -->
	<xsl:template match="/">
		<html>
			<!-- Head -->
			<head>
				<title>
					<xsl:choose>
						<xsl:when test="/concours/@participants = 'equipes'">
							Classement des équipes aux phases éliminatoires
						</xsl:when>
						<xsl:otherwise>
							Classement des joueurs aux phases éliminatoires
						</xsl:otherwise>
					</xsl:choose>
				</title>
				<link rel="shortcut icon" href="favicon.png" type="image/x-icon" />
				<link href="common.css" rel="stylesheet" type="text/css" />
				<link href="style.css" rel="stylesheet" type="text/css" />
				<script type="text/javascript" src="common.js"></script>
			</head>
			<!-- Body -->
			<body onload="setTimeout('scrollit()', 3000);">
				<!-- Titre -->
				<xsl:if test="count(/concours/listeCategories/categorie) = 1">
					<h1>
						<xsl:choose>
							<xsl:when test="/concours/@participants = 'equipes'">
								Classement des équipes aux phases éliminatoires
							</xsl:when>
							<xsl:otherwise>
								Classement des joueurs aux phases éliminatoires
							</xsl:otherwise>
						</xsl:choose>
					</h1>
				</xsl:if>
			
				<!-- Liste des catégories -->
				<xsl:apply-templates select="/concours/listeCategories">
				</xsl:apply-templates>
			</body>
		</html>
	</xsl:template>
	
	<!-- Template d'une liste de catégories -->
	<xsl:template match="listeCategories">
		<xsl:choose>
			<!-- Catégorie non indiquée -->
			<xsl:when test="$idCategorie = ''">
				<xsl:apply-templates select="./categorie">
				</xsl:apply-templates>
			</xsl:when>
			<!-- Catégorie existante -->
			<xsl:when test="count(./categorie[@id=$idCategorie]) = 1">
				<xsl:apply-templates select="./categorie[@id=$idCategorie]">
				</xsl:apply-templates>
			</xsl:when>
			<!-- Catégorie inexistante -->
			<xsl:otherwise>
				<p class="erreur">La catégorie indiquée n'existe pas.</p>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	
	<!-- Template d'une catégorie -->
	<xsl:template match="categorie">
		<xsl:if test="count(./listePoules/poule/listeParticipants/participant) != 0">
			<!-- Titre -->
			<xsl:if test="count(../categorie) != 1">
				<h2><xsl:value-of select="./@nom" /></h2>
			</xsl:if>
			
			<!-- Liste des participants -->
			<xsl:apply-templates select="./classementCategoriePhasesEliminatoires/listeClassementParticipants">
			</xsl:apply-templates>
		</xsl:if>
	</xsl:template>
	
	<!-- Template d'un classement de paticipants -->
	<xsl:template match="listeClassementParticipants">
		<table>
			<thead>
			<tr>
				<th id="th-rang">Rang</th>
				<th id="th-participant">
					<xsl:choose>
						<xsl:when test="/concours/@participants = 'equipes'">
							Equipe
						</xsl:when>
						<xsl:otherwise>
							Joueur
						</xsl:otherwise>
					</xsl:choose>
				</th>
			</tr>
			</thead>
			<tbody>
				<xsl:apply-templates select="./classementParticipant">
					<xsl:sort select="@rang" data-type="number" />
				</xsl:apply-templates>
			</tbody>
		</table>
	</xsl:template>
	
	<!-- Template d'un participant dans un classement -->
	<xsl:template match="classementParticipant">
		<xsl:variable name="id" select="./@refParticipant" />
		<tr>
			<td class="td-rang"><xsl:value-of select="./@rang" /></td>
			<td class="td-participant"><xsl:value-of select="//participant[@id=$id]/@nom" /></td>
		</tr>
	</xsl:template>
</xsl:stylesheet>
