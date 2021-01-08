import java.util.Arrays;

public class State implements Comparable<State>{
    int[][] matrix = new int[4][4];

    State parent;
    public State() {}
    public State(int[][] matrix) {
        this.matrix = matrix;

    }

public Index getEmpty(){
        Index index=new Index();
        for(int i=0; i < 4;i++){
            for (int j=0;j<4 ;j++){
                if(this.matrix[i][j]==0) {
                    index.i=i;
                    index.j=j;
                };
            }
        }
    return  index;
}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof State)) return false;
        State state = (State) o;
        for (int i = 0; i <4 ; i++) {
            for (int j = 0; j <4 ; j++) {
                if(matrix[i][j] != (state.matrix)[i][j]){
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public int compareTo(State o) {
        int c = 0;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (matrix[i][j] < o.matrix[i][j]) {
                    return -1;
                } else if (matrix[i][j] > o.matrix[i][j]) {
                    return 1;
                }
            }
        }
        return 0;
    }

    @Override
    public String toString() {
        for (int i = 0; i <4 ; i++) {
            for (int j = 0; j <4 ; j++) {
                System.out.print(matrix[i][j]+" ");
            }
            System.out.println();
        }
        return "";
    }
}
