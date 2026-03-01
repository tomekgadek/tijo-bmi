# BMI Manager - Mini aplikacja do zarządzania BMI

Aplikacja webowa do zarządzania indeksem masy ciała (BMI), śleddzenia wagi i dzielenia się osiągnięciami zdrowotnymi z całą społecznością.


## Funkcjonalności

### Dla Użytkowników
- **Rejestracja i Logowanie** - System uwierzytelniania z Basic Authentication.
- **Zarządzanie Profilem** - Edycja danych osobowych, wzrostu, celów i motywujących cytatów.
- **Śledzenie Wagi** - Dodawanie, edytowanie i usuwanie wpisów wagi.
- **Statystyki BMI** - Automatyczne obliczanie BMI na podstawie wagi i wzrostu.
- **Historia i Paginacja** - Przeglądanie historii pomiarów z wygodną paginacją.
- **Wykresy** - Interaktywna wizualizacja zmian wagi (Chart.js).
- **Profile Publiczne** - Opcja udostępnienia swojego postępu całej społeczności.
- **Internacjonalizacja (i18n)** - Pełne wsparcie dla języka polskiego i angielskiego.

### Dla Administratorów
- **Dashboard** - Przegląd aktywności użytkowników i statystyk systemu.
- **Zarządzanie Użytkownikami** - Szczegółowy podgląd profilu każdego użytkownika.
- **Statusy Kont** - Możliwość blokowania i odblokowywania użytkowników.

### Strony Publiczne
- **Strona Główna** - Nowoczesny landing page zachęcający do dbania o zdrowie.
- **Lista Profili** - Przeglądanie publicznych profili innych użytkowników.
- **Widok Profilu** - Dostęp do wykresów i historii użytkowników, którzy zdecydowali się na upublicznienie danych.

## Wymagania

- **Java 17+**
- **Maven 3.6+**
- **Przeglądarka internetowa**

## Instalacja

### 1. Klonowanie Repozytorium
```bash
git clone https://github.com/tomekgadek/tijo-bmi.git
cd tijo-bmi
```

### 2. Budowanie Projektu
```bash
mvn clean install
```

## Uruchomienie

### Uruchomienie z Linii Poleceń
```bash
mvn spring-boot:run
```

### Dostęp do Aplikacji
Otwórz przeglądarkę i wejdź na: `http://localhost:8080`

## Dane Testowe

Aplikacja automatycznie ładuje dane testowe przy starcie. Dostępne konta:

- **Admin:** `admin` / `admin123`
- **Użytkownik Demo:** `demo` / `demo123`
- **Użytkownik Tomek:** `tomek` / `tomek123`

## Architektura

### Wzorzec Fasada (Facade Pattern)
Aplikacja implementuje wzorzec **Fasada** w warstwie serwisów, agregując logikę biznesową i ukrywając złożoność podsystemów przed kontrolerami:
- `BMIFacadeService` - Główny punkt styku dla operacji związanych z BMI i statystykami.
- `UserService` - Zarządzanie kontami i profilami.
- `WeightService` - Logika pomiarów i obliczeń.


## Technologie

### Backend
- **Spring Boot 3.1.5**
- **Spring Security** - Uwierzytelnianie i autoryzacja (form-login, basic-auth).
- **Spring Data JPA** - Komunikacja z bazą danych (H2/Hibernate).
- **Spring MessageSource** - Obsługa wielu języków (i18n).
- **Lombok** - Redukcja kodu boilerplate.

### Frontend
- **Thymeleaf** - Silnik szablonów.
- **Pure.css** - Lekki szkielet CSS.
- **Chart.js** - Wizualizacje.
- **Custom CSS** - Dedykowane style dla nowoczesnego wyglądu.

## Bezpieczeństwo

- **BCrypt** - Hasła są bezpiecznie hashowane.
- **RBAC** - Dostęp do zasobów administracyjnych ograniczony rolami (ROLE_ADMIN).
- **Session Management** - Zarządzanie sesjami użytkowników.

## Kategorie BMI (Zgodnie z WHO)

- **< 18.5** - Niedowaga
- **18.5 - 24.9** - Prawidłowa waga
- **25.0 - 29.9** - Nadwaga
- **≥ 30.0** - Otyłość

## Planowane poprawki i rozwój

+ Wdrożenie testów jednostkowych i integracyjnych.
+ Obsługa wgrywania zdjęć profilowych.
+ Dodanie powiadomień o przypomnieniu o ważeniu.
+ Eksport danych do formatu CSV/PDF.
+ Optymalizacja zapytań SQL.
+ Poprawa obsługi błędów w warstwie UI (lepsze komunikaty walidacyjne).
+ Refaktoryzacja brakujących fragmentów kodu opisanych jako TODO.

## Licencja

Projekt jest dostępny na licencji MIT.

## Autor

Tomasz Gadek


