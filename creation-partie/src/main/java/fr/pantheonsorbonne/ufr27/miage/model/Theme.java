package fr.pantheonsorbonne.ufr27.miage.model;

public enum Theme {
    MUSIC("music"),
    SPORT_AND_LEISURE("sport_and_leisure"),
    FILM_AND_TV("film_and_tv"),
    ARTS_AND_LITERATURE("arts_and_literature"),
    HISTORY("history"),
    SOCIETY_AND_CULTURE("society_and_culture"),
    SCIENCE("science"),
    GEOGRAPHY("geography"),
    FOOD_AND_DRINK("food_and_drink"),
    GENERAL_KNOWLEDGE("general_knowledge");

    private final String value;

    Theme(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static Theme fromString(String text) {
        for (Theme theme : Theme.values()) {
            if (theme.value.equalsIgnoreCase(text)) {
                return theme;
            }
        }
        throw new IllegalArgumentException("No theme with value: " + text);
    }
}