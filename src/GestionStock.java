import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class GestionStock extends JFrame implements ActionListener {

    Container con;
    Connection connection;

    JMenuBar mbr = new JMenuBar();
    JMenu m1 = new JMenu("Fichier");
    JMenuItem ex = new JMenuItem("Exit");

    JLabel l1 = new JLabel("Famille :");
    JLabel l2 = new JLabel("Sous-famille :");
    JLabel l3 = new JLabel("Code :");
    JLabel l4 = new JLabel("Désignation :");
    JLabel l5 = new JLabel("Prix :");

    JComboBox<String> cb1 = new JComboBox<>();
    JComboBox<String> cb2 = new JComboBox<>();
    JTextField t1 = new JTextField(10);
    JTextField t2 = new JTextField(20);
    JTextField t3 = new JTextField(10);

    JButton b1 = new JButton("Insertion");
    JButton bExit = new JButton("Exit");

    public GestionStock() {
        con = getContentPane();
        con.setLayout(new BorderLayout());
        setSize(600, 400);
        setTitle("Gestion de stock");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Menu
        setJMenuBar(mbr);
        mbr.add(m1);
        m1.add(ex);

        // Panel principal
        JPanel panelForm = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        panelForm.add(l1, gbc);
        gbc.gridx = 1;
        panelForm.add(cb1, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panelForm.add(l2, gbc);
        gbc.gridx = 1;
        panelForm.add(cb2, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panelForm.add(l3, gbc);
        gbc.gridx = 1;
        panelForm.add(t1, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        panelForm.add(l4, gbc);
        gbc.gridx = 1;
        panelForm.add(t2, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        panelForm.add(l5, gbc);
        gbc.gridx = 1;
        panelForm.add(t3, gbc);

        // Boutons
        JPanel panelButtons = new JPanel();
        panelButtons.add(b1);
        panelButtons.add(bExit);

        con.add(panelForm, BorderLayout.CENTER);
        con.add(panelButtons, BorderLayout.SOUTH);

        // Listeners
        b1.addActionListener(this);
        bExit.addActionListener(this);
        ex.addActionListener(this);
        cb1.addActionListener(this);

        // Connexion BD
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/estem", "root", "");

            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT desF FROM famille");
            while (rs.next()) {
                cb1.addItem(rs.getString("desF"));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur d'initialisation : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == ex || e.getSource() == bExit) {
            JOptionPane.showMessageDialog(this, "Fermeture de l'application.");
            try {
                if (connection != null) connection.close();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Erreur lors de la fermeture de la connexion", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
            System.exit(0);
        }
        else if (e.getSource() == cb1) {
            try {
                cb2.removeAllItems();
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT codeF FROM famille WHERE desF = '" + cb1.getSelectedItem() + "'");

                if (rs.next()) {
                    int codeF = rs.getInt("codeF");
                    Statement stmtSF = connection.createStatement();
                    ResultSet rsSF = stmtSF.executeQuery("SELECT desS FROM sfamille WHERE codeF = " + codeF);

                    while (rsSF.next()) {
                        cb2.addItem(rsSF.getString("desS"));
                    }
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Erreur chargement sous-familles", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
        else if (e.getSource() == b1) {
            try {
                if (t1.getText().isEmpty() || t2.getText().isEmpty() || t3.getText().isEmpty()
                        || cb1.getSelectedIndex() == -1 || cb2.getSelectedIndex() == -1) {
                    JOptionPane.showMessageDialog(this, "Tous les champs doivent être remplis", "Erreur", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int codeP = Integer.parseInt(t1.getText());
                String desP = t2.getText();
                double prix = Double.parseDouble(t3.getText());

                Statement stmtF = connection.createStatement();
                ResultSet rsF = stmtF.executeQuery("SELECT codeF FROM famille WHERE desF = '" + cb1.getSelectedItem() + "'");
                rsF.next();
                int codeF = rsF.getInt("codeF");

                Statement stmtS = connection.createStatement();
                ResultSet rsS = stmtS.executeQuery("SELECT codeS FROM sfamille WHERE desS = '" + cb2.getSelectedItem() + "' AND codeF = " + codeF);
                rsS.next();
                int codeS = rsS.getInt("codeS");

                PreparedStatement pstmt = connection.prepareStatement(
                        "INSERT INTO produit (codeP, codeS, codeF, desP, prix) VALUES (?, ?, ?, ?, ?)");
                pstmt.setInt(1, codeP);
                pstmt.setInt(2, codeS);
                pstmt.setInt(3, codeF);
                pstmt.setString(4, desP);
                pstmt.setDouble(5, prix);
                pstmt.executeUpdate();

                JOptionPane.showMessageDialog(this, "Produit ajouté avec succès !");
                t1.setText(""); t2.setText(""); t3.setText("");

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Code et Prix doivent être numériques", "Erreur", JOptionPane.ERROR_MESSAGE);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Erreur base de données : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GestionStock().setVisible(true));
    }
}
