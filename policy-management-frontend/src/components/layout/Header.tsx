import React from 'react';
import { Link } from 'react-router-dom';

export const Header: React.FC = () => (
    <header className="bg-gray-800 text-white p-4 flex justify-between items-center">
        <h1 className="text-xl font-bold">Policy Manager</h1>
        <nav className="space-x-4">
            <Link to="/" className="hover:underline">Dashboard</Link>
            <Link to="/policies" className="hover:underline">Policies</Link>
        </nav>
    </header>
);
