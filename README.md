# movies-collection
Simple SpringBoot rest application.


Cechy aplikacji :
- pobranie listy filmów
- pobranie informacji o jednym filmie
- dodanie filmu do kolekcji

W celu sprawdzenia poprawności, jak i demonstracji działania aplikacji przy uruchomieniu dodawani są użytkownicy.

Applikacja obsługuje poniższe komendy (przykład dla użytkownika - rob) :
- pobranie listy filmów : curl -X GET http://localhost:8080/rob/movies/
- pobranie informacji o jednym filmie : curl -X GET http://localhost:8080/rob/movies/2
- dodanie filmu do kolekcji : curl -X POST http://localhost:8080/rob/movies/ 
-H "Content-Type: application/json" -d '{"title": "Movie Title","description": "Movie Description"}'

