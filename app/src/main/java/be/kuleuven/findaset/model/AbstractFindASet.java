package be.kuleuven.findaset.model;

import be.kuleuven.findaset.activities.MainActivity;

public abstract class AbstractFindASet implements TestableFindASet {
    protected MainActivity mainActivity;

    public final void setUI(MainActivity mainActivity){
        this.mainActivity = mainActivity;
    }
}
