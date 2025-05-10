@echo off
title Pflanzen-Klassifikation Training
echo Starte Training des Pflanzen-Klassifizierungsmodells...
echo ----------------------------------------

REM Projekt bauen
mvn clean package

REM Wenn der Build erfolgreich war, führe das Training aus
IF %ERRORLEVEL% EQU 0 (
    echo Build erfolgreich. Starte Training...
    java -cp "target/classes;target/dependency/*" ch.zhaw.fakereader.Main
) ELSE (
    echo Build fehlgeschlagen. Bitte Fehler im Code beheben.
)

echo ----------------------------------------
echo Training abgeschlossen. Drücke eine Taste zum Schließen...
pause
