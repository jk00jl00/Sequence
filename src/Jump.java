import java.awt.*;

public class Jump {
    int from;
    int to;
    int d;
    Interface i;

    static int mod = 5;

    Jump(int from, int to, Interface i){
        this.from = from;
        this.to = to;
        d = (from - to) * mod;
        this.i = i;
    }

    void draw(Graphics2D g){
        boolean top = (d < 0);

        if(top) {
            g.drawArc(from * mod, i.getHeight()/2 + d / 2, -d, -d, 0, 180);
        }
        else{
            g.drawArc(to * mod, i.getHeight()/2 - d/2, d, d, 0, -180);
        }
    }
}
