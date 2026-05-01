import csv

#  BACA DATA DARI FILE CSV
CSV_PATH = "data100.csv"   

data = []
with open(CSV_PATH, newline='', encoding='utf-8') as f:
    reader = csv.DictReader(f)
    for row in reader:
        data.append((int(row['id']), row['nama']))

# --- MIN-HEAP ---

def min_heap_push(heap, item):
    heap.append(item)
    _sift_up_min(heap, len(heap) - 1)

def min_heap_pop(heap):
    heap[0], heap[-1] = heap[-1], heap[0]
    item = heap.pop()
    if heap:
        _sift_down_min(heap, 0)
    return item

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

# --- MAX-HEAP ---

def max_heap_push(heap, item):
    heap.append(item)
    _sift_up_max(heap, len(heap) - 1)

def max_heap_pop(heap):
    heap[0], heap[-1] = heap[-1], heap[0]
    item = heap.pop()
    if heap:
        _sift_down_max(heap, 0)
    return item

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

#  TAMPILAN
def tampilkan_data_asli(data):
    print("=" * 55)
    print("  DATA DARI CSV (urutan asli)")
    print("=" * 55)
    print(f"{'No':<5} {'ID':<8} {'Nama'}")
    print("-" * 55)
    for i, (id_, nama) in enumerate(data, 1):
        print(f"{i:<5} {id_:<8} {nama}")
    print()

def tampilkan_ascending_min_heap(data):
    print()
    print("=" * 55)
    print("  MIN-HEAP — Urut ID Ascending (Terkecil → Terbesar)")
    print("=" * 55)
    heap = []
    for item in data:
        min_heap_push(heap, item)
    print(f"{'No':<5} {'ID':<8} {'Nama'}")
    print("-" * 55)
    nomor = 1
    while heap:
        id_, nama = min_heap_pop(heap)
        print(f"{nomor:<5} {id_:<8} {nama}")
        nomor += 1
    print()

def tampilkan_descending_max_heap(data):
    print()
    print("=" * 55)
    print("  MAX-HEAP — Urut ID Descending (Terbesar → Terkecil)")
    print("=" * 55)
    heap = []
    for item in data:
        max_heap_push(heap, item)
    print(f"{'No':<5} {'ID':<8} {'Nama'}")
    print("-" * 55)
    nomor = 1
    while heap:
        id_, nama = max_heap_pop(heap)
        print(f"{nomor:<5} {id_:<8} {nama}")
        nomor += 1
    print()


#  MAIN
if __name__ == "__main__":
    print("\n>>> PROGRAM STRUKTUR DATA HEAP <<<")
    print(f"    Total data : {len(data)} item\n")

    # Tampilkan data asli dari CSV
    tampilkan_data_asli(data)

    # Menu interaktif
    while True:
        print("=" * 55)
        print("  MENU")
        print("=" * 55)
        print("  1. Tampilkan urut ID Ascending  (Min-Heap)")
        print("  2. Tampilkan urut ID Descending (Max-Heap)")
        print("  3. End")
        print("-" * 55)
        pilihan = input("  Masukkan pilihan (1/2/3): ").strip()

        if pilihan == "1":
            tampilkan_ascending_min_heap(data)
        elif pilihan == "2":
            tampilkan_descending_max_heap(data)
        elif pilihan == "3":
            print("\n  Program selesai. Terima kasih!\n")
            break
        else:
            print("\n  Pilihan tidak valid! Masukkan angka 1, 2, atau 3.\n")
