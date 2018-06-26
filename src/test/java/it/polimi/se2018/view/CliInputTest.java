package it.polimi.se2018.view;

import it.polimi.se2018.view.cli.CliInput;
import org.junit.Test;

public class CliInputTest {

    @Test
    public void testReadInput(){
        CliInput input = new CliInput(System.in);
    }
}