package AnimalElements;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class Genome {
    //Klasa do przechowywania genomu zwierzaka

    final private List<Integer> genome;
    final static public int numberOfGenes = 32;
    private int [] genes = {0,0,0,0,0,0,0,0};

    public Genome(List<Integer> genome)  //mozemy ustawic genom wlasny
    {
        validateGenome(genome);
        this.genome = genome;
    }
    public Genome()
    {
        this.genome = generateGenome();  //generujemy losowy genom
        validateGenome(genome);
    }

    public int[] getGenes() {
        return genes;
    }

    private List<Integer> generateGenome()
    {
        //genomy beda arraylista ze wzgledu na latwiejszy dostep do poszczegolnych genomow
        //w przypadku ewentualnych zamian
        //lub sprawdzania genomu i zliczania genow
        //obie te rzeczy wymagaja dostepu do genow
        //po utworzeniu genomu, nie usuwamy/dodajemy genow

        List<Integer> genome = new ArrayList<>();
        for (int i = 0; i<8; i++)
        {
            genome.add(i);  //to nam zapewni, by zawsze bylo co najmniej jeden gen z mozliwych 8
        }

        Random generator = new Random();

        for (int i = 0; i<24; i++)
        {
            genome.add(generator.nextInt(8));  //generujemy pozostala liczbe
        }
        validateGenome(genome);

        return genome;
    }

    public List<Integer> getGenome() {
        return genome;
    }

    public Genome reproduce(Genome parent2)
    {
        //generowanie nowego genomu na podstawie 2 rodzicow
        //polega na cieciu obu genomow w 2 losowych miejscach
        //aby ciecia zawsze trafialy na genom i kazda czesc miala zawsze co najmniej jeden genom
        //wyznaczono wzory na kazde ciecie (divider)
        //ciecie stanowi granice w taki sposob, ze ponizej jest czesc, a ciecie i powyzej jest czesc nastepna
        //pierwsze ciecie wiec nie moze trafic ani w 0 (bo lewa czesc bedzie miec 0) ani w wiecej niz 32-3+1 (1-30)
        //bo musimy zostawic miejsce na ciecie drugie i co najmniej gen srodkowy i koncowy
        // drugie natomiast musi trafic za cieciem pierwszym i ponizej 31


        Random generator = new Random();
        int divider1 = generator.nextInt((int)Math.ceil(32-3))+1;
        int divider2 = generator.nextInt((int)Math.ceil(32 - divider1 -1)) + 1 + divider1;

        //losujemy ktora grupa idzie od drugiego rodzica
        int group =  generator.nextInt((int)Math.ceil(3));

        List<Integer> genomeResult = new ArrayList<>();

        switch(group)
        {
            case 0 -> {addGenes(0,divider1,this.genome,genomeResult);
                    addGenes(divider1,divider2,parent2.getGenome(),genomeResult);
                    addGenes(divider2,numberOfGenes,parent2.getGenome(),genomeResult);}
            case 1 ->  {addGenes(0,divider1,parent2.getGenome(),genomeResult);
                addGenes(divider1,divider2,this.genome,genomeResult);
                addGenes(divider2,numberOfGenes,parent2.getGenome(),genomeResult);}
            case 2 ->  {addGenes(0,divider1,parent2.getGenome(),genomeResult);
                addGenes(divider1,divider2,parent2.getGenome(),genomeResult);
                addGenes(divider2,numberOfGenes,this.genome,genomeResult);}
        }

        //musimy zadbac, by genom zawsze byl poprawny
        validateGenome(genomeResult);

        return new Genome(genomeResult);
    }
    private void validateGenome(List<Integer> genomeResult)
    {
        int [] flags = {0,0,0,0,0,0,0,0};  //bedziemy zliczac ile jest genow

        for(Integer gene : genomeResult) {
            flags[gene] += 1;
        }

        for(int i = 0; i < 8; i++) {
            if (flags[i] == 0) {   //jezeli ktoregos nie ma
                change_common(genomeResult, i, flags);
            }
        }

        genes = flags;
    }
    private void change_common(List<Integer> genomeResult,int i,int [] flags)
    {
        //aby zapewnic zawsze poprawny genom, bierzemy najczesciej wystepujacy gen
        // i zamieniamy jego sztuke na ten brakujacy

        int most_common = 0;
        int most_common_count = 0;

        //biore najczestszy gen i zamieniam na brakujacy
        for(int j = 0 ; j<8; j++)
        {
            if(most_common_count < flags[j])
            {
                most_common = j;
                most_common_count = flags[j];
            }
        }

       for(Integer gene : genomeResult)
       {
           if (gene == most_common)
           {
               gene = i;
               break;
           }
       }
       //ukradlismy jeden gen, wiec zmniejszamy jego ilosc
       flags[most_common] -=1;
    }


    private void addGenes(int start, int end, List<Integer> from, List <Integer> to)
    {
        for (int i = start; i < end; i++)
        {
            to.add(from.get(i));
        }
    }

    public int getMostPopularGene()
    {
        int mostPopular = -1;
        int mostPopularIndex = 0;
        for(int i = 0; i < 8; i++)
        {
            if(genes[i] > mostPopular)
            {
                mostPopular = genes[i];
                mostPopularIndex = i;
            }
        }
        return mostPopularIndex;
    }

    @Override
    public String toString() {
        String genesString = "";
        for(int i = 0;i < 8; i++)
        {
            int k = genes[i];
            genesString= genesString + k + " ";
        }
        return genesString;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Genome)) return false;
        Genome genome1 = (Genome) o;
        return Objects.equals(genome, genome1.genome);
    }

    @Override
    public int hashCode() {
        return genes[0]*1 + genes[1]*2 + genes[2]*61 + genes[3] * 5 + genes[4]*17 + genes[5]*27 + genes[6]*47 + genes[7]*89;
    }
}
