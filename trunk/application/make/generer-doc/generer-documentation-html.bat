@echo OFF

REM Se rendre dans le répertoire de la documentation
cd ..\..\doc

REM Générer la documentation HTML
java -jar ..\lib\saxon-6.5.5\saxon.jar documentation.xml ..\make\generer-doc\documentation-html.xsl > documentation.html
copy ..\make\generer-doc\documentation-html.css .\documentation.css

REM Se rendre dans le répertoire de génération de la documentation
cd ..\make\generer-doc