/**
 * © 2024 Jerry Tan. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Jerry Tan
 * ("Confidential Information"). You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms under which this software
 * was distributed or otherwise published, and solely for the prior express purposes
 * explicitly communicated and agreed upon, and only for those specific permissible purposes.
 *
 * This software is provided "AS IS," without a warranty of any kind. All express or implied
 * conditions, representations, and warranties, including any implied warranty of merchantability,
 * fitness for a particular purpose, or non-infringement, are disclaimed, except to the extent
 * that such disclaimers are held to be legally invalid.
 */
package tan.jerry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ParserX {

    private static final Logger logger = LoggerFactory.getLogger(ParserX.class);

    private final String input;
    private final String delimiter1;
    private final String delimiter2;
    private final int expectedFieldCount;

    // Constructor to initialize the parser with input, delimiters, and expected field count
    public ParserX(String input, String delimiter1, String delimiter2, int expectedFieldCount) {
        this.input = input;
        this.delimiter1 = delimiter1;
        this.delimiter2 = delimiter2;
        this.expectedFieldCount = expectedFieldCount;
    }

    /**
     * Public method to parse the input. Uses functional programming and streams to process the input.
     * Returns the fields parsed based on the input delimiters and expected field count.
     */
    public List<String> parse() {
        logger.info("Parsing input with delimiter1: '{}' and delimiter2: '{}'", delimiter1, delimiter2);

        // Try parsing with the first delimiter
        List<String> firstSplit = splitInput(input, delimiter1);

        if (firstSplit.size() == expectedFieldCount) {
            logger.debug("Successfully parsed input using delimiter1: '{}'", delimiter1);
            return firstSplit;
        }

        // If the first delimiter doesn't work, attempt the second one
        List<String> secondSplit = splitInput(input, delimiter2);
        if (secondSplit.size() != expectedFieldCount) {
            logger.error("Unable to parse input with expected number of fields: {}", expectedFieldCount);
            throw new IllegalArgumentException("Input cannot be parsed with the expected field count.");
        }

        // Resolve discrepancies using the functional paradigm
        return resolveDiscrepancy(firstSplit, secondSplit);
    }

    /**
     * Overloaded parse method with default delimiters (comma and pipe) for convenience.
     */
    public List<String> parse(String input, int expectedFieldCount) {
        return new ParserX(input, ",", "\\|", expectedFieldCount).parse();
    }

    /**
     * Overloaded parse method for parsing with a single delimiter.
     */
    public List<String> parseSingleDelimiter(String input, String delimiter, int expectedFieldCount) {
        List<String> split = splitInput(input, delimiter);
        if (split.size() == expectedFieldCount) {
            return split;
        } else {
            logger.error("Failed parsing with single delimiter '{}'", delimiter);
            throw new IllegalArgumentException("Input cannot be parsed with the expected field count using single delimiter.");
        }
    }

    /**
     * Private method to split the input based on a given delimiter using Streams.
     */
    private List<String> splitInput(String input, String delimiter) {
        return Arrays.stream(input.split(delimiter))
                .map(String::trim) // Clean the fields by trimming
                .collect(Collectors.toList());
    }

    /**
     * Private method to resolve discrepancies between the two splits using functional programming.
     */
    private List<String> resolveDiscrepancy(List<String> firstSplit, List<String> secondSplit) {
        logger.debug("Resolving discrepancies between two splits.");

        return Stream.iterate(0, i -> i + 1)
                .limit(firstSplit.size()) // Iterate through each index
                .map(i -> {
                    String fieldFromFirst = firstSplit.get(i);
                    if (i < secondSplit.size() && fieldFromFirst.equals(secondSplit.get(i))) {
                        return fieldFromFirst; // If fields match, return the field
                    } else {
                        // Merge fields when discrepancies arise
                        return mergeFields(firstSplit, i, secondSplit);
                    }
                })
                .collect(Collectors.toList());
    }

    /**
     * Helper method to merge fields from the two splits.
     */
    private String mergeFields(List<String> firstSplit, int startIndex, List<String> secondSplit) {
        logger.debug("Merging fields starting at index {}", startIndex);

        return secondSplit.stream()
                .skip(startIndex)
                .limit(expectedFieldCount - startIndex) // Limit to the remaining fields
                .reduce((field1, field2) -> field1 + delimiter1 + field2)
                .orElse(firstSplit.get(startIndex)); // Default to the first split field
    }

    public static void main(String[] args) {
        // Example usage
        String input = "Field1,Field2,Field3,Field4,Field5|Field6,Field7";
        String delimiter1 = ",";
        String delimiter2 = "\\|";
        int expectedFields = 7;

        // Create an instance of the parser and parse the input
        ParserX parser = new ParserX(input, delimiter1, delimiter2, expectedFields);
        List<String> parsedFields = parser.parse();

        // Log parsed fields
        parsedFields.forEach(field -> logger.info("Parsed field: {}", field));
    }
}
