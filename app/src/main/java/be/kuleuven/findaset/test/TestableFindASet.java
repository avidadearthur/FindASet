package be.kuleuven.findaset.test;

import be.kuleuven.findaset.model.card.AbstractCard;

public interface TestableFindASet {
    void setTable(AbstractCard[] table);
    void updateTable(int [] cardIndexes);
    AbstractCard getCard(int i);
    void select(int i);
    void unselect(int i);
}
