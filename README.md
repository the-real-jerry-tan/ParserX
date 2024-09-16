# ParserX Utility

## DelimitedInputParser Utility

## Overview

The `ParserX` is a revolutionary parsing utility that is robust enough to handle situations even where the payload content itself actually contains delimiter characters.

It is designed to parse input strings based on two specified delimiters. The parser first attempts to split the input string using the first delimiter, and if the number of fields doesn't match the expected count, it switches to the second delimiter. It also handles cases where delimiters might exist within field values themselves.

This utility is particularly useful in scenarios like parsing CSV or pipe-separated files where fields may contain delimiters. The parser ensures that the expected number of fields is met, and intelligently switches between delimiters when necessary.

## Features

- Supports two configurable delimiters for parsing.
- Handles cases where delimiters exist within field values.
- Provides robust error handling when the expected field count doesn't match.
- Utilizes Java's functional programming paradigms like Streams and `reduce()` to simplify logic.
- Built-in logging using SLF4J and Logback.

## Build Instructions

This project uses Gradle as the build system. The `build.gradle.kts` file configures the dependencies for SLF4J and Logback for logging and JUnit for unit testing.

### Steps to Build

1. Clone this repository:
    ```bash
    git clone https://github.com/the-real-jery-tan/ParserX.git
    ```
2. Navigate to the project directory:
    ```bash
    cd Parser
    ```
3. Build the project:
    ```bash
    ./gradlew build
    ```
4. Run the tests:
    ```bash
    ./gradlew test
    ```

### Dependencies

- **Java 11 or higher**: Ensure that Java 11+ is installed on your machine.
- **SLF4J and Logback**: For logging.
- **JUnit 5**: For unit testing.

## Testing

The project includes a comprehensive suite of tests covering:

- **Plain Vanilla Tests**: Regular, straightforward use cases where the delimiters work as expected.
- **Edge Cases**: Scenarios where the input may contain more or fewer fields than expected, or delimiters within the field values.
- **Oddball Cases**: Uncommon cases such as an empty input string or a single field with no delimiters.
- **Corner Cases**: Inputs where delimiters are at the start or end of the string, repeated delimiters, and so on.
- **Boundary Cases**: Tests for both very large and very small inputs to ensure performance and correctness.

### Running Tests

Run all unit tests using the following command:
```bash
./gradlew test
```
## Next Steps

- **Support for More Delimiters**: Extend the parser to support more than two delimiters.
- **Quoted Fields Support**: Add support for quoted field values as seen in standard CSV parsers (e.g., fields containing commas should be enclosed in quotes).
- **Performance Optimization**: Improve performance for parsing large datasets by optimizing memory usage and parsing logic.
- **Error Reporting**: Improve error handling to give more detailed feedback on parsing failures, including exact field counts and positions where errors occurred.

## Why This Utility is Important

- **Flexible Parsing**: This utility is highly flexible and can handle a wide range of scenarios involving delimited data, including cases where delimiters exist within field values.
- **Easy to Extend**: With a clear design, the utility is easy to extend for other delimiters or more complex parsing rules, such as supporting quoted fields or different escaping mechanisms.
- **Reusable in Data Processing Pipelines**: This parser can be reused in various data-processing pipelines where CSV, pipe-separated, or other delimited formats are common.
- **Reliability in Data Handling**: Robust error handling ensures that the utility can be used confidently in scenarios where data integrity is critical, such as financial or log data parsing.




