package guibaseball.gui;

import guibaseball.actionlisteners.SeekDirectionActionListener;
import guibaseball.actionlisteners.YearInputListener;
import guibaseball.data.DataManager;
import guibaseball.resource.Team;
import guibaseball.resource.WorldSeries;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class YearPanel extends SeekablePanel {

    private JTextField yearInput;
    private JLabel resultLabel;
    private int minYear, maxYear;

    /**
     * Constructor
     * Initializes a JPanel featuring a seeker. Will show which team won a given year
     */
    public YearPanel() {
        List<Integer> years = DataManager.getInstance().getYears();
        minYear = years.get(0);
        maxYear = years.get(years.size() - 1);

        GridBagLayout gridBagLayout = new GridBagLayout();
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        setLayout(gridBagLayout);

        SeekButton backButton = new SeekButton("<<", -1);
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1;
        gridBagConstraints.weighty = 0;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagLayout.setConstraints(backButton, gridBagConstraints );
        add(backButton);

        yearInput = new JTextField(4);
        gridBagConstraints.gridx = 8;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1;
        gridBagConstraints.weighty = 0;
        gridBagConstraints.anchor = GridBagConstraints.NORTH;
        gridBagLayout.setConstraints( yearInput, gridBagConstraints );
        add(yearInput);

        SeekButton forwardButton = new SeekButton(">>", 1);
        gridBagConstraints.gridx = 12;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1;
        gridBagConstraints.weighty = 0;
        gridBagConstraints.anchor = GridBagConstraints.EAST;
        gridBagLayout.setConstraints(forwardButton, gridBagConstraints);
        add(forwardButton);

        resultLabel = new JLabel("");
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 7;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1;
        gridBagConstraints.weighty = 1;
        gridBagConstraints.anchor = GridBagConstraints.CENTER;
        gridBagLayout.setConstraints(resultLabel, gridBagConstraints);
        add(resultLabel);

        SeekDirectionActionListener seekDirectionActionListener = new SeekDirectionActionListener();
        forwardButton.addActionListener(seekDirectionActionListener);
        backButton.addActionListener(seekDirectionActionListener);

        this.setFilter(minYear, true);
        yearInput.getDocument().addDocumentListener(new YearInputListener(yearInput));
    }

    /**
     * Get the current filter being used by the seeker
     * @return The filter being used by the seeker
     */
    @Override
    public int getFilter() {
        return Integer.parseInt(yearInput.getText());
    }

    /**
     * Update the filter being used by the seeker
     *
     * @param filter The new filter to be used
     * @param updateInput Whether or not the panel should update the text field
     */
    @Override
    public void setFilter(int filter, boolean updateInput) {
        if (filter < minYear) {
            filter = maxYear;
            this.setFilter(filter, true);
        }
        else if (filter > maxYear) {
            filter = minYear;
            this.setFilter(filter, true);
        }

        if (updateInput)
            yearInput.setText(Integer.toString(filter));

        updateResult();
    }

    /**
     * Updates the result text displayed by the panel
     * Shows which team won the current selected year's World Series
     */
    private void updateResult() {
        int year = getFilter();
        WorldSeries win = DataManager.getInstance().getByYear(year);
        if (win != null) {
            Team team = win.getTeam();
            String teamName = team != null ? team.getTeamName() : "N/A";

            resultLabel.setText(year + " Winner: " + teamName);
        }
    }

}
