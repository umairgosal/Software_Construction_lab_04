package twitter;

import static org.junit.Assert.*;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

public class FilterTest {

    /*
     * Testing Strategy for Filter Class:
     *
     * writtenBy():
     * - Test with single tweet by author
     * - Test with multiple tweets by author
     * - Test with mixed authors
     * - Test with different cases
     * - Test with no tweets by author
     * - Test with empty tweet list
     *
     * inTimespan():
     * - Test with all tweets within timespan
     * - Test with some tweets within timespan
     * - Test with no tweets within timespan
     * - Test with tweets on the boundaries
     * - Test with empty tweet list
     * - Test with single tweet within and outside timespan
     *
     * containing():
     * - Test with single word match
     * - Test with multiple word matches
     * - Test with different cases
     * - Test with no matching words
     * - Test with empty words list
     * - Test with empty tweets list
     * - Test with tweets containing punctuation
     * - Test with duplicate words in words list
     * - Test with partial word matches
     */

    private static final Instant d1 = Instant.parse("2016-02-17T10:00:00Z");
    private static final Instant d2 = Instant.parse("2016-02-17T11:00:00Z");
    private static final Instant d3 = Instant.parse("2016-02-17T12:00:00Z");
    private static final Instant d4 = Instant.parse("2016-02-17T13:00:00Z");

    private static final Tweet tweet1 = new Tweet(1, "alyssa", "is it reasonable to talk about rivest so much?", d1);
    private static final Tweet tweet2 = new Tweet(2, "bbitdiddle", "rivest talk in 30 minutes #hype", d2);
    private static final Tweet tweet3 = new Tweet(3, "Alyssa", "I love programming in Java!", d3);
    private static final Tweet tweet4 = new Tweet(4, "carl", "Java is versatile.", d4);
    private static final Tweet tweet5 = new Tweet(5, "diana", "@alyssa Have you seen the latest updates?", d1);
    private static final Tweet tweet6 = new Tweet(6, "Eve", "Check out my new blog at eve.blog.com", d2);
    private static final Tweet tweet7 = new Tweet(7, "frank", "JavaScript is different from Java.", d3);
    private static final Tweet tweet8 = new Tweet(8, "Gina", "JAVA is powerful!", d4);
    private static final Tweet tweet9 = new Tweet(9, "helen", "I enjoy hiking and outdoor activities.", d1);

    // Test that assertions are enabled
    @Test(expected = AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // Ensure that assertions are enabled with VM argument: -ea
    }

    // -------------------- Tests for writtenBy() --------------------

    // Test with single tweet by author
    @Test
    public void testWrittenBySingleTweet() {
        List<Tweet> result = Filter.writtenBy(Arrays.asList(tweet1), "alyssa");
        assertEquals("Expected one tweet authored by alyssa", 1, result.size());
        assertTrue("Expected result to contain tweet1", result.contains(tweet1));
    }

    // Test with multiple tweets by author
    @Test
    public void testWrittenByMultipleTweets() {
        Tweet tweetA = new Tweet(10, "john", "Hello world!", d1);
        Tweet tweetB = new Tweet(11, "john", "Learning Java.", d2);
        Tweet tweetC = new Tweet(12, "john", "Writing unit tests.", d3);
        List<Tweet> tweets = Arrays.asList(tweetA, tweetB, tweetC);
        List<Tweet> result = Filter.writtenBy(tweets, "john");
        assertEquals("Expected three tweets authored by john", 3, result.size());
        assertTrue("Expected result to contain tweetA", result.contains(tweetA));
        assertTrue("Expected result to contain tweetB", result.contains(tweetB));
        assertTrue("Expected result to contain tweetC", result.contains(tweetC));
    }

    // Test with mixed authors
    @Test
    public void testWrittenByMixedAuthors() {
        List<Tweet> tweets = Arrays.asList(tweet1, tweet2, tweet3, tweet4, tweet5);
        List<Tweet> result = Filter.writtenBy(tweets, "alyssa");
        assertEquals("Expected two tweets authored by alyssa", 2, result.size());
        assertTrue("Expected result to contain tweet1", result.contains(tweet1));
        assertTrue("Expected result to contain tweet3", result.contains(tweet3));
    }

