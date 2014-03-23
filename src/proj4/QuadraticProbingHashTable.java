/**
 * @version 5/ 10/ 12
 * @author Nathan Hartmann <nhartm1@umbc.edu>
 * CMSC 341 - Spring 2012 - Project #4
 * Section 01
 */
package proj4;

//QuadraticProbing Hash table class
//
//CONSTRUCTION: an approximate initial size or default of 101
//
//******************PUBLIC OPERATIONS*********************
//bool insert( x )       --> Insert x
//bool remove( x )       --> Remove x
//bool contains( x )     --> Return true if x is present
//void makeEmpty( )      --> Remove all items


/**
 * Probing table implementation of hash tables.
 * Note that all "matching" is based on the equals method.
 *
 * @param <AnyType> the generic type
 * @author Mark Allen Weiss
 */
public class QuadraticProbingHashTable<AnyType>
{
 /**
  * Construct the hash table.
  */
 public QuadraticProbingHashTable( )
 {
     this( DEFAULT_TABLE_SIZE );
 }

 /**
  * Construct the hash table.
  * @param size the approximate initial size.
  */
 public QuadraticProbingHashTable( int size )
 {
	 if(!isPrime(size))
		 size = nextPrime(size);
	 
     allocateArray( size );
     makeEmpty( );
     collisions = 0;
 }

 /**
  * Instantiates a new quadratic probing hash table.
  *
  * @param size the size
  * @param collisions the collisions
  * @param a the a
  * @param active the active
  */
 public QuadraticProbingHashTable( int size, int collisions, AnyType[] a, boolean[] active)
 {
	 this(size);
	 
	 for(int i = 0; i < a.length; i++)
	 {
		 if(a[i] != null)
		 {
			 this.array[i] = new HashEntry<AnyType>( a[i], active[i], a[i].hashCode() );
			 currentSize++;
		 }
	 }
	 
	 this.collisions = collisions;
 }
 
 
 /**
  * Insert into the hash table. If the item is
  * already present, do nothing.
  * @param x the item to insert.
  */
 public void insert( AnyType x )
 {
         // Insert x as active
     int currentPos = findPos( x );
     
     if( isActive( currentPos ))
    	 return;

     array[ currentPos ] = new HashEntry<AnyType>( x );

         // Rehash; see Section 5.5
     if( ++currentSize > array.length / 2 )
         rehash( );
 }

 /**
  * Expand the hash table.
  */
 private void rehash( )
 {
     HashEntry<AnyType> [ ] oldArray = array;

         // Create a new double-sized, empty table
     allocateArray( nextPrime( 2 * oldArray.length ) );
     currentSize = 0;

         // Copy table over
     for( int i = 0; i < oldArray.length; i++ )
         if( oldArray[ i ] != null && oldArray[ i ].isActive )
             insert( oldArray[ i ].element );
 }

 /**
  * Method that performs quadratic probing resolution.
  * Assumes table is at least half empty and table length is prime.
  * @param x the item to search for.
  * @return the position where the search terminates.
  */
 private int findPos( AnyType x )
 {
     int offset = 1;
     int currentPos = myhash( x );

     while( array[ currentPos ] != null &&
             array[ currentPos ].hashValue != x.hashCode() ) 
     {    	 
    	 collisions++;
         currentPos += offset;  // Compute ith probe
         offset += 2;
         if( currentPos >= array.length )
             currentPos -= array.length;
     }
     
     return currentPos;
 }
 
 /**
  * Remove from the hash table.
  * @param x the item to remove.
  */
 public void remove( AnyType x )
 {
     int currentPos = findPos( x );
     if( isActive( currentPos ) )
         array[ currentPos ].isActive = false;
 } 
 
 /**
  * Find an item in the hash table.
  * @param x the item to search for.
  * @return the matching item.
  */
 public boolean contains( AnyType x )
 {
     int currentPos = findPos( x );
     return isActive( currentPos );
 }
 
 /**
  * Gets the.
  *
  * @param x the x
  * @return the any type
  */
 public AnyType get( AnyType x )
 {
	 int currentPos = findPos( x );
	 
	 if(isActive( currentPos ) && array[ currentPos ].hashValue == x.hashCode())
		 return array[ currentPos ].element;
	 
	 return null;
 }
 
 
 /**
  * Return true if currentPos exists and is active.
  * @param currentPos the result of a call to findPos.
  * @return true if currentPos is active.
  */
 private boolean isActive( int currentPos )
 {
     return array[ currentPos ] != null && array[ currentPos ].isActive;
 }

 /**
  * Make the hash table logically empty.
  */
 public void makeEmpty( )
 {
     currentSize = 0;
     for( int i = 0; i < array.length; i++ )
         array[ i ] = null;
 }

 /**
  * Capacity.
  *
  * @return the int
  */
 public int capacity()
 {
	 return this.array.length;
 }
 
 /**
  * Current size.
  *
  * @return the int
  */
 public int currentSize()
 {
	 return currentSize;
 }
 
