package twitter;

import static org.junit.Assert.*;
import java.time.Instant;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.junit.Test;

public class ExtractTest {

    private static final Instant d1 = Instant.parse("2016-02-17T10:00:00Z");
    private static final Instant d2 = Instant.parse("2016-02-17T11:00:00Z");
    private static final Instant d3 = Instant.parse("2016-02-17T09:00:00Z");

    private static final Tweet tweet1 = new Tweet(1, "alyssa", "is it reasonable to talk about rivest so much?", d1);
    private static final Tweet tweet2 = new Tweet(2, "bbitdiddle", "rivest talk in 30 minutes #hype", d2);
    private static final Tweet tweet3 = new Tweet(3, "alyssa", "hey @bitdiddle, check this out!", d1);
    private static final Tweet tweet4 = new Tweet(4, "bbitdiddle", "email me at bob@mit.edu @Alyssa", d3);
    private static final Tweet tweet5 = new Tweet(5, "charlie", "@Bitdiddle @Alyssa", d2);

    // Test that assertions are enabled
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // Ensure that assertions are enabled with VM argument: -ea
    }

    // Test getTimespan for empty list of tweets
    @Test
    public void testGetTimespanEmpty() {
        Timespan timespan = Extract.getTimespan(Arrays.asList());
        assertNull("expected null timespan for empty list", timespan);
    }

    // Test getTimespan for one tweet
    @Test
    public void testGetTimespanOneTweet() {
        Timespan timespan = Extract.getTimespan(Arrays.asList(tweet1));
        assertEquals("expected same start and end time", d1, timespan.getStart());
        assertEquals("expected same start and end time", d1, timespan.getEnd());
    }

    // Test getTimespan for two tweets with increasing times
    @Test
    public void testGetTimespanTwoTweets() {
        Timespan timespan = Extract.getTimespan(Arrays.asList(tweet1, tweet2));
        assertEquals("expected start", d1, timespan.getStart());
        assertEquals("expected end", d2, timespan.getEnd());
    }

    // Test getTimespan for tweets in non-chronological order
    @Test
    public void testGetTimespanNonChronological() {
        Timespan timespan = Extract.getTimespan(Arrays.asList(tweet2, tweet3));
        assertEquals("expected start", d1, timespan.getStart());
        assertEquals("expected end", d2, timespan.getEnd());
    }

    // Test getMentionedUsers for a single tweet with no mentions
    @Test
    public void testGetMentionedUsersNoMention() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet1));
        assertTrue("expected empty set", mentionedUsers.isEmpty());
    }

    // Test getMentionedUsers for a single tweet with one mention
    @Test
    public void testGetMentionedUsersOneMention() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet3));
        assertEquals("expected one mention", new HashSet<>(Arrays.asList("bitdiddle")), mentionedUsers);
    }

    // Test getMentionedUsers for multiple mentions in one tweet
    @Test
    public void testGetMentionedUsersMultipleMentions() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet5));
        assertEquals("expected two mentions", new HashSet<>(Arrays.asList("bitdiddle", "alyssa")), mentionedUsers);
    }

    // Test getMentionedUsers for multiple tweets with mentions
    @Test
    public void testGetMentionedUsersMultipleTweets() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet3, tweet4, tweet5));
        assertEquals("expected two mentions ignoring case", new HashSet<>(Arrays.asList("alyssa", "bitdiddle")), mentionedUsers);
    }

    // Test getMentionedUsers for mentions with invalid formatting (e.g., email addresses)
    @Test
    public void testGetMentionedUsersInvalidMentions() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet4));
        assertEquals("expected one mention ignoring invalid formatting", new HashSet<>(Arrays.asList("alyssa")), mentionedUsers);
    }
}
