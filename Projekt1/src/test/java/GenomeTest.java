import AnimalElements.Genome;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.assertEquals;


class GenomeTest {

    @Test
    public void printGenome()
    {
        Integer [] mygenome1 = new Integer[] {0,0,1,2,2,2,3,3,3,4,4,4,4,4,5,5,5,5,5,5,5,6,6,6,6,6,6,7,7,7,7,7};
        LinkedList genes1 = new LinkedList(Arrays.asList(mygenome1));

        Integer [] mygenome2 = new Integer[] {0,1,0,2,2,3,3,2,3,4,4,4,4,5,4,5,5,5,5,5,5,6,6,6,6,6,7,6,7,7,7,7};
        LinkedList genes2 = new LinkedList(Arrays.asList(mygenome2));

        Genome genome1 = new Genome(genes1);
        Genome genome2 = new Genome(genes2);

        assertEquals(genome1.hashCode(),genome2.hashCode());
    }

}