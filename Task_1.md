# Opis zadania: Implementacja podstawowej konfiguracji Spring Security

Twoim zadaniem jest uzupełnienie klasy `SecurityConfig` tak, aby
poprawnie konfigurowała:

-   pamięciowy magazyn użytkowników (`InMemoryUserDetailsManager`),
-   publikowanie eventów logowania
    (`DefaultAuthenticationEventPublisher`),
-   filtr bezpieczeństwa (`SecurityFilterChain`).

Poniżej opis każdego kroku, który należy wykonać.

------------------------------------------------------------------------

## 1. Konfiguracja `InMemoryUserDetailsManager`

W metodzie `inMemoryUserDetailsManager()` wykonaj następujące kroki:

### 1.1 Wygeneruj hasło

Użyj podanego `BCryptPasswordEncoder`, aby stworzyć hasło, które
zostanie przypisane wszystkim użytkownikom.

Utwórz zmienną:

``` java
String generatedPassword = passwordEncoder.encode("test");
```

**Dlaczego:** Spring Security wymaga kodowanych haseł. Dzięki temu nie
przechowujesz hasła w czystej postaci.

------------------------------------------------------------------------

### 1.2 Utwórz obiekt `InMemoryUserDetailsManager`

Stwórz nową instancję:

``` java
InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
```

**Dlaczego:** Jest to najprostszy sposób przechowywania użytkowników --
wszystko dzieje się w pamięci, bez bazy danych.

------------------------------------------------------------------------

### 1.3 Dodaj użytkowników

Dodaj kilku użytkowników, np.: *Michał, Agnieszka, Krzysztof, Laura,
Kate*.

Dla każdego stwórz obiekt `UserDetails`, np.:

``` java
User.withUsername("Michal")
    .password(generatedPassword)
    .roles("USER")
    .build();
```

Następnie dodaj ich do managera:

``` java
manager.createUser(...);
```

**Dlaczego:** Chodzi o naukę zarządzania użytkownikami oraz rolami w
Spring Security.

------------------------------------------------------------------------

### 1.4 Zwróć przygotowany manager

Na końcu zwróć obiekt managera:

``` java
return manager;
```

------------------------------------------------------------------------

## 2. Konfiguracja `AuthenticationEventPublisher`

W metodzie `defaultAuthenticationEventPublisher()`:

### 2.1 Utwórz obiekt `DefaultAuthenticationEventPublisher`

Stwórz nowy obiekt:

``` java
return new DefaultAuthenticationEventPublisher(delegate);
```

**Dlaczego:** Pozwala to publikować eventy związane z logowaniem --- np.
sukces lub porażka logowania.

------------------------------------------------------------------------

## 3. Konfiguracja `SecurityFilterChain`

W metodzie `filterChain(HttpSecurity http)`:

### 3.1 Skonfiguruj dostęp do endpointów

Użyj:

``` java
http.authorizeHttpRequests(auth -> auth
    .requestMatchers("/login").permitAll()
    .requestMatchers("/images/image.png").permitAll()
    .anyRequest().authenticated()
);
```

**Dlaczego:**

-   Strona logowania musi być publiczna.
-   Plik graficzny ma być dostępny bez logowania (np. logo).
-   Wszystko inne wymaga autoryzacji.

------------------------------------------------------------------------

### 3.2 Skonfiguruj logowanie formularzowe

Ustaw własną stronę logowania:

``` java
http.formLogin(form -> form
    .loginPage("/login")
);
```

**Dlaczego:** Użytkownik ma korzystać z własnego HTML, a nie domyślnego
formularza Spring Security.

------------------------------------------------------------------------

### 3.3 Zbuduj i zwróć `SecurityFilterChain`

``` java
return http.build();
```

**Dlaczego:** `http.build()` finalizuje konfigurację i zwraca gotowy
łańcuch filtrów bezpieczeństwa.
