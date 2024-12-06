DROP TABLE IF EXISTS Kecamatan CASCADE;
DROP TABLE IF EXISTS Kelurahan CASCADE;
DROP TABLE IF EXISTS Alamat CASCADE;
DROP TABLE IF EXISTS Jabatan CASCADE;
DROP TABLE IF EXISTS Pegawai CASCADE;
DROP TABLE IF EXISTS DaftarKehadiran CASCADE;
DROP TABLE IF EXISTS Pemilik CASCADE;

CREATE TABLE Kecamatan (
    idKecamatan SERIAL PRIMARY KEY,
    namaKecamatan VARCHAR(50)
);

CREATE TABLE Kelurahan (
    idKelurahan SERIAL PRIMARY KEY,
    namaKelurahan VARCHAR(50),
    idKecamatan INT REFERENCES Kecamatan (idKecamatan)
);

CREATE TABLE Alamat (
    idAlamat SERIAL PRIMARY KEY,
    namaJalan VARCHAR(60),
    idKelurahan INT NOT NULL REFERENCES Kelurahan (idKelurahan)
);

CREATE TABLE Jabatan (
    idJabatan SERIAL PRIMARY KEY,
    namaJabatan VARCHAR(30),
    satuanGaji NUMERIC
);

CREATE TABLE Pegawai (
    nomorHP VARCHAR(20) PRIMARY KEY,
    namaPegawai VARCHAR(50),
    email VARCHAR(50),
    idJabatan INT REFERENCES Jabatan (idJabatan),
    idAlamat INT REFERENCES Alamat (idAlamat),
	status INT
);

CREATE TABLE DaftarKehadiran (
    idKehadiran SERIAL PRIMARY KEY,
    tanggal DATE,
    jamMasuk TIME,
    jamKeluar TIME,
    nomorHP VARCHAR(20) REFERENCES Pegawai (nomorHP),
    gaji NUMERIC
);

CREATE TABLE Pemilik (
    namaPemilik VARCHAR(50) PRIMARY KEY,
    usernamePemilik VARCHAR(30),
    passwordPemilik VARCHAR(20)
);

CREATE INDEX idx_idKecamatan ON Kecamatan (idKecamatan);
CREATE INDEX idx_idKelurahan ON Kelurahan (idKelurahan);
CREATE INDEX idx_idAlamat ON Alamat (idAlamat);
CREATE INDEX idx_idJabatan ON Jabatan (idJabatan);
CREATE INDEX idx_nomorHP ON Pegawai (nomorHP);
CREATE INDEX idx_idKehadiran ON DaftarKehadiran (idKehadiran);

insert into Kecamatan(namaKecamatan)
values	('Andir'), ('Antapani'), ('Arcamanik'), ('Astanaanyar'), ('Babakan Ciparay'), ('Bandung Kidul'), ('Bandung Kulon'), ('Bandung Wetan'), ('Batununggal'),
		('Bojongloa Kaler'), ('Bojongloa Kidul'), ('Buahbatu'), ('Cibeunying Kaler'), ('Cibeunying Kidul'), ('Cibiru'), ('Cicendo'), ('Cidadap'), ('Cinambo'),
		('Coblong'), ('Gedebage'), ('Kiaracondong'), ('Lengkong'), ('Mandalajati'), ('Panyileukan'), ('Rancasari'), ('Regol'), ('Sukajadi'), ('Sukasari'),
		('Sumur Bandung'), ('Ujung Berung');

-- Kecamatan Andir
INSERT INTO Kelurahan (namaKelurahan, idKecamatan)
VALUES
('Campaka', (SELECT idKecamatan FROM Kecamatan WHERE namaKecamatan = 'Andir')),
('Dungus Cariang', (SELECT idKecamatan FROM Kecamatan WHERE namaKecamatan = 'Andir')),
('Garuda', (SELECT idKecamatan FROM Kecamatan WHERE namaKecamatan = 'Andir')),
('Kebon Jeruk', (SELECT idKecamatan FROM Kecamatan WHERE namaKecamatan = 'Andir')),
('Maleber', (SELECT idKecamatan FROM Kecamatan WHERE namaKecamatan = 'Andir'));

