/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.app.ui.m3;

import com.app.model.m3.M3UpgradModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import m3.M3UpdObjModel;
import org.oxbow.swingbits.table.filter.TableRowFilterSupport;
import ui.panel.model.PanelButton;
import ui.tools.AutoComboBox;
import ui.tools.UITools;

/**
 *
 * @author Jeremy.CHAUT
 */
public class UIM3Analyze extends JPanel implements ActionListener {

    private final JTable table;
    private final MyTableModel tableModel;
    private final M3UpgradModel model;
    private final PanelButton pbutton = new PanelButton(PanelButton.BT_CUSTOM);

    public UIM3Analyze(M3UpgradModel m) {
        super(new BorderLayout());
        model = m;
        add(new UIM3Summary(model), BorderLayout.NORTH);

        tableModel = new MyTableModel();
        table = new JTable(tableModel);
        TableRowFilterSupport.forTable(table).searchable(true).apply();
        table.setPreferredScrollableViewportSize(new Dimension(1000, 70));
        table.setFillsViewportHeight(true);
        JScrollPane scrollPane = new JScrollPane(table);
        table.setRowHeight(20);
        table.setDefaultRenderer(Object.class, new MyTableCellRenderer());
        setComboboxColumn(table.getColumnModel().getColumn(table.getColumnCount() - 1), model.getM3UserModel().getListUserSelect().toArray());
        add(scrollPane, BorderLayout.CENTER);

        pbutton.addButton(pbutton.bExcel, "excel");
        pbutton.addButton(pbutton.bSave, "save");
        pbutton.bExcel.addActionListener(this);
        pbutton.bSave.addActionListener(this);
        add(pbutton, BorderLayout.SOUTH);
    }

    private void setComboboxColumn(TableColumn comboboxColumn, Object[] data) {
        AutoComboBox c = new AutoComboBox(new ArrayList(Arrays.asList(data)));
        c.setDataList(new ArrayList(Arrays.asList(data)));
        c.setMaximumRowCount(3);
        comboboxColumn.setCellEditor(new MyCellEditor(c));
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        comboboxColumn.setCellRenderer(renderer);
    }

    class MyTableCellRenderer extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            MyTableModel model = (MyTableModel) table.getModel();
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            model.rowColor(row);
            c.setBackground(model.getRowColor(row));
            c.setForeground(Color.black);
            return c;
        }
    }

    class MyCellEditor extends DefaultCellEditor {

        Map<String, ArrayList<String>> choicesMap;

        public MyCellEditor(JComboBox combo) {
            super(combo);
        }

        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            JComboBox combo = (JComboBox) super.getTableCellEditorComponent(table, value, isSelected, row, column);
            return combo;
        }
    }

    class MyTableModel extends AbstractTableModel {

        private String[] columnNames = null;//{"Type de classe", "Nom Classe", "Chemin", "Type", "Niveau", "Classe mère", "Source", "Champ", "A migrer", "Développpeur"};
        private Object[][] data = null;//{{"Nom", "Libellé", "Type", new Integer(20), new Boolean(true), new Boolean(true), new Boolean(true), new Boolean(false), new Boolean(true), "", "", new Boolean(false)}};
//public final Object[] initValues = {"Nom", "Libellé", "Type", new Integer(20), new Boolean(true), new Boolean(true), new Boolean(true), new Boolean(false), new Boolean(true), "", "", new Boolean(false)};
        List<Color> rowColors;

        public MyTableModel() {
            buildColumn();
            data = model.getData();
            loadColor();
        }

        private void buildColumn() {
            columnNames = new String[M3UpdObjModel.header.length];
            for (int i = 0; i < M3UpdObjModel.header.length; i++) {
                columnNames[i] = i18n.Language.getLabel(M3UpdObjModel.header[i]);
            }
        }

        private void loadColor() {
            rowColors = new ArrayList<>(model.getData().length);
            for (int i = 0; i < data.length; i++) {
                rowColor(i);
            }
        }

        private void rowColor(int i) {
            setRowColor(i, Color.WHITE);
            if (table != null) {
                if (!Boolean.parseBoolean(table.getValueAt(i, 8).toString())) {
                    setRowColor(i, Color.ORANGE);
                } else if (table.getValueAt(i, 0).toString().isEmpty() || !table.getValueAt(i, 3).toString().isEmpty()) {
                    setRowColor(i, Color.RED);
                }
            } else {
                if (!Boolean.parseBoolean(data[i][8].toString())) {
                    setRowColor(i, Color.ORANGE);
                } else if (data[i][0].toString().isEmpty() || !data[i][3].toString().isEmpty()) {
                    setRowColor(i, Color.RED);
                }
            }
        }

        public void setRowColor(int row, Color c) {
            if (row >= rowColors.size()) {
                rowColors.add(c);
            } else {
                rowColors.set(row, c);
            }
            fireTableRowsUpdated(row, row);
        }

        public Color getRowColor(int row) {
            return rowColors.get(row);
        }

//Méthode permettant de retirer une ligne du tableau
        public void removeRow(int position) {
            int indice = 0, indice2 = 0;
            int nbRow = this.getRowCount() - 1;
            int nbCol = this.getColumnCount();
            Object temp[][] = new Object[nbRow][nbCol];

            for (Object[] value : this.data) {
                if (indice != position) {
                    temp[indice2++] = value;
                }
                System.out.println("Indice = " + indice);
                indice++;
            }
            this.data = temp;
            temp = null;
            this.fireTableDataChanged();
        }

//Permet d'ajouter une ligne dans le tableau
        public void addRow(Object[] d) {
            int indice = 0, nbRow = this.getRowCount(), nbCol = this.getColumnCount();
            Object temp[][] = this.data;
            this.data = new Object[nbRow + 1][nbCol];
            for (Object[] value : temp) {
                this.data[indice++] = value;
            }
            this.data[indice] = d;
            temp = null;

            this.fireTableDataChanged();
        }

        public int getColumnCount() {
            return columnNames.length;
        }

        public int getRowCount() {
            return data.length;
        }

        public String getColumnName(int col) {
            return columnNames[col];
        }

        public Object getValueAt(int row, int col) {
            return data[row][col];
        }

        public Class getColumnClass(int c) {
            return getValueAt(0, c).getClass();
        }

        public boolean isCellEditable(int row, int col) {
            if (col < 9) {
                return false;
            } else {
                return true;
            }
        }

        public Object[][] getData() {
            return data;
        }

        public void setValueAt(Object value, int row, int col) {
            Object temp = data[row][col];
            data[row][col] = value;
//            setRowColor(row, Color.BLACK);
//            if (col != 0 && data[row][0].toString().isEmpty()) {
//                setRowColor(row, Color.RED);
//            }

            fireTableCellUpdated(row, col);
            System.out.println("New value of data:");
//printDebugData();
        }

        private void printDebugData() {
            int numRows = getRowCount();
            int numCols = getColumnCount();
            for (int i = 0; i < numRows; i++) {
                System.out.print("    row " + i + ":");
                for (int j = 0; j < numCols; j++) {
                    System.out.print("  " + data[i][j]);
                }
                System.out.println();
            }
            System.out.println("--------------------------");
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String act = e.getActionCommand();
        switch (act) {
            case "excel":
                UIExport exp = new UIExport(model);
                UITools.createDialog(null, "Export", exp);
                break;
            case "save":
                File[] f = zio.ToolsFile.fileChoice(this, "", new ArrayList(Arrays.asList(".m3up")));
                if (f != null) {
                    model.save(f[0].getAbsolutePath());
                }
                break;
        }
    }
}
