import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;

public class Controller implements Runnable {

    Interface I;
    Boolean running = false;
    Thread t;

    ArrayList<Jump> list = new ArrayList<>();
    ArrayList<Integer> visited = new ArrayList<>();

    int jump = 1;
    int maxJumps = 0;
    int jps = 1;
    int cjps = 0;
    int firstJump = 0;

    boolean breakAtScreenEdge = true;

    Controller(){
        this.I = new Interface();
    }

    private void tick() {
        if(list.size() == maxJumps) {
            return;
        }

        int i = list.size() - 1;

        if(i == -1){
            list.add(new Jump(firstJump, (firstJump -1 > -1) ? firstJump-1 : firstJump + 1,I));
            visited.add(firstJump);
            visited.add((firstJump -1 > -1) ? firstJump-1 : firstJump + 1);
            jump++;
            return;
        }

        if(contains(list.get(i).to - jump) || list.get(i).to - jump < 0){
            if(breakAtScreenEdge && list.get(i).to + jump > I.getWidth()/Jump.mod) return;
            list.add(new Jump(list.get(i).to, list.get(i).to + jump, I));
            visited.add(list.get(i).to + jump);
            jump++;
        } else{
            list.add(new Jump(list.get(i).to, list.get(i).to - jump, I));
            visited.add(list.get(i).to - jump);
            jump++;
        }
    }

    private boolean contains(int to) {
        if(visited.contains(to)) return true;
        return false;
    }

    private void draw() {
        BufferStrategy bs = I.getBufferStrategy();

        if (bs == null) {
            I.createBufferStrategy(2);
            return;
        }

        Graphics2D g = (Graphics2D) bs.getDrawGraphics();
        g.clearRect(0, 0, 4500, 2500);

        g.fillRect(0, I.getHeight()/2,4500,1);
        for (Jump j : list) j.draw(g);
        g.drawString("Current arc origin: " + String.valueOf(list.get(list.size()-1).from), 10, 20);
        g.drawString("Current n: " + String.valueOf(list.size()-1), 10, 40);
        g.drawString("Current jumps per second: " + String.valueOf(cjps), 10, 60);
        bs.show();
        g.dispose();


    }

    public synchronized void start() {
        try {
            firstJump = Integer.parseInt(JOptionPane.showInputDialog("Enter starting value"));
            if(firstJump < 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Input was not an Integer value 0 or larger, default value used");
        }
        try {
            Jump.mod = Integer.parseInt(JOptionPane.showInputDialog("Enter scale modifier"));
            if(Jump.mod < 1) throw new NumberFormatException();
            maxJumps = Integer.parseInt(JOptionPane.showInputDialog("Enter n limit"));
            if(maxJumps < 1) throw new NumberFormatException();
            jps = Integer.parseInt(JOptionPane.showInputDialog("Enter desired jumps per second"));
            if(jps < 1) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Please enter an integer with a value above 0");
            System.exit(0);
        }

        breakAtScreenEdge = JOptionPane.showConfirmDialog(null, "Break at window Edge?", "", JOptionPane.YES_NO_OPTION) == 0;


        if(running) return;
        this.running = true;
        this.t = new Thread(this);
        t.start();
    }

    @Override
    public void run() {
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        double timePerFrame = 1000000000 / jps;
        long start;
        long now;
        double dt = 0;
        start = System.nanoTime();
        long timer = 0;
        int ticks = 0;

        while (running) {
            now = System.nanoTime();
            dt += (now - start) / timePerFrame;
            timer += now - start;
            start = now;
            if (dt >= 1) {
                tick();
                draw();
                dt--;
                ticks++;
            }
            if (timer >= 1000000000) {
                System.out.println("Frames: " + ticks);
                cjps = ticks;

                /*for(Jump j: list){
                    System.out.println("n: " + list.indexOf(j) +"  " + j.from + " : " + j.to + "  l: " + jump);
                }*/

                ticks = 0;
                timer = 0;

            }

        }
    }
}
