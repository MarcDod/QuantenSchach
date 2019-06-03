
package fastquantenschach;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 *
 * @author marc.doderer
 */


public class FastQuantenSchach {

    public final static Color FIELD_COLOR_1 = Color.decode("#0000ff");
    public final static Color FIELD_COLOR_2 = Color.WHITE;
    
    public final static int SCREEN_WIDTH = 800;
    public final static int SCREEN_HEIGHT = 800;
    public final static int GRID_SIZE = 8;
    public final static int FIELD_SIZE = SCREEN_WIDTH / GRID_SIZE;
    
    private JFrame frame;
    private JPanel panel;

    private BufferedImage image;
    private Graphics g;
    
    private Steuerung steuerung;
    
    public FastQuantenSchach() {
        
        this.image = new BufferedImage(SCREEN_WIDTH, SCREEN_HEIGHT, BufferedImage.TYPE_INT_ARGB);
        this.g = this.image.getGraphics();
        
        this.frame = new JFrame("QuantenSchach");
        this.frame.setResizable(false);
        Dimension d = new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT);
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        this.panel = new JPanel(){
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 =(Graphics2D) g.create();
                g2.clearRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
                g2.drawImage(image, 0, 0, frame);
                g2.dispose();
            }
            
        };
        this.panel.setSize(d);
        this.panel.setPreferredSize(d);
        this.panel.setMinimumSize(d);
        this.panel.setLayout(null);
        this.panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent me) {
                mouseEvent(me);
            }
            
        });
        
        this.panel.setBackground(Color.red);
        this.frame.add(this.panel);
        this.frame.pack();
        this.frame.setLocationRelativeTo(null);
        this.frame.setVisible(true);
        
        
        this.steuerung = new Steuerung(this);
    }

    public void mouseEvent(MouseEvent me){
        this.steuerung.mousEvent(me.getX() / FIELD_SIZE, me.getY() / FIELD_SIZE);
    }
    
    
    public void zeichneSchachBrett(Figur[][] pFiguren){
        

        g.clearRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
        Color prevColor = g.getColor();
        
        for(int y = 0; y < pFiguren.length; y++){
            for (int x = 0; x < pFiguren[y].length; x++) {
                g.setColor(((x+y)%2 == 0) ? FIELD_COLOR_1 : FIELD_COLOR_2);
                g.fillRect(x * FIELD_SIZE, y * FIELD_SIZE, FIELD_SIZE, FIELD_SIZE);
               
                
                if(pFiguren[y][x] != null){
                    pFiguren[y][x].zeicheFigur(g, x * FIELD_SIZE + FIELD_SIZE/2, y * FIELD_SIZE + FIELD_SIZE/2);
                }
 
            }
        }
        g.setColor(prevColor);
        this.panel.repaint();
    }
    
    
    public static void main(String[] args) {
        try {
            Figur.loadFigurImages("res/figuren/SchachFiguren.png");
            new FastQuantenSchach();
        } catch (IOException ex) {
            Logger.getLogger(FastQuantenSchach.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
    }
    
}
