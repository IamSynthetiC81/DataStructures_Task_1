# Έκθεση

## [Βοηθητικές Διεπαφές](/src/Interfaces/)

Στο πακέτο [Interfaces](/src/Interfaces/) περιέχονται Διεπαφές οι οποίες βοηθούν στην υλοποίηση μερών της εργασίας.



## Βοηθητικές Κλάσεις

Στο πακέτο [HelperClasses](/src/HelperClasses/) περιέχονται κλάσεις οι οποίεσ βοηθούν στην υλοποίηση της εργασίας.
- [ByteBuffer](/src/HelperClasses/ByteBuffer.java) - Μία κλάση που περιέχει ένα buffer απο bytes. Οι μεθόδοι που περιέχονται είναι οι εξής :
    - <code>int AttachToBuffer(T data)</code> - [Doc]()
    - <code>boolean Contains(T data) </code> - [Doc]()
    - <code>void clear()</code> - [Doc]()

- [coordinates](/src/HelperClasses/coordinates.java) - Αυτή η κλάση αντιπροσοπεύει τα εισαγόμενα δεδομένα. Περιέχει δύο <code>Integer</code>, το Χ και το Υ, και δύο μεθόδους :
    - <code>byte[] ToByte()</code> - Μέθοδος απο το Interface [Convertable](/src/Interfaces/Convertable.java). Μετατρέπει τα δεδομένα απο <code>Integer</code> σε ενα Binary Array (Όχι Byte Array).<p>Κάθε Integer καταλαμβάνει 16 bit το καθένα αφού παίρνει μονάχα θετικές τιμές, και μόλις μέχρι το $2^{16}$. Ετσι το κάθε Datapoint καταλαμβάνει μόλις 32-bit.</p>
    - <code>boolean equals(object o)</code> Overidden Μέθοδος για την ασφαλέστερη σύγκριση δύο Datapoints.
    - [Page](/src/HelperClasses/Page.java) - Η κλάση αυτή ένα Node μίας LinkedList, αλλά και μία σελίδα δεδομένων επάνω στην μνήμη, ή σε ένα File. Περιέχει ένα [ByteBuffer](/src/HelperClasses/ByteBuffer.java) όπου γράφονται τα δεδομένα, και ένα δείκτη προς την επόμενη σελίδα. Αρχικά τα δεδοομένα της σελίδας αποθηκεύονται στην μνήμη εώς ότου γεμίσει το buffer. Αφού γεμίσει, το buffer παραμένει ανοιχτό μονάχα μέχρι να εγγραφτούν τα αρχεία, έπειτα κλείνει. Περιέχει τις μεθόδους :
        - <code>int Read(byte[] b)</code> - Διαβάζει το περιεχόμενο της σελίδας απο το <u>αρχείο</u>, και το αποθηκευει στο <code>b</code>.
        - <code>void Write(byte[] b)</code> - Γράφει τα δεδομένα του <code>b</code> στον χώρο της σελίδας στο <u>αρχείο</u>.
        - <code><T extends Convertable> int Query(T data)</code> - Αναζητά τα δεδομένα <code>data</code> τόσο στο αρχείο, όσο και στο buffer που είναι αποθηκευμένο στην μνήμη. Επιστρέφει 0 εάν το datapoint βρησκόταν στην μνήμη, 1 εάν βρησκόταν στο αρχείο, και -1 εάν δεν βρησκόταν πουθενά.
        - <code><T extends Convertable> int Append(T data)</code> - Βάζει δεδομένα στο buffer του Page. Οταν το buffer γεμίσει, γράφει τα δεδομένα του buffer στο αρχείο, κλείνει το buffer ωστε να μήν γίνονται άλλες εγγραφές μέσο αυτού (Μονάχα μέσω της <code>Write()</code> πλέων), και ώστε να εξοικονομίσει χώρο στην μνήμη.
    - [Element](/src/HelperClasses/Element.java) - To Element Αντιπροσοπεύει μία LinkedList απο [Pages](/src/HelperClasses/Page.java). Περιέχει δύο Pages (Head και Tail), και δύο μεθόδους :
        - <code><T extends Convertable> int Append(T data)</code> - Εισάγει το datapoint στο Tail. Εάν δεν υπάρχει χώρος, δημειουργεί ένα καινουργιο tail, και εισάγει τα δεδομένα εκεί.
        - <code><T extends Convertable> int Query(T data)</code> - Ψάχνει το datapoint σε όλα τα Pages της λίστας, και  εαν βρεθεί, επιστρέφει τον αριθμό προσβάσεων αρχείο, διαφορετικά επιστρέφει -1.