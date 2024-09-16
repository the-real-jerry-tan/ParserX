package tan.jerry;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for ParserX class.
 * This class contains various test cases, including edge cases, corner cases,
 * oddball cases, vanilla cases, and equivalence partitioning logic to ensure robust testing.
 */
class ParserXTest {

    private static final Logger logger = LoggerFactory.getLogger(ParserXTest.class);

    private ParserX parser;

    @BeforeEach
    void setUp() {
        // Set up common test variables if needed
    }

    /**
     * Plain vanilla test case: simple comma-delimited input with expected fields.
     */
    @Test
    void testVanillaCaseCommaDelimiter() {
        parser = new ParserX("Field1,Field2,Field3", ",", "\\|", 3);
        List<String> result = parser.parse();
        assertEquals(3, result.size());
        assertEquals("Field1", result.get(0));
        assertEquals("Field2", result.get(1));
        assertEquals("Field3", result.get(2));
    }

    /**
     * Vanilla test with pipe delimiter and expected fields.
     */
    @Test
    void testVanillaCasePipeDelimiter() {
        parser = new ParserX("Field1|Field2|Field3", ",", "\\|", 3);
        List<String> result = parser.parse();
        assertEquals(3, result.size());
        assertEquals("Field1", result.get(0));
        assertEquals("Field2", result.get(1));
        assertEquals("Field3", result.get(2));
    }

    /**
     * Edge case: Input with extra delimiters that are valid in field values.
     * The second field contains a comma, but the parser should still return 3 fields.
     */
    @Test
    void testEdgeCaseExtraDelimiterInField() {
        parser = new ParserX("Field1,Field2 with,comma,Field3", ",", "\\|", 3);
        List<String> result = parser.parse();
        assertEquals(3, result.size());
        assertEquals("Field1", result.get(0));
        assertEquals("Field2 with,comma", result.get(1)); // Ensure delimiter inside a field is handled.
        assertEquals("Field3", result.get(2));
    }

    /**
     * Edge case: Missing fields (less fields than expected).
     * Should throw an exception as the input does not match the expected field count.
     */
    @Test
    void testEdgeCaseMissingFields() {
        parser = new ParserX("Field1,Field2", ",", "\\|", 3);
        assertThrows(IllegalArgumentException.class, parser::parse);
    }

    /**
     * Edge case: More fields than expected.
     * Should throw an exception because the input has more fields than specified.
     */
    @Test
    void testEdgeCaseExtraFields() {
        parser = new ParserX("Field1,Field2,Field3,Field4", ",", "\\|", 3);
        assertThrows(IllegalArgumentException.class, parser::parse);
    }

    /**
     * Oddball case: Input is a single field with no delimiter.
     * The input should return a list with only one field.
     */
    @Test
    void testOddballCaseSingleField() {
        parser = new ParserX("Field1", ",", "\\|", 1);
        List<String> result = parser.parse();
        assertEquals(1, result.size());
        assertEquals("Field1", result.get(0));
    }

    /**
     * Oddball case: Empty input string.
     * Should throw an exception due to invalid input.
     */
    @Test
    void testOddballCaseEmptyString() {
        parser = new ParserX("", ",", "\\|", 1);
        assertThrows(IllegalArgumentException.class, parser::parse);
    }

    /**
     * Corner case: Delimiter at the end of the input.
     * Should throw an exception due to invalid trailing delimiter.
     */
    @Test
    void testCornerCaseDelimiterAtEnd() {
        parser = new ParserX("Field1,Field2,", ",", "\\|", 3);
        assertThrows(IllegalArgumentException.class, parser::parse);
    }

    /**
     * Corner case: Delimiter at the beginning of the input.
     * Should throw an exception due to invalid leading delimiter.
     */
    @Test
    void testCornerCaseDelimiterAtBeginning() {
        parser = new ParserX(",Field1,Field2", ",", "\\|", 3);
        assertThrows(IllegalArgumentException.class, parser::parse);
    }

    /**
     * Corner case: Repeated delimiters in the input.
     * Should throw an exception due to invalid input with consecutive delimiters.
     */
    @Test
    void testCornerCaseRepeatedDelimiters() {
        parser = new ParserX("Field1,,Field3", ",", "\\|", 3);
        assertThrows(IllegalArgumentException.class, parser::parse);
    }

    /**
     * Equivalence partitioning: Input with a mix of delimiters, but valid.
     * The input contains both comma and pipe delimiters but should still parse correctly.
     */
    @Test
    void testEquivalencePartitioningMixedDelimiters() {
        parser = new ParserX("Field1,Field2|Field3", ",", "\\|", 3);
        List<String> result = parser.parse();
        assertEquals(3, result.size());
        assertEquals("Field1", result.get(0));
        assertEquals("Field2", result.get(1));
        assertEquals("Field3", result.get(2));
    }

    /**
     * Equivalence partitioning: Mix of delimiters with a field containing the delimiter.
     * Tests if the parser can correctly handle delimiters within field values.
     */
    @Test
    void testEquivalencePartitioningFieldWithDelimiter() {
        parser = new ParserX("Field1,Field2|Field3 with,comma", ",", "\\|", 3);
        List<String> result = parser.parse();
        assertEquals(3, result.size());
        assertEquals("Field1", result.get(0));
        assertEquals("Field2", result.get(1));
        assertEquals("Field3 with,comma", result.get(2));
    }

    /**
     * Boundary case: Very large input string.
     * Tests how the parser handles a very large number of fields.
     */
    @Test
    void testBoundaryCaseLargeInput() {
        StringBuilder largeInput = new StringBuilder();
        for (int i = 0; i < 10000; i++) {
            largeInput.append("Field").append(i).append(",");
        }
        // Remove last comma
        largeInput.deleteCharAt(largeInput.length() - 1);
        parser = new ParserX(largeInput.toString(), ",", "\\|", 10000);
        List<String> result = parser.parse();
        assertEquals(10000, result.size());
        assertEquals("Field9999", result.get(9999));
    }

    /**
     * Boundary case: Minimum possible input (empty field, no delimiters).
     * Tests the handling of a minimal input.
     */
    @Test
    void testBoundaryCaseMinimumInput() {
        parser = new ParserX("", ",", "\\|", 0);
        List<String> result = parser.parse();
        assertEquals(0, result.size());
    }

    /**
     * Test with both delimiters being the same.
     * Valid input with the same delimiters should work without issue.
     */
    @Test
    void testBothDelimitersSame() {
        parser = new ParserX("Field1,Field2,Field3", ",", ",", 3);
        List<String> result = parser.parse();
        assertEquals(3, result.size());
        assertEquals("Field1", result.get(0));
    }

    /**
     * Test with trailing delimiters.
     * Should throw an exception due to trailing comma.
     */
    @Test
    void testTrailingDelimiter() {
        parser = new ParserX("Field1,", ",", "\\|", 2);
        assertThrows(IllegalArgumentException.class, parser::parse);
    }
}
