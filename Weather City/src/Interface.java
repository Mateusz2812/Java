package Weather;

//import org.apache.log4j.Logger;

import javax.swing.*;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.Map;

public class Interface extends JFrame implements ActionListener {
    private TextField textField;
    private JPanel panel1,panel2;
    private JLabel mainLabel;
    private JTable table;
    private JLabel name;

    private static final Logger log = Logger.getRootLogger();

    public void createAndShowGUI(){
        setTitle("Pogoda");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        panel1= new JPanel();
        panel2= new JPanel();
        textField = new TextField();
        textField.setText("szukaj");

        setLayout(new GridLayout(0,2));
        panel1.setBackground(Color.yellow);
        panel2.setBackground(Color.green);

        add(panel1);
        add(panel2);

        panel1.setLayout(new BoxLayout(panel1,BoxLayout.Y_AXIS));

        mainLabel = new JLabel("POGODA");
        mainLabel.setFont(new Font(null, Font.CENTER_BASELINE,20));
        panel1.add(mainLabel);

        panel1.add(textField);
        textField.setPreferredSize(new Dimension(190, 25));
        textField.setMaximumSize(new Dimension(190, 25));
        textField.addActionListener(this);

        JList list = new JList(new String[]{"Kraków", "Warszawa", "Lublin", "Poznań", "Gdańsk", "Łódź","Bydgoszcz", "Rzeszów", "Opole", "Katowice", "Olsztyn", "Szczecin", "Białystok", "Wrocław", "Zielona Góra", "Kielce"});
        list.setPreferredSize(new Dimension(180, 440));
        list.setFont(new Font("monospaced", Font.PLAIN,18));
        panel1.add(list);
        list.addMouseListener(mouseListener(list));

        setSize(550,600);
        setResizable(false);

        ConfigureTable();
        name = new JLabel("");
        panel2.add(name);
        panel2.add(table);

    }

    private void ConfigureTable()
    {
        table = new JTable(6,2);
        TableColumn column = table.getColumnModel().getColumn(0);
        column.setPreferredWidth(175);
        column = table.getColumnModel().getColumn(1);
        column.setPreferredWidth(75);
        table.setValueAt("Temperatura [°C]", 0, 0);
        table.setValueAt("Odczuwalna temperatura [°C]", 1, 0);
        table.setValueAt("Ciśnienie [hpa]", 2, 0);
        table.setValueAt("Wilgotność [%]", 3, 0);
        table.setValueAt("Prędkość wiatru [m/s]", 4, 0);
        table.setValueAt("Zachmurzenie [%]", 5, 0);
    }

    public void DisplayWeather(Map map)
    {
        double number = Double.parseDouble(map.get("temp").toString()) - 273.15;
        number = Math.round(number*10.0)/10.0;
        table.setValueAt((number + ""), 0, 1);
        number = Double.parseDouble(map.get("feels_like").toString()) - 273.15;
        number = Math.round(number*10.0)/10.0;
        table.setValueAt((number + ""), 1, 1);
        table.setValueAt(map.get("pressure"), 2, 1);
        table.setValueAt(map.get("humidity"), 3, 1);
        table.setValueAt(map.get("wind_speed"), 4, 1);
        table.setValueAt(map.get("clouds"), 5, 1);
    }

    private MouseListener mouseListener(JList list)
    {
        return new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) {
                    String selectedItem = (String) list.getSelectedValue();
                    City city = new City(selectedItem);
                    try
                    {
                        city.Update();
                        log.info(("City.Update: city info updated correctly"));
                    }
                    catch (IOException ex)
                    {
                        log.error(("City.Update: loss internet connection"));
                        name.setText("Brak internetu");
                        return;
                    }
                    DisplayWeather(city.BasicMap());
                    name.setText(city.BasicMap().get("name").toString());
                }
            }
        };
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        TextField text = (TextField) e.getSource();

        City city = new City(text.getText());
        try
        {
            city.Update();
            log.info(("City.Update: city info updated correctly"));
        }
        catch (IOException ex)
        {
            log.error(("City.Update: Invalid city or loss internet connection"));
            name.setText("Miasto nie istnieje");
            return;
        }
        DisplayWeather(city.BasicMap());
        name.setText(city.BasicMap().get("name").toString());
    }
}
