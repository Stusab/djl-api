import streamlit as st
import requests

# Der URL eurer Spring-Boot-API (z.B. https://mein-api.herokuapp.com)
API_URL = st.secrets["API_URL"]

st.set_page_config(page_title="Toxische vs. ungiftige Pflanzen")
st.title("Toxische vs. ungiftige Pflanzen")

uploaded_file = st.file_uploader("Bild hochladen", type=["jpg", "png", "jpeg"])
if uploaded_file:
    files = {"file": uploaded_file.getvalue()}
    with st.spinner("Analysiere..."):
        resp = requests.post(f"{API_URL}/api/analyze", files={"file": uploaded_file})
    if resp.ok:
        results = resp.json()
        st.json(results)
    else:
        st.error(f"Fehler beim Analysieren: {resp.status_code}")
