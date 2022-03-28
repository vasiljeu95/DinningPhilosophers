package com.github.vasiljeu95.hwDinningPhilosophers;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class PhilosWithoutPriorities {
    private final ReentrantLock[] reentrantLocks = new ReentrantLock[]{new ReentrantLock(), new ReentrantLock(), new ReentrantLock(), new ReentrantLock(), new ReentrantLock()};
    private final List<Condition> conditionList = new ArrayList<>(5);

    public void wantsToEat (int philosopher, Runnable pickLeftFork, Runnable pickRightFork, Runnable eat, Runnable putLeftFork, Runnable putRightFork) throws InterruptedException {
        int leftFork, rightFork, firstFork, secondFork;
        byte forkIndex = 0;
        Runnable pickFirst = pickLeftFork, pickSecond = pickRightFork, putFirst = putLeftFork, putSecond = putRightFork;
        leftFork = (philosopher+1)%5;
        rightFork = philosopher;
        firstFork = Math.min(leftFork, rightFork);

        if (firstFork == leftFork) {
            secondFork = rightFork;
        } else {
            pickFirst = pickRightFork;
            pickSecond = pickLeftFork;
            putFirst = putRightFork;
            putSecond = putLeftFork;
            secondFork = leftFork;
            forkIndex = 1;
        }

        reentrantLocks[firstFork].lock();
        pickFirst.run();
        reentrantLocks[secondFork].lock();
        pickSecond.run();
        eat.run();
        putSecond.run();
        reentrantLocks[secondFork].unlock();
        putFirst.run();
        reentrantLocks[firstFork].unlock();
    }

}
