@echo OFF

REM Se rendre dans le r�pertoire de la documentation
cd ..\..\doc

REM G�n�rer la documentation HTML
java -jar ..\lib\saxon-6.5.5\saxon.jar documentation.xml ..\make\generer-doc\documentation-html.xsl > documentation.html
copy ..\make\generer-doc\documentation-html.css .\documentation.css

REM Se rendre dans le r�pertoire de g�n�ration de la documentation
cd ..\make\generer-doc