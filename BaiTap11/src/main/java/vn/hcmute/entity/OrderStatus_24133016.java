package vn.hcmute.entity;

public enum OrderStatus_24133016 {
    NEW("Đơn hàng mới", "primary"),
    CONFIRMED("Đã xác nhận", "info"),
    PREPARING("Chuẩn bị hàng", "warning"),
    SHIPPING("Vận chuyển", "secondary"),
    DELIVERING("Giao hàng", "dark"),
    DELIVERED("Đã giao", "success"),
    CANCELLED("Đơn hàng hủy", "danger"),
    RETURNED("Đơn hàng hoàn", "danger");

    private final String label;
    private final String badgeClass;

    OrderStatus_24133016(String label, String badgeClass) {
        this.label = label;
        this.badgeClass = badgeClass;
    }

    public String getLabel() {
        return label;
    }

    public String getBadgeClass() {
        return badgeClass;
    }
}
