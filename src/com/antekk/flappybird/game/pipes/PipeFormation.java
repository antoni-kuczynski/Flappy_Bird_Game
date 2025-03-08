package com.antekk.flappybird.game.pipes;

import com.antekk.flappybird.view.GamePanel;

import java.awt.*;

import static com.antekk.flappybird.game.GameController.getBlockSizePx;

public class PipeFormation {
    private TopPipe topPipe;
    private BottomPipe bottomPipe;


    public PipeFormation() {
        int hGap = 240; //144 36
        int minPipeHeight = getBlockSizePx();
        int dy = GamePanel.GROUND - GamePanel.TOP;

        int top = (int) (Math.random() * (dy - 2 * minPipeHeight - hGap)) + minPipeHeight;
        int bottom = dy - top - hGap;

        topPipe = new TopPipe(top);
        bottomPipe = new BottomPipe(bottom);
    }

    public void draw(Graphics g) {
        topPipe.draw(g);
        bottomPipe.draw(g);
    }


    public TopPipe getTopPipe() {
        return topPipe;
    }

    public BottomPipe getBottomPipe() {
        return bottomPipe;
    }
}
