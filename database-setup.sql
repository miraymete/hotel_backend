-- PostgreSQL Database Setup Script
-- Bu script'i PostgreSQL'de çalıştırarak veritabanını oluşturun

-- Veritabanı oluştur
CREATE DATABASE hotel_db;

-- Kullanıcı oluştur (isteğe bağlı - güvenlik için)
CREATE USER hotel_user WITH PASSWORD 'hotel_password';

-- Kullanıcıya yetkileri ver
GRANT ALL PRIVILEGES ON DATABASE hotel_db TO hotel_user;

-- hotel_db veritabanına bağlan
\c hotel_db;

-- Kullanıcıya schema yetkileri ver
GRANT ALL ON SCHEMA public TO hotel_user;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO hotel_user;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO hotel_user;

-- Otomatik olarak gelecekte oluşturulacak tablolara da yetki ver
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON TABLES TO hotel_user;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON SEQUENCES TO hotel_user;
