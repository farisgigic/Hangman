# Hangman Game 🎮

Welcome to the **Hangman Game**—a text-based word-guessing game where players challenge themselves or others to uncover hidden words! This game is built to provide an interactive and fun console-based experience, complete with multiplayer options, progress saving, and a scoreboard.

## 🎯 Features

### Game Modes
- **Single Player Mode**: Guess words randomly generated from a predefined list.
- **Multiplayer Mode**: Player 1 selects a word for Player 2 to guess.

### Interactive Gameplay
- Guess letters to reveal the hidden word.
- Limited attempts (5–6 incorrect guesses) before the game is lost.
- ASCII art-based hangman visuals for each incorrect guess.
- Dynamic display of guessed letters and remaining hidden ones.

### User Options
- Start a new game.
- Continue a previously saved game.
- View the scoreboard with player statistics.

### Player Customization
- Choose meaningful player names for better immersion.
- Save progress at any point to continue later.

### Word Selection
- Predefined word list (words with more than five letters) for single-player mode.
- Player-provided words in multiplayer mode.

### Scoreboard
- Tracks total games played and wins for all players.
- Displays comprehensive player statistics.

### Save and Load
- Progress can be saved in a JSON file, storing:
  - Current word
  - Guessed letters
  - Remaining attempts
  - Player scores
  - Player names
- Reload the last saved state to pick up where you left off.

### Visual Feedback
- Enhanced gameplay experience with colors and dynamic graphics.
- Eye-catching UI with ASCII art hangman visuals.

## 🛠️ Technologies Used
- **Programming Language**: Assigned language (Rust, Go, Ruby, Scala, Perl, etc.).
- Libraries and packages specific to the assigned language for:
  - Random word generation.
  - File handling (JSON).
  - Console color and graphics.

## 🚀 How to Play

1. **Clone the Repository**:
   ```bash
   git clone https://github.com/farisgigic/Hangman.git
   cd hangman-game
