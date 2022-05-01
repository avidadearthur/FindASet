package be.kuleuven.findaset.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import be.kuleuven.findaset.R;
import be.kuleuven.findaset.model.FindASet;
import be.kuleuven.findaset.model.TestableFindASet;
import be.kuleuven.findaset.model.card.AbstractCard;

public class MainActivity extends AppCompatActivity {

    private TestableFindASet gameModel;
    private ImageView[] cardImages;
    private TextView[] cardTexts;
    private Button refreshBtn;
    private TextView testTxt;

    /**
     * Firstly bound all fields with UI components.
     *
     * Then bound gameModel with new FindASet()
     * to use the method in FindASet.
     *
     * @param savedInstanceState saved content
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        refreshBtn = findViewById(R.id.refreshBtn);
        testTxt = findViewById(R.id.testTxt);

        cardImages = new ImageView[12];
        cardImages[0] = findViewById(R.id.card1);
        cardImages[1] = findViewById(R.id.card2);
        cardImages[2] = findViewById(R.id.card3);
        cardImages[3] = findViewById(R.id.card4);
        cardImages[4] = findViewById(R.id.card5);
        cardImages[5] = findViewById(R.id.card6);
        cardImages[6] = findViewById(R.id.card7);
        cardImages[7] = findViewById(R.id.card8);
        cardImages[8] = findViewById(R.id.card9);
        cardImages[9] = findViewById(R.id.card10);
        cardImages[10] = findViewById(R.id.card11);
        cardImages[11] = findViewById(R.id.card12);

        cardTexts = new TextView[12];
        cardTexts[0] = findViewById(R.id.card1Text);
        cardTexts[1] = findViewById(R.id.card2Text);
        cardTexts[2] = findViewById(R.id.card3Text);
        cardTexts[3] = findViewById(R.id.card4Text);
        cardTexts[4] = findViewById(R.id.card5Text);
        cardTexts[5] = findViewById(R.id.card6Text);
        cardTexts[6] = findViewById(R.id.card7Text);
        cardTexts[7] = findViewById(R.id.card8Text);
        cardTexts[8] = findViewById(R.id.card9Text);
        cardTexts[9] = findViewById(R.id.card10Text);
        cardTexts[10] = findViewById(R.id.card11Text);
        cardTexts[11] = findViewById(R.id.card12Text);

        TestableFindASet findASet = new FindASet();
        setGameModel(findASet);

        gameModel.setTable(null);
    }

    public void refreshBtn_Clicked(View caller){
        gameModel.emptyTable();
        gameModel.setTable(null);
    }

    public void setGameModel(TestableFindASet newGameModel) {
        this.gameModel = newGameModel;
        this.gameModel.setUI(this);
    }

    public void notifyNewGame() {
        for(int i = 0; i < cardImages.length; i++){
            AbstractCard nextCard = gameModel.getCard(i);
            cardTexts[i].setText(nextCard.toString());
        }

        //JUST for TEST
        String str = "SET cards position: "
                + (gameModel.getJustForTest()[0]+1) + " "
                + (gameModel.getJustForTest()[1]+1) + " "
                + (gameModel.getJustForTest()[2]+1) + " ";
        testTxt.setText(str);
    }
}