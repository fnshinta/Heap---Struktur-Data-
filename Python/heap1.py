import csv

# ============================================================
#  BACA DATA DARI FILE CSV
# ============================================================
CSV_PATH = "data100.csv"

data = []
with open(CSV_PATH, newline='', encoding='utf-8') as f:
    reader = csv.DictReader(f)
    for row in reader:
        data.append((int(row['id']), row['nama']))

# ============================================================
# --- MIN-HEAP ---
# ============================================================

def min_heap_push(heap, item):
    heap.append(item)
    _sift_up_min(heap, len(heap) - 1)

def min_heap_pop(heap):
    if not heap:
        return None
    heap[0], heap[-1] = heap[-1], heap[0]
    item = heap.pop()
    if heap:
        _sift_down_min(heap, 0)
    return item

def min_heap_remove(heap, target_id):
    """Hapus elemen dengan id tertentu dari min-heap."""
    idx = None
    for i, (id_, _) in enumerate(heap):
        if id_ == target_id:
            idx = i
            break
    if idx is None:
        return False
    heap[idx] = heap[-1]
    heap.pop()
    if idx < len(heap):
        _sift_up_min(heap, idx)
        _sift_down_min(heap, idx)
    return True

def _sift_up_min(heap, i):
    while i > 0:
        parent = (i - 1) // 2
        if heap[i][0] < heap[parent][0]:
            heap[i], heap[parent] = heap[parent], heap[i]
            i = parent
        else:
            break

def _sift_down_min(heap, i):
    n = len(heap)
    while True:
        smallest = i
        left  = 2 * i + 1
        right = 2 * i + 2
        if left < n and heap[left][0] < heap[smallest][0]:
            smallest = left
        if right < n and heap[right][0] < heap[smallest][0]:
            smallest = right
        if smallest != i:
            heap[i], heap[smallest] = heap[smallest], heap[i]
            i = smallest
        else:
            break

# ============================================================
# --- MAX-HEAP ---
# ============================================================

def max_heap_push(heap, item):
    heap.append(item)
    _sift_up_max(heap, len(heap) - 1)

def max_heap_pop(heap):
    if not heap:
        return None
    heap[0], heap[-1] = heap[-1], heap[0]
    item = heap.pop()
    if heap:
        _sift_down_max(heap, 0)
    return item

def max_heap_remove(heap, target_id):
    """Hapus elemen dengan id tertentu dari max-heap."""
    idx = None
    for i, (id_, _) in enumerate(heap):
        if id_ == target_id:
            idx = i
            break
    if idx is None:
        return False
    heap[idx] = heap[-1]
    heap.pop()
    if idx < len(heap):
        _sift_up_max(heap, idx)
        _sift_down_max(heap, idx)
    return True

def _sift_up_max(heap, i):
    while i > 0:
        parent = (i - 1) // 2
        if heap[i][0] > heap[parent][0]:
            heap[i], heap[parent] = heap[parent], heap[i]
            i = parent
        else:
            break

def _sift_down_max(heap, i):
    n = len(heap)
    while True:
        largest = i
        left  = 2 * i + 1
        right = 2 * i + 2
        if left < n and heap[left][0] > heap[largest][0]:
            largest = left
        if right < n and heap[right][0] > heap[largest][0]:
            largest = right
        if largest != i:
            heap[i], heap[largest] = heap[largest], heap[i]
            i = largest
        else:
            break

# ============================================================
#  TAMPILAN
# ============================================================

def tampilkan_data_asli(data):
    print("=" * 55)
    print("  DATA DARI CSV (urutan asli)")
    print("=" * 55)
    print(f"{'No':<5} {'ID':<8} {'Nama'}")
    print("-" * 55)
    for i, (id_, nama) in enumerate(data, 1):
        print(f"{i:<5} {id_:<8} {nama}")
    print()

def tampilkan_ascending_min_heap(heap_data):
    print()
    print("=" * 55)
    print("  MIN-HEAP — Urut ID Ascending (Terkecil → Terbesar)")
    print("=" * 55)
    # Salin heap agar data asli tidak rusak
    temp_heap = heap_data[:]
    print(f"{'No':<5} {'ID':<8} {'Nama'}")
    print("-" * 55)
    nomor = 1
    while temp_heap:
        id_, nama = min_heap_pop(temp_heap)
        print(f"{nomor:<5} {id_:<8} {nama}")
        nomor += 1
    print()

def tampilkan_descending_max_heap(heap_data):
    print()
    print("=" * 55)
    print("  MAX-HEAP — Urut ID Descending (Terbesar → Terkecil)")
    print("=" * 55)
    # Salin heap agar data asli tidak rusak
    temp_heap = heap_data[:]
    print(f"{'No':<5} {'ID':<8} {'Nama'}")
    print("-" * 55)
    nomor = 1
    while temp_heap:
        id_, nama = max_heap_pop(temp_heap)
        print(f"{nomor:<5} {id_:<8} {nama}")
        nomor += 1
    print()

# ============================================================
#  FITUR TAMBAH DATA
# ============================================================