 /**
  * Collisions.
  *
  * @return the int
  */
 public int collisions()
 {
	 return collisions;
 }
 
 
 /**
  * Myhash.
  *
  * @param x the x
  * @return the int
  */
private int myhash( AnyType x )
 {
     int hashVal = x.hashCode( );

     hashVal %= array.length;
     if( hashVal < 0 )
         hashVal += array.length;

     return hashVal;
 }
 
 /* (non-Javadoc)
  * @see java.lang.Object#toString()
  */
 public String toString()
 {
	String str = "";
	str += "``````````````\n";
	str += "**Hash_Table**\n";
	str += "Collision: " + this.collisions + "\n";
	str += "Num Elements: " + currentSize() + "\n";
	str += "Size: " + capacity() + "\n";
	str += String.format("Percentage Filled: %.2f", ((double) currentSize() / capacity()) * 100) + "%\n";
	str += "``````````````\n";
	for(int i = 0; i < array.length; i++)
	{
		str += "\n[ " + i + " ]\n--------";
		
		if(array[i] != null)
			str += "\n" + array[i].element.toString();
		
		
		else
			str += "\n###EMPTY###\n";
		
		str += "--------\n";
	}
	return str;	
 }
 
 /**
  * Export to file.
  *
  * @return the string
  */
 public String exportToFile()
 {
	 String str = array.length + " " + collisions + "\n";
	 
	 for(HashEntry<AnyType> e : array)
	 {		 
		 if(e != null)
			 str += e.isActive + " " + e.element.toString() + "\n";
		
		 else
			 str += " \n";
	 }
	 
	 return str;
 }

 
 /**
  * The Class HashEntry.
  *
  * @param <AnyType> the generic type
  */
 @SuppressWarnings("hiding")
private class HashEntry<AnyType>
 {
     
     /** The element. */
     public AnyType  element;   // the element
     
     /** The is active. */
     public boolean isActive;  // false if marked deleted
     
     /** The hash value. */
     public int hashValue;
         
     /**
      * Instantiates a new hash entry.
      *
      * @param e the e
      */
     public HashEntry( AnyType e )
     {
         this( e, true, e.hashCode() );
     }

     /**
      * Instantiates a new hash entry.
      *
      * @param x the x
      * @param i the i
      * @param h the h
      */
     public HashEntry( AnyType x, boolean i, int h)
     {
         element  = x;
         isActive = i;
         hashValue = h;
     }
 }

 /** The Constant DEFAULT_TABLE_SIZE. */
 private static final int DEFAULT_TABLE_SIZE = 11;

 /** The array. */
 private HashEntry<AnyType> [ ] array; // The array of elements
 
 /** The current size. */
 private int currentSize;              // The number of occupied cells

 /** The collisions. */
 private int collisions;

 /**
  * Internal method to allocate array.
  * @param arraySize the size of the array.
  */ 
  @SuppressWarnings("unchecked")
 private void allocateArray( int arraySize )
 {
     array = new HashEntry[ nextPrime( arraySize ) ];
 }

 /**
  * Internal method to find a prime number at least as large as n.
  * @param n the starting number (must be positive).
  * @return a prime number larger than or equal to n.
  */
 private static int nextPrime( int n )
 {
     if( n <= 0 )
         n = 3;

     if( n % 2 == 0 )
         n++;

     for( ; !isPrime( n ); n += 2 )
         ;

     return n;
 }

 /**
  * Internal method to test if a number is prime.
  * Not an efficient algorithm.
  * @param n the number to test.
  * @return the result of the test.
  */
 private static boolean isPrime( int n )
 {
     if( n == 2 || n == 3 )
         return true;

     if( n == 1 || n % 2 == 0 )
         return false;

     for( int i = 3; i * i <= n; i += 2 )
         if( n % i == 0 )
             return false;

     return true;
 }


 // Simple main
 /**
  * The main method.
  *
  * @param args the arguments
  * @throws TicTacToeBoardException the tic tac toe board exception
  * @throws TicTacToePlayException the tic tac toe play exception
  */
 public static void main( String [ ] args ) throws TicTacToeBoardException, TicTacToePlayException
 {     
     QuadraticProbingHashTable<Tx3Board> T = new QuadraticProbingHashTable<Tx3Board>( );     

     Tx3Board gameOne = new Tx3Board("OO..XX.X.");
     Tx3Board gameTwo = new Tx3Board("...O.....");
     Tx3Board gameThree = new Tx3Board(".OOXXO.XX");
     
     System.out.println(gameOne.hashCode());
     System.out.println(gameOne + "\n");
     
     System.out.println(gameTwo.hashCode());
     System.out.println(gameTwo + "\n");
     
     System.out.println(gameThree.hashCode());
     System.out.println(gameThree + "\n");
     
     T.insert(gameOne);
     T.insert(gameTwo);
     T.insert(gameThree);
     
     System.out.println("________________\n");
     System.out.println(T);
     System.out.println("________________\n");
     
     //System.out.println(gameThree.equals(gameOne));
     System.out.println(T.get(gameThree));
    // System.out.println(T.get(new Tx3Board(".......X.")));
 }

}
