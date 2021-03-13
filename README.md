# Obiektowe-projekt-symulacja

Animacja została wykonana w Java fx, w tym celu założony został projekt gradlowy.

Projekt przedstawia uproszczoną symulację mechanizmu ewolucji zwierząt. Zwierzęta posiadają pewną ilość energii, którą zużywają na ruch po ograniczonej mapie lub rozmnażanie. Energia może być uzyskana poprzez zjedzenie rośliny, które generują się na mapie w określony sposób. Występują 2 osobne sfery generowania roślin: jungla oraz step otaczający jungle. W każdym dniu generowana jest dokładnie jedna roślina na stepie oraz jedna w jungli. Ponadto każde zwierzę ma swój genotyp, na podstawie którego generowany jest ruch zwierzaka. Genotyp jest przekazywany podczas reprodukcji. Symulacja zawiera wizualizację wykonaną w Java FX oraz pozwala na śledzenie statystyk.

Jedna epoka (dzień) składa się z kilku kroków:
1. Usunięcia martwych zwierząt na mapce
2. Skręt i przemieszczenie każdego zwierzęcia,
3. Jedzenie (roślina jest zjadana przez zwierzę posiadające najwięcej energii lub kilka najsilniejszych zwierząt, jeśli więcej niż jedno posiada taką samą, największą energię;  takim przypadku energia rośliny jest dzielona),
4. Rozmnażanie zwierząt (rozmnażają się zawsze dwa zwierzęta o najwyższej energii na danym polu; jeśli występuje więcej zwierząt o tej samej energii, wybór jest losowy),
5. Dodanie nowych roślin do mapy.

Po zatrzymaniu stosownej mapy (guziki stop1/stop2 lub stop zatrzymujący całą symulację) można wybrać zwierzaka klikając na niego (wyświetli się wtedy jego genotyp w okienku opisanym "Wybrany zwierzak" oraz zwierzak zmieni na stałe kolor swój na niebieski). Następnie należy wpisać w okienko, które w tym momencie powinno się pojawić, numer epoki, dla której chcemy znać szczegółowe informacje. Informacje te pojawią się w momencie, gdy na danej mapce będzie dokładnie taka epoka.

Jest również opcja showAnimals pokazująca zwierzęta, które mają jeden z 3 dominujących genów na całej mapce.

Klasy zostały podzielone na specjalne pakiety, co powinno pomóc w interpretacji poszczególnych funkcjonalności.

Projekt został zrealizowany w ramach programu studiów.




























