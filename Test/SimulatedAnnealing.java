package test;
import java.util.ArrayList;
import java.util.Random;
import java.util.Collections;
import java.math.BigInteger;
import static test.SimulatedAnnealing.initMatJumlah;

class SimulatedAnnealing {
	//Mendapatkan nilai-nilai dari kelas NamaKegiatan
	//Ruangan
	private static NamaKegiatan kegiatan = new NamaKegiatan();
	private static ArrayList<String> ruangan = kegiatan.getRuangan();
	private static ArrayList<String> ruanganclone = (ArrayList<String>)ruangan.clone();
	private static ArrayList<Integer> jamRuanganMulai = kegiatan.getJamMulai();
	private static ArrayList<Integer> jamRuanganSelesai = kegiatan.getJamSelesai();
	private static ArrayList<String> hariTersediaRuangan = kegiatan.getHari();
	//Jadwal
	private static ArrayList<String> namaKegiatan = kegiatan.getNamaKegiatan();
	private static ArrayList<String> ruanganKegiatan = kegiatan.getJadwalRuangan();
	private static ArrayList<Integer> jamJadwalMulai = kegiatan.getJadwalJamMulai();
	private static ArrayList<Integer> jamJadwalSelesai = kegiatan.getJadwalJamSelesai();
	private static ArrayList<Integer> durasiKegiatan = kegiatan.getJadwalDurasi();
	private static ArrayList<String> hariTersediaJadwal = kegiatan.getJadwalHari();
        public static String[][] Solusi = new String[11][5];
        public static String[][] RuanganJadwal = new String[11][5];
        private static int jumlahTabrakan;
        
	public static void initMatJumlah(int[][] M) {
        for (int i = 0; i < 11; i++) {
            for (int j = 0; j < 5; j++) {
                M[i][j] = 0;
            }
        }
    }

