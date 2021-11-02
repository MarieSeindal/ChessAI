public class AI {

    private static int maxDepth = 15;

    public AI(Board currentBoard){

    }


    public static int minimax (ChessNode nodeTosearch, int depth, boolean isMax, int alpha, int beta){

        System.out.println("Depth in minimax "+depth); //todo delete after use

        // If leaf node, return static value of the board
        if (depth == maxDepth){
            System.out.println("Leaf return in depth " + depth);
            return (0); //todo.  evaluateLeaf()
        }

        // node in depth 0,2 ... straight numbers is max, as the algorithm runs the AI's turn.
        else if (isMax){
            int bestValue = alpha;
            int value;
            System.out.println("Max alpha " + alpha); //todo debug print
            System.out.println("Max beta " + beta); //todo debug print

            //todo Fill children to list //Ad children to the parents arraylist

            for (ChessNode child : nodeTosearch.getChildren()) {
                value = minimax(child,depth+1,false,alpha,beta);
                bestValue = max(bestValue , value);
                alpha = max(alpha , bestValue);

                if(beta <= alpha)
                    break;
            }
            return bestValue;
        }
        else if (!isMax){ // node in depth 1,3 ... unequal numbers is min, as the algorithm runs the opponents turn.
            int bestValue = beta;
            int value;
            System.out.println("Min alpha " + alpha);
            System.out.println("Min beta " + beta);

            //todo Fill children to list //Ad children to the parents arraylist

            for (ChessNode child : nodeTosearch.getChildren()) {
                value =minimax(child, depth+1, true, alpha, beta);
                bestValue = min(bestValue , value);
                beta = min(beta , bestValue);

                if (beta <= alpha)
                    break;

            }
            return bestValue;
        }

        return 0;
    }


    public static int max(int a, int b){
        if (a>b) return a;
        else return b;
    }

    public static int min(int a, int b){
        if (a<b) return a;
        else return b;
    }

}
