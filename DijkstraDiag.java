import java.util.*;

public class DijkstraDiag implements PathFindingAlgorithm
{
    private Grid grid;
    private int startIndex, startR, startC;
    private int endIndex, endR, endC;
    private int rows, cols;
    private int adjWeight, diagWeight;
    public DijkstraDiag(Grid grid)
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
        this.adjWeight = 10;
        this.diagWeight = 14;
    }

    public ArrayList<Square> apply()
    {
        //setup of algorithm
        ArrayList<Square> path = new ArrayList<Square>(); //shows the final path of the nodes
        ArrayList<Square> visitedList = new ArrayList<Square>(); //open list for algorithm
        ArrayList<Square> unvisitedList = new ArrayList<Square>(); //closed list for algorithm
        grid.clearGCost(); //sets all gCost to -1
        grid.clearPath(); //clears any former path if there was one
        for(int r = 0; r < rows; r++)
        {
            for(int c = 0; c < cols; c++)
            {
                grid.get(r, c).setGCost(Integer.MAX_VALUE); //sets all distances to "infinity"
                if(grid.get(r, c) != grid.get(startR, startC))
                    unvisitedList.add(grid.get(r, c));
            }
        }
        Square currentNode = grid.get(startR, startC); //current best node to take from path
        currentNode.setGCost(0);

        //implementation of algorithm
        do
        {
            visitedList.add(currentNode); //add current node to visited list
            int currNodeIndex = grid.find(currentNode);
            int currNodeC = currNodeIndex % cols;
            int currNodeR = (currNodeIndex - currNodeC) / cols;
            for(Square neighbor : findBorderNodes(currNodeR, currNodeC))
            {
                if(visitedList.indexOf(neighbor) == -1) //not visited yet
                {
                    if((currentNode.getGCost()+adjWeight) < neighbor.getGCost()) //better path through this node
                    {
                        neighbor.setGCost(currentNode.getGCost() + adjWeight);
                        neighbor.setParent(currentNode);
                    }
                }
            }
            for(Square neighbor : findDiagonalNodes(currNodeR, currNodeC))
            {
                if(visitedList.indexOf(neighbor) == -1) //not visited yet
                {
                    if((currentNode.getGCost()+diagWeight) <neighbor.getGCost()) //better path through this node
                    {
                        neighbor.setGCost(currentNode.getGCost() + diagWeight);
                        neighbor.setParent(currentNode);
                    }
                }
            }
            currentNode = shortestDistance(unvisitedList);
            unvisitedList.remove(currentNode);
        }
        while(currentNode != grid.get(endR, endC) && unvisitedList.size() != 0);

        visitedList.add(currentNode);
        path = backTrackList(visitedList);
        return path;
    }

    public String toString()
    {
        return "Djikstra's Diagonal Algorithm";
    }

    public ArrayList<Square> findBorderNodes(int row, int col)
    {
        ArrayList<Square> temp = new ArrayList<Square>();
        if(row != 0)
            if(grid.get(row-1, col).getPassable())
                temp.add(grid.get(row-1, col));
        if(row != rows-1)
            if(grid.get(row+1, col).getPassable())
                temp.add(grid.get(row+1, col));
        if(col != 0)
            if(grid.get(row, col-1).getPassable())
                temp.add(grid.get(row, col-1));
        if(col != cols-1)
            if(grid.get(row, col+1).getPassable())
                temp.add(grid.get(row, col+1));
        return temp;
    }

    public ArrayList<Square> findDiagonalNodes(int row, int col)
    {
        ArrayList<Square> temp = new ArrayList<Square>();
        //sends in all valid diagonal nodes
        if(row != 0)
        {
            if(col != 0)
                if(grid.get(row-1, col-1).getPassable())
                    temp.add(grid.get(row-1, col-1));
            if((col+1) != cols)
                if(grid.get(row-1, col+1).getPassable())
                    temp.add(grid.get(row-1, col+1));
        }
        if((row+1) != rows)
        {
            if(col != 0)
                if(grid.get(row+1, col-1).getPassable())
                    temp.add(grid.get(row+1, col-1));
            if((col+1) != cols)
                if(grid.get(row+1, col+1).getPassable())
                    temp.add(grid.get(row+1, col+1));
        }
        return temp;
    }

    public ArrayList<Square> backTrackList(ArrayList<Square> cL)
    {
        ArrayList<Square> path = new ArrayList<Square>();
        Square currentNode = cL.get(cL.size()-1);
        do
        {
            currentNode = currentNode.getParent();
            path.add(currentNode);
        }
        while(currentNode != grid.get(startR, startC));
        path.remove(currentNode);
        return path;
    }

    public Square shortestDistance(ArrayList<Square> list)
    {
        Collections.shuffle(list);
        Square s = new Square();
        int lowest = Integer.MAX_VALUE;
        for(Square node : list)
        {
            if(node.getGCost() < lowest)
            {
                lowest = node.getGCost();
                s = node;
            }
        }
        return s;
    }
}