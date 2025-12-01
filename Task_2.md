# Zadanie: Implementacja logiki kontrolera MainController

Twoim zadaniem jest uzupełnienie metody `chat()` w klasie
`MainController` tak, aby poprawnie przygotowywała model danych dla
widoku *index* oraz pobierała informacje o zalogowanym użytkowniku i
jego wiadomościach.

Poniżej opis każdego kroku, który należy wykonać.

------------------------------------------------------------------------

## 1. Pobierz nazwę zalogowanego użytkownika

Użyj Spring Security, aby pobrać nazwę aktualnie zalogowanego
użytkownika:

``` java
String loggedUserName = SecurityContextHolder.getContext().getAuthentication().getName();
```

**Dlaczego:** Kontroler powinien wiedzieć, który użytkownik jest
aktualnie uwierzytelniony, aby wyświetlić jego wiadomości.

------------------------------------------------------------------------

## 2. Utwórz mapę modelu

Stwórz pustą mapę, która będzie przekazana do widoku:

``` java
var model = new HashMap<String, Object>();
```

**Dlaczego:** `ModelAndView` przyjmuje mapę danych, które będą widoczne
w szablonie HTML.

------------------------------------------------------------------------

## 3. Dodaj nazwę użytkownika do modelu

Dodaj klucz **"loggedUserName"** oraz jego wartość:

``` java
model.put("loggedUserName", loggedUserName);
```

**Dlaczego:** Widok może chcieć wyświetlić nazwę aktualnego użytkownika
(np. w nagłówku lub opisie czatu).

------------------------------------------------------------------------

## 4. Pobierz listę wiadomości użytkownika

Użyj serwisu `MessageService`, aby pobrać maksymalnie 100 ostatnich
wiadomości bieżącego użytkownika:

``` java
var messages = messageService.lastForActiveUser(100, loggedUserName);
```

**Dlaczego:** Widok czatu potrzebuje listy wiadomości przypisanych do
bieżącego użytkownika.

------------------------------------------------------------------------

## 5. Dodaj wiadomości do modelu

Dodaj klucz **"messages"** oraz pobraną listę:

``` java
model.put("messages", messages);
```

**Dlaczego:** Widok będzie renderował listę wiadomości w interfejsie
czatu.

------------------------------------------------------------------------

## 6. Zwróć obiekt ModelAndView

Zbuduj pełny widok:

``` java
return new ModelAndView("index", model);
```

**Dlaczego:** Widok `index` otrzyma wszystkie dane potrzebne do
wyświetlenia komponentów czatu.

------------------------------------------------------------------------
