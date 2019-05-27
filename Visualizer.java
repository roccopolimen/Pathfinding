import javax.swing.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;

public class Visualizer extends JPanel implements Runnable, MouseListener, MouseMotionListener, ActionListener
{
    private Thread main;
    private int width, height, gridWidth, panelX, panelWidth, buttonX, buttonWidth, buttonHeight;
    private int pathIndex;
    private Grid grid;
    private JButton selectStart, selectEnd, findPath, changeAlg, createMaze, reset, toggleLabel;
    private PathFindingAlgorithm [] pathfinders;
    private MazeAlgorithm mazeMaker;
    public Visualizer(int width, int height, int row, int col)
    {
        super.setDoubleBuffered(true);
        this.setLayout(null);
        this.width = width;
        this.height = height;
        this.gridWidth = width*8/10;
        this.panelX = gridWidth;
        this.panelWidth = width-gridWidth;
        this.buttonX = panelX+(panelWidth/10);
        this.buttonWidth = panelWidth*8/10;
        this.buttonHeight = height/12;

        addMouseListener(this);
        addMouseMotionListener(this);
        main = new Thread(this);
        main.start();

        grid = new Grid(0, 0, row, col, gridWidth, height);
        grid.updateHeuristic();
        setPathfinder();
        mazeMaker = new MazeAlgorithm(grid);
        pathIndex = 0;

        selectStart = new JButton("Select a Start Node");
        selectEnd = new JButton("Select an End Node");
        findPath = new JButton("Find a Path");
        changeAlg = new JButton("Change Pathfinding Algorithm");
        changeAlg.setLabel(pathfinders[pathIndex].toString());
        createMaze = new JButton("Draw a Maze");
        reset = new JButton("Reset Grid");
        toggleLabel = new JButton("Label On/Off");
        selectStart.addActionListener(this);
        selectEnd.addActionListener(this);
        findPath.addActionListener(this);
        changeAlg.addActionListener(this);
        createMaze.addActionListener(this);
        reset.addActionListener(this);
        toggleLabel.addActionListener(this);
        this.add(selectStart);
        this.add(selectEnd);
        this.add(findPath);
        this.add(changeAlg);
        this.add(createMaze);
        this.add(reset);
        this.add(toggleLabel);
        selectStart.setBounds(buttonX, height/8, buttonWidth, buttonHeight);
        selectEnd.setBounds(buttonX, height/4, buttonWidth, buttonHeight);
        findPath.setBounds(buttonX, ((int)(height*(3.0/8))), buttonWidth, buttonHeight);
        changeAlg.setBounds(buttonX, height/2, buttonWidth, buttonHeight);
        createMaze.setBounds(buttonX, ((int)(height*(5.0/8))), buttonWidth, buttonHeight);
        reset.setBounds(buttonX, ((int)(height*(3.0/4))), buttonWidth, buttonHeight);
        toggleLabel.setBounds(buttonX, ((int)(height*(7.0/8))), buttonWidth, buttonHeight);
    }

    public void run()
    {
        while(true)
        {
            try{main.sleep(100);}catch(Exception e) {}

            if(width != this.getSize().width || height != this.getSize().height)
                resize();

            repaint();
        }
    }

    public void resize()
    {
        width = this.getSize().width;
        height = this.getSize().height;
        gridWidth = width*8/10;
        panelX = gridWidth;
        panelWidth = width-gridWidth;
        buttonX = panelX+(panelWidth/10);
        buttonWidth = panelWidth*8/10;
        buttonHeight = height/10;
        grid.resize(gridWidth, height);
        selectStart.setBounds(buttonX, height/8, buttonWidth, buttonHeight);
        selectEnd.setBounds(buttonX, height/4, buttonWidth, buttonHeight);
        findPath.setBounds(buttonX, ((int)(height*(3.0/8))), buttonWidth, buttonHeight);
        changeAlg.setBounds(buttonX, height/2, buttonWidth, buttonHeight);
        createMaze.setBounds(buttonX, ((int)(height*(5.0/8))), buttonWidth, buttonHeight);
        reset.setBounds(buttonX, ((int)(height*(3.0/4))), buttonWidth, buttonHeight);
        toggleLabel.setBounds(buttonX, ((int)(height*(7.0/8))), buttonWidth, buttonHeight);
    }

