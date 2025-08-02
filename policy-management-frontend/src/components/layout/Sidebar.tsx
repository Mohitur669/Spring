import React from 'react';
import { Link, useLocation } from 'react-router-dom';

const linkCls = (active: boolean) =>
    `block px-4 py-2 rounded ${active ? 'bg-blue-600 text-white' : 'hover:bg-gray-100'}`;

export const Sidebar: React.FC = () => {
    const { pathname } = useLocation();
    return (
        <aside className="w-56 border-r h-full p-4 space-y-2">
            <Link to="/" className={linkCls(pathname === '/')}>Dashboard</Link>
            <Link to="/policies" className={linkCls(pathname.startsWith('/policies'))}>
                Policies
            </Link>
        </aside>
    );
};
