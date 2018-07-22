import javax.swing.*;
import java.awt.*;

public class Interface extends Canvas {
    JFrame frame;



    Interface(){
        super();

        this.frame = new JFrame("Sequence");
        this.frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        this.setPreferredSize(new Dimension(1200, 720));

        this.frame.add(this);
        this.frame.pack();
        this.frame.setLocationRelativeTo(null);
        this.frame.setVisible(true);

        requestFocus();
    }
}
