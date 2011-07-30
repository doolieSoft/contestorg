<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<!-- Importation des templates communs -->
	<xsl:import href="../../common.xsl"/>
	<xsl:import href="../common.xsl"/>
	
	<!-- Cible HTML -->
	<xsl:output method="html" encoding="utf-8" />
	
	<!-- Paramètres-->
	<xsl:param name="idCategorie" />
	<xsl:param name="idPoule" />
  
	<!-- Template principal -->
	<xsl:template match="/">
		<html>
			<!-- Head -->
			<head>
				<title>Liste des équipes</title>
				<link rel="shortcut icon" href="sport.png" type="image/x-icon" />
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
								Liste des équipes
							</xsl:when>
							<xsl:otherwise>
								Liste des joueurs
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
		<xsl:if test="count(./listePoules/poule/listeEquipes/equipe) != 0">
			<!-- Titre -->
			<xsl:if test="count(../categorie) != 1">
				<h2><xsl:value-of select="./@nom" /></h2>
			</xsl:if>
			
			<!-- Liste des poules -->
			<xsl:apply-templates select="./listePoules">
			</xsl:apply-templates>
		</xsl:if>
	</xsl:template>
	
	<!-- Template d'une liste de poules -->
	<xsl:template match="listePoules">
		<xsl:choose>
			<!-- Poule non indiquée -->
			<xsl:when test="$idPoule = ''">
				<xsl:apply-templates select="./poule">
				</xsl:apply-templates>
			</xsl:when>
			<!-- Poule existante -->
			<xsl:when test="count(./poule[@id=$idPoule]) = 1">
				<xsl:apply-templates select="./poule[@id=$idPoule]">
				</xsl:apply-templates>
			</xsl:when>
			<!-- Poule inexistante -->
			<xsl:otherwise>
				<p class="erreur">La poule indiquée n'existe pas.</p>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	
	<!-- Template d'une poule -->
	<xsl:template match="poule">
		<xsl:if test="count(./listeEquipes/equipe) != 0">
			<!-- Titre -->
			<xsl:if test="count(../poule) != 1">
				<h3><xsl:value-of select="./@nom" /></h3>
			</xsl:if>
			
			<!-- Liste des équipes -->
			<xsl:apply-templates select="./listeEquipes">
			</xsl:apply-templates>
		</xsl:if>
	</xsl:template>
	
	<!-- Template d'une liste d'équipes -->
	<xsl:template match="listeEquipes">
		<table>
			<thead>
				<tr>
					<th id="th-equipe">
						<xsl:choose>
							<xsl:when test="/concours/@participants = 'equipes'">
								Equipe
							</xsl:when>
							<xsl:otherwise>
								Joueur
							</xsl:otherwise>
						</xsl:choose> 
					</th>
					<th id="th-ville">Ville</th>
					<th id="th-statut">Statut</th>
				</tr>
			</thead>
			<tbody>
				<xsl:apply-templates select="./equipe">
					<xsl:sort select="@nom" />
				</xsl:apply-templates>
			</tbody>
		</table>
	</xsl:template>
	
	<!-- Template d'une équipe -->
	<xsl:template match="equipe">
		<tr>
			<td class="td-nom">
				<xsl:value-of select="./@nom" />
			</td>
			<td class="td-ville">
				<xsl:value-of select="./@ville" />
			</td>
			<td class="td-statut">
				<xsl:call-template name="html-equipe-statut">
				  <xsl:with-param name="id" select="./@id" />
				</xsl:call-template>
			</td>
		</tr>
	</xsl:template>
</xsl:stylesheet>
