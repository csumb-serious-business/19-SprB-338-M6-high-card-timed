public class Main {

    public static void main(String[] args) {
        Game game = new Game();
        GameView view = new GameView();
        GameController controller = new GameController(game, view);
        controller.startGame();
    }

}