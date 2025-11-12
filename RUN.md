# Hướng dẫn chạy Backend

## Cách chạy Backend

### Trên Windows:
```bash
gradlew.bat bootRun
```

### Trên Linux/Mac:
```bash
./gradlew bootRun
```

## Cách restart Backend

Nếu đã sửa code (đặc biệt là CORS config), bạn cần restart backend:

1. **Dừng backend**: Nhấn `Ctrl + C` trong terminal đang chạy backend
2. **Chạy lại**: `gradlew.bat bootRun` (Windows) hoặc `./gradlew bootRun` (Linux/Mac)

## Kiểm tra Backend đã chạy

Sau khi chạy lệnh trên, backend sẽ khởi động trên port **8081**.

Bạn có thể kiểm tra bằng cách:
1. Mở browser và truy cập: `http://localhost:8081/api/assets/edc`
2. Hoặc dùng PowerShell: `Invoke-WebRequest http://localhost:8081/api/assets/edc`

**Lưu ý**: Nếu thấy lỗi 500, có thể do EDC chưa chạy, nhưng backend vẫn hoạt động bình thường.

## Lưu ý

- Backend cần kết nối đến EDC Provider tại `http://localhost:19193`
- Nếu EDC chưa chạy, một số API có thể trả về lỗi, nhưng backend vẫn hoạt động
- Frontend sẽ chạy trên port **5173** (Vite default)

## Troubleshooting

Nếu gặp lỗi "Port already in use":
- Kiểm tra xem có process nào đang dùng port 8081 không
- Thay đổi port trong `application.yml` nếu cần

