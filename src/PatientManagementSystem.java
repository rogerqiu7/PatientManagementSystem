import javax.swing.*; // import all classes in swing library to create gui
import java.awt.*; // import abstract now toolkit to create windows, buttons, and other gui components
import java.awt.event.*; // import awt event to handle gui events, such as mouse clicks and key presses
import java.util.ArrayList; // import arraylist to use dynamic arrays, that can grow or shrink in size dynamically
import java.util.List; // list interface to handle lists, an ordered collection of elements
import java.util.stream.Collectors; // stream and collectors classes to perform filtering, mapping, and collecting

// public class patient management system
public class PatientManagementSystem {

    // Private classes below can only be accessed within the PatientManagementSystem class
    // JFrame class in swing used to create a window
    private JFrame frame;
    // JTextField objects to store various patient details, allows editing of text
    private JTextField firstNameField, lastNameField, dobField, lastAdmittedField, proceduresField, diagnosisField;
    // JComboBox provides drop-down list of items, hold string values of gender types
    private JComboBox<String> genderComboBox;
    // JCheckBox item that is either unchecked or on checked for older patients
    private JCheckBox isOlder65CheckBox;
    // create patients object as list to store the details of the Patient within the application
    private List<Patient> patients = new ArrayList<>();

    // constructor for PatientManagementSystem, executed when new instance of class is created
    public PatientManagementSystem() {
        initGUI();

        // add 3 dummy patients
        patients.add(new Patient("Roger", "Qiu", "11-01-1996", "01-01-2020", "Procedure1", "Diagnosis1", "Male", false));
        patients.add(new Patient("Jane", "Doe", "12-12-1995", "01-01-2021", "Procedure2", "Diagnosis2", "Female", true));
        patients.add(new Patient("John", "Doe", "10-10-1994", "01-01-2019", "Procedure3", "Diagnosis3", "Other", false));
    }

    // main entry point for app, JVM looks for this method to start executing the program
    // create a new instance of the PatientManagementSystem class, initializing the GUI of the application
    public static void main(String[] args) {
        new PatientManagementSystem();
    }

    // initialize gui components, set up the visual elements of the application interface
    private void initGUI() {

        // create frame with title, close operation and size
        frame = new JFrame("Patient Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);

        // create mainPanel with grid layout, add the below fields to the mainPanel
        JPanel mainPanel = new JPanel(new GridLayout(9, 2));

        // create text fields, with column size and label for data input
        firstNameField = new JTextField(20);
        mainPanel.add(new JLabel("First Name:"));
        mainPanel.add(firstNameField);

        lastNameField = new JTextField(20);
        mainPanel.add(new JLabel("Last Name:"));
        mainPanel.add(lastNameField);

        dobField = new JTextField(20);
        mainPanel.add(new JLabel("Date of Birth:"));
        mainPanel.add(dobField);

        lastAdmittedField = new JTextField(20);
        mainPanel.add(new JLabel("Last Admitted:"));
        mainPanel.add(lastAdmittedField);

        proceduresField = new JTextField(20);
        mainPanel.add(new JLabel("Procedures:"));
        mainPanel.add(proceduresField);

        diagnosisField = new JTextField(20);
        mainPanel.add(new JLabel("Diagnosis:"));
        mainPanel.add(diagnosisField);

        // combo box for selecting gender
        genderComboBox = new JComboBox<>(new String[]{"Male", "Female", "Other"});
        mainPanel.add(new JLabel("Gender:"));
        mainPanel.add(genderComboBox);

        // checkbox for age status
        isOlder65CheckBox = new JCheckBox("Is Older Than 65?");
        mainPanel.add(isOlder65CheckBox);

        // create save button with action listener, add to buttonsPanel
        JPanel buttonsPanel = new JPanel();
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> savePatient());
        buttonsPanel.add(saveButton);

        // create retrieve button with action listener, add to buttonsPanel
        JButton retrieveButton = new JButton("Retrieve");
        retrieveButton.addActionListener(e -> retrievePatients());
        buttonsPanel.add(retrieveButton);

        // add mainPanel and buttons panel to the frame
        frame.add(mainPanel, BorderLayout.CENTER);
        frame.add(buttonsPanel, BorderLayout.SOUTH);
        frame.setVisible(true);
    }

