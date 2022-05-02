package be.kuleuven.findaset.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.IntStream;

import be.kuleuven.findaset.R;
import be.kuleuven.findaset.model.FindASet;
import be.kuleuven.findaset.model.TestableFindASet;
import be.kuleuven.findaset.model.card.AbstractCard;
import be.kuleuven.findaset.model.card.Card;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private TestableFindASet gameModel;
    private ImageView[] cardImages;
    private TextView[] cardTexts;
    private Button refreshBtn;
    private TextView testTxt;
    private Integer[] cardPicturesIds = {
            Integer.valueOf(R.drawable.ovaal1groen), Integer.valueOf(R.drawable.ovaal2groen), Integer.valueOf(R.drawable.ovaal3groen),
            Integer.valueOf(R.drawable.ovaal1rood), Integer.valueOf(R.drawable.ovaal2rood), Integer.valueOf(R.drawable.ovaal3rood),
            Integer.valueOf(R.drawable.ovaal1paars), Integer.valueOf(R.drawable.ovaal2paars), Integer.valueOf(R.drawable.ovaal3paars),
            Integer.valueOf(R.drawable.ruit1groen), Integer.valueOf(R.drawable.ruit2groen), Integer.valueOf(R.drawable.ruit3groen),
            Integer.valueOf(R.drawable.ruit1rood), Integer.valueOf(R.drawable.ruit2rood), Integer.valueOf(R.drawable.ruit3rood),
            Integer.valueOf(R.drawable.ruit1paars), Integer.valueOf(R.drawable.ruit2paars), Integer.valueOf(R.drawable.ruit3paars),
            Integer.valueOf(R.drawable.tilde1groen), Integer.valueOf(R.drawable.tilde2groen), Integer.valueOf(R.drawable.tilde3groen),
            Integer.valueOf(R.drawable.tilde1rood), Integer.valueOf(R.drawable.tilde2rood), Integer.valueOf(R.drawable.tilde3rood),
            Integer.valueOf(R.drawable.tilde1paars), Integer.valueOf(R.drawable.tilde2paars), Integer.valueOf(R.drawable.tilde3paars)};

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

        for(int i = 0; i < 12; i++) {
            cardImages[i].setOnClickListener(this);
        }

        TestableFindASet findASet = new FindASet();
        setGameModel(findASet);
        gameModel.setTable(null);
    }

    @Override
    public void onClick(View view) {
        int index =  Arrays.asList(cardImages).indexOf(view);
        gameModel.select(index);
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
            cardImages[i].setImageBitmap(combineImageIntoOne(setBitmaps(nextCard)));
            cardTexts[i].setText(nextCard.toString());
        }

        //JUST for TEST
        String str = "SET cards position: "
                + (gameModel.getJustForTest()[0]+1) + " "
                + (gameModel.getJustForTest()[1]+1) + " "
                + (gameModel.getJustForTest()[2]+1) + " ";
        testTxt.setText(str);
    }

    private ArrayList<Bitmap> setBitmaps(AbstractCard card) {
        int index = 0;
        int color = card.getColorInt() - 1;
        int shading = card.getShadingInt() - 1;
        int shape = card.getTypeInt() - 1;
        index = shape * 9 + color * 3 + shading;
        Bitmap test = BitmapFactory.decodeResource(getResources(), cardPicturesIds[index]);
        ArrayList<Bitmap> newBitMap = new ArrayList<>();
        for (int i = 0; i < card.getShapeCountInt(); i++) {
            newBitMap.add(test);
        }
        return newBitMap;
    }

    private Bitmap combineImageIntoOne(ArrayList<Bitmap> bitmap) {
        int width = bitmap.get(0).getWidth();
        int height = bitmap.get(0).getHeight();
        int size = bitmap.size();

        Bitmap temp = Bitmap.createBitmap((int) (4.5 * width), height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(temp);
        float left = 0;
        if (size == 3) {
            for (int i = 0; i < size; i++) {
                left = (i == 0 ? 0.25f * width : left + 1.5f * width);
                canvas.drawBitmap(bitmap.get(i), left, 0f, null);
            }
        } else if (size == 2) {
            for (int i = 0; i < size; i++) {
                left = (i == 0 ? 0.5f * width : left + 2.5f * width);
                canvas.drawBitmap(bitmap.get(i), left, 0f, null);
            }
        } else {
            left = 1.75f * width;
            canvas.drawBitmap(bitmap.get(0), left, 0f, null);
        }

        return temp;
    }

    public void notifyToggle(int index) {
        if(gameModel.getCard(index).isSelected())
            cardImages[index].setBackground(getDrawable(R.drawable.selected_card));
        else
            cardImages[index].setBackground(null);
    }
}