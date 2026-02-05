package Coursework;


import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.io.*;
import java.time.LocalDate;
import java.time.Period;

public class UpHighschoolOlevelRegistrationForm extends JFrame {

    // Fields
    private JTextField txtFirst, txtLast, txtEmail, txtConfirmEmail;
    private JPasswordField txtPassword, txtConfirmPassword;
    private JComboBox<Integer> cbYear, cbDay;
    private JComboBox<String> cbMonth, cbDept;
    private JRadioButton rbMale, rbFemale;
    private JTextArea txtOutput;
    private JLabel lblPasswordError;
    private JButton btnSubmit;

    public UpHighschoolOlevelRegistrationForm() {
        setTitle("Student Registration");
        setSize(850, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        add(buildForm(), BorderLayout.CENTER);
        add(buildOutput(), BorderLayout.EAST);

        setupDOB();
        setupPasswordValidation();
    }

    private JPanel buildForm() {
        JPanel p = new JPanel(new GridLayout(12, 2, 5, 5));
        p.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        txtFirst = new JTextField();
        txtLast = new JTextField();
        txtEmail = new JTextField();
        txtConfirmEmail = new JTextField();
        txtPassword = new JPasswordField();
        txtConfirmPassword = new JPasswordField();

        lblPasswordError = new JLabel();
        lblPasswordError.setForeground(Color.RED);

        cbYear = new JComboBox<>();
        cbMonth = new JComboBox<>();
        cbDay = new JComboBox<>();

        rbMale = new JRadioButton("Male");
        rbFemale = new JRadioButton("Female");
        ButtonGroup bgGender = new ButtonGroup();
        bgGender.add(rbMale);
        bgGender.add(rbFemale);

        cbDept = new JComboBox<>(new String[]{
            "Civil", "CSE", "Electrical", "E&C", "Mechanical"
        });

        btnSubmit = new JButton("Submit");
        JButton btnCancel = new JButton("Cancel");

        btnSubmit.addActionListener(e -> handleSubmit());
        btnCancel.addActionListener(e -> System.exit(0));

        p.add(new JLabel("First Name")); p.add(txtFirst);
        p.add(new JLabel("Last Name")); p.add(txtLast);
        p.add(new JLabel("Email")); p.add(txtEmail);
        p.add(new JLabel("Confirm Email")); p.add(txtConfirmEmail);
        p.add(new JLabel("Password")); p.add(txtPassword);
        p.add(new JLabel("Confirm Password")); p.add(txtConfirmPassword);
        p.add(new JLabel("")); p.add(lblPasswordError);
        p.add(new JLabel("DOB")); p.add(buildDOBPanel());
        p.add(new JLabel("Gender")); p.add(buildGenderPanel());
        p.add(new JLabel("Department")); p.add(cbDept);
        p.add(btnSubmit); p.add(btnCancel);

        return p;
    }

    private JPanel buildDOBPanel() {
        JPanel p = new JPanel();
        p.add(cbYear);
        p.add(cbMonth);
        p.add(cbDay);
        return p;
    }

    private JPanel buildGenderPanel() {
        JPanel p = new JPanel();
        p.add(rbMale);
        p.add(rbFemale);
        return p;
    }

    private JScrollPane buildOutput() {
        txtOutput = new JTextArea(20, 30);
        txtOutput.setEditable(false);
        return new JScrollPane(txtOutput);
    }

    // ---------------- DOB ----------------
    private void setupDOB() {
        for (int y = 1990; y <= LocalDate.now().getYear(); y++) cbYear.addItem(y);
        cbMonth.setModel(new DefaultComboBoxModel<>(new String[]{
            "Jan","Feb","Mar","Apr","May","Jun",
            "Jul","Aug","Sep","Oct","Nov","Dec"
        }));

        cbYear.addActionListener(e -> updateDays());
        cbMonth.addActionListener(e -> updateDays());
        updateDays();
    }

    private void updateDays() {
        cbDay.removeAllItems();
        int year = (int) cbYear.getSelectedItem();
        int month = cbMonth.getSelectedIndex() + 1;
        int days = LocalDate.of(year, month, 1).lengthOfMonth();
        for (int d = 1; d <= days; d++) cbDay.addItem(d);
    }

    // ---------------- PASSWORD LIVE VALIDATION ----------------
    private void setupPasswordValidation() {
        DocumentListener dl = new DocumentListener() {
            private void validatePwd() {
                String p = new String(txtPassword.getPassword());
                String c = new String(txtConfirmPassword.getPassword());

                if (!p.matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,20}$")) {
                    lblPasswordError.setText("8–20 chars, letters & numbers required");
                    btnSubmit.setEnabled(false);
                } else if (!c.isEmpty() && !p.equals(c)) {
                    lblPasswordError.setText("Passwords do not match");
                    btnSubmit.setEnabled(false);
                } else {
                    lblPasswordError.setText("");
                    btnSubmit.setEnabled(true);
                }
            }
            public void insertUpdate(DocumentEvent e){ validatePwd(); }
            public void removeUpdate(DocumentEvent e){ validatePwd(); }
            public void changedUpdate(DocumentEvent e){ validatePwd(); }
        };
        txtPassword.getDocument().addDocumentListener(dl);
        txtConfirmPassword.getDocument().addDocumentListener(dl);
    }

    // ---------------- SUBMIT ----------------
    private void handleSubmit() {
        try {
            String email = txtEmail.getText().trim();
            if (!email.matches("^[^@]+@[^@]+\\.[^@]+$"))
                throw new Exception("Invalid email");

            if (!email.equals(txtConfirmEmail.getText().trim()))
                throw new Exception("Emails do not match");

            LocalDate dob = LocalDate.of(
                (int) cbYear.getSelectedItem(),
                cbMonth.getSelectedIndex() + 1,
                (int) cbDay.getSelectedItem()
            );

            int age = Period.between(dob, LocalDate.now()).getYears();
            if (age < 16 || age > 60)
                throw new Exception("Age must be 16–60");

            String gender = rbMale.isSelected() ? "M" : rbFemale.isSelected() ? "F" : null;
            if (gender == null) throw new Exception("Select gender");

            String dept = cbDept.getSelectedItem().toString();
            String id = generateID();

            String record = String.format(
                "%s | %s %s | %s | %s | %s | %s",
                id, txtFirst.getText(), txtLast.getText(),
                gender, dept, dob, email
            );

            txtOutput.append(record + "\n");
            saveCSV(record);
            JOptionPane.showMessageDialog(this, "Registration successful");

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ---------------- CSV ----------------
    private void saveCSV(String line) throws IOException {
        File f = new File("students.csv");
        try (FileWriter fw = new FileWriter(f, true)) {
            fw.write(line.replace(" | ", ",") + "\n");
        }
    }

    private String generateID() {
        int year = LocalDate.now().getYear();
        int count = 1;
        File f = new File("students.csv");
        if (f.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(f))) {
                count = (int) br.lines().filter(l -> l.startsWith("" + year)).count() + 1;
            } catch (Exception ignored) {}
        }
        return String.format("%d-%05d", year, count);
    }

    // ---------------- MAIN ----------------
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() ->
            new UpHighschoolOlevelRegistrationForm().setVisible(true)
        );
    }
}
