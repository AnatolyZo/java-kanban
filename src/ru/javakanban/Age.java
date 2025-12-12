public enum Age {
    CHILD("Для детей"),
    ADULT("Для взрослых");

    private String russianName;

    Age(String russianName) {
        this.russianName = russianName;
    }

    public String getRussianName() {
        return russianName;
    }
}