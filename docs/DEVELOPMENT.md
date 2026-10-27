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
- [x] Sustituir el saludo de la plantilla por la bienvenida de la aplicación.
- [x] Verificar el nombre visible de la aplicación.
- [x] Validar la pantalla de bienvenida.
- [x] Incorporar el README y revisar los archivos que se versionan.
- [x] Realizar el primer commit y push.

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

- [ ] Preparar la distribución en Google Play cuando exista una versión apta.

## Base de dependencias

- [x] Incorporar Hilt y KSP.
- [x] Configurar ArdaApplication y MainActivity como puntos de integración de Hilt.
- [x] Añadir las dependencias de persistencia, imágenes, navegación,
  serialización, preferencias, multimedia y splash.
- [x] Comprobar compilación debug, tests unitarios y tarea de lint.
- [x] Ejecutar la aplicación después de incorporar las dependencias.
- [ ] Validar cada biblioteca mediante sus funcionalidades consumidoras.

### Incidencia durante la integración

La tarea transformDebugClassesWithAsm falló con
IllegalArgumentException: !directory.isDirectory.

Una limpieza y reconstrucción sin caché completó correctamente.
La compilación habitual posterior también pasó, reutilizando la caché
de configuración. No se modificaron versiones para resolver el fallo.
La causa exacta no quedó determinada.

## Integración continua

Se incorpora `.github/workflows/android-ci.yml` para comprobar el proyecto
en un entorno Linux independiente del entorno local de desarrollo.

Alcance:

- Compilación debug.
- Tests unitarios.
- Android Lint.
- Conservación de informes durante siete días.
- Ejecución por push a main, pull request y disparador manual.

Pendientes de verificación:

- [ ] Primera ejecución correcta en GitHub Actions.
- [ ] Comprobar los informes descargables.
- [ ] Añadir el badge al README.

Las pruebas instrumentadas y la distribución quedan para incrementos
posteriores.