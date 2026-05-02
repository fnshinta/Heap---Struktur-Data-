import java.io.*;
import java.util.*;

public class HeapProgram {

    static final String CSV_PATH = "data100.csv";

    // ============================================================
    //  MODEL DATA
    // ============================================================

    static class DataItem {
        int id;
        String nama;

        DataItem(int id, String nama) {
            this.id   = id;
            this.nama = nama;
        }
    }

    // ============================================================
    //  MIN-HEAP
    // ============================================================

    static void minHeapPush(List<DataItem> heap, DataItem item) {
        heap.add(item);
        siftUpMin(heap, heap.size() - 1);
    }

    static DataItem minHeapPop(List<DataItem> heap) {
        if (heap.isEmpty()) return null;
        Collections.swap(heap, 0, heap.size() - 1);
        DataItem item = heap.remove(heap.size() - 1);
        if (!heap.isEmpty()) siftDownMin(heap, 0);
        return item;
    }

    static boolean minHeapRemove(List<DataItem> heap, int targetId) {
        int idx = -1;
        for (int i = 0; i < heap.size(); i++) {
            if (heap.get(i).id == targetId) {
                idx = i;
                break;
            }
        }
        if (idx == -1) return false;
        heap.set(idx, heap.get(heap.size() - 1));
        heap.remove(heap.size() - 1);
        if (idx < heap.size()) {
            siftUpMin(heap, idx);
            siftDownMin(heap, idx);
        }
        return true;
    }

    static void siftUpMin(List<DataItem> heap, int i) {
        while (i > 0) {
            int parent = (i - 1) / 2;
            if (heap.get(i).id < heap.get(parent).id) {
                Collections.swap(heap, i, parent);
                i = parent;
            } else break;
        }
    }

    static void siftDownMin(List<DataItem> heap, int i) {
        int n = heap.size();
        while (true) {
            int smallest = i;
            int left  = 2 * i + 1;
            int right = 2 * i + 2;
            if (left  < n && heap.get(left).id  < heap.get(smallest).id) smallest = left;
            if (right < n && heap.get(right).id < heap.get(smallest).id) smallest = right;
            if (smallest != i) {
                Collections.swap(heap, i, smallest);
                i = smallest;
            } else break;
        }
    }

    // ============================================================
    //  MAX-HEAP
    // ============================================================

    static void maxHeapPush(List<DataItem> heap, DataItem item) {
        heap.add(item);
        siftUpMax(heap, heap.size() - 1);
    }

    static DataItem maxHeapPop(List<DataItem> heap) {
        if (heap.isEmpty()) return null;
        Collections.swap(heap, 0, heap.size() - 1);
        DataItem item = heap.remove(heap.size() - 1);
        if (!heap.isEmpty()) siftDownMax(heap, 0);
        return item;
    }

    static boolean maxHeapRemove(List<DataItem> heap, int targetId) {
        int idx = -1;
        for (int i = 0; i < heap.size(); i++) {
            if (heap.get(i).id == targetId) {
                idx = i;
                break;
            }
        }
        if (idx == -1) return false;
        heap.set(idx, heap.get(heap.size() - 1));
        heap.remove(heap.size() - 1);
        if (idx < heap.size()) {
            siftUpMax(heap, idx);
            siftDownMax(heap, idx);
        }
        return true;
    }

    static void siftUpMax(List<DataItem> heap, int i) {
        while (i > 0) {
            int parent = (i - 1) / 2;
            if (heap.get(i).id > heap.get(parent).id) {
                Collections.swap(heap, i, parent);
                i = parent;
            } else break;
        }
    }

    static void siftDownMax(List<DataItem> heap, int i) {
        int n = heap.size();
        while (true) {
            int largest = i;
            int left  = 2 * i + 1;
            int right = 2 * i + 2;
            if (left  < n && heap.get(left).id  > heap.get(largest).id) largest = left;
            if (right < n && heap.get(right).id > heap.get(largest).id) largest = right;
            if (largest != i) {
                Collections.swap(heap, i, largest);
                i = largest;
            } else break;
        }
    }

    // ============================================================
    //  BACA DATA DARI CSV
    // ============================================================

