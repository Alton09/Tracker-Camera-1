package com.ninjapiratestudios.trackercamera;

/**
 * This class should contain common methods and fields used for different
 * test classes
 */
public class BaseTest {
    protected final String UNIT_TEST_SETUP_ERROR = "There was an error while " +
            "setting up the test: ";
    protected final String UNIT_TEST_SUT_ERROR = "There was an error " +
            "while " +
            "executing the method under test: ";
    protected final String UNIT_TEST_EXECUTE_ERROR = "The test either failed," +
            " or there was an error while executing the test: ";
    protected final String UNIT_TEST_BEFORE_ERROR = "Error in @Before setup " +
            "method: ";
    protected final String EXPECTED_EXCEPTION = "Expected test exception";
}
