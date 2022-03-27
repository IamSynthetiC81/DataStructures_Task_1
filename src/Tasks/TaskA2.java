package Tasks;

import java.util.LinkedList;

import HelperClasses.coordinates;


public class TaskA2 {
    
    private final int TableSize;
    private final Object[] list;
    public int ElementCounter;
    
    /**
     * Τα δεδομένα εισάγονται στην δομή δεδομένων που αποτελείται (α) από ένα πίνακα
     * μεγέθους Μ και (β) συνδεδεμένες λίστες στοιχείων (x,y) σε κάθε θέση του πίνακα. Κάθε
     * συντεταγμένη (x,y), εισάγεται ως τελευταίο στοιχείο της λίστας με αρχή την θέση Η(x,y)
     * που υπολογίζεται ως Η(x,y) = (x*Ν + y)%Μ. Κάθε θέση του πίνακα περιέχει (εκτός από
     * την αρχή) και δείκτη στο τελευταίο στοιχείο της λίστας. Κατασκευάστε την κλάση Hash
     * με τις πράξεις
     * • Εισαγωγή (x,y). Υπολογίστε πρώτα την θέση του στοιχείου στον πίνακα και κάντε
     * μετάβαση την τελευταία θέση της λίστας για την εισαγωγή. Χρησιμοποιείστε το δείκτη
     * προς της τελευταία θέση της λίστας (μην κάνετε διάσχιση).
     * • Ερώτηση με όρισμα μία θέση (x,y). Επιστρέφει (α) αν η αναζήτηση είναι επιτυχημένη
     * ή όχι και (β) τον αριθμό συγκρίσεων με (x,y) που έγιναν κατά την διάρκεια της
     * αναζήτησης.
     */
    public TaskA2(int TableSize) {
        this.ElementCounter = 0;
        this.TableSize = TableSize;
        list = new Object[TableSize];
        init_list();
    }
    
    private void init_list(){
        for (int i = 0; i < list.length; i++ ) {
            list[i] = new LinkedList<coordinates>();
        }
    }
    
    private int hash(coordinates object) throws IndexOutOfBoundsException{
        int hash =  (int)(((long)object.X* object.Limit + object.Y)%TableSize);
        
        if (hash < 0 || hash > TableSize)
            throw new IndexOutOfBoundsException("Invalid index");
        
        return hash;
    }
    
    /**
     * Adds the specified data into the array
     * - It first uses the {@link #hash(coordinates)} to find the
     *   position into the array.
     * - It then adds the data into the tail of the
     *   LinkedList element of the array using the
     *   {@link LinkedList#add(Object)}
     * @param data Data to be inserted into the database
     */
    @SuppressWarnings("unchecked")
    public void add(coordinates data) {
        int index = hash(data);
        
        /*  Unchecked casting                                               */
        LinkedList<coordinates> target = (LinkedList<coordinates>) list[index];
        target.add(data);
        list[index] = target;
        
        ElementCounter++;
        
    }
    
    @SuppressWarnings("unchecked")
    public int query(coordinates data) {
        int counter = 1;
        
        /*  Unchecked casting                                               */
        LinkedList<coordinates> target = (LinkedList<coordinates>) list[hash(data)];
        
        for (int i = 0; i < target.size(); i++) {
            if (target.get(i).equals(data))
                return counter;
            counter++;
        }
        
        /* The specified data do not exist  */
        return -1;
    }
    
}



