import java.awt.*;

public class Square
{
    private int x, y, width, height;
    private boolean isPassable, isStart, isEnd, isSelected, isPath, showLabel;
    private int heuristic, gCost;
    private Square parent;
    public Square()
    {
        this.x = 0;
        this.y = 0;
        this.width = 0;
        this.height = 0;
        this.isPassable = true;
        this.isStart = false;
        this.isEnd = false;
        this.isSelected = false;
        this.isPath = false;
        this.heuristic = Integer.MAX_VALUE;
        this.gCost = -1;
        this.parent = null;
        this.showLabel = true;
    }

    public void draw(Graphics2D g2D)
    {
        if(!isPassable)
            g2D.setColor(Color.gray);
        else if(isStart)
            g2D.setColor(Color.green);
        else if(isEnd)
            g2D.setColor(Color.red);
        else if(isPath)
            g2D.setColor(Color.cyan);
        else
            g2D.setColor(Color.white);
        g2D.fillRect(x, y, width, height);

        if(isSelected)
            g2D.setColor(Color.yellow);
        else
            g2D.setColor(Color.black);
        g2D.drawRect(x, y, width, height);

        //Information
        if(showLabel)
        {
            g2D.setColor(Color.black);
            int fontSize = width/4;
            if(height < width)
                fontSize = height/4;
            Font font = new Font("Arial", 0, fontSize);
            FontMetrics metrics = g2D.getFontMetrics(font);
            g2D.setFont(font);
            if(heuristic != Integer.MAX_VALUE && isPassable)
                g2D.drawString("H: "+heuristic, x, y+fontSize);
            if(gCost != -1)
                g2D.drawString("G: "+gCost, x, y+metrics.getAscent()+fontSize);
        }
    }

    public void setX(int x)
    {
        this.x = x;
    }

    public void setY(int y)
    {
        this.y = y;
    }

    public void setWidth(int width)
    {
        this.width = width;
    }

    public void setHeight(int height)
    {
        this.height = height;
    }

    public void setPassable(boolean isPassable)
    {
        this.isPassable = isPassable;
    }

    public void setStart(boolean isStart)
    {
        this.isStart = isStart;
    }

    public void setEnd(boolean isEnd)
    {
        this.isEnd = isEnd;
    }

    public void setSelected(boolean isSelected)
    {
        this.isSelected = isSelected;
    }

    public void setPath(boolean isPath)
    {
        this.isPath = isPath;
    }

    public void setHeuristic(int heuristic)
    {
        this.heuristic = heuristic;
    }

    public void setGCost(int gCost)
    {
        this.gCost = gCost;
    }

    public void setParent(Square parent)
    {
        this.parent = parent;
    }

    public void toggleLabel()
    {
        this.showLabel = !this.showLabel;
    }

    public boolean getStart()
    {
        return isStart;
    }

    public boolean getEnd()
    {
        return isEnd;
    }

    public boolean getPassable()
    {
        return isPassable;
    }

    public boolean getSelected()
    {
        return isSelected;
    }

    public boolean getPath()
    {
        return isPath;
    }

    public int getHeuristic()
    {
        return heuristic;
    }

    public int getGCost()
    {
        return gCost;
    }

    public int getFScore()
    {
        return gCost + heuristic;
    }

    public Square getParent()
    {
        return parent;
    }
}