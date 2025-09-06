import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

class Room {
    int roomNumber;
    String category;
    boolean available = true;

    Room(int num, String category) {
        this.roomNumber = num;
        this.category = category;
    }
}

class Booking {
    String customerName;
    Room room;

    Booking(String name, Room room) {
        this.customerName = name;
        this.room = room;
        room.available = false;
    }
}

public class HotelReservationSystem extends JFrame {
    private JTable roomTable;
    private DefaultTableModel model;
    private java.util.List<Room> rooms = new ArrayList<>();
    private java.util.List<Booking> bookings = new ArrayList<>();

    public HotelReservationSystem() {
        setTitle("🏨 Hotel Reservation System");
        setSize(800, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initRooms();
        initUI();
    }

    private void initRooms() {
        rooms.add(new Room(101, "Standard"));
        rooms.add(new Room(102, "Standard"));
        rooms.add(new Room(201, "Deluxe"));
        rooms.add(new Room(202, "Deluxe"));
        rooms.add(new Room(301, "Suite"));
        rooms.add(new Room(302, "Suite"));
    }

    private void initUI() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(new Color(245, 245, 255));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel title = new JLabel("🏨 Welcome to Hotel Reservation System", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(new Color(25, 25, 112));

        String[] cols = {"Room Number", "Category", "Availability"};
        model = new DefaultTableModel(cols, 0);
        roomTable = new JTable(model);
        roomTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        roomTable.setRowHeight(28);
        refreshRoomTable();

        JScrollPane scroll = new JScrollPane(roomTable);

        JButton bookBtn = styledButton("📅 Book Room", new Color(34, 139, 34));
        JButton cancelBtn = styledButton("❌ Cancel Booking", new Color(178, 34, 34));
        JButton viewBtn = styledButton("📖 View Bookings", new Color(70, 130, 180));
        JButton refreshBtn = styledButton("🔄 Refresh", new Color(218, 165, 32));

        JPanel btnPanel = new JPanel(new GridLayout(1, 4, 10, 10));
        btnPanel.setBackground(new Color(245, 245, 255));
        btnPanel.add(bookBtn);
        btnPanel.add(cancelBtn);
        btnPanel.add(viewBtn);
        btnPanel.add(refreshBtn);

        panel.add(title, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);
        panel.add(btnPanel, BorderLayout.SOUTH);

        add(panel);

        bookBtn.addActionListener(e -> bookRoom());
        cancelBtn.addActionListener(e -> cancelBooking());
        viewBtn.addActionListener(e -> viewBookings());
        refreshBtn.addActionListener(e -> refreshRoomTable());
    }

    private JButton styledButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        return btn;
    }

    private void refreshRoomTable() {
        model.setRowCount(0);
        for (Room r : rooms) {
            model.addRow(new Object[]{r.roomNumber, r.category, r.available ? "✅ Available" : "❌ Booked"});
        }
    }

    private void bookRoom() {
        int row = roomTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "⚠️ Please select a room to book.");
            return;
        }

        int roomNum = (int) model.getValueAt(row, 0);
        Room selected = rooms.stream().filter(r -> r.roomNumber == roomNum).findFirst().get();

        if (!selected.available) {
            JOptionPane.showMessageDialog(this, "❌ Room already booked!");
            return;
        }

        String name = JOptionPane.showInputDialog(this, "Enter your name:");
        if (name == null || name.trim().isEmpty()) return;

        // Payment simulation
        int confirm = JOptionPane.showConfirmDialog(this,
                "💳 Simulate payment for Room " + roomNum + " (" + selected.category + ")?",
                "Payment", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            bookings.add(new Booking(name, selected));
            JOptionPane.showMessageDialog(this, "✅ Booking confirmed for " + name + "!");
            refreshRoomTable();
        }
    }

    private void cancelBooking() {
        String name = JOptionPane.showInputDialog(this, "Enter your name to cancel:");
        if (name == null || name.trim().isEmpty()) return;

        Booking found = null;
        for (Booking b : bookings) {
            if (b.customerName.equalsIgnoreCase(name)) {
                found = b;
                break;
            }
        }

        if (found != null) {
            found.room.available = true;
            bookings.remove(found);
            JOptionPane.showMessageDialog(this, "❌ Booking cancelled for " + name);
            refreshRoomTable();
        } else {
            JOptionPane.showMessageDialog(this, "⚠️ No booking found for " + name);
        }
    }

    private void viewBookings() {
        if (bookings.isEmpty()) {
            JOptionPane.showMessageDialog(this, "📖 No bookings yet.");
            return;
        }

        StringBuilder sb = new StringBuilder("📖 Current Bookings:\n\n");
        for (Booking b : bookings) {
            sb.append("👤 ").append(b.customerName)
              .append(" → Room ").append(b.room.roomNumber)
              .append(" (").append(b.room.category).append(")\n");
        }
        JOptionPane.showMessageDialog(this, sb.toString());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new HotelReservationSystem().setVisible(true));
    }
}

