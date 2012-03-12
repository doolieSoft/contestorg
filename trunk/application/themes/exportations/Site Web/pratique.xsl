<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<!-- Importation des templates communs -->
	<xsl:import href="../../common.xsl"/>
	<xsl:import href="../common.xsl"/>
	<xsl:import href="common.xsl"/>
	
	<!-- Cible HTML -->
	<xsl:output method="html" encoding="utf-8" />
  
	<!-- Template principal -->
	<xsl:template match="/">
		<html>
			<!-- Head -->
			<xsl:call-template name="head" />
			
			<!-- Body -->
			<body>
				<!-- Page -->
				<div id="page">
					<!-- Header -->
					<xsl:call-template name="html-entete" />
					
					<!-- Menu -->
					<xsl:call-template name="menu" />
					
					<!-- Informations sur le tournoi -->
					<h2>Informations sur le tournoi</h2>
					<div class="bloc">
						<xsl:if test="count(/concours/@nom) = 1">
							<b>Nom :</b>&#160;<xsl:value-of select="/concours/@nom" /><br/>
						</xsl:if>
						<xsl:if test="count(/concours/@site) = 1">
							<b>Site web :</b>&#160;<a href="{/concours/@site}"><xsl:value-of select="/concours/@site" /></a><br/>
						</xsl:if>
						<xsl:if test="count(/concours/@lieu) = 1">
							<b>Lieu :</b>&#160;<xsl:value-of select="/concours/@lieu" /><br/>
						</xsl:if>
						<xsl:if test="count(/concours/@telephone) = 1">
							<b>Téléphone :</b>&#160;<xsl:value-of select="/concours/@telephone" /><br/>
						</xsl:if>
						<xsl:if test="count(/concours/@email) = 1">
							<b>Email :</b>&#160;<a href="mailto:{/concours/@email}"><xsl:value-of select="/concours/@email" /></a><br/>
						</xsl:if>
						<xsl:if test="count(/concours/@description) = 1">
							<b>Description :</b>
							<p>
								<xsl:call-template name="html-nl2br">
						          <xsl:with-param name="text" select="/concours/@description"/>
						        </xsl:call-template>
        					</p>
						</xsl:if>
					</div>
					
					<!-- Informations sur l'organisateur -->
					<xsl:if test="count(/concours/organisateur) = 1">
						<h2>Informations sur l'organisateur</h2>
						<div class="bloc">
							<xsl:if test="count(/concours/organisateur/@nom) = 1">
								<b>Nom :</b>&#160;<xsl:value-of select="/concours/organisateur/@nom" /><br/>
							</xsl:if>
							<xsl:if test="count(/concours/organisateur/@site) = 1">
								<b>Site web :</b>&#160;<a href="{/concours/organisateur/@site}"><xsl:value-of select="/concours/organisateur/@site" /></a><br/>
							</xsl:if>
							<xsl:if test="count(/concours/organisateur/@lieu) = 1">
								<b>Lieu :</b>&#160;<xsl:value-of select="/concours/organisateur/@lieu" /><br/>
							</xsl:if>
							<xsl:if test="count(/concours/organisateur/@telephone) = 1">
								<b>Téléphone :</b>&#160;<xsl:value-of select="/concours/organisateur/@telephone" /><br/>
							</xsl:if>
							<xsl:if test="count(/concours/organisateur/@email) = 1">
								<b>Email :</b>&#160;<a href="mailto:{/concours/organisateur/@email}"><xsl:value-of select="/concours/organisateur/@email" /></a><br/>
							</xsl:if>
							<xsl:if test="count(/concours/organisateur/@description) = 1">
								<b>Description :</b>
								<p>
									<xsl:call-template name="html-nl2br">
							          <xsl:with-param name="text" select="/concours/organisateur/@description"/>
							        </xsl:call-template>
	        					</p>
							</xsl:if>
						</div>
					</xsl:if>
					
					<!-- Lieux -->
					<xsl:if test="count(/concours/listeLieux/lieu) > 0">
						<h2>Lieux</h2>
						<xsl:apply-templates select="/concours/listeLieux">
						</xsl:apply-templates>
					</xsl:if>
					
					<!-- Footer -->
					<xsl:call-template name="html-pied" />
				</div>
			</body>
		</html>
	</xsl:template>
	
	<!-- Template pour un lieu -->
	<xsl:template match="lieu">
		<!-- Titre -->
		<h3><xsl:value-of select="./@nom"></xsl:value-of></h3>
		
		<div class="bloc">
			<!-- Informations du lieu -->
			<xsl:if test="./@lieu != ''">
				<b>Lieu : </b><xsl:value-of select="./@lieu"></xsl:value-of><br/>
			</xsl:if>
			<xsl:if test="./@telephone != ''">
				<b>Téléphone : </b><xsl:value-of select="./@telephone"></xsl:value-of><br/>
			</xsl:if>
			<xsl:if test="./@email != ''">
				<b>Email : </b><xsl:value-of select="./@email"></xsl:value-of><br/>
			</xsl:if>
			<xsl:if test="./@description != ''">
				<b>Description : </b><br/>
				<p>
					<xsl:call-template name="html-nl2br">
		          		<xsl:with-param name="text" select="./@description"/>
		        	</xsl:call-template>
		        </p>
			</xsl:if>
		
			<!-- Liste des horaires -->
			<xsl:if test="count(./listeHoraires/horaire) > 0">
				<b>Horaires :</b>
				<ul>
					<xsl:apply-templates select="./listeHoraires/horaire">
					</xsl:apply-templates>
				</ul>
			</xsl:if>
			
			<!-- Liste des emplacements -->
			<b>Emplacements :</b>
			<ul>
				<xsl:apply-templates select="./listeEmplacements/emplacement">
				</xsl:apply-templates>
			</ul>
		</div>
	</xsl:template>
	
	<!-- Template pour un horaire -->
	<xsl:template match="horaire">
		<li>
			<!-- Heure de début -->
			<xsl:call-template name="heure">
				<xsl:with-param name="timestamp"><xsl:number value="./@debut" /></xsl:with-param>
			</xsl:call-template>
			-
			<!-- Heure de fin -->
			<xsl:call-template name="heure">
				<xsl:with-param name="timestamp"><xsl:number value="./@fin" /></xsl:with-param>
			</xsl:call-template>
			:
			<!-- Jours concernés -->
			<xsl:call-template name="jours">
				<xsl:with-param name="valeur"><xsl:number value="./@jours" /></xsl:with-param>
				<xsl:with-param name="jour"><xsl:number value="1" /></xsl:with-param>
			</xsl:call-template>
		</li>
	</xsl:template>
	
	<!-- Template pour une heure -->
	<xsl:template name="heure">
		<!-- Paramètre -->
		<xsl:param name="timestamp" />
		
		<!-- Calculs -->
		<xsl:variable name="heures"><xsl:number value="floor($timestamp div 60)" /></xsl:variable>
		<xsl:variable name="minutes"><xsl:number value="$timestamp mod 60" /></xsl:variable>
		
		<!-- Heures et minutes -->
		<xsl:if test="10 > $heures">0</xsl:if><xsl:value-of select="$heures" />h<xsl:if test="10 > $minutes">0</xsl:if><xsl:value-of select="$minutes" />
	</xsl:template>
	
	<!-- Template pour une liste de jours -->
	<xsl:template name="jours">
		<xsl:param name="valeur" />
		<xsl:param name="jour" />
		<xsl:choose>
			<xsl:when test="($valeur mod ($jour * 2)) - ($valeur mod ($jour * 1))">
				<xsl:choose>
					<xsl:when test="$jour = 1">lundi</xsl:when>
					<xsl:when test="$jour = 2">mardi</xsl:when>
					<xsl:when test="$jour = 4">mercredi</xsl:when>
					<xsl:when test="$jour = 8">jeudi</xsl:when>
					<xsl:when test="$jour = 16">vendredi</xsl:when>
					<xsl:when test="$jour = 32">samedi</xsl:when>
					<xsl:when test="$jour = 64">dimanche</xsl:when>
				</xsl:choose>
				<xsl:variable name="valeur"><xsl:value-of select="$valeur - $jour" /></xsl:variable>
				<xsl:if test="$valeur != 0">
					,
					<xsl:call-template name="jours">
						<xsl:with-param name="valeur"><xsl:value-of select="$valeur" /></xsl:with-param>
						<xsl:with-param name="jour"><xsl:value-of select="$jour*2" /></xsl:with-param>
					</xsl:call-template>
				</xsl:if>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="jours">
					<xsl:with-param name="valeur"><xsl:value-of select="$valeur" /></xsl:with-param>
					<xsl:with-param name="jour"><xsl:value-of select="$jour*2" /></xsl:with-param>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	
	<!-- Template pour un emplacement -->
	<xsl:template match="emplacement">
		<li>
			<xsl:value-of select="./@nom" />
			<xsl:if test="./@description != ''">
				<p style="padding-left:10px;">
					<xsl:call-template name="html-nl2br">
			          <xsl:with-param name="text" select="./@description"/>
			        </xsl:call-template>
	    		</p>
			</xsl:if>
		</li>
	</xsl:template>
	
</xsl:stylesheet>