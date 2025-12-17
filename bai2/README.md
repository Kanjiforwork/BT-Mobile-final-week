# Ứng Dụng Quản Lý Lịch Hẹn Cá Nhân (Personal Appointment Manager)

## Tổng Quan (Overview)

Ứng dụng Android quản lý lịch hẹn cá nhân được xây dựng với kiến trúc MVVM, sử dụng Jetpack Compose cho giao diện người dùng. Ứng dụng hỗ trợ tạo, xem, xóa lịch hẹn, lọc theo thời gian và nhận thông báo trước 30 phút.

## Các Tính Năng (Features)

✅ **Hiển thị danh sách lịch hẹn** - RecyclerView-like LazyColumn hiển thị tất cả các lịch hẹn
✅ **Xóa lịch hẹn** - Click vào lịch hẹn để xóa với xác nhận qua Dialog
✅ **Lọc theo thời gian** - Chọn khoảng thời gian "Từ" và "Đến" để lọc lịch hẹn
✅ **Thêm lịch hẹn mới** - Button "Thêm Lịch Hẹn" hiển thị Dialog form nhập liệu
✅ **Nơi chốn/Địa điểm** - Lưu và hiển thị nơi chốn lịch hẹn với LocationOn icon
✅ **Tải hình đại diện** - Lưu URL hình đại diện người hẹn trong Database, load bằng Coil
✅ **Thông báo tự động** - Nhận Notification 30 phút trước thời điểm bắt đầu lịch hẹn (kèm địa điểm)
✅ **MVVM Architecture** - Tách biệt Model, View, ViewModel theo nguyên tắc MVVM

## Kiến Trúc Ứng Dụng (Architecture)

### Cấu Trúc Thư Mục (Project Structure)

```
app/src/main/java/com/example/bt_canhan_tuan_7/
├── data/
│   ├── dao/
│   │   └── AppointmentDao.kt              # Room DAO interface
│   ├── database/
│   │   └── AppointmentDatabase.kt         # Room Database setup
│   ├── entity/
│   │   └── Appointment.kt                 # Data model/Entity
│   ├── repository/
│   │   └── AppointmentRepository.kt       # Repository pattern
│   └── util/
│       └── LocalDateTimeConverter.kt      # Room TypeConverter
├── ui/
│   ├── notification/
│   │   └── NotificationWorker.kt          # WorkManager notification task
│   ├── screens/
│   │   ├── AppointmentScreen.kt           # Main screen with LazyColumn
│   │   ├── AppointmentCard.kt             # Card component cho mỗi appointment
│   │   ├── AddAppointmentDialog.kt        # Dialog thêm lịch hẹn mới
│   │   ├── TimeRangeFilterDialog.kt       # Dialog lọc theo thời gian
│   │   └── DeleteConfirmationDialog.kt    # Dialog xác nhận xóa
│   ├── viewmodel/
│   │   ├── AppointmentViewModel.kt        # ViewModel quản lý state
│   │   └── AppointmentViewModelFactory.kt # Factory để tạo ViewModel
│   └── theme/
│       ├── Color.kt
│       ├── Theme.kt
│       └── Type.kt
└── MainActivity.kt                        # Activity chính, khởi tạo database & notification worker
```

### MVVM Pattern

**Model (Data Layer)**
- `Appointment`: Entity chứa dữ liệu lịch hẹn
- `AppointmentDao`: Interface cho truy cập database
- `AppointmentRepository`: Pattern Repository tách biệt business logic

**View (UI Layer)**
- `AppointmentScreen`: Composable function hiển thị danh sách
- `AppointmentCard`: Composable component cho mỗi item
- Các Dialog: `AddAppointmentDialog`, `TimeRangeFilterDialog`, `DeleteConfirmationDialog`

**ViewModel**
- `AppointmentViewModel`: Quản lý state UI, xử lý business logic
- Sử dụng `StateFlow` để emit state changes
- Xử lý coroutines với `viewModelScope`

## Công Nghệ Sử Dụng (Technologies)

- **Kotlin**: Ngôn ngữ lập trình chính
- **Jetpack Compose**: UI framework hiện đại
- **Room Database**: Local SQLite database
- **Coil**: Image loading library từ URL
- **WorkManager**: Lập lịch background tasks cho notification
- **Coroutines**: Async programming
- **StateFlow**: Reactive state management
- **KSP**: Kotlin Symbol Processing (thay thế kapt)

