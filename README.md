# BMI Manager - Mini aplikacja do zarządzania BMI

Aplikacja webowa do zarządzania indeksem masy ciała (BMI), śleddzenia wagi i dzielenia się osiągnięciami zdrowotnymi z całą społecznością.


## Funkcjonalności

### Dla Użytkowników
- **Rejestracja i Logowanie** - Prosty system autentykacji z Basic Authentication
- **Zarządzanie Profilem** - Edycja danych osobowych, wzrostu i motywujących cytatów
- **Śledzenie Wagi** - Dodawanie i usuwanie wpisów wagi w ciągu czasu
- **Statystyki BMI** - Automatyczne obliczanie BMI na podstawie wagi i wzrostu
- **Wykresy** - Wizualizacja zmian wagi i BMI
- **Profile Publiczne** - Opcja udostępnienia swoich osiągnięć innym użytkownikom
- **Motywujący Cytaty** - Dodawanie cytatów w profilu jako inspiracja
- **Osiągnięcia** - Udostępnianie swoich celów i sukcesów

### Dla Administratorów
- **Dashboard** - Przegląd wszystkich użytkowników i ich statystyk BMI
- **Zarządzanie Użytkownikami** - Podgląd szczegółów każdego użytkownika
- **Wykresy Porównawcze** - Analiza BMI całej populacji użytkowników
- **Historia Wagi** - Pełny dostęp do historii wagi wszystkich użytkowników

### Strony Publiczne
- **Strona Główna** - Informacje o aplikacji i problemie otyłości na świecie
- **Profile Publiczne** - Przeglądanie i inspirowanie się osiągnięciami innych
- **Wykresy Publiczne** - Podgląd postępu użytkowników, którzy udostępnili swoje profile

## Wymagania

- **Java 17+** - Środowisko uruchomieniowe
- **Maven 3.6+** - Menadżer zależności
- **Przeglądarka** - Chrome, Firefox, Safari lub Edge

## Instalacja

### 1. Klonowanie Repozytorium
```bash
git clone https://github.com/tomaszgadek/tijo-bmi.git
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

### Uruchomienie z IDE (IntelliJ IDEA, Eclipse)
1. Otwórz projekt
2. Uruchom klasę `BmiManagerApplication` jako Java Application

### Dostęp do Aplikacji
Otwórz przeglądarkę i wejdź na:
```
http://localhost:8080
```

## Dane Testowe

Aplikacja automatycznie ładuje dane testowe przy starcie. Dostępne konta:

### Konto Administratora
- **Login:** `admin`
- **Hasło:** `admin123`
- **Dostęp:** Dashboard administracyjny

### Konto Użytkownika Demo
- **Login:** `demo`
- **Hasło:** `demo123`
- **Dostęp:** Profil publiczny z przykładowymi danymi

## Architektura

### Wzorzec Fasada (Facade Pattern)
Aplikacja implementuje wzorzec Fasada w warstwie serwisów:
- `BMIFacadeService` - Agreguje logikę biznesową
- `UserService` - Zarządzanie użytkownikami
- `WeightService` - Zarządzanie pomiarami wagi

## Technologie

### Backend
- **Spring Boot 3.1.5** - Framework webowy
- **Spring Data JPA** - Dostęp do bazy danych
- **Spring Security** - Autentykacja i autoryzacja
- **H2 Database** - Baza danych w pamięci
- **Hibernate** - ORM

### Frontend
- **Thymeleaf** - Szablonowanie HTML
- **Pure.css** - Lekki framework CSS
- **Chart.js** - Wykresy

## Baza Danych

### H2 Console
Podczas uruchamiania aplikacji, H2 Console jest dostępna pod adresem:
```
http://localhost:8080/h2-console
```

**Parametry połączenia:**
- **JDBC URL:** `jdbc:h2:mem:bmidb`
- **Username:** `admin`
- **Password:** `admin`

### Tabele
- **users** - Przechowuje dane użytkowników
- **weight_records** - Przechowuje pomiary wagi i daty

## Bezpieczeństwo

- **Basic Authentication** - Podstawowy system logowania
- **Password Encoding** - Hasła kodowane z BCrypt
- **CSRF Protection** - Wyłączone
- **Role-Based Access Control** - Dostęp do panelu admina ograniczony do roli ADMIN
- **Session Management** - Obsługa sesji użytkownika

### Kategorie BMI
- **< 18.5** - Niedowaga
- **18.5 - 24.9** - Prawidłowa waga
- **25.0 - 29.9** - Nadwaga
- **≥ 30.0** - Otyłość

## Licencja

Projekt jest dostępny na licencji MIT.

## Autor

Tomasz Gądek

## Planowane poprawki w kodzie

+ Poprawki związane z routingiem na stronie.
+ Zmiana frameworków frontendowych.
+ Dopracowanie dokumentacji.
+ Poprawne zastosowanie wzorca projektowego **Fasada**.
+ Pozbycie się niepotrzebnego nadmiarowego kodu.
+ Usunięcie niepotrzebnych komentarzy.
+ Dopracowanie UI (jeszcze nie działa dobrze).
+ Modele LLM tworzą działający kod, ale wymagają jeszcze refaktoryzacji - pasuje przeanalizować cały kod.

