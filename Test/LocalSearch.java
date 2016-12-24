/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

/**
 *
 * @author taufic
 */
public class LocalSearch {
    private JFrame main;
    private JLabel header;
    private JLabel header1;
    private JPanel control;
    private GridBagConstraints layout;
    private JFrame tabel;
    private JRadioButton hill;
    private JRadioButton simuAnne;
    private JRadioButton genetic;
    private final int ROW = 11;
    private final int COLUMN = 5;
    /* Matriks yang menyimpan detail jadwal */
    private String[][] jadwal = new String[ROW][COLUMN];
    private String[][] ruanganJadwal = new String[ROW][COLUMN];
    private int[][] nMatkul = new int[ROW][COLUMN];
    //private JTable table;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        LocalSearch frame = new LocalSearch();
        frame.PrepareFrame();
        frame.RadioButton();
        frame.StartButton();
    }
   
    public void PrepareFrame() {
        main = new JFrame("TUGAS AI PERTAMA");
        main.setSize(600, 600);
        main.setLayout(new GridLayout(3,1));
        main.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                System.exit(0);
            } 
        });
        
        layout = new GridBagConstraints();
        control =  new JPanel();
        control.setLayout(new GridBagLayout());
        
        
        
        main.setVisible(true);
        centerWindow(main);
        
        tabel = new JFrame("TUGAS AI PERTAMA");
        tabel.setSize(500, 600);
        tabel.setLayout(new FlowLayout());
        tabel.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                System.exit(0);
            } 
        });
       
        tabel.setVisible(false);
        centerWindow(tabel);
    }
    
    public static void centerWindow(final Window frame) {
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int eks = (int) ((dimension.getWidth() - frame.getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - frame.getHeight()) / 2);
        frame.setLocation(eks, y);
    }
    
    public void RadioButton() {
        header = new JLabel("", JLabel.CENTER);
        header.setSize(600,300);
        header.setFont(new Font("System", Font.PLAIN, 20));
        header.setText("Silahkan Pilih Algoritma yang akan Digunakan"); 
        main.add(header);
        
        //set the radio button position
        layout.gridx = 0;
        layout.gridy = GridBagConstraints.RELATIVE;
        layout.weighty = 1;
        layout.anchor = GridBagConstraints.WEST;
        
        hill = new JRadioButton("Hill", true);
        simuAnne = new JRadioButton("Simulated Annealing");
        genetic = new JRadioButton("Genetic");
        
        hill.setMnemonic(KeyEvent.VK_H);
        simuAnne.setMnemonic(KeyEvent.VK_S);
        genetic.setMnemonic(KeyEvent.VK_G);
        
        //Group the radio button
        ButtonGroup group = new ButtonGroup();
        group.add(hill);
        group.add(simuAnne);
        group.add(genetic);
        
        control.add(hill, layout);
        control.add(simuAnne, layout);
        control.add(genetic, layout);
        main.add(control);
        main.setVisible(true);
    }
    
    public void StartButton() {
        JButton start = new JButton("Start");
        
        //set button position
        layout.gridx = 0;
        layout.gridy = GridBagConstraints.RELATIVE;
        layout.weighty = 1;
        layout.anchor = GridBagConstraints.SOUTH;
        control.add(start, layout);
        main.add(control);
        main.setVisible(true);
        start.addActionListener((ActionEvent e) -> {
            TableTransferable table = new TableTransferable();
            //TableTransferable tableJadwal = new TableTransferable();
            int jumlahTabrak = 0;
            NamaKegiatan x = new NamaKegiatan();
            float[] efektifitas = new float[x.getRuangan().size()];
            ArrayList<String> ruangannya = new ArrayList<String>();
            if (hill.isSelected()) {
                Hill hill = new Hill();
                //System.out.println("ada gua gk");
                hill.randomize(jadwal, nMatkul, ruanganJadwal);
                hill.hillClimbing(jadwal, nMatkul, ruanganJadwal);
                tabel.add(table.createTable(jadwal));
                tabel.add(table.createTable(ruanganJadwal));
                jumlahTabrak = hill.getJumlahTabrakan();
                efektifitas = hill.countPresentaseKelas(ruanganJadwal);
                ruangannya = hill.getRuangan();
            } else if (simuAnne.isSelected()) {
                SimulatedAnnealing s = new SimulatedAnnealing();
		s.SA();
                tabel.add(table.createTable(s.getSolusi()));
                tabel.add(table.createTable(s.getSolusiRuangan()));
                jumlahTabrak = s.getCountTabrakan();
                efektifitas = s.countPresentaseKelas(s.getSolusiRuangan());
                ruangannya = s.getRuangan();
            } else {
                Genetic coba = new Genetic();
                coba.runGenetic();
                tabel.add(table.createTable(coba.jadwal));
                tabel.add(table.createTable(coba.getRuangan()));
                efektifitas = coba.countPresentaseKelas(coba.getRuangan());
                jumlahTabrak = coba.getCountJumlahTabrakan();
                ruangannya = x.getRuangan();
            }
            header1 = new JLabel("", JLabel.CENTER);
            header1.setSize(350,100);
            header1.setFont(new Font("System", Font.PLAIN, 14));
            JLabel efektif = new JLabel("", JLabel.CENTER);
            efektif.setSize(500, 100);
            efektif.setFont(new Font("System", Font.PLAIN, 14));
            tabel.add(header1);
            tabel.add(efektif);
            header1.setText("Jumlah tabrakan :" + jumlahTabrak);
            String inputEfektif = "";
            for (int i = 0; i < ruangannya.size(); i++) {
                inputEfektif = inputEfektif +  "Efektifitas ruangan " + ruangannya.get(i) + ": " + efektifitas[i] + "<br>";
            }
            
            efektif.setText("<html>"+inputEfektif+"</html>"); 
            tabel.setVisible(true);
            main.setVisible(false);
        });
    }
}
