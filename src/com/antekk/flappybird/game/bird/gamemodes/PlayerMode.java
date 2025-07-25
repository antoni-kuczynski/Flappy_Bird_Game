package com.antekk.flappybird.game.bird.gamemodes;

import com.antekk.flappybird.game.bird.Bird;
import com.antekk.flappybird.game.pipes.PipeFormation;
import com.antekk.flappybird.game.player.FlappyBirdPlayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class PlayerMode implements GameMode {
    private ArrayList<FlappyBirdPlayer> players;
    private Bird playerControlledBird;

    @Override
    public void resetPosition() {
        playerControlledBird.resetPosition();
    }

    @Override
    public void flap() {
        playerControlledBird.flap();
    }

    @Override
    public boolean isBetweenPipes(PipeFormation pipeFormation) {
        return playerControlledBird.isBetweenPipes(pipeFormation);
    }

    @Override
    public boolean areAllBirdsDead() {
        return !playerControlledBird.isAlive;
    }

    @Override
    public Iterator<Bird> iterator() {
        return new Iterator<>() {
            int index = 0;

            @Override
            public boolean hasNext() {
                if(index < 1) {
                    index++;
                    return true;
                }
                return false;
            }

            @Override
            public Bird next() {
                return playerControlledBird;
            }
        };
    }

    @Override
    public void init() {
        playerControlledBird = new Bird();
    }

    @Override
    public boolean usesMachineLearning() {
        return false;
    }

    @Override
    public String toString() {
        return "Player mode";
    }

    @Override
    public int size() {
        return 1;
    }

    @Override
    public Bird getBirdAt(int index) {
        if(index > 0)
            throw new IllegalArgumentException("index" + index + " is higher than the size (1)");
        return playerControlledBird;
    }

    @Override
    public ArrayList<FlappyBirdPlayer> getPlayers() {
        if(players == null || players.isEmpty())
            players = new ArrayList<>(Arrays.asList(playerControlledBird.getPlayer()));
        return players;
    }

    @Override
    public FlappyBirdPlayer getBestPlayer() {
        return players.get(0);
    }

    @Override
    public Bird getPlayerControlledBird() {
        return playerControlledBird;
    }

    @Override
    public ArrayList<Bird> getBirds() {
        return new ArrayList<>(Arrays.asList(getPlayerControlledBird()));
    }
}
