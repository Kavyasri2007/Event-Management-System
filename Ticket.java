public class Ticket {
    private User user;
    private Event event;
    private int seatsBooked;

    public Ticket(User user, Event event, int seatsBooked) {
        this.user = user;
        this.event = event;
        this.seatsBooked = seatsBooked;
    }

    public User getUser() { return user; }
    public Event getEvent() { return event; }
    public int getSeatsBooked() { return seatsBooked; }

    public String getTicketInfo() {
        return "Event: " + event.getTitle() +
               "\nDate: " + event.getDate() +
               "\nSeats: " + seatsBooked +
               "\nTotal: ₹" + (seatsBooked * event.getPrice());
    }
}

