/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package twitter;

import java.time.Instant;
import java.util.*;

/**
 * Filter consists of methods that filter a list of tweets for those matching a
 * condition.
 * 
 * DO NOT change the method signatures and specifications of these methods, but
 * you should implement their method bodies, and you may add new public or
 * private methods or classes if you like.
 */
public class Filter {

    /**
     * Find tweets written by a particular user.
     * 
     * @param tweets
     *            a list of tweets with distinct ids, not modified by this method.
     * @param username
     *            Twitter username, required to be a valid Twitter username as
     *            defined by Tweet.getAuthor()'s spec.
     * @return all and only the tweets in the list whose author is username,
     *         in the same order as in the input list.
     */
    public static List<Tweet> writtenBy(List<Tweet> tweets, String username) {
        List<Tweet> result = new ArrayList<>();
        String targetUsername = username.toLowerCase(Locale.ROOT);
        for (Tweet tweet : tweets) {
            if (tweet.getAuthor().toLowerCase(Locale.ROOT).equals(targetUsername)) {
                result.add(tweet);
            }
        }
        return result;
    }

    /**
     * Find tweets that were sent during a particular timespan.
     * 
     * @param tweets
     *            a list of tweets with distinct ids, not modified by this method.
     * @param timespan
     *            timespan
     * @return all and only the tweets in the list that were sent during the timespan,
     *         in the same order as in the input list.
     */
    public static List<Tweet> inTimespan(List<Tweet> tweets, Timespan timespan) {
        List<Tweet> result = new ArrayList<>();
        Instant start = timespan.getStart();
        Instant end = timespan.getEnd();
        for (Tweet tweet : tweets) {
            Instant timestamp = tweet.getTimestamp();
            if (!timestamp.isBefore(start) && !timestamp.isAfter(end)) {
                result.add(tweet);
            }
        }
        return result;
    }

    /**
     * Find tweets that contain certain words.
     * 
     * @param tweets
     *            a list of tweets with distinct ids, not modified by this method.
     * @param words
     *            a list of words to search for in the tweets. 
     *            A word is a nonempty sequence of nonspace characters.
     * @return all and only the tweets in the list such that the tweet text (when 
     *         represented as a sequence of nonempty words bounded by space characters 
     *         and the ends of the string) includes *at least one* of the words 
     *         found in the words list. Word comparison is not case-sensitive,
     *         so "Obama" is the same as "obama".  The returned tweets are in the
     *         same order as in the input list.
     */
    public static List<Tweet> containing(List<Tweet> tweets, List<String> words) {
        List<Tweet> result = new ArrayList<>();
        if (words.isEmpty()) {
            return result; // No words to search for
        }

        // Normalize search words to lower case and remove duplicates
        Set<String> wordSet = new HashSet<>();
        for (String word : words) {
            String trimmed = word.trim().toLowerCase(Locale.ROOT);
            if (!trimmed.isEmpty()) {
                wordSet.add(trimmed);
            }
        }

        if (wordSet.isEmpty()) {
            return result; // After trimming, no valid words to search for
        }

        for (Tweet tweet : tweets) {
            String[] tweetWords = tweet.getText().toLowerCase(Locale.ROOT).split("\\W+");
            for (String word : tweetWords) {
                if (wordSet.contains(word)) {
                    result.add(tweet);
                    break; // Move to next tweet after finding a match
                }
            }
        }

        return result;
    }

}
