# Obiektowe-projekt-symulacja


Projekt niestety nie spełnia następujących wymagań:
nie zczytuje parametrów z pliku oraz nie zapisuje statystyk do pliku.
Poza nimi, starałam się wszystko uwzględnić w kodzie programu, jednak oczywiście mogłam coś pominąć.

Animacja została wykonana w Java fx, w tym celu założony został projekt gradlowy.

Istotne elementy, które mogą nie być intuicyjne:
Niestety na początku zaprojektowałam wygląd, a potem uzupełniałam kolejne elementy, przez co napchane jest guzików i innych elementów w każde miejsce opakowania symulacji.
Górna mapka to mapka nr 1, dolna to mapka nr 2, wszystkie statystyki mają opisy 1/2 w zależności od tego, której mapki dotyczą.

Po zatrzymaniu stosownej mapy (guziki stop1/stop2 lub stop ogólny zatrzymujący całą symulację) można wybrać zwierzaka klikając na niego (wyświetli się wtedy jego genotyp w okienku opisanym "Wybrany zwierzak" oraz zwierzak zmieni na stałe kolor swój na niebieski). Następnie należy wpisać w okienko, które w tym momencie powinno się pojawić, numer epoki, dla której chcemy znać szczegółowe informacje. Informacje te pojawią się w momencie, gdy na danej mapce będzie dokładnie taka epoka.

Jest również opcja showAnimals - pokazuje na ładny niebieski kolor zwierzątka, które mają jako dominujący jeden z 3 dominujących genów na całej mapce.

Klasy zostały podzielone na specjalne pakiety, co powinno pomóc w interpretacji poszczególnych funkcjonalności.




























