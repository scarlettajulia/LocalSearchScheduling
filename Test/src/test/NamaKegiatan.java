/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author taufic
 */
public class NamaKegiatan {
    
    private ArrayList<String> ruangan;
    private ArrayList<Integer> jamMulai;
    private ArrayList<Integer> jamSelesai;
    private ArrayList<String> hari;
    
    private ArrayList<String> namaKegiatan;
    private ArrayList<String> jadwalRuangan;
    private ArrayList<Integer> jadwalJamMulai;
    private ArrayList<Integer> jadwalJamSelesai;
    private ArrayList<Integer> jadwalDurasi;
    private ArrayList<String> jadwalHari;
    
    public NamaKegiatan() {
        int i = 0;
        //System.out.println("Masukan Nama File :");
        File inputFile = new File("testcase2.txt");
        try {
            Scanner userInput = new Scanner(inputFile);
            String[][] inputs = new String[300][];
            userInput = new Scanner(inputFile);

            while (userInput.hasNextLine()) {
                inputs[i] = userInput.nextLine().split(";");
                i++;
            }
            //System.out.println(inputs[1][2]);
            ruangan = new ArrayList<String>();
            jamMulai = new ArrayList<Integer>();
            jamSelesai = new ArrayList<Integer>();
            hari = new ArrayList<String>();
            namaKegiatan = new ArrayList<String>();
            jadwalRuangan = new ArrayList<String>();
            jadwalJamMulai = new ArrayList<Integer>();
            jadwalJamSelesai = new ArrayList<Integer>();
            jadwalDurasi = new ArrayList<Integer>();
            jadwalHari = new ArrayList<String>();

            /* Pembagian list untuk ruangan */
            int j = 1;
            int k;
            while (i > j && !(inputs[j][0].equals("Jadwal")) && !(inputs[j][0].equals(""))) {
                k = 0;
                for (String content : inputs[j]) {
                    if (k == 0) {
                        ruangan.add(content);
                    } else if (k == 1) {
                        String[] jamMulaiArr = content.split("\\.");
                        int jamMulaiVar = Integer.parseInt(jamMulaiArr[0]);
                        jamMulai.add(jamMulaiVar);
                    } else if (k == 2) {
                        String[] jamSelesaiArr = content.split("\\.");
                        int jamSelesaiVar = Integer.parseInt(jamSelesaiArr[0]);
                        jamSelesai.add(jamSelesaiVar);
                    } else {
                        hari.add(content);
                    }
                    k++;
                }
                j++;
            }

            /* Pembagian list untuk Jadwal*/
            j = j + 2;
            while (i > j) {
                k = 0;
                for (String content : inputs[j]) {
                    if (k == 0) {
                        namaKegiatan.add(content);
                    } else if (k == 1) {
                        jadwalRuangan.add(content);
                    } else if (k == 2) {
                        String[] jamMulaiArr = content.split("\\.");
                        int jamMulai = Integer.parseInt(jamMulaiArr[0]);
                        jadwalJamMulai.add(jamMulai);
                    } else if (k == 3) {
                        String[] jamSelesaiArr = content.split("\\.");
                        int jamSelesai = Integer.parseInt(jamSelesaiArr[0]);
                          
                        jadwalJamSelesai.add(jamSelesai);
                    } else if (k == 4) {
                        int durasi = Integer.parseInt(content);
                        jadwalDurasi.add(durasi);
                    } else {
                        jadwalHari.add(content);
                    }
                    k++;
                }
                j++;
            }
            userInput.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found!");
            assert(true);
        }
        
        /* Tes Print */
        /*jadwalHari.stream().forEach((jadwalHari1) -> {
            System.out.println(jadwalHari1);
        });*/
    }

    /** 
    * @return Mengembalikan list ruangan yang ada
    */
    public ArrayList<String> getRuangan() {
        return ruangan;
    }

    /** 
    * @return Mengembalikan list jam mulai tersedianya ruangan
    */
    public ArrayList<Integer> getJamMulai() {
        return jamMulai;
    }

    /** 
    * @return Mengembalikan list jam selesai tersedianya ruangan
    */
    public ArrayList<Integer> getJamSelesai() {
        return jamSelesai;
    }

    /** 
    * @return Mengembalikan list hari tersedianya ruangan
    */
    public ArrayList<String> getHari() {
        return hari;
    }

    /** 
    * @return Mengembalikan list nama mata kuliah
    */
    public ArrayList<String> getNamaKegiatan() {
        return namaKegiatan;
    }

    /** 
    * @return Mengembalikan list batasan ruangan dari mata kuliah
    */
    public ArrayList<String> getJadwalRuangan() {
        return jadwalRuangan;
    }

    /** 
    * @return Mengembalikan list batasan jam mulai kuliah
    */
    public ArrayList<Integer> getJadwalJamMulai() {
        return jadwalJamMulai;
    }

    /** 
    * @return Mengembalikan list batasan jam selesai kuliah
    */
    public ArrayList<Integer> getJadwalJamSelesai() {
        return jadwalJamSelesai;
    }

    /** 
    * @return Mengembalikan list durasi kuliah
    */
    public ArrayList<Integer> getJadwalDurasi() {
        return jadwalDurasi;
    }

    /** 
    * @return Mengembalikan list batasan hari kuliah
    */
    public ArrayList<String> getJadwalHari() {
        return jadwalHari;
    }
}