    // savePatient method to save patient information
    private void savePatient() {

        // retrieve and trim the data from each field
        String firstName = firstNameField.getText().trim();
        String lastName = lastNameField.getText().trim();
        String dob = dobField.getText().trim();
        String lastAdmitted = lastAdmittedField.getText().trim();
        String procedures = proceduresField.getText().trim();
        String diagnosis = diagnosisField.getText().trim();
        String gender = (String) genderComboBox.getSelectedItem();
        boolean isOlder65 = isOlder65CheckBox.isSelected();

        // first name or last name is empty, give error
        if (firstName.isEmpty() || lastName.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Please input required patient information.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // check if patient with same firstName and lastName already exists
        boolean existingPatient = false;
        for (Patient patient : patients) {
            if (patient.firstName.equalsIgnoreCase(firstName) && patient.lastName.equalsIgnoreCase(lastName)) {

                // update existing patient information
                patient.dob = dob;
                patient.lastAdmitted = lastAdmitted;
                patient.procedures = procedures;
                patient.diagnosis = diagnosis;
                patient.gender = gender;
                patient.isOlder65 = isOlder65;

                existingPatient = true;
                break;
            }
        }

        // if new patient, add to the list
        if (!existingPatient) {
            Patient patient = new Patient(firstName, lastName, dob, lastAdmitted, procedures, diagnosis, gender, isOlder65);
            patients.add(patient);
        }

        // display a dialog message based on whether it's a new or existing patient
        String message = existingPatient ? "Updating existing patient information" : "Patient saved successfully.";
        JOptionPane.showMessageDialog(frame, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    private void retrievePatients() {

        // create retrieveFrame frame with title and size
        JFrame retrieveFrame = new JFrame("Retrieve Patients");
        retrieveFrame.setSize(500, 300);

        // create panel for search inputs
        JPanel searchPanel = new JPanel();

        // create text fields for search criteria and a search button
        JTextField searchFirstNameField = new JTextField(10);
        JTextField searchLastNameField = new JTextField(10);
        JButton searchButton = new JButton("Search");
        searchPanel.add(new JLabel("First Name:"));
        searchPanel.add(searchFirstNameField);
        searchPanel.add(new JLabel("Last Name:"));
        searchPanel.add(searchLastNameField);
        searchPanel.add(searchButton);

        // create a list model to hold patient data
        DefaultListModel<Patient> listModel = new DefaultListModel<>();

        // populate the list model with existing patient data
        for (Patient patient : patients) {
            listModel.addElement(patient);
        }

        // create a JList with the populated list model and add it to a scroll pane
        JList<Patient> patientList = new JList<>(listModel);
        JScrollPane scrollPane = new JScrollPane(patientList);

        // attach an action listener to the search button
        searchButton.addActionListener(e -> {
            String searchFirstName = searchFirstNameField.getText().trim().toLowerCase();
            String searchLastName = searchLastNameField.getText().trim().toLowerCase();

            // filter patients based on search criteria
            List<Patient> filteredPatients = patients.stream()
                    .filter(p -> p.firstName.toLowerCase().contains(searchFirstName) && p.lastName.toLowerCase().contains(searchLastName))
                    .collect(Collectors.toList());

            // clear the list model and add the filtered patients
            listModel.clear();
            for (Patient patient : filteredPatients) {
                listModel.addElement(patient);
            }

            // if no patients were found, display error message
            if (filteredPatients.isEmpty()) {
                JOptionPane.showMessageDialog(retrieveFrame, "Patient not found", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // attach a double click mouse listener to the patient list
        patientList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    // retrieve the selected patient and display their details
                    Patient selectedPatient = patientList.getSelectedValue();
                    retrieveFrame.dispose();
                    displayPatient(selectedPatient);
                }
            }
        });

        // add components to the retrieve frame using BorderLayout
        retrieveFrame.add(searchPanel, BorderLayout.NORTH); // search inputs at the top
        retrieveFrame.add(scrollPane, BorderLayout.CENTER); // patient list in the center
        retrieveFrame.add(searchButton, BorderLayout.SOUTH); // add search button to bottom
        retrieveFrame.setVisible(true); // make the frame visible
    }

    // displayPatient method to display patient information for a selected patient.
    private void displayPatient(Patient selectedPatient) {

        // populate field with the information of the selected patient
        firstNameField.setText(selectedPatient.firstName);
        lastNameField.setText(selectedPatient.lastName);
        dobField.setText(selectedPatient.dob);
        lastAdmittedField.setText(selectedPatient.lastAdmitted);
        proceduresField.setText(selectedPatient.procedures);
        diagnosisField.setText(selectedPatient.diagnosis);
        genderComboBox.setSelectedItem(selectedPatient.gender);
        isOlder65CheckBox.setSelected(selectedPatient.isOlder65);
    }

    // define Patient class
    private class Patient {
        // declare instance variables for Patient class
        String firstName, lastName, dob, lastAdmitted, procedures, diagnosis, gender;
        boolean isOlder65;

        // constructor for Patient class to initialize all instance variables
        public Patient(String firstName,
                       String lastName,
                       String dob,
                       String lastAdmitted,
                       String procedures,
                       String diagnosis,
                       String gender,
                       boolean isOlder65) {
            // initialize the instance variables using the values passed as parameters
            this.firstName = firstName;
            this.lastName = lastName;
            this.dob = dob;
            this.lastAdmitted = lastAdmitted;
            this.procedures = procedures;
            this.diagnosis = diagnosis;
            this.gender = gender;
            this.isOlder65 = isOlder65;
        }

        // override the toString method to provide a custom string representation of the object
        @Override
        public String toString() {
            return firstName + " " + lastName;
        }
    }
}