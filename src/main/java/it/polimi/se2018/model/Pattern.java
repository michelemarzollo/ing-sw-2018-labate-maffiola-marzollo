package it.polimi.se2018.model;

import it.polimi.se2018.utils.Coordinates;

import java.util.ArrayList;

public class Pattern {
    private String name;
    private int difficulty;
    private Cell[][] grid;

    public Pattern(String name, int difficulty, Cell[][] grid){ //si assume quindi che la matrice di Celle venga ISTANZIATA da qualche altra parte. Corretto?
        if(difficulty < 3 || difficulty > 6){
            throw new IllegalArgumentException("The difficuly is not between 3 and 6");
        }
        else {
            this.name = name;
            this.difficulty = difficulty;
            this.grid = grid;
        }
    }


    public String getName() {
        return name;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public Cell[][] getGrid() {
        return grid;
    }

    //quando si cerca di piazzare un dado in una cella già occupata e che non è adiacente a un altro dado viene lanciata l'eccezione relativa a questa seconda situazione--> è un problema?
    //stesso discorso quando piazzao un dado in una cella già occupata e che è adiacente ortogonalmente a una cella occupata da un dado dello stesso colore o dello stesso valore
    public void placeDie(Die d, Coordinates c) throws PlacementErrorException{ //metodo per piazzare il dado nella cella di coordinate c
        if(isEmpty(grid) && notEdgeOrCorner(c)) throw new PlacementErrorException("The first die is not placed on an edge or corner space");
        if(!isEmpty(grid) && !isAdjacent(c)) throw new PlacementErrorException("The die is not adjacent to a previously placed die");//NB: il primo dado NON deve ovviamente essere adiacente a nessuno: per questo l'eccezione viene lanciata solo se la griglia non è vuota
        if(!respectRestrictions(d,getOrtogAdiacent(c))) throw new PlacementErrorException("The die is placed orthogonally adjacent to a die of the same color or the same value");
        grid[c.getRow()][c.getCol()].place(d);

    }

    public Die removeDie(Coordinates c){ //metodo per rimuovere il dado nella cella alla riga 'raw' e alla colonna 'col'
        return grid[c.getRow()][c.getCol()].remove();
    }

    private boolean validCoordinates (int row, int col){ //verifica che gli indici di riga e colonna siano validi: l'indice di riga deve stare tra 0 e 3 (4 righe) mentre quello di colonna tra 0 e 4 (5 colonne). E' utile per il metodo isAdjacent
        return row >= 0 && row <= 3 && col >= 0 && col <= 4;
    }

    private boolean fullCell(int row, int col){ //verifica che la cella di indice di riga row e di indice di colonna col sia occupata da un dado. E' utile per il metodo isAdjacent
        return grid[row][col].getDie() != null;
    }
    private boolean isAdjacent(Coordinates c){ //ritorna True se la cella di coordinate c è adiacente ad almeno un dado, falso altrimenti
        for(int i = -1; i < 2; i++){
            for (int j = -1; j < 2; j++){
                if(validCoordinates(c.getRow()+i, c.getCol()+j) && fullCell(c.getRow()+i, c.getCol()+j) && (i != 0 || j != 0)){
                    return true;
                }
            }
        }
        return false;
    }


    private ArrayList<Die> getOrtogAdiacent(Coordinates c){ //ritorna una lista dei dadi ortogonalmente adiacenti alla cella di coordinate c . E' utile per il metodo respectRestrictions
        ArrayList<Die> result = new ArrayList<>();

        if(c.getRow()-1 >= 0 && fullCell(c.getRow()-1, c.getCol())){
            result.add(grid[c.getRow()-1][c.getCol()].getDie());
        }
        if(c.getRow()+1 <= 3 && fullCell(c.getRow()+1, c.getCol())){
            result.add(grid[c.getRow()+1][c.getCol()].getDie());
        }
        if(c.getCol()-1 >= 0 && fullCell(c.getRow(), c.getCol()-1)){
            result.add(grid[c.getRow()][c.getCol()-1].getDie());
        }
        if(c.getCol()+1 <= 4 && fullCell(c.getRow(), c.getCol()+1)){
            result.add(grid[c.getRow()][c.getCol()+1].getDie());
        }

        System.out.println("dadi adiacenti--> " + result);
        return result;
    }

    private boolean respectRestrictions(Die die, ArrayList<Die> adj){ //verifica che il dado die sia diverso in colore e valore dai dadi nella lista adj che rappresenterà la lista dei dadi ortogonalmente adiacenti a die
        for(Die d: adj){
            if(d.getColour() == die.getColour() || d.getValue() == die.getValue()) return false;
        }
        return true;
    }

    private boolean isEmpty(Cell[][] g){ //verifica che la griglia sia vuota. Serve a verificare nella placeDie se il dado che si sta piazzando è il primo
        for(int row = 0; row <= 3; row++){
            for(int col = 0; col <= 4; col++){
                if(g[row][col].getDie() != null) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean notEdgeOrCorner (Coordinates c){ //verifica che c non sia una coordinata che rappresenta i bordi o gli angoli del pattern. Serve a verificare nella placeDie che si rispettino le restrizioni sul piazzamento del primo dado
        return c.getRow() >= 1 && c.getRow() <= 2 && c.getCol() >= 1 && c.getCol() <= 3;
    }
}
