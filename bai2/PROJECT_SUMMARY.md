# Danh Sách Các File Đã Tạo (Files Created)

## Data Layer

### 1. Entity Model
- **File**: `app/src/main/java/com/example/bt_canhan_tuan_7/data/entity/Appointment.kt`
- **Mô tả**: Data class đại diện cho một lịch hẹn với các fields: id, name, description, **location**, fromTime, toTime, personName, personAvatarUrl, notificationSent, createdAt

### 2. Room DAO
- **File**: `app/src/main/java/com/example/bt_canhan_tuan_7/data/dao/AppointmentDao.kt`
- **Mô tả**: Interface Room DAO với các methods: insertAppointment, updateAppointment, deleteAppointment, getAllAppointments, getAppointmentsByTimeRange, getUnsyncedNotifications, markNotificationSent

### 3. Room Database
- **File**: `app/src/main/java/com/example/bt_canhan_tuan_7/data/database/AppointmentDatabase.kt`
- **Mô tả**: Abstract Room Database class với singleton pattern, khởi tạo database "appointment_database"

### 4. Type Converter
- **File**: `app/src/main/java/com/example/bt_canhan_tuan_7/data/util/LocalDateTimeConverter.kt`
- **Mô tả**: TypeConverter cho Room để serialize/deserialize LocalDateTime sang String

### 5. Repository
- **File**: `app/src/main/java/com/example/bt_canhan_tuan_7/data/repository/AppointmentRepository.kt`
- **Mô tả**: Repository pattern class, trung gian giữa DAO và ViewModel, quản lý tất cả data operations

## UI Layer

### 6. Main Screen
- **File**: `app/src/main/java/com/example/bt_canhan_tuan_7/ui/screens/AppointmentScreen.kt`
- **Mô tả**: Composable chính hiển thị Scaffold, TopAppBar, LazyColumn danh sách lịch hẹn, FAB button, và tích hợp các Dialog

### 7. Appointment Card
- **File**: `app/src/main/java/com/example/bt_canhan_tuan_7/ui/screens/AppointmentCard.kt`
- **Mô tả**: Composable component hiển thị mỗi lịch hẹn dưới dạng Card với hình đại diện (load từ URL bằng Coil), tên, mô tả, **địa điểm với LocationOn icon**, và thời gian

### 8. Add Appointment Dialog
- **File**: `app/src/main/java/com/example/bt_canhan_tuan_7/ui/screens/AddAppointmentDialog.kt`
- **Mô tả**: AlertDialog Composable để thêm lịch hẹn mới với các TextFields: tên, mô tả, **nơi chốn**, tên người, URL avatar, từ, đến. Có validation đầy đủ

### 9. Time Range Filter Dialog
- **File**: `app/src/main/java/com/example/bt_canhan_tuan_7/ui/screens/TimeRangeFilterDialog.kt`
- **Mô tả**: AlertDialog Composable để lọc lịch hẹn theo khoảng thời gian với validate format ngày giờ

### 10. Delete Confirmation Dialog
- **File**: `app/src/main/java/com/example/bt_canhan_tuan_7/ui/screens/DeleteConfirmationDialog.kt`
- **Mô tả**: AlertDialog Composable xác nhận trước khi xóa lịch hẹn

## ViewModel Layer

### 11. ViewModel
- **File**: `app/src/main/java/com/example/bt_canhan_tuan_7/ui/viewmodel/AppointmentViewModel.kt`
- **Mô tả**: ViewModel quản lý state: appointments, filteredAppointments, isLoading, errorMessage. Xử lý tất cả business logic và coroutines với viewModelScope

### 12. ViewModel Factory
- **File**: `app/src/main/java/com/example/bt_canhan_tuan_7/ui/viewmodel/AppointmentViewModelFactory.kt`
- **Mô tả**: ViewModelProvider.Factory để inject Repository vào ViewModel

## Notification

### 13. Notification Worker
- **File**: `app/src/main/java/com/example/bt_canhan_tuan_7/ui/notification/NotificationWorker.kt`
- **Mô tả**: CoroutineWorker sử dụng WorkManager, chạy mỗi 15 phút để check lịch hẹn sắp diễn ra (30 phút nữa) và gửi notification **kèm địa điểm**

## Activities

### 14. Main Activity
- **File**: `app/src/main/java/com/example/bt_canhan_tuan_7/MainActivity.kt`
- **Mô tả**: Activity chính, khởi tạo database, repository, ViewModel, Scaffold với AppointmentScreen, và lập lịch NotificationWorker

## Configuration Files

### 15. Build Gradle (App Level)
- **File**: `app/build.gradle.kts`
- **Cập nhật**: Thêm dependencies cho Room, Coil, WorkManager, KSP compiler plugin, cập nhật compileSdk và targetSdk thành 36

