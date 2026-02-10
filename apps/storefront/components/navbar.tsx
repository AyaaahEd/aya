"use client";

import { useAuth } from "../lib/auth-context";
import { Button } from "@repo/ui/button";
import Link from "next/link";

export function NavBar() {
    const { user, logout } = useAuth();

    return (
        <div className="w-full max-w-5xl items-center justify-between font-mono text-sm lg:flex mb-10 mt-10">
            <h1 className="text-4xl font-bold">
                <Link href="/">POD Store</Link>
            </h1>
            <div className="flex gap-4 items-center">
                {user ? (
                    <>
                        <span className="text-gray-700">Hi, {user.username}</span>
                        <Link href="/orders" className="text-gray-600 hover:text-black hover:underline mr-4">
                            My Orders
                        </Link>
                        <Link href="/admin" className="text-gray-600 hover:text-black hover:underline mr-4">
                            Admin
                        </Link>
                        <Button appName="storefront" onClick={logout} className="bg-red-500 text-white px-4 py-2 rounded">
                            Logout
                        </Button>
                    </>
                ) : (
                    <>
                        <a href="/register" className="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700 transition">
                            Register
                        </a>
                        <Link href="/login" className="bg-gray-200 text-black px-4 py-2 rounded hover:bg-gray-300 transition">
                            Login
                        </Link>
                    </>
                )}
            </div>
        </div>
    );
}
