# Enciclopedia de Arda

Aplicación Android sobre el universo de J. R. R. Tolkien, desarrollada con
Kotlin y Jetpack Compose.

Este repositorio contiene la reconstrucción desde cero de una aplicación
personal anterior. El objetivo es avanzar mediante incrementos pequeños,
con decisiones técnicas documentadas, comprobaciones y commits que cierren
cada etapa.

## Estado del proyecto

En desarrollo inicial. Actualmente incluye la configuración base de Android
y una pantalla de bienvenida con Jetpack Compose.

El catálogo de contenido, la navegación y la persistencia se incorporarán
en los siguientes incrementos.

## Próximos objetivos

- Implementar el catálogo de personajes y sus fichas de detalle.
- Incorporar progresivamente otras secciones de la enciclopedia.
- Añadir favoritos y juegos.

## Tecnologías y configuración

| Componente | Configuración |
|---|---|
| Lenguaje de la aplicación | Kotlin |
| Interfaz | Jetpack Compose y Material 3 |
| Android Gradle Plugin | 9.4.1 |
| Gradle Wrapper | 9.6.0 |
| Plugin de compilación de Compose | 2.2.10 |
| Compose BOM | 2026.02.01 |
| Core KTX | 1.19.1 |
| Activity Compose | 1.13.0 |
| Lifecycle Runtime KTX | 2.11.0 |
| Minimum SDK | 29 |
| Compile SDK / Target SDK | 37 |
| Criterio de JVM del daemon de Gradle | Java 25 |
| Compatibilidad de compilación Java | Java 11 |
| Scripts de compilación | Kotlin DSL |

AGP proporciona soporte integrado para Kotlin. La versión declarada para
el plugin de Compose y el Kotlin incorporado en Gradle son conceptos
distintos de la versión efectiva del compilador de la aplicación.

Las dependencias se centralizan en `gradle/libs.versions.toml`.

## Requisitos

- Android Studio Rabbit 1 | 2026.2.1, utilizado para crear el proyecto.
- Android SDK Platform 37.
- JDK compatible con los criterios de Java 25 del proyecto.
- Dispositivo o emulador con API 29 o superior.
- Acceso a Internet para descargar inicialmente herramientas y dependencias.

El repositorio incluye Gradle Wrapper; no requiere instalar Gradle
globalmente.

## Ejecución local

1. Clonar el repositorio:

   ```bash
   git clone https://github.com/cgaraydev/enciclopedia-de-arda.git
   ```

2. Abrir la carpeta del proyecto en Android Studio.
3. Instalar los componentes del SDK que solicite el IDE.
4. Esperar a que finalice la sincronización de Gradle.
5. Seleccionar el módulo `app` y un dispositivo.
6. Ejecutar la aplicación mediante Run.

`local.properties` contiene la ubicación local del SDK y no se versiona.

## Compilación y comprobaciones

Desde PowerShell, en la raíz del proyecto:

```powershell
.\gradlew.bat --version
.\gradlew.bat :app:assembleDebug :app:testDebugUnitTest :app:lintDebug
```

Desde Linux o macOS:

```bash
./gradlew --version
./gradlew :app:assembleDebug :app:testDebugUnitTest :app:lintDebug
```

Si el script no tiene permiso de ejecución:

```bash
chmod +x gradlew
```

Artefactos e informes principales:

- APK debug: `app/build/outputs/apk/debug/app-debug.apk`
- Tests unitarios: `app/build/reports/tests/testDebugUnitTest/index.html`
- Lint: `app/build/reports/lint-results-debug.html`

Las pruebas instrumentadas requieren un dispositivo o emulador conectado:

```powershell
.\gradlew.bat :app:connectedDebugAndroidTest
```

Su ejecución es independiente de los tests unitarios.

## Estructura y decisiones

Actualmente existe un único módulo Android: `app`.

- `MainActivity.kt`: actividad de entrada y pantalla de bienvenida.
- `ui/theme/`: tema Compose generado inicialmente.
- `res/values/strings.xml`: textos de la aplicación.
- `src/test/`: pruebas unitarias locales.
- `src/androidTest/`: pruebas instrumentadas.

La organización de las funcionalidades y sus capas se incorporará conforme
existan casos concretos. Este incremento todavía no incluye repositorios,
ViewModels, inyección de dependencias ni fuentes de datos.

Se mantiene la configuración inicial de herramientas validada mediante
compilación, tests unitarios y ejecución manual de la plantilla.

