public class Main
{

   public static void main(String[] args)
   {     
	   TimedViewer CardView = new TimedViewer(); //Intialize the View
	   TimedModel  CardModel = new TimedModel(); //Initialize the mode  
       TimedController myController = new TimedController(CardModel,CardView); 
       myController.doMainGame();

   }

}