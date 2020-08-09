package part2.week3;

import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.HashMap;

public class BaseballElimination {
    private final HashMap<String, TeamInfo> commandsInfo;
    private final HashMap<Integer, TeamInfo> commandsInfoById;
    private final int teamsCount;
    private final int[][] remainingGamesTable;

    // create a baseball division from given filename in format specified below
    public BaseballElimination(String filename) {
        In in = new In(filename);
        commandsInfo = new HashMap<>();
        commandsInfoById = new HashMap<>();
        teamsCount = in.readInt();
        remainingGamesTable = new int[teamsCount][teamsCount];
        for (int i = 0; i < teamsCount; i++) {
            String name = in.readString();
            int wins = in.readInt();
            int loses = in.readInt();
            int remainingGames = in.readInt();
            commandsInfo.put(name, new TeamInfo(name, wins, loses, remainingGames, i));
            commandsInfoById.put(i, new TeamInfo(name, wins, loses, remainingGames, i));
            for (int j = 0; j < teamsCount; j++)
                remainingGamesTable[i][j] = in.readInt();
        }
    }

    // number of teams
    public int numberOfTeams() {
        return teamsCount;
    }

    // all teams
    public Iterable<String> teams() {
        return commandsInfo.keySet();
    }

    // number of wins for given team
    public int wins(String team) {
        if (!commandsInfo.containsKey(team))
            throw new IllegalArgumentException();
        return commandsInfo.get(team).getWins();
    }

    // number of losses for given team
    public int losses(String team) {
        if (!commandsInfo.containsKey(team))
            throw new IllegalArgumentException();
        return commandsInfo.get(team).getLoses();
    }

    // number of remaining games for given team
    public int remaining(String team) {
        if (!commandsInfo.containsKey(team))
            throw new IllegalArgumentException();
        return commandsInfo.get(team).getRemainingGames();
    }

    // number of remaining games between team1 and team2
    public int against(String team1, String team2) {
        if (!commandsInfo.containsKey(team1) || !commandsInfo.containsKey(team2))
            throw new IllegalArgumentException();
        return remainingGamesTable[commandsInfo.get(team1).getId()][commandsInfo.get(team2).getId()];
    }

    // is given team eliminated?
    public boolean isEliminated(String team) {
        if (!commandsInfo.containsKey(team))
            throw new IllegalArgumentException();
        var teamInfo = commandsInfo.get(team);
        int maxPossibleWins = teamInfo.getWins() + teamInfo.getRemainingGames();
        for (TeamInfo info : commandsInfo.values()) {
            if (info.getWins() > maxPossibleWins)
                return true;
        }
        FlowNetwork network = new FlowNetwork(((teamsCount - 1) * (teamsCount - 2) / 2) + (teamsCount - 1) + 2);
        int nextEdge = 1;
        int expectedValue = 0;
        var gamesCounter = (teamsCount - 1) * (teamsCount - 2) / 2 + 1;
        for (int i = 0; i < teamsCount; i++) {
            for (int j = 0; j < i; j++) {
                if (i == teamInfo.getId() || j == teamInfo.getId())
                    continue;
                network.addEdge(new FlowEdge(0, nextEdge, remainingGamesTable[i][j]));
                int team1 = gamesCounter + i - (i > teamInfo.getId() ? 1 : 0);
                network.addEdge(new FlowEdge(nextEdge, team1, Integer.MAX_VALUE));
                int team2 = gamesCounter + j - (j > teamInfo.getId() ? 1 : 0);
                network.addEdge(new FlowEdge(nextEdge++, team2, Integer.MAX_VALUE));
                expectedValue += remainingGamesTable[i][j];
            }
        }
        for (int i = 0; i < teamsCount; i++) {
            var currentInfo = commandsInfoById.get(i);
            if (currentInfo.getName().equals(team))
                continue;
            network.addEdge(new FlowEdge(nextEdge++, network.V() - 1, maxPossibleWins - commandsInfo.get(currentInfo.getName()).getWins()));
        }
        FordFulkerson solver = new FordFulkerson(network, 0, network.V() - 1);
        return expectedValue != solver.value();
    }



    // subset R of teams that eliminates given team; null if not eliminated
    public Iterable<String> certificateOfElimination(String team) {
        if (!commandsInfo.containsKey(team))
            throw new IllegalArgumentException();
        var teamInfo = commandsInfo.get(team);
        int maxPossibleWins = teamInfo.getWins() + teamInfo.getRemainingGames();
        ArrayList<String> result = new ArrayList<>();
        for (TeamInfo info : commandsInfo.values()) {
            if (info.getWins() > maxPossibleWins)
                result.add(info.getName());
        }
        if (!result.isEmpty())
            return result;
        FlowNetwork network = new FlowNetwork(((teamsCount - 1) * (teamsCount - 2) / 2) + (teamsCount - 1) + 2);
        int nextEdge = 1;
        int expectedValue = 0;
        var gamesCounter = (teamsCount - 1) * (teamsCount - 2) / 2 + 1;
        for (int i = 0; i < teamsCount; i++) {
            for (int j = 0; j < i; j++) {
                if (i == teamInfo.getId() || j == teamInfo.getId())
                    continue;
                network.addEdge(new FlowEdge(0, nextEdge, remainingGamesTable[i][j]));
                int team1 = gamesCounter + i - (i > teamInfo.getId() ? 1 : 0);
                network.addEdge(new FlowEdge(nextEdge, team1, Integer.MAX_VALUE));
                int team2 = gamesCounter + j - (j > teamInfo.getId() ? 1 : 0);
                network.addEdge(new FlowEdge(nextEdge++, team2, Integer.MAX_VALUE));
                expectedValue += remainingGamesTable[i][j];
            }
        }
        for (int i = 0; i < teamsCount; i++) {
            var currentInfo = commandsInfoById.get(i);
            if (currentInfo.getName().equals(team))
                continue;
            network.addEdge(new FlowEdge(nextEdge++, network.V() - 1, maxPossibleWins - commandsInfo.get(currentInfo.getName()).getWins()));
        }
        FordFulkerson solver = new FordFulkerson(network, 0, network.V() - 1);
        if (expectedValue == solver.value())
            return null;
        int i = -1;
        for (String name : teams()) {
            i++;
            if (name.equals(team))
                continue;
            if (solver.inCut(gamesCounter + i))
                result.add(name);
        }
        return result;
    }

    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            }
            else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }

}

