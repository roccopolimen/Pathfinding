import java.awt.*;

public class Grid
{
    private Square [][] grid;
    private int x, y, row, col, width, height, cellWidth, cellHeight;
    private int selected;
    public Grid(int x, int y, int row, int col, int width, int height)
    {
        this.x = x;
        this.y = y;
        this.row = row;
        this.col = col;
        this.width = width;
        this.height = height;
        this.cellWidth = width/col;
        this.cellHeight = height/row;
        this.grid = new Square [row][col];
        this.selected = -1;
        makeEmptyGrid();
    }

    public void makeEmptyGrid()
    {
        for(int r = 0; r < row; r++)
        {
            for(int c = 0; c < col; c++)
            {
                grid[r][c] = new Square();
                grid[r][c].setX(x+c*cellWidth);
                grid[r][c].setY(y+r*cellHeight);
                grid[r][c].setWidth(cellWidth);
                grid[r][c].setHeight(cellHeight);
            }
        }
        grid[0][0].setStart(true);
        grid[row-1][col-1].setEnd(true);
    }

    public void resize(int width, int height)
    {
        this.width = width;
        this.height = height;
        this.cellWidth = width/col;
        this.cellHeight = height/row;
        for(int r = 0; r < row; r++)
        {
            for(int c = 0; c < col; c++)
            {
                grid[r][c].setX(x+c*cellWidth);
                grid[r][c].setY(y+r*cellHeight);
                grid[r][c].setWidth(cellWidth);
                grid[r][c].setHeight(cellHeight);
            }
        }
    }

    public void updateHeuristic()
    {
        int endIndex = findEnd();
        int endC = endIndex%col;
        int endR = (endIndex - endC) / col;

        for(int r = 0; r < row; r++)
            for(int c = 0;c < col; c++)
                grid[r][c].setHeuristic(Math.abs(endR - r) + Math.abs(endC - c));
    }

    public void clearGCost()
    {
        for(int r = 0; r < row; r++)
            for(int c = 0;c < col; c++)
                grid[r][c].setGCost(-1);
    }

    public int find(Square s)
    {
        for(int r = 0; r < row; r++)
            for(int c = 0; c < col; c++)
                if(grid[r][c].equals(s))
                    return r*col+c;
        return -1;
    }

    public void setCellPath(int index)
    {
        int c = index % col;
        int r = (index - c) / col;
        grid[r][c].setPath(true);
    }

    public void clearPath()
    {
        for(int r = 0; r < row; r++)
            for(int c= 0 ;c < col; c++)
                grid[r][c].setPath(false);
    }

    public void selectCell(int row, int col)
    {
        int c = selected%this.col;
        int r = (selected - c) / this.col;
        if(selected != -1)
            grid[r][c].setSelected(false);
        grid[row][col].setSelected(true);
        selected = row*this.col + col;
    }

    public void selectBlock(int r, int c)
    {
        if(!grid[r][c].getStart() && !grid[r][c].getEnd())
            grid[r][c].setPassable(!grid[r][c].getPassable());
    }

    public void toggleLabel()
    {
        for(int r = 0; r < row; r++)
            for(int c = 0;c < col; c++)
                grid[r][c].toggleLabel();
    }

    public int findStart()
    {
        for(int r = 0; r < row; r++)
            for(int c = 0; c < col; c++)
                if(grid[r][c].getStart())
                    return (r*col + c);
        return -1;
    }

    public int findEnd()
    {
        for(int r = 0; r < row; r++)
            for(int c = 0; c < col; c++)
                if(grid[r][c].getEnd())
                    return (r*col + c);
        return -1;
    }

    public void selectStart()
    {
        if(selected != -1)
        {
            int c = selected%col;
            int r = (selected - c) / col;
            int currStart = findStart();
            int currStartC = currStart%col;
            int currStartR = (currStart - currStartC) / col;
            grid[currStartR][currStartC].setStart(false);
            grid[r][c].setStart(true);
        }
    }

    public void selectEnd()
    {
        if(selected != -1)
        {
            int c = selected%col;
            int r = (selected - c) / col;
            int currEnd = findEnd();
            int currEndC = currEnd%col;
            int currEndR = (currEnd - currEndC) / col;
            grid[currEndR][currEndC].setEnd(false);
            grid[r][c].setEnd(true);
        }
    }

    public void draw(Graphics2D g2D)
    {
        for(int r = 0; r < grid.length; r++)
            for(int c = 0; c < grid[r].length; c++)
                grid[r][c].draw(g2D);
        //draws the selected grid again, so the highlight is visible
        if(selected != -1)
        {
            int c = selected%col;
            int r = (selected - c) / col;
            grid[r][c].draw(g2D);
        }
    }

    public int getRow()
    {
        return row;
    }

    public int getCol()
    {
        return col;
    }

    public int getCellWidth()
    {
        return cellWidth;
    }

    public int getCellHeight()
    {
        return cellHeight;
    }

    public boolean isSelected(int row, int col)
    {
        return selected == row*this.col+col;
    }

    public Square get(int row, int col)
    {
        return grid[row][col];
    }
}