-- Kecamatan Antapani
INSERT INTO Kelurahan (namaKelurahan, idKecamatan)
VALUES
('Antapani Kidul', (SELECT idKecamatan FROM Kecamatan WHERE namaKecamatan = 'Antapani')),
('Antapani Tengah', (SELECT idKecamatan FROM Kecamatan WHERE namaKecamatan = 'Antapani')),
('Antapani Wetan', (SELECT idKecamatan FROM Kecamatan WHERE namaKecamatan = 'Antapani')),
('Antapani Kulon', (SELECT idKecamatan FROM Kecamatan WHERE namaKecamatan = 'Antapani'));

-- Kecamatan Arcamanik
INSERT INTO Kelurahan (namaKelurahan, idKecamatan)
VALUES
('Cisaranten Bina Harapan', (SELECT idKecamatan FROM Kecamatan WHERE namaKecamatan = 'Arcamanik')),
('Cisaranten Endah', (SELECT idKecamatan FROM Kecamatan WHERE namaKecamatan = 'Arcamanik')),
('Cisaranten Kulon', (SELECT idKecamatan FROM Kecamatan WHERE namaKecamatan = 'Arcamanik')),
('Cisaranten Wetan', (SELECT idKecamatan FROM Kecamatan WHERE namaKecamatan = 'Arcamanik')),
('Sukamiskin', (SELECT idKecamatan FROM Kecamatan WHERE namaKecamatan = 'Arcamanik'));

-- Kecamatan Astanaanyar
INSERT INTO Kelurahan (namaKelurahan, idKecamatan)
VALUES
('Cibadak', (SELECT idKecamatan FROM Kecamatan WHERE namaKecamatan = 'Astanaanyar')),
('Karanganyar', (SELECT idKecamatan FROM Kecamatan WHERE namaKecamatan = 'Astanaanyar')),
('Nyengseret', (SELECT idKecamatan FROM Kecamatan WHERE namaKecamatan = 'Astanaanyar')),
('Panjunan', (SELECT idKecamatan FROM Kecamatan WHERE namaKecamatan = 'Astanaanyar'));

-- Kecamatan Babakan Ciparay
INSERT INTO Kelurahan (namaKelurahan, idKecamatan)
VALUES
('Babakan', (SELECT idKecamatan FROM Kecamatan WHERE namaKecamatan = 'Babakan Ciparay')),
('Babakan Ciparay', (SELECT idKecamatan FROM Kecamatan WHERE namaKecamatan = 'Babakan Ciparay')),
('Cirangrang', (SELECT idKecamatan FROM Kecamatan WHERE namaKecamatan = 'Babakan Ciparay')),
('Margasuka', (SELECT idKecamatan FROM Kecamatan WHERE namaKecamatan = 'Babakan Ciparay')),
('Sukahaji', (SELECT idKecamatan FROM Kecamatan WHERE namaKecamatan = 'Babakan Ciparay'));

-- Kecamatan Bandung Kidul
INSERT INTO Kelurahan (namaKelurahan, idKecamatan)
VALUES
('Batununggal', (SELECT idKecamatan FROM Kecamatan WHERE namaKecamatan = 'Bandung Kidul')),
('Mengger', (SELECT idKecamatan FROM Kecamatan WHERE namaKecamatan = 'Bandung Kidul')),
('Wates', (SELECT idKecamatan FROM Kecamatan WHERE namaKecamatan = 'Bandung Kidul'));

