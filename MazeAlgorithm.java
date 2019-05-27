public class MazeAlgorithm
{
    private Grid grid;
    private int startIndex, startR, startC;
    private int endIndex, endR, endC;
    private int rows, cols;
    public MazeAlgorithm(Grid grid)
    {
        this.grid = grid;
        this.startIndex = grid.findStart();
        this.endIndex = grid.findEnd();
        this.startC = startIndex % grid.getCol();
        this.startR = (startIndex - startC) / grid.getCol();
        this.endC = endIndex % grid.getCol();
        this.endR = (endIndex - endC) / grid.getCol();
        this.rows = grid.getRow();
        this.cols = grid.getCol();
    }

    public void createMaze()
    {

    }

    public void randomWalls()
    {
        double chance = .1;
        for(int r = 0; r < rows; r++)
            for(int c = 0;c < cols; c++)
                if(Math.random() < chance)
                    if(grid.get(r, c) != grid.get(startR, startC) && grid.get(r, c) != grid.get(endR, endC))
                        grid.get(r, c).setPassable(false);
    }
}