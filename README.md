# Enciclopedia de Arda

Aplicación Android sobre el universo de J. R. R. Tolkien, desarrollada con
Kotlin y Jetpack Compose.

Este repositorio contiene la reconstrucción desde cero de una aplicación
personal anterior. El objetivo es avanzar mediante incrementos pequeños,
con decisiones técnicas documentadas, comprobaciones y commits que cierren
cada etapa.

## Estado actual

Proyecto en fase inicial.

La aplicación dispone de una pantalla de bienvenida. Todavía no incluye
contenido de la enciclopedia, navegación entre secciones ni persistencia.

## Objetivos

- Construir una aplicación Android mantenible y fácil de comprender.
- Aplicar Kotlin y Jetpack Compose en funcionalidades completas.
- Incorporar pruebas acordes con el comportamiento implementado.
- Documentar decisiones, limitaciones y avances.
- Utilizar el proyecto como aprendizaje y parte del portafolio profesional.

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

## Forma de trabajo

El desarrollo se realiza en Android Studio y se versiona con Git y GitHub.

Se utiliza asistencia de IA para discutir decisiones, revisar código y
preparar implementaciones y documentación. Los cambios se revisan y
comprueban antes de incorporarlos al repositorio.

Cada incremento debe:

1. Tener un objetivo concreto.
2. Implementar únicamente el alcance acordado.
3. Ejecutar las comprobaciones pertinentes.
4. Actualizar este README cuando cambie el estado del proyecto.
5. Cerrarse con un commit descriptivo.

## Limitaciones actuales

- Sin catálogo de personajes ni otras secciones de contenido.
- Sin navegación funcional.
- Sin almacenamiento de favoritos.
- Sin juegos integrados.
- Las pruebas iniciales proceden de la plantilla y no validan funcionalidades
  de la enciclopedia.
- Sin validación de una versión release destinada a distribución.
- Sin publicación de esta reconstrucción en Google Play.

## Seguimiento y roadmap

Esta sección se actualizará durante el desarrollo. Un elemento se marca
como completado cuando esté implementado y comprobado.

### Base del proyecto

- [x] Crear el repositorio público.
- [x] Crear el proyecto Android con Kotlin y Compose.
- [x] Comprobar la compilación debug de la plantilla.
- [x] Ejecutar los tests unitarios de la plantilla y la tarea de lint.
- [x] Ejecutar manualmente la plantilla.
- [ ] Sustituir el saludo de la plantilla por la bienvenida de la aplicación.
- [ ] Verificar el nombre visible de la aplicación.
- [ ] Validar la pantalla de bienvenida.
- [ ] Incorporar el README y revisar los archivos que se versionan.
- [ ] Realizar el primer commit y push.

### Próximo incremento

- [ ] Definir el alcance de la primera funcionalidad: personajes.
- [ ] Acordar su modelo, fuente de datos y organización.
- [ ] Implementar y comprobar el primer flujo funcional.

### Funcionalidades pendientes de concretar

- [ ] Otras secciones de la enciclopedia.
- [ ] Favoritos.
- [ ] Calendario.
- [ ] Quiz.
- [ ] Juego de memoria.

### Ideas por evaluar

Las ideas de esta sección no constituyen compromisos de implementación.

- [ ] Añadir integración continua para compilación y comprobaciones.
- [ ] Preparar la distribución en Google Play cuando exista una versión apta.