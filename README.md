# Integrantes del grupo:

- **Delfina Ciccale**
- **Helena Cusworth**

# Sistema de Reservas: Club Social "Los Álamos"

Sistema para administrar la reserva de instalaciones y servicios del club (quinchos, salones, canchas, parrillas, etc.) por parte de sus socios.

## Stack

- **Lenguaje:** Java
- **IDE:** Eclipse

## Requisitos

- JDK 17 o superior
- Eclipse IDE for Java Developers (que incluye soporte para Maven)

## Ejecución en Eclipse

1. File -> Import -> Maven -> Existing Maven Projects.
2. En Root Directory, seleccionar la carpeta raíz del proyecto y presionar Finish.
3. Abrir `src/main/java/clublosalamos/Main.java`.
4. Clic derecho -> Run As -> Java Application.
5. Interactuar con el menú desde la pestaña Console.

Si Eclipse marca error de versión de Java: clic derecho sobre el proyecto -> Properties -> Java Compiler y configurar el nivel de compatibilidad en 17 o superior.

## Documentación

- [`docs/descripcion-textual.md`](docs/descripcion-textual.md) - enunciado del sistema
- [`docs/arquitectura/diagrama-clases.puml`](docs/arquitectura/diagrama-clases.puml) - diagrama de clases (PlantUML)
- [`docs/casos-de-uso/diagrama-casos-uso.puml`](docs/casos-de-uso/diagrama-casos-uso.puml) - diagrama de casos de uso (PlantUML)

## Diagramas

### Diagrama de clases

![Diagrama de clases](https://www.plantuml.com/plantuml/proxy?src=https://raw.githubusercontent.com/h3l333/proyecto-los-alamos/main/docs/arquitectura/diagrama-clases.puml&fmt=svg&cache=78c56879)

### Diagrama de casos de uso

![Diagrama de casos de uso](https://www.plantuml.com/plantuml/proxy?src=https://raw.githubusercontent.com/h3l333/proyecto-los-alamos/main/docs/casos-de-uso/diagrama-casos-uso.puml&fmt=svg&cache=69cc299b)
