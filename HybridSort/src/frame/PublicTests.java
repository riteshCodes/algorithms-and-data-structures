package frame;

import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.ValueSource;

import lab.Card;
import lab.Card.Suit;
import lab.HybridOptimizer;
import lab.HybridSort;
import lab.HybridSortRandomPivot;

/**
 * Do NOT change anything in this class!
 * 
 * The test cases defined by this class are used to test if the input file was
 * correctly sorted. This class is also responsible for outputting to the
 * console.
 * 
 */

@DisplayName("QuickSort tests")
class PublicTests {

	protected static int NrOfTestFiles;
	protected int correct = 0;
	protected Duration timeout = Duration.ofSeconds(10);

	protected boolean sortingTester(SortArray records) {
		boolean sorted = true;
		Card lastRecord = records.getElementAt(0);
		//System.out.println(lastRecord);
		for (int i = 1; i < records.getNumberOfItems() && sorted; i++) {
			Card currentRecord = records.getElementAt(i);
			//System.out.println(currentRecord);
			sorted = (currentRecord.value > lastRecord.value)
					|| ((currentRecord.value == lastRecord.value)
							&& ((currentRecord.suit == Suit.Clubs) 
									|| ((currentRecord.suit == Suit.Spades) && (lastRecord.suit != Suit.Clubs))
									|| ((currentRecord.suit == Suit.Hearts) && ((lastRecord.suit == Suit.Hearts)
																			|| (lastRecord.suit == Suit.Diamonds)))
									|| ((currentRecord.suit == Suit.Diamonds) && (lastRecord.suit == Suit.Diamonds))));
			lastRecord = currentRecord;
		}
		return sorted;
	}
	
	@Nested
	@TestInstance(Lifecycle.PER_CLASS)
	@DisplayName("Card.compareTo tests")
	class A_CardTests {
		
		@DisplayName("various Tests")
		@Test
		public void tests() {
			try {
				Card card1 = new Card(15, Suit.Hearts);
				Card card2 = new Card(10, Suit.Spades);
				Card card3 = new Card(-10, Suit.Clubs);
				Card card4 = new Card(14, Suit.Diamonds);
				Card card5 = new Card(0, Suit.Hearts);
				Card card6 = new Card(-0, Suit.Hearts);
				
				assertEquals(1, card1.compareTo(card2));
				assertEquals(-1, card2.compareTo(card1));
				assertEquals(1, card1.compareTo(card3));
				assertEquals(-1, card3.compareTo(card1));
				assertEquals(1, card1.compareTo(card4));
				assertEquals(-1, card4.compareTo(card1));
				assertEquals(1, card1.compareTo(card5));
				assertEquals(0, card5.compareTo(card6));
				assertEquals(-1, card3.compareTo(card2));
			} catch (Exception e) {
				System.out.println("Error: An exception was thrown: " + e.getMessage());
				throw e;
			}
		}
		
		@DisplayName("Tests with same value")
		@Test
		public void sameValueTests() {
			try {
				Card card1 = new Card(-128, Suit.Hearts);
				Card card2 = new Card(-128, Suit.Diamonds);
				Card card3 = new Card(-128, Suit.Spades);
				Card card4 = new Card(-128, Suit.Clubs);
				
				assertEquals(1, card1.compareTo(card2));
				assertEquals(-1, card1.compareTo(card3));
				assertEquals(-1, card1.compareTo(card4));
				assertEquals(1, card3.compareTo(card2));
				assertEquals(-1, card2.compareTo(card4));
				assertEquals(1, card4.compareTo(card3));
			} catch (Exception e) {
				System.out.println("Error: An exception was thrown: " + e.getMessage());
				throw e;
			}
		}
	}

	@Nested
	@TestInstance(Lifecycle.PER_CLASS)
	@DisplayName("HybridSort Sorting")
	class B_HybridSortSortingTest {

		@BeforeAll
		public void init() {
			correct = 0;
			System.out.println("Starting HybridSort tests!");
		}

		@AfterAll
		public void tearDown() {
			System.out.println("Correct Hybrid sortings: " + correct + " out of " + 6 + " tests\n");
		}

