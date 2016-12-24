package test;
import java.util.ArrayList;
import java.util.Random;
import java.util.Arrays;


class Hill {
    /* Mendapatkan nilai-nilai dari kelas NamaKegiatan */
    private NamaKegiatan kegiatan;
    private ArrayList<String> ruangan;
    private ArrayList<Integer> jamRuanganMulai;
    private ArrayList<Integer> jamRuanganSelesai;
    private ArrayList<String> hariTersediaRuangan;
    private ArrayList<String> namaKegiatan;
    private ArrayList<String> ruanganKegiatan;
    private ArrayList<Integer> jamJadwalMulai;
    private ArrayList<Integer> jamJadwalSelesai;
    private ArrayList<Integer> durasiKegiatan;
    private ArrayList<String> hariTersediaJadwal;
    private final int ROW = 11;
    private final int COLUMN = 5;
    private final int PANJANG_NAMA_MATKUL = 6;
    private int jumlahTabrakan = 0;

    public Hill() {
        kegiatan = new NamaKegiatan();
        ruangan = kegiatan.getRuangan();
        jamRuanganMulai = kegiatan.getJamMulai();
        jamRuanganSelesai = kegiatan.getJamSelesai();
        hariTersediaRuangan = kegiatan.getHari();
        namaKegiatan = kegiatan.getNamaKegiatan();
        ruanganKegiatan = kegiatan.getJadwalRuangan();
        jamJadwalMulai = kegiatan.getJadwalJamMulai();
        jamJadwalSelesai = kegiatan.getJadwalJamSelesai();
        durasiKegiatan = kegiatan.getJadwalDurasi();
        hariTersediaJadwal = kegiatan.getJadwalHari();
    }

