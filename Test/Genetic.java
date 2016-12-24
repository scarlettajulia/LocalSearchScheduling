package test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author utamidias
 */
public class Genetic {
    
    private int jumlahMatkul;
    private int jumlahRuangan;
    private int[][] matkulClash = new int[jumlahMatkul][jumlahMatkul];
    private String[] state1, state2, state3, state4;
    private int[][] m1, m2, m3, m4;
    public String[][] jadwal = new String[12][5];
    public String[][] ruanganJadwal = new String[12][5]; 
    private int jumlahTabrakan;
    
    public Genetic() {
    }
    
    private void initMatString(String[][] M, int length, int width) {
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < width; j++) {
                M[i][j] = " ";
            }
        }
    }
    private void initMatriks(int[][] M, int length, int width) {
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < width; j++) {
                M[i][j] = 0;
            }
        }
    }
   
    //Mengecek apakah hari random sesuai dengan hari matkul
    private boolean cekHariExist(int h, String[] hari, String[] hariRuang) {
        String s = String.valueOf(h);
        return (Arrays.asList(hari).contains(s)) && (Arrays.asList(hariRuang).contains(s));
    }
    
    //Check Initial Constraint
    //buat ngecek pas randomize awal sesuai constraint input apa engga
    private boolean checkInitCons(NamaKegiatan N, int h, int i, int j, int ruang) {
        //jam mulai sesuai range matriks
        int jMulai = N.getJadwalJamMulai().get(i) - 7;
       //jam selesai sesuai range matriks
        int jSelesai = N.getJadwalJamSelesai().get(i) - 7;
        //ubah durasi ke integer
        int durasi = N.getJadwalDurasi().get(i);
        
        //cek hari bisa kapan saja
        String s = N.getJadwalHari().get(i);
        String[] hari;
        hari = s.split(",");
        
       String s2 = (String) N.getHari().get(ruang);
        String[] hariRuang;
        hariRuang = s2.split(",");
        //jam mulai sesuai range matriks
        int jMulaiRuang = N.getJamMulai().get(ruang) - 7;
       //jam selesai sesuai range matriks
        int jSelesaiRuang = N.getJamSelesai().get(ruang) - 7;
        //cek kondisi salah
        return !(!cekHariExist(h, hari, hariRuang) ||  (jMulai > j) || (jSelesai < j + durasi) || (jMulaiRuang > j) || (jSelesaiRuang < j + durasi));
    }
    
    private boolean adaRuangDiHari(NamaKegiatan N, int i, int hari) {
        String[] hariRuang = N.getHari().get(i).split(",");
        String h = String.valueOf(hari);
        return (Arrays.asList(hariRuang).contains(h));
    }
    
    private int assignRuangan(NamaKegiatan N, int hari) {
        ArrayList<Integer> ruang = new ArrayList<Integer>();
        for (int i = 0; i < jumlahRuangan; i++) {
            if (adaRuangDiHari(N, i, hari)) {
                ruang.add(i);
            }
        }
        Random rand = new Random();
        int r = rand.nextInt(ruang.size());
        return ruang.get(r);
    }
    public int getCountJumlahTabrakan() {
        return jumlahTabrakan;
    }
    
    public String[][] getJadwal() {
        return jadwal;
    }
    public String[][] getRuangan() {
        return ruanganJadwal;
    }
    //randomize untuk menghasilkan state awal
    private void randomize(NamaKegiatan N, String[] state) {
        
        //Variabel
        Random rand = new Random();
        String[] hariJadwal;
        String ruangan;
        int idxhari, hari, idxruangan, jam, banyakHari, durasi;
        idxhari = 0;
        hari = 0;
        idxruangan = 0;
        jam = 0;
        banyakHari = 0;
        durasi = 0;
        int i = 0;
        System.out.println("Jumlah matkul " + jumlahMatkul);
        System.out.println(N.getNamaKegiatan());
        while (i < jumlahMatkul) {
            //Menentukan hari sesuai hari pada spesifikasi
            System.out.println(N.getJadwalHari().get(i));
            hariJadwal = N.getJadwalHari().get(i).split(",");
            banyakHari = hariJadwal.length;
            idxhari = rand.nextInt(banyakHari);
            hari = Integer.parseInt(hariJadwal[idxhari]);
            
            //Menentukan jam
            jam = rand.nextInt(12);
            
            //Menentukan durasi mata kuliah
            durasi = N.getJadwalDurasi().get(i);
        
            //Menentukan apakah ada constraint ruangan atau tidak
            ruangan = N.getJadwalRuangan().get(i);
                    
            if ("-".equals(ruangan)) {
                idxruangan = assignRuangan(N, hari);
                while (!checkInitCons(N, hari, i, jam, idxruangan)) {
                    idxhari = rand.nextInt(banyakHari);
                    hari = Integer.parseInt(hariJadwal[idxhari]);
                    jam = rand.nextInt(12 - durasi);
                    idxruangan = assignRuangan(N, hari); 
                }
            }
            else {
                System.out.println(ruangan);
                idxruangan = N.getRuangan().indexOf(ruangan);
                while (!checkInitCons(N, hari, i, jam, idxruangan)) {
                    idxhari = rand.nextInt(banyakHari);
                    hari = Integer.parseInt(hariJadwal[idxhari]);
                    jam = rand.nextInt(12 - durasi);
                }       
            }
          
            ruangan = N.getRuangan().get(idxruangan) + "-" + idxruangan;
            
            state[i] = ruangan;
            
            int h = jam;
            
            for (int j = 0; j < durasi; j++) {
                state[i] = state[i] + "," + String.valueOf(hari) + String.valueOf(h);
                h++;
            }
            
            i++;
        }
    }
    
    private String getIndexRuangan(String[] state, int i) {
        String[] temp = state[i].split(",");
        String[] temp2 = temp[0].split("-");
        return temp2[1];
    }
    
    private String getRuangMatkul(String[] state, int i) {
        String[] temp = state[i].split(",");
        String[] temp2 = temp[0].split("-");
        return temp2[0];
    }
    
    private int getHariMatkul(String[] state, int i) {
        String[] temp = state[i].split(",");
        return Character.getNumericValue(temp[1].charAt(0));
    }
    
    private int getJamMulaiMatkul(String[] state, int i) {
        int jamMatkul;
        String[] temp = state[i].split(",");
        jamMatkul = Integer.parseInt(temp[1].substring(1));
        return jamMatkul;
    }
    
    private int countClash(NamaKegiatan N, String[] state, int[][] matkulClash) {
        int clash = 0;
        for (int i = 0; i < jumlahMatkul - 1; i++) {
            String[] jadwal1 = state[i].split(",");
            for (int j = i + 1; j < jumlahMatkul; j++) {
                String[] jadwal2 = state[j].split(",");
                int l = 1;
                boolean found = false;
                while ((l < jadwal1.length) && !found) {
                    if( jadwal1[0].equals(jadwal2[0]) && Arrays.asList(jadwal2).contains(jadwal1[l]) && !"-".equals(jadwal1[l])) {
                        clash++;
                        found = true;
                        matkulClash[i][j] = 1;
                    }
                    l++;
                }
            }
        }
        return clash;
    }
    
    private int fitnessFunction(NamaKegiatan N, String[] state, int[][] matkulClash) {
        int total = jumlahMatkul * (jumlahMatkul - 1) / 2;
        initMatriks(matkulClash, jumlahMatkul, jumlahMatkul);
        int tabrakan = countClash(N, state, matkulClash);
        int fFunc = (total - tabrakan) * 100 / total;
        return fFunc;
    }
    
    
    private String[] pickRandom (int f1, int f2, int f3, int f4) {
        int sum = f1 + f2 + f3 + f4;
        int r1 = f1;
        int r2 = f1 + f2;
        int r3 = f1 + f2 + f3;
        Random r = new Random();
        if(sum == 0) { sum = 1;}
        int picked = r.nextInt(sum);
        if (picked >= 0 && picked < r1) {
            return state1;
        }
        else if (picked >= r1 && picked < r2) {
            return state2;
        }
        else if (picked >= r2 && picked < r3) {
            return state3;
        }
        return state4;
    }
    
    private int selectSplitting (String[] state1, int[][] matkulClash) {
        int splitIdx = 0;
        boolean found = false;
        int i = 0;
        int j = 0;
        //Bisa dioptimasi lagi pakai array, cek di mana aja clashnya ambil split di tengah
       
        while (!found && (i < jumlahMatkul)) {
            while (!found && j < jumlahMatkul) {
                if (matkulClash[i][j] > 0) {
                    found = true;
                    splitIdx = i;
                }
                j++;
            }
            i++;
        }
        return splitIdx + 1;
    }
    
    private String[] maxState(int f1, int f2, int f3, int f4) {
        int max = f1;
        if (f2 > max) {
            max = f2;
        }
        if (f3 > max) {
            max = f3;
        }
        if (f4 > max) {
            max = f4;
        }
        if (max == f1) {
            return state1;
        } else if (max == f2) {
            return state2;
        } else if (max == f2) {
            return state3;
        }
        return state4;
    }
    
    private String[] minState(int f1, int f2, int f3, int f4) {
        int min = f1;
        if (f2 < min) {
            min = f2;
        }
        if (f3 < min) {
            min = f3;
        }
        if (f4 < min) {
            min = f4;
        }
        if (min == f1) {
            return state1;
        } else if (min == f2) {
            return state2;
        } else if (min == f2) {
            return state3;
        }
        return state4;
    }
    
    private void crossOver(int f1, int f2, int f3, int f4) {
       //Versi ambil state minimum lepas comment di bawah ini, comment dari tanda yg sudah diberikan
        /* 
        String[] s1 = pickRandom(f1, f2, f3, f4);
        String[] s3 = pickRandom(f1, f2, f3, f4);
        */
        
        String[] s2 = pickRandom(f1, f2, f3, f4);
        String[] s4 = pickRandom(f1, f2, f3, f4);
        
        //Versi ambil state minimum, comment dari sini
        
        String[] s1 = maxState(f1, f2, f3, f4);
        String[] s3 = maxState(f1, f2, f3, f4);
        String[] s5 = minState(f1, f2, f3, f4);
        
        if (!(f1 == f2 && f2 == f3 && f3 == f4)) {
            while (s2 == s5 || s4 == s5) {
            s2 = pickRandom(f1, f2, f3, f4);
            s4 = pickRandom(f1, f2, f3, f4);
            }
        }
        
        //Sampai sini
        
        int[][] mtemp;
        int[][] mtemp2;
        if (s1 == state1) {
            mtemp = m1;
        } else if (s1 == state2) {
            mtemp = m2;
        } else if(s1 == state3) {
            mtemp = m3;
        } else {
            mtemp = m4;
        }
        if (s3 == state1) {
            mtemp2 = m1;
        } else if (s3 == state2) {
            mtemp2 = m2;
        } else if(s3 == state3) {
            mtemp2 = m3;
        } else {
            mtemp2 = m4;
        }

        int splitIndex = selectSplitting(s1, mtemp);
        int splitIndex2 = selectSplitting(s3, mtemp2);
        String[] temp = new String[jumlahMatkul];        
        for (int i = 0; i <= splitIndex; i++) {
            temp[i] = s1[i];
            s1[i] = s2[i];
            s2[i] = temp[i];
        }
        for (int i = 0; i <= splitIndex2; i++) {
            temp[i] = s3[i];
            s3[i] = s4[i];
            s4[i] = temp[i];
        }
        state1 = s1;
        state2 = s2;
        state3 = s3;
        state4 = s4;
    }
    
    private void mutate(NamaKegiatan N, String[] state, int[][] matkulClash, int type) {
        
        Random rand = new Random();
        String[] hariJadwal;
        String ruangan;
        int j, idxhari, hari, idxruangan, jam, banyakHari, durasi;
        int update = countClash(N, state, matkulClash);
        boolean adaClash;
        int temp;
        for (int i = 0; i < jumlahMatkul; i++) {
            
            adaClash = false;
            j = 0;
            durasi = N.getJadwalDurasi().get(i);
            temp = i;
            
            while ((j < jumlahMatkul) && !adaClash) {
                if (matkulClash[i][j] != 0) {
                    if (type == 2) {
                        i = j;
                    }

                    //Menentukan hari sesuai hari pada spesifikasi
                    hariJadwal = N.getJadwalHari().get(i).split(",");
                    banyakHari = hariJadwal.length;
                    idxhari = rand.nextInt(banyakHari);
                    hari = Integer.parseInt(hariJadwal[idxhari]);

                    //Menentukan jam
                    jam = rand.nextInt(12 - durasi);

                    //Menentukan durasi mata kuliah
                    durasi = N.getJadwalDurasi().get(i);

                    //Menentukan apakah ada constraint ruangan atau tidak
                    ruangan = N.getJadwalRuangan().get(i);

                    if ("-".equals(ruangan)) {
                        idxruangan = assignRuangan(N, hari);
                        while (!checkInitCons(N, hari, i, jam, idxruangan)) {
                            idxhari = rand.nextInt(banyakHari);
                            hari = Integer.parseInt(hariJadwal[idxhari]);
                            jam = rand.nextInt(12 - durasi);
                            idxruangan = assignRuangan(N, hari); 
                        }
                    }
                    
                    else {
                        idxruangan = N.getRuangan().indexOf(ruangan);
                        while (!checkInitCons(N, hari, i, jam, idxruangan)) {
                            idxhari = rand.nextInt(banyakHari);
                            hari = Integer.parseInt(hariJadwal[idxhari]);
                            jam = rand.nextInt(12 - durasi);
                        }       
                    }

                    ruangan = N.getRuangan().get(idxruangan) + "-" + idxruangan;

                    state[i] = ruangan;

                    int h = jam;

                    for (int k = 0; k < durasi; k++) {
                        state[i] = state[i] + "," + String.valueOf(hari) + String.valueOf(h);
                        h++;
                    }
                    adaClash = true;
                }
                
                i = temp;
                matkulClash[i][j] = 0;
                matkulClash[j][i] = 0;
                j++;
            }
        }
    }
    
    private boolean finishCond (int f1, int f2, int f3, int f4) {
        return (f1 == 100 || f2 == 100 || f3 == 100 || f4 == 100);
    }
    public float[] countPresentaseKelas(String[][] ruanganJadwal) {
        test.NamaKegiatan x = new test.NamaKegiatan();
        float[] arrPresentase = new float[x.getRuangan().size()];
        int[] arrCount = new int[x.getRuangan().size()];
        int[] arrJamMatkulTersedia = new int[x.getRuangan().size()];
        for (int i = 0; i < arrCount.length; i++) {
            arrCount[i] = 0;
            arrJamMatkulTersedia[i] = 0;
        }
        
        for (int i = 0; i < 11; i++) {
            for (int j = 0; j < 5; j++) {
                if (ruanganJadwal[i][j].length() > 6) {
                    if (ruanganJadwal[i][j].length() > 6) {
                        String[] matkul = ruanganJadwal[i][j].split(";");
                        for (int k = 0; k < matkul.length; k++) {
                            arrCount[x.getRuangan().indexOf(matkul[k])]++;
                        }
                    } else {
                        arrCount[x.getRuangan().indexOf(ruanganJadwal[i][j])]++;
                    }
                }
            }
        }
        
        for (int i = 0; i < arrCount.length; i++) {
            String[] hari = x.getHari().get(i).split(",");
            arrJamMatkulTersedia[i] = (x.getJamSelesai().get(i)
                    - x.getJamMulai().get(i)) * hari.length;
            arrPresentase[i] = ((float)arrCount[i] / (float)arrJamMatkulTersedia[i]) * 100;
        }
        return arrPresentase;
    }
    
    public void runGenetic() {
        
        NamaKegiatan jadwalbaru = new NamaKegiatan();
        jumlahRuangan = jadwalbaru.getRuangan().size();
        jumlahMatkul = jadwalbaru.getNamaKegiatan().size();
        String[] finalstate = new String[jumlahMatkul];
        
        System.out.println();
        //Generate state dan randomize
        state1 = new String[jumlahMatkul];
        state2 = new String[jumlahMatkul];
        state3 = new String[jumlahMatkul];
        state4 = new String[jumlahMatkul];
        m1 = new int[jumlahMatkul][jumlahMatkul];
        m2 = new int[jumlahMatkul][jumlahMatkul];
        m3 = new int[jumlahMatkul][jumlahMatkul];
        m4 = new int[jumlahMatkul][jumlahMatkul];
        initMatriks(m1, jumlahMatkul, jumlahMatkul);
        initMatriks(m2, jumlahMatkul, jumlahMatkul);
        initMatriks(m3, jumlahMatkul, jumlahMatkul);
        initMatriks(m4, jumlahMatkul, jumlahMatkul);
        initMatString(jadwal, 12, 5);
        initMatString(ruanganJadwal, 12, 5);
        randomize(jadwalbaru, state1);
        randomize(jadwalbaru, state2);
        randomize(jadwalbaru, state3);
        randomize(jadwalbaru, state4);
        
        System.out.println(Arrays.toString(state1));
        System.out.println(Arrays.toString(state2));
        System.out.println(Arrays.toString(state3));
        System.out.println(Arrays.toString(state4));
        System.out.println("Selesai randomize");
        System.out.println();
        
        int f1 = fitnessFunction(jadwalbaru, state1, m1);
        int f2 = fitnessFunction(jadwalbaru, state2, m2);
        int f3 = fitnessFunction(jadwalbaru, state3, m3);
        int f4 = fitnessFunction(jadwalbaru, state4, m4);
        System.out.println("Fitness function: "+ f1 + " " + f2 + " " + f3 + " " + f4);
        System.out.println();
        
        int loop = 0;
        while(!finishCond(f1,f2,f3,f4) && (loop < 2000)) {
            crossOver(f1, f2, f3, f4);
            mutate(jadwalbaru, state1, m1, 1);
            mutate(jadwalbaru, state2, m2, 2);
            mutate(jadwalbaru, state3, m3, 2);
            mutate(jadwalbaru, state4, m4, 1);
            f1 = fitnessFunction(jadwalbaru, state1, m1);
            f2 = fitnessFunction(jadwalbaru, state2, m2);
            f3 = fitnessFunction(jadwalbaru, state3, m3);
            f4 = fitnessFunction(jadwalbaru, state4, m4);
            loop++;
        }
        if (f1 == 100) {
            finalstate = state1;
        } else if (f2 == 100) {
            finalstate = state2;
        } else if (f3 == 100) {
            finalstate = state3;
        } else if (f4 == 100) {
            finalstate = state4;
        }
        if (f1 != 100 && f2 != 100 && f3 != 100 && f4 != 100) {
            finalstate = maxState(f1,f2,f3,f4);
        }
        int a, b, d;
        
        
        for (int i = 0; i < finalstate.length; i++) {
            a = getHariMatkul(finalstate, i);
            b = getJamMulaiMatkul(finalstate, i);
            d = jadwalbaru.getJadwalDurasi().get(i);
            for (int j = 0; j < d; j++) {
                if (" ".equals(jadwal[b][a-1])) {
                    jadwal[b][a-1] = jadwalbaru.getNamaKegiatan().get(i);
                    b++;
                }
                else {
                    jadwal[b][a-1] = jadwal[b][a-1] + ";" + jadwalbaru.getNamaKegiatan().get(i);
                    b++;
                }
            }
        }
        
        System.out.println("Fitness function: "+ f1 + " " + f2 + " " + f3 + " " + f4);
        System.out.println();
        
        System.out.println("Banyaknya loop = " + loop);
        System.out.println(Arrays.toString(finalstate));
    }
    
//    public static void main (String[] args) {
//        Genetic coba = new Genetic();
//        coba.runGenetic();
//        for (int i = 0; i < 12; i++) {
//            System.out.print( (i+7) + "   ");
//            for (int j = 0; j < 5; j++) {
//                if (" ".equals(coba.jadwal[i][j])) {
//                    System.out.print("XXXXXX" + "   ");
//                }
//                else {
//                    System.out.print(coba.jadwal[i][j] + "   ");
//                }
//            }
//            System.out.println();
//        }
//    }
}
