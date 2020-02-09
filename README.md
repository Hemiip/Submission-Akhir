# Submission-Akhir
Fitur yang harus ditambahkan pada aplikasi:
1.	Pencarian film
Syarat:
o	Pengguna dapat melakukan pencarian Movies.
o	Pengguna dapat melakukan pencarian Tv Show.

2.	Widget
Syarat:
o	Pengguna dapat menampilkan widget dari film favorite ke halaman utama smartphone.
o	Tipe widget yang diterapkan adalah Stack Widget.

3.	Reminder
Syarat:
o	Daily Reminder, mengirimkan notifikasi ke pengguna untuk kembali ke Aplikasi Movie Catalogue. Daily reminder harus selalu berjalan tiap jam 7 pagi.
o	Release Today Reminder, mengirimkan notifikasi ke pengguna berupa informasi film yang rilis hari ini (wajib menggunakan endpoint seperti yang telah disediakan pada bagian Resources di bawah). Release reminder harus selalu berjalan tiap jam 8 pagi.
o	Terdapat halaman pengaturan untuk mengaktifkan dan menonaktifkan reminder.

4.	Aplikasi Favorite
Syarat:
o	Membuat aplikasi atau modul baru yang menampilkan daftar film favorite.
o	Menggunakan Content Provider sebagai mekanisme untuk mengakses data dari satu aplikasi ke aplikasi lain.
 
Berikut kerangka tampilan yang bisa Anda gunakan sebagai referensi:
 
Kesempatan untuk submission Anda diterima akan lebih besar jika:
•	Notifikasi dapat berjalan pada perangkat Oreo dan setelahnya
•	Data pada widget dapat diperbarui secara otomatis ketika terdapat perubahan pada data favorite.
•	Menggunakan SearchView pada fitur pencarian film.
•	Menggunakan library pihak ketiga seperti Retrofit, Fast Android Networking, dsb.
•	Menggunakan library penyimpanan lokal pihak ketiga seperti Room, Realm, dsb.
•	Menerapkan design pattern seperti MVP, MVVM, Arch Component, dsb.
•	Aplikasi bisa memberikan pesan eror jika data tidak berhasil ditampilkan.
•	Menuliskan kode dengan bersih.
 
Submission Anda akan ditolak jika:
•	Fitur pencarian tidak berjalan dengan baik.
•	Fitur pencarian tidak memanfaatkan endpoint dari TheMovieDB.
•	Fitur reminder tidak berjalan dengan baik.
•	Fitur release today reminder tidak memanfaatkan endpoint dari TheMovieDB.
•	Tidak dapat menampilkan data favorite ke dalam Stack Widget.
•	Tidak terdapat aplikasi atau modul baru yang menampilkan data favorite.
•	Tidak menerapkan Content Provider sebagai mekanisme untuk mengakses data dari satu aplikasi ke aplikasi lain.
•	Tidak Mempertahankan semua fitur aplikasi dan komponen yang digunakan pada aplikasi Movie Catalogue (Local Storage).
•	Informasi yang ditampilkan pada daftar ataupun detail film, tidak relevan.
•	Aplikasi force closed.
•	Project tidak bisa di-build.
•	Mengirimkan file selain proyek Android Studio.
•	Mengirimkan proyek yang bukan karya sendiri.
 
Resources
Gunakan endpoint berikut untuk melakukan pencarian film.
Movies: https://api.themoviedb.org/3/search/movie?api_key={API KEY}&language=en-US&query={MOVIE NAME}
Tv Show: https://api.themoviedb.org/3/search/tv?api_key={API KEY}&language=en-US&query={TV SHOW NAME}
Contoh: https://api.themoviedb.org/3/search/movie?api_key=123456789&language=en-US&query=Avenger
 
Gunakan endpoint berikut untuk mendapatkan film yang rilis pada tanggal hari ini.
Movies release: https://api.themoviedb.org/3/discover/movie?api_key={API KEY}&primary_release_date.gte={TODAY DATE}&primary_release_date.lte={TODAY DATE}
Contoh: https://api.themoviedb.org/3/discover/movie?api_key=123456789&primary_release_date.gte=2019-01-31&primary_release_date.lte=2019-01-31
Catatan: Pastikan format tanggal yang kalian gunakan benar. Format tanggal yang digunakan adalah "yyyy-MM-dd".
 
Ketentuan
Beberapa ketentuan umum dari proyek aplikasi:
•	Menggunakan Android Studio.
•	Menggunakan bahasa pemrograman Kotlin atau Java.
•	Mengirimkan pekerjaan Anda dalam bentuk folder Proyek Android Studio yang telah diarsipkan (ZIP).
•	Tim penilai akan mengulas submission Anda dalam waktu selambatnya 3 (tiga) hari kerja (tidak termasuk Sabtu, Minggu, dan hari libur nasional).
•	Tidak disarankan untuk melakukan submit berkali-kali karena akan memperlama proses penilaian yang dilakukan tim penilai.
•	Anda akan mendapat notifikasi hasil pengumpulan submission Anda via email, atau Anda dapat mengecek status submission pada akun Dicoding Anda.
 
Tips
Sebelum mengirimkan proyek, pastikan Anda sudah mengekspornya dengan benar.
Bagaimana cara ekspor proyek ke dalam berkas ZIP?
1.	Pilih menu File → Export to ZIP File... pada Android Studio.
2.	Pilih direktori penyimpanan dan klik OK.
Dengan cara di atas, ukuran dari berkas ZIP akan lebih kecil dibandingkan Anda melakukan kompresi secara manual pada file explorer.

