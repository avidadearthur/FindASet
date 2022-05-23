package be.kuleuven.findaset.model;

public class FindTen extends AbstractFindASet implements InterfaceFindASet {

    public FindTen() {
    }

    @Override
    public void checkWin() {
        // check if there's still a set after updating
        if (foundedSetCardsIds.size()  < 30) {
            updateWholeTable(checkAllSetOnPage());
        }
        if (foundedSetCardsIds.size()  == 30) {
            win = true;
        }
    }
}
