package HelperClasses;

import Interfaces.Convertable;
import Interfaces.Paging;

import java.io.EOFException;
import java.io.IOException;

public class Element <G extends Paging>{
    /** Head of the PageLinkedList  |   1Kb     */
    private Page Head;
    /** Tail of the PageLinkedList  |   1Kb     */
    private Page Tail;
    /** Filename of the file        | negligible*/
    String TargetFileName;
    /* Rough estimation of size in bytes : 2Kb
        For each element only on page is going
        to be writable at a time. Each inactive
        page has a roughly estimated size of
        about 30 bytes, which is negligible.    */
    
    private final G Superclass;
    
    /**
     * Each Element holds two {@link Page Pages}, one is the {@link #Head} and
     * the other is the {@link #Tail}. This allows for a <i>linked list approach</i>
     * to Page Handling.
     * <p>All Queries, Writes or Reads are requested from the {@link Page}, and handled within</p>
     * @param FileName Name of the file where the data are going to be stored.
     */
    public Element(String FileName, G supperclass){
        /*  Assigning FileName                  */
        this.TargetFileName = FileName;
        /* Constructing Head                    */
        Head = new Page(TargetFileName, getPageCounter(supperclass));
        /* Linking Tail                         */
        Tail = Head;
        
        this.Superclass = supperclass;
    }

    /**
     * Getter for the {@link   }.
     * <p><b>This increments the counter so this must only be called when creating a new page</b></p>
     * @return {@link   }.
     */
    public int getPageCounter(G superclass){
        return superclass.getPageCounter();
    }

    /**
     * Appends data to the {@link Page Pages buffer}.
     * @implNote The data are not written into the file
     * unless the buffer is filled.
     * @param data Data to be appended.
     * @param <T> Must implement {@link Convertable}.
     */
    public <T extends Convertable> int Append(T data) throws IOException {
        int DataExists;
        
        try{
            DataExists = Tail.Append(data);
        }catch (EOFException e){
            /* There is no more room in current page    */
            
            /* Create new Page                          */
            Tail.NextPage = new Page(TargetFileName, getPageCounter(Superclass));
            
            /*  Reassign Tail                           */
            Tail = Tail.NextPage;
            
            /* Retry Appending data                     */
            DataExists = Append(data);
        }
        
        return DataExists;
    }
    
    public <T extends Convertable> int Query(T data) throws IOException {
        Page Pointer = Head;
        int Counter = 0;
        int Query;
        while(Pointer != null){
            /*  Perform Query                               */
            Query = Pointer.Query(data);
            if(Query != -1) {   /*  Data found  */
                return Counter + Query;
            }
            
            /*  Increment Pointer                           */
            Pointer = Pointer.NextPage;
            
            /*  Increment counter                           */
            Counter++;
        }
        
        /*  Data have not been found                        */
        return -1;
    }
    
    public Page getHead(){
        return Head;
    }
    
    public void setTail(Page tail){
        this.Tail = tail;
    }

}