	public static boolean cekHariExist(int h, int[] hari) {
        for (int i=0;i<hari.length;i++) {
			if (h==hari[i]) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean cekHariExistComplex(int[] hariTersediaRuangan, int[] hariTersediaJadwal) {
		for (int i=0;i<hariTersediaJadwal.length;i++) {
			for (int j=0;j<hariTersediaRuangan.length;j++) {
				if (hariTersediaJadwal[i]==hariTersediaRuangan[j]) {
					return true;
				}
			}
		}
		return false;
	}
	
	public static void randomize(String[][] jadwal, int[][] nMatkul, String[][] ruanganJadwal) {
		initMatJumlah(nMatkul);
		//System.out.println("baa");
		
		for (int idx=0;idx<namaKegiatan.size();idx++) {	
			String matkul = namaKegiatan.get(idx);
			int jamAwal1 = jamJadwalMulai.get(idx) - 7;
			int jamAwal2 = jamJadwalSelesai.get(idx) - durasiKegiatan.get(idx) - 7;
			String[] hariString = hariTersediaJadwal.get(idx).split(",");
			int[] hariInt = new int[hariString.length];
			for (int i = 0; i < hariString.length; i++) {
				hariInt[i] = Integer.parseInt(hariString[i]) - 1;
			}
			String[][] MatriksRandomize = new String[11][5];
			for (int i=0;i<11;i++) {
				for (int j=0;j<5;j++) {
					MatriksRandomize[i][j]="";
					
					if(ruanganKegiatan.get(idx).equals("-")) {
						for (int k=0; k<ruangan.size() ; k++) {
							int jamRMulai1 = jamRuanganMulai.get(k) - 7;
							int jamRMulai2 = jamRuanganSelesai.get(k) - durasiKegiatan.get(idx) - 7;
							String[] hariString2 = hariTersediaRuangan.get(k).split(",");
							int[] hariInt2 = new int[hariString2.length];
							for (int l = 0; l < hariString2.length; l++) {
								hariInt2[l] = Integer.parseInt(hariString2[l]) - 1;
							}
							if ((i>=jamRMulai1)&&(i<=jamRMulai2)&&(jamRMulai2>0)&&(cekHariExist(j,hariInt2))&&(i>=jamAwal1)&&(i<=jamAwal2)&&(cekHariExist(j,hariInt))) {
								if (MatriksRandomize[i][j].equals("")) {
									MatriksRandomize[i][j]=MatriksRandomize[i][j]+ruangan.get(k);
								}
								else {
									MatriksRandomize[i][j]=MatriksRandomize[i][j]+";"+ruangan.get(k);
								}
							}	
							
						}
					}
					else {
						int k = ruanganclone.indexOf(ruanganKegiatan.get(idx));
						int j1 = jamRuanganMulai.get(k)-7;
						//System.out.println(k);
						//int j2 = jamRuanganSelesai.get(k) - durasiKegiatan.get(idx) - 7;
						while ((jamAwal1<j1)) {
							//System.out.println("masuk");
							k++;
							//System.out.println(k);
							j1 = jamRuanganMulai.get(k)-7;
						}
						int kfix = k;
						//System.out.println("keluar");
						String[] hariString2 = hariTersediaRuangan.get(kfix).split(",");
						int[] hariInt2 = new int[hariString2.length];
						//System.out.println("here");
						for (int l = 0; l < hariString2.length; l++) {
							hariInt2[l] = Integer.parseInt(hariString2[l]) - 1;
							//System.out.println(hariInt2[l]);
						}
						
						while (cekHariExistComplex(hariInt2,hariInt)==false) {
							kfix++;
							hariString2 = hariTersediaRuangan.get(kfix).split(",");
							hariInt2 = new int[hariString2.length];
							//System.out.println("here");
							for (int l = 0; l < hariString2.length; l++) {
								hariInt2[l] = Integer.parseInt(hariString2[l]) - 1;
								//System.out.println(hariInt2[l]);
							}
						}
						int jamRMulai1 = jamRuanganMulai.get(kfix) - 7;
						int jamRMulai2 = jamRuanganSelesai.get(kfix) - durasiKegiatan.get(idx) - 7;
						//System.out.println(jamRuanganSelesai.get(kfix));
						//System.out.println(durasiKegiatan.get(idx));
						/*
						if(i==0&&j==4) {
							System.out.println("i = "+i);
							System.out.println("j = "+j);
							System.out.println(i>=jamRMulai1);
							System.out.println(i<=jamRMulai2);
							System.out.println(jamRMulai2>0);
							System.out.println(jamRMulai2);
							System.out.println(cekHariExist(j,hariInt2));
							System.out.println(i>=jamAwal1);
							System.out.println(i<=jamAwal2);
							System.out.println(cekHariExist(j,hariInt));
						}
						*/
						if ((i>=jamRMulai1)&&(i<=jamRMulai2)&&(jamRMulai2>=0)&&(cekHariExist(j,hariInt2))&&(i>=jamAwal1)&&(i<=jamAwal2)&&(cekHariExist(j,hariInt))) {
							//System.out.println("masuk");
							if (MatriksRandomize[i][j].equals("")) {
								MatriksRandomize[i][j]=MatriksRandomize[i][j]+ruangan.get(kfix);
							}
							else {
								MatriksRandomize[i][j]=MatriksRandomize[i][j]+";"+ruangan.get(kfix);
							}
						}		
					}
					
				}
			}
			
			Random rand = new Random();
			int hari = rand.nextInt(5);
			int jam = rand.nextInt(11);
			
			while (MatriksRandomize[jam][hari].equals("")) { 
				hari = rand.nextInt(5);
				jam = rand.nextInt(11);
			}
			
			String[] ruangsplit = MatriksRandomize[jam][hari].split(";");
			int idxruang = rand.nextInt(ruangsplit.length);
			String ruang = ruangsplit[idxruang];
			
			int x;
			for (x = jam; x < (jam+durasiKegiatan.get(idx)); x++) {
				if (jadwal[x][hari] == null) {
					jadwal[x][hari] = namaKegiatan.get(idx);
				} else {
					jadwal[x][hari] = jadwal[x][hari] + ";" + namaKegiatan.get(idx);
				}
				if (ruanganJadwal[x][hari] == null) {
					ruanganJadwal[x][hari] = ruang;
				} else {
					ruanganJadwal[x][hari] = ruanganJadwal[x][hari] + ";" + ruang;
				}
				nMatkul[x][hari]++;
			}
			hari = rand.nextInt(5);
			jam = rand.nextInt(11);
			
		}	
	}

	public static void printMatriks(String[][] m) {
		for (int i = 0; i < 11; i++) {
			for (int j = 0; j < 5; j++) {
				System.out.print(m[i][j] + "|");
			}
			System.out.println();
		}
	}
	
	public static String[][] copyMatriks(String[][] m) {
		String[][] M = new String[11][5];
		for (int i = 0; i < 11; i++) {
			for (int j = 0; j < 5; j++) {
				M[i][j] = m[i][j];
			}
		}
		return M;
	}
	
	
	public static void printMatriks(int[][] m) {
		for (int i = 0; i < 11; i++) {
			for (int j = 0; j < 5; j++) {
				System.out.print(m[i][j] + "|");
			}
			System.out.println();
		}
	}
	
	public static int[][] copyMatriks(int[][] m) {
		int[][] M = new int[11][5];
		for (int i = 0; i < 11; i++) {
			for (int j = 0; j < 5; j++) {
				M[i][j] = m[i][j];
			}
		}
		return M;
	}
	
	public static boolean isUnique (String[] S) {
		int k;
		int l;
		boolean unique=true;
		for (k=0;k<S.length;k++){
			for (l=0;l<S.length;l++) {
				if ((S[k].equals(S[l]))&&(k!=l)) {
					unique=false;
				}
			}
		}
		return unique;
	}
	
	public static int hitungCost (String[][] jadwal, int[][] nMatkul, String[][] ruanganJadwal) {
		int doubleOverlap=0;
		int countOverlap=0;
		int i;
		int j;
		for (i=0;i<11;i++) {
			for (j=0;j<5;j++) {
				if (nMatkul[i][j]>1) {
					String[] ruangsplit = ruanganJadwal[i][j].split(";");
					int k;
					int l;
					for (k=0;k<ruangsplit.length;k++){
						for (l=0;l<ruangsplit.length;l++) {
							if ((ruangsplit[k].equals(ruangsplit[l]))&&(k!=l)) {
								doubleOverlap++;
							}
						}
					}
				}
			}
		}
		countOverlap = doubleOverlap/2;
		return countOverlap;	
	}
	
	//Mendapatkan posisi matkul yang overlap
	public static int getNonUniqueIdx(String[] S) {
		int k=0;
		int l=0;
		int indeks;
		boolean unique=true;
		outerloop:
		for (k=0;k<S.length;k++){
			for (l=0;l<S.length;l++) {
				if ((S[k].equals(S[l]))&&(k!=l)) {
					unique=false;
					break outerloop;
				}
			}
		}
		indeks = l;
		return indeks;
	}
	
	//Mendapatkan nama matkul yang overlap
	public static String getOverlapMatkul(String[][] jadwal, int[][] nMatkul, String[][] ruanganJadwal) {
		int i;
		int j=0;
		int indeks=0;
		outerloop:
		for (i=0;i<11;i++) {
			for (j=0;j<5;j++) {
				if (nMatkul[i][j]>1) {
					String[] ruangsplit = ruanganJadwal[i][j].split(";");
					int k;
					int l;
					boolean unique = isUnique(ruangsplit);
					if (unique==false) {
						indeks = getNonUniqueIdx(ruangsplit);
						break outerloop;
					}
				}
			}
		}
		String[] jadwalsplit = jadwal[i][j].split(";");
		return jadwalsplit[indeks];
	}
	
	public static String[][] generateNeighbor(String[][] jadwal, int[][] nMatkul, String[][] ruanganJadwal) {
		//Mengambil dan menghapus sementara salah satu matkul overlap dari jadwal
		String matkul = getOverlapMatkul(jadwal,nMatkul,ruanganJadwal);
		int idx;
		int i;
		int a; int b;
		for (a=0;a<11;a++) {
			for (b=0;b<5;b++) {
				if (jadwal[a][b]!=null) {
					ArrayList<String> temp = new ArrayList<String>();
					ArrayList<String> temp2 = new ArrayList<String>();
					String[] jadwalsplit = jadwal[a][b].split(";");
					for (i=0;i<jadwalsplit.length;i++) {
						temp.add(jadwalsplit[i]);
					}
					String[] ruangansplit = ruanganJadwal[a][b].split(";");
					for (i=0;i<ruangansplit.length;i++) {
						temp2.add(ruangansplit[i]);
					} 
					
					if(temp.contains(matkul)==true) {
						idx = temp.indexOf(matkul);
						temp.remove(matkul);
						String ruang = temp2.get(idx);
						temp2.remove(ruang);
						nMatkul[a][b]--;
					}	
					if(temp.size()>=1) {
						jadwal[a][b] = temp.get(0);
						ruanganJadwal[a][b] = temp2.get(0);
						if (temp.size()>1) {
							for (i=1;i<temp.size();i++) {
								jadwal[a][b] = jadwal[a][b]+ ";" + temp.get(i);
							}
							
							for (i=1;i<temp2.size();i++) {
								ruanganJadwal[a][b] = ruanganJadwal[a][b]+ ";" + temp2.get(i);
							}
						}
						temp.clear();
						temp2.clear();	
					}
					else {
						jadwal[a][b]=null;
						ruanganJadwal[a][b]=null;
					}		
				}
				
			}
		}
		//Generate matriks neighbor
		idx = namaKegiatan.indexOf(matkul);
		int jamAwal1 = jamJadwalMulai.get(idx) - 7;
		int jamAwal2 = jamJadwalSelesai.get(idx) - durasiKegiatan.get(idx) - 7;
		String[] hariString = hariTersediaJadwal.get(idx).split(",");
		int[] hariInt = new int[hariString.length];
		for (i = 0; i < hariString.length; i++) {
            hariInt[i] = Integer.parseInt(hariString[i]) - 1;
        }
		String[][] MatriksNeighbor = new String[11][5];
		for (i=0;i<11;i++) {
			for (int j=0;j<5;j++) {
				MatriksNeighbor[i][j]="";
				
				if(ruanganKegiatan.get(idx).equals("-")) {
					for (int k=0; k<ruangan.size() ; k++) {
						int jamRMulai1 = jamRuanganMulai.get(k) - 7;
						int jamRMulai2 = jamRuanganSelesai.get(k) - durasiKegiatan.get(idx) - 7;
						String[] hariString2 = hariTersediaRuangan.get(k).split(",");
						int[] hariInt2 = new int[hariString2.length];
						for (int l = 0; l < hariString2.length; l++) {
							hariInt2[l] = Integer.parseInt(hariString2[l]) - 1;
						}
						if ((i>=jamRMulai1)&&(i<=jamRMulai2)&&(jamRMulai2>0)&&(cekHariExist((j+1),hariInt2))&&(i>=jamAwal1)&&(i<=jamAwal2)&&(cekHariExist((j+1),hariInt))) {
							if (MatriksNeighbor[i][j].equals("")) {
								MatriksNeighbor[i][j]=MatriksNeighbor[i][j]+ruangan.get(k);
							}
							else {
								MatriksNeighbor[i][j]=MatriksNeighbor[i][j]+";"+ruangan.get(k);
							}
						}	
					}
				}
				else {
					int k = ruanganclone.indexOf(ruanganKegiatan.get(idx));
					int j1 = jamRuanganMulai.get(k)-7;
					//System.out.println(k);
					//int j2 = jamRuanganSelesai.get(k) - durasiKegiatan.get(idx) - 7;
					while ((jamAwal1<j1)) {
						//System.out.println("masuk");
						k++;
						//System.out.println(k);
						j1 = jamRuanganMulai.get(k)-7;
					}
					int kfix = k;
					//System.out.println("keluar");
					String[] hariString2 = hariTersediaRuangan.get(kfix).split(",");
					int[] hariInt2 = new int[hariString2.length];
					//System.out.println("here");
					for (int l = 0; l < hariString2.length; l++) {
						hariInt2[l] = Integer.parseInt(hariString2[l]) - 1;
						//System.out.println(hariInt2[l]);
					}
					
					while (cekHariExistComplex(hariInt2,hariInt)==false) {
						kfix++;
						hariString2 = hariTersediaRuangan.get(kfix).split(",");
						hariInt2 = new int[hariString2.length];
						//System.out.println("here");
						for (int l = 0; l < hariString2.length; l++) {
							hariInt2[l] = Integer.parseInt(hariString2[l]) - 1;
							//System.out.println(hariInt2[l]);
						}
					}
					int jamRMulai1 = jamRuanganMulai.get(kfix) - 7;
					int jamRMulai2 = jamRuanganSelesai.get(kfix) - durasiKegiatan.get(idx) - 7;
					//System.out.println(jamRuanganSelesai.get(kfix));
					//System.out.println(durasiKegiatan.get(idx));
					/*
					if(i==0&&j==4) {
						System.out.println("i = "+i);
						System.out.println("j = "+j);
						System.out.println(i>=jamRMulai1);
						System.out.println(i<=jamRMulai2);
						System.out.println(jamRMulai2>0);
						System.out.println(jamRMulai2);
						System.out.println(cekHariExist(j,hariInt2));
						System.out.println(i>=jamAwal1);
						System.out.println(i<=jamAwal2);
						System.out.println(cekHariExist(j,hariInt));
					}
					*/
					if ((i>=jamRMulai1)&&(i<=jamRMulai2)&&(jamRMulai2>=0)&&(cekHariExist(j,hariInt2))&&(i>=jamAwal1)&&(i<=jamAwal2)&&(cekHariExist(j,hariInt))) {
						//System.out.println("masuk");
						if (MatriksNeighbor[i][j].equals("")) {
							MatriksNeighbor[i][j]=MatriksNeighbor[i][j]+ruangan.get(kfix);
						}
						else {
							MatriksNeighbor[i][j]=MatriksNeighbor[i][j]+";"+ruangan.get(kfix);
						}
					}			
				}
				
			}
		}
		return MatriksNeighbor;
	}
	
	//Memasukkan kembali matkul yang overlap ke matriks
	public static void getNeighbortoMatriks(String neighbors, String[][] nextJadwal, String[][]nextRuanganJadwal, int[][] nextNMatkul, int i, int j, String matkulOverlap) {
		int idx = namaKegiatan.indexOf(matkulOverlap);
		int durasi = durasiKegiatan.get(idx)+i;
		for (int l=i; l<durasi; l++) {
			if (nextJadwal[l][j]!=null) {
				nextJadwal[l][j] = matkulOverlap + ";"+ nextJadwal[i][j];
			}
			else {
				nextJadwal[l][j] = matkulOverlap;
			}
			if (nextRuanganJadwal[l][j]!=null) {
				nextRuanganJadwal[l][j] = neighbors + ";"+ nextRuanganJadwal[i][j];
			}
			else {
				nextRuanganJadwal[l][j] = neighbors;
			}
			nextNMatkul[l][j]++;	
		}	
	}
	
	public static double acceptanceProbability(int currentCost, int nextCost, double temperature) {
        if (nextCost < currentCost) {
            return 1.0;
        }
        return Math.exp((currentCost - nextCost) / temperature);
    }
	
	public static void SA () {
		int hillstep = 0;
		String[][] jadwal = new String[11][5];
		String[][] ruanganJadwal = new String[11][5];
		int[][] nMatkul = new int[11][5];
		double temperature= 10000;
		double coolingRate=0.005;
		String[][] bestSolution = new String[11][5];
		//System.out.println("aaa");
		
		randomize(jadwal, nMatkul, ruanganJadwal);
		//System.out.println("aab");
		
		printMatriks(jadwal);
		System.out.println();
		printMatriks(ruanganJadwal);
		System.out.println();
		printMatriks(nMatkul);
		int currentCost = hitungCost(jadwal,nMatkul,ruanganJadwal);
		System.out.println("Cost = "+currentCost);
		
		String[][] bestJadwal = new String[11][5];
		String[][] bestRuanganJadwal = new String[11][5];
		int[][] bestNMatkul = new int[11][5];
		
		bestJadwal = copyMatriks(jadwal);
		bestRuanganJadwal = copyMatriks(ruanganJadwal);
		bestNMatkul = copyMatriks(nMatkul);
		
		String[][] nextJadwal = new String[11][5];
		String[][] nextRuanganJadwal = new String[11][5];
		int[][] nextNMatkul = new int[11][5];
		
		int bestCost = hitungCost(bestJadwal, bestNMatkul, bestRuanganJadwal);
		
		boolean breakouter1 = false;
		boolean breakouter2 = false;
		while (bestCost!=0) {
			//bestCost = hitungCost(bestJadwal, bestNMatkul, bestRuanganJadwal);
			
			if (temperature>1) {
				jadwal = copyMatriks(bestJadwal);
				nMatkul = copyMatriks(bestNMatkul);
				ruanganJadwal = copyMatriks(bestRuanganJadwal);
				String matkulOverlap = getOverlapMatkul(jadwal,nMatkul,ruanganJadwal);
				String[][] neighbors = generateNeighbor(jadwal,nMatkul,ruanganJadwal);
				nextJadwal = copyMatriks(jadwal);
				nextRuanganJadwal = copyMatriks(ruanganJadwal);
				nextNMatkul = copyMatriks(nMatkul);
				String[][] temp1 = new String[11][5];
				String[][] temp2 = new String[11][5];
				int[][] temp3 = new int[11][5];
				
				temp1=copyMatriks(jadwal);
				temp2=copyMatriks(ruanganJadwal);
				temp3=copyMatriks(nMatkul);
				int i; int j;
				for (i=0;i<11;i++) {
					if (breakouter1) break;
					for (j=0;j<5;j++) {
						if (breakouter2) break;
						if (neighbors[i][j].equals("")==false) {
							String[] neighborssplit = neighbors[i][j].split(";");
							for (int k=0; k<neighborssplit.length;k++) {
								getNeighbortoMatriks(neighborssplit[k],nextJadwal, nextRuanganJadwal,nextNMatkul,i,j,matkulOverlap);
								int nextCost = hitungCost(nextJadwal,nextNMatkul,nextRuanganJadwal);
								
								if (acceptanceProbability(currentCost,nextCost,temperature)>Math.random()) {
									jadwal=copyMatriks(nextJadwal);
									ruanganJadwal=copyMatriks(nextRuanganJadwal);
									nMatkul = copyMatriks(nextNMatkul);
									currentCost = nextCost;
								}	
								if(bestCost>currentCost) {
									bestJadwal = copyMatriks(jadwal);
									bestRuanganJadwal = copyMatriks(ruanganJadwal);
									bestNMatkul = copyMatriks(nMatkul);
									bestCost = currentCost;
								}
								nextJadwal = copyMatriks(temp1);
								nextRuanganJadwal = copyMatriks(temp2);
								nextNMatkul = copyMatriks(temp3);
								
								if (bestCost==0) {
									breakouter2=true;
									breakouter1=true;
									break;
								}
								//if (temperature>1) {
									temperature *= 1-coolingRate;
								//}
								
							}
						}
					}
				}
			}
			
			else { //stochastic hill climbing
				Random rand = new Random();
				jadwal = copyMatriks(bestJadwal);
				nMatkul = copyMatriks(bestNMatkul);
				ruanganJadwal = copyMatriks(bestRuanganJadwal);
				String matkulOverlap = getOverlapMatkul(jadwal,nMatkul,ruanganJadwal);
				String[][] neighbors = generateNeighbor(jadwal,nMatkul,ruanganJadwal);
				nextJadwal = copyMatriks(jadwal);
				nextRuanganJadwal = copyMatriks(ruanganJadwal);
				nextNMatkul = copyMatriks(nMatkul);
				String[][] temp1 = new String[11][5];
				String[][] temp2 = new String[11][5];
				int[][] temp3 = new int[11][5];
				
				temp1=copyMatriks(jadwal);
				temp2=copyMatriks(ruanganJadwal);
				temp3=copyMatriks(nMatkul);
				int i; int j;
				i = rand.nextInt(11);
				j = rand.nextInt(5);
				if (neighbors[i][j].equals("")==false) {
					String[] neighborssplit = neighbors[i][j].split(";");
					int k = rand.nextInt(neighborssplit.length);
					getNeighbortoMatriks(neighborssplit[k],nextJadwal, nextRuanganJadwal,nextNMatkul,i,j,matkulOverlap);
					int nextCost = hitungCost(nextJadwal,nextNMatkul,nextRuanganJadwal);
					
					if (currentCost>nextCost) {
						jadwal=copyMatriks(nextJadwal);
						ruanganJadwal=copyMatriks(nextRuanganJadwal);
						nMatkul = copyMatriks(nextNMatkul);
						currentCost = nextCost;
					}	
					if(bestCost>currentCost) {
						bestJadwal = copyMatriks(jadwal);
						bestRuanganJadwal = copyMatriks(ruanganJadwal);
						bestNMatkul = copyMatriks(nMatkul);
						bestCost = currentCost;
					}
					nextJadwal = copyMatriks(temp1);
					nextRuanganJadwal = copyMatriks(temp2);
					nextNMatkul = copyMatriks(temp3);
				}
				hillstep++;
				if (hillstep==10000) {
					break;
				}		
			}
				
		}
		System.out.println();
		System.out.println("Best Solution");
		printMatriks(bestJadwal);
                Solusi = copyMatriks(bestJadwal);
                RuanganJadwal = copyMatriks(bestRuanganJadwal);
		System.out.println();
		printMatriks(bestRuanganJadwal);
		System.out.println();
		printMatriks(bestNMatkul);
		System.out.println("Cost = "+bestCost);
	}
        public float[] countPresentaseKelas(String[][] ruanganJadwal) {
        float[] arrPresentase = new float[ruangan.size()];
        int[] arrCount = new int[ruangan.size()];
        int[] arrJamMatkulTersedia = new int[ruangan.size()];
        for (int i = 0; i < arrCount.length; i++) {
            arrCount[i] = 0;
            arrJamMatkulTersedia[i] = 0;
        }
        
        for (int i = 0; i < 11; i++) {
            for (int j = 0; j < 5; j++) {
                if (ruanganJadwal[i][j]!= null) {
                    if (ruanganJadwal[i][j].length() > 6) {
                        String[] matkul = ruanganJadwal[i][j].split(";");
                        for (int k = 0; k < matkul.length; k++) {
                            arrCount[ruangan.indexOf(matkul[k])]++;
                        }
                    } else {
                        arrCount[ruangan.indexOf(ruanganJadwal[i][j])]++;
                    }
                }
            }
        }
        
        for (int i = 0; i < arrCount.length; i++) {
            String[] hari = hariTersediaRuangan.get(i).split(",");
            arrJamMatkulTersedia[i] = (jamRuanganSelesai.get(i)
                    - jamRuanganMulai.get(i)) * hari.length;
            arrPresentase[i] = ((float)arrCount[i] / (float)arrJamMatkulTersedia[i]) * 100;
        }
        return arrPresentase;
    }
        
    public ArrayList<String> getRuangan() {
        return ruangan;
    }
    public static String[][] getSolusi() {
        return Solusi;
    }

    public static String[][] getSolusiRuangan() {
        return RuanganJadwal;
    }
     public int getCountTabrakan() {
        return jumlahTabrakan;
    }
}




