import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;

public class EventBookingSystemApp extends JFrame {
    private ArrayList<User> users = new ArrayList<>();
    private ArrayList<Event> events = new ArrayList<>();
    private ArrayList<Ticket> tickets = new ArrayList<>();
    private User loggedInUser;

    public EventBookingSystemApp() {
        setTitle("🎫 Event Booking System");
        setSize(750, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        loadUsersFromFile();
        loadEventsFromFile();
        loadTicketsFromFile();

        showLoginPanel();
        setVisible(true);
    }

    // ---------------- LOGIN ----------------
    private void showLoginPanel() {
        JPanel loginPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        JTextField emailField = new JTextField();
        JPasswordField passField = new JPasswordField();
        JButton loginBtn = new JButton("Login");
        JButton registerBtn = new JButton("Register");

        loginPanel.add(new JLabel("Email:"));
        loginPanel.add(emailField);
        loginPanel.add(new JLabel("Password:"));
        loginPanel.add(passField);
        loginPanel.add(loginBtn);
        loginPanel.add(registerBtn);

        getContentPane().removeAll();
        add(loginPanel);
        revalidate();
        repaint();

        loginBtn.addActionListener(e -> {
             String email = emailField.getText().trim();
             String pass = new String(passField.getPassword()).trim();

                // EMPTY FIELD VALIDATION
             if (email.isEmpty() || pass.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Email and password cannot be empty!");
                    return;
              }


             // ALLOW ADMIN WITHOUT GMAIL CHECK
             if (!email.equalsIgnoreCase("admin@event.com") && !email.endsWith("@gmail.com")) {
                JOptionPane.showMessageDialog(this, "Please enter a valid Gmail address!");
                return;
             }

             

                // CHECK USER LOGIN
             boolean found = false;
             for (User u : users) {
              if (u.getEmail().equalsIgnoreCase(email) && u.getPassword().equals(pass)) {
                  found = true;
                 loggedInUser = u;

                    if (u.getRole().equals("admin"))
                         showAdminPanel();
                    else
                         showEventPanel();

                        return;
                    }
                }

                if (!found) {
                    JOptionPane.showMessageDialog(this, "Incorrect email or password!");
                }
         });


        registerBtn.addActionListener(e -> showRegisterPanel());
    }

    // ---------------- REGISTER ----------------
    private void showRegisterPanel() {
        JPanel registerPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        JTextField nameField = new JTextField();
        JTextField emailField = new JTextField();
        JPasswordField passField = new JPasswordField();
        JButton registerBtn = new JButton("Register");

        registerPanel.add(new JLabel("Name:"));
        registerPanel.add(nameField);
        registerPanel.add(new JLabel("Email:"));
        registerPanel.add(emailField);
        registerPanel.add(new JLabel("Password:"));
        registerPanel.add(passField);
        registerPanel.add(new JLabel(""));
        registerPanel.add(registerBtn);

        getContentPane().removeAll();
        add(registerPanel);
        revalidate();
        repaint();

        registerBtn.addActionListener(e -> {
            String name = nameField.getText().trim();
            String email = emailField.getText().trim();
            String password = new String(passField.getPassword()).trim();

            // ✅ Empty field check
            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields!");
                return;
            }

            // ✅ Gmail validation check
            if (!email.endsWith("@gmail.com")) {
                JOptionPane.showMessageDialog(this, "Please enter a valid Gmail address ending with @gmail.com");
                return;
            }

            // ✅ Check for duplicates
            for (User u : users) {
                if (u.getEmail().equalsIgnoreCase(email)) {
                    JOptionPane.showMessageDialog(this, "This email is already registered!");
                    return;
                }
            }

            // ✅ If everything is valid
            users.add(new User(name, email, password, "user"));
            saveUsersToFile();
            JOptionPane.showMessageDialog(this, "Registration successful!");
            showLoginPanel();
        });

    }

