package com.kob.matchingsystem.service;

public interface MatchingService {
    String addPlayer(Integer userId,
                     Integer rating,
                     Integer botId,
                     Integer win,
                     Integer lose,
                     Integer draw);
    String removePlayer(Integer userId);
}
