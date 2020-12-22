package View;

import AnimalElements.Animal;
import MapElements.IMapElement;
import ObserverInterface.IObserver;
import Statistics.SingleAnimalStatistics;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

//klasa do rysowania elementow na mapie

public class ViewController implements IObserver {

        private final static int size = 8;

        @FXML
        Pane world;

        //statystyki dla jednego zwierzaka
        @FXML
        TextField wybranyZwierzak;

        @FXML
        Label wybranyZwierzakLabel;

        @FXML
        TextField liczbaEpok;

        @FXML
        Label liczbaEpokLabel;

        @FXML
        TextField liczbaDzieci;

        @FXML
        Label liczbaDzieciLabel;

        @FXML
        TextField liczbaPotomkow;

        @FXML
        Label liczbaPotomkowLabel;

        @FXML
        TextField czyZyje;

        @FXML
        Label czyZyjeLabel;

        @FXML
        Button accept;

        private SingleAnimalStatistics singleStats;
        private boolean chosen = false;


        public ViewController(Pane world,TextField wybranyZwierzak,Label wybranyZwierzakLabel,TextField liczbaEpok,Label liczbaEpokLabel,TextField liczbaDzieci,Label liczbaDzieciLabel,TextField liczbaPotomkow,Label liczbaPotomkowLabel,TextField czyZyje,Label czyZyjeLabel, Button accept) {
                this.world = world;
                this.wybranyZwierzak = wybranyZwierzak;
                this.wybranyZwierzakLabel = wybranyZwierzakLabel;
                this.czyZyjeLabel = czyZyjeLabel;
                this.czyZyje = czyZyje;
                this.liczbaDzieci = liczbaDzieci;
                this.liczbaDzieciLabel = liczbaDzieciLabel;
                this.liczbaEpok = liczbaEpok;
                this.liczbaEpokLabel = liczbaEpokLabel;
                this.liczbaPotomkow = liczbaPotomkow;
                this.liczbaPotomkowLabel = liczbaPotomkowLabel;
                this.accept = accept;
        }

        @Override
        public void moveOnSimulation(IMapElement element) {
                element.getRectangle().setTranslateX(element.getPosition().getX()*size);
                element.getRectangle().setTranslateY(element.getPosition().getY()*size);
                element.getRectangle().setFill(element.getColor());
        }
        @Override
        public void drawSubject(IMapElement element) {
                element.getRectangle().setTranslateX(element.getPosition().getX()*size);
                element.getRectangle().setTranslateY(element.getPosition().getY()*size);
                element.getRectangle().setStroke(Color.DARKGREEN);
                world.getChildren().add(element.getRectangle());
        }

        @Override
        public void removeSubject(Rectangle oldRectangle) {
                world.getChildren().remove(oldRectangle);
        }

        public void clicked()
        {
                liczbaEpok.setDisable(false);
                liczbaEpok.setVisible(true);
                liczbaEpokLabel.setDisable(false);
                liczbaEpokLabel.setVisible(true);
                accept.setDisable(false);
                accept.setVisible(true);

                wybranyZwierzak.setText(singleStats.getGenome());
        }

        public void setSingleStats(Animal animal) {
                this.singleStats = new SingleAnimalStatistics(animal);
                this.chosen = true;
        }

        public boolean haveAnimalChosen()
        {
                return chosen;
        }

        public SingleAnimalStatistics getSingleStats() {
                return singleStats;
        }

}