def tambah_data(min_heap, max_heap):
    print()
    print("=" * 55)
    print("  TAMBAH DATA BARU")
    print("=" * 55)
    try:
        id_baru = int(input("  Masukkan ID baru : ").strip())
    except ValueError:
        print("\n  [ERROR] ID harus berupa angka!\n")
        return

    # Cek duplikat
    for id_, _ in min_heap:
        if id_ == id_baru:
            print(f"\n  [ERROR] ID {id_baru} sudah ada dalam heap!\n")
            return

    nama_baru = input("  Masukkan Nama    : ").strip()
    if not nama_baru:
        print("\n  [ERROR] Nama tidak boleh kosong!\n")
        return

    item = (id_baru, nama_baru)
    min_heap_push(min_heap, item)
    max_heap_push(max_heap, item)
    print(f"\n  [OK] Data (ID={id_baru}, Nama={nama_baru}) berhasil ditambahkan")
    print(f"       ke Min-Heap dan Max-Heap.\n")

# ============================================================
#  FITUR HAPUS DATA
# ============================================================

def hapus_dari_min_heap(min_heap):
    print()
    print("=" * 55)
    print("  HAPUS DATA DARI MIN-HEAP")
    print("=" * 55)
    try:
        target_id = int(input("  Masukkan ID yang ingin dihapus: ").strip())
    except ValueError:
        print("\n  [ERROR] ID harus berupa angka!\n")
        return

    hasil = min_heap_remove(min_heap, target_id)
    if hasil:
        print(f"\n  [OK] Data dengan ID={target_id} berhasil dihapus dari Min-Heap.\n")
    else:
        print(f"\n  [ERROR] ID={target_id} tidak ditemukan dalam Min-Heap.\n")

def hapus_dari_max_heap(max_heap):
    print()
    print("=" * 55)
    print("  HAPUS DATA DARI MAX-HEAP")
    print("=" * 55)
    try:
        target_id = int(input("  Masukkan ID yang ingin dihapus: ").strip())
    except ValueError:
        print("\n  [ERROR] ID harus berupa angka!\n")
        return

    hasil = max_heap_remove(max_heap, target_id)
    if hasil:
        print(f"\n  [OK] Data dengan ID={target_id} berhasil dihapus dari Max-Heap.\n")
    else:
        print(f"\n  [ERROR] ID={target_id} tidak ditemukan dalam Max-Heap.\n")

# ============================================================
#  FITUR MUAT DATA DARI CSV (Pilihan 6)
# ============================================================

def muat_data_csv(min_heap, max_heap):
    print()
    print("=" * 55)
    print("  MUAT DATA AWAL DARI CSV")
    print("=" * 55)

    if min_heap or max_heap:
        konfirmasi = input("  Heap sudah berisi data. Timpa dengan data CSV? (y/n): ").strip().lower()
        if konfirmasi != 'y':
            print("\n  [INFO] Pemuatan data dibatalkan.\n")
            return

    min_heap.clear()
    max_heap.clear()

    for item in data:
        min_heap_push(min_heap, item)
        max_heap_push(max_heap, item)

    print(f"\n  [OK] {len(data)} data dari '{CSV_PATH}' berhasil dimuat")
    print(f"       ke dalam Min-Heap dan Max-Heap.\n")
    tampilkan_data_asli(data)

# ============================================================
#  MAIN
# ============================================================

if __name__ == "__main__":
    print("\n>>> PROGRAM STRUKTUR DATA HEAP <<<\n")

    min_heap = []
    max_heap = []

    # Auto-load data CSV saat program pertama kali dijalankan
    for item in data:
        min_heap_push(min_heap, item)
        max_heap_push(max_heap, item)
    print(f"  [INFO] {len(data)} data dari '{CSV_PATH}' berhasil dimuat ke Min-Heap dan Max-Heap.\n")

    # Menu interaktif
    while True:
        print("=" * 55)
        print("  MENU")
        print("=" * 55)
        print("  1. Tambah data (ke Min-Heap & Max-Heap)")
        print("  2. Tampilkan urut ID Ascending  (Min-Heap)")
        print("  3. Tampilkan urut ID Descending (Max-Heap)")
        print("  4. Hapus data dari Min-Heap")
        print("  5. Hapus data dari Max-Heap")
        print("  6. Muat data awal dari CSV ke Min-Heap & Max-Heap")
        print("  7. End")
        print("-" * 55)
        pilihan = input("  Masukkan pilihan (1-7): ").strip()

        if pilihan == "1":
            tambah_data(min_heap, max_heap)
        elif pilihan == "2":
            tampilkan_ascending_min_heap(min_heap)
        elif pilihan == "3":
            tampilkan_descending_max_heap(max_heap)
        elif pilihan == "4":
            hapus_dari_min_heap(min_heap)
        elif pilihan == "5":
            hapus_dari_max_heap(max_heap)
        elif pilihan == "6":
            muat_data_csv(min_heap, max_heap)
        elif pilihan == "7":
            print("\n  Program selesai. Terima kasih!\n")
            break
        else:
            print("\n  Pilihan tidak valid! Masukkan angka 1 sampai 7.\n")
