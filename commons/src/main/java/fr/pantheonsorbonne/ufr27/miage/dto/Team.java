public class Team {
    private final List<User> users;
    private final String theme;
    private Integer teamId;

    public Team(List<User> users, int teamId) {
        this.users = users;
        this.theme = users.get(0).getTheme();
        this.teamId = ;
    }

    public List<User> getUsers() {
        return users;
    }

    public String getTheme() {
        return theme;
    }

    public int getTeamId() {
        return teamId;
    }
}
