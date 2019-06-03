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
    
    public Spieler(int farbe){
        this.quantFiguren = new ArrayList<>();
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
    
    public void schmeißeFigur(Figur figur){          
        this.quantFiguren.remove(figur);
    }

    public ArrayList<Figur> getFiguren() {
        return quantFiguren;
    }

    public Figur getRandomFigur() {
        return this.quantFiguren.get(new Random().nextInt(this.quantFiguren.size()));
    }
}
