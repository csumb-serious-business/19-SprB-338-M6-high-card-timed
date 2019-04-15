public class Main {

    public static void main(String[] args) {
        TimedViewer CardView = new TimedViewer(); //Intialize the View
        TimedModel CardModel = new TimedModel(); //Initialize the Model
        TimedController myController = new TimedController(CardModel, CardView); //Conenct it to the Controller
        myController.doMainGame(); //Start the Game

    }

}