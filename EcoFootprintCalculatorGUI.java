import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EcoFootprintCalculatorGUI {

    // Constants based on average values
    private static final double CO2_PER_MILE = 404;
    private static final double CO2_PER_KWH = 0.92;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Eco Footprint Calculator");
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel inputPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        JPanel resultPanel = new JPanel(new BorderLayout(10, 10));

        // Create GUI components
        JLabel milesLabel = new JLabel("Miles driven per week: ");
        JTextField milesField = new JTextField();

        JLabel kWhLabel = new JLabel("kWh consumed per week: ");
        JTextField kWhField = new JTextField();

        JLabel milesUnitLabel = new JLabel("Select units:");
        JLabel kWhUnitLabel = new JLabel("Select units:");

        JComboBox<String> milesUnitCombo = new JComboBox<>(new String[] { "Miles", "Kilometers" });
        JComboBox<String> kWhUnitCombo = new JComboBox<>(new String[] { "kWh", "MWh" });

        JLabel kWhInfoLabel = new JLabel(
                "<html>Your kWh consumption can typically be found on your monthly electricity bill.</html>");

        JButton calculateButton = new JButton("Calculate CO2 Emissions");

        // Add components to the input panel
        inputPanel.add(milesLabel);
        inputPanel.add(milesField);
        inputPanel.add(milesUnitLabel);
        inputPanel.add(milesUnitCombo);
        inputPanel.add(kWhLabel);
        inputPanel.add(kWhField);
        inputPanel.add(kWhUnitLabel);
        inputPanel.add(kWhUnitCombo);
        inputPanel.add(kWhInfoLabel);
        inputPanel.add(new JLabel());

        // Create a panel for result presentation
        JPanel resultContentPanel = new JPanel();
        resultContentPanel.setLayout(new BoxLayout(resultContentPanel, BoxLayout.Y_AXIS));
        JLabel totalEmissionsLabel = new JLabel();
        JLabel drivingEmissionsLabel = new JLabel();
        JLabel electricityEmissionsLabel = new JLabel();

        // Add padding around result labels
        int labelPadding = 10;
        totalEmissionsLabel.setBorder(new EmptyBorder(labelPadding, labelPadding, labelPadding, labelPadding));
        drivingEmissionsLabel.setBorder(new EmptyBorder(labelPadding, labelPadding, labelPadding, labelPadding));
        electricityEmissionsLabel.setBorder(new EmptyBorder(labelPadding, labelPadding, labelPadding, labelPadding));

        resultContentPanel.add(totalEmissionsLabel);
        resultContentPanel.add(drivingEmissionsLabel);
        resultContentPanel.add(electricityEmissionsLabel);

        // Wrap the result panel in another panel with margins
        JPanel resultContainerPanel = new JPanel(new BorderLayout());
        int marginSize = 20;
        resultContainerPanel.setBorder(new EmptyBorder(marginSize, marginSize, marginSize, marginSize));
        resultContainerPanel.add(resultContentPanel);

        resultPanel.add(resultContainerPanel, BorderLayout.CENTER);

        // Add components to the main panel
        mainPanel.add(inputPanel, BorderLayout.NORTH);
        mainPanel.add(calculateButton, BorderLayout.CENTER);
        mainPanel.add(resultPanel, BorderLayout.SOUTH);

        frame.add(mainPanel, BorderLayout.CENTER);

        // Add action listener to the calculate button
        calculateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    double milesDriven = Double.parseDouble(milesField.getText());
                    double kWhConsumed = Double.parseDouble(kWhField.getText());
                    String milesUnit = (String) milesUnitCombo.getSelectedItem();
                    String kWhUnit = (String) kWhUnitCombo.getSelectedItem();

                    // Convert miles to kilometers if needed
                    if ("Miles".equals(milesUnit)) {
                        milesDriven *= 1.60934; // 1 mile = 1.60934 kilometers
                    }

                    // Convert kWh to MWh if needed
                    if ("kWh".equals(kWhUnit)) {
                        kWhConsumed /= 1000; // 1 kWh = 0.001 MWh
                    }

                    double co2FromDriving = milesDriven * CO2_PER_MILE;
                    double co2FromElectricity = kWhConsumed * CO2_PER_KWH;
                    double totalEmissions = (co2FromDriving / 1000) + co2FromElectricity;

                    // Update the result labels with formatted text
                    totalEmissionsLabel.setText("Total CO2 emissions: " + formatEmissions(totalEmissions));
                    drivingEmissionsLabel.setText("CO2 emissions from driving: " + formatEmissions(co2FromDriving));
                    electricityEmissionsLabel
                            .setText("CO2 emissions from electricity: " + formatEmissions(co2FromElectricity));

                    // Show tips based on the results
                    showTips(co2FromDriving, co2FromElectricity);

                    // Show detailed information dialog
                    showInformationDialog(frame);

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Please enter valid numbers.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Center the frame on the screen
        frame.setLocationRelativeTo(null);

        frame.setVisible(true);
    }

    // Format emissions with two decimal places
    private static String formatEmissions(double emissions) {
        return String.format("%.2f kilograms", emissions);
    }

    // Show tips based on CO2 emissions
    private static void showTips(double co2FromDriving, double co2FromElectricity) {
        StringBuilder tips = new StringBuilder("<html>");

        if (co2FromDriving > 2000) { // Assuming 2000 grams as the threshold for driving
            tips.append("<b>Transportation Tips:</b><br>");
            tips.append("- Drive efficiently: Gentle acceleration and braking.<br>");
            tips.append("- Consider carpooling or public transportation.<br>");
            tips.append("- For short distances, consider biking or walking.<br>");
            tips.append("- Consider hybrid or electric vehicles.<br><br>");
        }

        if (co2FromElectricity > 15) { // Assuming 15 kilograms as the threshold for electricity consumption
            tips.append("<b>Home Energy Consumption Tips:</b><br>");
            tips.append("- Use energy-efficient appliances.<br>");
            tips.append("- Replace bulbs with LED versions.<br>");
            tips.append("- Insulate and seal your home properly.<br>");
            tips.append("- Unplug devices when not in use.<br>");
            tips.append("- Consider using renewable energy sources.<br>");
        }

        tips.append("</html>");

        JOptionPane.showMessageDialog(null, tips.toString(), "Tips for Reducing Carbon Footprint",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private static void showInformationDialog(JFrame frame) {
        String info = "<html><body style='width: 300px;'>";
        info += "<h2>Why Reduce Your Carbon Footprint?</h2>";
        info += "<p>Climate change poses a significant threat to the environment and humanity, ";
        info += "largely driven by the global increase in greenhouse gas emissions from human activities.</p>";
        info += "<p><b>Transportation:</b> Reducing miles driven, especially in gas-powered vehicles, ";
        info += "can significantly lower your carbon footprint. Consider alternatives like public transport, ";
        info += "biking, or electric vehicles.</p>";
        info += "<p><b>Electricity Consumption:</b> Electricity production, particularly from fossil fuels, ";
        info += "is a major contributor to climate change. Using energy-efficient appliances, ";
        info += "conserving energy, and utilizing renewable energy can reduce your impact.</p>";
        info += "<p><b>Diet:</b> The production of meat and other animal products generates significant ";
        info += "greenhouse gas emissions. A plant-based diet is generally more eco-friendly.</p>";
        info += "<p><b>Waste:</b> Landfills are a major source of methane, a potent greenhouse gas. ";
        info += "Recycling, composting, and reducing waste are effective strategies to reduce your footprint.</p>";
        info += "</body></html>";

        // Display the message
        JOptionPane.showMessageDialog(frame, info, "Understanding Your Carbon Footprint",
                JOptionPane.INFORMATION_MESSAGE);
    }
}