    // Test with different cases (case-insensitive)
    @Test
    public void testWrittenByDifferentCases() {
        List<Tweet> tweets = Arrays.asList(tweet1, tweet3, tweet8);
        List<Tweet> resultLower = Filter.writtenBy(tweets, "alyssa");
        List<Tweet> resultUpper = Filter.writtenBy(tweets, "ALYSSA");
        List<Tweet> resultMixed = Filter.writtenBy(tweets, "AlYsSa");

        // Assuming writtenBy is case-insensitive
        assertEquals("Expected two tweets authored by alyssa (lower case)", 2, resultLower.size());
        assertTrue("Expected result to contain tweet1", resultLower.contains(tweet1));
        assertTrue("Expected result to contain tweet3", resultLower.contains(tweet3));

        assertEquals("Expected two tweets authored by ALYSSA (upper case)", 2, resultUpper.size());
        assertTrue("Expected result to contain tweet1", resultUpper.contains(tweet1));
        assertTrue("Expected result to contain tweet3", resultUpper.contains(tweet3));

        assertEquals("Expected two tweets authored by AlYsSa (mixed case)", 2, resultMixed.size());
        assertTrue("Expected result to contain tweet1", resultMixed.contains(tweet1));
        assertTrue("Expected result to contain tweet3", resultMixed.contains(tweet3));
    }

    // Test with no tweets by author
    @Test
    public void testWrittenByNoMatches() {
        List<Tweet> tweets = Arrays.asList(tweet2, tweet4, tweet6);
        List<Tweet> result = Filter.writtenBy(tweets, "alyssa");
        assertTrue("Expected empty result when no tweets are authored by alyssa", result.isEmpty());
    }

    // Test with empty tweet list
    @Test
    public void testWrittenByEmptyList() {
        List<Tweet> result = Filter.writtenBy(Collections.emptyList(), "alyssa");
        assertTrue("Expected empty result for empty tweet list", result.isEmpty());
    }

    // -------------------- Tests for inTimespan() --------------------

    // Test with all tweets within timespan
    @Test
    public void testInTimespanAllWithin() {
        Instant start = Instant.parse("2016-02-17T09:00:00Z");
        Instant end = Instant.parse("2016-02-17T14:00:00Z");
        Timespan timespan = new Timespan(start, end);

        List<Tweet> tweets = Arrays.asList(tweet1, tweet2, tweet3, tweet4, tweet5, tweet6, tweet7, tweet8, tweet9);
        List<Tweet> result = Filter.inTimespan(tweets, timespan);

        assertEquals("Expected all tweets within timespan", tweets.size(), result.size());
        assertTrue("Expected all tweets to be in result", result.containsAll(tweets));
    }


    // Test with no tweets within timespan
    @Test
    public void testInTimespanNoneWithin() {
        Instant start = Instant.parse("2016-02-17T14:00:00Z");
        Instant end = Instant.parse("2016-02-17T15:00:00Z");
        Timespan timespan = new Timespan(start, end);

        List<Tweet> tweets = Arrays.asList(tweet1, tweet2, tweet3, tweet4, tweet5, tweet6, tweet7, tweet8, tweet9);
        List<Tweet> result = Filter.inTimespan(tweets, timespan);

        assertTrue("Expected empty result when no tweets are within timespan", result.isEmpty());
    }

    // Test with tweets on the boundaries
    @Test
    public void testInTimespanBoundary() {
        Instant start = d1; // 10:00
        Instant end = d2;   // 11:00
        Timespan timespan = new Timespan(start, end);

        List<Tweet> tweets = Arrays.asList(tweet1, tweet2, tweet5, tweet6);
        List<Tweet> expected = Arrays.asList(tweet1, tweet2, tweet5, tweet6);
        List<Tweet> result = Filter.inTimespan(tweets, timespan);

        assertEquals("Expected all tweets on boundaries to be included", expected.size(), result.size());
        assertTrue("Expected all boundary tweets to be in result", result.containsAll(expected));
    }

