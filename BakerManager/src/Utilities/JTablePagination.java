/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utilities;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.RowFilter.Entry;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

public class JTablePagination extends JPanel {

    public JTable table;
    RadioButtonUI ui = new RadioButtonUI();
    int pageSize = 5;
    public TableRowSorter sorter = new TableRowSorter();
    Box box = Box.createHorizontalBox();

    public JTablePagination(int numberPages) {
        super(new BorderLayout());
        table = new JTable() {
            public Component prepareRenderer(TableCellRenderer tcr, int row,
                    int column) {
                Component c = super.prepareRenderer(tcr, row, column);
                if (isRowSelected(row)) {
                    c.setForeground(getSelectionForeground());
                    c.setBackground(getSelectionBackground());
                } else {
                    c.setForeground(getForeground());
                    c.setBackground((row % 2 == 0) ? Color.lightGray
                            : getBackground());
                }
                return c;
            }
        };
        table.setIntercellSpacing(new Dimension());
        table.setShowGrid(false);
        table.setRowSorter(sorter);
        showPages(numberPages, 1);

        add(new JScrollPane(table));
        add(box, BorderLayout.SOUTH);
    }

    public void establecerModelo(TableModel model) {
        try {
            sorter.setModel(model);
            table.setModel(model);
            showPages(100, 1);
        } catch (Exception e) {
            
        }
    }

    private void showPages(final int itemsPerPage, final int currentPageIndex) {
        sorter.setRowFilter(filter(itemsPerPage, currentPageIndex - 1));
        ArrayList<JRadioButton> l = new ArrayList();

        int startPageIndex = currentPageIndex - pageSize;
        if (startPageIndex <= 0) {
            startPageIndex = 1;
        }
        int maxPageIndex = (table.getModel().getRowCount() / itemsPerPage) + 1;
        int endPageIndex = currentPageIndex + pageSize - 1;
        if (endPageIndex > maxPageIndex) {
            endPageIndex = maxPageIndex;
        }

        if (currentPageIndex > 1) {
            l
                    .add(createRadioButtons(itemsPerPage, currentPageIndex - 1,
                    "Prev "));
        }
        for (int i = startPageIndex; i <= endPageIndex; i++) {
            l.add(createLinks(itemsPerPage, currentPageIndex, i - 1));
        }
        if (currentPageIndex < maxPageIndex) {
            l
                    .add(createRadioButtons(itemsPerPage, currentPageIndex + 1,
                    " Next"));
        }

        box.removeAll();
        ButtonGroup bg = new ButtonGroup();
        box.add(Box.createHorizontalGlue());
        for (JRadioButton r : l) {
            box.add(r);
            bg.add(r);
        }
        box.add(Box.createHorizontalGlue());
        box.revalidate();
        box.repaint();
        l.clear();
    }

    private JRadioButton createLinks(final int itemsPerPage, final int current,
            final int target) {
        JRadioButton radio = new JRadioButton(" " + (target + 1)) {
            protected void fireStateChanged() {
                ButtonModel model = getModel();
                if (!model.isEnabled()) {
                    setForeground(Color.GRAY);
                } else if (model.isPressed() && model.isArmed()) {
                    setForeground(Color.GREEN);
                } else if (model.isSelected()) {
                    setForeground(Color.RED);
                }
                super.fireStateChanged();
            }
        };
        radio.setForeground(Color.BLUE);
        radio.setUI(ui);
        if (target + 1 == current) {
            radio.setSelected(true);
        }
        radio.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showPages(itemsPerPage, target + 1);
            }
        });
        return radio;
    }

    private JRadioButton createRadioButtons(final int itemsPerPage,
            final int target, String title) {
        JRadioButton radio = new JRadioButton(title);
        radio.setForeground(Color.BLUE);
        radio.setUI(ui);
        radio.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showPages(itemsPerPage, target);
            }
        });
        return radio;
    }

    private RowFilter filter(final int itemsPerPage,
            final int target) {
        return new RowFilter() {
            public boolean include(
                    Entry entry) {
                int ei = Integer.valueOf(String.valueOf(entry.getIdentifier().toString()));
                return (target * itemsPerPage <= ei && ei < target
                        * itemsPerPage + itemsPerPage);
            }
        };
    }
}
