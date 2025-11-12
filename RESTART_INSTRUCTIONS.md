# Hướng dẫn Restart Backend để áp dụng thay đổi

## ⚠️ QUAN TRỌNG: Sau khi sửa code, BẮT BUỘC phải restart backend!

## Các bước:

### 1. Dừng Backend hiện tại
- Tìm terminal đang chạy backend
- Nhấn `Ctrl + C` để dừng
- Đợi đến khi process dừng hoàn toàn

### 2. Chạy lại Backend
```bash
cd D:\kaishaProject\edc-admin-backend
gradlew.bat bootRun
```

### 3. Đợi Backend khởi động
- Đợi đến khi thấy dòng: `Started EdcAdminBackendApplication in X.XXX seconds`
- Không được dừng terminal này

### 4. Test trên Frontend
- Mở browser: http://localhost:5173
- Vào trang "アセット管理" (Asset Management)
- Kiểm tra xem có load được data không

## Kiểm tra Backend đang chạy:

```powershell
netstat -ano | findstr :8081
```

Nếu thấy `LISTENING`, backend đang chạy.

## Nếu vẫn lỗi:

1. Kiểm tra EDC Provider có đang chạy tại `http://localhost:19193` không
2. Kiểm tra log trong terminal backend để xem lỗi chi tiết
3. Kiểm tra endpoint có đúng không (có thể cần xem OpenAPI spec của EDC)

