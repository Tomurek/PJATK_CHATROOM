# Zadanie: Implementacja logiki odbioru wiadomości w `ChatKafkaListener`

Twoim zadaniem jest uzupełnienie metody `onMessage(MessageDto msg)` w
klasie `ChatKafkaListener`, aby poprawnie obsługiwała wiadomości
przychodzące z Apache Kafka, zapisywała je w systemie oraz wysyłała do
klientów przez WebSocket.

Poniżej przedstawiony jest dokładny opis kroków, które należy wykonać.

------------------------------------------------------------------------

## 1. Odbierz wiadomość z Kafki

Metoda `onMessage(MessageDto msg)` jest wywoływana automatycznie po
odebraniu wiadomości z tematu Kafka określonego w adnotacji
`@KafkaListener`.

Parametr:

``` java
MessageDto msg
```

zawiera treść odebranej wiadomości.

**Dlaczego:** Kafka dostarcza wiadomości do listenera, a metoda
`onMessage` jest odpowiednim miejscem na ich dalsze przetwarzanie.

------------------------------------------------------------------------

## 2. Zapisz i znormalizuj wiadomość

Wywołaj metodę:

``` java
messageService.addMessageWithNormalization(msg);
```

**Dlaczego:**\
Ta metoda:

-   normalizuje wiadomość (np. usuwa niepotrzebne znaki),
-   dodaje ją do historii czatu (np. w bazie danych).

Dzięki temu wszystkie wiadomości pojawiają się w historii i mają
ujednolicony format.

------------------------------------------------------------------------

## 3. Wyślij wiadomość do klientów WebSocket

Użyj komponentu `SimpMessagingTemplate`, aby wysłać wiadomość do
wszystkich subskrybentów kanału:

``` java
ws.convertAndSend("/topic/greetings", msg);
```

**Dlaczego:**\
Wysyłając wiadomość na **/topic/greetings**, wszystkie połączone
aplikacje WebSocket, które subskrybują ten kanał, natychmiast wyświetlą
nową wiadomość.

------------------------------------------------------------------------

## Podsumowanie działania

Kompletna metoda powinna:

1.  odebrać wiadomość z Kafki,\
2.  dodać ją do historii czatu z normalizacją,\
3.  przesłać ją dalej do klientów WebSocket.

Dzięki temu tworzy spójny przepływ danych: **Kafka → backend → WebSocket
→ frontend**.

------------------------------------------------------------------------