	public void initMatJumlah(int[][] M) {
        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COLUMN; j++) {
                M[i][j] = 0;
            }
        }
    }

	public boolean cekHariExist(int h, String[] hari) {
            String s = String.valueOf(h + 1);
            return (Arrays.asList(hari).contains(s));
	}

	public boolean checkInitsCons(int h, int i, int j, int k) {
        int jMulai = jamJadwalMulai.get(i) - 7;
	int jSelesai = jamJadwalSelesai.get(i) - 7;
        int jKelasMulai = jamRuanganMulai.get(k) - 7;
        int jKelasSelesai = jamRuanganSelesai.get(k) - 7;
	int durasi = durasiKegiatan.get(i);
        String[] hariRuanganStr = hariTersediaRuangan.get(k).split(",");
	String[] hariStr = hariTersediaJadwal.get(i).split(",");
        if (cekHariExist(h,hariStr) && cekHariExist(h,hariRuanganStr) && ((j >= jMulai)
            && (j + durasi <= jSelesai)) && ((j >= jKelasMulai) && (j + durasi <= jKelasSelesai))) {
                return true;
        } else {
                return false;
        }
	}

    /* Mengacak mata kuliah sebagai penentu titik start algoritma */
	public void randomize(String[][] jadwal, int[][] nMatkul, String[][] ruanganJadwal) {
            Random rand = new Random();
            int hari = rand.nextInt(COLUMN);
            int jam = rand.nextInt(ROW);
            int jumMatkul = namaKegiatan.size();
            int i = 0;
            int k = 0;
            int limit = 0;
            initMatJumlah(nMatkul);
            while (i < jumMatkul) {
            String ruangKelas = ruanganKegiatan.get(i);
            if (ruangKelas.equals("-")) {
                k = rand.nextInt(ruangan.size());
            } else {
                k = ruangan.indexOf(ruangKelas);
            }
            while (checkInitsCons(hari, i, jam, k) == false && limit < 10000) { //Membatasi step sampai 10000 langkah saja
                    hari = rand.nextInt(COLUMN);
                    jam = rand.nextInt(ROW);
                if (ruangKelas.equals("-")) {
                    k = rand.nextInt(ruangan.size());
                }
                limit++;
            }
            ruangKelas = ruangan.get(k);
            int x;
            int max = jam + durasiKegiatan.get(i);
            while (max > ROW) {
                jam--;
                max = jam + durasiKegiatan.get(i);
            }
            for (x = jam; x < max; x++) {
                if (jadwal[x][hari] == null) {
                        jadwal[x][hari] = namaKegiatan.get(i);
                } else {
                        jadwal[x][hari] = jadwal[x][hari] + ";" + namaKegiatan.get(i);
                }
                if (ruanganJadwal[x][hari] == null) {
                    ruanganJadwal[x][hari] = ruangKelas + "-" + k;
                } else {
                    ruanganJadwal[x][hari] = ruanganJadwal[x][hari] + ";" + ruangKelas + "-" + k;
                }
    			nMatkul[x][hari]++;
            }
			hari = rand.nextInt(COLUMN);
			jam = rand.nextInt(ROW);
			i++;
		}
	}

    /* Mengembalikan nilai true jika suatu mata kuliah sudah diletakkan di hari dan jam yang sesuai */
    public boolean isMatkulValid(String jadwal, int i, int j) {
        int idx = namaKegiatan.indexOf(jadwal);
        String[] hariStr = hariTersediaJadwal.get(idx).split(",");
        int[] hari = new int[hariStr.length];
        for (int l = 0; l < hariStr.length; l++) {
            hari[l] = Integer.parseInt(hariStr[l]) - 1;
        }
        int jMulai = jamJadwalMulai.get(idx) - 7;
        int jSelesai = jamJadwalSelesai.get(idx) - 7;
        if ((!cekHariExist(j, hariStr) || i < jMulai) || i > jSelesai) {
            return false;
        } else {
            return true;
        }
    }

    /* Jika ada mata kuliah yang bertabrakan, maka akan mengembalikan nilai true */
    public boolean  isTabrakan(int[][] nMatkul, String[][] ruanganJadwal, int i, int j) {
        if (nMatkul[i][j] > 1) {
            String[] jadwalJam = ruanganJadwal[i][j].split(";");
            for (int k = 0; k < jadwalJam.length; k++) {
                for (int l = k + 1; l < jadwalJam.length; l++) {
                    if (jadwalJam[k].equals(jadwalJam[l])) {
                        return true;
                    }
                }
            }
        } else {
            return false;
        }
        return false;
    }

    /* Prosedur untuk menghapus mata kuliah pada tabel jadwal */
    public void deleteMatkul(String[][] jadwal, int[][]nMatkul, String[][] ruanganJadwal,int i,
        int j, int k, String[] matkul, String[] tempRuangan) {
        int x = i;
        int durasi = durasiKegiatan.get(namaKegiatan.indexOf(matkul[k]));
        int count = 0;
        /* Menghapus satu blok mata kuliah */
        while (count < durasi) {
            if (jadwal[x][j] != null) {
                if (jadwal[x][j].contains(matkul[k])) {
                    if (jadwal[x][j].length() > PANJANG_NAMA_MATKUL) {
                        if (k == 0) {
                            String tempStr = jadwal[x][j].replace(matkul[k] + ";", "");
                            jadwal[x][j] = tempStr;
                            tempStr = ruanganJadwal[x][j].replace(tempRuangan[k] + ";", "");
                            ruanganJadwal[x][j] = tempStr;
                        } else {
                            String tempStr = jadwal[x][j].replace(";" + matkul[k], "");
                            jadwal[x][j] = tempStr;
                            tempStr = ruanganJadwal[x][j].replace(";" + tempRuangan[k], "");
                            ruanganJadwal[x][j] = tempStr;
                        }
                    } else {
                        jadwal[x][j] = null;
                        ruanganJadwal[x][j] = null;
                    }
                    nMatkul[x][j]--;
                    count++;
                    x++;
                    if (x >= ROW) {
                        x = x - 2;
                    }
                } else {
                    x = x - 2;
                }
            } else {
                x = x - 2;
            }
        }
    }

    public void tulisJadwal (String[][] jadwal, String[][] ruanganJadwal, int[][] nMatkul, String tempKegiatan, String tempRuangKegiatan) {
        int o = 0;
        boolean done = false;
        while (o < COLUMN && !done) {
            int p = 0;
            while (p < ROW && !done) {
                if (checkInitsCons(o, namaKegiatan.indexOf(tempKegiatan), p, ruangan.indexOf(tempRuangKegiatan))) {
                    int q = p + durasiKegiatan.get(namaKegiatan.indexOf(tempKegiatan));
                    while (q > ROW) {
                        p--;
                        q = p + durasiKegiatan.get(namaKegiatan.indexOf(tempKegiatan));
                    }
                    int temp_p = p;
                    /* Menuliskan 1 blok mata kuliah */
                    while (p < q) {
                        if (jadwal[p][o] == null) {
                            jadwal[p][o] = tempKegiatan;
                            ruanganJadwal[p][o] = tempRuangKegiatan;
                            nMatkul[p][o]++;
                        } else {
                            jadwal[p][o] = jadwal[p][o] + ";" + tempKegiatan;
                            ruanganJadwal[p][o] = ruanganJadwal[p][o] + ";" + tempRuangKegiatan;
                            nMatkul[p][o]++;
                            if (isTabrakan(nMatkul, ruanganJadwal, p, o)) {
                                String tempStr = jadwal[p][o].replace(";" + tempKegiatan, "");
                                jadwal[p][o] = tempStr;
                                tempStr = ruanganJadwal[p][o].replace(";" + tempRuangKegiatan, "");
                                ruanganJadwal[p][o] = tempStr;
                                nMatkul[p][o]--;
                            }
                        }
                        p++;
                    }
                    if (isTabrakan(nMatkul, ruanganJadwal, temp_p, o)) {
                        done = false;
                    } else {
                        done = true;
                    }
                } else {
                    p++;
                }
            }
            if (!done) {
                o++;
            }
        }
        /* Jika tetap tidak ditemukan solusi yang memiliki jumlah tabrakan = 0*/
        if (!done) {
            o = 0;
            while (o < COLUMN && !done) {
                int p = 0;
                while (p < ROW && !done) {
                    if (checkInitsCons(o, namaKegiatan.indexOf(tempKegiatan), p, ruangan.indexOf(tempRuangKegiatan))) {
                        int q = p + durasiKegiatan.get(namaKegiatan.indexOf(tempKegiatan));
                        while (q > ROW) {
                            p--;
                            q = p + durasiKegiatan.get(namaKegiatan.indexOf(tempKegiatan));
                        }
                        /* Menuliskan 1 blok mata kuliah */
                        while (p < q) {
                            if (jadwal[p][o] == null) {
                                jadwal[p][o] = tempKegiatan;
                                ruanganJadwal[p][o] = tempRuangKegiatan;
                                nMatkul[p][o]++;
                            } else {
                                jadwal[p][o] = jadwal[p][o] + ";" + tempKegiatan;
                                ruanganJadwal[p][o] = ruanganJadwal[p][o] + ";" + tempRuangKegiatan;
                                nMatkul[p][o]++;
                            }
                            p++;
                        }
                        done = true;
                    } else {
                        p++;
                    }
                }
                if (!done) {
                    o++;
                }
            }
        }
    }

    /* Prosedur untuk hill climbing search */
    public void hillClimbing(String[][] jadwal, int[][] nMatkul, String[][] ruanganJadwal) {
        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COLUMN; j++) {
                if (jadwal[i][j] != null) {
                    if (jadwal[i][j].length() > PANJANG_NAMA_MATKUL) {
                        String[] matkul = jadwal[i][j].split(";");
                        String[] tempRuangan = ruanganJadwal[i][j].split(";");
                        for (int k = 0; k < matkul.length; k++) {
                            /* Memindahkan jadwal yang tidak valid, tetapi di tempat tsb ada beberapa matkul */
                            if (!isMatkulValid(matkul[k], i, j)) {
                                if (k == 0) {
                                    deleteMatkul(jadwal, nMatkul, ruanganJadwal, i, j, k, matkul, tempRuangan);
                                } else {
                                    deleteMatkul(jadwal, nMatkul, ruanganJadwal, i, j, k, matkul, tempRuangan);
                                }
                                /* Memasukkan matkul tidak valid ke jadwal yang benar */
                                if (ruanganKegiatan.get(i).equals("-")) {
                                    Random rand = new Random();
                                    int idx = rand.nextInt(ruangan.size());
                                    tempRuangan[k] = ruangan.get(idx) + "-" + idx;
                                    tulisJadwal(jadwal, ruanganJadwal, nMatkul, matkul[k], tempRuangan[k]);
                                }
                            }
                        }
                    } else {
                        /* Memindahkan jadwal yang tidak valid */
                        if (!isMatkulValid(jadwal[i][j], i, j)) {
                            int x = i;
                            int durasi = durasiKegiatan.get(namaKegiatan.indexOf(jadwal[i][j]));
                            int count = 0;
                            String tempKegiatan = jadwal[i][j];
                            String tempRuangKegiatan = ruanganJadwal[i][j];
                            while (count < durasi) {
                                if (jadwal[x][j] != null) {
                                    if (jadwal[x][j].contains(tempKegiatan)) {
                                        if (jadwal[x][j].length() > PANJANG_NAMA_MATKUL) {
                                            String[] matkul = jadwal[x][j].split(";");
                                            String[] tempRuangan = ruanganJadwal[x][j].split(";");
                                            int k;
                                            for (k = 0; k < matkul.length; k++) {
                                                if (matkul[k].equals(tempKegiatan)) {
                                                    break;
                                                }
                                            }
                                            if (k == 0) {
                                                String tempStr = jadwal[x][j].replace(matkul[k] + ";", "");
                                                jadwal[x][j] = tempStr;
                                                tempStr = ruanganJadwal[x][j].replace(tempRuangan[k] + ";", "");
                                                ruanganJadwal[x][j] = tempStr;
                                            } else {
                                                String tempStr = jadwal[x][j].replace(";" + matkul[k], "");
                                                jadwal[x][j] = tempStr;
                                                tempStr = ruanganJadwal[x][j].replace(";" + tempRuangan[k], "");
                                                ruanganJadwal[x][j] = tempStr;
                                            }
                                        } else {
                                            jadwal[x][j] = null;
                                            ruanganJadwal[x][j] = null;
                                        }
                                        nMatkul[x][j]--;
                                        count++;
                                        x++;
                                        if (x >= ROW) {
                                            x = x - 2;
                                        }
                                    } else {
                                        x = x - 2;
                                    }
                                } else {
                                    x = x - 2;
                                }
                            }
                            /* Memasukkan matkul tidak valid ke jadwal yang benar */
                            tulisJadwal(jadwal, ruanganJadwal, nMatkul, tempKegiatan, tempRuangKegiatan);
                        }
                    }
                }
            }
        }
        //fixTabrakan(jadwal, nMatkul, ruanganJadwal);
        countTabrakan(jadwal, nMatkul, ruanganJadwal);
    }

    /* Fungsi untuk menghitung jumlah tabrakan yang terjadi */
    public void countTabrakan (String[][] jadwal, int[][] nMatkul, String[][] ruanganJadwal) {
        for (int i = 0; i < ROW; i++) {
            for(int j = 0; j < COLUMN; j++) {
                if (jadwal[i][j] != null) {
                    /* Menghitung jumlah tabrakan */
                    if (isTabrakan(nMatkul, ruanganJadwal, i , j)) {
                        jumlahTabrakan = jumlahTabrakan + countJumlahTabrakanDalamSel(nMatkul, ruanganJadwal, i, j);
                    }
                }
            }
        }
    }

    /* Sebuah prosedur untuk memindahkan mata kuliah yang bertabrakan */
    public void fixTabrakan(String[][] jadwal, int[][] nMatkul, String[][] ruanganJadwal) {
        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COLUMN; j++) {
                if (jadwal[i][j] != null) {
                    if (isTabrakan(nMatkul, ruanganJadwal, i , j)) {
                        String[] matkul = jadwal[i][j].split(";");
                        String[] tempRuangan = ruanganJadwal[i][j].split(";");
                        for (int k = 0; k < countJumlahTabrakanDalamSel(nMatkul, ruanganJadwal, i, j); k++) {
                            for (int l = 0; l < matkul.length; l++) {
                                deleteMatkul(jadwal, nMatkul, ruanganJadwal, i, j, l, matkul, tempRuangan);
                                tulisJadwal(jadwal, ruanganJadwal, nMatkul, matkul[l], tempRuangan[l]);
                            }
                        }
                    }
                }
            }
        }
    }

    public int countJumlahTabrakanDalamSel(int[][] nMatkul, String[][] ruanganJadwal, int i, int j) {
        int count = 0;
        if (nMatkul[i][j] > 1) {
            String[] jadwalJam = ruanganJadwal[i][j].split(";");
            for (int k = 0; k < jadwalJam.length; k++) {
                for (int l = k + 1; l < jadwalJam.length; l++) {
                    if (jadwalJam[l].equals(jadwalJam[k])) {
                        count++;
                    }
                }
            }
        }
        return count;
    }
    
    public float[] countPresentaseKelas(String[][] ruanganJadwal) {
        float[] arrPresentase = new float[ruangan.size()];
        int[] arrCount = new int[ruangan.size()];
        int[] arrJamMatkulTersedia = new int[ruangan.size()];
        for (int i = 0; i < arrCount.length; i++) {
            arrCount[i] = 0;
            arrJamMatkulTersedia[i] = 0;
        }
        
        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COLUMN; j++) {
                if (ruanganJadwal[i][j] != null) {
                    if (ruanganJadwal[i][j].length() > PANJANG_NAMA_MATKUL) {
                        String[] matkul = ruanganJadwal[i][j].split(";");
                        for (int k = 0; k < matkul.length; k++) {
                            String[] komponen_matkul = matkul[k].split("-");
                            if (k == 0) {
                                arrCount[Integer.parseInt(komponen_matkul[1])]++;
                            }
                            for (int l = 0; l < k; l++) {
                                if (!matkul[l].equals(matkul[k])) {
                                    arrCount[Integer.parseInt(komponen_matkul[1])]++;
                                }
                            }
                        }
                    } else {
                        arrCount[ruangan.indexOf(ruanganJadwal[i][j])]++;
                    }
                }
            }
        }
        
        for (int i = 0; i < arrCount.length; i++) {
            String[] hari = hariTersediaRuangan.get(i).split(",");
            arrJamMatkulTersedia[i] = (jamRuanganSelesai.get(i) - jamRuanganMulai.get(i)) * hari.length;
            arrPresentase[i] = ((float)arrCount[i] / (float)arrJamMatkulTersedia[i]) * 100;
        }
        return arrPresentase;
    }

	/**
	* @return objek kegiatan
	*/
	public NamaKegiatan getKegiatan() {
		return kegiatan;
	}

    public int getJumlahTabrakan() {
        return jumlahTabrakan;
    }
    
    public ArrayList<String> getRuangan() {
        return ruangan;
    }
    
}