### 16. Gradle Versions Catalog
- **File**: `gradle/libs.versions.toml`
- **Cập nhật**: Thêm versions và libraries cho Room, Coil, WorkManager, ViewModel Compose, thêm KSP plugin

### 17. Android Manifest
- **File**: `app/src/main/AndroidManifest.xml`
- **Cập nhật**: Thêm permissions: POST_NOTIFICATIONS, INTERNET, ACCESS_NETWORK_STATE

### 18. README Documentation
- **File**: `README.md` (tại root của project)
- **Mô tả**: Tài liệu toàn diện: features, architecture, technologies, setup guide, usage guide, troubleshooting

## Tóm Tắt Chức Năng (Features Summary)

✅ **Hiển thị Lịch Hẹn**: LazyColumn tương tự RecyclerView
✅ **Xóa Lịch Hẹn**: Click → Dialog xác nhận → Xóa
✅ **Lọc Theo Thời Gian**: Icon Search → Dialog nhập Từ/Đến → Hiển thị kết quả lọc
✅ **Thêm Lịch Hẹn Mới**: FAB → Dialog form → Validate → Lưu database
✅ **Nơi Chốn/Địa Điểm**: Lưu location trong database, hiển thị với LocationOn icon 📍
✅ **Tải Hình Đại Diện**: Lưu URL trong database, load AsyncImage (Coil) khi hiển thị
✅ **Thông Báo 30 Phút**: WorkManager chạy mỗi 15 phút, gửi notification khi còn 30 phút (kèm địa điểm)

## Kiến Trúc MVVM

```
┌─────────────────────┐
│    UI Layer         │
│  Composables        │
│  - AppointmentScreen│
│  - Cards, Dialogs   │
└──────────┬──────────┘
           │ StateFlow
           ↓
┌─────────────────────┐
│  ViewModel Layer    │
│ AppointmentViewModel│
│  - State Management │
│  - Business Logic   │
└──────────┬──────────┘
           │ suspend funs
           ↓
┌─────────────────────┐
│  Repository Layer   │
│ AppointmentRepository
│  - Data Operations  │
└──────────┬──────────┘
           │ Flow
           ↓
┌─────────────────────┐
│  Data Layer         │
│  - DAO              │
│  - Entity           │
│  - Database         │
│  - TypeConverters   │
└─────────────────────┘
```

## Công Nghệ Chính

| Công Nghệ | Phiên Bản | Mục Đích |
|-----------|----------|---------|
| Kotlin | 2.0.21 | Ngôn ngữ chính |
| Compose | 2024.09.00 | UI Framework |
| Room | 2.6.1 | Local Database |
| Coil | 2.5.0 | Image Loading |
| WorkManager | 2.8.1 | Background Tasks |
| Coroutines | Implicit | Async Programming |
| StateFlow | Implicit | State Management |

## Hướng Dẫn Chạy

1. **Cải Đặt Java 11+**
   ```bash
   # macOS
   brew install openjdk@11
   export JAVA_HOME=$(/usr/libexec/java_home -v 11)
   ```

2. **Sync Gradle**
   - Mở project trong Android Studio
   - File → Sync Now

3. **Build & Run**
   ```bash
   ./gradlew build
   ./gradlew installDebug
   # Hoặc nhấn Run trong Android Studio
   ```

4. **Test Tính Năng**
   - Nhấn "Thêm Lịch Hẹn" để thêm
   - Click lịch hẹn để xóa
   - Nhấn Search icon để lọc thời gian
   - Chờ notification (test: tạo lịch hẹn sắp bắt đầu)

## Cấu Trúc Package (Package Structure)

```
com.example.bt_canhan_tuan_7
├── data
│   ├── dao (DAO interfaces)
│   ├── database (Room database setup)
│   ├── entity (Data models)
│   ├── repository (Repository pattern)
│   └── util (Type converters)
├── ui
│   ├── notification (WorkManager tasks)
│   ├── screens (Composable screens/dialogs)
│   ├── viewmodel (ViewModel & Factory)
│   └── theme (Colors, themes, typography)
└── MainActivity.kt
```

## Notes Penting

- ✅ MVVM architecture đầy đủ với separation of concerns
- ✅ Room database tối ưu với TypeConverter cho LocalDateTime
- ✅ Coil image loading từ URL cho avatar
- ✅ WorkManager periodic notification mỗi 15 phút
- ✅ Compose UI với LazyColumn, Cards, Dialogs
- ✅ StateFlow reactive state management
- ✅ Coroutines với viewModelScope
- ✅ Validation input toàn diện
- ✅ Error handling và Snackbar messages
- ✅ Tiếng Việt UI labels

## Tiếp Theo (Next Steps)

Nếu muốn mở rộng:
1. Thêm edit functionality
2. Thêm repeat appointments
3. Thêm tags/categories
4. Thêm location
5. Thêm export/import
6. Thêm dark mode
7. Thêm reminder sounds/vibration
8. Thêm statistics/analytics
