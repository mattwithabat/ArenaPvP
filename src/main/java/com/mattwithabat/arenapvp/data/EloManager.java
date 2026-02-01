package com.mattwithabat.arenapvp.data;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class EloManager {
    private final ArenaPvP plugin;
    private final Map<UUID, Integer> ratings;
    private final int startingRating;
    private final int kFactor;

    public EloManager(ArenaPvP plugin) {
        this.plugin = plugin;
        this.ratings = new HashMap<>();
        this.startingRating = plugin.getConfig().getInt("elo.starting-rating", 1000);
        this.kFactor = plugin.getConfig().getInt("elo.k-factor", 32);
    }

    public int getRating(UUID uuid) {
        return ratings.getOrDefault(uuid, startingRating);
    }

    public void setRating(UUID uuid, int rating) {
        ratings.put(uuid, rating);
    }

    public void recordMatch(UUID winner, UUID loser) {
        int winnerRating = getRating(winner);
        int loserRating = getRating(loser);

        double expectedWinner = expectedScore(winnerRating, loserRating);
        double expectedLoser = expectedScore(loserRating, winnerRating);

        int newWinnerRating = (int) Math.round(winnerRating + kFactor * (1 - expectedWinner));
        int newLoserRating = (int) Math.round(loserRating + kFactor * (0 - expectedLoser));

        setRating(winner, newWinnerRating);
        setRating(loser, newLoserRating);
    }

    private double expectedScore(int ratingA, int ratingB) {
        return 1.0 / (1.0 + Math.pow(10, (ratingB - ratingA) / 400.0));
    }

    public Map<UUID, Integer> getTopRatings(int limit) {
        return ratings.entrySet().stream()
                .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
                .limit(limit)
                .collect(HashMap::new, (m, e) -> m.put(e.getKey(), e.getValue()), HashMap::putAll);
    }
}
