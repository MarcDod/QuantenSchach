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
        if(figur.isQuantenStatus())
            this.quantFiguren.remove(figur);
        this.geschmisseneFiguren.add(figur);
//        for(Figur figur1 : quantFiguren){
//            System.out.println(figur1.getType().name());
//        }
//        System.out.println("\r\n");
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
    
    /**
     * 
     * @param field
     * @return Quant Figur if no move posiible
     */
    public Figur getRandomFigurAbleToMove(Figur[][] field, int x, int y){
        ArrayList<Figur> figurAbleToMove = new ArrayList<>();      
        for (Figur figur : this.quantFiguren) {
            figur.setPosition(x, y);
            if(!figur.getMoves(field).isEmpty()){
                figurAbleToMove.add(figur);
                //System.out.println(figur.getMoves(field) + " : " + figur.getType() + " : " + figur.getColor());
            }
        }
        return (figurAbleToMove.isEmpty()) ? new Figur(Figur.FigurTyp.QUANT, Figur.FIGUR_BLACK) : figurAbleToMove.get(new Random().nextInt(figurAbleToMove.size()));
    }
}
