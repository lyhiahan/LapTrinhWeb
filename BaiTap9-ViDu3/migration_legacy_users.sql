-- Script di chuyển dữ liệu cũ: Chỉ chạy MỘT LẦN khi triển khai hệ thống mới lên CSDL cũ
-- Mục đích: Đánh dấu các tài khoản cũ đã hoạt động trước thời điểm áp dụng OTP thành email_verified = 1
-- Tránh chạy tự động khi khởi động ứng dụng để không vô tình kích hoạt các tài khoản mới đăng ký đang chờ OTP.

UPDATE users 
SET email_verified = 1 
WHERE enabled = 1 
  AND (email_verified IS NULL OR email_verified = 0);
