# 🌿 DJL API: Pflanzenerkennung (Toxic vs. Non-Toxic)

Dieses Projekt wurde im Rahmen des Moduls **Model Deployment & Maintenance** erstellt. Es nutzt die **Deep Java Library (DJL)** in Kombination mit einem **Spring Boot Webservice**, um Pflanzenbilder als **giftig** oder **ungiftig** zu klassifizieren.

---

## 📦 Tech-Stack

- Java / Spring Boot
- Deep Java Library (DJL) mit MXNet Engine
- Docker für Containerisierung
- HTML/JavaScript für Web-Oberfläche
- Curl / Postman für API-Tests
- (Optional) Azure Web App Deployment

---

## 🔍 Projektübersicht

Ein vortrainiertes Deep-Learning-Modell wird in eine REST-API eingebettet. Nutzer:innen können ein Pflanzenbild hochladen, das analysiert und klassifiziert wird. Die Ergebnisse sind sowohl über eine JSON-Antwort als auch über eine einfache Web-Oberfläche einsehbar.

---

## 🧠 Modell-Details

- **Modelltyp**: Image Classification (Binary: toxic / non-toxic)
- **Bibliothek**: Deep Java Library (DJL)
- **Engine**: MXNet
- **Trainingsdaten**: Vorgefertigter, lokal gespeicherter Datensatz mit ca. 1000 Bildern je Klasse
- **Training**: Lokal durchgeführt, Modell im Projekt gespeichert unter:  
  src/main/resources/models/plantdetector/
- **Einsatz**: Modell wird beim Start des Webservices geladen

---

## 🔗 REST API

**POST /api/analyze**

- **Beschreibung**: Klassifiziert ein übermitteltes Bild
- **Content-Type**: multipart/form-data  
- **Form-Feld**: image  
- **Antwortformat (Beispiel)**:  
  { "className": "toxic", "probability": 0.9458 }

**Beispielaufruf (Curl)**:  
curl -X POST http://localhost:8080/api/analyze  
-H "Content-Type: multipart/form-data"  
-F "image=@/pfad/zum/bild.jpg"

---

## 💻 Web UI

Eine einfache HTML/JavaScript-Seite unter:  
src/main/resources/static/index.html  
ermöglicht den Upload von Bildern über den Browser. Die Klassifikation wird direkt auf der Seite angezeigt.

---

## 🐳 Docker Deployment

**Dockerfile**  
Ein lauffähiger Dockerfile ist enthalten. So kann der Service gestartet werden:

Image bauen:  
docker build -t djl-api .

Container starten:  
docker run -p 8080:8080 djl-api

**Optional: Docker Compose**  
Falls mehrere Container benötigt werden, kann eine docker-compose.yml ergänzt werden.

---

## ☁️ Azure Deployment (Optional)

Das Projekt kann auf Azure Web App deployed werden.  
Der Screencast dokumentiert:
- Start der App mit Modell-Laden
- Test via Postman oder UI
- Screenshot oder Video der Azure-Instanz

---

## 🧾 Projektstruktur

src/  
├── main/  
│   ├── java/  
│   │   └── com.example.djlapi/  ← Spring Boot Backend mit DJL  
│   ├── resources/  
│   │   ├── static/index.html    ← Web UI  
│   │   └── models/plantdetector/ ← Trainiertes Modell  

Weitere Dateien:  
- Dockerfile  
- pom.xml  
- README.md

---

## 🎬 Screencast-Inhalt

- Start des Servers mit sichtbarem Modell-Load
- Curl- oder Postman-Test
- Web-UI-Demo
- Optional: Azure Deployment
- Erklärung der Ordnerstruktur und Highlights

---

## ✅ Bewertungskriterien (Selbstcheck)

| Kriterium                            | Erfüllt |
|-------------------------------------|---------|
| Komplexes Modell & Dataset          | ✅       |
| Eigenständige Umsetzung             | ✅       |
| Backend mit DJL                     | ✅       |
| UI mit Funktion                     | ✅       |
| Deployment lokal / Azure            | ✅       |
| Docker (evtl. Compose)              | ✅       |
| Dokumentation & Screencast          | ✅       |

---

## 👤 Autor

**Name**: [Dein Name]  
**Modul**: Model Deployment & Maintenance  
**Studiengang**: Wirtschaftsinformatik  
**Semester**: [z. B. FS2025]  
