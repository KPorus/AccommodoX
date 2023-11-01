package User_Page;

import DB.MySQLConnection;
import DB.UserDAO;
import Design.GradientPanel;
import User_data.Offer;
import User_data.User;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class Offers extends JFrame {

    private UserDAO userDAO;
    private final JPanel offersPanel;
    private final MySQLConnection mysqlConnection;
    private List<Offer> selectedOffers = new ArrayList<>();
    private final List<Offer> allOffers;

    public Offers(int userId) {

        mysqlConnection = new MySQLConnection();
        this.userDAO = new UserDAO(mysqlConnection.getConnection()); // Initialize UserDAO

        setTitle("Offers");
        setIconImage(getAppIcon());
       
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 50, 1000, 500);
        setResizable(false);
        setContentPane(new GradientPanel());

        // Create a main content panel with BorderLayout
        JPanel mainContentPanel = new JPanel(new BorderLayout());
        mainContentPanel.setOpaque(false);

        // Create a panel for the menu options
        JPanel menuPanel = new JPanel(new GridLayout(6, 1, 0, 20)); // Increase the row count to accommodate the "Add Room" button
        menuPanel.setOpaque(false);

        JButton profile = new JButton("Profile");
        JButton users = new JButton("Customers");
        JButton rooms = new JButton("Rooms");
        JButton offer = new JButton("Offers");
        rooms.addActionListener((ActionEvent e) -> {
            new rooms(userId).setVisible(true);
            dispose();
        });
        users.addActionListener((ActionEvent e) -> {
            new allCustomer(null, userId).setVisible(true);
            dispose();
        });
        JButton emp = new JButton("Employees");
        emp.addActionListener((ActionEvent e) -> {
            new employee(userId).setVisible(true);
            dispose();
        });
        profile.addActionListener((ActionEvent e) -> {
            User userData = userDAO.getUser(userId); // Fetch user data by ID
            new admin(userData).setVisible(true);
            dispose();
        });
        offer.addActionListener((ActionEvent e) -> {
            // Refresh the offers panel when the "Offers" button is clicked
            updateOffersPanel();
            System.out.println("Function called");
        });
        menuPanel.add(profile);
        menuPanel.add(users);
        menuPanel.add(emp);
        menuPanel.add(rooms);
        menuPanel.add(offer);

        mainContentPanel.add(menuPanel, BorderLayout.WEST);

        // Create a panel for the welcome message and user information
        JPanel welcomePanel = new JPanel(new BorderLayout());
        welcomePanel.setOpaque(false);

        JLabel message = new JLabel("Offers");
        message.setBorder(BorderFactory.createEmptyBorder(20, 20, 0, 0));
        message.setForeground(new Color(255, 255, 255));

        welcomePanel.add(message, BorderLayout.NORTH);

        // Create a panel to display the offers
        offersPanel = new JPanel();
        offersPanel.setLayout(new BoxLayout(offersPanel, BoxLayout.PAGE_AXIS));

        // Retrieve the list of offers from the database
        allOffers = userDAO.getAllOffers();

        // Add a mouse click listener to each offer box
        for (Offer off : allOffers) {
            JPanel offerBox = createOfferBox(off); // Create the offer box panel
            offersPanel.add(offerBox);

            // Add a mouse click listener to the offer box
            offerBox.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() == 1) {
                        // Single click, select the offer
                        System.out.println(e.getClickCount());
                        selectedOffers.clear();
                        selectOffer(off);
                    }
                }
            });
        }

        // Wrap the offersPanel in a JScrollPane for scrolling if there are many offers
        JScrollPane offersScrollPane = new JScrollPane(offersPanel);

        welcomePanel.add(offersScrollPane, BorderLayout.CENTER);
        // Add the offersScrollPane to the main content panel
        mainContentPanel.add(welcomePanel, BorderLayout.CENTER);

        // Add pagination controls
        JPanel paginationPanel = new JPanel();
        paginationPanel.setOpaque(false);
        JButton Open = new JButton("ADD OFFERS");
        JButton close = new JButton("CLOSE OFFERS");
        JButton Delete = new JButton("Delete OFFERS");

        paginationPanel.add(Open);
        paginationPanel.add(close);
        paginationPanel.add(Delete);
        mainContentPanel.add(paginationPanel, BorderLayout.SOUTH);
        mainContentPanel.add(welcomePanel, BorderLayout.CENTER);
        // Add the main content panel to the JFrame
        getContentPane().add(mainContentPanel);

        Open.addActionListener((ActionEvent e) -> {
            // Show the input dialog
            String title1 = JOptionPane.showInputDialog(Offers.this, "Enter Offer Title:");
            String description = JOptionPane.showInputDialog(Offers.this, "Enter Offer Description:");
            String percentInput = JOptionPane.showInputDialog(Offers.this, "Enter Offer amount (in percent):");
            String room_type = JOptionPane.showInputDialog(Offers.this, "Enter room types (Queen, Master, Normal, Double): ");

            // Check if both title, description, room_type, and percent are not empty or null
            if (title1 != null && !title1.isEmpty()
                    && description != null && !description.isEmpty()
                    && room_type != null && !room_type.isEmpty()
                    && percentInput != null && !percentInput.isEmpty()) {
                try {
                    int percent = Integer.parseInt(percentInput);

                    // Ensure the room_type is a valid option
                    String[] validRoomTypes = {"queen", "master", "normal", "double"};
                    String normalizedRoomType = room_type.toLowerCase();
                    System.out.println(normalizedRoomType);
                    if (Arrays.asList(validRoomTypes).contains(normalizedRoomType)) {
                        if(userDAO.updateRoomOffer(normalizedRoomType, percent))
                        {
                            userDAO.insertOffer(title1, description, "open");
                        }else
                        {
                            JOptionPane.showMessageDialog(Offers.this, "Failed to update room");
                        }
                        
                        // Refresh the offers panel to display the newly added offer
                        updateOffersPanel();
                    } else {
                        JOptionPane.showMessageDialog(Offers.this, "Invalid room type. Please enter a valid room type.");
                    }
                } catch (NumberFormatException ex) {
                    // Handle the case when "percentInput" is not a valid integer
                    JOptionPane.showMessageDialog(Offers.this, "Please enter a valid offer amount.");
                }
            } else {
                // Handle the case when either title, description, room_type, or percent is empty
                JOptionPane.showMessageDialog(Offers.this, "Title, description, room type, and offer amount are required.");
            }
        });

        close.addActionListener((ActionEvent e) -> {
            System.out.println(selectedOffers.isEmpty());
            if (selectedOffers.isEmpty() == false) {
                for (Offer selectedOffer : selectedOffers) {
                    System.out.println(selectedOffers);
                    boolean updated = userDAO.updateOffer(selectedOffer.getOfferId(), "close");
                    System.out.println(updated);
                    if (updated) {
                        JOptionPane.showMessageDialog(Offers.this, "Offer has been closed.");
                        System.out.println("Offer with ID " + selectedOffer.getOfferId() + " has been closed.");
                    } else {
                        JOptionPane.showMessageDialog(Offers.this, "Failed to close offer)");
                    }
                }
                // Clear the list of selected offers after closing
                selectedOffers.clear();
                // Refresh the offers panel after closing the selected offers
                updateOffersPanel();
            } else {

                JOptionPane.showMessageDialog(Offers.this, "Please select a offer.");
//                    selectedOffers.clear();
            }
        });

        Delete.addActionListener((ActionEvent e) -> {
            System.out.println(selectedOffers.isEmpty());
            if (selectedOffers.isEmpty() == false) {
                for (Offer selectedOffer : selectedOffers) {
                    System.out.println(selectedOffers);
                    boolean updated = userDAO.deleteOffer(selectedOffer.getOfferId());
                    System.out.println(updated);
                    if (updated) {
                        JOptionPane.showMessageDialog(Offers.this, "Offer has been deleted.");
                        System.out.println("Offer with ID " + selectedOffer.getOfferId() + " has been deleted.");
                    } else {
                        JOptionPane.showMessageDialog(Offers.this, "Failed to delete offer)");
                    }
                }
                // Clear the list of selected offers after closing
                selectedOffers.clear();
                // Refresh the offers panel after closing the selected offers
                updateOffersPanel();
            } else {

                JOptionPane.showMessageDialog(Offers.this, "Please select a offer.");
//                    selectedOffers.clear();
            }
        });
    }

    private void updateOffersPanel() {
        // Clear the offersPanel
        offersPanel.removeAll();
        System.out.println("Updated function.");

        if (allOffers.isEmpty()) {
            // No offers available
            JLabel noOfferLabel = new JLabel("No offers available");
            noOfferLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
            offersPanel.add(noOfferLabel);
        } else {
            // Display each offer in a box-like panel
            for (Offer off : allOffers) {
                JPanel offerBox = createOfferBox(off); // Create the offer box panel
                offersPanel.add(offerBox);
            }
        }

        // Repaint the offersPanel
        offersPanel.revalidate();
        offersPanel.repaint();
        System.out.println("Updated the offersPanel.");
    }

    // Create a method to create an offer box panel
    private JPanel createOfferBox(Offer off) {
        JPanel offerBox = new JPanel();
        offerBox.setPreferredSize(new Dimension(500, -1));
        offerBox.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        offerBox.setLayout(new BoxLayout(offerBox, BoxLayout.Y_AXIS));
        offerBox.setOpaque(false);
        offerBox.setBackground(new Color(0, 0, 0, 0)); // Transparent background
        JLabel titleLabel = new JLabel("Title: " + off.getTitle());
        JLabel descriptionLabel = new JLabel("Description: " + off.getDescription());

        // Check the status and set background color accordingly
        if ("open".equals(off.getStatus())) {
            offerBox.setBackground(Color.GREEN);
            titleLabel.setForeground(Color.WHITE);
            descriptionLabel.setForeground(Color.WHITE);
        } else if ("close".equals(off.getStatus())) {
            offerBox.setBackground(Color.RED);
            titleLabel.setForeground(Color.WHITE);
            descriptionLabel.setForeground(Color.WHITE);
        }

        offerBox.setOpaque(true); // Ensure that the panel's background is visible

        offerBox.add(titleLabel);
        offerBox.add(descriptionLabel);

        return offerBox;
    }

    // Add a method to select offers
    public void selectOffer(Offer offer) {
        System.out.println("Selected offer: " + offer.getTitle());
        selectedOffers.add(offer);
    }
    private Image getAppIcon() {
        ImageIcon icon = new ImageIcon("D:\\Java Project\\AccommodoX\\src\\Images\\hotel.jpeg");
        return icon.getImage();
    }
}