    // Test with empty tweet list
    @Test
    public void testInTimespanEmptyList() {
        Instant start = Instant.parse("2016-02-17T09:00:00Z");
        Instant end = Instant.parse("2016-02-17T14:00:00Z");
        Timespan timespan = new Timespan(start, end);

        List<Tweet> result = Filter.inTimespan(Collections.emptyList(), timespan);
        assertTrue("Expected empty result for empty tweet list", result.isEmpty());
    }

    // Test with single tweet within timespan
    @Test
    public void testInTimespanSingleTweetWithin() {
        Instant start = Instant.parse("2016-02-17T09:00:00Z");
        Instant end = Instant.parse("2016-02-17T11:00:00Z");
        Timespan timespan = new Timespan(start, end);

        List<Tweet> tweets = Arrays.asList(tweet2);
        List<Tweet> result = Filter.inTimespan(tweets, timespan);

        assertEquals("Expected one tweet within timespan", 1, result.size());
        assertTrue("Expected result to contain tweet2", result.contains(tweet2));
    }

    // Test with single tweet outside timespan
    @Test
    public void testInTimespanSingleTweetOutside() {
        Instant start = Instant.parse("2016-02-17T14:00:00Z");
        Instant end = Instant.parse("2016-02-17T15:00:00Z");
        Timespan timespan = new Timespan(start, end);

        List<Tweet> tweets = Arrays.asList(tweet1);
        List<Tweet> result = Filter.inTimespan(tweets, timespan);

        assertTrue("Expected empty result when single tweet is outside timespan", result.isEmpty());
    }

    // -------------------- Tests for containing() --------------------

    // Test with single word match
    @Test
    public void testContainingSingleWord() {
        List<String> words = Arrays.asList("Java");
        List<Tweet> tweets = Arrays.asList(tweet1, tweet3, tweet4, tweet7, tweet8, tweet9);
        List<Tweet> expected = Arrays.asList(tweet3, tweet4, tweet7, tweet8);
        List<Tweet> result = Filter.containing(tweets, words);

        assertEquals("Expected four tweets containing 'Java'", expected.size(), result.size());
        assertTrue("Expected result to contain tweet3", result.contains(tweet3));
        assertTrue("Expected result to contain tweet4", result.contains(tweet4));
        assertTrue("Expected result to contain tweet7", result.contains(tweet7));
        assertTrue("Expected result to contain tweet8", result.contains(tweet8));
    }

    // Test with multiple word matches
    @Test
    public void testContainingMultipleWords() {
        List<String> words = Arrays.asList("Java", "rivest");
        List<Tweet> tweets = Arrays.asList(tweet1, tweet2, tweet3, tweet4, tweet5, tweet7, tweet8, tweet9);
        List<Tweet> expected = Arrays.asList(tweet1, tweet2, tweet3, tweet4, tweet7, tweet8);
        List<Tweet> result = Filter.containing(tweets, words);

        assertEquals("Expected six tweets containing 'Java' or 'rivest'", expected.size(), result.size());
        assertTrue("Expected result to contain tweet1", result.contains(tweet1));
        assertTrue("Expected result to contain tweet2", result.contains(tweet2));
        assertTrue("Expected result to contain tweet3", result.contains(tweet3));
        assertTrue("Expected result to contain tweet4", result.contains(tweet4));
        assertTrue("Expected result to contain tweet7", result.contains(tweet7));
        assertTrue("Expected result to contain tweet8", result.contains(tweet8));
    }

    // Test with different cases (case-insensitive)
    @Test
    public void testContainingDifferentCases() {
        List<String> words = Arrays.asList("java", "RIVEST");
        List<Tweet> tweets = Arrays.asList(tweet1, tweet2, tweet3, tweet4, tweet5, tweet7, tweet8, tweet9);
        List<Tweet> expected = Arrays.asList(tweet1, tweet2, tweet3, tweet4, tweet7, tweet8);
        List<Tweet> result = Filter.containing(tweets, words);

        assertEquals("Expected six tweets containing 'java' or 'RIVEST' (case-insensitive)", expected.size(), result.size());
        assertTrue("Expected result to contain tweet1", result.contains(tweet1));
        assertTrue("Expected result to contain tweet2", result.contains(tweet2));
        assertTrue("Expected result to contain tweet3", result.contains(tweet3));
        assertTrue("Expected result to contain tweet4", result.contains(tweet4));
        assertTrue("Expected result to contain tweet7", result.contains(tweet7));
        assertTrue("Expected result to contain tweet8", result.contains(tweet8));
    }