    public void setPathfinder()
    {
        pathfinders = new PathFindingAlgorithm[4];
        pathfinders[0] = new AStarAdj(grid);
        pathfinders[1] = new AStarDiag(grid);
        pathfinders[2] = new DijkstraAdj(grid);
        pathfinders[3] = new DijkstraDiag(grid);
    }

    public void actionPerformed(ActionEvent e)
    {
        if(e.getSource() == selectStart)
        {
            grid.selectStart();
            setPathfinder();
            mazeMaker = new MazeAlgorithm(grid);
            grid.clearGCost();
            grid.clearPath();
            grid.updateHeuristic();
        }
        else if(e.getSource() == selectEnd)
        {
            grid.selectEnd();
            setPathfinder();
            mazeMaker = new MazeAlgorithm(grid);
            grid.clearGCost();
            grid.clearPath();
            grid.updateHeuristic();
        }
        else if(e.getSource() == findPath)
        {
            ArrayList<Square> path = pathfinders[pathIndex].apply();
            for(Square s : path)
                grid.setCellPath(grid.find(s));
        }
        else if(e.getSource() == changeAlg)
        {
            pathIndex = (pathIndex + 1) % pathfinders.length;
            changeAlg.setLabel(pathfinders[pathIndex].toString());
            grid.clearPath();
        }
        else if(e.getSource() == createMaze)
        {
            mazeMaker.randomWalls();
        }
        else if(e.getSource() == reset)
        {
            int row = grid.getRow();
            int col = grid.getCol();
            grid = new Grid(0, 0, row, col, gridWidth, height);
            grid.updateHeuristic();
            setPathfinder();
            mazeMaker = new MazeAlgorithm(grid);
        }
        else if(e.getSource() == toggleLabel)
        {
            grid.toggleLabel();
        }
    }

    public void mouseClicked(MouseEvent e)
    {
        int mouseX = e.getX();
        int mouseY = e.getY();
        if(mouseX < gridWidth)
        {
            int row = mouseY / grid.getCellHeight();
            int col = mouseX / grid.getCellWidth();
            if(grid.isSelected(row, col))
            {
                grid.selectBlock(row, col);
            }
            if(row < grid.getRow() && col < grid.getCol())
            {
                grid.selectCell(row, col);
            }
        }
    }

    public void mouseDragged(MouseEvent e){}

    public void mouseMoved(MouseEvent e){}

    public void mousePressed(MouseEvent e){}

    public void mouseReleased(MouseEvent e){}

    public void mouseEntered(MouseEvent e){}

    public void mouseExited(MouseEvent e){}

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D g2D = (Graphics2D)g;
        g2D.setColor(Color.black);
        g2D.fillRect(0, 0, width, height);
        grid.draw(g2D);
        drawPanel(g2D);
    }

    public void drawPanel(Graphics2D g2D)
    {
        int titleFontSize = (panelWidth/6)/2;
        Font title = new Font("Arial", Font.BOLD, titleFontSize);
        g2D.setColor(Color.black);
        g2D.fillRect(panelX, 0, panelWidth, height);
        g2D.setColor(Color.white);
        drawCenteredString(g2D, "PathFinding Visualization", new Rectangle(panelX, 0, panelWidth, height/6), title);
    }

    public void drawCenteredString(Graphics2D g, String text, Rectangle rect, Font font) 
    {
        FontMetrics metrics = g.getFontMetrics(font);
        int x = rect.x + (rect.width - metrics.stringWidth(text)) / 2;
        int y = rect.y + ((rect.height - metrics.getHeight()) / 2) + metrics.getAscent();
        g.setFont(font);
        g.drawString(text, x, y);
    }
}