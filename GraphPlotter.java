

import org.jfree.chart.ChartFactory; 
import org.jfree.chart.ChartPanel; 
import org.jfree.chart.JFreeChart; 
import org.jfree.chart.plot.PlotOrientation; 
import org.jfree.data.category.DefaultCategoryDataset; 
import org.jfree.data.general.DefaultPieDataset; 
 
import javax.swing.*; 
import javax.swing.border.EmptyBorder; 
import javax.swing.table.DefaultTableModel; 
import java.awt.*; 
 
public class GraphPlotter extends JFrame { 
    private JTable table; 
    private JComboBox<String> chartTypeCombo; 
    private DefaultTableModel model; 
 
    public GraphPlotter() { 
        setTitle("      Simple Graph Plotter"); 
        setSize(800, 600); 
        setLocationRelativeTo(null);  
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        setLayout(new BorderLayout(10, 10)); 
        getContentPane().setBackground(new Color(245, 245, 245)); // Light background 
 
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10)); 
        topPanel.setBackground(new Color(60, 90, 153)); // Dark blue 
        topPanel.setBorder(new EmptyBorder(10, 10, 10, 10)); 
 
        chartTypeCombo = new JComboBox<>(new String[]{"Line Chart", "Bar Chart", "Pie Chart"}); 
        chartTypeCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14)); 
        chartTypeCombo.setPreferredSize(new Dimension(140, 30)); 
 
        JButton setRowsButton = new JButton("Set Rows"); 
        JButton plotButton = new JButton("Plot Chart"); 
 
        Font buttonFont = new Font("Segoe UI", Font.BOLD, 13); 
        Color buttonColor = new Color(240, 240, 240); 
 
        setRowsButton.setFont(buttonFont); 
        plotButton.setFont(buttonFont); 
        setRowsButton.setBackground(buttonColor); 
        plotButton.setBackground(buttonColor); 
 
        topPanel.add(new JLabel("Select Chart:")); 
        topPanel.add(chartTypeCombo); 
        topPanel.add(setRowsButton); 
        topPanel.add(plotButton); 
 
        add(topPanel, BorderLayout.NORTH); 
 
        model = new DefaultTableModel(new String[]{"Category/Label", "Value"}, 0); 
        table = new JTable(model); 
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13)); 
        table.setRowHeight(22); 
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13)); 
        JScrollPane tableScroll = new JScrollPane(table); 
        tableScroll.setBorder(BorderFactory.createTitledBorder("Input Data")); 
        add(tableScroll, BorderLayout.CENTER); 
 
        setRowsButton.addActionListener(e -> { 
            String input = JOptionPane.showInputDialog(this, "How many data rows?"); 
            try { 
                int rows = Integer.parseInt(input); 
                if (rows <= 0) throw new NumberFormatException(); 
 
                model.setRowCount(0); // clear 
                for (int i = 0; i < rows; i++) { 
                    model.addRow(new Object[]{"", ""}); 
                } 
            } catch (NumberFormatException ex) { 
                JOptionPane.showMessageDialog(this, "Please enter a valid positive number."); 
            } 
        }); 
 
        plotButton.addActionListener(e -> plotChart()); 
    } 
 
    private void plotChart() { 
        String chartType = (String) chartTypeCombo.getSelectedItem(); 
        DefaultCategoryDataset categoryDataset = new DefaultCategoryDataset(); 
        DefaultPieDataset pieDataset = new DefaultPieDataset(); 
 
        for (int i = 0; i < table.getRowCount(); i++) { 
            Object labelObj = table.getValueAt(i, 0); 
            Object valueObj = table.getValueAt(i, 1); 
 
            if (labelObj != null && valueObj != null && 
                !labelObj.toString().isEmpty() && !valueObj.toString().isEmpty()) { 
 
                String label = labelObj.toString(); 
                double value; 
                try { 
                    value = Double.parseDouble(valueObj.toString()); 
                } catch (NumberFormatException ex) { 
                    JOptionPane.showMessageDialog(this, "Invalid number at row " + (i + 1)); 
                    return; 
                } 
 
                if ("Pie Chart".equals(chartType)) { 
                    pieDataset.setValue(label, value); 
                } else { 
                    categoryDataset.addValue(value, "Values", label); 
                } 
            } 
        } 
 
        JFreeChart chart = null; 
 
        switch (chartType) { 
            case "Line Chart": 
                chart = ChartFactory.createLineChart( 
                        "Line Chart", "Category", "Value", 
                        categoryDataset, PlotOrientation.VERTICAL, true, true, false); 
                break; 
            case "Bar Chart": 
                chart = ChartFactory.createBarChart( 
                        "Bar Chart", "Category", "Value", 
                        categoryDataset, PlotOrientation.VERTICAL, true, true, false); 
                break; 
            case "Pie Chart": 
                chart = ChartFactory.createPieChart( 
                        "Pie Chart", pieDataset, true, true, false); 
                break; 
        } 
 
        if (chart != null) { 
            showChart(chart); 
        } 
    } 
 
    private void showChart(JFreeChart chart) { 
        JFrame chartFrame = new JFrame("    Chart Viewer"); 
        chartFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
        chartFrame.setSize(700, 500); 
        chartFrame.setLocationRelativeTo(this); 
 
        ChartPanel chartPanel = new ChartPanel(chart); 
        chartPanel.setPopupMenu(null); // Disable right-click menu 
        chartPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); 
 
        chartFrame.setContentPane(chartPanel); 
        chartFrame.setVisible(true); 
    } 
 
    public static void main(String[] args) { 
        SwingUtilities.invokeLater(() -> { 
            GraphPlotter gp = new GraphPlotter(); 
            gp.setVisible(true); 
        }); 
    } 
} 
