
    //import com.sun.source.tree.ParenthesizedTree;

    import java.io.*;
    import java.lang.reflect.Array;
    import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
    import java.util.*;
    import java.util.stream.Stream;

    public class Main {
        static int count =0;
        static State failedState;
        public enum Strategy{BFS}
        public static void main(String[] args) {
            Path path = Paths.get(args[0]);
            int[][] vals = new int[4][4];

            int[][] goalMatrix = {
                    {1, 2, 3, 4},
                    {5, 6, 7, 8},
                    {9, 10, 11, 12},
                    {13, 14, 15, 0}
            };
            try (Stream<String> lines = Files.lines(path)) {
                ArrayList<String> al = new ArrayList<String>();

                lines.forEach(s -> {
                    al.add(s);
                });

                int i = 0;
                for (String s : al) {
                    s = s.replaceAll("\\s", ",");
                    String[] numbers = s.split(",");
                    int j = 0;
                    for (String str : numbers) {
                        int num = Integer.parseInt(str);
                        vals[i][j] = num;
                        j++;
                    }
                    i++;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println(System.getProperty("user.dir"));

            PrintWriter writer = null;
            try {
                FileWriter fileWriter;
                if(args[0].contains("initial-state-1.txt")){
                    fileWriter = new FileWriter(System.getProperty("user.dir")+"\\initial-state1\\bfs\\results.txt");
                }else{
                    fileWriter = new FileWriter(System.getProperty("user.dir")+"\\initial-state2\\bfs\\results.txt");
                }
               // FileWriter fileWriter = new FileWriter(System.getProperty("user.dir")+"");
                writer = new PrintWriter(fileWriter);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }catch (IOException e){
                e.printStackTrace();
            }
            State goalState = new State(goalMatrix);
            State initialState = new State(vals);
            Strategy strategy= Strategy.BFS;
            HashMap<State, State> previous = searchAlgorithm(new Problem(initialState, goalState), strategy);
            ArrayList<State> sol = new ArrayList<State>();
            if (previous.containsKey(goalState)) {
                State s = previous.get(goalState);
                sol.add(goalState);
                do {
                    sol.add(s);
                    s = previous.get(s);
                } while (s != null);
            }else{
                State s = previous.get(failedState);
                do{
                    sol.add(s);
                    s=previous.get(s);
                } while( s!=null);

            }
            Collections.reverse(sol);
            for(State s : sol){
                State parent = previous.get(s);
                if(parent!=null) writer.println("MOVE<"+s.matrix[parent.getEmpty().i][parent.getEmpty().j]+"> TO ZERO");
            }
            writer.println(count);
            writer.close();



        }
        private static void removeTheFirstNameDuplicates(final Map<State, State> map) {
            final Iterator<Map.Entry<State, State>> iter = map.entrySet().iterator();
            final HashSet<State> valueSet = new HashSet<State>();
            while (iter.hasNext()) {
                final Map.Entry<State, State> next = iter.next();
                if (!valueSet.add(next.getValue())) {
                    iter.remove();
                }
            }
        }

        public static HashMap<State,State> searchAlgorithm(Problem problem, Strategy strategy){
            count = 0;

            State initialState = new State(problem.initialState.matrix);
            PriorityQueue<State> Q = new PriorityQueue<State>();
            Q.add(initialState);
            ArrayList<State> expanded = new ArrayList<State>();
            HashMap<State,State> previous = new HashMap<State,State>(); // <State, State's parent>
            if(strategy.equals(Strategy.BFS)){
                for (;;){
                    if (Q.isEmpty()){
                        return previous;
                    }
                    State poppedState =Q.poll();
                    expanded.add(poppedState);
                    count++;
                    if(poppedState.equals(problem.goalState)){
                        System.out.println(count);
                        return previous;
                    }
                    for(State childState : successor(poppedState)){
                        if(!expanded.contains(childState)){
                            Q.add(childState);
                            previous.put(childState,poppedState);
                        }
                    }
                    failedState = poppedState;
                }
            }

          return null;
        }


        public static Set<State> successor(State state){
            Set<State> states = new TreeSet<State>(new Comparator<State>() {
                @Override
                public int compare(State o1, State o2) {
                    int c = 0;
                    for (int i = 0; i < 4; i++) {
                        for (int j = 0; j < 4; j++) {
                            if (o1.matrix[i][j] < o2.matrix[i][j]) {
                                return -1;
                            } else if (o1.matrix[i][j] > o2.matrix[i][j]) {
                                return 1;
                            }
                        }
                    }
                    return 0;
                }
                  });
            int row = state.getEmpty().i;
            int col = state.getEmpty().j;
            ArrayList<int[][]> matrices = new ArrayList<>();
            ArrayList<Index> indeces = new ArrayList<Index>();
            if(row == 0 || row == 1 ){
                if(row == 1 ){
                    if(col<2){
                        int i=row-1;
                        int j=col+2;
                        indeces.add(new Index(i,j));
                    }
                    if(col>1){
                        int i=row-1;
                        int j=col-2;
                        indeces.add(new Index(i,j));
                    }
                }
                if(col<2){
                    int i=row+1; // row+2
                    int j=col+2;
                    indeces.add(new Index(i,j));
                }
                if(col<3) {
                    int i=row+2;
                    int j=col+1;
                    indeces.add(new Index(i,j));
                }
                if(col>1){
                    int i=row+1;
                    int j=col-2;
                    indeces.add(new Index(i,j));
                }
                if(col>0){
                    int i=row+2;
                    int j=col-1;
                    indeces.add(new Index(i,j));
                }
            }else if(row == 2){
                if(col<2){
                    int i=row+1;
                    int j=col+2;
                    indeces.add(new Index(i,j));
                }
                if(col>1){
                    int i=row+1;
                    int j=col-2;
                    indeces.add(new Index(i,j));
                }if(col<2){
                    int i=row-1;
                    int j=col+2;
                    indeces.add(new Index(i,j));
                }
                if(col>1){
                    int i=row-1;
                    int j=col-2;
                    indeces.add(new Index(i,j));
                }
                if(col>0){
                    int i=row-2;
                    int j=col-1;
                    indeces.add(new Index(i,j));
                }
                if(col<3){
                    int i=row-2;
                    int j=col+1;
                    indeces.add(new Index(i,j));
                }
            }else {
                if(col<2){
                    int i=row-1;
                    int j=col+2;
                    indeces.add(new Index(i,j));
                }
                if(col>1){
                    int  i=row-1;
                    int  j=col-2;
                    indeces.add(new Index(i,j));
                }
                if(col>0){
                    int  i=row-2;
                    int  j=col-1;
                    indeces.add(new Index(i,j));
                }
                if(col<3){
                    int  i=row-2;
                    int  j=col+1;
                    indeces.add(new Index(i,j));
                }
            }

            for(Index i : indeces){
                int[][] temp = new int[4][4];
                for (int j = 0; j <4 ; j++) {
                    for (int k = 0; k < 4; k++) {
                        temp[j][k]=state.matrix[j][k];
                    }
                }
                for (int j = 0; j <4 ; j++) {
                    for (int k = 0; k < 4; k++) {
                      if(state.matrix[j][k]==0){
                          temp[j][k] = state.matrix[i.i][i.j];
                          temp[i.i][i.j] =0;
                          matrices.add(temp);
                      }
                    }
                }
            }

            for(int[][] s : matrices){
                states.add(new State(s));
            }

            return states;
        }

    }
