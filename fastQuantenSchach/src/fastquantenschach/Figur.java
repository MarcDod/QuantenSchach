package fastquantenschach;

import com.sun.javafx.iio.ios.IosDescriptor;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author marc.doderer
 */


public class Figur {
    
    public final static int FIGUR_SIZE = 90;
    public final static int FIGUR_AURA = FIGUR_SIZE + 3*2;
    public final static int FIGUR_BLACK = 0;
    public final static int FIGUR_WHITE = 1;
    
    private final static BufferedImage[][] FIGUR_IMAGE = new BufferedImage[2][7]; 

    public int getColor() {
        return this.farbe;
    }

    public FigurTyp getType() {
        return this.typ;
    }
        
    public enum FigurTyp{
        DAME("Dame"), KOENIG("König"), TURM("Turm"), LAUEFER("Läufer"), SPIRNGER("Springer"), BAUER("Bauer") , QUANT("Quant");
        
        private String name;

        private FigurTyp(String name) {
            this.name = name;
        }
        
    }
    
    private FigurTyp typ;
    
    private int farbe;
    
    public Figur(int farbe){
        this.farbe = farbe;
        this.typ = FigurTyp.QUANT;
    }
    
    public Figur(FigurTyp typ, int farbe){
        this.farbe = farbe;
        this.typ = typ;
    }
    
    public void anschauen(Spieler s){
        this.typ = s.getRandomFigur().getType();
    }
    
    public void zeicheFigur(Graphics g, int x, int y){
        Color prevColor = g.getColor();
        // TODO: figur zeichnen
        //System.out.println(this.typ.ordinal());
        g.drawImage(FIGUR_IMAGE[this.farbe][this.typ.ordinal()], x - FIGUR_SIZE/2, y - FIGUR_SIZE/2, null);

        g.setColor(prevColor);
    }
    
    
    public static void loadFigurImages(String url) throws IOException{
        BufferedImage image = ImageIO.read(new File(url));
        
        for(int y = 0; y < FIGUR_IMAGE.length; y++){
            for(int x = 0; x < FIGUR_IMAGE[y].length; x++){
                FIGUR_IMAGE[y][x] = image.getSubimage(x * FIGUR_SIZE, y * FIGUR_SIZE, FIGUR_SIZE, FIGUR_SIZE);
            }
        }
        
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Figur other = (Figur) obj;
        if (this.farbe != other.farbe) {
            return false;
        }
        if (this.typ != other.typ) {
            return false;
        }
        return true;
    }
}
