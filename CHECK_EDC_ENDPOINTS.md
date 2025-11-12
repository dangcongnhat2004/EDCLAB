# Kiểm tra EDC Endpoints

## Cách 1: Kiểm tra OpenAPI Spec

Mở browser và truy cập:
```
http://localhost:19193/management/v3/api-docs
```

Hoặc:
```
http://localhost:19193/management/v3/swagger-ui.html
```

Tìm endpoint cho assets để xem:
- Method nào được hỗ trợ (GET, POST, etc.)
- Endpoint chính xác là gì
- Body format như thế nào

## Cách 2: Test các endpoint có thể

### Thử 1: POST /v3/assets (không có /management)
```
POST http://localhost:19193/v3/assets
Headers:
  X-Api-Key: password
Body:
{
  "@context": {
    "@vocab": "https://w3id.org/edc/v0.0.1/ns/"
  }
}
```

### Thử 2: POST /management/v3/assets (không có /query)
```
POST http://localhost:19193/management/v3/assets
Headers:
  X-Api-Key: password
Body:
{
  "@context": {
    "@vocab": "https://w3id.org/edc/v0.0.1/ns/"
  }
}
```

### Thử 3: GET /management/v3/assets với query params
```
GET http://localhost:19193/management/v3/assets?limit=100
Headers:
  X-Api-Key: password
```

## Cách 3: Kiểm tra EDC Samples

Xem trong EDC Samples để biết cách gọi API:
- https://github.com/eclipse-edc/Samples
- Transfer sample 00, 01

## Sau khi tìm được endpoint đúng:

Cập nhật code trong `EdcService.java` với endpoint và method đúng.