    static List<DataItem> bacaCSV() {
        List<DataItem> data = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(CSV_PATH), "UTF-8"))) {

            String header = br.readLine(); // skip header
            if (header == null) return data;

            // Tentukan index kolom 'id' dan 'nama' dari header
            String[] cols = header.split(",");
            int idxId   = -1;
            int idxNama = -1;
            for (int i = 0; i < cols.length; i++) {
                String col = cols[i].trim().toLowerCase();
                if (col.equals("id"))   idxId   = i;
                if (col.equals("nama")) idxNama = i;
            }

            if (idxId == -1 || idxNama == -1) {
                System.out.println("  [ERROR] Kolom 'id' atau 'nama' tidak ditemukan di CSV.");
                return data;
            }

            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(",", -1);
                if (parts.length <= Math.max(idxId, idxNama)) continue;
                try {
                    int    id   = Integer.parseInt(parts[idxId].trim());
                    String nama = parts[idxNama].trim();
                    data.add(new DataItem(id, nama));
                } catch (NumberFormatException e) {
                    // lewati baris yang ID-nya bukan angka
                }
            }
        } catch (IOException e) {
            System.out.println("  [ERROR] Gagal membaca file CSV: " + e.getMessage());
        }
        return data;
    }

    // ============================================================
    //  TAMPILAN
    // ============================================================

    static void tampilkanDataAsli(List<DataItem> data) {
        System.out.println("=".repeat(55));
        System.out.println("  DATA DARI CSV (urutan asli)");
        System.out.println("=".repeat(55));
        System.out.printf("%-5s %-8s %s%n", "No", "ID", "Nama");
        System.out.println("-".repeat(55));
        int no = 1;
        for (DataItem d : data) {
            System.out.printf("%-5d %-8d %s%n", no++, d.id, d.nama);
        }
        System.out.println();
    }

    static void tampilkanAscendingMinHeap(List<DataItem> heapData) {
        System.out.println();
        System.out.println("=".repeat(55));
        System.out.println("  MIN-HEAP \u2014 Urut ID Ascending (Terkecil \u2192 Terbesar)");
        System.out.println("=".repeat(55));
        List<DataItem> temp = new ArrayList<>(heapData);
        System.out.printf("%-5s %-8s %s%n", "No", "ID", "Nama");
        System.out.println("-".repeat(55));
        int no = 1;
        while (!temp.isEmpty()) {
            DataItem d = minHeapPop(temp);
            System.out.printf("%-5d %-8d %s%n", no++, d.id, d.nama);
        }
        System.out.println();
    }

    static void tampilkanDescendingMaxHeap(List<DataItem> heapData) {
        System.out.println();
        System.out.println("=".repeat(55));
        System.out.println("  MAX-HEAP \u2014 Urut ID Descending (Terbesar \u2192 Terkecil)");
        System.out.println("=".repeat(55));
        List<DataItem> temp = new ArrayList<>(heapData);
        System.out.printf("%-5s %-8s %s%n", "No", "ID", "Nama");
        System.out.println("-".repeat(55));
        int no = 1;
        while (!temp.isEmpty()) {
            DataItem d = maxHeapPop(temp);
            System.out.printf("%-5d %-8d %s%n", no++, d.id, d.nama);
        }
        System.out.println();
    }

    // ============================================================
    //  FITUR TAMBAH DATA
    // ============================================================

    static void tambahData(List<DataItem> minHeap, List<DataItem> maxHeap, Scanner sc) {
        System.out.println();
        System.out.println("=".repeat(55));
        System.out.println("  TAMBAH DATA BARU");
        System.out.println("=".repeat(55));

        System.out.print("  Masukkan ID baru : ");
        int idBaru;
        try {
            idBaru = Integer.parseInt(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("\n  [ERROR] ID harus berupa angka!\n");
            return;
        }

        // Cek duplikat
        for (DataItem d : minHeap) {
            if (d.id == idBaru) {
                System.out.println("\n  [ERROR] ID " + idBaru + " sudah ada dalam heap!\n");
                return;
            }
        }

        System.out.print("  Masukkan Nama    : ");
        String namaBaru = sc.nextLine().trim();
        if (namaBaru.isEmpty()) {
            System.out.println("\n  [ERROR] Nama tidak boleh kosong!\n");
            return;
        }

        DataItem item = new DataItem(idBaru, namaBaru);
        minHeapPush(minHeap, item);
        maxHeapPush(maxHeap, item);
        System.out.println("\n  [OK] Data (ID=" + idBaru + ", Nama=" + namaBaru + ") berhasil ditambahkan");
        System.out.println("       ke Min-Heap dan Max-Heap.\n");
    }

    // ============================================================
    //  FITUR HAPUS DATA
    // ============================================================

    static void hapusDariMinHeap(List<DataItem> minHeap, Scanner sc) {
        System.out.println();
        System.out.println("=".repeat(55));
        System.out.println("  HAPUS DATA DARI MIN-HEAP");
        System.out.println("=".repeat(55));

        System.out.print("  Masukkan ID yang ingin dihapus: ");
        int targetId;
        try {
            targetId = Integer.parseInt(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("\n  [ERROR] ID harus berupa angka!\n");
            return;
        }

        if (minHeapRemove(minHeap, targetId)) {
            System.out.println("\n  [OK] Data dengan ID=" + targetId + " berhasil dihapus dari Min-Heap.\n");
        } else {
            System.out.println("\n  [ERROR] ID=" + targetId + " tidak ditemukan dalam Min-Heap.\n");
        }
    }

    static void hapusDariMaxHeap(List<DataItem> maxHeap, Scanner sc) {
        System.out.println();
        System.out.println("=".repeat(55));
        System.out.println("  HAPUS DATA DARI MAX-HEAP");
        System.out.println("=".repeat(55));

        System.out.print("  Masukkan ID yang ingin dihapus: ");
        int targetId;
        try {
            targetId = Integer.parseInt(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("\n  [ERROR] ID harus berupa angka!\n");
            return;
        }

        if (maxHeapRemove(maxHeap, targetId)) {
            System.out.println("\n  [OK] Data dengan ID=" + targetId + " berhasil dihapus dari Max-Heap.\n");
        } else {
            System.out.println("\n  [ERROR] ID=" + targetId + " tidak ditemukan dalam Max-Heap.\n");
        }
    }

    // ============================================================
    //  FITUR MUAT DATA DARI CSV (Pilihan 6)
    // ============================================================

    static void muatDataCSV(List<DataItem> minHeap, List<DataItem> maxHeap,
                             List<DataItem> data, Scanner sc) {
        System.out.println();
        System.out.println("=".repeat(55));
        System.out.println("  MUAT DATA AWAL DARI CSV");
        System.out.println("=".repeat(55));

        if (!minHeap.isEmpty() || !maxHeap.isEmpty()) {
            System.out.print("  Heap sudah berisi data. Timpa dengan data CSV? (y/n): ");
            String konfirmasi = sc.nextLine().trim().toLowerCase();
            if (!konfirmasi.equals("y")) {
                System.out.println("\n  [INFO] Pemuatan data dibatalkan.\n");
                return;
            }
        }

        minHeap.clear();
        maxHeap.clear();
        data.clear();
        data.addAll(bacaCSV());

        for (DataItem item : data) {
            minHeapPush(minHeap, item);
            maxHeapPush(maxHeap, item);
        }

        System.out.println("\n  [OK] " + data.size() + " data dari '" + CSV_PATH + "' berhasil dimuat");
        System.out.println("       ke dalam Min-Heap dan Max-Heap.\n");
        tampilkanDataAsli(data);
    }

    // ============================================================
    //  MAIN
    // ============================================================

    public static void main(String[] args) {
        System.out.println("\n>>> PROGRAM STRUKTUR DATA HEAP <<<\n");

        List<DataItem> minHeap = new ArrayList<>();
        List<DataItem> maxHeap = new ArrayList<>();
        List<DataItem> data    = new ArrayList<>();
        Scanner sc = new Scanner(System.in);

        // Auto-load data CSV saat program pertama kali dijalankan
        data.addAll(bacaCSV());
        for (DataItem item : data) {
            minHeapPush(minHeap, item);
            maxHeapPush(maxHeap, item);
        }
        System.out.println("  [INFO] " + data.size() + " data dari '" + CSV_PATH + "' berhasil dimuat ke Min-Heap dan Max-Heap.\n");

        while (true) {
            System.out.println("=".repeat(55));
            System.out.println("  MENU");
            System.out.println("=".repeat(55));
            System.out.println("  1. Tambah data (ke Min-Heap & Max-Heap)");
            System.out.println("  2. Tampilkan urut ID Ascending  (Min-Heap)");
            System.out.println("  3. Tampilkan urut ID Descending (Max-Heap)");
            System.out.println("  4. Hapus data dari Min-Heap");
            System.out.println("  5. Hapus data dari Max-Heap");
            System.out.println("  6. Muat data awal dari CSV ke Min-Heap & Max-Heap");
            System.out.println("  7. End");
            System.out.println("-".repeat(55));
            System.out.print("  Masukkan pilihan (1-7): ");
            String pilihan = sc.nextLine().trim();

            switch (pilihan) {
                case "1" -> tambahData(minHeap, maxHeap, sc);
                case "2" -> tampilkanAscendingMinHeap(minHeap);
                case "3" -> tampilkanDescendingMaxHeap(maxHeap);
                case "4" -> hapusDariMinHeap(minHeap, sc);
                case "5" -> hapusDariMaxHeap(maxHeap, sc);
                case "6" -> muatDataCSV(minHeap, maxHeap, data, sc);
                case "7" -> {
                    System.out.println("\n  Program selesai. Terima kasih!\n");
                    sc.close();
                    return;
                }
                default -> System.out.println("\n  Pilihan tidak valid! Masukkan angka 1 sampai 7.\n");
            }
        }
    }
}
