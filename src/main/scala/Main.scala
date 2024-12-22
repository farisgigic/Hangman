import scala.io.StdIn.readLine
import scala.util.Random
import java.io.{File, PrintWriter}
import scala.annotation.tailrec
import scala.io.Source

object Main {

  // Game Variables
  private var wordToGuess = ""
  private var attemptsLeft = 7
  private var guessedLetters = Set[Char]()
  private var currentPlayer = ""
  private var playerScores = Map[String, Int]().withDefaultValue(0)
  private val wordsList = List("programming", "scala", "functional", "project", "skafiskafnjak")

  def main(args: Array[String]): Unit = {
    println("Welcome to the Hangman Game!")
    gameMenu()
  }


  // head and tail recursion (tailrec and headrec)
  // @headrec -> less common because it is harder to optimize, it involves doing some operations before making the recursive call
  // @tailrec -> last operation in function
  // Menu System
  @tailrec
  private def gameMenu(): Unit = {
    println("1. Start a New Game")
    println("2. Continue Saved Game")
    println("3. View Scoreboard")
    println("4. Close")
    println("Choose an option: ")
    val choice = readLine()

    choice match {
      case "1" => startNewGame()
      case "2" => continueGame()
      case "3" => showScoreboard()
      case "4" => close()
      case "\n" => continueGame()
      case _ => println("Invalid choice! Please choose again."); gameMenu()
    }
  }

  private def close(): Unit = {
    println("Program is closing !")
    System.exit(0)
  }
  // Starting a New Game
  @tailrec
  private def startNewGame(): Unit = {
    println("Enter your name: ")
    currentPlayer = readLine()

    println("Choose game mode: 1 for Single Player, 2 for Multiplayer")
    val mode = readLine()

    mode match {
      case "1" => singlePlayerGame()
      case "2" => multiplayerGame()
      case _ => println("Invalid choice!"); startNewGame()
    }
  }

  // Single Player Mode (Random word)
  private def singlePlayerGame(): Unit = {
    wordToGuess = wordsList(Random.nextInt(wordsList.length))
    playGame()
  }

  // Multiplayer Mode (Player 1 chooses the word)
  private def multiplayerGame(): Unit = {
    println("Player 1, please enter the word for Player 2 to guess (No cheating!): ")
    wordToGuess = readLine().toLowerCase
    playGame()
  }

  // Core Game Loop
  private def playGame(): Unit = {
    attemptsLeft = 7
    guessedLetters = Set()

    while (attemptsLeft > 0 && !wordIsGuessed) {
      printGameStatus()
      println("Guess a letter: ")
      val guessedLetter = readLine().toLowerCase.charAt(0)

      if (wordToGuess.contains(guessedLetter)) {
        guessedLetters += guessedLetter
        println(s"Good guess! '$guessedLetter' is in the word.")
      } else {
        attemptsLeft -= 1
        println(s"Wrong guess! You have $attemptsLeft attempts left.")
      }

      if (wordIsGuessed) {
        println(s"Congratulations, $currentPlayer! You've guessed the word: $wordToGuess")
        updateScore(currentPlayer)
        saveGameProgress()
      } else if (attemptsLeft == 0) {
        println(s"Game Over! The word was: $wordToGuess")
      }
    }
    gameMenu() // Go back to the menu after the game finishes
  }

  // Check if the word is completely guessed
  private def wordIsGuessed: Boolean = {
    wordToGuess.forall(guessedLetters.contains)
  }

  // Display the current game status
  private def printGameStatus(): Unit = {
    val displayedWord = wordToGuess.map(letter => if (guessedLetters.contains(letter)) letter else '_')
    println(s"Current Word: $displayedWord")
    println(s"Guessed Letters: ${guessedLetters.mkString(", ")}")
    drawHangman()
  }

  // Draw the hangman based on incorrect guesses
  private def drawHangman(): Unit = {
    val stages = List(
      """
        |
        |
        |
        |
        |_____
      """,
      """
        |____
        |   |
        |   
        |
        |
        |_____
      """,
      """
        |____
        |   |
        |   O
        |
        |
        |_____
      """,
      """
        |____
        |   |
        |   O
        |   |
        |
        |_____
      """,
      """
        |____
        |   |
        |   O
        |  /|
        |
        |_____
      """,
      """
        |____
        |   |
        |   O
        |  /|\
        |
        |_____
      """,
      """
        |____
        |   |
        |   O
        |  /|\
        |  /
        |_____
      """,
      """
        |____
        |   |
        |   O
        |  /|\
        |  / \
        |_____
      """
    )
    println(stages(7 - attemptsLeft))
  }

  // Saving game progress
  private def saveGameProgress(): Unit = {
    val pw = new PrintWriter(new File("savedGame1.json"))
    pw.write(s"""{
                |"currentPlayer": "$currentPlayer",
                |"wordToGuess": "$wordToGuess",
                |"attemptsLeft": $attemptsLeft,
                |"guessedLetters": "${guessedLetters.mkString}"
                |}""".stripMargin)
    pw.close()
    println("Game saved!")
  }

  // Continuing saved game
  private def continueGame(): Unit = {
    val savedGame = Source.fromFile("savedGame1.json").getLines.mkString
    val data = ujson.read(savedGame)
    currentPlayer = data("currentPlayer").str
    wordToGuess = data("wordToGuess").str
    attemptsLeft = data("attemptsLeft").num.toInt
    guessedLetters = data("guessedLetters").str.toSet
    println(s"Continuing game for $currentPlayer...")
    //savedGame.close();

    playGame()
  }

  // Show the scoreboard
  private def showScoreboard(): Unit = {
    println("Scoreboard:")
    playerScores.toList.sortBy(-_._2).foreach {
      case (player, score) => println(s"$player: $score")
    }
    gameMenu()
  }

  // Update player score
  private def updateScore(player: String): Unit = {
    playerScores += player -> (playerScores(player) + 1)
    println(s"$player's score: ${playerScores(player)}")
  }
}
