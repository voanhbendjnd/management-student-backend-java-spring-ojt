import { useEffect, useState } from 'react';
import StudentPage from './components/admin/student/student.table';
import LoginPage from './components/auth/login';

function App() {
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    // Check if token exists in localStorage
    const token = localStorage.getItem('access_token');
    setIsLoggedIn(!!token);
    setLoading(false);
  }, []);

  if (loading) {
    return <div style={{ textAlign: 'center', padding: '50px' }}>Loading...</div>;
  }

  return isLoggedIn ? (
    <StudentPage />
  ) : (
    <LoginPage onLoginSuccess={() => setIsLoggedIn(true)} />
  );
}

export default App
