import React, { useEffect } from 'react';
import { usePolicy } from '../hooks/usePolicy';
import { LoadingSpinner } from '../components/common/LoadingSpinner';
import { Link } from 'react-router-dom';

export const Dashboard: React.FC = () => {
    const { policies, loading, fetchAllPolicies } = usePolicy();

    useEffect(() => {
        fetchAllPolicies();
    }, [fetchAllPolicies]);

    if (loading) return <LoadingSpinner />;

    // ✅ Add safety checks before using reduce
    const safePolicies = policies || []; // Handle undefined/null
    const totalPremium = safePolicies.reduce((s, p) => s + (p.premiumAmount || 0), 0);

    return (
        <div>
            <h2 className="text-2xl font-bold mb-6">Dashboard</h2>
            <div className="grid grid-cols-2 md:grid-cols-4 gap-4 my-6">
                <Stat title="Total Policies" value={safePolicies.length} />
                <Stat title="Active" value={safePolicies.filter(p => p.status === 'ACTIVE').length} />
                <Stat title="Members" value={safePolicies.reduce((s, p) => s + ((p.members && p.members.length) || 0), 0)} />
                <Stat title="Premium ($)" value={totalPremium.toLocaleString()} />
            </div>
            <Link to="/policies/create"
                className="bg-blue-600 text-white px-4 py-2 rounded">Create Policy</Link>

            {/* Recent Policies Table */}
            {safePolicies.length > 0 && (
                <div className="mt-8 bg-white p-6 rounded-lg shadow-md">
                    <h3 className="text-xl font-semibold mb-4">Recent Policies</h3>
                    <table className="min-w-full">
                        <thead>
                            <tr className="border-b">
                                <th className="text-left py-2">Policy Number</th>
                                <th className="text-left py-2">Type</th>
                                <th className="text-left py-2">Status</th>
                                <th className="text-left py-2">Premium</th>
                                <th className="text-left py-2">Members</th>
                                <th className="text-left py-2">Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            {safePolicies.slice(0, 5).map((policy) => (
                                <tr key={policy.id} className="border-b">
                                    <td className="py-2">{policy.policyNumber}</td>
                                    <td className="py-2">{policy.policyType}</td>
                                    <td className="py-2">
                                        <span className={`px-2 py-1 rounded-full text-xs ${policy.status === 'ACTIVE'
                                                ? 'bg-green-100 text-green-800'
                                                : 'bg-gray-100 text-gray-800'
                                            }`}>
                                            {policy.status}
                                        </span>
                                    </td>
                                    <td className="py-2">${(policy.premiumAmount || 0).toLocaleString()}</td>
                                    <td className="py-2">{(policy.members && policy.members.length) || 0}</td>
                                    <td className="py-2">
                                        <Link
                                            to={`/policies/${policy.policyNumber}`}
                                            className="text-blue-600 hover:text-blue-800"
                                        >
                                            View Details
                                        </Link>
                                    </td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
                </div>
            )}

            {safePolicies.length === 0 && !loading && (
                <div className="mt-8 text-center text-gray-500">
                    <p>No policies found. Create your first policy!</p>
                </div>
            )}
        </div>
    );
};

const Stat = ({ title, value }: { title: string; value: number | string }) => (
    <div className="bg-white p-4 rounded shadow text-center">
        <p className="text-gray-600">{title}</p>
        <p className="text-2xl font-semibold">{value}</p>
    </div>
);
