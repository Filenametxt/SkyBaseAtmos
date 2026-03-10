# SkyBaseAtmos
Applicazione Android per il monitoraggio meteo basata sulla posizione, sviluppata con **Jetpack Compose** e **Clean Architecture**.
## 🛠 Caratteristiche Tecniche
- **UI Modern**: Interfaccia interamente costruita con Jetpack Compose e Material 3.
- **Gestione Posizione**: Monitoraggio della posizione dell'utente (Fine & Coarse) tramite Google Play Services per dati meteo localizzati.
- **Mappe**: Integrazione di Google Maps (Maps-Compose) per visualizzazione geolocalizzata.
- **Networking**: Consumo di API REST tramite Retrofit e GSON.
- **Database Locale**: Persistenza dei dati offline gestita con Room.
- **Dependency Injection**: Architettura modulare basata su Hilt (Dagger).
- **Permessi**: Gestione fluida dei permessi Android tramite la libreria Accompanist.

## 📚 Stack Tecnologico
- **Linguaggio**: Kotlin
- **UI**: Jetpack Compose (BOM 2026.02.00)
- **Architettura**: MVVM + Clean Architecture
- **Data**: Room, Retrofit, Kotlin Coroutines & Flow
- **DI**: Hilt
- **Maps**: Google Maps SDK per Android

## ⚙️ Configurazione
Per avviare il progetto è necessario:
1. Clonare il repository.
2. Aggiungere una chiave API per Google Maps e OpenWeather nel file local.properties:
```Properties
  maps_api=YOUR_KEY
  OpenWeather_api=YOUR_KEY
```
3. Sincronizzare Gradle e avviare l'applicazione.
