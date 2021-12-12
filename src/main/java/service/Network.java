package service;

import domain.Friendship;
import domain.User;

import java.util.*;

/**
 * Network is a class for community operation
 * mat- matrix with integer values
 * size-integer(size of matrix)
 *ind-Set that contains Long keys
 */
public class Network {
    private Integer[][] mat;
    private Integer size;
    private Set<Long> ind;

    /**
     * create a matrix of friends connections
     * @param size of matrix, number of users
     */
    public Network(int size) {
        this.ind = new HashSet<>();
        this.mat = new Integer[size][size];
        this.size = size;
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
                this.mat[i][j] = 0;
    }

    /**
     * add a friendships to the network
     * @param list-list of Friendships
     */
    public void addFriendships(Iterable<Friendship> list) {
        list.forEach(f ->
                this.mat[(int) (f.getId().getLeft() - 1)][(int) (f.getId().getRight() - 1)] = 1);
        list.forEach(f ->
                this.mat[(int) (f.getId().getRight() - 1)][(int) (f.getId().getLeft() - 1)] = 1);

    }

    /**
     * add users to the network
     * @param list- list of Users
     */
    public void addUsers(Iterable<User> list) {
        list.forEach(x -> ind.add(x.getId()-1));
    }

    /**
     * dfs algorithm
     * @param v-integer
     * @param visited-boolean array
     */
    private void DFSUtils(int v, boolean[] visited) {
        visited[v] = true;
        for (int i = 0; i < size; i++)
            if (mat[v][i] == 1 && !visited[i])
                DFSUtils(i, visited);
    }

    /**
     * check how many connected Components we have
     * @return nr-integer (the result)
     */
    public int connectedComponents() {
        int nr = 0;
        boolean[] visited = new boolean[size];

        for (int i = 0; i < size; i++) {
            if (!visited[i] && ind.contains(Long.valueOf(i))) {
                DFSUtils(i, visited);
                nr++;
            }
        }
        return nr;
    }

    /**
     * find the biggest Component
     * @return list of Integer(the result)
     */
    public List<Long> biggestComponent() {
        boolean[] visited = new boolean[size];
        boolean[] viz=new boolean[size];
        List<Long>listFinal=new ArrayList<>();
        int maxim=0;
        for (int i = 0; i < size; i++)
            if (!visited[i] && ind.contains(Long.valueOf(i))) {
                DFSUtils(i, visited);
                int nr=0;
                List<Long>listForNow=new ArrayList<>();
                for(int p=0;p<size;p++){
                    if(visited[p]!=viz[p]){
                        nr++;
                        listForNow.add((long) p + 1);
                        viz[p]=visited[p];
                    }
                    if(nr>maxim){
                        listFinal.clear();
                        listFinal.addAll(listForNow);
                        maxim=nr;
                    }
                }
            }
        return listFinal;
    }
}
