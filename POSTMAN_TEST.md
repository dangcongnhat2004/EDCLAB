# Hướng dẫn Test EDC API trên Postman

## Cấu hình cơ bản

- **Base URL**: `http://localhost:19193`
- **Management Path**: `/management/v3`
- **API Key**: `password` (header: `X-Api-Key`)

## Các endpoint cần test:

### 1. Test GET /assets (không có /query)
```
Method: GET
URL: http://localhost:19193/management/v3/assets
Headers:
  X-Api-Key: password
  Content-Type: application/json
```

### 2. Test POST /assets/query
```
Method: POST
URL: http://localhost:19193/management/v3/assets/query
Headers:
  X-Api-Key: password
  Content-Type: application/json
Body (raw JSON):
{
  "@context": {
    "@vocab": "https://w3id.org/edc/v0.0.1/ns/"
  }
}
```

### 3. Test POST /assets/request
```
Method: POST
URL: http://localhost:19193/management/v3/assets/request
Headers:
  X-Api-Key: password
  Content-Type: application/json
Body (raw JSON):
{
  "@context": {
    "@vocab": "https://w3id.org/edc/v0.0.1/ns/"
  }
}
```

### 4. Test GET với query parameters
```
Method: GET
URL: http://localhost:19193/management/v3/assets?limit=100
Headers:
  X-Api-Key: password
  Content-Type: application/json
```

### 5. Kiểm tra OpenAPI Spec
```
Method: GET
URL: http://localhost:19193/management/v3/api-docs
```

## Kết quả mong đợi:

- **200 OK**: Endpoint đúng, method đúng
- **405 Method Not Allowed**: Method sai, nhưng endpoint có thể đúng
- **404 Not Found**: Endpoint sai
- **401 Unauthorized**: Thiếu hoặc sai API Key

## Sau khi tìm được endpoint đúng:

Cập nhật code trong `EdcService.java` với endpoint và method đúng.

