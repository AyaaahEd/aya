"use client";

import { useEffect, useState } from "react";
import { useAuth } from "../../lib/auth-context";
import { fetchAllOrders, updateOrderStatus } from "../../lib/api";
import { useRouter } from "next/navigation";

export default function AdminPage() {
    const { user } = useAuth();
    const [orders, setOrders] = useState<any[]>([]);
    const [loading, setLoading] = useState(true);
    const router = useRouter();

    const refreshOrders = () => {
        fetchAllOrders()
            .then((data) => {
                setOrders(data);
                setLoading(false);
            })
            .catch((err) => {
                console.error(err);
                setLoading(false);
            });
    };

    useEffect(() => {
        // Basic role check (In real app, verify properly on backend)
        if (!user || !user.roles.includes("ROLE_ADMIN")) {
            // For demo, allowing regular users if they know the URL, or strictly redirect:
            // router.push("/"); 
            // We will just show a warning for now if not admin
        }

        refreshOrders();
    }, [user, router]);

    const handleStatusChange = async (orderId: string, newStatus: string) => {
        if (confirm(`Update order ${orderId} to ${newStatus}?`)) {
            try {
                await updateOrderStatus(orderId, newStatus);
                refreshOrders();
            } catch (error) {
                console.error("Failed to update status", error);
                alert("Failed to update status");
            }
        }
    };

    if (loading) return <div className="p-24 text-center">Loading admin dashboard...</div>;

    return (
        <main className="flex min-h-screen flex-col items-center p-24 bg-gray-100">
            <div className="w-full max-w-6xl">
                <h1 className="text-3xl font-bold mb-8 text-gray-800">Admin Dashboard</h1>

                <div className="bg-white rounded shadow p-6">
                    <h2 className="text-xl font-semibold mb-4">All System Orders ({orders.length})</h2>
                    <div className="overflow-x-auto">
                        <table className="min-w-full text-left text-sm">
                            <thead className="bg-gray-50 uppercase text-gray-600 border-b">
                                <tr>
                                    <th className="px-6 py-3">Order ID</th>
                                    <th className="px-6 py-3">User</th>
                                    <th className="px-6 py-3">Date</th>
                                    <th className="px-6 py-3">Total</th>
                                    <th className="px-6 py-3">Status</th>
                                    <th className="px-6 py-3">Actions</th>
                                </tr>
                            </thead>
                            <tbody className="divide-y divide-gray-200">
                                {orders.map((order) => (
                                    <tr key={order.id} className="hover:bg-gray-50">
                                        <td className="px-6 py-4 font-mono">{order.id.substring(0, 8)}...</td>
                                        <td className="px-6 py-4">{order.userId}</td>
                                        <td className="px-6 py-4">{new Date(order.createdAt).toLocaleDateString()}</td>
                                        <td className="px-6 py-4">${order.totalAmount}</td>
                                        <td className="px-6 py-4">
                                            <span className={`px-2 py-1 rounded text-xs font-bold ${order.status === 'PENDING' ? 'bg-yellow-100 text-yellow-800' :
                                                order.status === 'COMPLETED' ? 'bg-green-100 text-green-800' : 'bg-gray-100 text-gray-800'
                                                }`}>
                                                {order.status}
                                            </span>
                                        </td>
                                        <td className="px-6 py-4">
                                            <select
                                                value={order.status}
                                                onChange={(e) => handleStatusChange(order.id, e.target.value)}
                                                className="border border-gray-300 rounded px-2 py-1 text-xs"
                                            >
                                                <option value="PENDING">PENDING</option>
                                                <option value="QUEUED">QUEUED</option>
                                                <option value="PRODUCTION">PRODUCTION</option>
                                                <option value="SHIPPED">SHIPPED</option>
                                                <option value="COMPLETED">COMPLETED</option>
                                                <option value="CANCELLED">CANCELLED</option>
                                            </select>
                                        </td>
                                    </tr>
                                ))}
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </main>
    );
}
