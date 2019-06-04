/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fastquantenschach;

import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author Marc
 */
public class Spieler {
    private ArrayList<Figur> quantFiguren;
    
    private ArrayList<Figur> geschmisseneFiguren;
    
    public Spieler(int farbe){
        this.quantFiguren = new ArrayList<>();
        this.geschmisseneFiguren = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            quantFiguren.add(new Figur(Figur.FigurTyp.BAUER, farbe));
        }
        for (int i = 0; i < 2; i++) {
            quantFiguren.add(new Figur(Figur.FigurTyp.SPIRNGER, farbe));
            quantFiguren.add(new Figur(Figur.FigurTyp.TURM, farbe));
            quantFiguren.add(new Figur(Figur.FigurTyp.LAUEFER, farbe));
        }
        quantFiguren.add(new Figur(Figur.FigurTyp.KOENIG, farbe));
        quantFiguren.add(new Figur(Figur.FigurTyp.DAME, farbe));
    }
    
    public void deckeFigurAuf(Figur figur){          
        this.quantFiguren.remove(figur);
    }

    public void schmeisseFigur(Figur figur){
        if(!figur.isQuantenStatus())
            this.quantFiguren.remove(figur);
        this.geschmisseneFiguren.add(figur);
    }
    
    public ArrayList<Figur> getQuantFiguren() {
        return quantFiguren;
    }
    
    public ArrayList<Figur> getGeschmisseneFiguren(){
        return this.geschmisseneFiguren;
    }
    
    public Figur getRandomFigur() {
        return this.quantFiguren.get(new Random().nextInt(this.quantFiguren.size()));
    }
}
