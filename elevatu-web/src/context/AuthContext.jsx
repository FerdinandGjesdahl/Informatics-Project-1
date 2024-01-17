import {createContext, useCallback, useContext, useMemo, useState} from 'react';

export const AuthContext = createContext({});

export const AuthProvider = ({ children }) => {
    const [userSession, setUserSession] = useState(null);
    const isLoggedIn = useMemo(() => userSession !== null, [userSession]);

    const handleLogin = useCallback((session) => {
        setUserSession(session);
    }, []);
    const handleLogout = useCallback(() => {
        setUserSession(null);
    }, []);


    return (
        <AuthContext.Provider value={{
            userSession,
            setUserSession,
            isLoggedIn,
        }}>
            {children}
        </AuthContext.Provider>
    );
}

export const useAuth = () => {
    const context = useContext(AuthContext);
    if (!context) {
        throw new Error(`useAuth must be in a AuthProvider`);
    }

    return context;
};