-- Kecamatan Bandung Kulon
INSERT INTO Kelurahan (namaKelurahan, idKecamatan)
VALUES
('Caringin', (SELECT idKecamatan FROM Kecamatan WHERE namaKecamatan = 'Bandung Kulon')),
('Cigondewah Kaler', (SELECT idKecamatan FROM Kecamatan WHERE namaKecamatan = 'Bandung Kulon')),
('Cigondewah Kidul', (SELECT idKecamatan FROM Kecamatan WHERE namaKecamatan = 'Bandung Kulon')),
('Cigondewah Rahayu', (SELECT idKecamatan FROM Kecamatan WHERE namaKecamatan = 'Bandung Kulon')),
('Gempol Sari', (SELECT idKecamatan FROM Kecamatan WHERE namaKecamatan = 'Bandung Kulon')),
('Cibuntu', (SELECT idKecamatan FROM Kecamatan WHERE namaKecamatan = 'Bandung Kulon')),
('Warung Muncang', (SELECT idKecamatan FROM Kecamatan WHERE namaKecamatan = 'Bandung Kulon'));

-- Kecamatan Bandung Wetan
INSERT INTO Kelurahan (namaKelurahan, idKecamatan)
VALUES
('Cihapit', (SELECT idKecamatan FROM Kecamatan WHERE namaKecamatan = 'Bandung Wetan')),
('Citarum', (SELECT idKecamatan FROM Kecamatan WHERE namaKecamatan = 'Bandung Wetan')),
('Tamansari', (SELECT idKecamatan FROM Kecamatan WHERE namaKecamatan = 'Bandung Wetan'));

-- Kecamatan Batununggal
INSERT INTO Kelurahan (namaKelurahan, idKecamatan)
VALUES
('Batununggal', (SELECT idKecamatan FROM Kecamatan WHERE namaKecamatan = 'Batununggal')),
('Cibangkong', (SELECT idKecamatan FROM Kecamatan WHERE namaKecamatan = 'Batununggal')),
('Kebonwaru', (SELECT idKecamatan FROM Kecamatan WHERE namaKecamatan = 'Batununggal')),
('Kacapiring', (SELECT idKecamatan FROM Kecamatan WHERE namaKecamatan = 'Batununggal')),
('Maleer', (SELECT idKecamatan FROM Kecamatan WHERE namaKecamatan = 'Batununggal')),
('Samoja', (SELECT idKecamatan FROM Kecamatan WHERE namaKecamatan = 'Batununggal'));

-- Kecamatan Bojongloa Kaler
INSERT INTO Kelurahan (namaKelurahan, idKecamatan)
VALUES
('Jamika', (SELECT idKecamatan FROM Kecamatan WHERE namaKecamatan = 'Bojongloa Kaler')),
('Suka Asih', (SELECT idKecamatan FROM Kecamatan WHERE namaKecamatan = 'Bojongloa Kaler')),
('Suka Gumiwang', (SELECT idKecamatan FROM Kecamatan WHERE namaKecamatan = 'Bojongloa Kaler'));

-- Kecamatan Bojongloa Kidul
INSERT INTO Kelurahan (namaKelurahan, idKecamatan)
VALUES
('Cibaduyut', (SELECT idKecamatan FROM Kecamatan WHERE namaKecamatan = 'Bojongloa Kidul')),
('Cibaduyut Kidul', (SELECT idKecamatan FROM Kecamatan WHERE namaKecamatan = 'Bojongloa Kidul')),
('Cibaduyut Wetan', (SELECT idKecamatan FROM Kecamatan WHERE namaKecamatan = 'Bojongloa Kidul')),
('Kebon Lega', (SELECT idKecamatan FROM Kecamatan WHERE namaKecamatan = 'Bojongloa Kidul')),
('Mekarwangi', (SELECT idKecamatan FROM Kecamatan WHERE namaKecamatan = 'Bojongloa Kidul'));

-- Kecamatan Buahbatu
INSERT INTO Kelurahan (namaKelurahan, idKecamatan)
VALUES
('Cijagra', (SELECT idKecamatan FROM Kecamatan WHERE namaKecamatan = 'Buahbatu')),
('Jatisari', (SELECT idKecamatan FROM Kecamatan WHERE namaKecamatan = 'Buahbatu')),
('Margacinta', (SELECT idKecamatan FROM Kecamatan WHERE namaKecamatan = 'Buahbatu')),
('Sekejati', (SELECT idKecamatan FROM Kecamatan WHERE namaKecamatan = 'Buahbatu'));

