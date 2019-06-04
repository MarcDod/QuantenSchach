/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fastquantenschach;

import java.awt.Point;
import java.util.ArrayList;

/**
 *
 * @author Marc
 */
public class Steuerung {

    private FastQuantenSchach gui;
    private Spieler[] spieler;
    private Figur[][] figurenFelder;

    private int aktuellerSpieler;

    private Figur figurSelected;

    public Steuerung(FastQuantenSchach gui) {
        this.gui = gui;
        this.spieler = new Spieler[2];
    }
    
    public void startGame(){
        this.spieler[0] = new Spieler(Figur.FIGUR_BLACK);
        this.spieler[1] = new Spieler(Figur.FIGUR_WHITE);
        this.aktuellerSpieler = Figur.FIGUR_WHITE;
        this.figurSelected = null;
        this.figurenFelder
                = new Figur[FastQuantenSchach.GRID_SIZE][FastQuantenSchach.GRID_SIZE];
        
        for (int y = 0; y < this.figurenFelder.length; y++) {
            Figur[] figurenreihe = this.figurenFelder[y];
            for (int x = 0; x < figurenreihe.length; x++) {
                if (y < 2 || y > FastQuantenSchach.GRID_SIZE - 3) {
                    this.figurenFelder[y][x] = new Figur((y < 2)
                            ? Figur.FIGUR_BLACK : Figur.FIGUR_WHITE, x, y);
                }
            }
        }
        this.gui.setTextSpielerAnzeige("Weiß am Zug");
        this.gui.setButtonText("Reset");
        this.gui.zeichneSchachBrett(this.figurenFelder, new ArrayList<Point>());
        this.gui.zeichneGeworfeneFiguren(spieler);
    }

    public void mousEvent(int x, int y) { //Click Figur -> Selected + move abfrage;anzeige -> Ziel auswählen;bewegen
        ArrayList<Point> laufMöglichkeiten = new ArrayList<>();
        if (figurSelected == null) {
            if (this.figurenFelder[y][x] != null && this.figurenFelder[y][x]
                    .getColor() == aktuellerSpieler) {
                this.figurSelected = figurenFelder[y][x];
                this.figurSelected.anschauen(this.spieler[this.aktuellerSpieler]);
                
                laufMöglichkeiten = this.figurSelected.getMoves(figurenFelder);
                //this.spieler[aktuellerSpieler].deckeFigurAuf(figurSelected);
                //this.spieler[aktuellerSpieler].schmeisseFigur(figurSelected);
                
            }
        } else {
            //TODO: richtiger move -> spieler wechsel
            if(this.figurSelected.validateMove(new Point(x, y), figurenFelder))  {
                this.figurenFelder[y][x] = figurSelected;
                this.figurenFelder[figurSelected.getY()][figurSelected.getX()] = null;
                this.figurSelected.setPosition(x, y);
            }          
            
            this.aktuellerSpieler = (aktuellerSpieler == Figur.FIGUR_WHITE)?
                    Figur.FIGUR_BLACK:Figur.FIGUR_WHITE;
            this.gui.setTextSpielerAnzeige(
                    (aktuellerSpieler == Figur.FIGUR_WHITE) ?
                            "Weiß am Zug" : "Schwarz am Zug");
            this.gui.zeichneGeworfeneFiguren(spieler);
            this.figurSelected.setQuantPosition();
            figurSelected = null;
        }

        this.gui.zeichneSchachBrett(this.figurenFelder, laufMöglichkeiten);
    }

    
}
