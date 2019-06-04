package fastquantenschach;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;

/**
 *
 * @author marc.doderer
 */
public class Figur{

    public final static int FIGUR_SIZE = 90;
    public final static int FIGUR_AURA = FIGUR_SIZE + 3 * 2;
    public final static int FIGUR_BLACK = 0;
    public final static int FIGUR_WHITE = 1;

    private final static BufferedImage[][] FIGUR_IMAGE = new BufferedImage[2][7];
    private boolean quantenstatus;
    
    
    private int x, y;
    
    public int getColor(){
        return this.farbe;
    }

    public FigurTyp getType(){
        return this.typ;
    }

    public enum FigurTyp{
        DAME("Dame"), KOENIG("König"), TURM("Turm"), LAUEFER("Läufer"),
        SPIRNGER("Springer"), BAUER("Bauer"), QUANT("Quant");

        private String name;

        private FigurTyp(String name){
            this.name = name;
        }
        public ArrayList<Point> getMoves(Point stand,boolean down,
                Figur[][] field, int color){
            ArrayList<Point> result = new ArrayList<>();
            for(int y = 0; y < field.length; y++){
                Figur[] fieldRow = field[y];
                for(int x = 0; x < fieldRow.length; x++){
                    Figur figur = fieldRow[x];
                    Point possibleTarget = new Point(x, y);
                    if(this.validateMove(stand, possibleTarget,
                            down, field, color)){
                        result.add(possibleTarget);
                    }
                }
            }
            return result;
        }

        public boolean validateMove(Point stand, Point target, boolean down,
                Figur[][] field, int color){
            int dX = target.x - stand.x;
            int dY = target.y - stand.y;
            if(dX == 0 && dY == 0){
                return false;
            }
            int absX = Math.abs(dX);
            int absY = Math.abs(dY);
            final Figur targetFigur = field[target.y][target.x];
            switch(this){
                case DAME:
                    if(!(absX == absY || dX == 0 || dY == 0)){
                        return false;
                    }
                    for(int i = 1; i < ((absX > absY) ? absX : absY); i++){
                        if(field[stand.y + i * ((absY == 0)? 0:dY / absY)][stand.x + i
                                * ((absX == 0)? 0:dX / absX)] != null){
                            return false;
                        }
                    }
                    break;
                case KOENIG:
                    if(absX > 1 || absY > 1){
                        return false;
                    }
                    break;
                case TURM:
                    if(!(dX == 0 || dY == 0)){
                        return false;
                    }
                    for(int i = 1; i < ((absX > absY) ? absX : absY); i++){
                        if(field[stand.y + i * ((absY == 0)? 0:dY / absY)][stand.x + i
                                * ((absX == 0)? 0:dX / absX)] != null){
                            return false;
                        }
                    }
                    break;
                case LAUEFER:
                    if(!(absX == absY)){
                        return false;
                    }
                    for(int i = 1; i < ((absX > absY) ? absX : absY); i++){
                        if(field[stand.y + i * ((absY == 0)? 0:dY / absY)][stand.x + i
                                * ((absX == 0)? 0:dX / absX)] != null){
                            return false;
                        }
                    }
                    break;
                case SPIRNGER:
                    if(!((absX == 2 && absY == 1) || (absX == 1 && absY == 2))){
                        return false;
                    }
                    break;
                case BAUER:
                    if(absX > 1){
                        return false;
                    }
                    if(absX == 1 &&
                            (targetFigur == null || targetFigur.getColor() == color)){
                        return false;
                    }
                    if((down ? 1 : -1) != dY){
                        return false;
                    }
                    if(field[stand.y + dY][stand.x] != null){
                        return false;
                    }
                    break;
                default: throw new UnsupportedOperationException(
                            "No Moves programmed");
            }
            if(targetFigur != null && targetFigur.getColor() == color){
                return false;
            }
            return true;
        }

    }

    private FigurTyp typ;

    private int farbe;
    
    
    public Figur(int farbe, int x, int y){
        this.farbe = farbe;
        this.typ = FigurTyp.QUANT;
        this.x = x;
        this.y = y;
    }

    public Figur(FigurTyp typ, int farbe){
        this.farbe = farbe;
        this.typ = typ;
    }

    public void anschauen(Spieler s){
        this.typ = s.getRandomFigur().getType();
    }

    public ArrayList<Point> getMoves(Figur[][] field){
        return this.typ.getMoves(new Point(x,y), (this.farbe == FIGUR_BLACK), field, farbe);
    }
    
    public boolean validateMove(Point target,Figur[][] field){
        return this.typ.validateMove(new Point(x, y), target, (this.farbe == FIGUR_BLACK), field, farbe);
    }
    public void zeicheFigur(Graphics g, int x, int y){
        Color prevColor = g.getColor();
        // TODO: figur zeichnen
        //System.out.println(this.typ.ordinal());
        g.drawImage(FIGUR_IMAGE[this.farbe][this.typ.ordinal()], x - FIGUR_SIZE
                / 2, y - FIGUR_SIZE / 2, null);

        g.setColor(prevColor);
    }

    public void setPosition(int x, int y){
        this.x = x;
        this.y = y;
    }
    
    public void setQuantPosition(){
        this.typ = FigurTyp.QUANT;
    }
    
    public int getX(){
        return this.x;
    }
    
    public int getY(){
        return this.y;
    }
    
    public static void loadFigurImages(String url) throws IOException{
        BufferedImage image = ImageIO.read(new File(url));

        for(int y = 0; y < FIGUR_IMAGE.length; y++){
            for(int x = 0; x < FIGUR_IMAGE[y].length; x++){
                FIGUR_IMAGE[y][x] = image.getSubimage(x * FIGUR_SIZE, y
                        * FIGUR_SIZE, FIGUR_SIZE, FIGUR_SIZE);
            }
        }

    }

    @Override
    public boolean equals(Object obj){
        if(this == obj){
            return true;
        }
        if(obj == null){
            return false;
        }
        if(getClass() != obj.getClass()){
            return false;
        }
        final Figur other = (Figur) obj;
        if(this.farbe != other.farbe){
            return false;
        }
        if(this.typ != other.typ){
            return false;
        }
        return true;
    }
}
