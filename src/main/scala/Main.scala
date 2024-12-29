import scala.io.StdIn.readLine
import scala.util.Random
import java.io.{File, PrintWriter}
import scala.annotation.tailrec
import scala.io.Source

object Hangman {

  private var wordToGuess = ""
  private var attemptsLeft = 6
  private var guessedLetters = Set[Char]()
  private var currentPlayer = ""
  private var playerScores = Map[String, Int]().withDefaultValue(0)
  private var isMultiplayer = false
  private val wordsList = List("programming", "semestar", "scala", "randomize", "bucapotok", "functional", "language")

  def main(args: Array[String]): Unit = {
    println(Console.BLUE + "Welcome to Hangman Game!" + Console.RESET)
    gameMenu()
  }

  @tailrec
  private def gameMenu(): Unit = {
    //println("1. Start New Game")
    //println("2. Continue Saved Game")
    //println("3. View Scoreboard")
    //println("4. Exit")
    //println("Choose an option:")

    println(Console.GREEN + "1. Start New Game" + Console.RESET)
    println(Console.YELLOW + "2. Continue Saved Game" + Console.RESET)
    println(Console.CYAN + "3. View Scoreboard" + Console.RESET)
    println(Console.RED + "4. Exit" + Console.RESET)
    println("Choose an option:")
    // CONSOLE.RESET used for resetting color, it starts from green but it needs to end with this color and start another

    readLine() match {
      case "1" => startNewGame()
      case "2" => continueGame()
      case "3" => showScoreboard()
      case "4" => exitGame()
      case _   => println("Invalid option. Try again."); gameMenu()
    }
  }

  private def exitGame(): Unit = {
    saveScoreboard()
    println(Console.RED + "Thank you for playing our game! Bye!" + Console.RESET)
    System.exit(0)
  }

  private def startNewGame(): Unit = {
    println("Enter your name: ")
    currentPlayer = readLine()

    println("Choose a mode: ")
    println("1. Single Player")
    println("2. Multiplayer")
    isMultiplayer = readLine() match {
      case "1" => false
      case "2" => true
      case _   => println("Invalid choice. Defaulting to Single Player."); false
    }

    if (isMultiplayer) {
      println("Player 1, enter a word for Player 2 to guess: ")
      wordToGuess = readLine().toLowerCase()
      println("\n" * 50) // Clear the screen
      println("The word is set! Player 2, your turn!")
    } else {
      wordToGuess = wordsList(Random.nextInt(wordsList.length))
    }

    attemptsLeft = 6
    guessedLetters = Set()
    playGame()
  }

  private def playGame(): Unit = {
    while (attemptsLeft > 0 && !wordIsGuessed) {
      printGameStatus()
      println("Guess a letter or type 'stop' to save and exit: ")
      val input = readLine().toLowerCase()

      if (input == "stop") {
        saveGame()
        println(Console.GREEN + "Game progress saved. You can continue later!" + Console.RESET)
        gameMenu()
        return
      }

      if (input.length != 1 || !input.head.isLetter) {
        println("Invalid input. Please guess a single letter.")
      } else {
        val guess = input.head
        if (guessedLetters.contains(guess)) {
          println(s"You've already guessed '$guess'. Try again!")
        } else if (wordToGuess.contains(guess)) {
          guessedLetters += guess
          println(s"Good guess! '$guess' is in the word.")
        } else {
          attemptsLeft -= 1
          guessedLetters += guess
          println(s"Wrong guess! '$guess' is not in the word. Attempts left: $attemptsLeft")
        }
      }
    }

    if (wordIsGuessed) {
      println(Console.GREEN + s"Congratulations, $currentPlayer! You guessed the word: $wordToGuess" + Console.RESET)
      playerScores += currentPlayer -> (playerScores(currentPlayer) + 1)
    } else {
      println(Console.RED + s"Game Over! The word was: $wordToGuess" + Console.RESET)
    }

    saveScoreboard()
    gameMenu()
  }
  private def wordIsGuessed: Boolean = wordToGuess.forall(guessedLetters.contains)

  private def printGameStatus(): Unit = {
    val displayedWord = wordToGuess.map(ch => if (guessedLetters.contains(ch)) ch else '_').mkString(" ")
    println(s"\nWord: $displayedWord")
    println(s"Guessed Letters: ${guessedLetters.mkString(", ")}")
    printHangman()
  }

  private def printHangman(): Unit = {
    val stages = Array(
      """
        |
        |
        |
        |
        |_________
      """,
      """
        |
        |    |
        |
        |
        |_________
      """,
      """
        |    ----
        |    |
        |
        |
        |_________
      """,
      """
        |    ----
        |    |
        |    O
        |
        |_________
      """,
      """
        |    ----
        |    |
        |    O
        |   /|\
        |_________
      """,
      """
        |    ----
        |    |
        |    O
        |   /|\
        |   / \
        |_________
      """
    )
    println(stages(6 - attemptsLeft))
  }

  private def saveGame(): Unit = {
    val saveData = s"$currentPlayer\n$wordToGuess\n$attemptsLeft\n${guessedLetters.mkString("")}\n$isMultiplayer"
    val writer = new PrintWriter(new File("savedGame.json"))
    writer.write(saveData)
    writer.close()
    println("Game saved!")
  }

  private def continueGame(): Unit = {
    try {
      val lines = Source.fromFile("savedGame.json").getLines().toList
      currentPlayer = lines(0)
      wordToGuess = lines(1)
      attemptsLeft = lines(2).toInt
      guessedLetters = lines(3).toSet
      isMultiplayer = lines(4).toBoolean
      println(Console.GREEN + s"Welcome back, $currentPlayer!" + Console.RESET)
      playGame()
    } catch {
      case _: Exception => println("No saved game found."); gameMenu()
    }
  }

  private def showScoreboard(): Unit = {
    println(Console.CYAN + "\nScoreboard:" + Console.RESET)
    playerScores.toList.sortBy(-_._2).foreach {
      case (player, score) => println(s"$player: $score")
    }
    println()
    gameMenu()
  }

  private def saveScoreboard(): Unit = {
    val writer = new PrintWriter(new File("scoreboard.json"))
    playerScores.foreach { case (player, score) =>
      writer.write(s"$player,$score\n")
    }
    writer.close()
  }

  private def loadScoreboard(): Unit = {
    try {
      val lines = Source.fromFile("scoreboard.json").getLines()
      playerScores = lines.map { line =>
        val Array(player, score) = line.split(",")
        player -> score.toInt
      }.toMap.withDefaultValue(0)
    } catch {
      case _: Exception => println("No previous scores found.")
    }
  }
}