    // Test with no matching words
    @Test
    public void testContainingNoMatches() {
        List<String> words = Arrays.asList("Python", "Ruby");
        List<Tweet> tweets = Arrays.asList(tweet1, tweet2, tweet3, tweet4, tweet5, tweet7, tweet8, tweet9);
        List<Tweet> result = Filter.containing(tweets, words);

        assertTrue("Expected empty result when no tweets contain the specified words", result.isEmpty());
    }

    // Test with empty words list
    @Test
    public void testContainingEmptyWordsList() {
        List<String> words = Collections.emptyList();
        List<Tweet> tweets = Arrays.asList(tweet1, tweet2, tweet3);
        List<Tweet> result = Filter.containing(tweets, words);

        assertTrue("Expected empty result when words list is empty", result.isEmpty());
    }

    // Test with empty tweets list
    @Test
    public void testContainingEmptyTweetsList() {
        List<String> words = Arrays.asList("Java");
        List<Tweet> result = Filter.containing(Collections.emptyList(), words);

        assertTrue("Expected empty result when tweets list is empty", result.isEmpty());
    }

    // Test with tweets containing punctuation
    @Test
    public void testContainingWithPunctuation() {
        List<String> words = Arrays.asList("blog");
        List<Tweet> tweets = Arrays.asList(tweet5, tweet6);
        List<Tweet> expected = Arrays.asList(tweet6);
        List<Tweet> result = Filter.containing(tweets, words);

        assertEquals("Expected one tweet containing 'blog'", expected.size(), result.size());
        assertTrue("Expected result to contain tweet6", result.contains(tweet6));
    }

    // Test with duplicate words in words list
    @Test
    public void testContainingDuplicateWords() {
        List<String> words = Arrays.asList("Java", "java", "Rivest", "RIVEST");
        List<Tweet> tweets = Arrays.asList(tweet1, tweet2, tweet3, tweet4, tweet5, tweet7, tweet8, tweet9);
        List<Tweet> expected = Arrays.asList(tweet1, tweet2, tweet3, tweet4, tweet7, tweet8);
        List<Tweet> result = Filter.containing(tweets, words);

        assertEquals("Expected six tweets containing 'Java' or 'Rivest' with duplicate words handled", expected.size(), result.size());
        assertTrue("Expected result to contain tweet1", result.contains(tweet1));
        assertTrue("Expected result to contain tweet2", result.contains(tweet2));
        assertTrue("Expected result to contain tweet3", result.contains(tweet3));
        assertTrue("Expected result to contain tweet4", result.contains(tweet4));
        assertTrue("Expected result to contain tweet7", result.contains(tweet7));
        assertTrue("Expected result to contain tweet8", result.contains(tweet8));
    }

    // Test with partial word matches
    @Test
    public void testContainingPartialWordMatches() {
        List<String> words = Arrays.asList("talk");
        List<Tweet> tweets = Arrays.asList(tweet1, tweet2, tweet3);
        List<Tweet> expected = Arrays.asList(tweet1, tweet2);
        List<Tweet> result = Filter.containing(tweets, words);

        assertEquals("Expected two tweets containing the word 'talk'", expected.size(), result.size());
        assertTrue("Expected result to contain tweet1", result.contains(tweet1));
        assertTrue("Expected result to contain tweet2", result.contains(tweet2));
    }

    // Test with words appearing multiple times in a tweet
    @Test
    public void testContainingWordsMultipleOccurrences() {
        List<String> words = Arrays.asList("java");
        Tweet tweetMultiple = new Tweet(13, "harry", "Java Java Java!", d1);
        List<Tweet> tweets = Arrays.asList(tweetMultiple, tweet3);
        List<Tweet> expected = Arrays.asList(tweetMultiple, tweet3);
        List<Tweet> result = Filter.containing(tweets, words);

        assertEquals("Expected two tweets containing the word 'java' multiple times", expected.size(), result.size());
        assertTrue("Expected result to contain tweetMultiple", result.contains(tweetMultiple));
        assertTrue("Expected result to contain tweet3", result.contains(tweet3));
    }


}
