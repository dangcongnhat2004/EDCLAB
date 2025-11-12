# Kiểm tra kết nối Backend

## Bước 1: Kiểm tra Backend có đang chạy không

Mở PowerShell và chạy:
```powershell
netstat -ano | findstr :8081
```

Nếu thấy `LISTENING`, backend đang chạy.

## Bước 2: Test kết nối từ Browser

Mở browser và truy cập:
```
http://localhost:8081/api/assets/edc
```

Nếu thấy response (dù là lỗi 500), nghĩa là backend đang chạy.

## Bước 3: Kiểm tra CORS

1. Mở Developer Tools (F12) trong browser
2. Vào tab **Network**
3. Refresh trang frontend
4. Click vào request bị lỗi
5. Kiểm tra **Response Headers** có `Access-Control-Allow-Origin` không

## Bước 4: Restart Backend (QUAN TRỌNG)

Sau khi sửa code, **BẮT BUỘC** phải restart backend:

1. Tìm terminal đang chạy backend
2. Nhấn `Ctrl + C` để dừng
3. Chạy lại: `gradlew.bat bootRun`
4. Đợi đến khi thấy: `Started EdcAdminBackendApplication`

## Troubleshooting

### Lỗi "Failed to fetch"
- ✅ Backend chưa chạy → Chạy backend
- ✅ Backend chưa restart sau khi sửa CORS → Restart backend
- ✅ CORS chưa được cấu hình đúng → Kiểm tra CorsConfig.java

### Lỗi 500 Internal Server Error
- Đây là lỗi từ EDC, không phải từ backend
- Backend vẫn hoạt động bình thường
- Cần chạy EDC Provider tại `http://localhost:19193`

