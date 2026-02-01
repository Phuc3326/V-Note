package util;

import java.sql.Date;
import java.text.SimpleDateFormat;

public class DateConverter {
    public static String formatDate(Date sqlDate) {
        if (sqlDate == null) {
            return ""; // Tránh lỗi NullPointerException
        }
        
        // 1. Tạo đối tượng định dạng với mẫu mong muốn
        // Lưu ý: MM là tháng (Month), mm là phút (minute). Phải dùng MM viết hoa.
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        
        // 2. Sử dụng hàm format() để chuyển đổi
        return sdf.format(sqlDate);
    }
}