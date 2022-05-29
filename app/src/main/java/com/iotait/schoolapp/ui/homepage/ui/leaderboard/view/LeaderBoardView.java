package com.iotait.schoolapp.ui.homepage.ui.leaderboard.view;

import com.iotait.schoolapp.ui.homepage.ui.leaderboard.model.LeaderBoardModel;

public interface LeaderBoardView {
    void OnLeaderBoardItemClickAllTime(LeaderBoardModel leaderBoardModel);

    void OnLeaderBoardItemClickMonthlyTop(LeaderBoardModel leaderBoardModel);

    void OnLeaderBoardItemClickWeeklyTop(LeaderBoardModel leaderBoardModel);

    void OnLeaderBoardItemClickWeeklyContestTop(LeaderBoardModel leaderBoardModel);
}