-- Kecamatan Cibeunying Kaler
INSERT INTO Kelurahan (namaKelurahan, idKecamatan)
VALUES
('Cigadung', (SELECT idKecamatan FROM Kecamatan WHERE namaKecamatan = 'Cibeunying Kaler')),
('Cihaurgeulis', (SELECT idKecamatan FROM Kecamatan WHERE namaKecamatan = 'Cibeunying Kaler')),
('Neglasari', (SELECT idKecamatan FROM Kecamatan WHERE namaKecamatan = 'Cibeunying Kaler'));

-- Kecamatan Cibeunying Kidul
INSERT INTO Kelurahan (namaKelurahan, idKecamatan)
VALUES
('Cikutra', (SELECT idKecamatan FROM Kecamatan WHERE namaKecamatan = 'Cibeunying Kidul')),
('Padasuka', (SELECT idKecamatan FROM Kecamatan WHERE namaKecamatan = 'Cibeunying Kidul')),
('Sukamaju', (SELECT idKecamatan FROM Kecamatan WHERE namaKecamatan = 'Cibeunying Kidul'));

-- Kecamatan Cibiru
INSERT INTO Kelurahan (namaKelurahan, idKecamatan)
VALUES
('Cipadung', (SELECT idKecamatan FROM Kecamatan WHERE namaKecamatan = 'Cibiru')),
('Cipadung Kidul', (SELECT idKecamatan FROM Kecamatan WHERE namaKecamatan = 'Cibiru')),
('Cipadung Kulon', (SELECT idKecamatan FROM Kecamatan WHERE namaKecamatan = 'Cibiru')),
('Palasari', (SELECT idKecamatan FROM Kecamatan WHERE namaKecamatan = 'Cibiru')),
('Pasirbiru', (SELECT idKecamatan FROM Kecamatan WHERE namaKecamatan = 'Cibiru'));

-- Kecamatan Cicendo
INSERT INTO Kelurahan (namaKelurahan, idKecamatan)
VALUES
('Arjuna', (SELECT idKecamatan FROM Kecamatan WHERE namaKecamatan = 'Cicendo')),
('Babakan Ciamis', (SELECT idKecamatan FROM Kecamatan WHERE namaKecamatan = 'Cicendo')),
('Husein Sastranegara', (SELECT idKecamatan FROM Kecamatan WHERE namaKecamatan = 'Cicendo')),
('Pajajaran', (SELECT idKecamatan FROM Kecamatan WHERE namaKecamatan = 'Cicendo')),
('Pasirkaliki', (SELECT idKecamatan FROM Kecamatan WHERE namaKecamatan = 'Cicendo'));

-- Kecamatan Cidadap
INSERT INTO Kelurahan (namaKelurahan, idKecamatan)
VALUES
('Ciumbuleuit', (SELECT idKecamatan FROM Kecamatan WHERE namaKecamatan = 'Cidadap')),
('Hegarmanah', (SELECT idKecamatan FROM Kecamatan WHERE namaKecamatan = 'Cidadap')),
('Ledeng', (SELECT idKecamatan FROM Kecamatan WHERE namaKecamatan = 'Cidadap'));

-- Kecamatan Cinambo
INSERT INTO Kelurahan (namaKelurahan, idKecamatan)
VALUES
('Babakan Penghulu', (SELECT idKecamatan FROM Kecamatan WHERE namaKecamatan = 'Cinambo')),
('Cisaranten Kulon', (SELECT idKecamatan FROM Kecamatan WHERE namaKecamatan = 'Cinambo')),
('Pakemitan', (SELECT idKecamatan FROM Kecamatan WHERE namaKecamatan = 'Cinambo')),
('Sukamulya', (SELECT idKecamatan FROM Kecamatan WHERE namaKecamatan = 'Cinambo'));

