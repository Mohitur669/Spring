import React, { useEffect } from 'react';
import { Link } from 'react-router-dom';
import { usePolicy } from '../hooks/usePolicy';
import { LoadingSpinner } from '../components/common/LoadingSpinner';

export const PolicyList: React.FC = () => {
    const { policies, loading, fetchAllPolicies } = usePolicy();

    useEffect(() => { fetchAllPolicies(); }, []);

    if (loading) return <LoadingSpinner />;

    return (
        <div>
            <h2 className="text-2xl font-bold mb-4">Policies</h2>
            <table className="min-w-full bg-white rounded shadow">
                <thead><tr>
                    {['Number', 'Type', 'Status', 'Premium', 'Members', ''].map(h => (
                        <th key={h} className="text-left px-4 py-2 border-b">{h}</th>
                    ))}
                </tr></thead>
                <tbody>
                    {policies.map(p => (
                        <tr key={p.id} className="border-b hover:bg-gray-50">
                            <td className="px-4 py-2">{p.policyNumber}</td>
                            <td className="px-4 py-2">{p.policyType}</td>
                            <td className="px-4 py-2">{p.status}</td>
                            <td className="px-4 py-2">${p.premiumAmount}</td>
                            <td className="px-4 py-2">{p.members.length}</td>
                            <td className="px-4 py-2">
                                <Link to={`/policies/${p.policyNumber}`}
                                    className="text-blue-600 hover:underline">Details</Link>
                            </td>
                        </tr>
                    ))}
                </tbody>
            </table>
        </div>
    );
};
