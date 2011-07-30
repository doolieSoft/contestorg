@echo OFF

REM Optimiser la taille un fichier PNG donn�
REM @param %1 R�pertoire du fichier PNG
REM @param %2 Nom du fichier PNG

REM Cr�er le r�pertoire temporaire
mkdir .\_temp

REM Optimisation 1
.\pngnq-0.5\pngnq.exe -vf -s1 -d .\_temp -e .png %1%2

REM Optimisation 2
REM .\optipng-0.6.5\optipng.exe -o7 %1%2

REM D�placer les fichiers du r�pertoire temporaire vers le r�pertoire d'origine
move .\_temp\%2 %1

REM Supprimer le r�pertoire temporaire
del /q .\_temp\*
rmdir .\_temp