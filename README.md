# FindASet

Project done for the class of Programming techniques @ Group T - Faculty of Engineering Technology / academic year 2021-22

### Authors:
* [@avidadearthur](https://github.com/avidadearthur)
* [@shengzhelyu65](https://github.com/shengzhelyu65)

## About the Game

Set (stylized as SET) is a real-time card game designed by Marsha Falco in 1974 and published by Set Enterprises in 1991. 
The deck consists of 81 unique cards that vary in four features across three possibilities for each kind of feature: number of shapes (one, two, or three),
shape (diamond, squiggle, oval), shading (solid, striped, or open), and color (red, green, or purple).[[1]](https://en.wikipedia.org/wiki/Set_(card_game))

In the game, certain combinations of three cards are said to make up a set. For each one of the four categories of features — 
color, number, shape, and shading — the three cards must display that feature as either a) all the same, or b) all different. 
Put another way: the number of features that are all the same and the number of features that are all different may break down as 0 the same + 4 different;
or 1 the same + 3 different; or 2 the same + 2 different; or 3 the same + 1 different.[[1]](https://en.wikipedia.org/wiki/Set_(card_game))

The aim of our project is to create an Android app implementation of Set game.

The current version of this app includes:

* Register and login 
* Ranking with other users scores 
* Different playing modes:
	* Learning
	* Find 10 sets
	* Find all sets    

## Description

Originally created for Android Studio version 3.5.2; updated for version 4.1.2 (March, 2021); updated for Android Studio Bumblebee|2021.1.1 Patch 2 (February 2022) 

InterfaceFindASet lists all methods implemented by AbstractFindASet and their javadocs explaining their basic functionality.
 
The logic of the game is implemented based on the translation of the features of a card into a 4-digit cardId that facilitates the operations needed to carryout the game.

AbstractFindASet is the parent class of the set Find All , Find learning and Find Ten game extensions. It contains the main logic common to all games.

The RV adapters stand for the recycler view adapters that carry out the leaderboard display. This might become disfunctional when the campus deletes our database for the next group students' projects.

### UML

![alt text](https://github.com/avidadearthur/FindASet/blob/master/screenshots/uml.png)


### Database and RESTful service

The MySQL database architecture is very simple and consists of only two tables here's the ERD diagram:

![alt text](https://github.com/avidadearthur/FindASet/blob/master/screenshots/ERD.png)

The app communication to the database is done through a RESTful api service and the queries used can be found [here](https://github.com/avidadearthur/FindASet/blob/master/queries.txt)


## Features:
### Register and login
The register and login functions use SHA-1 (Secure Hash Algorithm 1) just for illustration purposes. Since 2005, SHA-1 has not been considered secure against well-funded opponents. It also doesn't use cryptographic salt.

<img src="https://github.com/avidadearthur/FindASet/blob/master/screenshots/welcome_guest.jpeg" width=24% height=24%>	<img src="https://github.com/avidadearthur/FindASet/blob/master/screenshots/login.jpeg" width=24% height=24%>	<img src="https://github.com/avidadearthur/FindASet/blob/master/screenshots/Welcome_logged.jpeg" width=24% height=24%>		<img src="https://github.com/avidadearthur/FindASet/blob/master/screenshots/register.jpeg" width=24% height=24%>

### Offline version
When the app is first downloaded it creates a text file that will store the device's best scores so that the player can keep track of their performance offline. Here's the format of the JSON array stored:
```
{
	"device": [{
		"thisDevice": " ",
		"FindTenScore": [" ", " ", " "],
		"FindAllScore": [" ", " ", " "]
	}],
	"session": [{
		"username": " "
	}]
}
```

### Three playing modes:
#### Find Learning
When you click on two cards and watch the features at the bottom light up: Yellow means that this feature should differ in the 3rd card; Green means that the 3rd card should have this feature in common; if you press hint the a set shows up in blue on the screen.
The cards icon in the top-center of the screen indicates how many cards that form a set have been found.

<img src="https://github.com/avidadearthur/FindASet/blob/master/screenshots/learning_mode.jpeg" width=24% height=24%>	<img src="https://github.com/avidadearthur/FindASet/blob/master/screenshots/learning_mode_set.jpeg" width=24% height=24%>	<img src="https://github.com/avidadearthur/FindASet/blob/master/screenshots/normal_mode.jpeg" width=24% height=24%>	

#### Find all sets
The game has 81 unique cards and as you find sets of three cards, you eliminate other groups of possible sets. Throughout the game development we observed that other mobile versions of this game flag a win after the player has selected 23, 24, 25. Hence in the Find all mode, after the player has found 69, 72 or 75 cards that formed a set the check win method looks for a set in the current table of cards. If no sets are found the player wins the game.

The third screen above shows the Find all activity screen. Notice that there's no user logged in (indicated by 'guest') and that the stopwatch is running on the top-right corner. The player can click on hint to reveal one set or on restart to reset the game.

Here's the overriden method:

```
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
        // the player has already found 24 sets or more
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
```
The find 10 mode works the same way as the Find All but flags the win after the player has found 30 cards that form a set (10 sets).

### Online Ranking
Add info about the how the online ranking works

<img src="https://github.com/avidadearthur/FindASet/blob/master/screenshots/leader_board.jpeg" width=24% height=24%>	<img src="https://github.com/avidadearthur/FindASet/blob/master/screenshots/high_score_logged.jpeg" width=24% height=24%>

### Installing and Executing program

Go to [release](https://github.com/avidadearthur/FindASet/tree/master/app/release) with your android device and download the apk!

## Version History

* 0.0
    * Various bug fixes and optimizations
    * README.md


## License

The SET! game is copyright protected. Any textual or graphic material you copy, print, or download is licensed to you by Set Enterprises, Inc. ("Set Enterprises") for your personal, non-commercial home use only, provided that you do not change or delete any copyright, trademark or other proprietary notices.

See the LICENSE.md file for details

## Acknowledgments

Inspiration, code snippets, etc.
* [awesome-readme](https://github.com/matiassingers/awesome-readme)
* [UI inspiration](https://www.behance.net/gallery/52633383/Learnr-Online-Courses-Educational-App-UI-Kit)
