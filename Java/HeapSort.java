import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class HeapSort {

    //  BACA DATA DARI FILE CSV
    static final String CSV_PATH = "data100.csv";

    static List<Integer> idList = new ArrayList<>();
    static List<String> namaList = new ArrayList<>();

    static void bacaCSV() throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                new FileInputStream(CSV_PATH), StandardCharsets.UTF_8))) {
            br.readLine(); // skip header
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",", 2);
                if (parts.length < 2) continue;
                int id = Integer.parseInt(parts[0].trim());
                String nama = parts[1].trim();
                idList.add(id);
                namaList.add(nama);
            }
        }
    }

    //  MIN-HEAP
    static void minHeapPush(List<Integer> heapIds, List<String> heapNamas, int id, String nama) {
        heapIds.add(id);
        heapNamas.add(nama);
        siftUpMin(heapIds, heapNamas, heapIds.size() - 1);
    }

    static Object[] minHeapPopObj(List<Integer> heapIds, List<String> heapNamas) {
        swap(heapIds, heapNamas, 0, heapIds.size() - 1);
        int id = heapIds.remove(heapIds.size() - 1);
        String nama = heapNamas.remove(heapNamas.size() - 1);
        if (!heapIds.isEmpty()) siftDownMin(heapIds, heapNamas, 0);
        return new Object[]{id, nama};
    }

    static void siftUpMin(List<Integer> heapIds, List<String> heapNamas, int i) {
        while (i > 0) {
            int parent = (i - 1) / 2;
            if (heapIds.get(i) < heapIds.get(parent)) {
                swap(heapIds, heapNamas, i, parent);
                i = parent;
            } else break;
        }
    }

    static void siftDownMin(List<Integer> heapIds, List<String> heapNamas, int i) {
        int n = heapIds.size();
        while (true) {
            int smallest = i;
            int left  = 2 * i + 1;
            int right = 2 * i + 2;
            if (left  < n && heapIds.get(left)  < heapIds.get(smallest)) smallest = left;
            if (right < n && heapIds.get(right) < heapIds.get(smallest)) smallest = right;
            if (smallest != i) {
                swap(heapIds, heapNamas, i, smallest);
                i = smallest;
            } else break;
        }
    }

    //  MAX-HEAP
    static void maxHeapPush(List<Integer> heapIds, List<String> heapNamas, int id, String nama) {
        heapIds.add(id);
        heapNamas.add(nama);
        siftUpMax(heapIds, heapNamas, heapIds.size() - 1);
    }

    static Object[] maxHeapPopObj(List<Integer> heapIds, List<String> heapNamas) {
        swap(heapIds, heapNamas, 0, heapIds.size() - 1);
        int id = heapIds.remove(heapIds.size() - 1);
        String nama = heapNamas.remove(heapNamas.size() - 1);
        if (!heapIds.isEmpty()) siftDownMax(heapIds, heapNamas, 0);
        return new Object[]{id, nama};
    }

    static void siftUpMax(List<Integer> heapIds, List<String> heapNamas, int i) {
        while (i > 0) {
            int parent = (i - 1) / 2;
            if (heapIds.get(i) > heapIds.get(parent)) {
                swap(heapIds, heapNamas, i, parent);
                i = parent;
            } else break;
        }
    }

    static void siftDownMax(List<Integer> heapIds, List<String> heapNamas, int i) {
        int n = heapIds.size();
        while (true) {
            int largest = i;
            int left  = 2 * i + 1;
            int right = 2 * i + 2;
            if (left  < n && heapIds.get(left)  > heapIds.get(largest)) largest = left;
            if (right < n && heapIds.get(right) > heapIds.get(largest)) largest = right;
            if (largest != i) {
                swap(heapIds, heapNamas, i, largest);
                i = largest;
            } else break;
        }
    }

    //  HELPER: SWAP
    static void swap(List<Integer> ids, List<String> namas, int a, int b) {
        int tmpId = ids.get(a);
        ids.set(a, ids.get(b));
        ids.set(b, tmpId);

        String tmpNama = namas.get(a);
        namas.set(a, namas.get(b));
        namas.set(b, tmpNama);
    }

    //  TAMPILAN
    static void tampilkanDataAsli() {
        System.out.println("=".repeat(55));
        System.out.println("  DATA DARI CSV (urutan asli)");
        System.out.println("=".repeat(55));
        System.out.printf("%-5s %-8s %s%n", "No", "ID", "Nama");
        System.out.println("-".repeat(55));
        for (int i = 0; i < idList.size(); i++) {
            System.out.printf("%-5d %-8d %s%n", i + 1, idList.get(i), namaList.get(i));
        }
        System.out.println();
    }

    static void tampilkanAscendingMinHeap() {
        System.out.println();
        System.out.println("=".repeat(55));
        System.out.println("  MIN-HEAP — Urut ID Ascending (Terkecil → Terbesar)");
        System.out.println("=".repeat(55));

        List<Integer> heapIds  = new ArrayList<>();
        List<String>  heapNamas = new ArrayList<>();
        for (int i = 0; i < idList.size(); i++) {
            minHeapPush(heapIds, heapNamas, idList.get(i), namaList.get(i));
        }

        System.out.printf("%-5s %-8s %s%n", "No", "ID", "Nama");
        System.out.println("-".repeat(55));
        int nomor = 1;
        while (!heapIds.isEmpty()) {
            Object[] item = minHeapPopObj(heapIds, heapNamas);
            System.out.printf("%-5d %-8d %s%n", nomor++, item[0], item[1]);
        }
        System.out.println();
    }

    static void tampilkanDescendingMaxHeap() {
        System.out.println();
        System.out.println("=".repeat(55));
        System.out.println("  MAX-HEAP — Urut ID Descending (Terbesar → Terkecil)");
        System.out.println("=".repeat(55));

        List<Integer> heapIds   = new ArrayList<>();
        List<String>  heapNamas = new ArrayList<>();
        for (int i = 0; i < idList.size(); i++) {
            maxHeapPush(heapIds, heapNamas, idList.get(i), namaList.get(i));
        }

        System.out.printf("%-5s %-8s %s%n", "No", "ID", "Nama");
        System.out.println("-".repeat(55));
        int nomor = 1;
        while (!heapIds.isEmpty()) {
            Object[] item = maxHeapPopObj(heapIds, heapNamas);
            System.out.printf("%-5d %-8d %s%n", nomor++, item[0], item[1]);
        }
        System.out.println();
    }

    //  MAIN
    public static void main(String[] args) throws IOException {
        bacaCSV();

        System.out.println("\n>>> PROGRAM STRUKTUR DATA HEAP <<<");
        System.out.println("    Total data : " + idList.size() + " item\n");

        tampilkanDataAsli();

        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("=".repeat(55));
            System.out.println("  MENU");
            System.out.println("=".repeat(55));
            System.out.println("  1. Tampilkan urut ID Ascending  (Min-Heap)");
            System.out.println("  2. Tampilkan urut ID Descending (Max-Heap)");
            System.out.println("  3. End");
            System.out.println("-".repeat(55));
            System.out.print("  Masukkan pilihan (1/2/3): ");
            String pilihan = scanner.nextLine().trim();

            switch (pilihan) {
                case "1" -> tampilkanAscendingMinHeap();
                case "2" -> tampilkanDescendingMaxHeap();
                case "3" -> {
                    System.out.println("\n  Program selesai. Terima kasih!\n");
                    scanner.close();
                    return;
                }
                default -> System.out.println("\n  Pilihan tidak valid! Masukkan angka 1, 2, atau 3.\n");
            }
        }
    }
}