		@DisplayName("Tests")
		@ParameterizedTest(name = "HybridSort sorting test with input: {0}")
		@ValueSource(strings = { "tests/public/TestFile1", "tests/public/TestFile2", "tests/public/TestFile3" })
		public void testHybridSort(String inputFile) {
			CardTestfileReader reader = new CardTestfileReader(inputFile);
			SortArray records = new SortArray(reader.readFile());
			HybridSort sortAlgorithm = new HybridSort();
			assertTimeoutPreemptively(timeout, () -> {
				try {
					sortAlgorithm.sort(records, 10);
				} catch (Exception e) {
					System.out.println("Error: An exception was thrown: " + e.getMessage());
					throw e;
				}
			}, () -> {
				System.out.println("HybridSort [" + inputFile + "]: Execution timed out after: " + timeout.getSeconds()
						+ " seconds");
				return "Test failed!";
			});
			int readOps = records.getReadingOperations();
			int writeOps = records.getWritingOperations();
			assertTrue(sortingTester(records), () -> {
				System.out.println("HybridSort [" + inputFile + "]: Wrong order!");
				return "Test failed!";
			});
			System.out.println(
					"HybridSort [" + inputFile + "]: Correct order! Read Ops: " + readOps + "; Write Ops: " + writeOps);
			correct++;
		}
		
		@DisplayName("Tests with k=0")
		@ParameterizedTest(name = "HybridSort sorting test (k=0) with input: {0}")
		@ValueSource(strings = { "tests/public/TestFile1", "tests/public/TestFile2", "tests/public/TestFile3" })
		public void testHybridSortk0(String inputFile) {
			CardTestfileReader reader = new CardTestfileReader(inputFile);
			SortArray records = new SortArray(reader.readFile());
			HybridSort sortAlgorithm = new HybridSort();
			assertTimeoutPreemptively(timeout, () -> {
				try {
					sortAlgorithm.sort(records, 0);
				} catch (Exception e) {
					System.out.println("Error: An exception was thrown: " + e.getMessage());
					throw e;
				}
			}, () -> {
				System.out.println("HybridSort, k=0 [" + inputFile + "]: Execution timed out after: " + timeout.getSeconds()
						+ " seconds");
				return "Test failed!";
			});
			int readOps = records.getReadingOperations();
			int writeOps = records.getWritingOperations();
			assertTrue(sortingTester(records), () -> {
				System.out.println("HybridSort, k=0 [" + inputFile + "]: Wrong order!");
				return "Test failed!";
			});
			System.out.println(
					"HybridSort, k=0 [" + inputFile + "]: Correct order! Read Ops: " + readOps + "; Write Ops: " + writeOps);
			correct++;
		}
	}
	
	@Nested
	@TestInstance(Lifecycle.PER_CLASS)
	@DisplayName("HybridSortRandomPivot Sorting")
	class C_HybridSortRandomPivotSortingTest {

		@BeforeAll
		public void init() {
			correct = 0;
			System.out.println("Starting HybridSortRandomPivot tests!");
		}

		@AfterAll
		public void tearDown() {
			System.out.println("Correct Hybrid sortings: " + correct + " out of " + 6 + " tests\n");
		}

		@DisplayName("Tests")
		@ParameterizedTest(name = "HybridSortRandomPivot sorting test with input: {0}")
		@ValueSource(strings = { "tests/public/TestFile1", "tests/public/TestFile2", "tests/public/TestFile3" })
		public void testHybridSort(String inputFile) {
			CardTestfileReader reader = new CardTestfileReader(inputFile);
			SortArray records = new SortArray(reader.readFile());
			HybridSortRandomPivot sortAlgorithm = new HybridSortRandomPivot();
			assertTimeoutPreemptively(timeout, () -> {
				try {
					sortAlgorithm.sort(records, 10);
				} catch (Exception e) {
					System.out.println("Error: An exception was thrown: " + e.getMessage());
					throw e;
				}
			}, () -> {
				System.out.println("HybridSortRandomPivot [" + inputFile + "]: Execution timed out after: " + timeout.getSeconds()
						+ " seconds");
				return "Test failed!";
			});
			int readOps = records.getReadingOperations();
			int writeOps = records.getWritingOperations();
			assertTrue(sortingTester(records), () -> {
				System.out.println("HybridSortRandomPivot [" + inputFile + "]: Wrong order!");
				return "Test failed!";
			});
			System.out.println(
					"HybridSortRandomPivot [" + inputFile + "]: Correct order! Read Ops: " + readOps + "; Write Ops: " + writeOps);
			correct++;
		}
		
		@DisplayName("Tests with k=0")
		@ParameterizedTest(name = "HybridSortRandomPivot sorting test (k=0) with input: {0}")
		@ValueSource(strings = { "tests/public/TestFile1", "tests/public/TestFile2", "tests/public/TestFile3" })
		public void testHybridSortk0(String inputFile) {
			CardTestfileReader reader = new CardTestfileReader(inputFile);
			SortArray records = new SortArray(reader.readFile());
			HybridSortRandomPivot sortAlgorithm = new HybridSortRandomPivot();
			assertTimeoutPreemptively(timeout, () -> {
				try {
					sortAlgorithm.sort(records, 0);
				} catch (Exception e) {
					System.out.println("Error: An exception was thrown: " + e.getMessage());
					throw e;
				}
			}, () -> {
				System.out.println("HybridSortRandomPivot, k=0 [" + inputFile + "]: Execution timed out after: " + timeout.getSeconds()
						+ " seconds");
				return "Test failed!";
			});
			int readOps = records.getReadingOperations();
			int writeOps = records.getWritingOperations();
			assertTrue(sortingTester(records), () -> {
				System.out.println("HybridSortRandomPivot, k=0 [" + inputFile + "]: Wrong order!");
				return "Test failed!";
			});
			System.out.println(
					"HybridSortRandomPivot, k=0 [" + inputFile + "]: Correct order! Read Ops: " + readOps + "; Write Ops: " + writeOps);
			correct++;
		}
	}

