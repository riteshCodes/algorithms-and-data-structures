package lab;

/**
 * Aufgabe H1
 * Abgabe von: 
 * @author Florian Kosterhon, Matrikel Nummer:2291024
 * @author Ritesh Shrestha, Matrikel Nummer:2478287
 */

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import frame.TableEntry;

/**
 * Use this class to implement your own tests.
 */
class YourTests {

	@Test
	void test() {
		HashTable table = new HashTable(2);

		table.insert(new TableEntry("abcdefghi", "asd"));
		table.insert(new TableEntry("qwerty", "asd"));

		assertEquals(11, table.getCapacity());
		
		for (int i = 0; i < 6; i++) {
			table.insert(new TableEntry("id" + i, "asd"));
		}
		
		assertEquals(59, table.getCapacity());
		
		int hash_a = table.getHash_a();
		int hash_b = table.getHash_b();
		
		for (int i = 0; i < 34; i++) {
			table.insert(new TableEntry("id_" + i, "asd"));
		}
		assertEquals(307, table.getCapacity());
		assertTrue(hash_a != table.getHash_a() || hash_b != table.getHash_b());
		
		int hash = table.hash("abcdefghi");
		assertTrue(table.getEntryLists()[hash] != null);
		assertTrue(table.getEntryLists()[hash].length() != 0);
	}

}
