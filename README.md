# DolarVzla Monitor 🇻🇪 - Android App

Aplicación nativa en Kotlin para monitoreo de tasas de cambio en Venezuela. Desarrollada con Jetpack Compose, Firebase y arquitectura MVVM.

## 📱 Características

### Monitoreo en Tiempo Real
- **Tasas de Cambio**: Obtiene precios del dólar desde BCV (Banco Central de Venezuela) y Binance P2P
- **Soporte Multi-Divisa**: Monitoreo de Dólar (USD) y Euro (EUR) con cálculo de tasas paralelas
- **Actualización Automática**: Widget de pantalla de inicio que se actualiza periódicamente

### Calculadora de Conversión
- Conversión rápida entre Dólares/Euros y Bolívares
- Cálculo simultáneo para tasas oficiales y paralelas
- Interfaz intuitiva con resultados en tiempo real

### Bloqueo Remoto
- Sistema de Kill Switch mediante Firebase Remote Config
- Control centralizado de acceso a la aplicación
- Pantalla de mantenimiento para versiones deshabilitadas

### Experiencia de Usuario
- **Diseño Moderno**: Interfaz Glassmorphism (Liquid Glass) con modo oscuro
- **Widget de Inicio**: Acceso rápido a las tasas desde la pantalla principal
- **Compartir Contenido**: Comparte tarjetas de cambio como imágenes
- **Historial de Mercado**: Visualización de cambios y tendencias
- **Segmentación**: Anuncios personalizados por ubicación geográfica

### Configuración y Personalización
- Modo oscuro/claro configurable
- Selección de ciudad para personalización
- Preferencias persistentes con DataStore

## 🛠️ Tecnologías

- **Lenguaje**: Kotlin
- **UI Framework**: Jetpack Compose
- **Arquitectura**: MVVM (Model-View-ViewModel)
- **Backend**: Firebase (Analytics, Remote Config)
- **Networking**: Retrofit, OkHttp, Jsoup
- **Coroutines**: Kotlin Coroutines para operaciones asíncronas
- **Persistencia**: DataStore Preferences, SharedPreferences
- **Widgets**: Jetpack Glance
- **Background Tasks**: WorkManager

## 📋 Requisitos

- Android 8.0 (API 26) o superior
- Conexión a Internet para obtener tasas actualizadas

## 🏗️ Estructura del Proyecto

```
app/src/main/java/com/yesdan/dolarczlamonitor/
├── data/
│   ├── api/              # Clientes de API (Binance)
│   ├── local/            # Almacenamiento local (DataStore, SharedPreferences)
│   ├── model/            # Modelos de datos
│   ├── remote/           # Firebase Remote Config
│   ├── repository/       # Repositorios de datos
│   └── scraper/          # Web scraping (BCV)
├── ui/
│   ├── components/       # Componentes reutilizables
│   ├── screen/           # Pantallas principales
│   ├── theme/            # Temas y estilos
│   └── viewmodel/        # ViewModels
├── utils/                # Utilidades (Analytics, Share)
├── widget/               # Widget de pantalla de inicio
└── worker/               # Trabajos en segundo plano
```

## 📝 Notas de Desarrollo

Esta aplicación utiliza:
- **Web Scraping** para obtener datos del BCV
- **APIs REST** para obtener datos de Binance P2P
- **Firebase Remote Config** para control remoto y configuración
- **Firebase Analytics** para métricas de uso

## ⚠️ Copyright

**Copyright (c) 2025 Yesdam_. All rights reserved.**

Este código fuente es solo para fines educativos y de visualización de portafolio. El uso, reproducción, distribución o modificación de este código está estrictamente prohibido sin el permiso explícito del autor.

Este proyecto es una demostración de habilidades de desarrollo Android y no está destinado para uso comercial sin autorización.
