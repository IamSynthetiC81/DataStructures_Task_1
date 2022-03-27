package Tasks;

import HelperClasses.coordinates;

import java.util.LinkedList;
import java.util.NoSuchElementException;

public class TaskA1 {
    
    LinkedList<coordinates> list = new LinkedList<>();
    
    /**
     * Adds the specified HelperClasses.coordinates to the list only if
     * they are not allready pressent.
     * @param coord Coordinates to add.
     * @return Return true if the HelperClasses.coordinates were not previously
     *          present and where added, and false if the where
     *          present and the addition was skipped.
     */
    public boolean insert(coordinates coord) {
        if(list.contains(coord)) return false;
        
        list.add(coord);
        
        return true;
        
    }
    
    /**
     * Returns whether the query was successful, and if it was, it
     * returns the amount of comparisons performed during the search
     * @param coord Coordinate object.
     * @return Returns the amount of comparisons performed during the query.
     * @throws NoSuchElementException When the HelperClasses.coordinates specified are not
     *                                included in the array.
     */
    public int query(coordinates coord) throws NoSuchElementException {
            return list.indexOf(coord);
        }
}
