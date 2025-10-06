# PostgreSQL Kurulum ve Konfigürasyon Rehberi

## 🐘 PostgreSQL Kurulumu

### Windows için:

1. **PostgreSQL İndirin:**
   - https://www.postgresql.org/download/windows/ adresinden PostgreSQL'i indirin
   - Veya Chocolatey kullanın: `choco install postgresql`

2. **Kurulum Sonrası:**
   - PostgreSQL servisi otomatik başlayacak
   - Port: 5432 (varsayılan)

### Docker ile (Önerilen):

```bash
# PostgreSQL container'ı başlat
docker run --name hotel-postgres \
  -e POSTGRES_DB=hotel_db \
  -e POSTGRES_USER=hotel_user \
  -e POSTGRES_PASSWORD=hotel_password \
  -p 5432:5432 \
  -d postgres:15

# Container'ı durdur
docker stop hotel-postgres

# Container'ı başlat
docker start hotel-postgres
```

## 🗄️ Veritabanı Kurulumu

### Yöntem 1: SQL Script ile
```bash
# PostgreSQL'e bağlan
psql -U postgres

# Script'i çalıştır
\i database-setup.sql
```

### Yöntem 2: Manuel
```sql
-- PostgreSQL'de çalıştır
CREATE DATABASE hotel_db;
CREATE USER hotel_user WITH PASSWORD 'hotel_password';
GRANT ALL PRIVILEGES ON DATABASE hotel_db TO hotel_user;
```

## 🔧 Environment Variables

### Development (.env dosyası oluşturun):
```env
DB_USERNAME=hotel_user
DB_PASSWORD=hotel_password
JWT_SECRET=myVerySecretKeyThatShouldBeAtLeast256BitsLongForSecurity123456789
```

### Production (Vercel):
```env
DATABASE_URL=postgresql://username:password@host:port/database
DB_USERNAME=your_db_username
DB_PASSWORD=your_db_password
JWT_SECRET=your_production_secret_key
```

## 🚀 Uygulamayı Başlatma

```bash
# PostgreSQL çalıştığından emin olun
# Sonra uygulamayı başlatın
mvn spring-boot:run
```

## ✅ Test Etme

Uygulama başladıktan sonra:
- http://localhost:6060/api/auth/register - Kullanıcı kaydı
- http://localhost:6060/api/auth/login - Giriş
- H2 Console artık kullanılamaz (PostgreSQL kullanıyoruz)

## 🐛 Sorun Giderme

### Bağlantı Hatası:
```
Connection refused
```
**Çözüm:** PostgreSQL servisinin çalıştığından emin olun

### Authentication Hatası:
```
FATAL: password authentication failed
```
**Çözüm:** Kullanıcı adı ve şifreyi kontrol edin

### Database Bulunamadı:
```
database "hotel_db" does not exist
```
**Çözüm:** `database-setup.sql` script'ini çalıştırın
