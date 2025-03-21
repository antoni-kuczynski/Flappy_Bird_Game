package com.antekk.flappybird.game.bird;

import com.antekk.flappybird.game.pipes.BottomPipe;
import com.antekk.flappybird.game.pipes.PipeFormation;
import com.antekk.flappybird.game.pipes.TopPipe;
import com.antekk.flappybird.view.GamePanel;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import static com.antekk.flappybird.view.GamePanel.getBlockSizePx;
import static com.antekk.flappybird.view.themes.GameColors.*;

public class Bird {
    private int spritePosX;
    private int spritePosY;
    private int spriteWidth;
    private int spriteHeight;
    private int hitboxPosX;
    private int hitboxPosY;
    private int hitboxWidth;
    private int hitboxHeight;
    public boolean isMovingUp = false;
    public int framesSinceBirdStartedMoving = 0;
    public int rotationAngle = 0;


    private static BufferedImage rotateImage(BufferedImage image, double angle) {
        int w = image.getWidth();
        int h = image.getHeight();
        double radians = Math.toRadians(angle);

        int newWidth = (int) Math.round(w * Math.abs(Math.cos(radians)) + h * Math.abs(Math.sin(radians)));
        int newHeight = (int) Math.round(h * Math.abs(Math.cos(radians)) + w * Math.abs(Math.sin(radians)));

        BufferedImage rotatedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = rotatedImage.createGraphics();

        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);

        AffineTransform transform = new AffineTransform();
        transform.translate((newWidth - w) / 2.0, (newHeight - h) / 2.0);
        transform.rotate(radians, w / 2.0, h / 2.0);
        g2d.setTransform(transform);

        g2d.drawImage(image, 0, 0, null);
        g2d.dispose();

        return rotatedImage;
    }

    public void resetPosition() {
        spritePosX = (int) ((GamePanel.getBoardCols() - 1.5) * getBlockSizePx() / 2);
        spritePosY = (GamePanel.getBoardRows() - 3) * getBlockSizePx() / 2;
        spriteWidth = (int) (1.7 * getBlockSizePx());
        spriteHeight = (int) (1.7 * getBlockSizePx());

        hitboxPosX = spritePosX + getBlockSizePx() / 2;
        hitboxPosY = spritePosY + getBlockSizePx() / 2;
        hitboxWidth = (int) (getBlockSizePx() * 0.75);
        hitboxHeight = (int) (getBlockSizePx() * 0.75);
    }

    public void flap() {
        framesSinceBirdStartedMoving = 0;
        isMovingUp = true;
    }

    public void draw(Graphics g) {
        //so that the bird doesnt flip when falling
        if(rotationAngle > 33) {
            rotationAngle = 33;
        }

        if(isMovingUp && framesSinceBirdStartedMoving != 0 || rotationAngle < 15) {
            BufferedImage img = rotateImage(birdUpFlap, -20);
            g.drawImage(
                    img,
                    getSpriteXPos(),
                    getSpritePosY(),
                    (int) (1.17 * getSpriteWidth()),
                    (int) (1.17 * getSpriteHeight()),
                    null
            );
        } else if(!isMovingUp && framesSinceBirdStartedMoving != 0) {
            BufferedImage img = rotateImage(birdDownFlap, 3 * rotationAngle - 15);
            g.drawImage(img,
                    getSpriteXPos(), getSpritePosY(), getSpriteWidth(), getSpriteHeight(), null);

        } else {
            g.drawImage(birdMidFlap, getSpriteXPos(), getSpritePosY(), getSpriteWidth(), getSpriteHeight(), null);
        }
    }

    public void drawWithoutRotation(Graphics g) {
        if(isMovingUp && framesSinceBirdStartedMoving != 0) {
            g.drawImage(birdUpFlap, getSpriteXPos(), getSpritePosY(), getSpriteWidth(), getSpriteHeight(), null);
        } else if(!isMovingUp && framesSinceBirdStartedMoving != 0) {
            g.drawImage(birdDownFlap, getSpriteXPos(), getSpritePosY(), getSpriteWidth(), getSpriteHeight(), null);

        } else {
            g.drawImage(birdMidFlap, getSpriteXPos(), getSpritePosY(), getSpriteWidth(), getSpriteHeight(), null);
        }
    }

    public void moveUpBy(int dy) {
        spritePosY -= dy;
        hitboxPosY -= dy;
    }

    public boolean collidesWithPipeFormation(PipeFormation pipeFormation) {
        BottomPipe bottomPipe = pipeFormation.getBottomPipe();
        TopPipe topPipe = pipeFormation.getTopPipe();

        //bottom pipe collision from top
        if((getHitboxPosX() + getHitboxWidth() >= bottomPipe.getX() &&
                getHitboxPosX() <= bottomPipe.getX() + bottomPipe.getWidth()) &&
                getHitboxPosY() + getHitboxHeight() >= bottomPipe.getY()) {
            return true;
        }

        //top pipe collision from bottom
        if((getHitboxPosX() >= topPipe.getX() &&
                getHitboxPosX() <= topPipe.getX() + topPipe.getWidth()) &&
                getHitboxPosY() <= topPipe.getY() + topPipe.getHeight()) {
            return true;
        }

        //collisions for pipes sides
        if((getHitboxPosX() + getHitboxWidth() >= topPipe.getX() && getHitboxPosX() <= topPipe.getX() + getBlockSizePx()) &&
                (getHitboxPosY() <= (topPipe.getY() + topPipe.getHeight()) || getHitboxPosY() >= (bottomPipe.getY()))) {
            return true;
        }

        return false;
    }

    public boolean isBetweenPipes(PipeFormation pipeFormation) {
        return (getHitboxPosX() + getHitboxWidth() >= pipeFormation.getTopPipe().getX() &&
                getHitboxPosX() <= pipeFormation.getTopPipe().getX() + pipeFormation.getTopPipe().getWidth() &&
                getHitboxPosY() >= pipeFormation.getTopPipe().getY() + pipeFormation.getTopPipe().getHeight() &&
                getHitboxPosY() + getHitboxHeight() <= pipeFormation.getBottomPipe().getY());
    }

    public Bird() {
        resetPosition();
    }

    public int getSpriteXPos() {
        return spritePosX;
    }

    public int getSpritePosY() {
        return spritePosY;
    }

    public int getSpriteWidth() {
        return spriteWidth;
    }

    public int getSpriteHeight() {
        return spriteHeight;
    }

    public int getHitboxWidth() {
        return hitboxWidth;
    }

    public int getHitboxHeight() {
        return hitboxHeight;
    }

    public int getHitboxPosX() {
        return hitboxPosX;
    }

    public int getHitboxPosY() {
        return hitboxPosY;
    }
}
