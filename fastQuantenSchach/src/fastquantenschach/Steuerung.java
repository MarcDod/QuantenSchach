S/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fastquantenschach;

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
        this.spieler[0] = new Spieler(Figur.FIGUR_BLACK);
        this.spieler[1] = new Spieler(Figur.FIGUR_WHITE);
        this.aktuellerSpieler = Figur.FIGUR_WHITE;
        this.figurenFelder
                = new Figur[FastQuantenSchach.GRID_SIZE][FastQuantenSchach.GRID_SIZE];

        for (int y = 0; y < this.figurenFelder.length; y++) {
            Figur[] figurenreihe = this.figurenFelder[y];
            for (int x = 0; x < figurenreihe.length; x++) {
                if (y < 2 || y > FastQuantenSchach.GRID_SIZE - 3) {
                    this.figurenFelder[y][x] = new Figur((y < 2)
                            ? Figur.FIGUR_BLACK : Figur.FIGUR_WHITE);
                }
            }
        }
        this.gui.zeichneSchachBrett(this.figurenFelder);
    }

    public void mousEvent(int x, int y) { //Click Figur -> Selected + move abfrage;anzeige -> Ziel auswählen;bewegen
        if (figurSelected == null) {
            if (this.figurenFelder[y][x] != null && this.figurenFelder[y][x]
                    .getColor() == aktuellerSpieler) {
                this.figurSelected = figurenFelder[y][x];
                this.figurSelected.anschauen(this.spieler[this.aktuellerSpieler]);
                this.spieler[aktuellerSpieler].schmeißeFigur(figurSelected);
            }
        } else {
            figurSelected = null;
            //TODO: richtiger move -> spieler wechsel
        }

        this.gui.zeichneSchachBrett(this.figurenFelder);
    }

}
