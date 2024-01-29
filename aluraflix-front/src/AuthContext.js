import React, { createContext, useContext, useState, useEffect } from 'react';

const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
    const [token, setToken] = useState(null);
  
    useEffect(() => {
      // Get the token from the query parameter
      const urlParams = new URLSearchParams(window.location.search);
      const pathToken = urlParams.get('token');
  
      if (pathToken) {
        setToken(pathToken);
      }
    }, []); // Run this effect only once when the component mounts
  
    const setAuthToken = (newToken) => {
      setToken(newToken);
    };
  
    return (
      <AuthContext.Provider value={{ token, setAuthToken }}>
        {children}
      </AuthContext.Provider>
    );
  };

export const useAuth = () => {
  return useContext(AuthContext);
};
