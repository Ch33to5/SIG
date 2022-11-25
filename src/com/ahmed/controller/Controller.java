package com.ahmed.controller;

import com.ahmed.model.Invoice;
import com.ahmed.model.InvoicesTableModel;
import com.ahmed.model.Item;
import com.ahmed.model.ItemsTableModel;
import com.ahmed.view.InvoD;
import com.ahmed.view.InvoiceFrame;
import com.ahmed.view.ItemsD;
import javax.swing.JFileChooser;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class Controller implements ActionListener, ListSelectionListener {
    
    
    private InvoiceFrame theFrame;
    private InvoD invoD;
    private ItemsD itemsD;
    
    
    
    public Controller(InvoiceFrame theFrame) {
        this.theFrame = theFrame;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        String actCmd = e.getActionCommand();
        System.out.println("Action: "+actCmd);
        switch (actCmd){
            case "Load File":
                loadFile();
                break;
            case "Save File":
                saveFile();
                break;
            case "New Invoice":
                newInvoice();
                break;
            case "Delete Invoice":
                deleteInvoice();
                break;
            case "New Item":
                newItem();
                break;
            case "Delete Item":
                delItem();
                break;
            case "newInvoiceCancel":
                newInvoiceCancel();
                break;
            case "newInvoiceOK":
                newInvoiceOK();
                break;
            case "newItemOK":
                newItemOK();
                break;
            case "newItemCancel":
                newItemCancel();
                break;
                    
        }
    }

    
    
    @Override
    public void valueChanged(ListSelectionEvent e) {
        int selectedIndex = theFrame.getInvoiceTable().getSelectedRow();
        if (selectedIndex != -1){
            Invoice currentInvoice = theFrame.getInvoices().get(selectedIndex);
            theFrame.getInvoiceNumLbl().setText(""+currentInvoice.getNum());
            theFrame.getInvoiceDateLbl().setText(currentInvoice.getDate());
            theFrame.getCustNameLbl().setText(currentInvoice.getCust());
            theFrame.getInvoiceTotalLbl().setText(""+currentInvoice.getInvoiceTotal());
            ItemsTableModel itemsTableModel = new ItemsTableModel(currentInvoice.getItems());
            theFrame.getItemsTable().setModel(itemsTableModel);
            itemsTableModel.fireTableDataChanged();
        
        }   
    }
    
        
    
    private void loadFile() {
                JFileChooser fc = new JFileChooser();
        try {
             
            int result = fc.showOpenDialog(theFrame);
            if (result == JFileChooser.APPROVE_OPTION) {
                File file1 = fc.getSelectedFile();
                Path file1Path = Paths.get(file1.getAbsolutePath());
                List<String> headerLines = Files.readAllLines(file1Path);
              
                ArrayList<Invoice> invoicesArray = new ArrayList<>();
                
                for (String headerLine : headerLines) {
                    try {
                        String[] headerParts = headerLine.split(",");
                        int invoiceNum = Integer.parseInt(headerParts[0]);
                        String invoiceDate = headerParts[1];
                        String customerName = headerParts[2];

                        Invoice invoice = new Invoice(invoiceNum, invoiceDate, customerName);
                        invoicesArray.add(invoice);  
                    }
                    catch (Exception ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(theFrame, "Wrong Invoice Data format", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            
                result = fc.showOpenDialog(theFrame);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File file2 = fc.getSelectedFile();
                    Path file2Path = Paths.get(file2.getAbsolutePath());
                    List<String> itemForItems = Files.readAllLines(file2Path);
                    
                   
                    for (String itemByItem : itemForItems) {
                        try {
                            String lineParts[] = itemByItem.split(",");
                            int invoiceNum = Integer.parseInt(lineParts[0]);
                            String itemName = lineParts[1];
                            double itemPrice = Double.parseDouble(lineParts[2]);
                            int count = Integer.parseInt(lineParts[3]);
                            Invoice inv = null;
                            for (Invoice invoice : invoicesArray) {
                                if (invoice.getNum() == invoiceNum) {
                                    inv = invoice;
                                    break;
                                }
                            }

                            Item item = new Item(itemName, itemPrice, count, inv);
                            inv.getItems().add(item);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(theFrame, "Wrong date format", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
                theFrame.setInvoices(invoicesArray);
                InvoicesTableModel invoicesTableModel = new InvoicesTableModel(invoicesArray);
                theFrame.setInvoicesTableModel(invoicesTableModel);
                theFrame.getInvoiceTable().setModel(invoicesTableModel);
                theFrame.getInvoicesTableModel().fireTableDataChanged();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(theFrame, "Folder/File path is not found", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    
    
    private void saveFile() {
        ArrayList<Invoice> invoices = theFrame.getInvoices();
        String headers = "";
        String lines = "";
        for (Invoice invoice : invoices) {
            String invCSV = invoice.getSaveFile();
            headers += invCSV;
            headers += "\n";

            for (Item line : invoice.getItems()) {
                String lineCSV = line.getSaveFile();
                lines += lineCSV;
                lines += "\n";
            }
        }

        try {
            JFileChooser fc = new JFileChooser();
            int result = fc.showSaveDialog(theFrame);
            if (result == JFileChooser.APPROVE_OPTION) {
                File headerFile = fc.getSelectedFile();
                FileWriter hfw = new FileWriter(headerFile);
                hfw.write(headers);
                hfw.flush();
                hfw.close();
                result = fc.showSaveDialog(theFrame);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File lineFile = fc.getSelectedFile();
                    FileWriter lfw = new FileWriter(lineFile);
                    lfw.write(lines);
                    lfw.flush();
                    lfw.close();
                }
            }
        } catch (Exception a) {

        }
    }

    
    
    private void newInvoice() {
        invoD = new InvoD(theFrame);
        invoD.setVisible(true);
    }

    
    
    private void deleteInvoice() {
        int selectedRow = theFrame.getInvoiceTable().getSelectedRow();
       
        if (selectedRow != -1) {
            theFrame.getInvoices().remove(selectedRow); 
            theFrame.getInvoicesTableModel().fireTableDataChanged();
            
        }  
    }

    
    
    private void newItem() {
        itemsD = new ItemsD(theFrame);
        itemsD.setVisible(true);
    }

    
    
    private void delItem() {
        int selectedInv = theFrame.getInvoiceTable().getSelectedRow();
        int selectedRow = theFrame.getItemsTable().getSelectedRow();

        if (selectedInv != -1 && selectedRow != -1) {
            Invoice invoice = theFrame.getInvoices().get(selectedInv);
            invoice.getItems().remove(selectedRow);
            ItemsTableModel itemsTableModel = new ItemsTableModel(invoice.getItems());
            theFrame.getItemsTable().setModel(itemsTableModel);
            itemsTableModel.fireTableDataChanged();
            theFrame.getInvoicesTableModel().fireTableDataChanged();
        }
    }

    
    
    private void newInvoiceCancel() {
        invoD.setVisible(false);
        invoD.dispose();
        invoD = null;
    }

    
    
    private void newInvoiceOK() {
        String date = invoD.getInvDateField().getText();
        String customer = invoD.getCustNameField().getText();
        int num = theFrame.getNextInvoiceNum();
        try {
            String[] dateParts = date.split("-");
            if (dateParts.length < 3) {
                JOptionPane.showMessageDialog(theFrame, "Wrong date format", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                int dd = Integer.parseInt(dateParts[0]); 
                int mm = Integer.parseInt(dateParts[1]);
                int yy = Integer.parseInt(dateParts[2]);
                if (dd > 31 || mm > 12) {
                    JOptionPane.showMessageDialog(theFrame, "Wrong date format", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    Invoice invoice = new Invoice(num, date, customer);
                    theFrame.getInvoices().add(invoice);
                    theFrame.getInvoicesTableModel().fireTableDataChanged();
                    invoD.setVisible(false);
                    invoD.dispose();
                    invoD = null;
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(theFrame, "Wrong date format", "Error", JOptionPane.ERROR_MESSAGE);
        }

    }

    
    
    private void newItemOK() {
        String itemStr = itemsD.getItemNameField().getText();
        String countStr = itemsD.getItemCountField().getText();
        String priceStr = itemsD.getItemPriceField().getText();
        int count = Integer.parseInt(countStr);
        double price = Double.parseDouble(priceStr);
        int selectedInvoie = theFrame.getInvoiceTable().getSelectedRow();
        if (selectedInvoie != -1) {
            Invoice invoice = theFrame.getInvoices().get(selectedInvoie);
            Item item = new Item(itemStr, price, count, invoice);
            invoice.getItems().add(item);
            ItemsTableModel itemsTableModel = (ItemsTableModel) theFrame.getItemsTable().getModel();
            itemsTableModel.fireTableDataChanged();
            theFrame.getInvoicesTableModel().fireTableDataChanged();
        }
    }

    
    
    private void newItemCancel() {
        itemsD.setVisible(false);
        itemsD.dispose();
        itemsD = null;
        
    }

}
