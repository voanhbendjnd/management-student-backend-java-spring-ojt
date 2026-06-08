import { DeleteOutlined, EditOutlined, PlusOutlined, LogoutOutlined } from "@ant-design/icons";
import { Button, Table, message, Popconfirm, Space, Modal, Form, Input } from "antd";
import { useEffect, useState } from "react";
import { studentApi } from "../../../services/student.api";

interface Student {
    id?: number;
    userId: number;
    studentCode: string;
    name: string;
    email: string;
    major: string;
    countCredits: number;
}

const StudentPage = () => {
    const [students, setStudents] = useState<Student[]>([]);
    const [loading, setLoading] = useState(false);
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [editingStudent, setEditingStudent] = useState<Student | null>(null);
    const [form] = Form.useForm();

    async function fetchStudents() {
        setLoading(true);
        try {
            const res = await studentApi.getAll();
            console.log("API response:", res);

            const items: any = Array.isArray(res)
                ? res
                : Array.isArray(res?.data)
                ? res.data
                : [];

            const mapped: Student[] = items.map((b: any) => ({
                id: b.id ?? b.userId ?? b.user_id ?? 0,
                userId: b.userId ?? b.user_id ?? b.id ?? 0,
                studentCode: b.studentCode ?? b.code ?? b.student_code ?? "",
                name: b.studentName ?? b.student_name ?? b.name ?? b.fullName ?? b.full_name ?? "",
                email: b.email ?? b.user?.email ?? b.user_email ?? "",
                major: b.major ?? b.majorName ?? b.major_name ?? "",
                countCredits: b.countCredits ?? b.credits ?? b.count_credits ?? 0,
            }));

            console.log("Mapped students:", mapped);
            setStudents(mapped);
        } catch (error) {
            console.error("Lỗi lấy danh sách sinh viên:", error);
            message.error("Lỗi tải dữ liệu");
        } finally {
            setLoading(false);
        }
    }

    // Đã có thể gọi fetchStudents thoải mái mà không sợ bị lỗi "Cannot access before initialization"
    // Thêm đuôi .catch() để xử lý triệt để cảnh báo "Promise returned is ignored" của ESLint
    useEffect(() => {
         void fetchStudents().catch((err) => console.error("Lỗi useEffect:", err));
    }, []);

    const openAddModal = () => {
        setEditingStudent(null);
        form.resetFields();
        setIsModalOpen(true);
    };

    const openEditModal = (record: Student) => {
        setEditingStudent(record);
        form.setFieldsValue({
            name: record.name,
            email: record.email,
            major: record.major,
            studentCode: record.studentCode,
            countCredits: record.countCredits,
        });
        setIsModalOpen(true);
    };

    const closeAddModal = () => {
        setEditingStudent(null);
        setIsModalOpen(false);
    };

    const onAddFinish = async (values: any) => {
        try {
            if (editingStudent) {
                const studentId = editingStudent.id ?? editingStudent.userId;
                await studentApi.update(studentId, {
                    name: values.name,
                    email: values.email,
                    major: values.major,
                });
                message.success("Cập nhật sinh viên thành công");
            } else {
                await studentApi.create(values);
                message.success("Thêm sinh viên thành công");
            }
            closeAddModal();
            await fetchStudents();
        } catch (error) {
            console.error("Lỗi thêm/sửa sinh viên:", error);
            message.error(editingStudent ? "Cập nhật sinh viên thất bại" : "Thêm sinh viên thất bại");
        }
    };

    const handleDelete = async (record: Student) => {
        const deleteId = record.id ?? record.userId;
        if (!deleteId || deleteId === 0) {
            console.error("Không thể xóa: ID sinh viên không hợp lệ", record);
            message.error("Không thể xóa: ID sinh viên không hợp lệ");
            return;
        }

        try {
            console.log("Đang thực hiện xóa sinh viên có ID:", deleteId, "(record)", record);

            await studentApi.delete(deleteId);
            message.success("Xóa sinh viên thành công");
            await fetchStudents();
        } catch (error) {
            console.error("Lỗi khi xóa sinh viên:", error);
            message.error("Xóa sinh viên thất bại");
        }
    };

    const columns = [
        {
            title: "Mã SV",
            dataIndex: "studentCode",
            key: "studentCode",
        },
        {
            title: "Tên",
            dataIndex: "name",
            key: "name",
        },
        {
            title: "Email",
            dataIndex: "email",
            key: "email",
        },
        {
            title: "Chuyên ngành",
            dataIndex: "major",
            key: "major",
        },
        {
            title: "Số tín chỉ",
            dataIndex: "countCredits",
            key: "countCredits",
        },
        {
            title: "Thao tác",
            key: "action",
            render: (_: unknown, record: Student) => (
                <Space>
                    <Button
                        type="primary"
                        icon={<EditOutlined />}
                        size="small"
                        onClick={() => openEditModal(record)}
                    >
                        Sửa
                    </Button>
                    <Popconfirm
                        title="Xác nhận?"
                        description="Bạn có chắc muốn xóa không?"
                        onConfirm={() => handleDelete(record)}
                    >
                        <Button
                            type="primary"
                            danger
                            icon={<DeleteOutlined />}
                            size="small"
                        >
                            Xóa
                        </Button>
                    </Popconfirm>
                </Space>
            ),
        },
    ];

    return (
        <div style={{ padding: "20px" }}>
            <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: "20px" }}>
                <Button
                    type="primary"
                    icon={<PlusOutlined />}
                    onClick={openAddModal}
                >
                    Thêm sinh viên
                </Button>
                <Button
                    danger
                    icon={<LogoutOutlined />}
                    onClick={() => {
                        localStorage.removeItem('access_token');
                        localStorage.removeItem('refresh_token');
                        localStorage.removeItem('user_data');
                        window.location.reload();
                    }}
                >
                    Đăng xuất
                </Button>
            </div>
            <Modal
                title={editingStudent ? "Sửa sinh viên" : "Thêm sinh viên"}
                open={isModalOpen}
                onCancel={closeAddModal}
                footer={null}
                destroyOnClose
            >
                <Form form={form} layout="vertical" onFinish={onAddFinish}>
                    <Form.Item name="name" label="Tên" rules={[{ required: true, message: "Vui lòng nhập tên" }]}>
                        <Input placeholder="Nhập tên sinh viên" />
                    </Form.Item>

                    <Form.Item name="email" label="Email" rules={[{ required: true, type: 'email', message: "Vui lòng nhập email hợp lệ" }]}>
                        <Input placeholder="Nhập email" />
                    </Form.Item>

                    <Form.Item name="major" label="Chuyên ngành" rules={[{ required: true, message: "Vui lòng nhập chuyên ngành" }]}>
                        <Input placeholder="Nhập chuyên ngành" />
                    </Form.Item>

                    <Form.Item>
                        <Space style={{ display: 'flex', justifyContent: 'flex-end' }}>
                            <Button onClick={closeAddModal}>Hủy</Button>
                            <Button type="primary" htmlType="submit">
                                {editingStudent ? "Cập nhật" : "Thêm"}
                            </Button>
                        </Space>
                    </Form.Item>
                </Form>
            </Modal>
            <Table
                columns={columns}
                dataSource={students}
                loading={loading}
                rowKey={(record) => record.id ?? record.userId}
                bordered
                pagination={{ pageSize: 10 }}
            />
        </div>
    );
};

export default StudentPage;