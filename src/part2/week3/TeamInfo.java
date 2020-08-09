package part2.week3;

class TeamInfo {
    private final String name;
    private final int wins;
    private final int loses;
    private final int remainingGames;
    private final int id;

    public TeamInfo(String name, int wins, int loses, int remainingGames, int id) {
        this.name = name;
        this.wins = wins;
        this.loses = loses;
        this.remainingGames = remainingGames;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public int getLoses() {
        return loses;
    }

    public int getWins() {
        return wins;
    }

    public int getRemainingGames() {
        return remainingGames;
    }

    public String getName() {
        return name;
    }
}