-- Kecamatan Coblong
INSERT INTO Kelurahan (namaKelurahan, idKecamatan)
VALUES
('Cipaganti', (SELECT idKecamatan FROM Kecamatan WHERE namaKecamatan = 'Coblong')),
('Dago', (SELECT idKecamatan FROM Kecamatan WHERE namaKecamatan = 'Coblong')),
('Lebakgede', (SELECT idKecamatan FROM Kecamatan WHERE namaKecamatan = 'Coblong')),
('Lebaksiuh', (SELECT idKecamatan FROM Kecamatan WHERE namaKecamatan = 'Coblong')),
('Sadangserang', (SELECT idKecamatan FROM Kecamatan WHERE namaKecamatan = 'Coblong')),
('Sekeloa', (SELECT idKecamatan FROM Kecamatan WHERE namaKecamatan = 'Coblong'));

-- Kecamatan Gedebage
INSERT INTO Kelurahan (namaKelurahan, idKecamatan)
VALUES
('Cimincrang', (SELECT idKecamatan FROM Kecamatan WHERE namaKecamatan = 'Gedebage')),
('Rancabolang', (SELECT idKecamatan FROM Kecamatan WHERE namaKecamatan = 'Gedebage')),
('Rancanumpang', (SELECT idKecamatan FROM Kecamatan WHERE namaKecamatan = 'Gedebage'));

-- Kecamatan Kiaracondong
INSERT INTO Kelurahan (namaKelurahan, idKecamatan)
VALUES
('Babakan Surabaya', (SELECT idKecamatan FROM Kecamatan WHERE namaKecamatan = 'Kiaracondong')),
('Babakansari', (SELECT idKecamatan FROM Kecamatan WHERE namaKecamatan = 'Kiaracondong')),
('Binong', (SELECT idKecamatan FROM Kecamatan WHERE namaKecamatan = 'Kiaracondong')),
('Cicaheum', (SELECT idKecamatan FROM Kecamatan WHERE namaKecamatan = 'Kiaracondong')),
('Kebonjayanti', (SELECT idKecamatan FROM Kecamatan WHERE namaKecamatan = 'Kiaracondong')),
('Kebun Kangkung', (SELECT idKecamatan FROM Kecamatan WHERE namaKecamatan = 'Kiaracondong'));

-- Kecamatan Lengkong
INSERT INTO Kelurahan (namaKelurahan, idKecamatan)
VALUES
('Burangrang', (SELECT idKecamatan FROM Kecamatan WHERE namaKecamatan = 'Lengkong')),
('Cijagra', (SELECT idKecamatan FROM Kecamatan WHERE namaKecamatan = 'Lengkong')),
('Malabar', (SELECT idKecamatan FROM Kecamatan WHERE namaKecamatan = 'Lengkong')),
('Turangga', (SELECT idKecamatan FROM Kecamatan WHERE namaKecamatan = 'Lengkong'));

-- Kecamatan Mandalajati
INSERT INTO Kelurahan (namaKelurahan, idKecamatan)
VALUES
('Jatihandap', (SELECT idKecamatan FROM Kecamatan WHERE namaKecamatan = 'Mandalajati')),
('Karang Pamulang', (SELECT idKecamatan FROM Kecamatan WHERE namaKecamatan = 'Mandalajati')),
('Pasir Impun', (SELECT idKecamatan FROM Kecamatan WHERE namaKecamatan = 'Mandalajati')),
('Sindang Jaya', (SELECT idKecamatan FROM Kecamatan WHERE namaKecamatan = 'Mandalajati'));

-- Kecamatan Panyileukan
INSERT INTO Kelurahan (namaKelurahan, idKecamatan)
VALUES
('Cipadung Kidul', (SELECT idKecamatan FROM Kecamatan WHERE namaKecamatan = 'Panyileukan')),
('Cipadung Wetan', (SELECT idKecamatan FROM Kecamatan WHERE namaKecamatan = 'Panyileukan')),
('Mekarmulya', (SELECT idKecamatan FROM Kecamatan WHERE namaKecamatan = 'Panyileukan'));

