
package fastquantenschach;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
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
    private JPanel chessPanel;
    private JPanel menuPanel;

    private Font font;
    
    private BufferedImage image;
    private Graphics g;
   
    
    private Steuerung steuerung;
    
    public FastQuantenSchach() {
        
        this.image = new BufferedImage(SCREEN_WIDTH, SCREEN_HEIGHT, BufferedImage.TYPE_INT_ARGB);
        this.g = this.image.getGraphics();
        this.font =  new Font( "Verdana", Font.BOLD, 20 );
        
        
        
        this.frame = new JFrame("QuantenSchach");
        this.frame.setResizable(false);
        Dimension d = new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT);
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setLayout(new FlowLayout(0, 0, 0));
        this.chessPanel = new JPanel(){
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 =(Graphics2D) g.create();
                g2.clearRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
                g2.drawImage(image, 0, 0, frame);
                g2.dispose();
            }
            
        };
        this.chessPanel.setSize(d);
        this.chessPanel.setPreferredSize(d);
        this.chessPanel.setMinimumSize(d);
        this.chessPanel.setLayout(null);
        this.chessPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent me) {
                mouseEvent(me);
            }
            
        });
        
        initMenu();
        
        this.frame.add(this.chessPanel);
        this.frame.add(this.menuPanel);
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
        this.chessPanel.repaint();
    }
    
    public void zeichneGeworfeneFiguren(Spieler[] spielerliste){
        gGeworfen.setColor(Color.WHITE);
        gGeworfen.fillRect(0, 0, this.geworfeneFiguren.getWidth(), this.geworfeneFiguren.getHeight());
        for(int spieler = 0; spieler< spielerliste.length; spieler++){
            ArrayList<Figur> figuren = spielerliste[spieler].getGeschmisseneFiguren();
            for (int i = 0; i < figuren.size(); i++) {
                
                int y = (i > 7)? i - 8: i;
                int x = (i > 7)? FIELD_SIZE : 0; 
                figuren.get(i).zeicheFigur(gGeworfen,spieler * (FIELD_SIZE * 2) + FIELD_SIZE/2 + x, y * Figur.FIGUR_SIZE + FIELD_SIZE/2);
            }
        }
        this.menuPanel.repaint();
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
    
    
    private JLabel spielerAnzeige;
    private JButton resetButton;
    private JPanel geschmisseneFiguren;
    private Graphics gGeworfen;
    private BufferedImage geworfeneFiguren;
    
    private void initMenu(){
        this.menuPanel = new JPanel();
        Dimension d2 = new Dimension(400, SCREEN_HEIGHT);
        this.menuPanel.setSize(d2);
        this.menuPanel.setPreferredSize(d2);
        this.menuPanel.setMinimumSize(d2);
        this.menuPanel.setLayout(null);
        this.menuPanel.setBackground(Color.WHITE);
        
        this.resetButton = new JButton();
        this.resetButton.setFont(font);
        this.resetButton.setSize(d2.width / 2, 50);
        this.resetButton.setLocation(d2.width / 2, 0);
        this.resetButton.setText("Reset");
        this.resetButton.setBackground(Color.WHITE);
        this.resetButton.setFocusPainted(false);
        this.resetButton.addActionListener(ActionEvent -> resetGame());
        
        this.spielerAnzeige = new JLabel();
        this.spielerAnzeige.setMaximumSize(new Dimension(d2.width / 2, 50));
        this.spielerAnzeige.setSize(new Dimension(d2.width / 2, 50));
        this.spielerAnzeige.setFont(font);
        this.spielerAnzeige.setLocation(0, 0);
        
        setTextSpielerAnzeige("Weiß am Zug");
        
        this.geworfeneFiguren = new BufferedImage(d2.width, 
                SCREEN_HEIGHT - this.resetButton.getHeight(),
                BufferedImage.TYPE_INT_ARGB);
        this.gGeworfen = this.geworfeneFiguren.getGraphics();
        this.geschmisseneFiguren = new JPanel(){
            @Override
            protected void paintComponent(Graphics grphcs) {
                Graphics2D g2 =(Graphics2D) grphcs.create();
                g2.clearRect(0, 0, geworfeneFiguren.getWidth(), geworfeneFiguren.getHeight());
                g2.drawImage(geworfeneFiguren, 0, 0, this);
                g2.dispose();
            }
            
        };
        //Dimension test = new Dimension(this.geworfeneFiguren.getWidth(), this.geworfeneFiguren.getHeight());
        this.geschmisseneFiguren.setSize(this.geworfeneFiguren.getWidth(), this.geworfeneFiguren.getHeight());
        this.geschmisseneFiguren.setLocation(0, this.spielerAnzeige.getHeight() + 5);
                

        this.menuPanel.add(this.geschmisseneFiguren);
        this.menuPanel.add(this.spielerAnzeige);
        this.menuPanel.add(this.resetButton);
        
    }
    
    public void resetGame(){
        
    }
    
    public void setTextSpielerAnzeige(String text){
        this.spielerAnzeige.setText(text);

    }
}
