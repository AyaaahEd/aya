"use client";

import { useEffect, useState } from "react";
import { fetchProductById, createOrder } from "../../../lib/api";
import { useRouter } from "next/navigation";
import { Button } from "@repo/ui/button";
import { useAuth } from "../../../lib/auth-context";

export default function ProductPage({ params }: { params: { id: string } }) {
    const [product, setProduct] = useState<any>(null);
    const [loading, setLoading] = useState(true);
    const [quantity, setQuantity] = useState(1);
    const [quality, setQuality] = useState("High");
    const [hasBorder, setHasBorder] = useState(false);


    const { user } = useAuth();
    const router = useRouter();

    useEffect(() => {
        fetchProductById(params.id)
            .then((data) => {
                setProduct(data);
                setLoading(false);
            })
            .catch((err) => {
                console.error(err);
                setLoading(false);
            });
    }, [params.id]);

    const handleOrder = async () => {
        try {
            if (!user) {
                alert("Please login to place an order.");
                router.push("/login");
                return;
            }

            const orderPayload = {
                userId: user.id,
                items: [
                    {
                        productId: product.id,
                        quantity: quantity,
                        price: product.price,
                        quality: quality,
                        hasBorder: hasBorder
                    }
                ],
                totalAmount: (product.price * quantity)
            };

            await createOrder(orderPayload);
            alert("Order placed successfully!");
            router.push("/");
        } catch (e) {
            console.error(e);
            alert("Failed to place order.");
        }
    };

    if (loading) return <div className="p-24">Loading...</div>;
    if (!product) return <div className="p-24">Product not found.</div>;

    return (
        <main className="flex min-h-screen flex-col items-center p-24">
            <div className="max-w-4xl w-full bg-white p-8 rounded shadow-lg flex gap-8">
                <div className="w-1/2 bg-gray-200 h-96 rounded flex items-center justify-center">
                    <span className="text-gray-500">Product Image</span>
                </div>
                <div className="w-1/2 space-y-6">
                    <h1 className="text-3xl font-bold">{product.name}</h1>
                    <p className="text-gray-600">{product.description}</p>
                    <div className="text-2xl font-bold text-green-600">${product.price}</div>

                    <div className="space-y-4 border-t pt-4">
                        <div>
                            <label className="block text-sm font-medium mb-1">Quantity</label>
                            <input
                                type="number"
                                min="1"
                                value={quantity}
                                onChange={(e) => setQuantity(parseInt(e.target.value))}
                                className="border p-2 rounded w-24"
                            />
                        </div>

                        {product.isCustomizable && (
                            <>
                                <div>
                                    <label className="block text-sm font-medium mb-1">Print Quality</label>
                                    <select
                                        value={quality}
                                        onChange={(e) => setQuality(e.target.value)}
                                        className="border p-2 rounded w-full"
                                    >
                                        <option value="Standard">Standard</option>
                                        <option value="High">High</option>
                                        <option value="Premium">Premium</option>
                                    </select>
                                </div>

                                <div className="flex items-center gap-2">
                                    <input
                                        type="checkbox"
                                        id="border"
                                        checked={hasBorder}
                                        onChange={(e) => setHasBorder(e.target.checked)}
                                    />
                                    <label htmlFor="border">Add White Border?</label>
                                </div>
                            </>
                        )}
                    </div>

                    <Button appName="storefront" className="w-full bg-blue-600 text-white py-3 rounded mt-4" onClick={handleOrder}>
                        Place Order
                    </Button>
                </div>
            </div>
        </main>
    );
}
