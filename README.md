# Seats Reservation

## 資料庫連線資訊

### MySQL 本機連線資訊
- **資料庫**: esun
- **主機**: localhost:3306
- **使用者**: 環境變數 `DB_USER_NAME`
- **密碼**: 環境變數 `DB_USER_PASSWORD`

### 設置環境變數：
```text
DB_USER_NAME=資料庫使用者名稱
DB_USER_PASSWORD=資料庫密碼
```

## 啟動服務

### 資料庫
1. 本專案使用之資料庫為 MySQL
2. 於 DB 中執行專案程式中的 `schema.sql` 檔

### 後端
1. 使用 IDE 打開 backend 這包程式
<img width="457" alt="image" src="https://github.com/user-attachments/assets/98c0dbe3-5ce8-47ee-b35c-445b09344f37" />

2. 環境為 JDK 17 或更高版本
3. 確認已設置環境變數
4. server port:8080
5. 執行`BackendApplication.java`：

### 前端
1. 使用 IDE 打開 frontend 這包程式
<img width="387" alt="image" src="https://github.com/user-attachments/assets/9bc3f5c6-b5a6-4e8d-aa1e-ea4a3965e60a" />

2. 安裝 dependencies：
   ```bash
   npm install
   ```
3. 啟動 server：
   ```bash
   npm run dev
   ```
4. 使用本機環境測試：http://localhost:5173/

## 前端畫面
<img width="1495" alt="image" src="https://github.com/user-attachments/assets/bfa64210-6641-4a30-8c87-abc3fa471575" />


