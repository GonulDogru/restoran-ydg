CREATE TABLE IF NOT EXISTS admin (
    id SERIAL PRIMARY KEY,
    password VARCHAR(255),
    superadmin_id INTEGER,
    username VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS calisan (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255),
    restaurant_id INTEGER
);

CREATE TABLE IF NOT EXISTS garson (
    calisan_id INTEGER,
    id SERIAL PRIMARY KEY,
    name VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS geribildirim (
    id SERIAL PRIMARY KEY,
    user_id INTEGER,
    yorum VARCHAR(500)
);

CREATE TABLE IF NOT EXISTS icecek (
    id SERIAL PRIMARY KEY,
    menu_id INTEGER,
    name VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS iletisim (
    id SERIAL PRIMARY KEY,
    mesaj VARCHAR(500),
    restaurant_id INTEGER,
    user_id INTEGER
);

CREATE TABLE IF NOT EXISTS indirim (
    amount NUMERIC(10,2),
    id SERIAL PRIMARY KEY,
    kullanici_id INTEGER
);

CREATE TABLE IF NOT EXISTS kullanici (
    adres VARCHAR(500),
    id SERIAL PRIMARY KEY,
    password VARCHAR(255),
    telefon VARCHAR(255),
    username VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS masa (
    id SERIAL PRIMARY KEY,
    kapasite INTEGER,
    numara INTEGER,
    restaurant_id INTEGER
);

CREATE TABLE IF NOT EXISTS menu (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255),
    restaurant_id INTEGER
);

CREATE TABLE IF NOT EXISTS odeme (
    id SERIAL PRIMARY KEY,
    masa_id INTEGER,
    odeme_yontemi VARCHAR(255),
    tarih TIMESTAMP,
    tutar NUMERIC(10,2)
);

CREATE TABLE IF NOT EXISTS restaurant (
    adres VARCHAR(500),
    id SERIAL PRIMARY KEY,
    name VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS rezervasyon (
    id SERIAL PRIMARY KEY,
    masa_id INTEGER,
    saat VARCHAR(255),
    tarih TIMESTAMP,
    user_id INTEGER
);

CREATE TABLE IF NOT EXISTS siparis (
    amount NUMERIC(10,2),
    id SERIAL PRIMARY KEY,
    masa_id INTEGER,
    tarih TIMESTAMP,
    user_id INTEGER
);

CREATE TABLE IF NOT EXISTS stok (
    icecek_id INTEGER,
    id SERIAL PRIMARY KEY,
    miktar INTEGER,
    tatli_id INTEGER,
    tedarikci_id INTEGER,
    yemek_id INTEGER
);

CREATE TABLE IF NOT EXISTS superadmin (
    id SERIAL PRIMARY KEY,
    password VARCHAR(255),
    username VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS tatli (
    id SERIAL PRIMARY KEY,
    menu_id INTEGER,
    name VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS tedarikci (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255),
    phone VARCHAR(255),
    restaurant_id INTEGER
);

CREATE TABLE IF NOT EXISTS teslimat (
    address VARCHAR(500),
    durum VARCHAR(255),
    id SERIAL PRIMARY KEY,
    restaurant_id INTEGER,
    siparis_id INTEGER
);

CREATE TABLE IF NOT EXISTS vardiya (
    calisan_id INTEGER,
    id SERIAL PRIMARY KEY,
    saatler VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS yemek (
    id SERIAL PRIMARY KEY,
    menu_id INTEGER,
    name VARCHAR(255)
);
