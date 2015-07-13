package com.csg.demo.azure.insights.model;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

/**
 * Created by Dan on 7/11/2015.
 */
@Component
@Scope("session")
public class WinLoss {
    private int wins;

    public void  addWin(){
        wins+=1;
    }

    public  void  addLoss(){
        losses+=1;
    }

    public  void  addTie(){
        ties+=1;
    }

    public int getLosses() {
        return losses;
    }

    public void setLosses(int losses) {
        this.losses = losses;
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public int getTies() {
        return ties;
    }

    public void setTies(int ties) {
        this.ties = ties;
    }

    private int losses;
    private int ties;

    @Override
    public String toString() {
        return "WinLoss{" +
                "wins=" + wins +
                ", losses=" + losses +
                ", ties=" + ties +
                '}';
    }
}
