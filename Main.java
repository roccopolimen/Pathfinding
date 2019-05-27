import java.awt.*;
import javax.swing.*;

public class Main extends JFrame
{
    private int width, height;

    public Main(int row, int col)
    {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        width = (int)(screenSize.getWidth());
        height = (int)(screenSize.getHeight());

        int rows = row;
        int cols = col;

        super.setTitle("Pathfinding Visualization");
        super.setSize(width,height);
        super.setLocation(0,0);
        super.setResizable(true);
        super.add(new Visualizer(width, height, rows, cols));
        super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        super.setVisible(true);
    }

    public static void main(String [] args)
    {
        new Main(10, 18);
    }

    public static void main(int row, int col)
    {
        new Main(row, col);
    }
}