-- Kecamatan Rancasari
INSERT INTO Kelurahan (namaKelurahan, idKecamatan)
VALUES
('Derwati', (SELECT idKecamatan FROM Kecamatan WHERE namaKecamatan = 'Rancasari')),
('Manjahlega', (SELECT idKecamatan FROM Kecamatan WHERE namaKecamatan = 'Rancasari')),
('Margasari', (SELECT idKecamatan FROM Kecamatan WHERE namaKecamatan = 'Rancasari')),
('Rancabolang', (SELECT idKecamatan FROM Kecamatan WHERE namaKecamatan = 'Rancasari'));

-- Kecamatan Regol
INSERT INTO Kelurahan (namaKelurahan, idKecamatan)
VALUES
('Ancol', (SELECT idKecamatan FROM Kecamatan WHERE namaKecamatan = 'Regol')),
('Balonggede', (SELECT idKecamatan FROM Kecamatan WHERE namaKecamatan = 'Regol')),
('Ciseureuh', (SELECT idKecamatan FROM Kecamatan WHERE namaKecamatan = 'Regol')),
('Cigereleng', (SELECT idKecamatan FROM Kecamatan WHERE namaKecamatan = 'Regol')),
('Pungkur', (SELECT idKecamatan FROM Kecamatan WHERE namaKecamatan = 'Regol'));

-- Kecamatan Sukajadi
INSERT INTO Kelurahan (namaKelurahan, idKecamatan)
VALUES
('Cipedes', (SELECT idKecamatan FROM Kecamatan WHERE namaKecamatan = 'Sukajadi')),
('Pasteur', (SELECT idKecamatan FROM Kecamatan WHERE namaKecamatan = 'Sukajadi')),
('Sukabungah', (SELECT idKecamatan FROM Kecamatan WHERE namaKecamatan = 'Sukajadi')),
('Sukagalih', (SELECT idKecamatan FROM Kecamatan WHERE namaKecamatan = 'Sukajadi')),
('Sukawarna', (SELECT idKecamatan FROM Kecamatan WHERE namaKecamatan = 'Sukajadi'));

-- Kecamatan Sukasari
INSERT INTO Kelurahan (namaKelurahan, idKecamatan)
VALUES
('Gegerkalong', (SELECT idKecamatan FROM Kecamatan WHERE namaKecamatan = 'Sukasari')),
('Isola', (SELECT idKecamatan FROM Kecamatan WHERE namaKecamatan = 'Sukasari')),
('Sarijadi', (SELECT idKecamatan FROM Kecamatan WHERE namaKecamatan = 'Sukasari')),
('Sukarasa', (SELECT idKecamatan FROM Kecamatan WHERE namaKecamatan = 'Sukasari'));

-- Kecamatan Sumur Bandung
INSERT INTO Kelurahan (namaKelurahan, idKecamatan)
VALUES
('Braga', (SELECT idKecamatan FROM Kecamatan WHERE namaKecamatan = 'Sumur Bandung')),
('Kebon Pisang', (SELECT idKecamatan FROM Kecamatan WHERE namaKecamatan = 'Sumur Bandung')),
('Merdeka', (SELECT idKecamatan FROM Kecamatan WHERE namaKecamatan = 'Sumur Bandung'));

-- Kecamatan Ujung Berung
INSERT INTO Kelurahan (namaKelurahan, idKecamatan)
VALUES
('Cigending', (SELECT idKecamatan FROM Kecamatan WHERE namaKecamatan = 'Ujung Berung')),
('Pasirjati', (SELECT idKecamatan FROM Kecamatan WHERE namaKecamatan = 'Ujung Berung')),
('Pasirwangi', (SELECT idKecamatan FROM Kecamatan WHERE namaKecamatan = 'Ujung Berung')),
('Pasanggrahan', (SELECT idKecamatan FROM Kecamatan WHERE namaKecamatan = 'Ujung Berung')),
('Ujungberung', (SELECT idKecamatan FROM Kecamatan WHERE namaKecamatan = 'Ujung Berung'));

