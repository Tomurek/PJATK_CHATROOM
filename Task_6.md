# Zadanie: Przygotowanie Dockerfile dla aplikacji Spring Boot (ChatRoom)

Twoim zadaniem jest stworzenie pliku `Dockerfile`, który pozwoli uruchomić aplikację Spring Boot (plik JAR) w kontenerze Docker.

Poniżej masz opis krok po kroku, co należy zrobić i dlaczego.

---

## 1. Wybór obrazu bazowego

Na początku ustaw obraz bazowy Javy 17 w wersji JRE:

```dockerfile
FROM eclipse-temurin:17-jre
```

**Dlaczego:**  
Aplikacja jest uruchamiana jako plik JAR, więc potrzebuje środowiska uruchomieniowego Javy (JRE). Obraz `eclipse-temurin` to oficjalna dystrybucja OpenJDK.

---

## 2. Ustaw katalog roboczy

Ustaw katalog roboczy wewnątrz kontenera:

```dockerfile
WORKDIR /app
```

**Dlaczego:**  
Wszystkie polecenia wykonywane później (COPY, ENTRYPOINT) będą wykonywane względnie do `/app`.

---

## 3. Skopiuj plik JAR do kontenera

Skopiuj artefakt z katalogu `target` do obrazu i nazwij go `app.jar`:

```dockerfile
COPY target/ChatRoom-0.0.1-SNAPSHOT.jar app.jar
```

**Dlaczego:**  
Budujesz aplikację zwykle poleceniem `mvn package` / `mvn clean package`, które umieszcza JAR w folderze `target`. Kontener musi mieć ten plik w środku, żeby go uruchomić.

---

## 4. Skonfiguruj zmienną środowiskową `JAVA_OPTS`

Dodaj zmienną środowiskową z opcjami JVM:

```dockerfile
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"
```

**Dlaczego:**

- `-XX:+UseContainerSupport` — JVM świadomie wykorzystuje limity zasobów kontenera (CPU, RAM),
- `-XX:MaxRAMPercentage=75.0` — ogranicza maksymalną ilość pamięci używanej przez JVM do 75% dostępnej pamięci kontenera.

---

## 5. Wystaw port HTTP aplikacji

Udostępnij port, na którym działa aplikacja:

```dockerfile
EXPOSE 8080
```

**Dlaczego:**  
Domyślnie aplikacje Spring Boot nasłuchują na porcie `8080`. `EXPOSE` dokumentuje ten port i ułatwia mapowanie w `docker run` lub w Kubernetes.

---

## 6. Zdefiniuj polecenie startowe (ENTRYPOINT)

Najważniejsza część – polecenie, które zostanie uruchomione przy starcie kontenera:

```dockerfile
ENTRYPOINT ["sh","-c","java $JAVA_OPTS -Dspring.config.additional-location=optional:file:/config/ -jar app.jar"]
```

**Co tu się dzieje:**

- `sh -c "..."` – uruchamiamy powłokę, aby móc użyć zmiennej środowiskowej `$JAVA_OPTS`.
- `java $JAVA_OPTS` – uruchamiamy JVM z dodatkowymi opcjami.
- `-Dspring.config.additional-location=optional:file:/config/` – **najważniejsze**:
    - Spring Boot oprócz standardowych plików `application.properties` / `application.yml` będzie próbował wczytać dodatkowe pliki konfiguracyjne z katalogu `/config` w kontenerze,
    - `optional:` oznacza, że katalog może nie istnieć – aplikacja wciąż się uruchomi.
- `-jar app.jar` – uruchamiamy naszą aplikację.

Dzięki temu możesz podłączać zewnętrzną konfigurację (np. przez volume `/config`) bez zmiany obrazu Dockera.

---