## Hướng Dẫn Cài Đặt & Chạy (Setup & Run)

### Yêu Cầu (Requirements)
- Android Studio Koala (2024.1.1) hoặc mới hơn
- Android SDK 36
- Java 11 hoặc mới hơn
- Gradle 8.10.1

### Bước 1: Cài Đặt Java 11

**Trên macOS (nếu cần):**
```bash
# Cài đặt Java 11 bằng Homebrew
brew install openjdk@11

# Thiết lập JAVA_HOME
export JAVA_HOME=$(/usr/libexec/java_home -v 11)
export PATH=$JAVA_HOME/bin:$PATH

# Kiểm tra
java -version
```

**Trên Linux:**
```bash
sudo apt-get update
sudo apt-get install openjdk-11-jdk
```

**Trên Windows:**
Tải từ https://adoptium.net/ và cài đặt

### Bước 2: Mở Project trong Android Studio

1. Mở Android Studio
2. File → Open → Chọn thư mục project
3. Chờ Gradle sync hoàn tất

### Bước 3: Build & Run

```bash
# Build project
./gradlew build

# Run trên emulator/device
./gradlew installDebug

# Hoặc nhấn Run trong Android Studio
```

## Hướng Dẫn Sử Dụng (Usage Guide)

### Thêm Lịch Hẹn Mới

1. Nhấn nút "Thêm Lịch Hẹn" (FAB button ở dưới bên phải)
2. Điền các thông tin:
   - **Tên lịch hẹn**: VD: "Họp với khách hàng"
   - **Mô tả**: VD: "Thảo luận dự án mới"
   - **Nơi chốn/Địa điểm**: VD: "Tòa nhà A, Phòng 301"
   - **Tên người hẹn**: VD: "Nguyễn Văn A"
   - **URL Hình đại diện**: VD: `https://example.com/avatar.jpg`
   - **Từ (ngày bắt đầu)**: VD: `2025-11-22 14:00`
   - **Đến (ngày kết thúc)**: VD: `2025-11-22 15:30`
3. Nhấn "Thêm"

**Format ngày giờ:** `yyyy-MM-dd HH:mm` (VD: 2025-11-22 14:30)

### Xóa Lịch Hẹn

1. Click vào lịch hẹn trong danh sách
2. Xác nhận xóa trong Dialog
3. Lịch hẹn sẽ bị xóa khỏi database

### Lọc Lịch Hẹn theo Thời Gian

1. Nhấn icon "Search" (🔍) ở thanh header
2. Nhập thời gian bắt đầu (Từ) và thời gian kết thúc (Đến)
3. Nhấn "Lọc"
4. Danh sách sẽ hiển thị chỉ các lịch hẹn trong khoảng thời gian được chọn
5. Nhấn "Hủy" để xem tất cả lịch hẹn lại

### Thông Báo Tự Động

- Ứng dụng sẽ kiểm tra mỗi 15 phút
- Khi lịch hẹn còn 30 phút nữa mới bắt đầu, bạn sẽ nhận được notification
- Notification chứa đầy đủ thông tin: tên lịch hẹn, người hẹn, **địa điểm**, và thời gian

## Cấu Trúc Database (Database Schema)

```sql
CREATE TABLE appointments (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    description TEXT NOT NULL,
    location TEXT NOT NULL,
    fromTime TEXT NOT NULL,
    toTime TEXT NOT NULL,
    personName TEXT NOT NULL,
    personAvatarUrl TEXT NOT NULL,
    notificationSent INTEGER DEFAULT 0,
    createdAt TEXT DEFAULT CURRENT_TIMESTAMP
)
```

## Các Màn Hình (Screenshots Description)

**1. Màn hình chính (Main Screen)**
- Header: "Quản lý Lịch Hẹn" + Search icon
- LazyColumn danh sách lịch hẹn dạng Card
- FAB button "Thêm Lịch Hẹn" ở góc dưới phải
- Empty state: "Không có lịch hẹn nào"

**2. Card lịch hẹn (Appointment Card)**
- Hình đại diện (tròn, 64dp)
- Tên lịch hẹn
- Tên người hẹn
- Mô tả (tối đa 1 dòng)
- **Địa điểm với LocationOn icon** 📍
- Thời gian: "Từ: 22/11/2025 14:00"
- Thời gian: "Đến: 22/11/2025 15:30"

