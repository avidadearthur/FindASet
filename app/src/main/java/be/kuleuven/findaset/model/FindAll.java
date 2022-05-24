package be.kuleuven.findaset.model;

public class FindAll extends AbstractFindASet implements InterfaceFindASet{
    public FindAll() {
    }

    @Override
    public void checkWin() {
        // check if there's still a set after updating
        if (foundedSetCardsIds.size() < 69) {
            updateWholeTable(checkAllSetOnPage());
        }
        else if (foundedSetCardsIds.size() == 69) {
            if (!checkAllSetOnPage()) {
                win = true;
            }
        }
        // the player ha already found 24 sets or more
        // so they win the find All game
        else if (foundedSetCardsIds.size() == 72) {
            // No need to display to display 12 cards
            for (int j = 9; j < 12; j++) {
                mainActivity.notifyUnavailable(j);
            }
            // display last 9 cards
            initializeTable(9);
            setCardsTable(9);
            mainActivity.notifyNewGame(9);
            // If there's no set remaining in the 9 cards the player wins the game
            if (!checkAllSetOnPage()) {
                win = true;
            }
        }
        else if (foundedSetCardsIds.size() == 75) {
            // No need to display to display 12 cards, just 6
            for (int k = 6; k < 9; k++) {
                mainActivity.notifyUnavailable(k);
            }
            initializeTable(6);
            setCardsTable(6);
            mainActivity.notifyNewGame(6);
            // If there's no set remaining in the 6 cards the player wins the game
            if (!checkAllSetOnPage()) {
                win = true;
            }
        }
    }
}
