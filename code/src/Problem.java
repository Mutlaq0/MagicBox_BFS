public class Problem {

    State initialState;
    State goalState;
    Main.Strategy strategy;
    public Problem(State initialState , State goalState){
        this.initialState=initialState;
        this.goalState=goalState;
    }
}
