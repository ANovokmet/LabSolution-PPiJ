package com.swag.solutions;

/**
 * Created by Goran on 15.5.2015..
 */
public interface AbstractGoogleServices {
    public void signIn();
    public void signOut();
    public void rateGame();
    public void submitScore(long score);
    public void showScores();
    public boolean isSignedIn();
}
