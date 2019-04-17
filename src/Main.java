public class Main {

    public static void main(String[] args) {
        Game game = new Game();
        GameView view = new GameView("Timed Card Game",
                Game.DEFAULT_NUM_CARDS_PER_HAND,
                Game.DEFAULT_NUM_PLAYERS);

        GameController controller = new GameController(game, view);
        controller.startGame();
    }

}