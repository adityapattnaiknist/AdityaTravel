import React from 'react';

const AuthContext = React.createContext(null);

export function AuthProvider({ children }) {
  const [token, setToken] = React.useState(() => localStorage.getItem('token') || '');
  const [user, setUser] = React.useState(() => {
    const raw = localStorage.getItem('user');
    return raw ? JSON.parse(raw) : null;
  });

  const login = (nextToken, nextUser) => {
    setToken(nextToken || '');
    setUser(nextUser || null);
    if (nextToken) localStorage.setItem('token', nextToken); else localStorage.removeItem('token');
    if (nextUser) localStorage.setItem('user', JSON.stringify(nextUser)); else localStorage.removeItem('user');
  };

  const logout = () => login('', null);

  const value = React.useMemo(() => ({ token, user, login, logout }), [token, user]);
  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

export function useAuth() {
  return React.useContext(AuthContext);
}

