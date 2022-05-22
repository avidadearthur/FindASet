package be.kuleuven.findaset.model;

import java.util.ArrayList;

import be.kuleuven.findaset.activities.LearningActivity;
import be.kuleuven.findaset.activities.MainActivity;

public abstract class AbstractFindASet implements TestableFindASet {
    protected MainActivity mainActivity;
    protected LearningActivity learningActivity;

    public final void setUI(MainActivity mainActivity){
        this.mainActivity = mainActivity;
    }
    public final void setUILearning(LearningActivity learningActivity){
        this.learningActivity = learningActivity;
    }
}
