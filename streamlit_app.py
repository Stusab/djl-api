import streamlit as st
import requests
API_URL = https://djl-api.onrender.com

# Seite konfigurieren
st.set_page_config(
    page_title="🌿 Pflanzen-Toxizität analysieren",
    page_icon="🌿",
    layout="centered"
)

# Titel
st.markdown("<h1 style='text-align: center; color: #2c3e50;'>🌿 Pflanzen-Toxizität analysieren</h1>", unsafe_allow_html=True)

# Datei-Upload
uploaded_file = st.file_uploader("Bitte ein Pflanzenbild hochladen:", type=["jpg", "jpeg", "png"])

# Analyse durchführen
if uploaded_file:
    with st.spinner("🔍 Analyse läuft..."):
        try:
            files = {"file": uploaded_file.getvalue()}
            response = requests.post(f"{st.secrets['API_URL']}/api/analyze", files={"file": uploaded_file})
            if response.ok:
                result = response.json()
                class_label = result["className"]
                prob = float(result["probability"]) * 100
                st.success(f"🌱 **Ergebnis:** {class_label.upper()}")
                st.info(f"📊 **Wahrscheinlichkeit:** {prob:.2f}%")
            else:
                st.error(f"❌ Fehler bei der Analyse (Status {response.status_code})")
        except Exception as e:
            st.error(f"⚠️ Fehler: {e}")
else:
    st.markdown("⬆️ Lade ein Bild hoch, um mit der Analyse zu beginnen.")

