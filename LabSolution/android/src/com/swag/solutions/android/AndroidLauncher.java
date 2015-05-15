package com.swag.solutions.android;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Toast;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.android.gms.games.Games;
import com.google.example.games.basegameutils.GameHelper;
import com.swag.solutions.AbstractGoogleServices;
import com.swag.solutions.LabGame;
import com.swag.solutions.MainGame;
import com.swag.solutions.AbstractGoogleServices;

public class AndroidLauncher extends AndroidApplication implements AbstractGoogleServices {
    protected LabGame labGame;
    private GameHelper gameHelper;
    private final static int REQUEST_CODE_UNUSED = 9002;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        gameHelper = new GameHelper(this, GameHelper.CLIENT_GAMES);
        gameHelper.enableDebugLog(false);

        GameHelper.GameHelperListener gameHelperListener = new GameHelper.GameHelperListener()
        {
            @Override
            public void onSignInSucceeded()
            {
            }

            @Override
            public void onSignInFailed()
            {
            }
        };

        gameHelper.setup(gameHelperListener);

		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.useAccelerometer = true;

        labGame = new LabGame(this);
		initialize(labGame, config);
	}

    @Override
    protected void onPause(){
        super.onPause();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //labGame.pause();
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //labGame.pause();
        return true;
    }*/
    @Override
    protected void onStart()
    {
        super.onStart();
        gameHelper.onStart(this);
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        gameHelper.onStop();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        gameHelper.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void signIn() {
        try
        {
            runOnUiThread(new Runnable()
            {
                public void run()
                {
                    gameHelper.beginUserInitiatedSignIn();
                }
            });
        }
        catch (Exception e)
        {
            Gdx.app.log("Google Services", "Log in failed: " + e.getMessage());
        }
    }

    @Override
    public void signOut() {
        try {
            runOnUiThread(new Runnable() {
                //@Override
                public void run() {
                    gameHelper.signOut();
                }
            });
        }
        catch (Exception e)
        {
            Gdx.app.log("Google Services", "Log out failed: " + e.getMessage());
        }
    }

    @Override
    public void rateGame() {
        String str ="https://play.google.com/store/apps/details?id=com.swag.solutions";
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(str)));
    }

    @Override
    public void submitScore(long score) {
        if (isSignedIn() == true)
        {
            Games.Leaderboards.submitScore(gameHelper.getApiClient(), getString(R.string.leaderboard_id), score);
            startActivityForResult(Games.Leaderboards.getLeaderboardIntent(gameHelper.getApiClient(), getString(R.string.leaderboard_id)), REQUEST_CODE_UNUSED);
        }
        else
        {
            Toast.makeText(this, "Unable to submit score. You are not signed in.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void showScores() {
        if (isSignedIn() == true)
            startActivityForResult(Games.Leaderboards.getLeaderboardIntent(gameHelper.getApiClient(), getString(R.string.leaderboard_id)), REQUEST_CODE_UNUSED);
        else
        {

        }
    }

    @Override
    public boolean isSignedIn() {
        return gameHelper.isSignedIn();
    }
}
