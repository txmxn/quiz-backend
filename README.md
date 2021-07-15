# Projektbeschreibung
Backend für ein Quiz in [Java](https://openjdk.java.net/) auf Basis von [Spring Boot](https://spring.io/projects/spring-boot).

# Vorbereitungen
Folgende Tools werden benötigt:
* [Java 15.0.2](https://openjdk.java.net/)
* [Maven 3.8.1](https://maven.apache.org/download.cgi)
* [Docker 20.10.7](https://www.docker.com/products/docker-desktop)

# Build
Im Basis Verzeichnis die Angaben zu den Pfaden für Java und Maven anpassen in der Datei *build.bat* anpassen und dann ausführen.

Dabei wird der Quellcode kompiliert, die Spring Boot runtime vorbereitet und anschließend die Anwendung in einem Docker-Container verpackt.
Das Tag des Containers lautet *tkayser/quiz-backend:1.0.0-SNAPSHOT*.

# IDE
Die Anwendung wird über die Klasse *de.tkayser.quiz.QuizApplication* gestartet.

# Einstellungen
Folgende Einstellungen sind möglich (Die Werte entsprechen den Default Werten): 
* SERVER_PORT=8081
* MAX_QUESTIONS=10