insert into Jabatan(namaJabatan, satuanGaji)
values	('Barista', 22000.0),
		('Kasir', 18000.0),
		('Waiter', 20000.0),
		('Janitor', 15000.0),
		('Security', 15000.0);

insert into Pemilik  values 
	('agus', 'agus123', 'admin123');

INSERT INTO Alamat(namaJalan, idKelurahan) VALUES 
	('Jl. Ciumbuleuit No.42A', 72),
	('jl Cianjur No.34', 40),
	('Jl. Raya Cijerah No.19', 32),
	('Jl. Raya Kopo No.46', 18),
	('Jl. Holis No.257', 27),
	('Jl. Lamping No.18', 114),
	('Jl. Banda No.30', 35);

INSERT INTO Pegawai(nomorHP, namaPegawai, email, idJabatan, idAlamat, status) VALUES 
	('08111', 'Steven', 'steven@gmail.com', 1,1,1), --Barista
	('08222', 'Kris', 'kris@gmail.com', 1,2,1), --Barista
	('08333', 'Seba', 'seba@gmail.com', 2,3,1), --Kasir
	('08444', 'Oliv', 'oliv@gmail.com', 3,4,1), --Waiter
	('08555', 'Sam', 'sam@gmail.com', 3,5,1), --Waiter
	('08666', 'Budi', 'budi@gmail.com', 4,6,0),
	('08777', 'Bagus', 'bagus@gmail.com', 5,7,0);
	

INSERT INTO DaftarKehadiran (tanggal, jamMasuk, jamKeluar, nomorHP, gaji) VALUES
    ('2024-12-01', '08:00:00', '15:00:00', '08111', 120000.0),
    ('2024-12-01', '08:05:00', '15:10:00', '08222', 122000.0),
    ('2024-12-01', '07:54:00', '15:45:00', '08333', 118000.0),
    ('2024-12-01', '08:20:00', '15:55:00', '08444', 121500.0),
    ('2024-12-01', '08:15:00', '16:05:00', '08555', 123000.0),

    ('2024-12-02', '08:00:00', '15:05:00', '08111', 130000.0),
    ('2024-12-02', '08:05:00', '15:10:00', '08222', 132000.0),
    ('2024-12-02', '07:50:00', '15:35:00', '08333', 128500.0),
    ('2024-12-02', '08:30:00', '16:10:00', '08444', 131000.0),
    ('2024-12-02', '08:10:00', '16:00:00', '08555', 133500.0),

    ('2024-12-03', '08:00:00', '15:00:00', '08111', 140000.0),
    ('2024-12-03', '08:05:00', '15:12:00', '08222', 142000.0),
    ('2024-12-03', '07:58:00', '15:48:00', '08333', 138500.0),
    ('2024-12-03', '08:15:00', '16:00:00', '08444', 141000.0),
    ('2024-12-03', '08:10:00', '16:08:00', '08555', 144000.0),

    ('2024-12-04', '08:00:00', '15:05:00', '08111', 115000.0),
    ('2024-12-04', '08:10:00', '15:15:00', '08222', 117000.0),
    ('2024-12-04', '07:55:00', '15:50:00', '08333', 114000.0),
    ('2024-12-04', '08:30:00', '16:00:00', '08444', 118500.0),
    ('2024-12-04', '08:20:00', '16:05:00', '08555', 120000.0),

    ('2024-12-05', '08:00:00', '15:05:00', '08111', 125000.0),
    ('2024-12-05', '08:15:00', '15:20:00', '08222', 127000.0),
    ('2024-12-05', '07:57:00', '15:55:00', '08333', 123500.0),
    ('2024-12-05', '08:10:00', '16:00:00', '08444', 126000.0),
    ('2024-12-05', '08:05:00', '16:10:00', '08555', 128500.0);