	@Nested
	@TestInstance(Lifecycle.PER_CLASS)
	@DisplayName("HybridSort Complexity")
	class D_HybridSortComplexityTest {

		@BeforeAll
		public void init() {
			correct = 0;
			System.out.println("Starting HybridSort complexity tests!");
		}

		@AfterAll
		public void tearDown() {
			System.out.println(
					"Passed complexity tests for HybridSort: " + correct + " out of " + 3 + " tests\n");
		}

		private void complexityTesterA(SortArray records, String inputFile, int readOps) {
			int n = records.getNumberOfItems();
			double nlogn = n * (Math.log(n) / Math.log(2)) * 5;
			assertTrue(readOps > 0);
			if (inputFile.contains("TestFile1") || inputFile.contains("TestFile2")) {
				assertTrue(readOps < nlogn, () -> {
					System.out.println("HybridSort complexity test failed for file: " + inputFile
							+ " - complexity out of allowed range: O(nlog(n)) required!");
					return "Test failed!";
				});
			} else {
				assertTrue(readOps > Math.pow(n, 2) / 2, () -> {
					System.out.println("HybridSort complexity test failed for file: " + inputFile
							+ " - complexity out of allowed range: O(n^2) required!");
					return "Test failed!";
				});
			}
			correct++;
		}

		@DisplayName("Tests")
		@ParameterizedTest(name = "HybridSort complexity test with input: {0}")
		@ValueSource(strings = { "tests/public/TestFile1", "tests/public/TestFile2", "tests/public/TestFile3" })
		public void testQuicksortAComplexity(String inputFile) {
			CardTestfileReader reader = new CardTestfileReader(inputFile);
			SortArray records = new SortArray(reader.readFile());
			HybridSort sortAlgorithm = new HybridSort();
			assertTimeoutPreemptively(timeout, () -> {
				try {
					sortAlgorithm.sort(records, 10);
				} catch (Exception e) {
					System.out.println("Error: An exception was thrown: " + e.getMessage());
					throw e;
				}
			}, () -> {
				System.out.println("Complexity HybridSort [" + inputFile + "]: Execution timed out after: "
						+ timeout.getSeconds() + " seconds");
				return "Test failed!";
			});
			int readOps = records.getReadingOperations();
			complexityTesterA(records, inputFile, readOps);
			System.out.println("Complexity HybridSort [" + inputFile + "]: Complexity within allowed range!");
		}
	}
	
	@Nested
	@TestInstance(Lifecycle.PER_CLASS)
	@DisplayName("HybridOprimizer")
	class E_HybridOptimizer {
		
		@BeforeAll
		public void init() {
			correct = 0;
			System.out.println("Starting HybridOptimizer tests!");
		}

		@AfterAll
		public void tearDown() {
			System.out.println(
					"Passed complexity tests for HybridOptimizer: " + correct + " out of " + 2 + " tests\n");
		}
		
		@DisplayName("Test HybridOptimizer")
		@ParameterizedTest(name = "HybridOptimizer test with input: {0}")
		@ValueSource(strings = { "tests/public/TestFile1", "tests/public/TestFile2" })
		public void testOptimizer(String inputFile) {
			CardTestfileReader reader = new CardTestfileReader(inputFile);
			ArrayList<Card> testData = reader.readFile();
			assertTimeoutPreemptively(timeout,() -> {
				try {
					int result = HybridOptimizer.optimize(testData);
					assertTrue( result >= 0 && result <= testData.size(), () -> {
						System.out.println( "HybridOptimizer: " + inputFile + ": Result is not in valid range.");
						return "Test failed!";
					});
					
					System.out.println("HybridOptimizer: " + inputFile + ": Optimal k is " + result);
					correct++;
				} catch (Exception e) {
					System.out.println("Error: An exception was thrown: " + e.getMessage());
					throw e;
				}
			}, () -> {
				System.out.println("HybridOptimizer: Execution timed out after "+timeout.getSeconds() +" seconds");
				return "Test failed!";
			});
		}
	}
}