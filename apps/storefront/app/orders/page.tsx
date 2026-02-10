"use client";

import { useEffect, useState } from "react";
import { useAuth } from "../../lib/auth-context";
import { fetchOrders } from "../../lib/api";
import { useRouter } from "next/navigation";

export default function OrderHistoryPage() {
    const { user } = useAuth();
    const [orders, setOrders] = useState<any[]>([]);
    const [loading, setLoading] = useState(true);
    const router = useRouter();

    useEffect(() => {
        if (!user) {
            router.push("/login");
            return;
        }

        fetchOrders(user.id)
            .then((data) => {
                setOrders(data);
                setLoading(false);
            })
            .catch((err) => {
                console.error(err);
                setLoading(false);
            });
    }, [user, router]);

    if (!user) return null;
    if (loading) return <div className="p-24 text-center">Loading orders...</div>;

    return (
        <main className="flex min-h-screen flex-col items-center p-24 bg-gray-50">
            <div className="w-full max-w-5xl">
                <h1 className="text-3xl font-bold mb-8">My Orders</h1>

                {orders.length === 0 ? (
                    <div className="bg-white p-8 rounded shadow text-center text-gray-500">
                        You haven't placed any orders yet.
                    </div>
                ) : (
                    <div className="space-y-4">
                        {orders.map((order) => (
                            <div key={order.id} className="bg-white p-6 rounded shadow border-l-4 border-blue-500">
                                <div className="flex justify-between items-start mb-4">
                                    <div>
                                        <span className="text-sm text-gray-500">Order ID: {order.id}</span>
                                        <div className="text-sm text-gray-500">{new Date(order.createdAt).toLocaleString()}</div>
                                    </div>
                                    <div className="text-right">
                                        <span className={`px-2 py-1 rounded text-sm font-bold ${order.status === 'PENDING' ? 'bg-yellow-100 text-yellow-800' :
                                                order.status === 'COMPLETED' ? 'bg-green-100 text-green-800' : 'bg-gray-100 text-gray-800'
                                            }`}>
                                            {order.status}
                                        </span>
                                        <div className="font-bold text-lg mt-1">${order.totalAmount}</div>
                                    </div>
                                </div>

                                <div className="border-t pt-4">
                                    <h3 className="text-sm font-semibold mb-2">Items:</h3>
                                    <ul className="space-y-2">
                                        {order.items.map((item: any, idx: number) => (
                                            <li key={idx} className="flex justify-between text-sm">
                                                <span>Product ID: {item.productId} (x{item.quantity}) - {item.quality} {item.hasBorder ? '+ Border' : ''}</span>
                                                <span>${item.price}</span>
                                            </li>
                                        ))}
                                    </ul>
                                </div>
                            </div>
                        ))}
                    </div>
                )}
            </div>
        </main>
    );
}
