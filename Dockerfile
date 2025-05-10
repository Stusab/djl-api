# ------------------ Stage 1: Build both projects --------------------
FROM maven:3.9.3-eclipse-temurin-17 AS builder

WORKDIR /workspace

# 1️⃣ Copy und build des Trainings-Moduls (djl-model)
COPY djl-model/pom.xml        djl-model/
COPY djl-model/src            djl-model/src
RUN mvn -f djl-model/pom.xml clean install -DskipTests

# 2️⃣ Copy und build des API-Moduls (djl-api)
COPY djl-api/pom.xml          djl-api/
COPY djl-api/src              djl-api/src
RUN mvn -f djl-api/pom.xml clean package -DskipTests

# ------------------ Stage 2: Runtime-Image --------------------
FROM eclipse-temurin:17-jre

WORKDIR /app

# 3️⃣ Jar aus der builder-Stage rein
COPY --from=builder /workspace/djl-api/target/djl-api-0.0.1-SNAPSHOT.jar app.jar

# 4️⃣ Model-Ordner direkt aus dem Host (über Kontext) ins Image
#    Dein Projektbaum: Projekt2/
#      ├─ djl-model/
#      │   └─ models/plantdetector/…
#      └─ djl-api/
COPY djl-model/models models

# 5️⃣ Standard-Entrypoint
ENTRYPOINT ["java", "-jar", "app.jar"]
