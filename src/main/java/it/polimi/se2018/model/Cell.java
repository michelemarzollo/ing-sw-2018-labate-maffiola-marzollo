package it.polimi.se2018.model;

public class Cell {
    private int value; //restrizione sul valore (0 se non ci sono restrizioni di questo tipo)
    private Colour colour; //restrizione sul colore (null se non ci sono restrizioni di questo tipo)
    private Die die; //dado nella cella: viene lasciato a null finchè non viene chiamata la place (finchè non viene piazzato un dado)


    public Cell(){
    }

    public Cell(int value) { //bisogna controllare che sia un valore tra 1 e 6
        if(value < 1 || value > 6){
            throw new IllegalArgumentException("Restriction on the Cell's value must be between 1 and 6 ");
        }
        else {
            this.value = value;
        }
    }

    public Cell(Colour colour){
        this.colour = colour;
    }


    public int getValue() {
        return value;
    }

    public Colour getColour() {
        return colour;
    }

    public Die getDie() {
        return die;
    }

    public void place (Die d) throws PlacementErrorException{ //metodo per piazzare il dado 'd' nella cella. Lancia una eccezione se la cella è già occupata oppure se si sta andando contro le restrizioni di valore o di colore
        if(die != null){
            throw new PlacementErrorException("This cell has already a placed die. Choose another cell.");
        }
        if(value != 0 && value != d.getValue()){
            throw new PlacementErrorException("The die's value is different from the value restriction of the cell");
        }
        if(colour != null && colour != d.getColour()){
            throw new PlacementErrorException("The die's colour is different from the colour restriction of the cell");
        }
        die = d;
    }

    @Override
    public String toString() {
        return "Value: " + this.value + "," + "Colour: "+ this.colour + "," + "Die: " + this.die;
    }

    public Die remove (){ //metodo per rimuovere il dado dalla cella e lasciarla vuota (null). Serve per le toolcard che mi permettono di muovere dadi già piazzati
        //può avere senso che non lanci eccezioni? Al massimo ritornerà null se non c'era nessun dado
        //l'unico problema è che la removeDie di Pattern può ritornare null. E' effettivamente un problema?
        Die tmp = die;
        die = null;
        return tmp;
    }
}
