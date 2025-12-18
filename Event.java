public class Event {
    private int id;
    private String title;
    private String date;
    private int availableSeats;
    private double price;

    public Event(int id, String title, String date, int availableSeats, double price) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.availableSeats = availableSeats;
        this.price = price;
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getDate() { return date; }
    public int getAvailableSeats() { return availableSeats; }
    public double getPrice() { return price; }

    public boolean bookSeats(int count) {
        if (count <= availableSeats) {
            availableSeats -= count;
            return true;
        }
        return false;
    }

    public void cancelSeats(int count) {
        availableSeats += count;
    }
}
