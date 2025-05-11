# 🌿 DJL API: Pflanzenerkennung (Toxic vs. Non-Toxic)

Dieses Projekt wurde im Rahmen des Moduls Model Deployment & Maintenance erstellt. Es kombiniert ein lokal trainiertes Deep-Learning-Modell (basierend auf DJL) mit einem Spring Boot Backend, einer Web-UI und einem Docker-basierten Deployment auf Render.com. Das Ziel ist es, Pflanzenbilder automatisiert als giftig oder ungiftig zu klassifizieren.

## 📦 Tech-Stack

- Java 17 / Spring Boot
- Deep Java Library (DJL) mit MXNet Engine
- HTML / JavaScript für UI
- Maven & Docker
- Render.com (Deployment)
- GitHub (Versionierung)

## 🧠 Modelltraining

Das Modell basiert auf einem ResNet-18 (über DJL basicmodelzoo).  
Das Training erfolgte lokal mit einem eigenen Datensatz aus ca. 1000 Bildern pro Klasse (toxic, nontoxic).

Trainingsablauf:
- Main.java: Startet das Training mit DJL und speichert Checkpoints unter /models/plantdetector
- Models.java: Definiert das ResNet-18 Modell mit festen Bilddimensionen (224×224, 3 Kanäle)
- Evaluate.java: Prüft Genauigkeit und erzeugt eine Confusion-Matrix für das Validierungsset

Trainingsdatenstruktur:

dataset/
├── train/
│   ├── toxic/
│   └── nontoxic/
├── valid/
    ├── toxic/
    └── nontoxic/

Trainiert wurde mit EasyTrain.fit, 10 Epochen, Learning Rate 0.001, Batch-Größe 32.  
Das Modell wird automatisch beim Start des Backends geladen.

## 🔍 Anwendung & Ablauf

1. Nutzer lädt ein Bild auf der Web-Oberfläche hoch  
2. Das Bild wird per POST /api/analyze an das Backend gesendet  
3. Das Modell klassifiziert das Bild lokal auf dem Server  
4. Die JSON-Antwort enthält className und probability  
5. Die UI zeigt die Resultate visuell formatiert an

## 🔗 REST API

POST /api/analyze

- Content-Type: multipart/form-data
- Form-Feld: image

Beispielantwort:

{
  "className": "toxic",
  "probability": 0.9458
}

## 💻 Benutzeroberfläche (UI)

Die HTML/JavaScript-Webseite (index.html) befindet sich unter:

src/main/resources/static/index.html

- intuitives Hochladen von Bildern
- direkte Anzeige von Klassifikation + Wahrscheinlichkeit
- modernisiert mit Icons und Formatierung

Live-Demo:  
https://djl-api.onrender.com

## 🐳 Docker Deployment

Image bauen:  
docker build -t djl-api .

Container starten:  
docker run -p 8080:8080 djl-api

Das Backend lädt beim Start automatisch das Modell aus /resources/models/plantdetector.

## ☁️ Deployment auf Render

- Docker-Webservice
- Öffentliche URL erreichbar
- Modell wird beim Start geladen
- Logs und Status einsehbar im Render-Dashboard

## 🧾 Projektstruktur

Projekt2/
├── djl-api/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   └── ch/zhaw/fakereader/api/
│   │   │   │       ├── DjlApiApplication.java
│   │   │   │       ├── ModelController.java
│   │   │   │       └── ModelService.java
│   │   │   └── resources/
│   │   │       └── static/
│   │   │           └── index.html
│   └── pom.xml
├── djl-model/
│   ├── dataset/
│   ├── models/
│   ├── src/
│   │   └── main/
│   │       └── java/
│   │           └── ch/zhaw/fakereader/
│   │               ├── Main.java
│   │               ├── Models.java
│   │               └── Evaluate.java
├── Dockerfile
├── README.md
└── streamlit_app.py (nicht verwendet)


## 🎬 Screencast-Inhalt (5 Minuten)

- Projekt- & Dateistruktur
- Deployment auf Render.com
- Analyse eines Bilds über die Web-UI
- Erklärung API-Aufruf
- Darstellung des Trainings: Datensatz, Modellarchitektur, Evaluierung
- Ergebnisdarstellung mit visueller UI

## ✅ Bewertungskriterien (Selbstcheck)

| Kriterium                            | Erfüllt |
|-------------------------------------|---------|
| Eigenständige Daten & Modell        | ✅       |
| Training & Evaluierung dokumentiert | ✅       |
| Funktionierendes Backend/API        | ✅       |
| UI vollständig integriert           | ✅       |
| Deployment öffentlich zugänglich    | ✅       |
| Docker / CI optional                | ✅       |
| Screencast in Hochdeutsch           | ✅       |

## 👤 Autorin

Name: Sabrina Studer  
Modul: Model Deployment & Maintenance  
Studiengang: Wirtschaftsinformatik  
Semester: FS2025
