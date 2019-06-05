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
    private boolean figurGeschmissen;

    private Figur figurSelected;

    public Steuerung(FastQuantenSchach gui) {
        this.gui = gui;
        this.spieler = new Spieler[2];
    }

    public void startGame() {
        this.spieler[0] = new Spieler(Figur.FIGUR_BLACK);
        this.spieler[1] = new Spieler(Figur.FIGUR_WHITE);
        this.aktuellerSpieler = Figur.FIGUR_WHITE;
        this.figurSelected = null;
        this.figurGeschmissen = false;
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

    private void figurAufzeigen(int x, int y) { 

        if (this.figurenFelder[y][x] != null && this.figurenFelder[y][x]
                .getColor() == aktuellerSpieler) {
            if (figurenFelder[y][x].isQuantenStatus()) {
                figurenFelder[y][x].anschauen(this.spieler[aktuellerSpieler]);
                this.spieler[aktuellerSpieler]
                        .deckeFigurAuf(figurenFelder[y][x]);
                figurenFelder[y][x].setQuantenstatus(false);
                aufzeigenBeendet();
            }
        }
    }

    
    private boolean checkNoQuant(int player){
        boolean noQuant = true;

        for (Figur[] figuren : this.figurenFelder) {
            for (Figur figur : figuren) {
                if (figur == null) {
                    continue;
                }
                if (figur.isQuantenStatus() && figur.getColor()
                        == player) {
                    noQuant = false;
                }
            }
        }
        
        return noQuant;
    }
    
    private void aufzeigenBeendet() {
        this.gui.setTextSpielerAnzeige(
                (aktuellerSpieler == Figur.FIGUR_WHITE) ? "Weiß am Zug"
                        : "Schwarz am Zug");
        figurGeschmissen = false;
    }

    private void figurAnschauen() {
        if (this.figurSelected.isQuantenStatus()) {
            this.figurSelected.
                    anschauen(this.spieler[this.aktuellerSpieler]);
        }
    }

    private boolean checkKoenigWurf(int x, int y) {
        if (this.figurenFelder[y][x].getType()
                == Figur.FigurTyp.KOENIG) {
            return true;
        }
        return false;
    }

    private ArrayList<Point> findePassendeFigurTyp() {
        if (this.figurSelected.isQuantenStatus()) {
            this.figurSelected.anschauenNurBewegungsFaehig(
                    spieler[aktuellerSpieler], figurenFelder);
        }

        return (this.figurSelected.getType() == Figur.FigurTyp.QUANT)
                ? new ArrayList<>()
                : this.figurSelected.getMoves(figurenFelder);
    }

    public void mousEvent(int x, int y) { //Click Figur -> Selected + move abfrage;anzeige -> Ziel auswählen;bewegen
        ArrayList<Point> laufMöglichkeiten = new ArrayList<>();
        if (figurGeschmissen) { // Figur aufdecken wenn geschmissen wurde.
            figurAufzeigen(x, y);
        } else if (figurSelected == null) {
            if (this.figurenFelder[y][x] != null && this.figurenFelder[y][x]
                    .getColor() == aktuellerSpieler) {
                this.figurSelected = figurenFelder[y][x];

                laufMöglichkeiten = findePassendeFigurTyp();

                // Wenn keine Laufmöglichkeiten gegeben sind.
                if (laufMöglichkeiten.isEmpty()) {
                    if (this.figurSelected.isQuantenStatus()) {
                        this.figurSelected.setQuantPosition();
                    }
                    this.figurSelected = null;
                    return;
                } // 

            }
        } else {
            if (this.figurSelected.validateMove(new Point(x, y), figurenFelder)) {
                if (this.figurenFelder[y][x] != null) {
                    Spieler otherPlayer = this.spieler[(this.aktuellerSpieler
                            == Figur.FIGUR_WHITE) ? Figur.FIGUR_BLACK
                                    : Figur.FIGUR_WHITE];

                    if (this.figurenFelder[y][x].isQuantenStatus()) {
                        this.figurenFelder[y][x].
                                anschauen(this.spieler[(this.aktuellerSpieler
                            == Figur.FIGUR_WHITE) ? Figur.FIGUR_BLACK
                                    : Figur.FIGUR_WHITE]);
                    }

                    if (checkKoenigWurf(x, y)) {
                        this.endGame();
                        return;
                    }

                    otherPlayer.schmeisseFigur(this.figurenFelder[y][x]);
                    this.figurGeschmissen = !checkNoQuant((this.aktuellerSpieler
                            == Figur.FIGUR_WHITE) ? Figur.FIGUR_BLACK
                                    : Figur.FIGUR_WHITE);
                }
                this.figurenFelder[y][x] = figurSelected;
                this.figurenFelder[figurSelected.getY()][figurSelected.getX()]
                        = null;
                this.figurSelected.setPosition(x, y);

                if(this.figurSelected.getType() == Figur.FigurTyp.BAUER && y
                        == ((figurSelected.getColor() == Figur.FIGUR_WHITE) ? 0
                        : 7)){
                    //TODO: Bauernwechsel
                    System.out.println("hallo");
                }
            }else {
                return;
            }

            changePlayer();
            this.gui.zeichneGeworfeneFiguren(spieler);
            if (this.figurSelected.isQuantenStatus()) {
                this.figurSelected.setQuantPosition();
            }
            figurSelected = null;
        }

        this.gui.zeichneSchachBrett(this.figurenFelder, laufMöglichkeiten);
    }

    private void changePlayer() {
        this.aktuellerSpieler = (aktuellerSpieler == Figur.FIGUR_WHITE)
                ? Figur.FIGUR_BLACK : Figur.FIGUR_WHITE;
        if (this.figurGeschmissen) {
            this.gui.setTextSpielerAnzeige(
                    (aktuellerSpieler == Figur.FIGUR_WHITE) ? "Weiß Aufdecken"
                            : "Schwarz Aufdecken");
        } else {
            this.gui.setTextSpielerAnzeige(
                    (aktuellerSpieler == Figur.FIGUR_WHITE) ? "Weiß am Zug"
                            : "Schwarz am Zug");
        }
    }

    private void endGame() {
        startGame();
    }

}
