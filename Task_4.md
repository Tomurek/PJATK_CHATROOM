# Zadanie: Implementacja logiki serwisu `MessageService`

Twoim zadaniem jest uzupełnienie wszystkich metod w klasie
`MessageService`, które odpowiadają za normalizację wiadomości,
zarządzanie historią oraz przekształcanie ich na model widoku.

Poniżej znajdziesz szczegółowy opis kroków do wykonania w każdej
metodzie.

------------------------------------------------------------------------

# 1. `addMessageWithNormalization(MessageDto message)`

## Kroki:

### 1.1 Sprawdź, czy `message` jest null

Jeśli tak --- zakończ metodę.

------------------------------------------------------------------------

### 1.2 Pobierz autora

`String author = message.author();`

------------------------------------------------------------------------

### 1.3 Znormalizuj autora

`author = normalize(author);`

------------------------------------------------------------------------

### 1.4 Pobierz treść wiadomości

`String content = message.content();`

------------------------------------------------------------------------

### 1.5 Przytnij treść

`content = safeTrim(content);`

------------------------------------------------------------------------

### 1.6 Zabezpiecz dostęp do historii

W bloku `synchronized(history)`:

-   jeśli historia przekracza limit → `history.removeFirst()`
-   dodaj nową wiadomość:\
    `history.addLast(new MessageDto(author, content));`

------------------------------------------------------------------------

# 2. `lastForActiveUser(int limit, String currentUser)`

## Kroki:

### 2.1 Pobierz ostatnie wiadomości DTO

`List<MessageDto> dtos = lastMessagesForLimit(limit);`

------------------------------------------------------------------------

### 2.2 Zamień każde DTO na obiekt widoku

`Message vm = toViewModel(m, currentUser);`

------------------------------------------------------------------------

### 2.3 Zwróć niemodyfikowalną listę

`return List.copyOf(list);`

------------------------------------------------------------------------

# 3. `lastMessagesForLimit(int limit)`

## Kroki:

### 3.1 Zabezpiecz dostęp `synchronized(history)`

------------------------------------------------------------------------

### 3.2 Pobierz rozmiar historii

`int size = history.size();`

------------------------------------------------------------------------

### 3.3 Wyznacz liczbę wiadomości do zwrócenia

`int number = min(max(0, limit), size);`

------------------------------------------------------------------------

### 3.4 Jeśli `number == 0` → zwróć pustą listę

------------------------------------------------------------------------

### 3.5 Utwórz migawkę historii

`snapshot = history.toArray(...)`

------------------------------------------------------------------------

### 3.6 Utwórz wynikową listę i skopiuj ostatnie `number` elementów

------------------------------------------------------------------------

### 3.7 Zwróć wynikową listę

------------------------------------------------------------------------

# 4. `toViewModel(MessageDto m, String currentUser)`

## Kroki:

### 4.1 Ustal, czy wiadomość należy do aktualnego użytkownika

`boolean mine = isMineMessage(m, currentUser);`

------------------------------------------------------------------------

### 4.2 Utwórz obiekt widoku

`return new Message(m.author(), m.content(), mine);`

------------------------------------------------------------------------

# 5. `isMineMessage(MessageDto m, String currentUser)`

## Kroki:

### 5.1 Jeśli autor jest null → zwróć false

### 5.2 W przeciwnym razie:

`return m.author().equalsIgnoreCase(currentUser);`

------------------------------------------------------------------------

# 6. `normalize(String s)`

## Kroki:

### 6.1 Jeśli string jest pusty lub null → zwróć `ANON`

### 6.2 W przeciwnym razie → `return s.trim();`

------------------------------------------------------------------------

# 7. `safeTrim(String s)`

## Kroki:

### 7.1 Jeśli s == null → zwróć `EMPTY_STRING`

### 7.2 W przeciwnym razie → `return s.trim();`