    // ---------------- ADMIN DASHBOARD ----------------
    private void showAdminPanel() {
        JPanel adminPanel = new JPanel(new BorderLayout());
        JLabel title = new JLabel("Admin Dashboard", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        adminPanel.add(title, BorderLayout.NORTH);

        JButton addEventBtn = new JButton("Add Event");
        JButton manageEventBtn = new JButton("Manage Events");
        JButton manageBookingsBtn = new JButton("Manage Bookings");
        JButton logoutBtn = new JButton("Logout");

        JPanel btnPanel = new JPanel();
        btnPanel.add(addEventBtn);
        btnPanel.add(manageEventBtn);
        btnPanel.add(manageBookingsBtn);
        btnPanel.add(logoutBtn);

        adminPanel.add(btnPanel, BorderLayout.CENTER);

        getContentPane().removeAll();
        add(adminPanel);
        revalidate();
        repaint();

        addEventBtn.addActionListener(e -> showAddEventPanel());
        manageEventBtn.addActionListener(e -> showAdminEventList());
        manageBookingsBtn.addActionListener(e -> showAllBookingsForAdmin());
        logoutBtn.addActionListener(e -> { loggedInUser = null; showLoginPanel(); });
    }

    // ---------------- USER EVENT LIST ----------------
    private void showEventPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        String[] cols = {"ID", "Title", "Date", "Seats", "Price"};
        Object[][] data = new Object[events.size()][5];
        for (int i = 0; i < events.size(); i++) {
            Event ev = events.get(i);
            data[i] = new Object[]{ev.getId(), ev.getTitle(), ev.getDate(), ev.getAvailableSeats(), ev.getPrice()};
        }

        JTable table = new JTable(data, cols);
        JButton bookBtn = new JButton("Book");
        JButton myBookingsBtn = new JButton("My Bookings");
        JButton logoutBtn = new JButton("Logout");

        JPanel btnPanel = new JPanel();
        btnPanel.add(bookBtn);
        btnPanel.add(myBookingsBtn);
        btnPanel.add(logoutBtn);

        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        panel.add(btnPanel, BorderLayout.SOUTH);

        getContentPane().removeAll();
        add(panel);
        revalidate();
        repaint();

        bookBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) showBookingPanel(events.get(row));
            else JOptionPane.showMessageDialog(this, "Select an event!");
        });

        myBookingsBtn.addActionListener(e -> showBookings());
        logoutBtn.addActionListener(e -> { loggedInUser = null; showLoginPanel(); });
    }

    // ---------------- BOOK EVENT ----------------
    private void showBookingPanel(Event ev) {
        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        JTextField seatsField = new JTextField();
        JButton confirmBtn = new JButton("Confirm");

        panel.add(new JLabel("Event: " + ev.getTitle()));
        panel.add(new JLabel(""));
        panel.add(new JLabel("Seats:"));
        panel.add(seatsField);
        panel.add(new JLabel(""));
        panel.add(confirmBtn);

        getContentPane().removeAll();
        add(panel);
        revalidate();
        repaint();

        confirmBtn.addActionListener(e -> {
            try {
                int count = Integer.parseInt(seatsField.getText());
                if (ev.bookSeats(count)) {
                    Ticket t = new Ticket(loggedInUser, ev, count);
                    tickets.add(t);
                    saveTicketsToFile();
                    saveEventsToFile();
                    JOptionPane.showMessageDialog(this, "Booking confirmed!\n" + t.getTicketInfo());
                    showEventPanel();
                } else JOptionPane.showMessageDialog(this, "Not enough seats!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Enter valid number!");
            }
        });
    }

    // ---------------- ADMIN: ADD EVENT ----------------
    private void showAddEventPanel() {
        JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10));
        JTextField title = new JTextField();
        JTextField date = new JTextField();
        JTextField seats = new JTextField();
        JTextField price = new JTextField();
        JButton addBtn = new JButton("Add");
        JButton backBtn = new JButton("Back");

        panel.add(new JLabel("Title:")); panel.add(title);
        panel.add(new JLabel("Date:")); panel.add(date);
        panel.add(new JLabel("Seats:")); panel.add(seats);
        panel.add(new JLabel("Price:")); panel.add(price);
        panel.add(addBtn); panel.add(backBtn);

        getContentPane().removeAll(); add(panel); revalidate(); repaint();

        addBtn.addActionListener(e -> {
            try {
                Event ev = new Event(events.size() + 1, title.getText(), date.getText(),
                        Integer.parseInt(seats.getText()), Double.parseDouble(price.getText()));
                events.add(ev);
                saveEventsToFile();
                JOptionPane.showMessageDialog(this, "Event added!");
                showAdminPanel();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid input!");
            }
        });

        backBtn.addActionListener(e -> showAdminPanel());
    }

    // ---------------- ADMIN: MANAGE EVENTS ----------------
    private void showAdminEventList() {
        JPanel panel = new JPanel(new BorderLayout());
        String[] cols = {"ID", "Title", "Date", "Seats", "Price"};
        Object[][] data = new Object[events.size()][5];
        for (int i = 0; i < events.size(); i++) {
            Event ev = events.get(i);
            data[i] = new Object[]{ev.getId(), ev.getTitle(), ev.getDate(), ev.getAvailableSeats(), ev.getPrice()};
        }

        JTable table = new JTable(data, cols);
        JButton edit = new JButton("Edit");
        JButton del = new JButton("Delete");
        JButton back = new JButton("Back");

        JPanel btn = new JPanel();
        btn.add(edit); btn.add(del); btn.add(back);

        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        panel.add(btn, BorderLayout.SOUTH);

        getContentPane().removeAll(); add(panel); revalidate(); repaint();

        edit.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) showEditEventPanel(events.get(row));
            else JOptionPane.showMessageDialog(this, "Select an event!");
        });

        del.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                Event ev = events.get(row);
                int c = JOptionPane.showConfirmDialog(this, "Delete " + ev.getTitle() + "?", "Confirm", JOptionPane.YES_NO_OPTION);
                if (c == JOptionPane.YES_OPTION) {
                    events.remove(ev);
                    saveEventsToFile();
                    JOptionPane.showMessageDialog(this, "Deleted!");
                    showAdminEventList();
                }
            }
        });

        back.addActionListener(e -> showAdminPanel());
    }

    // ---------------- FIXED EDIT EVENT PANEL ----------------
    private void showEditEventPanel(Event ev) {
        JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10));
        JTextField titleField = new JTextField(ev.getTitle());
        JTextField dateField = new JTextField(ev.getDate());
        JTextField seatsField = new JTextField(String.valueOf(ev.getAvailableSeats()));
        JTextField priceField = new JTextField(String.valueOf(ev.getPrice()));
        JButton saveBtn = new JButton("Save");
        JButton backBtn = new JButton("Back");

        panel.add(new JLabel("Title:")); panel.add(titleField);
        panel.add(new JLabel("Date:")); panel.add(dateField);
        panel.add(new JLabel("Seats:")); panel.add(seatsField);
        panel.add(new JLabel("Price:")); panel.add(priceField);
        panel.add(saveBtn); panel.add(backBtn);

        getContentPane().removeAll(); add(panel); revalidate(); repaint();

        saveBtn.addActionListener(e -> {
            try {
                Event updated = new Event(ev.getId(), titleField.getText(), dateField.getText(),
                        Integer.parseInt(seatsField.getText()), Double.parseDouble(priceField.getText()));
                for (int i = 0; i < events.size(); i++) {
                    if (events.get(i).getId() == ev.getId()) {
                        events.set(i, updated);
                        break;
                    }
                }
                saveEventsToFile();
                JOptionPane.showMessageDialog(this, "Event updated successfully!");
                showAdminEventList();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid input!");
            }
        });

        backBtn.addActionListener(e -> showAdminEventList());
    }

    // ---------------- ADMIN: MANAGE BOOKINGS ----------------
    private void showAllBookingsForAdmin() {
        JPanel panel = new JPanel(new BorderLayout());
        String[] cols = {"User Email", "Event", "Date", "Seats"};
        Object[][] data = new Object[tickets.size()][4];
        for (int i = 0; i < tickets.size(); i++) {
            Ticket t = tickets.get(i);
            data[i] = new Object[]{t.getUser().getEmail(), t.getEvent().getTitle(),
                    t.getEvent().getDate(), t.getSeatsBooked()};
        }

        JTable table = new JTable(data, cols);
        JButton cancel = new JButton("Cancel Booking");
        JButton back = new JButton("Back");
        JPanel btn = new JPanel(); btn.add(cancel); btn.add(back);

        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        panel.add(btn, BorderLayout.SOUTH);

        getContentPane().removeAll(); add(panel); revalidate(); repaint();

        cancel.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                Ticket t = tickets.get(row);
                int c = JOptionPane.showConfirmDialog(this,
                        "Cancel booking for " + t.getUser().getEmail() + "?", "Confirm", JOptionPane.YES_NO_OPTION);
                if (c == JOptionPane.YES_OPTION) {
                    t.getEvent().cancelSeats(t.getSeatsBooked());
                    tickets.remove(t);
                    saveTicketsToFile();
                    saveEventsToFile();
                    JOptionPane.showMessageDialog(this, "Booking cancelled!");
                    showAllBookingsForAdmin();
                }
            }
        });

        back.addActionListener(e -> showAdminPanel());
    }

    // ---------------- USER: VIEW BOOKINGS ----------------
    private void showBookings() {
        StringBuilder sb = new StringBuilder();
        for (Ticket t : tickets)
            if (t.getUser().getEmail().equals(loggedInUser.getEmail()))
                sb.append(t.getTicketInfo()).append("\n-----------------\n");
        if (sb.length() == 0) sb.append("No bookings yet!");
        JOptionPane.showMessageDialog(this, sb.toString());
    }

    // ---------------- FILE I/O ----------------
    private void loadUsersFromFile() {
        users.clear();
        File f = new File("users.txt");
        if (!f.exists()) {
            try { f.createNewFile(); } catch (Exception ignored) {}
            users.add(new User("Admin", "admin@event.com", "admin123", "admin"));
            saveUsersToFile();
            return;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line; while ((line = br.readLine()) != null) {
                String[] p = line.split(","); if (p.length == 4)
                    users.add(new User(p[0], p[1], p[2], p[3]));
            }
        } catch (Exception ignored) {}
    }

    private void saveUsersToFile() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("users.txt"))) {
            for (User u : users) {
                bw.write(u.getName() + "," + u.getEmail() + "," + u.getPassword() + "," + u.getRole());
                bw.newLine();
            }
        } catch (Exception ignored) {}
    }

    private void loadEventsFromFile() {
        events.clear();
        File f = new File("events.txt");
        if (!f.exists()) { try { f.createNewFile(); } catch (Exception ignored) {} return; }
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line; while ((line = br.readLine()) != null) {
                String[] p = line.split(","); if (p.length == 5)
                    events.add(new Event(Integer.parseInt(p[0]), p[1], p[2],
                            Integer.parseInt(p[3]), Double.parseDouble(p[4])));
            }
        } catch (Exception ignored) {}
    }

    private void saveEventsToFile() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("events.txt"))) {
            for (Event e : events) {
                bw.write(e.getId() + "," + e.getTitle() + "," + e.getDate() + "," + e.getAvailableSeats() + "," + e.getPrice());
                bw.newLine();
            }
        } catch (Exception ignored) {}
    }

    private void loadTicketsFromFile() {
        tickets.clear();
        File f = new File("tickets.txt");
        if (!f.exists()) { try { f.createNewFile(); } catch (Exception ignored) {} return; }
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line; while ((line = br.readLine()) != null) {
                String[] p = line.split(","); if (p.length == 3) {
                    String email = p[0]; int eventId = Integer.parseInt(p[1]); int seats = Integer.parseInt(p[2]);
                    User u = users.stream().filter(us -> us.getEmail().equals(email)).findFirst().orElse(null);
                    Event e = events.stream().filter(ev -> ev.getId() == eventId).findFirst().orElse(null);
                    if (u != null && e != null) tickets.add(new Ticket(u, e, seats));
                }
            }
        } catch (Exception ignored) {}
    }

    private void saveTicketsToFile() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("tickets.txt"))) {
            for (Ticket t : tickets) {
                bw.write(t.getUser().getEmail() + "," + t.getEvent().getId() + "," + t.getSeatsBooked());
                bw.newLine();
            }
        } catch (Exception ignored) {}
    }
}
