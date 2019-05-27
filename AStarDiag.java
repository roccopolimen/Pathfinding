import java.util.*;

public class AStarDiag implements PathFindingAlgorithm
{
    private Grid grid;
    private int startIndex, startR, startC;
    private int endIndex, endR, endC;
    private int rows, cols;
    private int adjWeight, diagWeight;

    public AStarDiag(Grid grid)
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

    public Square lowestFScore(ArrayList<Square> openList)
    {
        Square s = new Square();
        int lowestScore = Integer.MAX_VALUE;
        for(Square node : openList)
        {
            if(node.getFScore() < lowestScore || (node.getFScore() == lowestScore && node.getGCost() < s.getGCost()))
            {
                lowestScore = node.getFScore();
                s = node;
            }
        }
        return s;
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

    public ArrayList<Square> apply()
    {
        ArrayList<Square> path = new ArrayList<Square>(); //shows the final path of the nodes
        ArrayList<Square> openList = new ArrayList<Square>(); //open list for algorithm
        ArrayList<Square> closedList = new ArrayList<Square>(); //closed list for algorithm
        grid.updateHeuristic(); //sets the heuristic distance values for all tiles on the grid
        grid.clearGCost(); //sets all gCost to -1
        grid.clearPath(); //clears any former path if there was one
        Square currentNode = grid.get(startR, startC); //current best node to take from path
        currentNode.setGCost(0);
        do
        {
            closedList.add(currentNode); //add the best node to this path
            int currNodeIndex = grid.find(currentNode);
            int currNodeC = currNodeIndex % cols;
            int currNodeR = (currNodeIndex - currNodeC) / cols;
            for(Square neighbor : findBorderNodes(currNodeR, currNodeC)) //loops through all neighboring nodes that are passable
            {
                if(closedList.indexOf(neighbor) == -1 && openList.indexOf(neighbor) == -1)
                // if neighbor is not in the open or closed list
                {
                    neighbor.setGCost(currentNode.getGCost()+adjWeight); //sets G values for the new tiles in the openList
                    neighbor.setParent(currentNode); //makes the currentNode the parent of its neighbors
                    openList.add(neighbor); //adds the neighbors to the open list
                }
                else if(closedList.indexOf(neighbor) == -1)
                //if its in the open list already but not in the closed list, compare scores
                {
                    int origFScore = neighbor.getFScore();
                    int newFScore = neighbor.getHeuristic() + currentNode.getGCost()+adjWeight;
                    if(newFScore < origFScore)
                    {
                        neighbor.setGCost(currentNode.getGCost()+adjWeight); 
                        neighbor.setParent(currentNode);
                    }
                }
                //if it is already in the closed list, ignore the node
            }
            for(Square neighbor : findDiagonalNodes(currNodeR, currNodeC)) //loops through all diagonal neighboring nodes that are passable
            {
                if(closedList.indexOf(neighbor) == -1 && openList.indexOf(neighbor) == -1)
                // if neighbor is not in the open or closed list
                {
                    neighbor.setGCost(currentNode.getGCost()+diagWeight); //sets G values for the new tiles in the openList
                    neighbor.setParent(currentNode); //makes the currentNode the parent of its neighbors
                    openList.add(neighbor); //adds the neighbors to the open list
                }
                else if(closedList.indexOf(neighbor) == -1)
                //if its in the open list already but not in the closed list, compare scores
                {
                    int origFScore = neighbor.getFScore();
                    int newFScore = neighbor.getHeuristic() + currentNode.getGCost()+diagWeight;
                    if(newFScore < origFScore)
                    {
                        neighbor.setGCost(currentNode.getGCost()+diagWeight); 
                        neighbor.setParent(currentNode);
                    }
                }
                //if it is already in the closed list, ignore the node
            }
            currentNode = lowestFScore(openList);
            openList.remove(currentNode);
        }
        while(currentNode != grid.get(endR, endC) && openList.size() != 0);
        closedList.add(currentNode);
        path = backTrackList(closedList);
        return path;
    }
    
    public String toString()
    {
        return "A Star Diagonal";
    }
}