**3. Dialog thêm lịch hẹn (Add Dialog)**
- TextFields: tên, mô tả, **nơi chốn**, tên người, URL avatar, từ, đến
- Validate: kiểm tra dữ liệu đầy đủ, định dạng ngày giờ
- Nút "Thêm" và "Hủy"

**4. Dialog lọc thời gian (Filter Dialog)**
- TextFields: Từ ngày giờ, Đến ngày giờ
- Validate: kiểm tra định dạng
- Nút "Lọc" và "Hủy"

**5. Dialog xác nhận xóa (Delete Dialog)**
- Tiêu đề: "Xác nhận xóa"
- Nội dung: "Bạn có chắc chắn muốn xóa lịch hẹn 'Tên lịch hẹn'?"
- Nút "Xóa" và "Hủy"

## State Management (MVVM)

### AppointmentViewModel States:
- `appointments`: Tất cả lịch hẹn từ database
- `filteredAppointments`: Lịch hẹn được lọc/hiển thị
- `isLoading`: Trạng thái loading
- `errorMessage`: Thông báo lỗi

### State Flow:
```kotlin
// Collect in Composable
val appointments by viewModel.filteredAppointments.collectAsState()

// Emit changes when data updates
repository.getAllAppointments().collect { appointments ->
    _appointments.value = appointments
}
```

## Thông Báo (Notifications)

### NotificationWorker
- Chạy mỗi 15 phút (periodic task)
- Kiểm tra các lịch hẹn chưa gửi notification
- Tính thời gian còn lại cho mỗi lịch hẹn
- Nếu trong 30 phút: gửi notification, đánh dấu notificationSent = true

### Permission
```xml
<uses-permission android:name="android.permission.POST_NOTIFICATIONS" />
```

## Manifest Permissions

```xml
<uses-permission android:name="android.permission.POST_NOTIFICATIONS" />
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
```

## Dependencies

```toml
# Room Database
androidx-room-runtime
androidx-room-ktx
androidx-room-compiler (KSP)

# Coil Image Loading
coil-compose

# WorkManager
androidx-work-runtime

# Compose & Material
androidx-ui
androidx-material3
androidx-lifecycle-viewmodel-compose
```

## Troubleshooting

### Lỗi: "Unresolved reference 'FilterList'"
**Giải pháp**: Sử dụng `Icons.Default.Search` thay vì `Icons.Default.FilterList`

### Lỗi: Compilation failed
**Giải pháp**: 
- Đảm bảo Java 11+ được cài đặt
- Chạy `./gradlew clean build`
- Đặt `JAVA_HOME` tới Java 11

### Database bị reset
- Ứng dụng sử dụng `fallbackToDestructiveMigration()` - database sẽ bị xóa khi thay đổi schema
- Để sửa: implement proper migrations hoặc thay đổi database version

### Hình đại diện không load
**Giải pháp**: 
- Kiểm tra URL hình ảnh có hợp lệ không
- Đảm bảo có permission `INTERNET` trong AndroidManifest.xml
- Kiểm tra kết nối internet

### Notification không hiển thị
**Giải pháp**:
- Cấp quyền `POST_NOTIFICATIONS` cho ứng dụng
- Kiểm tra notification setting trong device
- Đảm bảo device không ở Do Not Disturb mode

## Development Notes

### Thêm tính năng mới

1. **Thêm field mới vào Appointment:**
   - Update `Appointment.kt` entity
   - Update `AppointmentDao.kt` queries (nếu cần)
   - Tăng database version

2. **Thêm UI mới:**
   - Tạo Composable function mới
   - Update `AppointmentScreen.kt` để hiển thị
   - Cập nhật ViewModel nếu cần state mới

3. **Thêm business logic:**
   - Thêm method vào `AppointmentRepository`
   - Thêm method vào `AppointmentViewModel`
   - Call từ UI composable

### Best Practices

- Luôn sử dụng `viewModelScope` cho coroutines trong ViewModel
- Sử dụng `StateFlow` thay vì `LiveData` trong Compose
- Tách biệt UI logic khỏi business logic (MVVM)
- Validate input trước khi lưu vào database
- Sử dụng Repository pattern để truy cập data

## License

Educational Project - Bài Tập Cá Nhân Tuần 7

## Support

Nếu gặp vấn đề hoặc có câu hỏi, vui lòng kiểm tra:
1. AndroidManifest.xml có permissions đầy đủ không
2. Java version là 11+
3. Gradle sync thành công không
4. Device/Emulator API level >= 29
