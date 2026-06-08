import { Button, Form, Input, message } from "antd";
import { useState } from "react";
import { authApi } from "../../services/auth.api";

interface LoginProps {
    onLoginSuccess: () => void;
}

const LoginPage = ({ onLoginSuccess }: LoginProps) => {
    const [form] = Form.useForm();
    const [loading, setLoading] = useState(false);

    const normalizeLoginPayload = (response: any) => {
        if (!response) return undefined;

        if (response.accessToken || response.refreshToken || response.user) {
            return response;
        }

        if (response.data) {
            return normalizeLoginPayload(response.data);
        }

        if (response.data?.data) {
            return normalizeLoginPayload(response.data.data);
        }

        return undefined;
    };

    const handleLogin = async (values: { email: string; password: string }) => {
        setLoading(true);
        try {
            const res = await authApi.login(values.email, values.password);
            console.debug("Login response:", res);

            const payload = normalizeLoginPayload(res);
            console.debug("Normalized login payload:", payload);

            const accessToken = payload?.accessToken;
            const refreshToken = payload?.refreshToken;
            const user = payload?.user;
            const statusOk = (res as any)?.status === 200 || (res as any)?.statusCode === 200;

            if (accessToken) {
                localStorage.setItem("access_token", accessToken);
            }
            if (refreshToken) {
                localStorage.setItem("refresh_token", refreshToken);
            }
            if (user) {
                localStorage.setItem("user_data", JSON.stringify(user));
            }

            if (accessToken || statusOk) {
                message.success("Đăng nhập thành công!");
                onLoginSuccess();
            } else {
                message.error("Đăng nhập thất bại: Token không hợp lệ");
            }
        } catch (error: any) {
            console.error("Lỗi đăng nhập:", error);
            const errorMsg =
                error?.response?.data?.message ||
                error?.response?.data?.error ||
                error?.message ||
                "Lỗi đăng nhập";
            message.error(errorMsg);
        } finally {
            setLoading(false);
        }
    };

    return (
        <div
            style={{
                display: "flex",
                justifyContent: "center",
                alignItems: "center",
                minHeight: "100vh",
                backgroundColor: "#f0f2f5",
            }}
        >
            <div
                style={{
                    width: "100%",
                    maxWidth: "400px",
                    padding: "24px",
                    backgroundColor: "white",
                    borderRadius: "8px",
                    boxShadow: "0 2px 8px rgba(0,0,0,0.1)",
                }}
            >
                <h1 style={{ textAlign: "center", marginBottom: "24px", color: "#000" }}>
                    Đăng Nhập
                </h1>
                <Form form={form} layout="vertical" onFinish={handleLogin}>
                    <Form.Item
                        label="Email"
                        name="email"
                        rules={[
                            { required: true, message: "Vui lòng nhập email" },
                            { type: "email", message: "Email không hợp lệ" },
                        ]}
                    >
                        <Input placeholder="Nhập email" />
                    </Form.Item>

                    <Form.Item
                        label="Mật khẩu"
                        name="password"
                        rules={[{ required: true, message: "Vui lòng nhập mật khẩu" }]}
                    >
                        <Input.Password placeholder="Nhập mật khẩu" />
                    </Form.Item>

                    <Form.Item>
                        <Button
                            type="primary"
                            htmlType="submit"
                            block
                            loading={loading}
                            size="large"
                        >
                            Đăng Nhập
                        </Button>
                    </Form.Item>
                </Form>
            </div>
        </div>
    );
};

export default LoginPage;
