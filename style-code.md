** Dự án quản lý sinh viên với mục tiêu tối ưu hiệu năng khi nhiều sinh viên đăng kí môn học đồng thời
** Kiến trúc dự án dụng là Domain Driven Design (DDD)

** Tên hàm tên phương thức đều phải là tiếng anh
** Sao khi code nếu được thì comment giải thích cái phương thức đó làm gì ở trên hàm, có thể comment tiếng việt, anh,...
** Đặt tên phương thức phải theo kiểu camel
** Các dữ liệu nhạy cảm như thông tin tài khoản có các thuộc tính như user response trả ra phải thông qua 1 cái DTO thay vì entity
** Nếu tối ưu được code thì hãy tối ưu
** Bắt lỗi bằng cách throw ra không được bắt bằng cách khác
** Dùng throw custom, không dùng của hệ thống
** Nếu sử dụng AI sinh code thì không được refactor toàn bộ code, chỉ sinh code thay đổi, không làm ảnh hưởng các code không liên quan -> Response AI trả ra luôn là 1 hoặc nhiều phương thức được sửa không được trả ra toàn bộ code


